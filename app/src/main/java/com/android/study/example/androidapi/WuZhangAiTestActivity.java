package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.study.example.R;

public class WuZhangAiTestActivity extends AppCompatActivity {

    private Button btnWuZhangAi1;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WuZhangAiTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wu_zhang_ai_test);

        initView();
    }

    private void initView(){
        btnWuZhangAi1 = findViewById(R.id.btn_wuzhangai1);

        btnWuZhangAi1.setContentDescription("无障碍测试");
    }

    public void onClickWuZhangAiTest1(View view) {
        btnWuZhangAi1.setContentDescription("#12AB12");
    }
}
