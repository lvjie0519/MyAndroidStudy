package com.android.study.example.websocket.client;

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
            mMessageEventListener.onMessageEvent("onOpen call, " + "成功连接到：" + getRemoteSocketAddress(getConnection()));
        }
    }

    @Override
    public void onMessage(String s) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onMessage call, " + s);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onClose call," + s);
        }
    }

    @Override
    public void onError(Exception e) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent("onError call, " + e.toString());
        }
    }
}
