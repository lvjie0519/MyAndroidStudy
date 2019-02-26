package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.study.example.R;

public class AndroidApiTestActivity extends AppCompatActivity {

    private TextView tvShowInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AndroidApiTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alterDisplayMetrics();
        setContentView(R.layout.activity_android_api_test);

        initView();
    }

    private void initView(){
        tvShowInfo = (TextView) findViewById(R.id.tv_show_info);

        findViewById(R.id.btn_get_screen_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScreenInfos();
            }
        });

        findViewById(R.id.btn_alter_DisplayMetrics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterDisplayMetrics();
            }
        });

        findViewById(R.id.btn_go_next_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AndroidApiTestActivity.this, AndroidApiTestActivity.class));
//                keepScreenNotLock(true);
            }
        });
    }

    private void getScreenInfos(){
        StringBuilder stringBuilder = new StringBuilder();

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int pxWidth = dm.widthPixels;
        int pxHeight = dm.heightPixels;
        int dpi = dm.densityDpi;
        float density = dm.density;

        stringBuilder.append("屏幕像素宽度： ").append(pxWidth).append(" px\n")
                .append("屏幕像素高度： ").append(pxHeight).append("px \n")
                .append("屏幕dpi值： ").append(dpi).append("\n")
                .append("屏幕density(dpi/160)值： ").append(density).append("\n");

        tvShowInfo.setText(stringBuilder.toString());

        tvShowInfo.setTextSize(1);
        tvShowInfo.setMaxHeight(12);
    }

    public void alterDisplayMetrics(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.density = 2.13333334f;
        displayMetrics.densityDpi = 341;
        displayMetrics.scaledDensity = 2.13333334f;
        displayMetrics.xdpi = 320;
    }

    public void keepScreenNotLock(boolean light) {
        if (light) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

}
