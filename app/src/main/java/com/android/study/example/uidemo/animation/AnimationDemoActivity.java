package com.android.study.example.uidemo.animation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.study.example.R;

public class AnimationDemoActivity extends AppCompatActivity {

    private View scaleAnimView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AnimationDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);

        startScaleAnimation();
    }

    private void startScaleAnimation(){
        scaleAnimView = findViewById(R.id.view_scale_anim);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale_animation);
        scaleAnimView.startAnimation(animation);
    }
}
