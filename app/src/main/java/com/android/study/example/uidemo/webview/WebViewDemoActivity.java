package com.android.study.example.uidemo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
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

        setWebViewCookie();
        initData();
        initView();
    }

    private void setWebViewCookie(){

        CookieSyncManager.createInstance(this);

        String StringCookie = "key=" + "aaaaaa" + ";path=bbb";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie("www.baidu.com", StringCookie);
        cookieManager.setCookie("www.baidu.com:443", StringCookie);
        cookieManager.setCookie("https://www.baidu.com/", StringCookie);

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    private void initView(){
        initWebProgress();
        initWebViewStyle1();
//        initWebViewStyle2();
    }

    /**
     * 加载本地html文件
     */
    private void initWebViewStyle1(){
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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    /**
     * 加载线上html文件
     */
    private void initWebViewStyle2(){

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
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("https://www.baidu.com/");

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
