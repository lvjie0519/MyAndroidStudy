package com.android.study.example.websocket;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyWebSocketServer extends WebSocketServer {

    private MessageEventListener mMessageEventListener;

    public MyWebSocketServer(InetSocketAddress address, MessageEventListener listener) {
        super(address);

        mMessageEventListener = listener;
        Log.i("websocket", "MyWebSocketServer create.");
    }

    /**
     * 连接建立时回调
     *
     * @param conn
     * @param handshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.i("websocket", "MyWebSocketServer onOpen call, conn remote: " + conn.getRemoteSocketAddress().toString()
                + " conn local: " + conn.getLocalSocketAddress().toString());
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onOpen...");
        }
    }

    /**
     * 连接关闭时回调
     *
     * @param conn
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i("websocket", "MyWebSocketServer onClose call, conn remote: " + conn.getRemoteSocketAddress().toString()
                + " conn local: " + conn.getLocalSocketAddress().toString()
                + "  reason: " + reason+"  remote: "+remote);
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onClose...");
        }
    }

    /**
     * 接收到消息时回调
     *
     * @param conn
     * @param message
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.i("websocket", "MyWebSocketServer onMessage call, conn remote: " + conn.getRemoteSocketAddress().toString()
                + " conn local: " + conn.getLocalSocketAddress().toString()
                + "  message: " + message);
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e("websocket", "MyWebSocketServer onError call, conn remote: " + conn.getRemoteSocketAddress().toString()
                + " conn local: " + conn.getLocalSocketAddress().toString()
                + "  error: " + ex.toString());
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(conn, "onError..." + ex.toString());
        }
    }

    /**
     * 启动服务时回调
     */
    @Override
    public void onStart() {
        Log.i("websocket", "MyWebSocketServer onStart call.");
        if (mMessageEventListener != null) {
            mMessageEventListener.onMessageEvent(null, "onStart...");
        }
    }
}
