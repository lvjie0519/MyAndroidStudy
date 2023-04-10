package com.android.study.example.websocket;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebSocketServerManager {
    private static String TAG = "WebSocketServerManager";
    private Context mContext;
    private MyWebSocketServer mMyWebSocketServer = null;
    private boolean mInit = false;

    private MessageEventListener mMessageEventListener;

    private WebSocketServerManager() {

    }

    public static WebSocketServerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context, MessageEventListener listener) {
        if (mInit) {
            return;
        }
        mInit = true;
        mContext = context;
        mMessageEventListener = listener;
    }

    public void startServer() {
        stopServer();

        int port = NetUtils.getAvailablePort();
        InetSocketAddress address = new InetSocketAddress(port);
        mMessageEventListener.onMessageEvent(null, "startServer, ip: " + address.getHostName() + ", port: " + port);
        mMyWebSocketServer = new MyWebSocketServer(address, mMessageEventListener);

        mMyWebSocketServer.start();
    }

    public void stopServer() {
        if (mMyWebSocketServer == null) {
            return;
        }

        try {
            mMyWebSocketServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mMyWebSocketServer = null;
    }

    public void sendMessage(String message) {
        if (mMyWebSocketServer == null) {
            return;
        }

        if (TextUtils.isEmpty(message)) {
            return;
        }

        mMyWebSocketServer.broadcast(message);
    }

    private static class SingletonHolder {
        private static final WebSocketServerManager INSTANCE = new WebSocketServerManager();
    }
}
