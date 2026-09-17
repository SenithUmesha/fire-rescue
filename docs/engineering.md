# Fire Rescue — engineering notes

Fire Rescue is a useful snapshot of how I was building Android apps in **2022**: Java, XML layouts, activity-heavy navigation, Firebase for remotely changing data, SQLite for local data, and direct use of platform APIs for maps, location, camera, notifications, media and web content.

This document is not meant to present the project as modern production architecture. It is here to explain what the app actually does, how the pieces interact, what aged well, and what I would redesign now.

---

## 1. Product shape

The app grew around a dashboard of firefighter-oriented utilities rather than one tightly scoped workflow.

The main screen links into modules for:

- fire-station discovery
- firefighter/contact information
- alerts
- events
- careers
- live dispatch audio
- QR scanning
- witness statements
- commendations / feedback
- fire-safety tips
- news
- social/contact pages
- settings

A bottom navigation bar exposes alerts, news, events and social content, while the central dashboard cards open the larger utility screens.

That makes the project closer to a small **portal app** than a single-purpose utility.

---

## 2. Architecture at a glance

The project predates the architecture I would use today. Most feature orchestration lives directly inside `Activity` and `Fragment` classes.

```text
┌──────────────────────────────────────────────────┐
│                Android UI layer                  │
│                                                  │
│ Activities + Fragments + XML + RecyclerViews     │
└──────────────────────┬───────────────────────────┘
                       │
             direct service/data access
                       │
      ┌────────────────┼─────────────────────┐
      │                │                     │
      ▼                ▼                     ▼
  SQLite          Firebase RTDB       Android platform APIs
      │                │                     │
 reference data     events              location / camera
 alert history      careers             notifications
 scan history       dispatch config     MediaPlayer / WebView
 contacts/tips                         intents / permissions
      │
      └──────────────────────┐
                             ▼
                         RecyclerViews
```

There is no formal domain layer, repository abstraction or ViewModel state holder across most of the app. The screen class often owns:

1. view lookup,
2. permission handling,
3. data access,
4. network calls,
5. navigation,
6. error feedback,
7. lifecycle behavior.

That coupling is the biggest architectural limitation in the codebase, but it also makes the historical implementation easy to trace feature-by-feature.

---

## 3. Local SQLite layer

### Why local storage exists

A meaningful amount of the app's information does not need to be fetched every time the app opens.

The project therefore uses SQLite for things such as:

- fire-station records
- contact/reference data
- tips/reference data
- received alert history
- QR scan history

`DatabaseAdapter.java` is the central access point for those tables and queries.

The original project also used a pre-created database copied into the app through `PreCreateDB`. The database asset itself is intentionally not included in this public snapshot.

### Pre-created database flow

Conceptually:

```text
bundled Firefighters.db
        ↓
PreCreateDB.copyDB(...)
        ↓
app-private database location
        ↓
DatabaseAdapter
        ↓
models / RecyclerViews / map markers
```

For station/reference data, this avoids waiting for a server before the UI can have something useful to display.

### Local alert history

The notification service writes received alerts into SQLite.

This separates two different concerns:

- **FCM** answers: “how does a message reach the phone?”
- **SQLite** answers: “how can the user see that message again later?”

That distinction still makes sense today, even though I would now put the local layer behind Room and a repository.

### Local QR history

The same idea applies to QR scanning. The camera produces a result, but history belongs to the device.

The scan-history screen reads the saved rows through `DatabaseAdapter`, renders them in a `RecyclerView`, and allows a swipe gesture to remove a row.

Earlier commits show that scan and alert history were moved to SQLite as the project evolved. That was a useful design improvement: those histories did not need a remote database simply because Firebase already existed elsewhere in the app.

---

## 4. Station discovery

Station discovery is one of the more complete flows in the project.

There are both list and map representations of the local station catalogue.

### Map flow

`StationMapFragment` performs several jobs:

```text
local station database
        ↓
Station models
        ↓
GoogleMap markers
        ↓
custom info windows
```

Each marker can carry details such as:

- station name
- address
- phone number
- total firefighters
- total vehicles

The map uses a custom station icon and a custom info-window adapter.

### Geographic constraint

The map camera is bounded to Sri Lanka, and text searches through Android's `Geocoder` are accepted only when the resulting country code is `LK`.

That made the search behavior fit the app's actual data instead of behaving like a general global maps client.

### Device location

The fragment uses Google's fused location provider to move the map to the user's last known location.

The surrounding flow also handles:

- fine-location permission
- GPS disabled state
- redirecting to Android settings
- a custom “allow access / not now” UI state

### What I would change now

`StationMapFragment` currently knows too much: permission state, map setup, SQLite access, search, markers, GPS state and UI animation.

Today I would separate this into something closer to:

```text
StationMapScreen
      ↓ events
StationMapViewModel
      ↓
StationRepository ── Room
      ↓
LocationProvider
```

The map composable/view would render state; it would not directly own the storage and location workflows.

---

## 5. Push-alert pipeline

`PushNotificationService` extends `FirebaseMessagingService`.

When an FCM notification arrives, the service:

