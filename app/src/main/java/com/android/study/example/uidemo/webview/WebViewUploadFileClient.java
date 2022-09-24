package com.android.study.example.uidemo.webview;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class WebViewUploadFileClient extends WebChromeClient {
    public ValueCallback<Uri> UploadMsg;
    public ValueCallback<Uri[]> UploadMsg2;
    private Activity activity;
    private Uri cameraUri;
    private Uri videoUri;

    public static final int FILECHOOSER_RESULTCODE = 5173;

    public static String mCameraFilePath = "";

    @SuppressWarnings("deprecation")
    public WebViewUploadFileClient(Activity cordova) {

        this.activity = cordova;

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        super.onProgressChanged(view, newProgress);

        Log.i("lvjie", "onProgressChanged.");
    }



    // Android > 4.1.1
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {

        Log.i("lvjie", "1. onShowFileChooser.  fileChooserParams: "+fileChooserParamsToStr(fileChooserParams));
        UploadMsg2 = filePathCallback;
        Intent intent = WebChromUtils.createIntentByFileChooser(activity, fileChooserParams);

        cameraUri = intent.getParcelableExtra(WebViewUploadFileActivity.INTENT_CAMERA_PHOTO_URI);
        videoUri = intent.getParcelableExtra(WebViewUploadFileActivity.INTENT_VIDEO_PHOTO_URI);

        activity.startActivityForResult(intent, FILECHOOSER_RESULTCODE);
//        activity.startActivityForResult(Intent.createChooser(intent, "选择"), FILECHOOSER_RESULTCODE);


//        String[] acceptTypes = fileChooserParams.getAcceptTypes();
//        boolean allowMultiple = fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE;
//        Intent intent = fileChooserParams.createIntent();
//
//        ArrayList<Parcelable> extraIntents = new ArrayList<>();
//        if (acceptsImages(acceptTypes)) {
//            extraIntents.add(createCameraIntent());
//        }
////        if (acceptsVideo(acceptTypes)) {
////            extraIntents.add(getVideoIntent());
////        }
//
//        Intent fileSelectionIntent = createFileChooseIntent(fileChooserParams);
//
//        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, fileSelectionIntent);
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new Parcelable[]{}));
//
//        if (chooserIntent.resolveActivity(activity.getPackageManager()) != null) {
//            activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
//        } else {
//            Log.w("RNCWebViewModule", "there is no Activity to handle this Intent");
//        }

        return true;
    }

    private String fileChooserParamsToStr(FileChooserParams fileChooserParams) {
        if (fileChooserParams == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ getMode(1表示多文件选择): ").append(fileChooserParams.getMode())
                .append("; isCaptureEnabled(是否打开相机): ").append(fileChooserParams.isCaptureEnabled());

        stringBuilder.append("; getAcceptTypes(文件类型): ");
        for (String s : fileChooserParams.getAcceptTypes()) {
            stringBuilder.append(s).append("; ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public ValueCallback<Uri> getUploadMsg() {
        return UploadMsg;
    }

    public ValueCallback<Uri[]> getUploadMsg2() {
        return UploadMsg2;
    }

    public void setUploadMsg(ValueCallback<Uri> uploadMsg) {
        UploadMsg = uploadMsg;
    }

    public void setUploadMsg2(ValueCallback<Uri[]> uploadMsg2) {
        UploadMsg2 = uploadMsg2;
    }

    public void invokeChromValueCallback(Uri[] uris){
        if (UploadMsg2 != null) {
            UploadMsg2.onReceiveValue(uris);
            UploadMsg2 = null;
        }
    }

    public Uri getCameraUri() {
        Uri tempUri = cameraUri;
        cameraUri = null;
        return tempUri;
    }

    public Uri getVideoUri(){
        Uri tempUri = videoUri;
        videoUri = null;
        return tempUri;
    }

    public void clearUri(){
        cameraUri = null;
        videoUri = null;
    }

    public void getUri(){

    }
}
