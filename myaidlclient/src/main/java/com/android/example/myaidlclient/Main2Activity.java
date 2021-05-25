package com.android.example.myaidlclient;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main2Activity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, Main2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.i("lvjie", "aidl app Main2Activity onCreate task id is "+getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie", "aidl app Main2Activity onStart task id is "+getTaskId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie", "aidl app Main2Activity onNewIntent task id is "+getTaskId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie", "aidl app Main2Activity onResume task id is "+getTaskId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("lvjie", "aidl app Main2Activity onActivityResult task id is "+getTaskId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie", "aidl app Main2Activity onStop task id is "+getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "aidl app Main2Activity onDestroy task id is "+getTaskId());
    }

    // 打开主应用
    public void btnOpenHostApp(View view) {
        Intent hostAppIntent = getPackageManager().getLaunchIntentForPackage("com.android.study.example");
        Intent subAppIntent = getBaseIntent();
        if (subAppIntent != null) {
            //去掉FLAG_ACTIVITY_MULTIPLE_TASK 标记
            subAppIntent.setFlags(subAppIntent.getFlags() & (~Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
        }
        hostAppIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        hostAppIntent.putExtra("EXTRA_HOSTAPP_GO_BACK_BASE_INTENT", subAppIntent);
        startActivity(hostAppIntent);
    }

    public void btnOpenMySelf(View view) {
        Intent intent = getBaseIntent();
        startActivity(intent);
    }

    private Intent getBaseIntent(){
        Intent intent = null;
        final int taskId = this.getTaskId();
        if (taskId < 0) {
            Log.i("lvjie", "taskId invalid: " + taskId);
            return null;
        }

        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= 21) {
            Log.i("lvjie", "trying getAppTasks...");
            List<ActivityManager.AppTask> appTasks = am.getAppTasks();
            if (appTasks != null) {
                Log.i("lvjie","getAppTasks return not null");
                for (ActivityManager.AppTask task : appTasks) {
                    ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                    if (info != null && info.id == taskId) {
                        intent = info.baseIntent;
                        break;
                    }
                }
            }
        }
        if (intent != null) {
            return intent;
        }

        List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(100, ActivityManager.RECENT_WITH_EXCLUDED);
        if (recentTasks != null) {
            Log.i("lvjie", "getRecentTasks return not null");

            for (ActivityManager.RecentTaskInfo info : recentTasks) {
                if (info.id == taskId) {
                    intent = info.baseIntent;
                    break;
                }
            }
        }

        return intent;
    }
}