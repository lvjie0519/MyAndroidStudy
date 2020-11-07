package com.android.study.example.androidapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.MainActivity;
import com.android.study.example.R;

/**
 * 1、activity的启动模式？
 *      standard、singleTop、singleTask、singleInstance
 * 如果你个activity 设置为singleTop且该activity已经在栈顶，再次启动该activity，activity会执行哪些方法？
 * onPause-->onNewIntent-->onResume， 注意连View的周期函数也不会执行
 *
 * 如果你个activity 设置为singleTask且该activity已经在栈顶，再次启动该activity，activity会执行哪些方法？
 * onPause-->onNewIntent-->onResume， 注意连View的周期函数也不会执行
 * 如果activity不在栈顶呢？
 * 由于不在栈顶，所以之前已经执行了onStop；再次startActivity时如下：
 * onNewIntent-->onRestart-->onStart-->onResume
 * 同时View只会执行onDraw（onMeasure onLayout 不会执行）
 *
 * 2、activity及view的生命周期
 *  onCreate-->onStart-->onResume-->onAttachedToWindow   (这都是activity执行的)
 *  -->onWindowFocusChanged-->onMeasure-->onLayout-->onDraw  (这都是View执行的)
 *  -->onWindowFocusChanged   (这都是activity执行的)
 *  -->onWindowFocusChanged   (这都是View执行的)
 *
 */
public class LifeCycleTestActivity extends Activity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LifeCycleTestActivity.class);
        intent.putExtra("startTime", System.currentTimeMillis());
        context.startActivity(intent);
    }

    public static void startMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("startTime", System.currentTimeMillis());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        long startTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle_test);
        Log.i("lvjie","super.onCreate... timeCost="+(System.currentTimeMillis()-startTime));
        startTime = getIntent().getLongExtra("startTime", 0);
        Log.i("lvjie","onCreate... timeCost="+(System.currentTimeMillis()-startTime));
        Log.i("lvjie","LifeCycleTestActivity onCreate...");
    }

    public void goNextActivity(View view){
        LifeCycleTestActivity.startMainActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie","LifeCycleTestActivity onNewIntent...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie","LifeCycleTestActivity onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie","LifeCycleTestActivity onResume...");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lvjie","LifeCycleTestActivity onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie","LifeCycleTestActivity onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie","LifeCycleTestActivity onDestroy...");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lvjie","LifeCycleTestActivity onRestart...");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("lvjie","LifeCycleTestActivity onAttachedToWindow...");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("lvjie","LifeCycleTestActivity onWindowFocusChanged..."+hasFocus);
    }

}
