package com.android.study.example.socket;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SysSocketClient {
    private static final String TAG = "SysSocketClient";
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private SysSocketClientListener mSysSocketClientListener;

    private Thread mListenReceiveMessageThread;

    public SysSocketClient(SysSocketClientListener listener) {
        this.mSysSocketClientListener = listener;
    }

    public void connectServer(String serverIpAddress, int port) {
        try {
            // 创建Socket对象并连接到服务器
            socket = new Socket(serverIpAddress, port);
            socket.setReceiveBufferSize(1024*1024);
            socket.setSendBufferSize(1024*1024);

            // 获取输入输出流
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            writer = new BufferedWriter(osw);

            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isr);

            listenReceiveMessage();
        } catch (IOException e) {
            callBackOnError(e.toString());
        }
    }

    private void listenReceiveMessage() {
        mListenReceiveMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()){
                    try {
                        String reveiveMsg = receiveMessage();
                        callBackOnReceiveMessage(reveiveMsg);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
        mListenReceiveMessageThread.start();
    }

    public void sendMessage(String message) throws IOException {
        // 向服务器发送消息
        writer.write(message + "\n");
        writer.flush();
    }

    public String receiveMessage() throws IOException {
        // 从服务器接收消息
        return reader.readLine();
    }

    public void closeConnection() throws IOException {
        mListenReceiveMessageThread.interrupt();
        // 关闭连接
        writer.close();
        reader.close();
        socket.close();
    }

    private void callBackOnReceiveMessage(String message) {
        if (mSysSocketClientListener != null) {
            mSysSocketClientListener.onReceiveMessage(message);
        }
    }

    private void callBackOnError(String errorMessage) {
        if (mSysSocketClientListener != null) {
            mSysSocketClientListener.onError(errorMessage);
        }
    }

    public interface SysSocketClientListener {
        void onReceiveMessage(String message);

        void onError(String errorMessage);
    }
}
