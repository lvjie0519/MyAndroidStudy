package com.android.study.example.performance_optimization;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

public class PerformOptMainTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PerformOptMainTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_opt_main_test);
    }

    public void openTraceViewUseTestActivity(View view) {
        TraceViewUseTestActivity.startActivity(this);
    }
}
