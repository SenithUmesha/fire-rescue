package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class News extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    ImageView back;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getWindow().setStatusBarColor(ContextCompat.getColor(News.this, R.color.dark_red));

        init();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(view.VISIBLE);
                int refreshCycleColor = getResources().getColor(R.color.dark_red);
                swipeRefreshLayout.setColorSchemeColors(refreshCycleColor);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
        registerForContextMenu(webView);

        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(webView.getScrollY() == 0);
            }
        });

       webView.loadUrl("https://www.google.com/search?q=colombo+fire+brigade&source=lnms&tbm=nws&sa=X&ved=2ahUKEwjKpKm2n8P1AhXjH7cAHUocDawQ_AUoA3oECAEQBQ");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void init() {
        webView = findViewById(R.id.news_webView);
        progressBar = findViewById(R.id.news_pb);
        back = findViewById(R.id.news_back);
        swipeRefreshLayout = findViewById(R.id.news_swipeContainer);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}