package com.example.rtsp.client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.example.rtsp.client.player.SimpleTextureViewPlayer;
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
                    updatePlayerOrientation(lastOrientation, currentOrientation);
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

    private void updatePlayerOrientation(int lastOrientation, int currentOrientation) {
        Log.i(TAG, "updatePlayerOrientation call,  lastOrientation: " + lastOrientation + ", currentOrientation:" + currentOrientation);
        if (mWeakReferencePlayer.get() != null) {
            mWeakReferencePlayer.get().setRotation(currentOrientation);
        }
    }

    private void showLog(String message) {
        Log.i("lvjielvjie", message);
    }

}
