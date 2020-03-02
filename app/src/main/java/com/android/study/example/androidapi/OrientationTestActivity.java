package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

/**
 * Android 8.0 横屏测试
 */
public class OrientationTestActivity extends FragmentActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OrientationTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_test);

        initView();
    }

    private void initView(){

        findViewById(R.id.btn_screen_h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });

        findViewById(R.id.btn_screen_v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        });

    }
}
