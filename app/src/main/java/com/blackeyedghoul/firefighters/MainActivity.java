package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    DecimalFormat df = new DecimalFormat("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark_red));

        Init();

        bottomNavigationView.setBackground(null);

        MenuItem item1 = bottomNavigationView.getMenu().findItem(R.id.alerts);
        item1.setChecked(false);
        MenuItem item2 = bottomNavigationView.getMenu().findItem(R.id.news);
        item2.setChecked(false);
        MenuItem item3 = bottomNavigationView.getMenu().findItem(R.id.events);
        item3.setChecked(false);
        MenuItem item4 = bottomNavigationView.getMenu().findItem(R.id.social_media);
        item4.setChecked(false);
        MenuItem item5 = bottomNavigationView.getMenu().findItem(R.id.holder);
        item5.setChecked(false);

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

        getWeatherDetails();
    }

    private void Init() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        tempTxt = findViewById(R.id.temp);
        fab = findViewById(R.id.fab);
    }

    private void getWeatherDetails() {
        String tempUrl;

        if (!isConnected(MainActivity.this)) {
            Toast.makeText(this, "Online features will be unavailable. Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
        } else {
            String api_key = "aa08a65605580df4a2fd2089f117b732";
            String url = "https://api.openweathermap.org/data/2.5/weather";
            tempUrl = url + "?q=" + "Colombo" + "&appid=" + api_key;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        output += df.format(temp) + "°C";
                        tempTxt.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }
}