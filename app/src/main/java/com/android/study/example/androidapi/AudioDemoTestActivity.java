package com.android.study.example.androidapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import rx.functions.Action1;

/**
 * 录音播放等相关功能
 */
public class AudioDemoTestActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer = null;

    // 录音文件
    private String recordFileName = "";
    private MediaRecorder mRecorder;
    private RxPermissions rxPermissions;

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

        initData();
        initView();
    }

    private void initData(){
        recordFileName = getFilesDir()+File.separator+"aa.mp3";
        rxPermissions = RxPermissions.getInstance(this);
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

        findViewById(R.id.btn_audio_start_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始录音, 需要动态获取权限
                rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if(granted){
                            startAudioRecord();
                        }
                    }
                });

            }
        });

        findViewById(R.id.btn_audio_stop_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束录音
                stopAudioRecord();
            }
        });

    }

    private void startPlayAudio(){
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }else{
            mMediaPlayer.reset();
        }
//        File file = new File(getFilesDir().getPath()+File.separator+"lovewholelife.mp3");
        File file = new File(recordFileName);

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

    private void startAudioRecord(){
        // 开始录音
        Log.i("lvjie", "recordFileName="+recordFileName);
        File audioFile = new File(recordFileName);
        if(audioFile.exists()){
            audioFile.delete();
        }
        try {
            audioFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mRecorder == null){
            mRecorder = new MediaRecorder();
        }else{
            mRecorder.reset();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);        // 设置声音来源, 需要在setOutputFormat之前调用

        // 设置所录制声音编码位率、采样率、音频通道数等,  在prepare之前调用即可
        mRecorder.setAudioEncodingBitRate(16);        //
        mRecorder.setAudioSamplingRate(9500);        //  format for the audio recording  AAC: 8 to 96 kHz   AMRNB is 8kHz  AMRWB is 16kHz
        mRecorder.setAudioChannels(2);              // 1 or 2

        // https://blog.csdn.net/dodod2012/article/details/80474490  两者关系
        // 音频文件的格式, after setAudioSource()/setVideoSource() but before prepare()
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        // 编码格式, after setOutputFormat() but before prepare()
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // 设置录制音频文件的保存位置, after setOutputFormat() but before prepare().
        mRecorder.setOutputFile(recordFileName);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void stopAudioRecord(){
        // 结束录音
        Log.i("lvjie", "stopAudioRecord...");
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }


}
