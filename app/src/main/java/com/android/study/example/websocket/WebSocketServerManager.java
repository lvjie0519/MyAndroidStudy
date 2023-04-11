package com.android.study.example.websocket;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.study.example.websocket.utils.NetUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebSocketServerManager {
    private static String TAG = "WebSocketServerManager";
    private Context mContext;
    private MyWebSocketServer mMyWebSocketServer = null;
    private boolean mInit = false;

    private MessageEventListener mMessageEventListener;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

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

        initHandler();
    }

    private void initHandler() {
        mHandlerThread = new HandlerThread("wbsocket_server_Thread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void startServer() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doStartServer();
            }
        });
    }

    private void doStartServer(){
        doStopServer();

        int port = NetUtils.getAvailablePort();
        InetSocketAddress address = new InetSocketAddress(port);
//        InetSocketAddress address = new InetSocketAddress("192.168.3.93", port);
        mMessageEventListener.onMessageEvent(null, "startServer, ip: " + address.getHostName() + ", port: " + port);
        mMyWebSocketServer = new MyWebSocketServer(address, mMessageEventListener);

        mMyWebSocketServer.start();
    }

    public void stopServer() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doStopServer();
            }
        });
    }

    private void doStopServer(){
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
