package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.study.example.androidapi.views.NetworkErrorView;

public class ViewTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ViewTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new NetworkErrorView(this));

        Log.i("lvjie", "ViewTestActivity onCreate...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "ViewTestActivity onDestroy...");
    }
}