package com.android.example.myaidlclient.outapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.example.myaidlclient.R;

public class ExportedActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ExportedActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exported);
    }
}