package com.android.study.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommonGlobalReceiverManager {

    private Context mContext = null;
    private boolean mInit = false;
    private List<CommonGlobalReceiver> mGlobalReceiverList;

    private CommonGlobalReceiverManager() {
        mGlobalReceiverList = new ArrayList<>();
    }

    public static CommonGlobalReceiverManager getInstance() {
        return CommonGlobalReceiverHolder.INSTANCE;
    }

    public void init(Context context) {
        if (!mInit) {
            mInit = true;
            mContext = context;
        }
    }

    public BroadcastReceiver registerCommonGlobalReceiver(IntentFilter filter, GlobalReceiverListener listener) {
        if(filter == null || listener == null){
            return null;
        }

        CommonGlobalReceiver receiver = new CommonGlobalReceiver(listener);
        mContext.registerReceiver(receiver, filter);
        mGlobalReceiverList.add(receiver);
        return receiver;
    }

    private final static class CommonGlobalReceiverHolder {
        private static final CommonGlobalReceiverManager INSTANCE = new CommonGlobalReceiverManager();
    }

    private class CommonGlobalReceiver extends BroadcastReceiver {

        private GlobalReceiverListener mListener;
        public CommonGlobalReceiver(GlobalReceiverListener listener){
            mListener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("lvjie", "oBroadcastReceiver onReceive call, action: "+intent.getAction());
            if(mListener != null){
                mListener.onReceive(context, intent);
            }
        }
    }

    public interface GlobalReceiverListener {
        void onReceive(Context context, Intent intent);
    }
}
