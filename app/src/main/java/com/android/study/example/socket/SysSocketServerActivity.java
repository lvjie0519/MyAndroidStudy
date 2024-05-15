package com.android.study.example.socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.study.example.R;
import com.android.study.example.utils.ToastUtil;

import java.net.Socket;

public class SysSocketServerActivity extends AppCompatActivity {
    private static final String TAG = "SysSocketServerActivity";
    private TextView mTvShowInfo;
    private EditText mEtMessage;

    private SysSocketServer mSysSocketServer;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SysSocketServerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_socket_server);

        initView();
        initServer();
    }

    private void initView() {
        mTvShowInfo = findViewById(R.id.tv_showInfo);
        mEtMessage = findViewById(R.id.et_message);
    }

    private void initServer(){
        mSysSocketServer = new SysSocketServer(new SysSocketServer.SysSocketServerListener() {
            @Override
            public void onConnect(Socket client) {
                showInfo("New client connected from " + client.getInetAddress());
            }

            @Override
            public void onReceiveMessage(Socket client, String message) {
                showInfo("receive msg from " + client.getInetAddress()+"  message len: "+message.length());
            }

            @Override
            public void onError(Socket client, String errorMessage) {

            }
        });
    }

    public void onClickStartServer(View view) {
        mSysSocketServer.startServer();
    }

    public void onClickStopServer(View view) {
        mSysSocketServer.closeServer();
    }

    public void onClickGetServerInfo(View view) {
        showInfo(NetUtils.getCurrentHostAddress() + ":" + mSysSocketServer.getServer().getLocalPort());
    }

    public void onClickSendMsg(View view) {
        String message = mEtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            showToast("请输入发送的消息");
            return;
        }
    }

    private void showToast(String text) {
        ToastUtil.showToast(this, text);
    }

    private void showInfo(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = mTvShowInfo.getText().toString();
                text = text + "\n" + message;
                mTvShowInfo.setText(text);
            }
        });
    }

    private void showInfoNotAdd(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvShowInfo.setText(message);
            }
        });
    }
}