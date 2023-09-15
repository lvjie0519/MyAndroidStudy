package com.android.study.example.uidemo.selfdefineview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.android.study.example.R;

public class FigureView extends View {

    private Context mContext;
    private Rect mHeadRect;
    private Rect mLeftHandRect;
    private Rect mRightHandRect;

    public FigureView(Context context) {
        this(context, null);
    }

    public FigureView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public FigureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context){
        this.mContext = context;

        int headLeft = 400;
        int headTop = headLeft;
        int add = 400;
        mHeadRect = new Rect(headLeft, headTop, headLeft+add, headTop+add);

        headLeft = 100;
        headTop = 1000;
        mLeftHandRect = new Rect(headLeft, headTop, headLeft+add, headTop+add);

        headLeft = 800;
        headTop = 1000;
        mRightHandRect = new Rect(headLeft, headTop, headLeft+add, headTop+add);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_figure), null, mHeadRect, null);
        canvas.drawBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_figure), null, mLeftHandRect, null);
        canvas.drawBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_figure), null, mRightHandRect, null);
    }
}
