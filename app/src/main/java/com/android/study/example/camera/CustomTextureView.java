package com.android.study.example.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

public class CustomTextureView extends TextureView implements TextureView.SurfaceTextureListener, Runnable{

    private boolean mRunning = false;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private Rect mRect;

    private static Paint mBitmapPaint;
    private static Matrix mBitmapMatrix;

    public CustomTextureView(Context context) {
        super(context);
        init();
    }

    public CustomTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initBitmapMatrix();
        initBitmapPaint();
        setSurfaceTextureListener(this);
    }

    private void initBitmapPaint(){
        if(mBitmapPaint != null){
            return;
        }

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);
    }

    private void initBitmapMatrix(){
        mBitmapMatrix = new Matrix();
        mBitmapMatrix.postRotate(90);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
        mRect = new Rect(0, 0, width, height);
        mSurface = new Surface(mSurfaceTexture);

        new Thread(this).start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
        mRect = new Rect(0, 0, width, height);
        mSurface = new Surface(mSurfaceTexture);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mRunning = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void run() {
        mRunning = true;
//        while (mRunning) {
//            SystemClock.sleep(333);
//            Canvas canvas = mSurface.lockCanvas(mRect);
//            if (canvas != null) {
//                try {
//                    synchronized (mSurface) {
//                        onRender(canvas);
//                    }
//                } finally {
//                    mSurface.unlockCanvasAndPost(canvas);
//                }
//            }
//        }
    }

    private void onRender(Canvas canvas) {
        canvas.drawColor(Color.RED);
    }

    public void drawBitMap(Bitmap bitmap){

        if(mSurface == null || mRect == null){
            return;
        }

        Canvas canvas = mSurface.lockCanvas(mRect);
        if(canvas == null){
            return;
        }

        try {
            synchronized (mSurface){
//                canvas.drawBitmap(bitmap, mBitmapMatrix, null);
                canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mSurface.unlockCanvasAndPost(canvas);
        }


    }
}
