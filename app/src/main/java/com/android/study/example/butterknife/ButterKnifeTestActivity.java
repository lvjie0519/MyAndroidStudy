package com.android.study.example.butterknife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.study.example.BaseActivity;
import com.android.study.example.R;

import butterknife.BindView;

public class ButterKnifeTestActivity extends BaseActivity {

    @BindView(R.id.tv_show_info)
    TextView tvInfo;
//    @BindView(R.id.tv_show_info_2)
//    TextView tvInfo2;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ButterKnifeTestActivity.class);
        context.startActivity(intent);
    }

    private void initView(){
        tvInfo.setText("测试信息");
//        tvInfo2.setText("测试信息2");
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_butter_knife_test;
    }

}
