package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;

public class SoundPoolTestActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private SoundPool.OnLoadCompleteListener mOnLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    };

    private MediaPlayer mBeatMouseBgPlayer;

    private int mBeatMouseSuccessSoundId = -1;
    private int mBeatMouseFailedSoundId = -1;
    private int mMouseOutSoundId = -1;
    private int mBeatMouseEndSoundId = -1;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SoundPoolTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_pool_test);

        initSoundPool();
    }

    private void initSoundPool(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                    .build();
        } else {
            mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener(mOnLoadCompleteListener);
    }

    public void btnOnClickBgMusic(View view) {
        if (mBeatMouseBgPlayer == null) {
            mBeatMouseBgPlayer = MediaPlayer.create(this, R.raw.beat_mouse_bg_music);
            mBeatMouseBgPlayer.setLooping(true);
            mBeatMouseBgPlayer.setVolume(0.4f, 0.4f);
        }

        if (!mBeatMouseBgPlayer.isPlaying()) {
            mBeatMouseBgPlayer.start();
        }
    }

    public void btnOnClickBeatFailed(View view) {
        if (mBeatMouseFailedSoundId == -1) {
            mBeatMouseFailedSoundId = mSoundPool.load(this, R.raw.beat_mouse_failed, 1);
        } else {
            mSoundPool.play(mBeatMouseFailedSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void btnOnClickBeatSuccess(View view) {
        if (mBeatMouseSuccessSoundId == -1) {
            mBeatMouseSuccessSoundId = mSoundPool.load(this, R.raw.beat_mouse_success, 1);
        } else {
            mSoundPool.play(mBeatMouseSuccessSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void btnOnClickMouseOut(View view) {
        if (mMouseOutSoundId == -1) {
            mMouseOutSoundId = mSoundPool.load(this, R.raw.mouse_out, 1);
        } else {
            mSoundPool.play(mMouseOutSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void btnOnClickGameEnd(View view) {
        if (mBeatMouseEndSoundId == -1) {
            mBeatMouseEndSoundId = mSoundPool.load(this, R.raw.beat_mouse_game_end, 1);
        } else {
            mSoundPool.play(mBeatMouseEndSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void stopBeatMouseBgMusic() {
        if(mBeatMouseBgPlayer == null || !mBeatMouseBgPlayer.isPlaying()){
            return;
        }

        mBeatMouseBgPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBeatMouseBgMusic();
    }
}