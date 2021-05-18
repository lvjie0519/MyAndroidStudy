package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }
}