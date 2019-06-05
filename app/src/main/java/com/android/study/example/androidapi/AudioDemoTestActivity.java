package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 录音播放等相关功能
 */
public class AudioDemoTestActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                Log.i("lvjie", "handleMessage--> time="+mMediaPlayer.getCurrentPosition()/1000);
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AudioDemoTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_demo_test);

        initView();
    }

    private void initView(){
        findViewById(R.id.btn_audio_start_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始播放
                startPlayAudio();
            }
        });

        findViewById(R.id.btn_audio_stop_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止播放
                stopPlayAudio();
            }
        });

    }

    private void startPlayAudio(){
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }else{
            mMediaPlayer.reset();
        }
        File file = new File(getFilesDir().getPath()+File.separator+"lovewholelife.mp3");

        try {
            if(file.exists()){
                Log.i("lvjie", file.getPath()+" is exist...");
                mMediaPlayer.setDataSource(file.getPath());
                mMediaPlayer.prepareAsync();
            }else{
                Log.i("lvjie", file.getPath()+" is not exist...");
                AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.lovewholelife);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepareAsync();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("lvjie", "onError-->  what="+what+"  extra="+extra);
                return false;
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("lvjie", "onCompletion()....");
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e("lvjie", "onPrepared()....");
                mMediaPlayer = mp;
                mMediaPlayer.start();

                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        });



    }


    private void stopPlayAudio(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }


//    private static class MyHandler extends Handler{
//
//        // 统计播放时间
//        private static short MSY_TYPE_TOTAL_AUDIO_PALYER_TIME = 1;
//
//        private  WeakReference<MediaPlayer> mediaPlayerWeakReference;
//
//        public MyHandler(MediaPlayer mediaPlayer){
//            super();
//            mediaPlayerWeakReference = new WeakReference<>(mediaPlayer);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            if(mediaPlayerWeakReference.get() != null){
//                Log.i("lvjie", "handleMessage-->time="+mediaPlayerWeakReference.get().getCurrentPosition());
//            }
//        }
//    }

}
