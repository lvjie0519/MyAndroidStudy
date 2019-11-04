package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyViewGroupA extends LinearLayout {

    public MyViewGroupA(Context context) {
        super(context);
    }

    public MyViewGroupA(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroupA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        Log.i("lvjie","MyViewGroupA  dispatchTouchEvent-->result: "+result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        Log.i("lvjie","MyViewGroupA  onInterceptTouchEvent-->result: "+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        Log.i("lvjie","MyViewGroupA onTouchEvent-->result: "+result);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.i("lvjie","MyViewGroupA onTouchEvent-->MotionEvent.ACTION_DOWN");
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            Log.i("lvjie","MyViewGroupA onTouchEvent-->MotionEvent.ACTION_UP");
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.i("lvjie","MyViewGroupA onTouchEvent-->MotionEvent.ACTION_MOVE");
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("lvjie", "MyViewGroupA  onMeasure...");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("lvjie", "MyViewGroupA  onLayout...");
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("lvjie", "MyViewGroupA  onDraw...");
        super.onDraw(canvas);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i("lvjie", "MyViewGroupA  dispatchDraw...");
        super.dispatchDraw(canvas);

    }
}
