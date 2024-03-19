package com.android.study.example.socket;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientActivity extends AppCompatActivity {
    private static final String TAG = "SocketClientActivity";

    private TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
    }

    public void onClickStartConnect(View view) {
        tcpClient = new TcpClient();
        new Thread(tcpClient).start();
    }

    private class TcpClient implements Runnable {

        private PrintWriter mPrintWriter;

        @Override
        public void run() {
            Socket socket = null;
            while (socket == null){
                try {
                    //选择和服务器相同的ip和端口 8688
                    socket = new Socket("192.168.3.110",8688);
                    mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())),true);
                }catch (IOException exception){
                    Log.d(TAG, "connectSocketServer: " + exception);
                    SystemClock.sleep(1000);
                }
            }
            try {
                // 用于接收服务端的消息
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(!isFinishing()){
                    final String msg = br.readLine();
                    if (msg != null){
                    }
                }
                mPrintWriter.close();
                br.close();
                socket.close();
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }

        public void sendMessage(final String message){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println(message);
                }
            }).start();
        }
    }
}