1. reads the notification title/body,
2. timestamps the message,
3. inserts an alert record into local SQLite,
4. creates a high-importance notification channel,
5. posts the Android notification.

```text
Firebase Cloud Messaging
          │
          ▼
PushNotificationService
          │
     ┌────┴────┐
     ▼         ▼
 Android     SQLite
notification  history
                 │
                 ▼
          Notifications screen
```

This is one of the places where the app mixes remote delivery with durable local UX in a useful way.

### Limitations

A production implementation would need much more care around:

- notification/data payload variants
- null-safe handling of `RemoteMessage`
- notification IDs / grouping
- deep links
- Android 13+ notification permission
- background behavior
- schema migrations for saved alerts
- deduplication

The current implementation is intentionally left as a historical snapshot rather than being partially modernized into a misleading “production-ready” state.

---

## 6. QR scanning

The QR feature has three main responsibilities:

```text
QRScannerHome
    │
    ├── asks for camera permission
    │
    ▼
QRScanner
    │
    ▼
scan result
    │
    ├── result UI
    └── local SQLite history
             │
             ▼
       QRScannerHome
       swipe-to-delete
```

The history screen intentionally reloads local rows on `onResume()`, so returning from the capture screen refreshes the visible list.

The implementation uses an older camera/scanner library and would be replaced today with something like CameraX + ML Kit / ZXing depending on requirements.

---

## 7. Live dispatch audio

The live-dispatch experiment is one of the project's stranger features.

The app reads two values from Firebase Realtime Database:

```text
live_dispatch/
├── url
└── status
```

Those values let the stream endpoint and visible status change without publishing a new app version.

`LiveDispatch` then:

1. creates a `MediaPlayer`,
2. assigns the remote stream URL,
3. prepares and starts playback,
4. updates the UI with the remote status,
5. reflects play/stop state with Lottie ripple animations,
6. connects an in-app seek bar to the device media volume.

```text
Firebase RTDB
   │
   ├── stream URL
   └── status
        │
        ▼
   LiveDispatch
        │
        ▼
    MediaPlayer
        │
        ▼
   device audio
```

### Lifecycle concern

The app does stop the player when the activity is destroyed, and the project history includes a fix specifically for audio continuing after leaving the activity.

A modern implementation would still go further:

- use lifecycle-aware playback state,
- release rather than only stop resources,
- consider Media3/ExoPlayer,
- model buffering/error states,
- avoid blocking `prepare()`,
- isolate playback from UI code.

---

## 8. Firebase Realtime Database

Firebase is not used as the only data store in this app.

It is mainly useful for content that was expected to change remotely, such as:

- events
- career listings
- live-dispatch configuration
- other small remotely managed values

That distinction is worth calling out because it reflects an early architectural instinct that I still use: **remote data should earn its need to be remote**.

Static/reference data and device history do not automatically belong in a cloud database.

### What is missing from this public snapshot

The repository intentionally does not include `google-services.json` or the original build configuration.

Anyone studying the source should therefore treat Firebase references as architecture examples from the historical app, not as a currently configured backend they can connect to.

---

## 9. Weather experiment

The dashboard includes a small temperature tile that uses Volley to call OpenWeather for Colombo.

The original prototype embedded the API key in source. The current public branch no longer does that.

Instead, `MainActivity` looks for a local Android string resource called:

```text
openweather_api_key
```

If it is absent, the tile gracefully displays `--°C` rather than shipping a credential.

An explorer could define it in an ignored local file such as:

```xml
<!-- app/src/main/res/values/secrets.xml -->
<resources>
    <string name="openweather_api_key">YOUR_LOCAL_KEY</string>
</resources>
```

`secrets.xml` is ignored by Git.

For a real product, I would still consider whether the third-party API key can safely live on-device at all. If the provider treats the value as a true secret, the request should go through a backend/proxy with appropriate abuse controls.

---

## 10. Witness statements and feedback

This part of the project is a useful example of something that **worked as a prototype but should not be shipped as designed**.

### Original approach

The early implementation attempted to send email directly from the Android application using SMTP credentials.

That meant the client needed access to mail authentication material — an unnecessary and risky responsibility for a mobile app.

### Current public snapshot

The cleaned-up branch no longer authenticates to SMTP.

Both the witness-statement and feedback/commendation forms now:

1. validate the form locally,
2. build a subject/body,
3. launch an `ACTION_SENDTO` email intent,
4. hand the draft to an installed mail app.

```text
form
  ↓
local validation
  ↓
pre-filled mail draft
  ↓
user's mail application
```

This is intentionally less “automatic” but much safer for a public historical app because the Android client no longer needs an SMTP password.

### What I would do in a real rebuild

For a genuine structured submission workflow:

```text
mobile app
    ↓ authenticated HTTPS
submission API
    ↓
validation / rate limiting / audit
    ↓
mail / ticket / database integration
```

Sensitive delivery credentials belong on the server, not in the APK or in remotely downloadable app configuration.

---

## 11. News and WebView content

The news screen is intentionally simple: a `WebView` loads a news search for Colombo fire-brigade-related content.

