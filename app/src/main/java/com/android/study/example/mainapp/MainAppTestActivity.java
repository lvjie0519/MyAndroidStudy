package com.android.study.example.mainapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

public class MainAppTestActivity extends Activity {

    private static final String TAG = "lvjie-MainAppTestActivity";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainAppTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app_test);

        Log.i(TAG,"onCreate..."+this.getTaskId());
    }

    public void btnOnclickStartSubApp(View view) {

        String url = "scheme://subapp/test_activity";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}