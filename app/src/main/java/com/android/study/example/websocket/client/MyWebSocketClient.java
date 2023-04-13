package com.android.study.example.websocket.client;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {
    private static final String TAG = "MyWebSocketClient";

    private MessageEventListener mMessageEventListener;

    public MyWebSocketClient(URI serverURI, MessageEventListener listener) {
//        super(serverURI);
//        super(serverURI, new Draft_6455());
        super(serverURI, new Draft_6455(), null, 5000);
        mMessageEventListener = listener;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onOpen call, localAddress: "+getLocalSocketAddress() + "成功连接到：" + getRemoteSocketAddress(getConnection()));
        }
    }

    @Override
    public void onMessage(String s) {
        Log.i("websocket", "onMessage call, message: "+s);
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onMessage call, message: " + s);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onClose call, code: " + code+" reason: "+reason+" remote: "+remote);
        }
    }

    @Override
    public void onError(Exception e) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onError call, " + e.toString());
        }
    }
}
