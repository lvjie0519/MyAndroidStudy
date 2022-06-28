package com.android.study.example.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

/**
 * 本地广播注册
 */
public class LocalBroadcastRegisterDemoActivity extends Activity {

    private SelfDefineBroadcast mSelfDefineBroadcast;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LocalBroadcastRegisterDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_broadcast_register_demo);

        registerBroadcast();

        // 测试多次注册相同的reviever， action不一样， 会不会被覆盖
        initBrodcast();
    }

    private void initBrodcast(){
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("action.demo01");
        CommonGlobalReceiverManager.getInstance().registerCommonGlobalReceiver(intentFilter1, listener);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("action.demo02");
        CommonGlobalReceiverManager.getInstance().registerCommonGlobalReceiver(intentFilter2, listener);

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("action.demo03");
        CommonGlobalReceiverManager.getInstance().registerCommonGlobalReceiver(intentFilter3, listener);
    }
    private CommonGlobalReceiverManager.GlobalReceiverListener listener = new CommonGlobalReceiverManager.GlobalReceiverListener() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("lvjie", "GlobalReceiverListener onReceive call, action: "+intent.getAction());
        }
    };

    private void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.com.android.study.example.broadcast");
        mSelfDefineBroadcast = new SelfDefineBroadcast();
        //注册广播
        registerReceiver(mSelfDefineBroadcast,intentFilter);
    }


    private void unRegisterBroadcast(){
        if(mSelfDefineBroadcast != null){
            unregisterReceiver(mSelfDefineBroadcast);
        }
    }

    public void onClickOpenBroadcastSendPage(View view) {
        LocalBroadcastSendDemoActivity.startActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    private class SelfDefineBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("lvjie", "1-LocalBroadcastRegisterDemoActivity receive broadcast: "+intent.getAction());
        }
    }
}