package com.android.study.example.books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadTestActivity extends AppCompatActivity {

    private static final String TAG = "ThreadTestActivity";

    private ScheduledExecutorService mTimer;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ThreadTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_test);
    }

    public void onClickStartTimer(View view) {
        startTimer();
    }

    public void onClickStopTimer(View view) {
        stopTimer();
    }

    private static int sCount = 0;
    private void startTimer() {
        Log.i(TAG, "threadName: "+Thread.currentThread().getName()+"  sCount: " + (sCount++));
        if (mTimer == null) {
            // 创建一个定长线程池，支持定时及周期性任务执行,   定期执行，可以用来做定时器
            mTimer = Executors.newScheduledThreadPool(2);
            mTimer.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "threadName: "+Thread.currentThread().getName()+"  sCount: " + (sCount++));
                    try {
                        // 会等三秒才执行一次
                        Thread.sleep(2000);
                        // 会等1.2秒 才执行一次
//                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);  //  MILLISECONDS  毫秒
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.shutdownNow();
            mTimer = null;
        }
    }
}