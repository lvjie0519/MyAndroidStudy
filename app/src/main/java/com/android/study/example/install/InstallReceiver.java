package com.android.study.example.install;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class InstallReceiver extends BroadcastReceiver {
    private static final String TAG = "MyInstallReceiver";
    private static final String SCHEME_PACKAGE_HEADER = "package:";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive, action: " + intent.getAction());
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
        }
    }


    public static void registerReceiver(Context context) {
        Log.i(TAG, "registerReceiver call");
        InstallReceiver myInstallReceiver = new InstallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        context.registerReceiver(myInstallReceiver, intentFilter);
    }
}
