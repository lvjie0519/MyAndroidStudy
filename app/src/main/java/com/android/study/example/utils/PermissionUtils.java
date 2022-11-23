package com.android.study.example.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.study.example.BaseActivity;
import com.android.study.example.baseinterface.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    /**
     * 检查当前app是否有权限
     *
     * @param activity    仅支持BaseActivity
     * @param permissions
     * @param callback
     */
    public static void checkToRequestPermissions(BaseActivity activity, String[] permissions, final RequestPermissionsCallback callback) {

        if (callback == null) {
            Log.w(TAG, "not checkToRequestPermissions, callback is null.");
            return;
        }

        if (permissions == null || permissions.length == 0) {
            Log.w(TAG, "not checkToRequestPermissions, permissions is null or empty.");
            callback.onPermissionCheckResult(true);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // android 6.0 之前没有动态申请权限
            Log.i(TAG, "current system version is less than android 6.0");
            callback.onPermissionCheckResult(true);
            return;
        }

        // 当前对 MainActivity 支持权限申请
        BaseActivity mainActivity = null;
        if (activity instanceof BaseActivity) {
            mainActivity = (BaseActivity) activity;
        }

        if (mainActivity == null) {
            Log.e(TAG, "mainActivity is null, activity is null or not MainActivity, activity: " + activity);
            callback.onPermissionCheckResult(false);
            return;
        }

        List<String> permissionsList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, permission + " not granted.");
                permissionsList.add(permission);
            }
        }

        if (permissionsList.isEmpty()) {
            Log.i(TAG, "all permission granted.");
            callback.onPermissionCheckResult(true);
            return;
        }

        Log.i(TAG, permissionsList.toString() + " will requestPermissions.");
        permissions = permissionsList.toArray(new String[permissionsList.size()]);
        mainActivity.requestPermissions(permissions, BaseActivity.PERMISSION_REQUESTCODE, new PermissionListener() {
            @Override
            public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

                Log.i(TAG, "onRequestPermissionsResult, requestCode: " + requestCode);
                if (requestCode != BaseActivity.PERMISSION_REQUESTCODE) {
                    return false;
                }

                boolean allGrant = true;
                int len = grantResults.length;
                for (int i = 0; i < len; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "permission: "+permissions[i]+" not permit.");
                        allGrant = false;
                        break;
                    }
                }

                callback.onPermissionCheckResult(allGrant);
                return allGrant;
            }
        });

    }

    public interface RequestPermissionsCallback {
        void onPermissionCheckResult(boolean result);
    }
}
