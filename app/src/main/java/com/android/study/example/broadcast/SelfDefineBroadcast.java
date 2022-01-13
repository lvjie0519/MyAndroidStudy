package com.android.study.example.broadcast;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SelfDefineBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("lvjie", "2-receive broadcast: "+intent.getAction());
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        int taskId = intent.getIntExtra("task_id", -1);
//        if(taskId > 0){
//            am.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_NO_USER_ACTION);
//        }

//        Intent intent1 = intent.getParcelableExtra("sub_intent");
//        context.startActivity(intent1);


        Intent intent1 = new Intent("action.com.android.study.example.broadcast");
        intent1.setPackage(intent.getStringExtra("sub_app_package"));
        context.sendBroadcast(intent1);

    }
}