It adds:

- a progress indicator,
- JavaScript support,
- swipe-to-refresh,
- back-stack navigation inside the web view.

This is another part I would rethink today. A production app should prefer a clear, trusted content source/API rather than embedding a search result page in a WebView.

---

## 12. Permissions and connectivity

The app directly checks and reacts to several device/environment states:

- camera permission
- fine location permission
- GPS enabled/disabled
- network connectivity
- device media volume

Dexter is used for some permission flows, while platform APIs handle GPS and connectivity.

The current `NetworkInfo` connectivity helper is deprecated on modern Android. A contemporary version would use `ConnectivityManager.NetworkCallback` / `NetworkCapabilities`, expose connection state through a lifecycle-aware data source, and avoid scattering connectivity checks through individual click handlers.

---

## 13. UI approach

This is a classic XML/View-era Android app.

The UI layer uses:

- activities
- fragments
- XML layouts
- RecyclerView adapters
- Material components
- bottom sheets
- floating action buttons
- custom drawables
- custom fonts
- Lottie animations
- explicit intents between screens

That is quite different from the Compose-based Android work I would start today, but it is still useful context because the underlying Android concepts — lifecycle, permissions, intents, services and local persistence — remain relevant.

---

## 14. Repository cleanup

Revisiting the project for portfolio use also meant cleaning up repository habits from the original prototype.

The current tree is intended to contain source and useful assets, not generated/local state.

Ignored or removed from the current snapshot include:

- `.idea/`
- release APK output
- `google-services.json`
- local build configuration
- local database asset
- local API-key resource

A historical Git repository can still retain deleted values in older commits. Removing something from the latest tree is **not** the same thing as erasing Git history, so any old credential should be considered retired and rotated rather than trusted again.

---

## 15. Why the repository is not drop-in buildable

Some old Android repos look cleaner if every historical configuration file is checked in, but that is not a good trade here.

The public snapshot intentionally excludes environment-specific or service-linked files, including parts of the old Gradle/Firebase setup and the pre-created database asset.

On top of that, the dependency set is from an older Android toolchain.

So the honest status is:

> This repository is for **source exploration and project history**, not for `git clone && ./gradlew installDebug` in 2026.

Reconstructing a runnable version would mean treating it as a migration project rather than simply filling in one missing file.

---

## 16. What I would build now

If I rebuilt the same idea today, I would separate the app into explicit feature/data boundaries.

```text
Compose screens
      │
      ▼
ViewModels
      │
      ▼
Use cases
      │
      ▼
Repositories
   ┌──┴───────────────┐
   ▼                  ▼
Room             Remote sources
   │             Firebase / APIs
   │                  │
   └──── cached / merged state ────┘
```

### Likely stack

- Kotlin
- Jetpack Compose
- Coroutines / Flow
- ViewModel + saved state
- Room
- Hilt
- Retrofit / Ktor client where appropriate
- Firebase Messaging
- Firebase Remote Config / Firestore / RTDB only where justified
- CameraX
- Google Maps Compose
- Media3
- WorkManager for durable background work

### Feature boundaries

I would probably split the code into modules/packages resembling:

```text
feature/stations/
feature/alerts/
feature/scanner/
feature/dispatch/
feature/events/
feature/careers/
feature/forms/
data/local/
data/remote/
core/location/
core/notifications/
core/network/
```

### State ownership

Instead of an activity doing this:

```text
click → connectivity check → Firebase/SQLite → UI mutation → Toast
```

I would prefer:

```text
UI event
   ↓
ViewModel
   ↓
repository/use case
   ↓
StateFlow<UiState>
   ↓
UI render
```

That would make error states, loading, retries and testing much easier to reason about.

---

## 17. Things this project taught me

A lot of the code is not how I write mobile apps now, but several lessons stuck around:

### Local data is a product feature

Station data, alerts and scan history showed how much nicer a mobile experience becomes when useful information does not disappear with connectivity.

### Remote configuration is powerful

The live-dispatch experiment showed the usefulness of changing an endpoint/status without an app release — but also why remote configuration needs validation and security boundaries.

### Platform APIs create real complexity

Location, GPS, permissions, camera, notifications and media all have lifecycle/error states that mockup-only projects never force you to confront.

### “It works” and “it should ship” are different bars

The old direct-SMTP flow is probably the cleanest example. It could deliver an email, but the security model was wrong. Revisiting the project years later makes that difference very obvious.

### Architecture starts hurting when a project grows

An activity-centric structure feels fast at the beginning. Once a dashboard has maps, Firebase, weather, forms, notifications, SQLite and media, the cost of separating responsibilities becomes impossible to ignore.

That lesson carried directly into the local-first and architecture work in my newer apps.

---

## 18. Status

Fire Rescue is a **legacy portfolio project**. I am not actively developing it into an emergency-services product.

The goal of keeping it here is to preserve an early native Android project with enough real moving parts to be interesting, while being explicit about its age, limitations and security history.

**Do not use this software for emergencies, dispatch, incident reporting, safety decisions or real fire-service operations.**
