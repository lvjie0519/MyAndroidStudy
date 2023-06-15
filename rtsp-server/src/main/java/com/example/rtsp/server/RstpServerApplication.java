package com.example.rtsp.server;

import android.app.Application;

public class RstpServerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MGWebSocketClientManager.getInstance().init(this);
    }
}
