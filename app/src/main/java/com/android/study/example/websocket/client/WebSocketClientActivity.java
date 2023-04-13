package com.android.study.example.websocket.client;

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

public class WebSocketClientActivity extends Activity {

    private TextView mTvShowInfo;
    private EditText mEtMessage;

    private MessageEventListener messageEventListener = new MessageEventListener() {
        @Override
        public void onMessageEvent(String message) {
            showInfo(message);
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WebSocketClientActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket_client);

        initView();
        WebSocketClientManager.getInstance().init(this.getApplicationContext(), messageEventListener);
    }

    private void initView() {
        mTvShowInfo = findViewById(R.id.tv_showInfo);
        mEtMessage = findViewById(R.id.et_message);
    }

    public void onClickConnectServer(View view) {
        WebSocketClientManager.getInstance().connectServer("192.168.3.94", 8887);
    }

    public void onClickStopConnect(View view) {
        WebSocketClientManager.getInstance().disConnectServer();
    }

    public void onClickSendMsg(View view) {
        String message = mEtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            showToast("请输入发送的消息");
            return;
        }

        WebSocketClientManager.getInstance().sendMessage(message);
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
                Toast.makeText(WebSocketClientActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}