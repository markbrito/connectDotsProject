package com.mdb.connectdots;

import android.content.Intent;
import android.webkit.*;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.util.Log;

public class MainActivity extends Activity {
    public static final String NAME="connectdots";
    public static final String TAG="com.mdb.connectdots";

    public static MainActivity mainActivity;
    public static WebServer webServer;
    public static ConnectDots connectDotsGame;
    public static android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=this;
        this.setContentView(R.layout.web);
        webView= (android.webkit.WebView)
                this.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewClient wvc=new WebViewClient();
        WebChromeClient wcc=new WebChromeClient();
        // webView.setWebViewClient(wvc);
        //webView.setWebChromeClient(wcc);
        webView.canGoBack();
        webView.canGoForward();
        webView.setSaveEnabled(true);
        webView.setSoundEffectsEnabled(true);
        startWebServer();
    }
    @Override
    public void onResume(){
        super.onResume();
        try{
            String mMainURL ="http://localhost:" +
                    WebServer.mPort + "/";
            MainActivity.webView.loadUrl(mMainURL);
        }
        catch(Exception e){
            Log.e(MainActivity.TAG, e.getMessage() + e.getStackTrace());
        }
    }
    public void startWebServer() {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(MainActivity.this,SocketServer.class);
        startService(serviceIntent);
    }
}