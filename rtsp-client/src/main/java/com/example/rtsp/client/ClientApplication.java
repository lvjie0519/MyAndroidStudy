package com.example.rtsp.client;

import android.app.Application;

public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FollowMeStudyManager.getIntance().init(this);
    }
}
