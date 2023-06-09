package com.example.rtsp.server;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;

/**
 * https://github.com/fyhertz/libstreaming
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private SurfaceView mSurfaceView;
    private Session mSession;

    private Session.Callback mSessionCallback = new Session.Callback() {
        @Override
        public void onBitrateUpdate(long bitrate) {
            showLog("onBitrateUpdate call, bitrate: " + bitrate);
        }

        @Override
        public void onSessionError(int reason, int streamType, Exception e) {
            e.printStackTrace();
            showLog("onSessionError call, reason: " + reason + ", streamType: " + streamType);
        }

        @Override
        public void onPreviewStarted() {
            showLog("onPreviewStarted call");
        }

        @Override
        public void onSessionConfigured() {
            showLog("onSessionConfigured call");
            mSession.start();
        }

        @Override
        public void onSessionStarted() {
            showLog("onSessionStarted call");
        }

        @Override
        public void onSessionStopped() {
            showLog("onSessionStopped call");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        PermissionsUtils.getInstance().checkPermissions(this, permissions, permissionsResult);
    }

    private void initView(){
        mSurfaceView = findViewById(R.id.surface);
    }

    private PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void requestPermissions(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
        }

        @Override
        public void passPermissons() {
            initRstpServer();
        }

        @Override
        public void forbitPermissons() {

        }

        @Override
        public void positiveClick(Intent intent) {

        }
    };

    private void initRstpServer() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
        editor.commit();

        mSession = SessionBuilder.getInstance()
                .setCallback(mSessionCallback)
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(0)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setAudioQuality(new AudioQuality(16000, 32000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
                .setVideoQuality(new VideoQuality(320,240,20,500000))
                .build();

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mSession.startPreview();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                mSession.stop();
            }
        });

        this.startService(new Intent(this,RtspServer.class));
    }

    private void showLog(String message){
        Log.i(TAG, message);
    }
}