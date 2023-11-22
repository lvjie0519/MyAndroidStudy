package com.android.study.example;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

import com.android.study.example.broadcast.CommonGlobalReceiverManager;
import com.android.study.example.broadcast.LocalBroadcastRegisterDemoActivity;
import com.android.study.example.broadcast.SelfDefineBroadcast;
import com.android.study.example.install.InstallReceiver;
import com.squareup.leakcanary.LeakCanary;


public class MainApplication extends Application {

    private SelfDefineBroadcast mSelfDefineBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();

//        initGlobalCrash();
//        initLeakCanary();
        registerBroadcast();

        InstallReceiver.registerReceiver(this);
        CommonGlobalReceiverManager.getInstance().init(this);
    }

    private void initLeakCanary(){
        if(LeakCanary.isInAnalyzerProcess(this)){
            Log.i("LeakCanary", "LeakCanary.isInAnalyzerProcess...");
            return;
        }
        Log.i("LeakCanary", "LeakCanary.install...");
        LeakCanary.install(this);
    }

    private void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.com.android.study.example.broadcast");
        if(mSelfDefineBroadcast == null){
            mSelfDefineBroadcast = new SelfDefineBroadcast();
        }
        //注册广播
        Log.i("lvjie", "will registerReceiver...");
        registerReceiver(mSelfDefineBroadcast,intentFilter);
    }

    /**
     * 全局crash 监控
     * https://mp.weixin.qq.com/s/DElEJx6D2Tt3D0qpX3MEOg
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
