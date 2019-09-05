package com.android.study.example.books.jjzg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.books.jjzg.chapter2.Chapter2TestActivity;

public class JjzgMainTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, JjzgMainTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjzg_main_test);
    }

    public void chapter2MainTest(View view){
        Chapter2TestActivity.startActivity(this);
    }
}
