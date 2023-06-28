package com.example.rtsp.client;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rtsp.client.player.SimpleTextureViewPlayer;
import com.example.rtsp.client.player.anim.CustomValueAnimator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SimpleTextureViewPlayer mVideoView;
    private MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initPlayer();
        FollowMeStudyManager.getIntance().setWeakReferencePlayer(mVideoView);
    }

    private void init() {
        mVideoView = findViewById(R.id.video_view);
    }

    private void initPlayer() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPrepared...");
                mp.start();
            }
        });
        mVideoView.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.i(TAG, "onVideoSizeChanged...width: " + width + ", height: " + height);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "onCompletion...");
            }
        });
    }

    public void onClickStartVideo(View view) {
        showLog("onClickStartVideo..." + mVideoView.getDuration());
        // 开始播放
        String url = "rtsp://192.168.3.93:1234";
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        mVideoView.startPlay(url);
    }

    public void onClickStopVideo(View view) {
        // 停止播放
        showLog("onClickStopVideo..." + mVideoView.getDuration());
    }

    public void onClickPauseVideo(View view) {
        showLog("onClickPauseVideo..." + mVideoView.getDuration());
        // 暂停播放
        mVideoView.pause();
    }

    public void onClickResumeVideo(View view) {
        showLog("onClickResumeVideo..." + mVideoView.getDuration());
        // 继续播放
        degree+=90;
        mVideoView.setRotation(degree%360);
    }

    private int degree = 0;
    public void onClickGetVideoInfo(View view) {
        // 正常旋转动画
        CustomValueAnimator customValueAnimator = new CustomValueAnimator(){

            @Override
            protected void onAnimProgress(float percent, int value) {
                mVideoView.setRotation(value);
            }

            @Override
            protected void onAnimEnd() {
                degree+=90;
            }
        };
        customValueAnimator.setParams(degree, degree+90);
        customValueAnimator.start();

        translate(degree+90);
    }

    private void translate(int degree) {



        int halfWidth = mVideoView.getWidth() / 2;
        int halfHeight = mVideoView.getHeight() / 2;
        int scrollX = Math.abs((halfHeight - halfWidth + 1) / 4);

        Log.i(TAG, "translate call, degree: " + degree + ", halfWidth:" + halfWidth + ", halfHeight:" + halfHeight+", scrollX:"+scrollX);

        if (degree % 360 == 0 || degree % 360 == 180) {

            float startX = 0;
            float[] x = {startX + 0f};
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mVideoView, "translationX", x);
            objectAnimatorX.setDuration(1000);
            objectAnimatorX.start();

            float[] y = {startX + 0f};
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mVideoView, "translationY", y);
            objectAnimatorY.setDuration(1000);
            objectAnimatorY.start();

        } else {
            float startX = 0;
            float[] x = {startX + 0f, startX - scrollX, startX - scrollX * 2, startX - scrollX * 3, startX - scrollX * 4};
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mVideoView, "translationX", x);
            objectAnimatorX.setDuration(1000);
            objectAnimatorX.start();

            float[] y = {startX + 0f, startX + scrollX, startX + scrollX * 2, startX + scrollX * 3, startX + scrollX * 4};
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mVideoView, "translationY", y);
            objectAnimatorY.setDuration(1000);
            objectAnimatorY.start();
        }
    }


    private void showLog(String message) {
        Log.i("MainActivity", message);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FollowMeStudyManager.getIntance().setWeakReferencePlayer(null);
    }
}