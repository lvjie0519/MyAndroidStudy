package com.android.study.example.leak;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

public class LeakTest1Activity extends AppCompatActivity {

    private String valueStr = "";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LeakTest1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_test1);

        valueStr = "LeakTest1Activity";
    }


    public void onClick1(View view){

        LeakTestInstance.getInstance().setOnBtnClick(new OnBtnClick() {
            @Override
            public String onLeak2Click(String value) {
                return "value: "+value+"\n     valueStr: "+valueStr;
            }
        });

        LeakTestInstance.getInstance().setSoftReference(new OnBtnClick() {
            @Override
            public String onLeak2Click(String value) {
                return "value: "+value+"\n     valueStr: "+valueStr;
            }
        });

        LeakTest2Activity.startActivity(this);
    }

    public void onClick2(View view){

        LeakTestInstance.getInstance().setSoftReference(new OnBtnClick() {
            @Override
            public String onLeak2Click(String value) {
                return "setSoftReference--> value: "+value+"\n     valueStr: "+valueStr;
            }
        });

        LeakTest2Activity.startActivity(this);
        finish();
    }
}
