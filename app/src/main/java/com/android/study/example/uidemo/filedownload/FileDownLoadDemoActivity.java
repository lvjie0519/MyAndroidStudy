package com.android.study.example.uidemo.filedownload;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.study.example.R;

/**
 * 文件上传及下载   支持断点续传
 */
public class FileDownLoadDemoActivity extends AppCompatActivity implements View.OnClickListener, DownLoad.IProgress  {

//    private String path = "http://video.dameiketang.com/mkt2016%2F%E9%83%91%E7%82%9C%E4%B8%9C%2F%E5%A4%B4%E7%9A%AE%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%981.mp4";
    private String path = "https://download.alicdn.com/wireless/taobao4android/latest/702757.apk";
    private ProgressBar pBar;
    private OkManager downLoad;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, FileDownLoadDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_down_load_demo);

        downLoad = new OkManager(path,this);
        initView();
    }

    private void initView() {
        Button start = (Button) findViewById(R.id.btn_start_down_load);
        Button stop = (Button) findViewById(R.id.btn_stop_down_load);
        pBar = (ProgressBar) findViewById(R.id.progress);
        pBar.setMax(100);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_down_load:
                downLoad.start();
                break;
            case R.id.btn_stop_down_load:
                downLoad.stop();
                break;
        }
    }

    @Override
    public void onProgress(int progress) {
        pBar.setProgress(progress);
    }
}
