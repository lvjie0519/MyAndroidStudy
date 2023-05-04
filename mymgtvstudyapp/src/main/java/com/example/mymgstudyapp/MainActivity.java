package com.example.mymgstudyapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymgstudyapp.localnet.LocalNetScanActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickOpenLocalNetScanPage(View view) {
        LocalNetScanActivity.startActivity(this);
    }
}