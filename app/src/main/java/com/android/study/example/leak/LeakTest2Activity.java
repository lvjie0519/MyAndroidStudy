package com.android.study.example.leak;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;

public class LeakTest2Activity extends AppCompatActivity {

    private TextView tvShowInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LeakTest2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_test2);

        tvShowInfo = (TextView) findViewById(R.id.tv_show_info);
    }

    public void onClick0(View view) {
        System.gc();
    }

    public void onClick1(View view){

        OnBtnClick onBtnClick = LeakTestInstance.getInstance().getOnBtnClick();
        String res = "";
        if(onBtnClick != null){
            res = onBtnClick.onLeak2Click("LeakTest2Activity--> getOnBtnClick");
        }else{
            res = "LeakTest2Activity--> getOnBtnClick null";
        }

        onBtnClick = LeakTestInstance.getInstance().getSoftReference();
        if(onBtnClick != null){
            res += "\n "+onBtnClick.onLeak2Click("LeakTest2Activity--> getSoftReference");
        }else{
            res += "\n "+"LeakTest2Activity--> getSoftReference null";
        }

        tvShowInfo.setText(res);
    }

    public void onClick2(View view){
        OnBtnClick onBtnClick ;
        String res = "";
        onBtnClick = LeakTestInstance.getInstance().getSoftReference();
        if(onBtnClick != null){
            res += "\n "+onBtnClick.onLeak2Click("LeakTest2Activity--> getSoftReference");
        }else{
            res += "\n "+"LeakTest2Activity--> getSoftReference null";
        }

        tvShowInfo.setText(res);
    }


}
