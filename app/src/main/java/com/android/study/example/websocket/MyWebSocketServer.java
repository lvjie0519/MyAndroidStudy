package com.android.study.example.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyWebSocketServer extends WebSocketServer {

    private MessageEventListener mMessageEventListener;

    public MyWebSocketServer(InetSocketAddress address, MessageEventListener listener) {
        super(address);

        mMessageEventListener = listener;
    }

    /**
     * 连接建立时回调
     * @param conn
     * @param handshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onOpen...");
        }
    }

    /**
     * 连接关闭时回调
     * @param conn
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onClose...");
        }
    }

    /**
     * 接收到消息时回调
     * @param conn
     * @param message
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onError..." + ex.toString());
        }
    }

    /**
     * 启动服务时回调
     */
    @Override
    public void onStart() {
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(null, "onStart...");
        }
    }
}
