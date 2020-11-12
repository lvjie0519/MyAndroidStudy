package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyViewGroupC extends ViewGroup {

    public MyViewGroupC(Context context) {
        super(context);
    }

    public MyViewGroupC(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroupC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("lvjie", "MyViewGroupB  onMeasure... width="+MeasureSpec.getSize(widthMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("lvjie", "MyViewGroupB  onLayout...left="+left+"  right="+right);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Log.i("lvjie", "MyViewGroupB  onDraw...");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i("lvjie", "MyViewGroupB  before super dispatchDraw...");
        super.dispatchDraw(canvas);
        Log.i("lvjie", "MyViewGroupB  after super dispatchDraw...");
    }
}
