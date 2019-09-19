package com.android.study.example.uidemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MapView extends View {

    // 背景Paint
    private Paint mBackgroundPaint;

    // 画线Paint
    private Paint mDrawLinePaint;
    // 画线路径 path
    private Path mMapPath;


    private float mViewWidth;
    private float mViewHeight;

    private List<Point> mHistoryPoints;

    public MapView(@NonNull Context context) {
        this(context, null);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        initPaint();
        initData();
    }

    private void initPaint(){
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.parseColor("#45addc"));
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mDrawLinePaint = new Paint();
        mDrawLinePaint.setColor(Color.parseColor("#333333"));
        mDrawLinePaint.setStrokeWidth(4f);      // 线宽
        mDrawLinePaint.setStyle(Paint.Style.STROKE);

        mMapPath = new Path();
    }

    private void initData(){
        mHistoryPoints = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        canvas.drawPath(mMapPath, mDrawLinePaint);
    }

    private void drawBackground(Canvas canvas){
        canvas.drawRect(0, 0, mViewWidth, mViewHeight, mBackgroundPaint);
    }

    private void drawHistoryLines(){
        if(mHistoryPoints == null || mHistoryPoints.size() ==0){
            return;
        }
        int len = mHistoryPoints.size();
        for(int i=0; i<len; i++){
            if(i == 0){
                mMapPath.moveTo(mHistoryPoints.get(i).pointX, mHistoryPoints.get(i).pointY);
            }else{
                mMapPath.lineTo(mHistoryPoints.get(i).pointX, mHistoryPoints.get(i).pointY);
            }
        }
    }

    public void setNewPoint(Point point){
        if(point == null || point.pointX <0 || point.pointY <0){
            return;
        }
        mMapPath.lineTo(point.pointX, point.pointY);
    }

    public void drawNewPoint(Point point){
        setNewPoint(point);
        invalidate();
    }

    public void setHistoryPoints(List<Point> points){
        if(points == null || points.size() == 0){
            return;
        }
        mHistoryPoints = points;
        drawHistoryLines();
    }

    public float getViewWidth() {
        return mViewWidth;
    }

    public float getViewHeight() {
        return mViewHeight;
    }

    public void setViewWidth(float mViewWidth) {
        this.mViewWidth = mViewWidth;
    }

    public void setViewHeight(float mViewHeight) {
        this.mViewHeight = mViewHeight;
    }

    public static class Point{
        private int pointX;
        private int pointY;
        private int pointStyle;

        public Point() {
        }

        public Point(int pointX, int pointY, int pointStyle) {
            this.pointX = pointX;
            this.pointY = pointY;
            this.pointStyle = pointStyle;
        }

        public int getPointX() {
            return pointX;
        }

        public int getPointY() {
            return pointY;
        }

        public int getPointStyle() {
            return pointStyle;
        }
    }
}