package com.android.study.example.uidemo.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

/**
 * scheme = mihome://plugin111
 */
public class OtherAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_app);

        // 获取通过scheme 打开activity， url中携带的参数
//        getIntent().getData().getQueryParameter("model");
    }

    public void onClickOpenAppResultActivity(View view){

        Intent intent = new Intent();
        intent.putExtra("name", "lvjie");
        intent.putExtra("age", 24);
        this.setResult(10, intent);   //设置当前Activity的结果码
        finish();
    }
}
