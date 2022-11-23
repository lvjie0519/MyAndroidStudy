package com.android.study.example.baseinterface;

public interface PermissionListener {
    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
