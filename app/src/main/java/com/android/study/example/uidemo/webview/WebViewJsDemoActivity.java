package com.android.study.example.uidemo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.study.example.R;
import com.android.study.example.utils.AssetFileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

/**
 * 测试 js 与 java的交互
 */
public class WebViewJsDemoActivity extends AppCompatActivity {

    private WebView mWebView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WebViewJsDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_js_demo);

        initView();
    }

    private void initView(){
        initWebView();
    }

    private void initWebView(){
        this.mWebView = findViewById(R.id.wb_webView);

        // 支持javascript
        this.mWebView.getSettings().setJavaScriptEnabled(true);

        this.mWebView.addJavascriptInterface(new MyJavascriptInterface(this), "javaInjectedObject");

//        String content = AssetFileUtil.getAssertFileContent(this, "webview-js-test.html");
//        mWebView.loadData(content, "text/html", "charset=utf-8");

        mWebView.loadUrl("file:///android_asset/webview-js-test.html");

        this.mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("lvjie", "onProgressChanged newProgress="+newProgress);
            }
        });

        this.mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("lvjie", "onPageStarted ..."+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("lvjie", "onPageFinished ..."+url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("lvjie", "shouldOverrideUrlLoading ..."+url);
                return true;
            }
        });
    }

    /**
     * java 调用 js 方法
     * @param view
     */
    public void onClickJavaCallJsFunc1(View view) {
        this.mWebView.loadUrl("javascript:javacalljs()");
    }

    /**
     * java 调用 js 方法
     * @param view
     */
    public void onClickJavaCallJsFunc2(View view) {
        String data = "java data";
        mWebView.loadUrl("javascript:javacalljswithargs('" + data+ "')");
    }

    private class MyJavascriptInterface{

        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        /**
         * js调用java方法，方法无参数
         */
        @JavascriptInterface
        public void startJavaFunction() {
            Log.i("lvjie", "startJavaFunction...");
        }

        /**
         * js调用java方法，方法有参数，且参数名为data
         *
         * @param data 网页js里的参数名
         */
        @JavascriptInterface
        public void startJavaFunctionWithParams(String data) {
            Log.i("lvjie", "startJavaFunctionWithParams..."+data);
        }

        /**
         * js调用java方法，方法有参数，且参数名为data
         *
         * @param data 网页js里的参数名
         */
        @JavascriptInterface
        public String startJavaFunctionReturnData(String data) {
            Log.i("lvjie", "startJavaFunctionReturnData..."+data);
            return data+"，  java 返回的！";
        }
    }
}