package com.android.study.example;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;


public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        initGlobalCrash();
        initLeakCanary();
    }

    private void initLeakCanary(){
        if(LeakCanary.isInAnalyzerProcess(this)){
            Log.i("LeakCanary", "LeakCanary.isInAnalyzerProcess...");
            return;
        }
        Log.i("LeakCanary", "LeakCanary.install...");
        LeakCanary.install(this);
    }

    /**
     * 全局crash 监控
     */
    private void initGlobalCrash(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                /**
                 * e.printStackTrace()  的内容 要比 e.toString  信息更多
                 */
                Log.i("lvjie", "GlobalCrash  threadName: "+t.getName()+"  error: "+e.toString());
                e.printStackTrace();
            }
        });
    }
}
