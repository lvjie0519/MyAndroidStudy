package com.android.study.example.uidemo.mapsweeper;

import java.util.List;

public class MapPoint {

    public int pointX;
    public int pointY;
    public int pointStyle;  // 1: 无障碍  2： 有障碍  3：当前点   4： 有碰撞点

    private boolean mIsConvert = false;     // 是否经过转换成屏幕坐标，防止多次转换

    public MapPoint() {
    }

    public MapPoint(int pointX, int pointY, int pointStyle) {
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

    public static MapPoint convertToScreenPoint(MapPoint point, float scale){
        if(point == null){
            return point;
        }
        if(!point.mIsConvert && scale>0 && scale!=1.0f){
            point.pointX = (int) (point.pointX*scale);
            point.pointY = (int) (point.pointY*scale);
            point.mIsConvert = true;
        }
        return point;
    }

    public static List<MapPoint> convertToScreenPoints(List<MapPoint> points, float scale){
        if(points == null || points.size() == 0){
            return points;
        }

        int len = points.size();
        for(int i=0; i<len; i++){
            MapPoint.convertToScreenPoint(points.get(i), scale);
        }
        return points;
    }

}
