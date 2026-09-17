<p align="center">
  <img src="https://raw.githubusercontent.com/SenithUmesha/fire-rescue/master/app/src/main/res/drawable/app_icon.png" width="96" alt="Fire Rescue app icon" />
</p>

<h1 align="center">Fire Rescue 🚒</h1>

<p align="center">
  an early native Android experiment that got way more features than planned.
</p>

<p align="center">
  <code>Java</code> · <code>Android Views</code> · <code>Firebase</code> · <code>SQLite</code> · <code>Google Maps</code>
</p>

---

Fire Rescue is a **2022 Android concept app** built around a simple question: what would a mobile companion for firefighter information and day-to-day tools look like?

It started with station details and grew into maps, push alerts, QR scanning, live dispatch audio, events, recruitment listings, contacts, safety tips, weather, news, witness-statement drafts and a few other experiments.

This is one of my earlier native Android projects, so I keep it public for a different reason than my newer apps: it shows where a lot of the mobile-app curiosity started.

> **Important:** this is a historical/educational project, not an official fire-service application and **not suitable for real emergency or operational use**.

## preview

<p align="center">
  <img src="https://user-images.githubusercontent.com/90299964/150658768-5433f167-a8c5-4009-99d4-6bee6db7a275.png" alt="Fire Rescue Android app screens" />
</p>

## what ended up in it

### 🗺️ station explorer

Fire stations can be browsed as a list or explored on a Google Map.

The map side includes:

- station markers loaded from the app's local data set
- station name, address and phone details
- firefighter and vehicle counts attached to markers
- current-device location
- place search through Android's geocoder
- camera bounds constrained to Sri Lanka
- location/GPS permission flows
- directions-oriented station discovery

The station catalogue lives locally, which means the core directory does not depend on a network request every time the user opens it.

### 🚨 alerts that leave a history

The app uses **Firebase Cloud Messaging** for heads-up notifications.

Incoming messages are not just displayed and forgotten. They are also written into the local SQLite layer with a timestamp, so the app can expose an alert history later. Saved alerts can be removed with swipe actions.

```text
FCM message
    ↓
FirebaseMessagingService
    ↓
heads-up Android notification
    ↓
SQLite alert history
    ↓
Notifications screen
```

### 📷 QR scanner + local history

There is a camera-based QR workflow with a separate scan-history screen.

The interesting part for this old project is that the scan history eventually moved away from remote storage and into **SQLite**. Scan results can be revisited locally and deleted with a swipe gesture.

### 📻 live dispatch experiment

A small audio-player screen reads a stream URL and status from **Firebase Realtime Database**, then hands the stream to Android's `MediaPlayer`.

It includes:

- play / stop controls
- live status text
- animated listening state
- in-app volume control tied to the device media stream
- remote stream configuration without rebuilding the APK

Definitely an experiment, but still one of my favorite odd little pieces in this repo.

### 🗓️ remotely updated content

Firebase Realtime Database is also used for content that was meant to change independently of the installed app, including things such as:

- events
- career / vacancy information
- parts of the app's remotely configurable content

There is also a news screen implemented with a `WebView` and swipe-to-refresh.

### 👥 contacts, tips & utility screens

The project grew into a fairly large set of classic Android screens: contacts, station details, a chief's welcome page, fire-safety tips, social links, contact information, settings and other small utility flows.

### 📝 witness & feedback drafts

The original prototype experimented with sending form data directly over SMTP from the Android client. That is **not a pattern I would ship today**.

The public snapshot has been cleaned up so the witness-statement and commendation forms now hand a pre-filled draft to the user's installed email app instead. No SMTP password needs to exist in the Android client.

### 🌤️ tiny weather tile

The home screen contains a small Colombo temperature experiment using **Volley + OpenWeather**.

The API key is intentionally absent from this repository. A local checkout can provide an ignored `openweather_api_key` string resource if someone is exploring that code path.

## how the pieces fit together

```text
┌─────────────────────────────────────────────┐
│          Android Activities / Views         │
│     dashboard · maps · lists · forms        │
└───────────────────┬─────────────────────────┘
                    │
          ┌─────────┼──────────┬───────────────┬──────────────┐
          │         │          │               │              │
          ▼         ▼          ▼               ▼              ▼
       SQLite    Firebase     FCM          Google Maps      Volley
          │        RTDB        │          + Location         │
          │         │          │               │              │
 stations/contacts  │      push alerts     station map    weather
 alerts/scan history│      + local log     + geocoder
                    │
             events / careers /
             dispatch config
```

