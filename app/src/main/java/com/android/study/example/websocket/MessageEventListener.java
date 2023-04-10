package com.android.study.example.websocket;

import org.java_websocket.WebSocket;

public interface MessageEventListener {
    void onMessageEvent(WebSocket conn, String message);
}
