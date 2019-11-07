package com.android.study.example.androidapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;

import rx.functions.Action1;

/**
 * 录音播放等相关功能
 * 获取分贝值
 * https://blog.csdn.net/cjh_android/article/details/51341004
 * https://blog.csdn.net/greatpresident/article/details/38402147
 */
public class AudioDemoTestActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer = null;

    // 录音文件
    private String recordFileName = "";
    private MediaRecorder mRecorder;
    private RxPermissions rxPermissions;
    private boolean mIsMediaRecording = false;

    // AudioRecord  录音文件
    private String audioRecordFileName = "";
    private AudioRecord mAudioRecord;
    private boolean mIsAudioRecording = false;

    private AudioTrack mAudioTrack;
    private boolean mIsAudioTrackPlaying = false;

    // 显示录音分贝
    private TextView mTvRecordDB;
    private int maxRatio = 0;
    private int maxDb = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg != null && msg.what == 1 && mMediaPlayer != null && mMediaPlayer.isPlaying()){
                Log.i("lvjie", "handleMessage--> time="+mMediaPlayer.getCurrentPosition()/1000);
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }

            if(msg != null && msg.what == 2 && mRecorder != null && mIsMediaRecording){
                int db = getMediaRecordDb();
                mTvRecordDB.setText("录音分贝： "+db+"  最大db"+maxDb+"  最大ratio"+maxRatio);
                mHandler.sendEmptyMessageDelayed(2, 500);
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
        audioRecordFileName = getFilesDir()+File.separator+"bb.raw";
        rxPermissions = RxPermissions.getInstance(this);
    }

    private void initView(){

        this.mTvRecordDB = (TextView) findViewById(R.id.tv_show_db);

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

        findViewById(R.id.btn_audio_start_play_track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始播放
                startPlayTrack();
            }
        });

        findViewById(R.id.btn_audio_stop_play_track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束播放
                stopPlayTrack();
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

        // 播放AudioRecordl录制的音频,还有问题
//        String newFileName = getFilesDir()+File.separator+"bbb.raw";
//        copyWaveFile(audioRecordFileName, newFileName);
//        File file = new File(newFileName);


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
        mIsMediaRecording = true;
        mHandler.sendEmptyMessageDelayed(2, 500);

    }

    private void stopMediaRecord(){
        // 结束录音
        Log.i("lvjie", "stopMediaRecord...");
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        mIsMediaRecording = false;
    }

    private int getMediaRecordDb(){

        int db = 0;

        if(mIsMediaRecording && mRecorder != null){
            double ratio = (double)mRecorder.getMaxAmplitude() /1;
            db = 0;// 分贝
            if (ratio > 1){
                db = (int) (20 * Math.log10(ratio));
            }
            if(db>=maxDb){
                maxDb = db;
            }
            if(ratio>=maxRatio){
                maxRatio = (int) ratio;
            }
            Log.i("lvjie","分贝值："+db+"   ratio="+ratio);
        }

        return db;
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

    /**
     * 之所以使用同步方法，是因为防止多次点击录音，录音在写文件的过程中，是在子线程中进行的，需要考虑线程的同步
     */
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

    /**
     * MediaPlayer 是不能直接播放 AudioRecorder的录音的音频文件的，需要经过转换
     * @param inFileName
     * @param outFileName
     */
    private void copyWaveFile(String inFileName, String outFileName) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long sampleRateInHz = 9500;
        long longSampleRate = sampleRateInHz;
        int channels = 2;
        long byteRate = 16 * sampleRateInHz * channels / 8;

        byte[] data = new byte[1024];
        try
        {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(outFileName);

            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while(in.read(data) != -1)
            {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
     * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有
     * 自己特有的头文件。
     */
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }


    private void startPlayTrack(){

        // 音频源：指的是从哪里采集音频。这里我们当然是从麦克风采集音频，所以此参数的值为MIC
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 采样率：音频的采样频率，每秒钟能够采样的次数，采样率越高，音质越高。
        int sampleRateInHz = 9500;
        // 声道设置：android支持双声道立体声和单声道。MONO单声道，STEREO立体声
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        // 编码制式和采样大小   android支持的采样大小16bit 或者8bit。当然采样大小越大，那么信息量越多，音质也越高，现在主流的采样大小都是16bit，在低质量的语音传输的时候8bit足够了。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 采集数据需要的缓冲区的大小，如果不知道最小需要的大小可以在getMinBufferSize()查看。
        final int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

        if(mAudioTrack != null){
            stopPlayTrack();
        }

        // int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, AudioTrack.MODE_STREAM);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream inputStream = null;
                try {
                    inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(audioRecordFileName)));
                    byte[] buffer = new byte[bufferSizeInBytes];
                    int readCount = inputStream.read(buffer);
                    Log.i("lvjie", Thread.currentThread().getName()+"  开始播放   readCount="+readCount);
                    mIsAudioTrackPlaying = true;
                    mAudioTrack.play();
                    while (mIsAudioTrackPlaying && readCount > 0) {
                        int writeResult = mAudioTrack.write(buffer, 0, readCount);
                        if (writeResult >= 0) {
                            //success
                        } else {
                            //fail
                            //丢掉这一块数据

                        }
                        readCount = inputStream.read(buffer);
                        Log.i("lvjie", Thread.currentThread().getName()+"  播放   readCount="+readCount);
                    }
                    Log.i("lvjie", Thread.currentThread().getName()+"  停止播放   readCount="+readCount);
                    stopPlayTrack();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void stopPlayTrack(){

        Log.i("lvjie", Thread.currentThread().getName()+"  stopPlayTrack...");
        mIsAudioTrackPlaying = false;
        if(mAudioTrack != null){
            if(mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
                mAudioTrack.flush();
                mAudioTrack.stop();
            }
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

}
