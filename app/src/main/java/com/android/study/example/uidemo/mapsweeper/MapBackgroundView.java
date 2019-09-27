package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图底层背景
 */
public class MapBackgroundView extends View {

    private Context mContext;

    // 底图墙、障碍物等点
    private List<MapPoint> mPoints;
    private Paint mSquarePaint;

    private Rect mSweeperChargingPileReact;
    // 充电桩所在的位置
    private MapPoint mSweeperChargingPilePosition;

    public MapBackgroundView(@NonNull Context context) {
        this(context, null);
    }

    public MapBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        initData();
        initPaint();
    }

    private void initData(){
        mPoints = new ArrayList<>();
        mSweeperChargingPilePosition = new MapPoint(20, 20, 1);
    }

    private void initPaint(){
        mSquarePaint = new Paint();
        mSquarePaint.setColor(Color.parseColor("#333333"));
        mSquarePaint.setStrokeWidth(10);
        mSquarePaint.setStrokeCap(Paint.Cap.SQUARE);

        int width = DisplayUtil.dip2px(this.mContext, 14);
        int height = DisplayUtil.dip2px(this.mContext, 20);
        mSweeperChargingPileReact = new Rect(mSweeperChargingPilePosition.pointX, mSweeperChargingPilePosition.pointY,
                (mSweeperChargingPilePosition.pointX+width), (mSweeperChargingPilePosition.pointY+height));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float px = (float) width/255;
        if(px>10){
            mSquarePaint.setStrokeWidth(px);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPoints(canvas);
        canvas.drawBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.icon_sweeper_charging_pile), null, mSweeperChargingPileReact, null);
    }

    private void drawPoints(Canvas canvas){
        if(this.mPoints == null || this.mPoints.size() == 0){
            return;
        }

        int len = this.mPoints.size();
        float pts[] = new float[len<<1];
        for(int i=0, j=0; i<len; i++,j+=2){
            pts[j] = this.mPoints.get(i).pointX;
            pts[j+1] = this.mPoints.get(i).pointY;
        }
        canvas.drawPoints(pts, this.mSquarePaint);
    }

    // 设置充电桩的位置
    public void setChargingPilePostion(MapPoint point){
        mSweeperChargingPilePosition = point;
    }


    public void setPoints(List<MapPoint> points) {
        this.mPoints = points;
    }

    public void addNewPoints(List<MapPoint> points){
        if(points == null || points.size() == 0){
            return;
        }
        if(this.mPoints == null){
            this.mPoints = points;
        }else{
            this.mPoints.addAll(points);
        }
        invalidate();
    }
}
