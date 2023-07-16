package com.example.mymgstudyapp.webview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mymgstudyapp.R;

public class WebViewTestActivity extends AppCompatActivity {

    private WebView mWebView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WebViewTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);

        initWebView();
    }

    private void initWebView(){

        this.mWebView = findViewById(R.id.wb_webView);

        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);

        //缩放操作
        mWebView.getSettings().setSupportZoom(true);  // 支持缩放，默认为true。是下面那个的前提。
        mWebView.getSettings().setBuiltInZoomControls(true);  // 设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebView.getSettings().setDisplayZoomControls(false); // 隐藏原生的缩放控件


        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);

        //设置WebView缓存
        //优先使用缓存:
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("file:///android_asset/upload_image.html");

        this.mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("lvjie", "onPageStarted ...");

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("lvjie", "onPageFinished ...");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("lvjie", "shouldOverrideUrlLoading ..."+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i("lvjie", "onReceivedError ...WebResourceError: "+error.toString());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.i("lvjie", "onReceivedError ...failingUrl: "+failingUrl);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });

    }


}