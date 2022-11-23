package com.android.study.example.uidemo.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.study.example.R;
import com.android.study.example.androidapi.utils.SystemSettingUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.jingbin.progress.WebProgress;
import rx.functions.Action1;

public class WebViewDemoActivity extends Activity {

    private String mContent;
    private WebView mWebView;
    private WebProgress mWebProgress;

    GeolocationPermissions.Callback mCallback;
    String mOrigin;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WebViewDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_demo);

//        setWebViewCookie();
        initData();
        initView();
    }

    /**
     * 检查是否需要申请权限
     * @return  true: 需要申请   false: 不需要申请
     */
    private boolean checkToRequestPermmission(){

        if(Build.VERSION.SDK_INT < 23){
            // android 6.0以上才需要动态权限获取
            return false;
        }

        boolean result = false;
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, permissions, 1001);
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGrant = true;
        for (int result: grantResults) {
            if(result != PackageManager.PERMISSION_GRANTED){
                allGrant = false;
                break;
            }
        }

        Log.i("lvjie", "allGrant: "+allGrant);
        if(mCallback != null){
            Log.i("lvjie", "callback.invoke on request.");
//            mCallback.invoke(mOrigin, allGrant, false);
        }
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
//        initWebViewStyle1();
        initWebViewStyle2();
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

        mWebView.getSettings().setGeolocationEnabled(true);     // 允许地理位置可用

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
//        mWebView.loadUrl("https://map.baidu.com/mobile/webapp/index/index/third_party=wisesquare");
        mWebView.loadUrl("https://m.amap.com/");

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                Log.i("lvjie", "onGeolocationPermissionsShowPrompt call. origin: "+origin);
//                mCallback = callback;
//                mOrigin = origin;

                callback.invoke(origin, false, false);

//                if(!checkToRequestPermmission()){
//                    Log.i("lvjie", "callback.invoke on not request.");
//                    callback.invoke(origin, true, false);
//                }
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
                Log.i("lvjie", "shouldOverrideUrlLoading ..."+url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
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

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.i("lvjie", "onReceivedError ...SslError: "+error.getUrl());
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
