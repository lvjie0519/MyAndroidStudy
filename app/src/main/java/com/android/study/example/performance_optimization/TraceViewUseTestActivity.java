package com.android.study.example.performance_optimization;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

public class TraceViewUseTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, TraceViewUseTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_view_use_test);
    }

    public void onClickFuncTimeCost(View view) {
        /**
         * 1、打开 Android studio Profiler
         * 2、添加startMethodTracing 和 stopMethodTracing代码；
         * 3、当点击按钮时， 会看到自动生成一个文件， 会显示api的调用及耗时；
         * 文件位于 sdcard/Android/包名/files 文件夹下
         */
        Debug.startMethodTracing("lvjie.trace");
        Log.i("lvjie", "startMethodTracing...");
        func1();
        func2();
        func3();
        Debug.stopMethodTracing();
        Log.i("lvjie", "stopMethodTracing...");
    }

    public void func1(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        func2();
    }
    public void func2(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void func3(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                func1();
                func2();
            }
        }).start();
    }

    public void onClickTraceMethod(View view) {

        Trace.beginSection("lvjieTrace");
        Log.i("lvjie", "start onClickTraceMethod...");
        func1();
        func2();
        Trace.endSection();
        Log.i("lvjie", "stop onClickTraceMethod...");

    }
}
