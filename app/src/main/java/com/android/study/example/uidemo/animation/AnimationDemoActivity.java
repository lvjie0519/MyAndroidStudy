package com.android.study.example.uidemo.animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.study.example.R;
import com.android.study.example.utils.ToastUtil;

public class AnimationDemoActivity extends AppCompatActivity {

    private View scaleAnimView;

    private ObjectAnimator objectAnimatorX;
    private ObjectAnimator objectAnimatorY;

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

        startX+=240;
        startY+=90f;
    }
}
