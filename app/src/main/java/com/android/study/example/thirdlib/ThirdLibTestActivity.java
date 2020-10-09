package com.android.study.example.thirdlib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

/**
 * 第三方库的测试
 */
public class ThirdLibTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ThirdLibTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_lib_test);
    }

    public void onCLickLeakCanary(View view) {
        LeakCanaryTestActivity.startActivity(this);
    }
}
