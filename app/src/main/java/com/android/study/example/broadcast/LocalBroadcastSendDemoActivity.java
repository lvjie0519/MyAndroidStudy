package com.android.study.example.broadcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
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
    }

    public void onClickSendBroadcastMsg(View view) {
        Intent intent = new Intent("action.com.android.study.example.broadcast");
        sendBroadcast(intent);
    }
}