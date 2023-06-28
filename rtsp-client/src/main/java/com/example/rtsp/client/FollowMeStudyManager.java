package com.example.rtsp.client;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.example.rtsp.client.player.SimpleTextureViewPlayer;
import com.example.rtsp.client.player.anim.CustomValueAnimator;
import com.mgtv.tvos.link.MGLinkFactory;
import com.mgtv.tvos.link.interfaces.IClientProxy;
import com.mgtv.tvos.link.websocket.MGWebSocketParamsConfig;
import com.mgtv.tvos.link.websocket.interfaces.IMGWebSocketServer;
import com.mgtv.tvos.link.websocket.interfaces.MGWebSocketServerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 跟我学管理类
 */
public class FollowMeStudyManager {
    private static final String TAG = "FollowMeStudyManager";
    private static final String VideoView_TAG = "VideoView";
    private static final String QrView_TAG = "QrView";

    private static FollowMeStudyManager sInstance;

    private boolean mInit = false;
    private Context mContext;
    private IMGWebSocketServer mgWebSocketServer;

    private Handler mMainHandler;

    private WeakReference<SimpleTextureViewPlayer> mWeakReferencePlayer;

    // 旋转和平移动画
    private CustomValueAnimator mCustomValueAnimator;
    private ObjectAnimator mObjectAnimatorX;
    private ObjectAnimator mObjectAnimatorY;

    private FollowMeStudyManager(){}

    public static FollowMeStudyManager getIntance() {
        if (sInstance == null) {
            synchronized (FollowMeStudyManager.class) {
                if (sInstance == null) {
                    sInstance = new FollowMeStudyManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        showLog("init call, mInit:" + mInit);
        if (mInit) {
            return;
        }

        mInit = true;
        mContext = context;
        initMainHandler(context);
        initWebSocketServer(context);
    }

    private void initMainHandler(Context context){
        mMainHandler = new Handler(context.getMainLooper());
    }

    private void initWebSocketServer(Context context) {
        MGWebSocketParamsConfig paramsConfig = new MGWebSocketParamsConfig.Builder(context).build();
        try {
            mgWebSocketServer = MGLinkFactory.createMGWebSocketServer(paramsConfig, mgWebSocketServerListener);
            mgWebSocketServer.startServer();
            showLog("WebSocketServer start success."+mgWebSocketServer.getServerInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MGWebSocketServerListener mgWebSocketServerListener = new MGWebSocketServerListener() {
        @Override
        public void onConnect(IClientProxy client) {
            showLog("onConnect..." + client.toString());
        }

        @Override
        public void onDisConnect(IClientProxy client) {
            showLog("onDisConnect..." + client.toString());
        }

        @Override
        public void onReceiveMessage(IClientProxy client, String message) {
            showLog(client.toString() + ": " + message);
            // 接受客户端发送过来的摄像头 rtsp链接
            if (TextUtils.isEmpty(message)) {
                return;
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(jsonObject != null){
                try {
                    String rtspServer = jsonObject.getString("rtspServer");
                } catch (JSONException e) {

                }


                try {
                    int lastOrientation = jsonObject.getInt("last_orientation");
                    int currentOrientation = jsonObject.getInt("current_orientation");
                    updatePlayerByClientScreen(lastOrientation, currentOrientation);
                } catch (JSONException e) {

                }

            }

        }

        @Override
        public void onError(String errorCode, String errorMsg) {
            showLog("errorCode: " + errorCode + "  errorMsg: " + errorMsg);
        }
    };

    public void setWeakReferencePlayer(SimpleTextureViewPlayer player) {
        this.mWeakReferencePlayer = new WeakReference<>(player);
    }

    private void updatePlayerByClientScreen(int lastOrientation, int currentOrientation) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "updatePlayerOrientation call,  lastOrientation: " + lastOrientation + ", currentOrientation:" + currentOrientation);
                if (mWeakReferencePlayer.get() != null) {
                    updatePlayerRotation(mWeakReferencePlayer.get(), lastOrientation, currentOrientation);
                    updatePlayerPosition(mWeakReferencePlayer.get(), lastOrientation, currentOrientation);
                }
            }
        });
    }

    private void updatePlayerRotation(SimpleTextureViewPlayer player, int lastOrientation, int currentOrientation) {
        if(mCustomValueAnimator == null){
            mCustomValueAnimator = new CustomValueAnimator() {
                @Override
                protected void onAnimProgress(float percent, int value) {
                    player.setRotation(value);
                }

                @Override
                protected void onAnimEnd() {

                }
            };

            mCustomValueAnimator.setParams(lastOrientation, currentOrientation);
        }

        mCustomValueAnimator.updateParams(lastOrientation, currentOrientation);
        mCustomValueAnimator.start();
    }

    public void updatePlayerPosition(SimpleTextureViewPlayer player,  int lastOrientation, int currentOrientation) {
        int halfWidth = player.getWidth() / 2;
        int halfHeight = player.getHeight() / 2;
        int scrollX = Math.abs((halfHeight - halfWidth + 1) / 4);

        Log.i(TAG, "translate call, currentOrientation: " + currentOrientation + ", halfWidth:" + halfWidth + ", halfHeight:" + halfHeight+", scrollX:"+scrollX);

        if (mObjectAnimatorX == null) {
            mObjectAnimatorX = ObjectAnimator.ofFloat(player, "translationX", 0);
            mObjectAnimatorX.setDuration(1000);
        }

        if (mObjectAnimatorY == null) {
            mObjectAnimatorY = ObjectAnimator.ofFloat(player, "translationY", 0);
            mObjectAnimatorY.setDuration(1000);
        }

        if (currentOrientation == 0 || currentOrientation == 180) {
            float[] x = {0f};
            mObjectAnimatorX.setFloatValues(x);
            mObjectAnimatorY.setFloatValues(x);
        } else {
            float startX = 0;
            float[] x = {startX + 0f, startX - scrollX, startX - scrollX * 2, startX - scrollX * 3, startX - scrollX * 4};
            float[] y = {startX + 0f, startX + scrollX, startX + scrollX * 2, startX + scrollX * 3, startX + scrollX * 4};

            mObjectAnimatorX.setFloatValues(x);
            mObjectAnimatorY.setFloatValues(y);
        }

        mObjectAnimatorX.start();
        mObjectAnimatorY.start();
    }

    private void showLog(String message) {
        Log.i("lvjielvjie", message);
    }

}
