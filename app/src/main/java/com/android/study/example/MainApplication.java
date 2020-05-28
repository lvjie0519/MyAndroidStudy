package com.android.study.example;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initGlobalCrash();
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
