package com.android.example.myaidlclient.outapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.example.myaidlclient.MainActivity;
import com.android.example.myaidlclient.R;

public class OutAppTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OutAppTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_app_test);

        Log.i("lvjie", "OutAppTestActivity onCreate task id is "+getTaskId());
    }

    public void btnGoMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie", "OutAppTestActivity onNewIntent task id is "+getTaskId());
    }
}
