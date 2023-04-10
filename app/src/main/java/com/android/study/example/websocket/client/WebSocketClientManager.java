package com.android.study.example.websocket.client;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.net.URI;

public class WebSocketClientManager {
    private static String TAG = "WebSocketClientManager";
    private Context mContext;
    private MyWebSocketClient mMyWebSocketClient = null;
    private boolean mInit = false;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private MessageEventListener mMessageEventListener;

    private WebSocketClientManager() {

    }

    public static WebSocketClientManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context, MessageEventListener listener) {
        if (mInit) {
            return;
        }
        mInit = true;
        mContext = context;
        mMessageEventListener = listener;

        initHandler();
    }

    private void initHandler() {
        mHandlerThread = new HandlerThread("wbsocket_client_Thread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void connectServer(final String ip, final int port) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doConnectServer(ip, port);
            }
        });
    }

    private void doConnectServer(String ip, int port) {
        if (mMyWebSocketClient != null) {
            doDisConnectServer();
        }

        if (TextUtils.isEmpty(ip)) {
            return;
        }

        URI uri = URI.create("ws://" + ip + ":" + port);
        mMyWebSocketClient = new MyWebSocketClient(uri, mMessageEventListener);
        Log.i("lvjie", "doConnectServer call, connect start...");
        mMyWebSocketClient.connect();
        Log.i("lvjie", "doConnectServer call, connect end...");

//        try {
//            mMyWebSocketClient.connectBlocking();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void disConnectServer() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doDisConnectServer();
            }
        });
    }

    private void doDisConnectServer() {
        if (mMyWebSocketClient == null) {
            return;
        }

        try {
            mMyWebSocketClient.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mMyWebSocketClient = null;
    }

    public void sendMessage(String message) {
        if (mMyWebSocketClient == null) {
            return;
        }

        if (TextUtils.isEmpty(message)) {
            return;
        }

        mMyWebSocketClient.send(message);
    }

    private static class SingletonHolder {
        private static final WebSocketClientManager INSTANCE = new WebSocketClientManager();
    }
}
