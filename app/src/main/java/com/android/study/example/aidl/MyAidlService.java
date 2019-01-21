package com.android.study.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.study.example.IMyAidlInterface;

public class MyAidlService extends Service {

    public MyAidlService() {
    }

    /**
     * 当客户端绑定该服务的时候会执行
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    private IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.i("lvjie", "收到了客户端的请求...num1="+num1+"   num2="+num2);
            int result = num1+num2;
            return result;
        }
    };

}
