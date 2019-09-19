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

        mSweeperView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("lvjie", "mLayoutSweeper  onClick...");
            }
        });
    }

    private void initData(Context context){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = DisplayUtil.dip2px(this.mContext, 200);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);

        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void moveSweeper(MapView.Point point){
        if(point == null){
            return;
        }

        Log.i("lvjie", "x: "+point.getPointX()+"  y: "+point.getPointY());
        this.mSweeperView.smoothScrollTo(point.getPointX(), point.getPointY());
        this.mMapView.drawNewPoint(point);
    }

    public void drawHistoryMap(List<MapView.Point> points){
        if(points == null || points.size() == 0){
            return;
        }

        this.mMapView.setHistoryPoints(points);
    }
}
