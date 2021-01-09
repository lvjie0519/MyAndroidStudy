package com.android.study.example.aidl;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.study.example.IMyAidlInterface;

public class MyAidlService extends Service {

    public MyAidlService() {
        Log.i("lvjie", "MyAidlService--->MyAidlService()...");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("lvjie", "MyAidlService--->onCreate()...");
    }

    /**
     * 当客户端绑定该服务的时候会执行
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("lvjie", "MyAidlService--->onBind()...");
        return iBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("lvjie", "MyAidlService--->onRebind()...");
    }


    private IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.i("lvjie", "收到了客户端的请求...num1="+num1+"   num2="+num2);
            int result = num1+num2;
            return result;
        }

        @Override
        public String getString() throws RemoteException {
            return "hello world";
        }

        @Override
        public StudentInfo getStudentInfo() throws RemoteException {
            Log.i("lvjie", "收到了客户端的请求...getStudentInfo()...");
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setUserName("zhangsan");
            studentInfo.setUserAge(23);
            return studentInfo;
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("lvjie", "MyAidlService--->onUnbind()...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lvjie", "MyAidlService--->onDestroy()...");
    }
}
