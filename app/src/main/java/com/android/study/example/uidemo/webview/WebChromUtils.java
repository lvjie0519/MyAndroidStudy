package com.android.study.example.uidemo.webview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WebChromUtils {

    private static final String TAG = "lvjie";

    /**
     * 文件选择：            <input type="file" />
     * 多文件选择：           <input type="file" multiple="multiple" />
     * 多种类型文件选择：      <input type="file" accept="image/*,audio/mp4,video/mp4" />
     * 相册选择：            <input type="file" accept="image/*" />
     * 打开相机：            <input type="file" accept="image/*" capture="user" />
     * @param activity
     * @param fileChooserParams
     * @return
     */
    public static Intent createIntentByFileChooser(Activity activity, WebChromeClient.FileChooserParams fileChooserParams) {

        if (fileChooserParams == null) {
            Log.e(TAG, "can not createIntentByFileChooser. fileChooserParams  is null");
            return null;
        }

        if (activity == null) {
            Log.e(TAG, "can not createIntentByFileChooser. activity is null");
            return null;
        }

        /**
         * <input type="file" accept="image/*" capture="user" />
         * 这种类型的标签，仅打开相机拍照
         */
        if (fileChooserParams.isCaptureEnabled() && onlyImageAcceptType(fileChooserParams.getAcceptTypes())) {
            // 打开相机
            Log.i(TAG, "createCameraIntent.");
            return createCameraIntent(activity);
        }

        /**
         * <input type="file" accept="" capture="user" />
         * accept为空或其他类型的， 这种类型的标签，显示系统弹窗，让用户选择相机、摄像及文件
         */
        if (fileChooserParams.isCaptureEnabled()) {
            Log.i(TAG, "createCameraVideoFileIntent.");
            return createCameraVideoFileIntent(activity, fileChooserParams);
        }

        Intent intent;

        /**
         * <input type="file" accept="image/*" />
         * accept 仅传递 image/* ： 显示系统弹窗，让用户选择相机及相册
         */
        if (onlyImageAcceptType(fileChooserParams.getAcceptTypes())) {
            // 显示系统弹窗，让用户选择相机及相册
            Log.i(TAG, "createCameraPhotoIntent.");
            intent = createCameraPhotoIntent(activity, fileChooserParams);
        } else {
            // 其他， 显示系统弹窗，让用户选择相机、摄像及文件
            Log.i(TAG, "createCameraVideoFileIntent.");
            intent = createCameraVideoFileIntent(activity, fileChooserParams);
        }

        return intent;
    }

    /**
     * acceptTypes 仅仅包含image/*
     * @param acceptTypes
     * @return
     */
    private static boolean onlyImageAcceptType(String []acceptTypes){
        if(acceptTypes == null){
            return false;
        }

        String acceptType = (acceptTypes.length == 1) ? acceptTypes[0] : "";
        if (!TextUtils.isEmpty(acceptType) && acceptType.startsWith("image")) {
            return true;
        }
        return false;
    }

    /**
     * 创建一个能进入 拍照、摄像、文件管理器的弹窗intent
     * @return
     */
    private static Intent createCameraVideoFileIntent(Activity activity, WebChromeClient.FileChooserParams fileChooserParams) {
        Intent fileChooseIntent = createFileChooseIntent(fileChooserParams);

        ArrayList<Parcelable> extraIntents = new ArrayList<>();
        // 添加拍照
        Intent cameraIntent = createCameraIntent(activity);
        Uri cameraPhotoUri = cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        extraIntents.add(cameraIntent);

        // 添加摄像
        Intent videoIntent = createVideoIntent(activity);
        Uri videoPhotoUri = videoIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        extraIntents.add(videoIntent);

        // 弹窗intent
        Intent resultIntent = new Intent(Intent.ACTION_CHOOSER);
        resultIntent.putExtra(Intent.EXTRA_INTENT, fileChooseIntent);
        resultIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new Parcelable[]{}));
        resultIntent.putExtra(WebViewUploadFileActivity.INTENT_CAMERA_PHOTO_URI, cameraPhotoUri);
        resultIntent.putExtra(WebViewUploadFileActivity.INTENT_VIDEO_PHOTO_URI, videoPhotoUri);

        return resultIntent;
    }

    /**
     * 创建一个能进入 拍照、相册的弹窗
     * @param activity
     * @param fileChooserParams
     * @return
     */
    private static Intent createCameraPhotoIntent(Activity activity, WebChromeClient.FileChooserParams fileChooserParams) {
        Intent photoChooseIntent = createPhotoChooseIntent(fileChooserParams);

        ArrayList<Parcelable> extraIntents = new ArrayList<>();
        // 添加拍照
        Intent cameraIntent = createCameraIntent(activity);
        Uri cameraPhotoUri = cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        extraIntents.add(cameraIntent);

        // 弹窗intent
        Intent resultIntent = new Intent(Intent.ACTION_CHOOSER);
        resultIntent.putExtra(Intent.EXTRA_INTENT, photoChooseIntent);
        resultIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new Parcelable[]{}));
        resultIntent.putExtra(WebViewUploadFileActivity.INTENT_CAMERA_PHOTO_URI, cameraPhotoUri);

        return resultIntent;
    }

    private static Intent createFileChooseIntent(WebChromeClient.FileChooserParams fileChooserParams) {
        Intent intent = fileChooserParams.createIntent();
        intent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
        if (TextUtils.isEmpty(intent.getType())) {
            intent.setType("*/*");
        }

        if (fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) {
            // 支持多个文件选择
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        return intent;
    }

    private static Intent createPhotoChooseIntent(WebChromeClient.FileChooserParams fileChooserParams) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        if (fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) {
            // 支持多个文件选择
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        return intent;
    }

    private static Intent createVideoIntent(Activity activity){

        boolean isCameraAvailable = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        if (!isCameraAvailable) {
            Log.i(TAG, "camera unavailable");
            return null;
        }

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = createFile(activity, "mp4");
        if (videoFile == null) {
            Log.i(TAG, "createVideoFile failed.");
            return null;
        }

        Uri videoUri = createFileUri(activity, videoFile);
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        videoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        return videoIntent;
    }

    private static Intent createCameraIntent(Activity activity) {

        boolean isCameraAvailable = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        if (!isCameraAvailable) {
            Log.i(TAG, "camera unavailable");
            return null;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    // 拍照

        File cameraFile = createFile(activity, "jpg");
        if (cameraFile == null) {
            Log.i(TAG, "createImageFile failed.");
            return null;
        }

        Uri cameraUri = createFileUri(activity, cameraFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        return cameraIntent;
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片
     */
    private static Uri createFileUri(Activity activity, File file) {
        String authority = activity.getApplicationContext().getPackageName() + ".provider";
        return FileProvider.getUriForFile(activity, authority, file);
    }

    /**
     * 创建保存图片的文件
     */
    private static File createFile(Activity activity, String fileType) {
        try {
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + fileType;

            // getCacheDir will auto-clean according to android docs
            File fileDir = activity.getCacheDir();
            File file = new File(fileDir, filename);
            file.createNewFile();
            return file;
        } catch (Exception e) {
            Log.e(TAG, "createImageFile error." + e.toString());
            return null;
        }
    }

    public static boolean hasReadUriPermission(Activity activity, Uri uri){
        if(uri == null){
            return false;
        }

        if(activity == null){
            return false;
        }

        boolean result = true;
        try {
            int permission = activity.checkUriPermission(uri, Process.myPid(), Process.myUid(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.i("lvjielvjie", "getScheme: "+uri.getScheme()+"  permission: "+permission);



            ParcelFileDescriptor parcelFileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r");
            parcelFileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("lvjielvjie", e.toString());
            result = false;
        }
        Log.i("lvjielvjie", "getScheme: "+uri.getScheme()+"  getPath: "+uri.getPath()+"  getAuthority: "+uri.getAuthority()+"  toString: "+uri.toString());

        return result;
    }

}
