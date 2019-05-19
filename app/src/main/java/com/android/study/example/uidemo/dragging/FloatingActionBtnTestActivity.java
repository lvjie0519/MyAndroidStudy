package com.android.study.example.uidemo.dragging;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

/**
 * 界面中具有悬浮按钮的activity
 */
public class FloatingActionBtnTestActivity extends AppCompatActivity {

    private DraggingTextView mDraggintTextView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, FloatingActionBtnTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_action_btn_test);

        mDraggintTextView = (DraggingTextView) findViewById(R.id.tv_dragging);
        mDraggintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
