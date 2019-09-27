package com.android.study.example.uidemo.mapsweeper;

public class MapPoint {

    public int pointX;
    public int pointY;
    public int pointStyle;  // 1: 无障碍  2： 有障碍  3：当前点   4： 有碰撞点

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

}
