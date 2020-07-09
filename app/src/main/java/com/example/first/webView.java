package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webView extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView= findViewById(R.id.tutorial);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com/search?q=java+tutorial&sxsrf=ALeKk01nRvliV-FYyMd8PNvcQfpcc9fD9A:1594183116541&source=lnms&tbm=vid&sa=X&ved=2ahUKEwiX4vjy6rzqAhU3yjgGHZBuDZ8Q_AUoAXoECA8QAw&biw=1280&bih=610");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}