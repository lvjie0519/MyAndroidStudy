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
 * 动态换肤
 */
public class OrientationTestActivity extends FragmentActivity {

    private int mThemeId = -1;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OrientationTestActivity.class);
        intent.putExtra("theme", R.style.ActivityTheme);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //android:theme="@android:style/Theme.Translucent.NoTitleBar"
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("theme", -1) != -1) {// 读取皮肤主题ID，-1 不处理
                mThemeId = savedInstanceState.getInt("theme");
                this.setTheme(mThemeId);  //设置主题皮肤
            }
        }

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

        findViewById(R.id.btn_dy_theme_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTheme(R.style.Theme_Design_Light);
            }
        });

        findViewById(R.id.btn_dy_theme_night).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTheme(R.style.Theme_Design);
            }
        });

    }

    // 设置主题，并重建
    private void onTheme(int iThemeId){
        mThemeId = iThemeId;
        this.recreate();
    }

    // 保存主题ID，onCreate 时读取主题
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mThemeId);
    }
}
