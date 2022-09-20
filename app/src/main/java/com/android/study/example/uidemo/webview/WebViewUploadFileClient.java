package com.android.study.example.uidemo.webview;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;

public class WebViewUploadFileClient extends WebChromeClient {
    public ValueCallback<Uri> UploadMsg;
    public ValueCallback<Uri[]> UploadMsg2;
    private Activity activity;

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



    // <input type="file" name="fileField" id="fileField" />

    // Android > 4.1.1

    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {

        Log.i("lvjie", "1. onShowFileChooser.");
        UploadMsg2 = filePathCallback;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");
//        intent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                && fileChooserParams.getAcceptTypes().length > 0) {
//            intent.setType(fileChooserParams.getAcceptTypes()[0]);

        } else {
//            intent.setType("*/*");
        }
        boolean isMulti = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;
        if(isMulti){
            // 支持多个文件选择
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        this.activity.startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"),this.FILECHOOSER_RESULTCODE);


//        boolean isMulti = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setType("*/*");
//        if (isMulti) {
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        }
//        this.activity.startActivityForResult(Intent.createChooser(intent, "FileChooser"), FILECHOOSER_RESULTCODE);

        return true;
    }

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg,

                                String acceptType, String capture) {

        Log.i("lvjie", "2. onShowFileChooser.");
        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);

    }

    // 3.0 +

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);
        Log.i("lvjie", "3. onShowFileChooser.");
    }

    // Android < 3.0

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);
        Log.i("lvjie", "4. onShowFileChooser.");
    }

    private Intent createDefaultOpenableIntent() {

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        i.addCategory(Intent.CATEGORY_OPENABLE);

        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

        /*
         *
         * ,
         *
         * createCamcorderIntent
         *
         * (),
         *
         * createSoundRecorderIntent
         *
         * ()
         */
        Intent chooser = createChooserIntent(createCameraIntent());

        chooser.putExtra(Intent.EXTRA_INTENT, i);

        return chooser;

    }

    private Intent createChooserIntent(Intent... intents) {

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);

        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");

        return chooser;

    }

    @SuppressWarnings("static-access")
    private Intent createCameraIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File externalDataDir = Environment

                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File cameraDataDir = new File(externalDataDir.getAbsolutePath()

                + File.separator + "515aaa");

        cameraDataDir.mkdirs();

        String mCameraFilePath = cameraDataDir.getAbsolutePath()

                + File.separator + System.currentTimeMillis() + ".jpg";

        this.mCameraFilePath = mCameraFilePath;

        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,

                Uri.fromFile(new File(mCameraFilePath)));

        return cameraIntent;

    }

    /*
     *
     * private Intent createCamcorderIntent() { return new
     *
     * Intent(MediaStore.ACTION_VIDEO_CAPTURE); }
     *
     *
     *
     * private Intent createSoundRecorderIntent() { return new
     *
     * Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION); }
     */

    public static Uri getImageContentUri(Context context, java.io.File imageFile) {

        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(

                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                new String[] { MediaStore.Images.Media._ID },

                MediaStore.Images.Media.DATA + "=? ",

                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {

            int id = cursor.getInt(cursor

                    .getColumnIndex(MediaStore.MediaColumns._ID));

            Uri baseUri = Uri.parse("content://media/external/images/media");

            return Uri.withAppendedPath(baseUri, "" + id);

        } else {

            if (imageFile.exists()) {

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATA, filePath);

                return context.getContentResolver().insert(

                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } else {

                return null;

            }

        }

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
}
