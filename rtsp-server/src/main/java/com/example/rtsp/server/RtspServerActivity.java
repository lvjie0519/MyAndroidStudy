package com.example.rtsp.server;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;

import com.example.rtsp.server.utils.AutoSizeUtils;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * https://github.com/fyhertz/libstreaming
 *
 * 源码解读
 * https://www.freesion.com/article/6241271232/
 */
public class RtspServerActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private SurfaceView mSurfaceView;
    private Session mSession;

    private OrientationEventListener mOrientationEventListener;
    private int mLastOrientation;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RtspServerActivity.class);
        context.startActivity(intent);
    }

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
            // rtsp://192.168.3.109:1234
            String rtspServer = "rtsp://"+MGWebSocketClientManager.getInstance().getCurrentHostAddress()+":1234";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("rtspServer", rtspServer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showLog("sendMessage: "+jsonObject.toString());
            MGWebSocketClientManager.getInstance().sendMessage(jsonObject.toString());
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
        setContentView(R.layout.activity_rtsp_server);

        initView();
        addListener();
        PermissionsUtils.getInstance().checkPermissions(this, permissions, permissionsResult);
    }

    private void initView(){
        mSurfaceView = findViewById(R.id.surface);
    }

    private void addListener() {
        mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                int resultOrientation = orientation;
                if (resultOrientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }
                // 只检测是否有四个角度的改变
                if (resultOrientation > 340 || resultOrientation < 20) {
                    // 0度
                    resultOrientation = 0;
                } else if (resultOrientation > 70 && resultOrientation < 110) {
                    // 90度
                    resultOrientation = 90;
                } else if (resultOrientation > 160 && resultOrientation < 200) {
                    // 180度
                    resultOrientation = 180;
                } else if (resultOrientation > 250 && resultOrientation < 290) {
                    // 270度
                    resultOrientation = 270;
                } else {
                    resultOrientation = mLastOrientation;
                }

                if (mLastOrientation != resultOrientation) {
                    Log.i(TAG, "onOrientationChanged call, orientation:" + orientation + ", resultOrientation: " + resultOrientation);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("MSG_TYPE", "screen_orientation");
                        jsonObject.put("last_orientation", mLastOrientation);
                        jsonObject.put("current_orientation", resultOrientation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mLastOrientation = resultOrientation;
                    MGWebSocketClientManager.getInstance().sendMessage(jsonObject.toString());
                }
            }
        };

        if (mOrientationEventListener.canDetectOrientation()) {
            Log.v(TAG, "Can detect orientation");
            mOrientationEventListener.enable();
        } else {
            Log.v(TAG, "Cannot detect orientation");
            mOrientationEventListener.disable();
        }
    }

    private PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void requestPermissions(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(RtspServerActivity.this, permissions, requestCode);
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
                .setPreviewOrientation(90)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setAudioQuality(new AudioQuality(16000, 32000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
//                .setVideoQuality(new VideoQuality(320,240,20,500000))
                .setVideoQuality(new VideoQuality(1280,720,20,500000))
//                .setVideoQuality(new VideoQuality(AutoSizeUtils.dp2px(this, 200),AutoSizeUtils.dp2px(this, 400),20,500000))
//                .setVideoQuality(new VideoQuality(160,96,20,500000))
                .setCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .build();

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                showLog("surfaceCreated call");
                mSession.startPreview();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                showLog("surfaceChanged call");
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                showLog("surfaceDestroyed call");
                mSession.stop();
            }
        });

        this.startService(new Intent(this,RtspServer.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationEventListener.disable();
    }

    private void showLog(String message){
        Log.i(TAG, message);
    }
}