package com.android.study.example.books.jjzg;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.android.study.example.R;
import com.android.study.example.books.jjzg.chapter2.Chapter2TestActivity;

public class JjzgMainTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, JjzgMainTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjzg_main_test);
        initWindow();
    }

    private void initWindow(){
        getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON          // 点亮屏幕
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD         // 解锁
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);      // 保持屏幕不息屏

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);              //这个在锁屏状态下
        }
    }

    public void chapter2MainTest(View view){
        Chapter2TestActivity.startActivity(this);
    }
}
