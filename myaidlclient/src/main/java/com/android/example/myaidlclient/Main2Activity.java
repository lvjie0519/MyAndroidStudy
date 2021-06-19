package com.android.example.myaidlclient;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
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
        openHostAppForIntentFlag();
    }

    private void openHostAppForIntentFlag(){
        Intent hostAppIntent = getPackageManager().getLaunchIntentForPackage("com.android.study.example");
        hostAppIntent.getCategories().clear();
        hostAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        hostAppIntent.setPackage("");
        Intent subAppIntent = getBaseIntent();
        if (subAppIntent != null) {
            //去掉FLAG_ACTIVITY_MULTIPLE_TASK 标记
            subAppIntent.setFlags(subAppIntent.getFlags() & (~Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
        }
        subAppIntent.putExtra("EXTRA_HOSTAPP_GO_BACK_CLASS_NAME", this.getIntent().getComponent().getClassName());
        subAppIntent.putExtra("EXTRA_HOSTAPP_GO_BACK_PACKAGE_NAME", this.getIntent().getComponent().getPackageName());

        // 0、hostAppIntent 默认是 Intent.FLAG_ACTIVITY_NEW_TASK

        // 1、Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP  等价于singleTask
//        hostAppIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 2、
        hostAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        hostAppIntent.putExtra("EXTRA_HOSTAPP_GO_BACK_BASE_INTENT", subAppIntent);



        Intent intent1 = new Intent();
        intent1.setComponent(hostAppIntent.getComponent());
        intent1.setAction(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent1);
    }

    public void btnOpenMySelf(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.example.myaidlclient", "com.android.example.myaidlclient.Main2Activity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("host_key", "host_value");
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

    public void btnCheckAppProcess(View view) {

        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos =
                am.getRunningAppProcesses();
        String mainProcessName = "com.android.study.example";
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            Log.i("lvjie", "processName: " + info.processName);
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                Log.i("lvjie", "find process ");
                break;
            }
        }

    }

    public void btnLauncherMainActivity(View view) {
        MainActivity.startActivity(this);
    }
}