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
    private ArrayList<Point> points;
    private Paint mPaint;

    public StickFigureView(Context context) {
        this(context, null);

    }

    public StickFigureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize initial coordinates here
        points = null;
        mPaint = new Paint();
    }

    public void updateCoordinates(ArrayList<Point> points) {
        Log.i(TAG, "updateCoordinates");
        // Update the coordinates with new values
        this.points = points;
        // Trigger a redraw of the view
        invalidate();
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
            canvas.drawLine(pointA.getX()*1000, pointA.getY()*1000 , pointB.getX()*1000, pointB.getY()*1000, mPaint);
        }
    }


    @Override
    protected void onDraw(Canvas canvas)  {
        super.onDraw(canvas);
        if (null == mPaint) {
            Log.i(TAG, "mPaint is null.");
            return;
        }
        mPaint.setColor(Color.BLACK);  // 设置画笔颜色为黑色
        mPaint.setStrokeWidth(5);
        if (null == points) {
            Log.i(TAG, "points is null.");
            return;
        }
        if (points.size() < 23) {
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
