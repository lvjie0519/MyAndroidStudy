package com.android.study.example.androidapi.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyFpsTestView extends View {

    public MyFpsTestView(Context context) {
        super(context);
    }

    public MyFpsTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFpsTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("lvjie","MyFpsTestView onAttachedToWindow...");
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.i("lvjie","MyFpsTestView onVisibilityChanged..."+visibility);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("lvjie","MyFpsTestView onMeasure...");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("lvjie","MyFpsTestView onLayout...");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0; i<100000000; i++){
            if(i == 99999999){
                Log.i("lvjie","MyFpsTestView onDraw...i="+i);
            }
        }
        Log.i("lvjie","MyFpsTestView onDraw...");
    }
}
