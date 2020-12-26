package com.android.study.example.androidapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;

public class SharedPreferencesTestActivity extends Activity {

    private TextView mTvShowInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SharedPreferencesTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences_test);

        mTvShowInfo = findViewById(R.id.tv_show_info);
    }

    public void onClickWriteData(View view) {

        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences= getSharedPreferences("data-test",Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("name", "jack");
        editor.putInt("age", 28);
        //步骤4：提交
        editor.commit();

    }

    public void onClickReadData(View view) {
        SharedPreferences sharedPreferences= getSharedPreferences("data-test", Context .MODE_PRIVATE);
        String name=sharedPreferences.getString("name","");
        int age=sharedPreferences.getInt("age",0);
        this.mTvShowInfo.setText("name="+name+",  age="+age);
    }

    public void onClickDeleteData(View view) {
    }
}
