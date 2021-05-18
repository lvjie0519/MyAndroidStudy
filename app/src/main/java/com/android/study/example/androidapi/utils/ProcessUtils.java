package com.android.study.example.androidapi.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ProcessUtils {

    public static boolean checkAppRunningWithPkg(Context context, String packageName){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    // 表明程序在运行中
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
