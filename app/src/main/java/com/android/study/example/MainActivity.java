package com.android.study.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.butterknife.ButterKnifeTestActivity;
import com.android.study.example.butterknife.MyButterKnifeTestActivity;
import com.annotaions.example.MyAnnotation;

@MyAnnotation("hello world")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        findViewById(R.id.btn_butterknife_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButterKnifeTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_mybutterknife_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyButterKnifeTestActivity.startActivity(MainActivity.this);
            }
        });
    }

}
