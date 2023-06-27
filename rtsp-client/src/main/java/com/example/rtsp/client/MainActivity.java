package com.example.rtsp.client;

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
        int duration = mVideoView.getDuration();
        int currentPosition = mVideoView.getCurrentPosition();
        showLog("duration: " + duration + ", currentPosition: " + currentPosition);
        degree+=90;
        mVideoView.postRotate(degree%360);
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