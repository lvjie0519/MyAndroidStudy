package com.android.study.example.books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.android.study.example.R;
import com.android.study.example.books.jjzg.JjzgMainTestActivity;

public class BooksMainTestActivity extends Activity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, BooksMainTestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindowSize();
        setContentView(R.layout.activity_books_main_test);

        Log.i("lvjie", "onCreate...");
    }

    private void initWindowSize(){
        //特殊设备，设置宽度
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.60);    //宽度设置为屏幕的0.95
        getWindow().setAttributes(p);     //设置生效
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JjzgMainTestActivity.startActivity(BooksMainTestActivity.this);
        }
    };

    /**
     * 进阶之光
     * @param view
     */
    public void jjzgOnClick(View view){
        mHandler.sendMessageDelayed(Message.obtain(), 1000*5);
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
