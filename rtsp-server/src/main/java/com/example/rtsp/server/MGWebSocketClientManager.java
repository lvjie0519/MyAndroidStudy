package com.example.rtsp.server;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;

import com.example.rtsp.server.utils.ToastUtils;
import com.mgtv.tvos.link.MGLinkFactory;
import com.mgtv.tvos.link.utils.NetUtils;
import com.mgtv.tvos.link.websocket.MGWebSocketParamsConfig;
import com.mgtv.tvos.link.websocket.bean.MGWebSocketClientInfo;
import com.mgtv.tvos.link.websocket.interfaces.IMGWebSocketClient;
import com.mgtv.tvos.link.websocket.interfaces.MGWebSocketClientListener;

/**
 * @author lvjie
 * @creatTime 2023/5/18
 * @describe MGWebSocketClientManager
 * 功能：对WebSocketClient管理
 */
public class MGWebSocketClientManager {
    private static final String TAG = MGWebSocketClientManager.class.getSimpleName();
    private Context mContext;
    private static MGWebSocketClientManager sInstance;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private volatile boolean mIsSending = false;

    private IMGWebSocketClient mgWebSocketClient;
    private MGWebSocketClientListener mgWebSocketClientListener = new MGWebSocketClientListener() {
        @Override
        public void onConnect() {
            Log.i(TAG, "onConnect call.");
            mIsSending = false;
            ToastUtils.showToast(mContext, "连接成功!");
        }

        @Override
        public void onDisConnect(int code, String reason) {
            Log.i(TAG, "断开连接, code" + code + ", reason:" + reason);
            mIsSending = false;
            ToastUtils.showToast(mContext, "断开连接, code" + code + ", reason:" + reason);
        }

        @Override
        public void onReceiveMessage(String message) {
            Log.i(TAG, "收到消息： " + message);
            mIsSending = false;
            ToastUtils.showToast(mContext, "收到消息： " + message);
        }

        @Override
        public void onError(String errorCode, String errorMsg) {
            Log.e(TAG, "onError call, errorCode:" + errorCode + ", errorMsg:" + errorMsg);
            mIsSending = false;
            ToastUtils.showToast(mContext, "onError call, errorCode:" + errorCode + ", errorMsg:" + errorMsg);
        }
    };

    private MGWebSocketClientManager() {
    }

    public static MGWebSocketClientManager getInstance() {
        if (sInstance == null) {
            synchronized (MGWebSocketClientManager.class) {
                if (sInstance == null) {
                    sInstance = new MGWebSocketClientManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context) {
        Context appContext = context.getApplicationContext();
        mContext = appContext == null ? context : appContext;
        mIsSending = false;
        initBackgroundHandler();
    }

    private void initBackgroundHandler() {
        mBackgroundThread = new HandlerThread("MGSocketClient");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void connectServer(String ip, int port) {
        if (mgWebSocketClient != null) {
            mgWebSocketClient.disConnectServer();
            mgWebSocketClient = null;
        }

        initClient(ip, port);
        if (mgWebSocketClient != null) {
            mgWebSocketClient.connectServer();
        }
    }

    private void initClient(String ip, int port) {
        MGWebSocketParamsConfig paramsConfig = new MGWebSocketParamsConfig.Builder(mContext)
                .setIp(ip)
                .setPort(port)
                .setHeardBeatTime(60)
                .build();

        try {
            mgWebSocketClient = MGLinkFactory.createMGWebSocketClient(paramsConfig, mgWebSocketClientListener);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (mgWebSocketClient != null && mgWebSocketClient.isConnected()) {
            mgWebSocketClient.sendMessage(message);
        }
    }

    public String getCurrentHostAddress() {
        if (mgWebSocketClient != null && mgWebSocketClient.isConnected()) {
            return mgWebSocketClient.getClientInfo().getIp();
        }
        return NetUtils.getCurrentHostAddress();
    }

    public void disConnectServer() {
        if (mgWebSocketClient != null) {
            mgWebSocketClient.disConnectServer();
        }
    }

    public void release() {
        disConnectServer();
        mgWebSocketClient = null;
    }
}
