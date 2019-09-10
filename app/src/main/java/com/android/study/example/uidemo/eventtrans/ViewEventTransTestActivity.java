package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.study.example.R;

public class ViewEventTransTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ViewEventTransTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_trans_test);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String actionName = "";
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            actionName = "MotionEvent.ACTION_DOWN";
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            actionName = "MotionEvent.ACTION_UP";
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            actionName = "MotionEvent.ACTION_MOVE";
        }
        Log.i("lvjie", "Activity ..."+actionName);
        boolean result = super.dispatchTouchEvent(ev);
        Log.i("lvjie", "Activity result: "+result);
        return result;
    }
}
