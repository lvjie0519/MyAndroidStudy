package com.android.study.example.uidemo.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.http.SslError;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.study.example.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.jingbin.progress.WebProgress;

public class WebViewUploadFileActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebProgress mWebProgress;
    private WebViewUploadFileClient mWebViewUploadFileClient;

    public static final String INTENT_CAMERA_PHOTO_URI = "cameraPhotoUri";
    public static final String INTENT_VIDEO_PHOTO_URI = "videoPhotoUri";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WebViewUploadFileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_upload_file);

        initView();
    }

    private void initView(){
        initWebProgress();
        initWebView();
    }

    private void initWebProgress(){
        this.mWebProgress = findViewById(R.id.progress);

        this.mWebProgress.show(); // 显示
        this.mWebProgress.setWebProgress(10);              // 设置进度
//        this.mWebProgress.setColor("#D81B60");             // 设置颜色
        this.mWebProgress.setColor("#00D81B60","#D81B60"); // 设置渐变色
        this.mWebProgress.hide(); // 隐藏

    }

    /**
     * 加载线上html文件
     */
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
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("file:///android_asset/upload_image.html");
//        mWebView.loadUrl("https://mail.qq.com/");
//        mWebView.loadUrl("https://m.sogou.com/web/searchList.jsp?s_from=pcsearch&keyword=百度");
//        mWebView.loadUrl("http://www.imagetotxt.com");
//        mWebView.loadUrl("http://192.168.99.25:8090/");

        mWebViewUploadFileClient = new WebViewUploadFileClient(this);
        mWebView.setWebChromeClient(mWebViewUploadFileClient);

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

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.i("lvjie", "onReceivedError ...SslError: "+error.getUrl());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("lvjie", "requestCode: "+requestCode+"  permissions: "+permissions.length+"  grantResults: "+grantResults.length);
    }

    private boolean checkRequestPermissions(){
        List<String> permissionsList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("lvjielvjie", "No CAMERA permission");
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("lvjielvjie", "No READ_EXTERNAL_STORAGE permission");
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionsList.isEmpty()) {
            Log.i("lvjielvjie", "has permission");
            return true;
        }
        String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
        ActivityCompat.requestPermissions(this, permissions, 1001);
        return false;
    }

    public void onClickCheckRequestPermissions(View view) {
        checkRequestPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("lvjie", "requestCode: "+requestCode+"  resultCode: "+resultCode);

        if (resultCode != Activity.RESULT_OK) {
            if (requestCode == WebViewUploadFileClient.FILECHOOSER_RESULTCODE) {
                Log.i("lvjie", "doWebViewFileChooseResultCancel.");
                doWebViewFileChooseResultCancel();
            }
            return;
        }

        if (requestCode == WebViewUploadFileClient.FILECHOOSER_RESULTCODE) {
            doWebViewFileChooseResultOk(data);
        }
    }

    private void doWebViewFileChooseResultCancel() {
        mWebViewUploadFileClient.clearUri();
        mWebViewUploadFileClient.invokeChromValueCallback(null);
    }

    private void doWebViewFileChooseResultOk(Intent data) {

        if (data != null && data.getClipData() != null) {
            // 有选择多个文件
            Log.i("lvjie", "doWebViewFileChooseResultOk, multi file choose.");
            int count = data.getClipData().getItemCount();
            List<Uri> uris = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Uri fileUri = data.getClipData().getItemAt(i).getUri();
                if(WebChromUtils.hasReadUriPermission(this, fileUri)){
                    uris.add(fileUri);
                }else{
                    Log.e("lvjielvjie", fileUri.toString()+"has no read permission.");
                }
            }
            mWebViewUploadFileClient.invokeChromValueCallback(uris.toArray(new Uri[uris.size()]));
        } else {
            Log.i("lvjie", "doWebViewFileChooseResultOk, single file choose.");
            Uri uri = (data == null) ? null : data.getData();
            if (uri == null) {
                Log.i("lvjie", "doWebViewFileChooseResultOk, will use camera photo uri.");
                uri = mWebViewUploadFileClient.getCameraUri();
            }

            if(uri != null){
                Log.i("lvjielvjie", "getPath: "+uri.getPath()+"  getAuthority: "+uri.getAuthority()+"  toString: "+uri.toString());
            }

            if (!WebChromUtils.hasReadUriPermission(this, uri)) {
                Log.e("lvjielvjie", uri+" has no read permission.");
                uri = null;
            }

            Uri[] uris = (uri == null) ? null : new Uri[]{uri};
            mWebViewUploadFileClient.invokeChromValueCallback(uris);
        }
    }



}