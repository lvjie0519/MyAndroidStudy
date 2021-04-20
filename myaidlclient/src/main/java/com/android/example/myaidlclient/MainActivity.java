package com.android.example.myaidlclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.example.myaidlclient.outapp.ExportedActivity;
import com.android.example.myaidlclient.outapp.OutAppTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("lvjie", "aidl app MainActivity onCreate task id is "+getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie", "aidl app MainActivity onStart task id is "+getTaskId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie", "aidl app MainActivity onResume task id is "+getTaskId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie", "aidl app MainActivity onStop task id is "+getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "aidl app MainActivity onDestroy task id is "+getTaskId());
    }

    public void btnTestAidl(View view){
        Intent intent  = new Intent(this, AidlClientTestMainActivity.class);
        startActivity(intent);
    }

    public void btnTestOutApp(View view){
        OutAppTestActivity.startActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie", "aidl app MainActivity onNewIntent task id is "+getTaskId());
    }

    public void btnTestOpenExportedActivity(View view) {
        ExportedActivity.startActivity(this);
    }
}
