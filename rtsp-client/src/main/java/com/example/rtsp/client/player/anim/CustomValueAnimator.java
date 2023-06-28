package com.example.rtsp.client.player.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;

public abstract class CustomValueAnimator extends ValueAnimator {
    private static final String TAG = "CustomValueAnimator";

    // 动画时间
    public static final int ANIMATOR_DURATION = 1000;

    private int mStartValue;
    private int mEndValue;
    private int mDiffValue;

    private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float percent = (Float) animation.getAnimatedValue();
            onAnimProgress(percent, (int) (mStartValue+percent*mDiffValue));
        }
    };

    private AnimatorListener mAnimatorListener = new AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            Log.i(TAG, "onAnimationStart call.");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Log.i(TAG, "onAnimationEnd call.");
            onAnimEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.i(TAG, "onAnimationCancel call.");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.i(TAG, "onAnimationRepeat call.");
        }
    };

    public void setParams(int startValue, int endValue) {
        this.mStartValue = startValue;
        this.mEndValue = endValue;
        this.mDiffValue = this.mEndValue-this.mStartValue;

        setFloatValues(0, 1f);
        setDuration(ANIMATOR_DURATION);
        addUpdateListener(mAnimatorUpdateListener);
        addListener(mAnimatorListener);
    }

    public void updateParams(int startValue, int endValue) {
        this.mStartValue = startValue;
        this.mEndValue = endValue;
        this.mDiffValue = this.mEndValue-this.mStartValue;
    }

    protected abstract void onAnimProgress(float percent, int value);
    protected abstract void onAnimEnd();
}
