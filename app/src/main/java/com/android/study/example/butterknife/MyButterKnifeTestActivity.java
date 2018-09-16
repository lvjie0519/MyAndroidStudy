package com.android.study.example.butterknife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.study.example.R;

public class MyButterKnifeTestActivity extends AppCompatActivity {

    @MyBindView(R.id.tv_show_info)
    public TextView tvShowInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MyButterKnifeTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_butter_knife_test);
        MyButterKnife.bind(this);

        initView();
    }

    private void initView(){
        tvShowInfo.setText("重新设置显示信息");
    }
}
