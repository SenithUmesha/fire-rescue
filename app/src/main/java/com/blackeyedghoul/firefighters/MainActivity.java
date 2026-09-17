package com.blackeyedghoul.firefighters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    TextView tempTxt;
    ImageView settings;
    DecimalFormat df = new DecimalFormat("#");
    MenuItem item1, item2, item3, item4, item5;
    CardView card_0, card_1, card_2, card_3, card_4, card_5, card_6, card_7, card_8, card_9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark_red));

        init();

        bottomNavigationView.setBackground(null);

        clearMenu();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());

                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_grow);
                fab.startAnimation(animation);

                if (view.getId() == R.id.fab) {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        card_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChiefsWelcome.class);
                startActivity(intent);
            }
        });

        card_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, Stations.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Commends.class);
                startActivity(intent);
            }
        });

        card_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactList.class);
                startActivity(intent);
            }
        });

        card_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, Careers.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WitnessStatements.class);
                startActivity(intent);
            }
        });

        card_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRScannerHome.class);
                startActivity(intent);
            }
        });

        card_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FAQs.class);
                startActivity(intent);
            }
        });

        card_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactUs.class);
                startActivity(intent);
            }
        });

        card_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, LiveDispatch.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                item1.setChecked(true);
                Intent intent = new Intent(MainActivity.this, Notifications.class);
                startActivity(intent);
                return true;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (isConnected(MainActivity.this)) {
                    item2.setChecked(true);
                    Intent intent = new Intent(MainActivity.this, News.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (isConnected(MainActivity.this)) {
                    item3.setChecked(true);
                    Intent intent = new Intent(MainActivity.this, Events.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        item4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (isConnected(MainActivity.this)) {
                    item4.setChecked(true);
                    Intent intent = new Intent(MainActivity.this, SocialMedia.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        getWeatherDetails();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clearMenu();
    }

    private void clearMenu() {
        item1.setChecked(false);
        item2.setChecked(false);
        item3.setChecked(false);
        item4.setChecked(false);
        item5.setChecked(false);
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        tempTxt = findViewById(R.id.temp);
        fab = findViewById(R.id.fab);
        card_0 = findViewById(R.id.card_0);
        card_1 = findViewById(R.id.card_1);
        card_2 = findViewById(R.id.card_2);
        card_3 = findViewById(R.id.card_3);
        card_4 = findViewById(R.id.card_4);
        card_5 = findViewById(R.id.card_5);
        card_6 = findViewById(R.id.card_6);
        card_7 = findViewById(R.id.card_7);
        card_8 = findViewById(R.id.card_8);
        card_9 = findViewById(R.id.card_9);
        item1 = bottomNavigationView.getMenu().findItem(R.id.alerts);
        item2 = bottomNavigationView.getMenu().findItem(R.id.news);
        item3 = bottomNavigationView.getMenu().findItem(R.id.events);
        item4 = bottomNavigationView.getMenu().findItem(R.id.social_media);
        item5 = bottomNavigationView.getMenu().findItem(R.id.holder);
        settings = findViewById(R.id.settings);
    }

    private void getWeatherDetails() {
        if (!isConnected(MainActivity.this)) {
            Toast.makeText(this, "Online features will be unavailable. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiKey = getOpenWeatherApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            // The public repository intentionally does not include API credentials.
            // Add an ignored `openweather_api_key` string resource in a local secrets.xml
            // if you want to restore the weather tile while experimenting with the app.
            tempTxt.setText("--°C");
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather";
        String tempUrl = url + "?q=Colombo&appid=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    tempTxt.setText(df.format(temp) + "°C");
                } catch (JSONException e) {
                    Log.e("weather", "Unable to parse weather response", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("weather", "Weather request failed", error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private String getOpenWeatherApiKey() {
        int resourceId = getResources().getIdentifier(
                "openweather_api_key",
                "string",
                getPackageName()
        );

        if (resourceId == 0) {
            return null;
        }

        return getString(resourceId);
    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }
}
