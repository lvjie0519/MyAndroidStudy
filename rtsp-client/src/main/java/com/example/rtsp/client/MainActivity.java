package com.example.rtsp.client;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CustomVideoView mVideoView;
    private MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mVideoView = findViewById(R.id.video_view);

        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);

        // 设置监听视频装载完成的事件
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                showLog("监听视频装载完成..." + mVideoView.getDuration());
                mVideoView.start();
                mVideoView.resizeVideo(1080, 1868);
            }
        });

        // 设置监听播放完成的事件
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showLog("监听播放完成...");
                mVideoView.stopPlayback();
            }
        });
    }

    public void onClickStartVideo(View view) {
        showLog("onClickStartVideo..." + mVideoView.getDuration());
        // 开始播放
        String url = "rtsp://192.168.3.93:1234";
        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.requestFocus();
    }

    public void onClickStopVideo(View view) {
        // 停止播放
        showLog("onClickStopVideo..." + mVideoView.getDuration());
        mVideoView.stopPlayback();
    }

    public void onClickPauseVideo(View view) {
        showLog("onClickPauseVideo..." + mVideoView.getDuration());
        // 暂停播放
        mVideoView.pause();
    }

    public void onClickResumeVideo(View view) {
        showLog("onClickResumeVideo..." + mVideoView.getDuration());
        // 继续播放
        mVideoView.start();
    }

    public void onClickGetVideoInfo(View view) {
        int duration = mVideoView.getDuration();
        int currentPosition = mVideoView.getCurrentPosition();
        showLog("duration: " + duration + ", currentPosition: " + currentPosition);
    }


    private void showLog(String message) {
        Log.i("MainActivity", message);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 停止播放
        mVideoView.stopPlayback();
        // 释放资源
        mVideoView.suspend();
    }
}