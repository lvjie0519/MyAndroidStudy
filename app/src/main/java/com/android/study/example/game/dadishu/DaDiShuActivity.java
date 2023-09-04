package com.android.study.example.game.dadishu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

public class DaDiShuActivity extends Activity {
    private static final String TAG = "DaDiShuActivity";

    private WhackMoleView mWhackMoleView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DaDiShuActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_di_shu);

        initView();
    }

    private void initView(){
        mWhackMoleView = findViewById(R.id.whackMoleView);
    }

    public void btnOnClickMouseOut(View view) {
        Log.i(TAG, "btnOnClickMouseOut");
        mWhackMoleView.startBulletViewAnimatorOut();
    }

    public void btnOnClickBeatMouse(View view) {
        Log.i(TAG, "btnOnClickBeatMouse");
        mWhackMoleView.startBulletViewAnimatorIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWhackMoleView.destory();
    }
}