package com.android.study.example.uidemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.study.example.utils.DisplayUtil;

import java.util.List;

public class MapSweeperView extends RelativeLayout {

    private Context mContext;
    // 扫地机
    private SweeperView mSweeperView;

    // 地图部分
    private MapView mMapView;

    private int mViewHeight;
    private int mViewWidth;

    public MapSweeperView(Context context) {
        this(context, null);
    }

    public MapSweeperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSweeperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initView(context);
        initData(context);
    }

    private void initView(Context context){

        this.mSweeperView = new SweeperView(context);
        this.mMapView = new MapView(context);
        LayoutParams mapViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(this.mMapView, mapViewParams);

        LayoutParams sweeperViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sweeperViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(this.mSweeperView, sweeperViewParams);

    }

    private void initData(Context context){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(1080, MeasureSpec.EXACTLY);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void moveSweeper(MapView.Point point){
        if(point == null){
            return;
        }

        this.mSweeperView.smoothScrollTo(convertSweeperViewPoint(point.getPointX()), convertSweeperViewPoint(point.getPointY()));
        this.mMapView.drawNewPoint(point);
    }

    private int convertSweeperViewPoint(int point){
        /**
         * 扫地机最开始处于画布最中心，中心点的坐标为 0，0
         * 当scroll x为正时，是向左移  需要统一转换下坐标系
         */
        int pointCenter = mViewWidth>>1;
        return pointCenter-point;
    }

    public void drawHistoryMap(List<MapView.Point> points){
        if(points == null || points.size() == 0){
            return;
        }

        this.mMapView.setHistoryPoints(points);
    }
}
