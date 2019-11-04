package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyView extends android.support.v7.widget.AppCompatTextView{

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result = super.dispatchTouchEvent(event);
        Log.i("lvjie","MyView dispatchTouchEvent-->result: "+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        Log.i("lvjie","MyView onTouchEvent-->result: "+result);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.i("lvjie","MyView onTouchEvent-->MotionEvent.ACTION_DOWN");
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            Log.i("lvjie","MyView onTouchEvent-->MotionEvent.ACTION_UP");
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.i("lvjie","MyView  onTouchEvent-->MotionEvent.ACTION_MOVE");
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("lvjie", "MyView  onMeasure...");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("lvjie", "MyView  onLayout...");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("lvjie", "MyView  onDraw...");
    }
}

