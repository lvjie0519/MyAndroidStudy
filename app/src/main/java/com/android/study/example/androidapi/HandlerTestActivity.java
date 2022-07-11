package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

public class HandlerTestActivity extends AppCompatActivity {

    private static final String TAG = "HandlerTestActivity";
    private HandlerThread mHandlerThread = null;
    private Handler mWorkHandler = null;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HandlerTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);
    }

    public void onClickInitHandler(View view) {
        initWorkHandler();
    }

    private void initWorkHandler() {
        Log.i(TAG, "initWorkHandler called");
        if(mHandlerThread == null){
            mHandlerThread = new HandlerThread("HandlerTestActivityHandler");
            mHandlerThread.start();
            mWorkHandler = new Handler(mHandlerThread.getLooper());
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "exec mRunnable run...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "exec mRunnable run, will remove mRunnable...");
            /**
             * 队列里面有mRunnable， 则会清除掉
             */
            mWorkHandler.removeCallbacks(mRunnable);
        }
    };

    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "exec mRunnable2 run...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public void onClickTestSendMsg(View view) {
        for(int i=0; i<10; i++){
            Log.i(TAG, "post(mRunnable)  i="+i);
            // mRunnable, mRunnable2 的run方法都是在同一个线程中执行的， 因此run方法里面的睡眠会影响另外的run方法执行
            mWorkHandler.post(mRunnable);
            mWorkHandler.post(mRunnable2);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}