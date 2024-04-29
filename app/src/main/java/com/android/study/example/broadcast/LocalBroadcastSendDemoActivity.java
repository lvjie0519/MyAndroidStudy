package com.android.study.example.broadcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.uidemo.photoview.demo.PhotoViewTestActivity;

/**
 * 本地广播发送
 */
public class LocalBroadcastSendDemoActivity extends Activity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LocalBroadcastSendDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_broadcast_send_demo);
        Log.i("lvjielvjie", "LocalBroadcastSendDemoActivity onCreate...");
    }

    public void onClickSendBroadcastMsg(View view) {
//        Intent intent = new Intent("action.com.android.study.example.broadcast");
//        intent.setPackage(getPackageName());        // 添加包名，则只有相同包名的进程可以收到广播信息
//        sendBroadcast(intent);

        Log.i("lvjielvjie", "LocalBroadcastSendDemoActivity onCreate...");
        PhotoViewTestActivity.startActivity(this);
    }

    public void onClickReStartApp(View view) {
//        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

        Intent intent = new Intent("action.com.android.study.example.broadcast");
        intent.setPackage(getPackageName());        // 添加包名，则只有相同包名的进程可以收到广播信息
        sendBroadcast(intent);

        //杀掉以前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static int count = 0;
    public void onClickSendBroadcastMsg01(View view) {
        String action = "action.demo01";
        int dif = count % 3;
        if(dif == 0){
            action = "action.demo01";
        }else if(dif == 1){
            action = "action.demo02";
        }else if(dif == 2){
            action = "action.demo03";
        }
        count++;
        Log.i("lvjie", "sendBroadcast, action: "+action);
        Intent intent = new Intent(action);
        intent.setPackage(getPackageName());        // 添加包名，则只有相同包名的进程可以收到广播信息
        sendBroadcast(intent);
    }
}