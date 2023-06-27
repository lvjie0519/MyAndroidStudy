package com.example.rtsp.client;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    private static final String TAG = "CustomVideoView";

    private static final int DEFAULT_WIDTH = 1080;
    private static final int DEFAULT_HEIGHT = 1868;
    private int mWidth = DEFAULT_WIDTH/2;
    private int mHeight = DEFAULT_HEIGHT/2;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure call, width: " + MeasureSpec.getSize(widthMeasureSpec) + ", height: " + MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(mWidth, mHeight);
    }

    public void resizeVideo(int width, int height) {
//        mWidth = width;
//        mHeight = height;
        // not sure whether it is useful or not but safe to do so
        getHolder().setFixedSize(mWidth, mHeight);
        requestLayout();
        invalidate(); // very important, so that onMeasure will be triggered
    }

    public void postRotate(float rotateDegree) {
        Matrix matrix = getTransformMatrix();
        matrix.postRotate(rotateDegree, mWidth/2, mHeight/2);
        invalidate();

        setRotation(rotateDegree);
//        updateMatrixToTexture(matrix);
//        setRotateDegrees(getCurrentRotateDegree() + rotateDegree);
    }

    private Matrix getTransformMatrix() {
        if (isTextureViewValid()) {
            return getMatrix();
        }
        return null;
    }

    private boolean isTextureViewValid() {
        return true;
    }
}
