package com.android.study.example.butterknife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.study.example.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ButterKnifeTestActivity extends AppCompatActivity {

    @BindView(R.id.tv_show_info)
    TextView tvInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ButterKnifeTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife_test);

        //绑定初始化ButterKnife
        ButterKnife.bind(this);

        initView();

    }

    private void initView(){
        tvInfo.setText("测试信息");
    }

}
