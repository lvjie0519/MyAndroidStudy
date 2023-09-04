package com.android.study.example.game.dadishu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.study.example.R;

public class WhackMoleView extends RelativeLayout {
    private static final String TAG = "WhackMoleView";

    private View mMouseView;
    private ObjectAnimator mMouseViewAnimator;
    private int mStartY = 0;
    private int mDivide = -150;

    public WhackMoleView(Context context) {
        this(context, null);
    }

    public WhackMoleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhackMoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_whack_mole, this, false);
        addView(view);

        mMouseView = view.findViewById(R.id.view_mouse);
    }

    public void startBulletViewAnimatorOut() {
        if (mMouseViewAnimator == null) {
            float[] y = {mStartY, mStartY + mDivide};
            mMouseViewAnimator = ObjectAnimator.ofFloat(mMouseView, "translationY", y);
            mMouseViewAnimator.setDuration(200);

            mMouseViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.i(TAG, "getAnimatedValue: " + animation.getAnimatedValue());
//                    if(((Float)animation.getAnimatedValue()) > 600){
//                        objectAnimatorY.cancel();
//                    }
                }
            });
        } else {
            float[] y = {mStartY, mStartY + mDivide};
            mMouseViewAnimator.setFloatValues(y);
        }

        mMouseViewAnimator.start();
    }


    public void startBulletViewAnimatorIn() {
        if (mMouseViewAnimator == null) {
            float[] y = {mStartY + mDivide, mStartY};
            mMouseViewAnimator = ObjectAnimator.ofFloat(mMouseView, "translationY", y);
            mMouseViewAnimator.setDuration(200);

            mMouseViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.i(TAG, "getAnimatedValue: " + animation.getAnimatedValue());
//                    if(((Float)animation.getAnimatedValue()) > 600){
//                        objectAnimatorY.cancel();
//                    }
                }
            });
        } else {
            float[] y = {mStartY + mDivide, mStartY};
            mMouseViewAnimator.setFloatValues(y);
        }

        mMouseViewAnimator.start();
    }

    public void destory() {
        if (mMouseViewAnimator != null) {
            mMouseViewAnimator.cancel();
            mMouseViewAnimator = null;
        }
    }
}
