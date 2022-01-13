package com.android.example.myaidlclient.outapp;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.example.myaidlclient.R;

import java.util.List;

public class SubAppTestActivity extends Activity {

    private static final String TAG = "lvjie-SubAppTestActivity";
    private SelfDefineBroadcast mSelfDefineBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_app_test);

        Log.i(TAG,"onCreate..."+this.getTaskId());
        registerBroadcast();
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume...");
    }

    public void btnOnClickStartMainApp(View view) {
        Intent mainAppIntent = getMainAppIntent();
        Intent baseIntent = getCurrentBaseIntent();

//        Intent intent = new Intent("action.com.android.study.example.broadcast");
//        intent.putExtra("task_id", this.getTaskId());
//        intent.putExtra("sub_intent", baseIntent);
//        intent.putExtra("sub_app_package", this.getPackageName());
//        sendBroadcast(intent);

//        startActivity(mainAppIntent);
        Intent intent = new Intent(this, SubAppTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Intent getMainAppIntent(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.study.example");
        return intent;
    }

    private Intent getCurrentBaseIntent(){

        Intent intent = null;
        int taskId = this.getTaskId();

        if (taskId < 0) {
            return null;
        }

        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.AppTask> appTasks = am.getAppTasks();
            if (appTasks != null) {
                for (ActivityManager.AppTask task : appTasks) {
                    ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                    if (info != null && info.id == taskId) {
                        intent = info.baseIntent;
                        break;
                    }
                }
            }
        }

        if(intent == null){
            List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(100, ActivityManager.RECENT_WITH_EXCLUDED);
            if (recentTasks != null) {
                for (ActivityManager.RecentTaskInfo info : recentTasks) {
                    if (info.id == taskId) {
                        intent = info.baseIntent;
                        break;
                    }
                }
            }
        }

        return intent;
    }





    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy...");
        if(mSelfDefineBroadcast != null){
            unregisterReceiver(mSelfDefineBroadcast);
        }
    }


    private class SelfDefineBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("lvjie", "1-receive broadcast: "+intent.getAction());

        }
    }
}