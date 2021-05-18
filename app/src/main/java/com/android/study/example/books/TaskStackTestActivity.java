package com.android.study.example.books;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.study.example.R;

public class TaskStackTestActivity extends AppCompatActivity {

    /**
     * 清空任务栈 在启动自己
     * @param context
     */
    public static void startActivityForClearTask(Context context){
        Intent intent = new Intent(context.getApplicationContext(), TaskStackTestActivity.class);
        //清空任务栈
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_stack_test);
    }
}