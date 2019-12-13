package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

public class SweeperImageView extends android.support.v7.widget.AppCompatImageView {

    public SweeperImageView(Context context) {
        super(context);
    }

    public SweeperImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SweeperImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("lvjie", "SweeperImageView  width="+MeasureSpec.getSize(widthMeasureSpec)+" height="+MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
