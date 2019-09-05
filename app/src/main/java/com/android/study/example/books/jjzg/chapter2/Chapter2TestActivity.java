package com.android.study.example.books.jjzg.chapter2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.study.example.R;

public class Chapter2TestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, Chapter2TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charpter2_test);
    }
}
