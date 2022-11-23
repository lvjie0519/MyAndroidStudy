package com.android.study.example;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.study.example.baseinterface.PermissionListener;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/16 0016.
 */

public abstract class BaseActivity extends Activity{

    public static final int PERMISSION_REQUESTCODE = 1002;
    protected PermissionListener mPermissionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        afterCreate(savedInstanceState);
    }

    public abstract void afterCreate(Bundle savedInstanceState);

    public abstract int getLayoutId();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionListener != null) {
            mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        mPermissionListener = null;
    }

    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        this.mPermissionListener = listener;

        if(Build.VERSION.SDK_INT < 23){
            // android 6.0以上才需要动态权限获取
            int len = permissions.length;
            int[] grantResults = new int[len];
            for(int i=0; i<len; i++){
                grantResults[i] = PackageManager.PERMISSION_GRANTED;
            }
            onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        requestPermissions(permissions, requestCode);
    }
}
