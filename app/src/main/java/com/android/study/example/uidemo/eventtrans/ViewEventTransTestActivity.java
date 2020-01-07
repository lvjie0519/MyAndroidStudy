package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.study.example.R;
import com.android.study.example.utils.AndroidBug5497Workaround;
import com.android.study.example.utils.TitleBarUtil;
import com.android.study.example.utils.ToastUtil;

public class ViewEventTransTestActivity extends AppCompatActivity {

    private MyViewC myView;
    private MyViewGroupB myViewGroupB;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ViewEventTransTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TitleBarUtil.enableTranslucentStatus(this);
        setContentView(R.layout.activity_view_event_trans_test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        AndroidBug5497Workaround.getInstance().assistActivity(this);

        initView();
    }

    private void initView(){

        myView = (MyViewC) findViewById(R.id.myview);
        myViewGroupB = (MyViewGroupB) findViewById(R.id.view_myViewGroupB);

        myView.post(new Runnable() {
            @Override
            public void run() {
                Log.i("lvjie", "myView  width="+myView.getWidth()+"  height="+myView.getHeight());
            }
        });

        findViewById(R.id.btn_test_invalidate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.invalidate();
            }
        });

        findViewById(R.id.btn_test_requestLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.requestLayout();
            }
        });

        findViewById(R.id.btn_test_addView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewGroupB.removeAllViews();
                View view = new View(ViewEventTransTestActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                view.setBackgroundColor(Color.parseColor("#21211a"));
                myViewGroupB.addView(view, layoutParams);
            }
        });
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

    private int softInputStyle = 1;
    public void onBtnClickToSoftwareTest(View view){
        if(softInputStyle %2 == 1){
            ToastUtil.showToast(this, "SOFT_INPUT_ADJUST_PAN");
            AndroidBug5497Workaround.getInstance().setShouldFitSoftKeybord(false);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }else{
            ToastUtil.showToast(this, "SOFT_INPUT_ADJUST_RESIZE");
            AndroidBug5497Workaround.getInstance().setShouldFitSoftKeybord(true);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        softInputStyle++;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidBug5497Workaround.getInstance().cleanInstance();
    }
}
