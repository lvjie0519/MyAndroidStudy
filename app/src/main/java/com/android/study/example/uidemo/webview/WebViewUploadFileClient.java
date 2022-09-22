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
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class WebViewUploadFileClient extends WebChromeClient {
    public ValueCallback<Uri> UploadMsg;
    public ValueCallback<Uri[]> UploadMsg2;
    private Activity activity;
    private Uri cameraUri;

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
        Intent intent = createIntentByFileChooser(activity, fileChooserParams);
        activity.startActivityForResult(intent, FILECHOOSER_RESULTCODE);
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
    public Intent createIntentByFileChooser(Activity activity, FileChooserParams fileChooserParams) {

        if (fileChooserParams == null) {
            Log.i("lvjie", "can not createIntentByFileChooser, fileChooserParams  is null");
            return null;
        }

        if (activity == null) {
            Log.i("lvjie","can not createIntentByFileChooser, activity is null");
            return null;
        }

        if (fileChooserParams.isCaptureEnabled()) {
            // 打开相机
            Log.i("lvjie","createCameraIntent.");
            return createCameraIntent2();
        }

        Intent intent;

        String acceptType = (fileChooserParams.getAcceptTypes().length == 1) ? fileChooserParams.getAcceptTypes()[0] : "";
        if (!TextUtils.isEmpty(acceptType) && acceptType.startsWith("image")) {
            // 图片选择，跳转到相册
            Log.i("lvjie","createPhotoChooseIntent.");
            intent = createPhotoChooseIntent();
        } else {
            // 文件选择，跳转到文件管理器
            Log.i("lvjie","createFileChooseIntent.");
            intent = createFileChooseIntent(fileChooserParams);
        }

        if (fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) {
            // 支持多个文件选择
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        return intent;
    }

    private Intent createFileChooseIntent(FileChooserParams fileChooserParams) {
        Intent intent = fileChooserParams.createIntent();
        intent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
        if (TextUtils.isEmpty(intent.getType())) {
            intent.setType("*/*");
        }
        return intent;
    }

    private Intent createPhotoChooseIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }


    private Intent createCameraIntent() {

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
//                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            cameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        return captureIntent;
    }

    private Intent createCameraIntent2(){
        boolean isCameraAvailable = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        if(!isCameraAvailable){
            return null;
        }

        Intent cameraIntent;
//        cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);  // 录像
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    // 拍照

        File file = createFile(activity, "jpg");
        cameraUri = createUri(file, activity);

        Uri fileUri = Uri.fromFile(file);           // 用来删除
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        return cameraIntent;
    }

    public File createFile(Activity activity, String fileType) {
        try {
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date())+"."+fileType;

            // getCacheDir will auto-clean according to android docs
            File fileDir = activity.getCacheDir();
            File file = new File(fileDir, filename);
            file.createNewFile();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri createUri(File file, Activity activity) {
        String authority = activity.getApplicationContext().getPackageName() + ".provider";
        return FileProvider.getUriForFile(activity, authority, file);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
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

    public Uri getCameraUri() {
        Uri tempUri = cameraUri;
        cameraUri = null;
        return tempUri;
    }
}
