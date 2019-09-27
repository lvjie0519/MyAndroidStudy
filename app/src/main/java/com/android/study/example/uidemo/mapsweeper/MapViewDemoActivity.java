package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapViewDemoActivity extends AppCompatActivity {

    private MapView mapView;
    private List<MapPoint> points;
    private List<MapPoint> backgroundPoints;
    private Random mRandom = new Random();

    private static final int msgMapSweeperView = 10001;

    private ZoomMapSweeperView zoomView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mHandler!=null && msg.what == 1){
                mapView.drawNewPoint(generatePoint());
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }else if(mHandler != null && msg.what == msgMapSweeperView){

            }
        }
    };

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MapViewDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_demo);

        initData();
        initView();

        zoomView.getMapSweeperView().drawHistoryMap(points);
        zoomView.getMapSweeperView().drawHistoryMapBackground(backgroundPoints);
    }

    private int x = 0;
    private void initView(){

        mapView = (MapView) findViewById(R.id.map_view);

        findViewById(R.id.btn_draw_history_data_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示历史数据
            }
        });
        findViewById(R.id.btn_start_sweeper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始扫地
//                mHandler.sendEmptyMessageDelayed(msgMapSweeperView, 1000);

//                mapSweeperView.moveSweeper(new MapView.Point(x, x, 1));
//                x+=20;

                MapPoint point = generatePoint();
                zoomView.getMapSweeperView().moveSweeper(point);
            }
        });
        findViewById(R.id.btn_stop_sweeper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 回充

            }
        });

        findViewById(R.id.btn_draw_history_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setHistoryPoints(points);
                mapView.invalidate();
            }
        });

        findViewById(R.id.btn_draw_new_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        });

        findViewById(R.id.btn_clean_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        zoomView = (ZoomMapSweeperView) findViewById(R.id.zoom_view);
        zoomView.setMaxZoom(2.0f);
        zoomView.setScreenWidth(widthPixels);
    }

    private void initData(){
        points = new ArrayList<>();
        points.add(new MapPoint(110,110, 1));
        points.add(new MapPoint(120,130, 1));
        points.add(new MapPoint(130,260, 1));
        points.add(new MapPoint(140,200, 1));
        points.add(new MapPoint(150,120, 1));
        points.add(new MapPoint(160,140, 1));
        points.add(new MapPoint(170,190, 1));
        points.add(new MapPoint(180,110, 1));

        backgroundPoints = new ArrayList<>();
        backgroundPoints.add(new MapPoint(10,10, 1));
        backgroundPoints.add(new MapPoint(20,10, 1));
        backgroundPoints.add(new MapPoint(30,10, 1));
        backgroundPoints.add(new MapPoint(80,10, 1));
        backgroundPoints.add(new MapPoint(90,10, 1));

        backgroundPoints.add(new MapPoint(10,10, 1));
        backgroundPoints.add(new MapPoint(10,20, 1));
        backgroundPoints.add(new MapPoint(10,30, 1));
        backgroundPoints.add(new MapPoint(10,80, 1));
        backgroundPoints.add(new MapPoint(10,90, 1));

        backgroundPoints.add(new MapPoint(200,200, 1));
        backgroundPoints.add(new MapPoint(205,200, 1));
        backgroundPoints.add(new MapPoint(500,500, 1));
    }



    private MapPoint generatePoint(){
        int x = mRandom.nextInt((int) zoomView.getMapSweeperView().getViewWidth());
        int y = mRandom.nextInt((int) zoomView.getMapSweeperView().getViewHeight());

        return new MapPoint(x, y, 1);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(1);
        mHandler.removeMessages(msgMapSweeperView);
        mHandler = null;
        super.onDestroy();
    }
}
