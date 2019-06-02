package com.android.study.example.uidemo.dragging;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

/**
 * 界面中具有悬浮按钮的activity
 */
public class FloatingActionBtnTestActivity extends AppCompatActivity {

    private WindowManager mWindowManager;
    private DraggingButton mDraggintTextView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, FloatingActionBtnTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_action_btn_test);

        mDraggintTextView = (DraggingButton) findViewById(R.id.tv_dragging);
        mDraggintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingActionBtnTestActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
