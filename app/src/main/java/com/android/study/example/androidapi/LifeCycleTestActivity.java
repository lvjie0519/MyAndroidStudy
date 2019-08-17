package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.study.example.R;

public class LifeCycleTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LifeCycleTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle_test);
        Log.i("lvjie","onCreate...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie","onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie","onResume...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lvjie","onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie","onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie","onDestroy...");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lvjie","onRestart...");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("lvjie","onAttachedToWindow...");
    }

}