package com.android.study.example.game.dadishu.stickfigure;

public class FigureArea {
    private float mLeftX = Integer.MAX_VALUE;
    private float mRightX = Integer.MIN_VALUE;
    private float mTopY = Integer.MAX_VALUE;
    private float mBottomY = Integer.MIN_VALUE;

    public FigureArea(float leftX, float rightX, float topY, float bottomY) {
        this.mLeftX = leftX;
        this.mRightX = rightX;
        this.mTopY = topY;
        this.mBottomY = bottomY;
    }

    public float getLeftX() {
        return mLeftX;
    }

    public void setLeftX(float leftX) {
        this.mLeftX = leftX;
    }

    public float getRightX() {
        return mRightX;
    }

    public void setRightX(float rightX) {
        this.mRightX = rightX;
    }

    public float getTopY() {
        return mTopY;
    }

    public void setTopY(float topY) {
        this.mTopY = topY;
    }

    public float getBottomY() {
        return mBottomY;
    }

    public void setBottomY(float bottomY) {
        this.mBottomY = bottomY;
    }

    public String toJson() {
        return toString();
    }

    @Override
    public String toString() {
        return "{" +
                "leftX=" + mLeftX +
                ", rightX=" + mRightX +
                ", topY=" + mTopY +
                ", bottomY=" + mBottomY +
                '}';
    }
}
