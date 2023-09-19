package com.mx.filepicker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int RC_READ_EXTERNAL_STORAGE = 123;
    private static final String TAG = BaseActivity.class.getName();

    protected FolderListHelper mFolderHelper;
    protected boolean isNeedFolderList;
    public static final String IS_NEED_FOLDER_LIST = "isNeedFolderList";

    abstract void permissionGranted();

    public static void startActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isNeedFolderList = getIntent().getBooleanExtra(IS_NEED_FOLDER_LIST, false);
        if (isNeedFolderList) {
            mFolderHelper = new FolderListHelper();
            mFolderHelper.initFolderListView(this);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        readExternalStorage();
    }

    private void readExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted();
            return;
        }

        String permission = "android.permission.READ_EXTERNAL_STORAGE";
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission)  == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            permissionGranted();
        } else {
            String[] permissions = {permission};
            requestPermissions(permissions, RC_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != RC_READ_EXTERNAL_STORAGE) {
            return;
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

        if (allGrant) {
            permissionGranted();
        } else {
            // 没有给权限，则界面退出
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // todo 是否需要处理 从设置界面的返回
    }

    public void onBackClick(View view) {
        finish();
    }
}
