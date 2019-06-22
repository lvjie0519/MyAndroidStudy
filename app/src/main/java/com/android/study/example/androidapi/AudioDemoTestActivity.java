package com.android.study.example.androidapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioRecord;
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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;

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

    // AudioRecord  录音文件
    private String audioRecordFileName = "";
    private AudioRecord mAudioRecord;
    private boolean mIsAudioRecording = false;

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
        audioRecordFileName = getFilesDir()+File.separator+"bb.mp3";
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

        findViewById(R.id.btn_audio_start_media_recorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始录音, 需要动态获取权限
                rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if(granted){
                            startMediaRecord();
                        }
                    }
                });

            }
        });

        findViewById(R.id.btn_audio_stop_media_recorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束录音
                stopMediaRecord();
            }
        });

        findViewById(R.id.btn_audio_start_audio_recorder).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.btn_audio_stop_audio_recorder).setOnClickListener(new View.OnClickListener() {
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

    private void startMediaRecord(){
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

    private void stopMediaRecord(){
        // 结束录音
        Log.i("lvjie", "stopMediaRecord...");
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }


    private void startAudioRecord() {

        // 开始录音
        Log.i("lvjie", "recordFileName="+audioRecordFileName);
        File audioFile = new File(audioRecordFileName);
        if(audioFile.exists()){
            audioFile.delete();
        }
        try {
            audioFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopAudioRecord();

        // 音频源：指的是从哪里采集音频。这里我们当然是从麦克风采集音频，所以此参数的值为MIC
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 采样率：音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。
        int sampleRateInHz = 9500;
        // 声道设置：android支持双声道立体声和单声道。MONO单声道，STEREO立体声
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        // 编码制式和采样大小   android支持的采样大小16bit 或者8bit。当然采样大小越大，那么信息量越多，音质也越高，现在主流的采样大小都是16bit，在低质量的语音传输的时候8bit足够了。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 采集数据需要的缓冲区的大小，如果不知道最小需要的大小可以在getMinBufferSize()查看。
        final int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

        if(bufferSizeInBytes == AudioRecord.ERROR_BAD_VALUE){
            // channelConfig错误或sampleRateInHz错误
            Log.i("lvjie", "channelConfig错误或sampleRateInHz错误");
            return;
        }else if(bufferSizeInBytes == AudioRecord.ERROR){
            // sampleRateInHz错误
            Log.i("lvjie", "sampleRateInHz错误");
            return;
        }

        // int audioSource, int sampleRateInHz, int channelConfig, int audioFormat,int bufferSizeInBytes
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

        new Thread(new Runnable() {
            @Override
            public void run() {

                DataOutputStream outputStream = null;
                try {
                    outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioRecordFileName)));
                    byte[] buffer = new byte[bufferSizeInBytes];
                    //开始录音
                    mAudioRecord.startRecording();
                    mIsAudioRecording = true;
                    while (mIsAudioRecording) {
                        int readResult = mAudioRecord.read(buffer, 0, buffer.length);
                        for (int i = 0; i < readResult; i++) {
                            outputStream.write(buffer[i]);
                        }
                        Log.i("lvjie", Thread.currentThread().getName()+"  录制中....");
                    }
                    Log.i("lvjie", Thread.currentThread().getName()+"  录制结束....");
                    stopAudioRecord();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    private synchronized void stopAudioRecord() {

        // 结束录音
        Log.i("lvjie", Thread.currentThread().getName()+"  stopAudioRecord...mIsAudioRecording="+mIsAudioRecording);

        if(mIsAudioRecording){
            mIsAudioRecording = false;
            if(mAudioRecord != null){
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }

    }


}
