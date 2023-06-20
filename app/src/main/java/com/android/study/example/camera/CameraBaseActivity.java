package com.android.study.example.camera;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class CameraBaseActivity extends Activity {

    protected void showLog(String tag, String message){
        Log.i(tag, message);
    }

    protected void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CameraBaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
