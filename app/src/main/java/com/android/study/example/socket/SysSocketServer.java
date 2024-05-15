package com.android.study.example.socket;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SysSocketServer {
    private static final String TAG = "SysSocketServer";
    private static final int SERVER_PORT = 8899; // 设置服务器监听的端口号
    private ServerSocket mServer;
    private Thread mListenClientThread;
    private SysSocketServerListener mSysSocketServerListener;

    public SysSocketServer(SysSocketServerListener listener) {
        this.mSysSocketServerListener = listener;
    }

    public void startServer() {
        try {
            // 创建ServerSocket并绑定到指定端口
            mServer = new ServerSocket(SERVER_PORT);
            mServer.setReceiveBufferSize(1024*1024);
            System.out.println("Socket server started on port " + SERVER_PORT);
            listenClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenClient() {

        mListenClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 循环等待客户端连接
                while (!Thread.interrupted()) {
                    // 接受来自客户端的Socket连接请求
                    Socket socket = null;
                    try {
                        socket = mServer.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG, "New client connected from " + socket.getInetAddress());
                    callBackOnConnect(socket);

                    // 为每个客户端连接创建单独的处理线程
                    new ClientHandler(socket).start();
                }
            }
        });

        mListenClientThread.start();
    }

    public ServerSocket getServer() {
        return mServer;
    }

    public void closeServer() {
        // 在退出前关闭ServerSocket
        if (mServer != null) {
            try {
                mServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;

            try {
                // 从Socket获取输入输出流
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, "Received message: " + line);
                    callBackOnReceiveMessage(socket, line);

                    // 处理收到的消息，这里仅将其原样返回给客户端作为示例
                    writer.println(line);

                    // 如果收到特定终止命令或者其他逻辑判断，可以在此处关闭连接
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                Log.i(TAG, "close client..." + socket);
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void callBackOnConnect(Socket client) {
        if (mSysSocketServerListener != null) {
            mSysSocketServerListener.onConnect(client);
        }
    }

    private void callBackOnReceiveMessage(Socket client, String message) {
        if (mSysSocketServerListener != null) {
            mSysSocketServerListener.onReceiveMessage(client, message);
        }
    }

    private void callBackOnError(Socket client, String errorMessage) {
        if (mSysSocketServerListener != null) {
            mSysSocketServerListener.onError(client, errorMessage);
        }
    }

    public interface SysSocketServerListener {
        void onConnect(Socket client);

        void onReceiveMessage(Socket client, String message);

        void onError(Socket client, String errorMessage);
    }
}
