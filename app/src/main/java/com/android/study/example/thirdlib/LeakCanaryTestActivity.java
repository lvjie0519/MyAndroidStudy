package com.android.study.example.thirdlib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.squareup.leakcanary.LeakCanary;

public class LeakCanaryTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LeakCanaryTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_canary_test);
    }

    public void onClickDivide(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyLeakCanaryInstance.getInstance(LeakCanaryTestActivity.this);
                try {
                    Thread.sleep(6000);
                    Log.i("LeakCanary", "(LeakCanaryTestActivity.this == null)= "+(LeakCanaryTestActivity.this == null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClickInitLeakCanary(View view) {
        LeakCanary.install(this.getApplication());
    }


    private static class MyLeakCanaryInstance{

        private static MyLeakCanaryInstance instance;
        private Context context;

        private MyLeakCanaryInstance(Context context) {
            this.context = context;
        }

        public static MyLeakCanaryInstance getInstance(Context context){
            if(instance == null){
                instance = new MyLeakCanaryInstance(context);
            }
            return instance;
        }
    }
}
