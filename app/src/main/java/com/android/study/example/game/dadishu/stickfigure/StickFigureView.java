package com.android.study.example.game.dadishu.stickfigure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class StickFigureView extends View {
    private static final String TAG = "StickFigureView";
    private static final int DEFAULT_SCALE_SIZE = 1000;
    private static final int DEFAULT_POINT_SIZE = 23;
    private ArrayList<Point> points;
    private Paint mPaint;

    /**
     * 左右手、左右脚、头的区域
     */
    private FigureArea mLeftHandArea;
    private FigureArea mRightHandArea;
    private FigureArea mLeftFootArea;
    private FigureArea mRightFootArea;
    private FigureArea mHeadArea;

    public StickFigureView(Context context) {
        this(context, null);

    }

    public StickFigureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize initial coordinates here
        initData();
    }

    private void initData(){
        points = null;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);  // 设置画笔颜色为黑色
        mPaint.setStrokeWidth(5);

        mLeftHandArea = new FigureArea(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        mRightHandArea = new FigureArea(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        mLeftFootArea = new FigureArea(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        mRightFootArea = new FigureArea(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        mHeadArea = new FigureArea(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    public void updateCoordinates(ArrayList<Point> points) {
        Log.i(TAG, "updateCoordinates");

        if (null == points) {
            Log.i(TAG, "points is null.");
            return;
        }
        if (points.size() < DEFAULT_POINT_SIZE) {
            Log.i(TAG, "insufficient points.");
            return;
        }

        // Update the coordinates with new values
        this.points = points;
        preProcessPoints(this.points);
        // Trigger a redraw of the view
        invalidate();
    }

    private void preProcessPoints(ArrayList<Point> points) {
        if (points == null || points.size() < DEFAULT_POINT_SIZE) {
            return;
        }

        // scale point
        for (Point point : points) {
            point.setX(point.getX()*DEFAULT_SCALE_SIZE);
            point.setY(point.getY()*DEFAULT_SCALE_SIZE);
        }

        // process head
        for (int i = 0; i < 5; i++) {
            updateFigureArea(mHeadArea, points.get(i));
        }

        // process left hand
        updateFigureArea(mLeftHandArea, points.get(10));
        updateFigureArea(mLeftHandArea, points.get(18));

        // process right hand
        updateFigureArea(mRightHandArea, points.get(9));
        updateFigureArea(mRightHandArea, points.get(17));

        // process left foot
        updateFigureArea(mLeftFootArea, points.get(16));
        updateFigureArea(mLeftFootArea, points.get(20));
        updateFigureArea(mLeftFootArea, points.get(22));

        // process right foot
        updateFigureArea(mRightFootArea, points.get(15));
        updateFigureArea(mRightFootArea, points.get(19));
        updateFigureArea(mRightFootArea, points.get(21));
    }

    private void updateFigureArea(FigureArea figureArea, Point point){
        if(figureArea == null || point == null){
            return;
        }

        if (!isValid(point)) {
            return;
        }

        if (figureArea.getLeftX() >= point.getX()) {
            figureArea.setLeftX(point.getX());
        }

        if (figureArea.getTopY() >= point.getY()) {
            figureArea.setTopY(point.getY());
        }

        if (figureArea.getRightX() <= point.getX()) {
            figureArea.setRightX(point.getX());
        }

        if (figureArea.getBottomY() <= point.getY()) {
            figureArea.setBottomY(point.getY());
        }
    }

    private boolean isValid(Point point) {
        if (point.getX() > 0 && point.getY() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void drawLine(Point pointA, Point pointB, Canvas canvas) {
        if(isValid(pointA) && isValid(pointB)) {
            canvas.drawLine(pointA.getX(), pointA.getY() , pointB.getX(), pointB.getY(), mPaint);
        }
    }


    @Override
    protected void onDraw(Canvas canvas)  {
        super.onDraw(canvas);
        if (null == mPaint) {
            Log.i(TAG, "mPaint is null.");
            return;
        }

        if (null == points) {
            Log.i(TAG, "points is null.");
            return;
        }
        if (points.size() < DEFAULT_POINT_SIZE) {
            Log.i(TAG, "insufficient points.");
            return;
        }
        // Draw the head
        drawLine(points.get(0), points.get(1), canvas);
        drawLine(points.get(1), points.get(3), canvas);
        drawLine(points.get(0), points.get(2), canvas);
        drawLine(points.get(2), points.get(4), canvas);

        // Draw the body
        drawLine(points.get(5), points.get(6), canvas);
        drawLine(points.get(11), points.get(12), canvas);
        drawLine(points.get(6), points.get(12), canvas);
        drawLine(points.get(5), points.get(11), canvas);

        // Draw the arms
        drawLine(points.get(6), points.get(8), canvas);
        drawLine(points.get(8), points.get(10), canvas);
        drawLine(points.get(10), points.get(18), canvas);

        drawLine(points.get(5), points.get(7), canvas);
        drawLine(points.get(7), points.get(9), canvas);
        drawLine(points.get(9), points.get(17), canvas);

        // Draw the legs
        drawLine(points.get(12), points.get(14), canvas);
        drawLine(points.get(14), points.get(16), canvas);
        drawLine(points.get(16), points.get(20), canvas);
        drawLine(points.get(20), points.get(22), canvas);

        drawLine(points.get(11), points.get(13), canvas);
        drawLine(points.get(13), points.get(15), canvas);
        drawLine(points.get(15), points.get(19), canvas);
        drawLine(points.get(15), points.get(21), canvas);
    }
}
