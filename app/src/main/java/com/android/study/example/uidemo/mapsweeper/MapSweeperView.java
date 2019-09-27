package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * 扫地机综合管理类
 * 服务端的点坐标到手机屏幕的坐标系转换在此类进行转换
 */
public class MapSweeperView extends RelativeLayout {

    private Context mContext;
    // 扫地机
    private SweeperView mSweeperView;

    // 地图部分
    private MapView mMapView;

    // 地图底层部分
    private MapBackgroundView mMapBackgroundView;

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
        // 背景底图
        this.mMapBackgroundView = new MapBackgroundView(context);
        // 扫地机
        this.mSweeperView = new SweeperView(context);
        // 画线轨迹
        this.mMapView = new MapView(context);
        LayoutParams mapViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(this.mMapBackgroundView, mapViewParams);
        addView(this.mMapView, mapViewParams);

        LayoutParams sweeperViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sweeperViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(this.mSweeperView, sweeperViewParams);

    }

    private void initData(Context context){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mViewHeight, MeasureSpec.EXACTLY);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.mViewWidth, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 移动扫地机 同时 画出轨迹
     * @param point
     */
    public void moveSweeper(MapPoint point){
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

    /**
     * 绘制历史轨迹
     * @param points
     */
    public void drawHistoryMap(List<MapPoint> points){
        if(points == null || points.size() == 0){
            return;
        }

        this.mMapView.setHistoryPoints(points);
    }

    /**
     * 绘制底图 墙、障碍物
     * @param points
     */
    public void drawHistoryMapBackground(List<MapPoint> points){
        this.mMapBackgroundView.setPoints(points);
    }

    /**
     * 基于历史数据， 根据新增的碰撞点绘制底图 墙、障碍物
     * @param points
     */
    public void drawNewPointsToMapBackground(List<MapPoint> points){
        this.mMapBackgroundView.addNewPoints(points);
    }

    public void setWidth(int width) {
        this.mViewWidth= width;
    }

    public void setHeight(int height) {
        this.mViewHeight = height;
    }

    public int getViewHeight() {
        return mViewHeight;
    }

    public int getViewWidth() {
        return mViewWidth;
    }
}
