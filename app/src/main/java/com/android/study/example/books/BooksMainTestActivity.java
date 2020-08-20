package com.android.study.example.books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.books.jjzg.JjzgMainTestActivity;

public class BooksMainTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, BooksMainTestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_main_test);

        Log.i("lvjie", "onCreate...");
    }


    /**
     * 进阶之光
     * @param view
     */
    public void jjzgOnClick(View view){
        JjzgMainTestActivity.startActivity(this);
    }

    public void onClickSelfBtn(View view) {
        startActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie", "onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie", "onResume...");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie", "onNewIntent...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lvjie", "onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie", "onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "onDestroy...");
    }
}
