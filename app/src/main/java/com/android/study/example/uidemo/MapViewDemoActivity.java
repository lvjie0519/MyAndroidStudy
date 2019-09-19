package com.android.study.example.uidemo;

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
    private List<MapView.Point> points;
    private Random mRandom = new Random();

    private MapSweeperView mapSweeperView;
    private static final int msgMapSweeperView = 10001;

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

                MapView.Point point = generatePoint();
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
    }

    private void initData(){
        points = new ArrayList<>();
        points.add(new MapView.Point(100,100, 1));
        points.add(new MapView.Point(300,100, 1));
        points.add(new MapView.Point(300,200, 1));
        points.add(new MapView.Point(100,200, 1));
        points.add(new MapView.Point(400,200, 1));
        points.add(new MapView.Point(300,500, 1));
        points.add(new MapView.Point(900,400, 1));
        points.add(new MapView.Point(400,100, 1));

    }

    private MapView.Point generatePoint(){
        int x = mRandom.nextInt((int) mapView.getViewWidth());
        int y = mRandom.nextInt((int) mapView.getViewHeight());

        return new MapView.Point(x, y, 1);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(1);
        mHandler.removeMessages(msgMapSweeperView);
        mHandler = null;
        super.onDestroy();
    }
}
