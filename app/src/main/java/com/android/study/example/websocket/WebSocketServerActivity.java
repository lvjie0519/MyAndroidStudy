package com.android.study.example.websocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.study.example.R;

import org.java_websocket.WebSocket;

public class WebSocketServerActivity extends Activity {

    private TextView mTvShowInfo;
    private EditText mEtMessage;
    private MessageEventListener messageEventListener = new MessageEventListener() {
        @Override
        public void onMessageEvent(WebSocket conn, String message) {
            if (conn != null) {
                showInfo(conn.toString() + ": " + message);
            } else {
                showInfo(message);
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WebSocketServerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket_server);

        initView();
        WebSocketServerManager.getInstance().init(this.getApplicationContext(), messageEventListener);
    }

    private void initView() {
        mTvShowInfo = findViewById(R.id.tv_showInfo);
        mEtMessage = findViewById(R.id.et_message);
    }

    public void onClickStartServer(View view) {
        WebSocketServerManager.getInstance().startServer();
    }

    public void onClickStopServer(View view) {
        WebSocketServerManager.getInstance().stopServer();
    }

    public void onClickSendMsg(View view) {
        String message = mEtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            showToast("请输入发送的消息");
            return;
        }

        WebSocketServerManager.getInstance().sendMessage(message);
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

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebSocketServerActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClickGetPort(View view) {
        showToast("port: "+NetUtils.getAvailablePort());
    }
}