package com.android.example.myaidlclient;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.example.myaidlclient.outapp.ExportedActivity;
import com.android.example.myaidlclient.outapp.OutAppTestActivity;

public class MainActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("lvjie", "aidl app MainActivity onCreate task id is "+getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie", "aidl app MainActivity onStart task id is "+getTaskId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie", "aidl app MainActivity onResume task id is "+getTaskId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie", "aidl app MainActivity onStop task id is "+getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "aidl app MainActivity onDestroy task id is "+getTaskId());
    }

    public void btnTestAidl(View view){
        Intent intent  = new Intent(this, AidlClientTestMainActivity.class);
        startActivity(intent);
    }

    public void btnTestOutApp(View view){
        OutAppTestActivity.startActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie", "aidl app MainActivity onNewIntent task id is "+getTaskId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && data.getData() != null){
            Log.i("lvjie", "aidl app MainActivity onActivityResult task id is "+getTaskId()
                    +"  requestCode: "+requestCode
                    +"  resultCode: "+resultCode
                    +"  getData: "+data.getData());
        }else{
            Log.i("lvjie", "aidl app MainActivity onActivityResult task id is "+getTaskId()
                    +"  requestCode: "+requestCode
                    +"  resultCode: "+resultCode);
        }

    }

    public void btnTestOpenExportedActivity(View view) {
        ExportedActivity.startActivity(this);
    }

    public void btnOpenMain2Activity(View view) {
        Main2Activity.startActivity(this);
    }


    public void btnLauncherSelf(View view) {
//        MainActivity.startActivity(this);


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"),200);

            /**
             * 一般使用 ACTION_PICK 选择图片，使用 ACTION_GET_CONTENT 或 ACTION_OPEN_DOCUMENT 选择文件。
             */
//        // 仅仅只会选择图片
//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intentToPickPic, 200);


    }
}
