package com.android.study.example.books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.books.jjzg.JjzgMainTestActivity;

public class BooksMainTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, BooksMainTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_main_test);
    }


    /**
     * 进阶之光
     * @param view
     */
    public void jjzgOnClick(View view){
        JjzgMainTestActivity.startActivity(this);
    }
}
