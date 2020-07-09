package com.android.example.myaidlclient.outapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.example.myaidlclient.MainActivity;
import com.android.example.myaidlclient.R;

/**
 * 跨app跳转 中转activity
 */
public class OutCenterTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_center_transfer);

        Log.i("lvjie", "OutCenterTransferActivity task id is "+getTaskId());

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Uri uri = getIntent().getData();
        if(uri != null){
            Log.i("lvjie", "getScheme="+uri.getScheme()+"  getHost="+uri.getHost()+"  getPath="+uri.getPath());
        }
        OutAppTestActivity.startActivity(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
