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
    private Random mRandom = new Random();

    private MapSweeperView mapSweeperView;
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

        this.mapSweeperView.drawHistoryMap(points);
    }

    private int x = 0;
    private void initView(){

        mapView = (MapView) findViewById(R.id.map_view);
        mapSweeperView = (MapSweeperView) findViewById(R.id.map_sweeper_view);

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
                mapSweeperView.moveSweeper(point);
            }
        });
        findViewById(R.id.btn_stop_sweeper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止扫地
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

        zoomView = (ZoomMapSweeperView) findViewById(R.id.zoom_view);
        zoomView.setMaxZoom(2.0f);
    }

    private void initData(){
        points = new ArrayList<>();
        points.add(new MapPoint(100,100, 1));
        points.add(new MapPoint(300,100, 1));
        points.add(new MapPoint(300,200, 1));
        points.add(new MapPoint(100,200, 1));
        points.add(new MapPoint(400,200, 1));
        points.add(new MapPoint(300,500, 1));
        points.add(new MapPoint(900,400, 1));
        points.add(new MapPoint(400,100, 1));

    }

    private MapPoint generatePoint(){
        int x = mRandom.nextInt((int) mapView.getViewWidth());
        int y = mRandom.nextInt((int) mapView.getViewHeight());

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
