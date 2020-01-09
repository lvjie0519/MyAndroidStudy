package com.android.study.example.uidemo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.study.example.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.jingbin.progress.WebProgress;

public class WebViewDemoActivity extends AppCompatActivity {

    private String mContent;
    private WebView mWebView;
    private WebProgress mWebProgress;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WebViewDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_demo);

        initData();
        initView();
    }

    private void initView(){
        initWebProgress();
        initWebView();
    }

    private void initWebView(){
        this.mWebView = findViewById(R.id.wb_webView);
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        long time = System.currentTimeMillis();
        Log.i("lvjie", "start loadDataWithBaseURL  "+time);
        mWebView.loadData(mContent, "text/html", "charset=utf-8");
//        mWebView.loadDataWithBaseURL(null, mContent, "text/html", "charset=utf-8", null);
        Log.i("lvjie", "end loadDataWithBaseURL  time is cost "+(System.currentTimeMillis()-time));

        this.mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("lvjie", "onProgressChanged newProgress="+newProgress);
                mWebProgress.setWebProgress(newProgress);
            }


        });

        this.mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("lvjie", "onPageStarted ...");
                mWebProgress.setWebProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("lvjie", "onPageFinished ...");
                mWebProgress.hide();
            }
        });

    }

    private void initWebProgress(){
        this.mWebProgress = findViewById(R.id.progress);

        this.mWebProgress.show(); // 显示
        this.mWebProgress.setWebProgress(10);              // 设置进度
//        this.mWebProgress.setColor("#D81B60");             // 设置颜色
        this.mWebProgress.setColor("#00D81B60","#D81B60"); // 设置渐变色
        this.mWebProgress.hide(); // 隐藏

    }

    private void initData(){
        long time = System.currentTimeMillis();
        Log.i("lvjie", "start read  "+time);
        mContent = getDataFromAssert();
        Log.i("lvjie", "end read  time is cost "+(System.currentTimeMillis()-time));
    }

    private String getDataFromAssert(){

        StringBuffer stringBuffer = new StringBuffer();

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("webview-test.html");
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            // 分行读取
            while ((line = buffreader.readLine()) != null) {
                stringBuffer.append(line);
            }
            buffreader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

}
