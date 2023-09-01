package com.android.study.example.uidemo.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.android.study.example.R;
import com.android.study.example.utils.ToastUtil;

public class AnimationDemoActivity extends AppCompatActivity {

    private View scaleAnimView;

    private ObjectAnimator objectAnimatorX;
    private ObjectAnimator objectAnimatorY;

    private View scanView;
    private Animation animation;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AnimationDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);

        initView();
    }

    private void initView(){
        scaleAnimView = findViewById(R.id.view_anim);
        findViewById(R.id.btn_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPopsAnimTrans();
            }
        });

        scaleAnimView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(AnimationDemoActivity.this, "我被点击了...");
            }
        });

        scanView = findViewById(R.id.iv_scan_ing);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_scan_circular);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);     // 代码设置下，旋转不会停顿



        findViewById(R.id.btn_start_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanView.getAnimation() == null){
                    scanView.startAnimation(animation);
                }
            }
        });

        findViewById(R.id.btn_stop_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanView.getAnimation() != null){
                    scanView.clearAnimation();
                }
            }
        });


    }

    // 区间动画-缩放
    private void startScaleAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale_animation);
        scaleAnimView.startAnimation(animation);
    }

    private int startX = 0;
    private int startY = 0;
    // 属性动画-平移
    private void startPopsAnimTrans(){
        if(objectAnimatorX == null){
            float [] x= {startX+0f,startX+60f,startX+120f, startX+240f};
            float [] y= {startY+0f,startY+30f,startY+220f,startY+90f};
//            objectAnimatorX = ObjectAnimator.ofFloat(scaleAnimView,"alpha", 1f, 0.5f, 1f);
            objectAnimatorX = ObjectAnimator.ofFloat(scaleAnimView,"translationX", x);
            objectAnimatorX.setDuration(2000);
            objectAnimatorY = ObjectAnimator.ofFloat(scaleAnimView,"translationY", y);
            objectAnimatorY.setDuration(2000);
        }else{
            float [] x= {startX+0f,startX+60f,startX+120f, startX+240f};
            float [] y= {startY+0f,startY+30f,startY+220f,startY+90f};
            objectAnimatorX.setFloatValues(x);
            objectAnimatorY.setFloatValues(y);
        }

        objectAnimatorX.start();
        objectAnimatorY.start();

        objectAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("lvjielvjie", "getAnimatedValue: "+animation.getAnimatedValue());
            }
        });

        startX+=240;
        startY+=90f;
    }
}
