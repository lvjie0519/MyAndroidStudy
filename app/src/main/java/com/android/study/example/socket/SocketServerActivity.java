package com.android.study.example.socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerActivity extends AppCompatActivity {
    private static final String TAG = "SocketServerActivity";
    private boolean isServiceDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);
    }

    public void onClickStartSocketServer(View view) {
        new Thread(new TcpServer()).start();
    }


    private class TcpServer implements Runnable{
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try{
                serverSocket = new ServerSocket(8688);
            }catch (IOException exception){
                exception.printStackTrace();
            }
            while(!isServiceDestroyed){
                try {
                    if (serverSocket == null){
                        return;
                    }
                    Log.i(TAG, "ServerSocket loop listen ClientSocket");
                    //接收客户端的请求，并且阻塞直到接收到消息
                    final Socket client = serverSocket.accept();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }).start();
                }catch (IOException exception){
                    exception.printStackTrace();
                }
            }

        }
    }

    private void responseClient(Socket client) throws IOException{
        // 用于接收客户端的消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                client.getOutputStream())),true);
        out.println("你好，我是服务端");
        while(!isServiceDestroyed){
            String str = in.readLine();
            Log.i(TAG, "收到客户端发来的消息" + str);
            if (TextUtils.isEmpty(str)){
                //客户端断开了连接
                Log.i(TAG, "客户端断开了连接");
                break;
            }
            String message = "收到了客户端的消息为：" + str;
            // 从客户端收到的消息加工再发送给客户端
            out.println(message);
        }
        out.close();
        in.close();
        client.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isServiceDestroyed = true;
    }
}