There is a more detailed walkthrough in **[docs/engineering.md](docs/engineering.md)**.

## local data vs remote data

One thing I was already experimenting with here was choosing storage based on what the feature actually needed.

| Data | Where it lives | Why |
| --- | --- | --- |
| Station/contact/reference data | bundled SQLite database | useful without waiting on a network request |
| Alert history | SQLite | belongs to the device and should remain browsable |
| QR scan history | SQLite | simple local history with fast reads/deletes |
| Events / careers | Firebase Realtime Database | content can change remotely |
| Dispatch URL + status | Firebase Realtime Database | can be changed without an app release |
| Push alerts | Firebase Cloud Messaging | server-to-device notification delivery |

It is not a modern offline-first architecture, but it was an early step toward thinking about **local state and remote state as different jobs** instead of putting everything behind one database.

## project structure

```text
app/src/main/java/com/blackeyedghoul/firefighters/
├── MainActivity.java             # dashboard / navigation / weather
├── StationMapFragment.java       # maps, geocoding and location
├── StationListFragment.java      # station directory
├── DatabaseAdapter.java          # local SQLite access
├── PushNotificationService.java  # FCM → notification + local history
├── Notifications.java            # alert history
├── QRScanner*.java               # QR capture, result and history flows
├── LiveDispatch.java             # remotely configured audio stream
├── Events.java / Careers.java    # Firebase-backed dynamic content
└── ...                           # contacts, tips, forms, settings, etc.

app/src/main/res/
├── layout/                       # classic XML Android UI
├── drawable/                     # icons and artwork
└── raw/                          # Lottie animations + media
```

## stack

**Android**

`Java` · `AndroidX` · `Activities` · `Fragments` · `XML layouts` · `RecyclerView`

**Data & backend**

`SQLite` · `SQLiteAssetHelper` · `Firebase Realtime Database` · `Firebase Cloud Messaging`

**Device / platform**

`Google Maps SDK` · `Fused Location Provider` · `Geocoder` · `Camera` · `MediaPlayer` · `WebView`

**Other experiments**

`Volley` · `Lottie` · `Dexter` · `CamView`

## about running it

This repository is best treated as a **source snapshot**, not a freshly maintained starter project.

Some original project/configuration files are intentionally not published, including service configuration, local secrets and the bundled database asset. The project also targets an older Android/Gradle dependency set, so cloning it today is **not expected to produce a one-command build** without reconstruction and modernization.

That is intentional. The useful part of this repo is the app code and the engineering history, not an old deployable emergency-services APK.

The old generated release binary and IDE project metadata are also excluded from the current tree to keep the repository focused on source.

## security cleanup

This project predates a lot of the patterns I use now, and revisiting it exposed a couple of classic prototype mistakes:

- an API key had been embedded directly in source
- the prototype attempted to obtain SMTP credentials for client-side email delivery
- generated release files and IDE state were committed

The current branch removes those runtime credential patterns. Weather credentials are local-only, and form submission is handed off to an installed mail app rather than authenticating to SMTP from the client.

The old values may still exist in Git history, so historical credentials should be considered retired rather than reusable.

## if I rebuilt it today

This would look very different:

```text
Jetpack Compose
      ↓
ViewModel + immutable UI state
      ↓
Use cases / repositories
      ↓
Room ───────────── remote data source
 │                       │
local cache          Firebase / API
```

I would use Kotlin + Compose, Room, lifecycle-aware state, dependency injection, a proper backend for form submission, stricter Firebase rules, modern location/permission APIs and a much smaller set of responsibilities per screen.

And I definitely would not let an `Activity` become the place where navigation, weather, connectivity and remote credentials all meet. 😅

## why keep an old project public?

Because polished current work only shows the latest version of the story.

This repo is messy in places, very `2022 Android`, and full of decisions I would make differently now — but it also has maps, location, push notifications, SQLite, camera work, Firebase, audio streaming and a surprising number of complete interaction flows.

That makes it a pretty good time capsule.

---

Built as a learning / experimental project by [Senith Umesha](https://github.com/SenithUmesha).

**Not affiliated with any fire department or emergency authority. Do not use this application for emergencies, dispatch, incident reporting or operational decisions.**
