package com.android.study.example.uidemo.mapsweeper;

public class MapPoint {

    public int pointX;
    public int pointY;
    public int pointStyle;

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
