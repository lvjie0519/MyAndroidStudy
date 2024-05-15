package com.android.study.example.socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.study.example.R;
import com.android.study.example.utils.ToastUtil;

import java.io.IOException;

public class SysSocketClientActivity extends AppCompatActivity {
    private static final String TAG = "SysSocketClientActivity";
    private EditText mEtServerIp;
    private EditText mEtServerPort;
    private TextView mTvShowInfo;

    // ==================  性能统计使用到的变量 start  ==================
    private EditText mEtCount;
    private EditText mEtDataSize;
    private Button mBtnStatisticState;
    private int mCurrentSendCount = 0;
    private int mTotalSendCount = 0;
    private String mSendMessage = "";
    private int mTransmitTotalTime = 0;
    // ==================  性能统计使用到的变量 end  ==================

    private SysSocketClient mClient;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SysSocketClientActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_socket_client);

        initView();
        initClient();
    }

    private void initView() {
        this.mEtServerIp = findViewById(R.id.et_server_ip);
        this.mEtServerPort = findViewById(R.id.et_server_port);
        this.mTvShowInfo = findViewById(R.id.tv_showInfo);

        this.mEtCount = findViewById(R.id.et_count);
        this.mEtDataSize = findViewById(R.id.et_data_size);
        this.mBtnStatisticState = findViewById(R.id.btn_statistic_state);
    }

    private void initClient(){
        mClient = new SysSocketClient(new SysSocketClient.SysSocketClientListener() {
            @Override
            public void onReceiveMessage(String message) {
                Log.i(TAG, "onReceiveMessage call: " + message.length()+"  "+message);
                if (!TextUtils.isEmpty(message)) {
                    // 接收到server 回复的消息，是 "100#abcd" 格式的数据表示用来统计使用
                    long currentTime = System.currentTimeMillis();
                    String[] data = message.split("#", 2);
                    if (data.length == 2) {
                        long clientTime = Long.parseLong(data[0]);
                        Log.i("WebSocket", "currentTime: "+currentTime+", clientTime: "+clientTime);
                        mTransmitTotalTime +=(currentTime-clientTime);

                        sendMsgForStatistic();
                        return;
                    }
                }
                showInfo("receive message:" + message);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i(TAG, "onError call: " + errorMessage);
                showInfo("onError call: " + errorMessage);
            }
        });
    }

    public void onClickConnectServer(View view) {
        final String serverIp = this.mEtServerIp.getText().toString().trim();
        final String serverPort = this.mEtServerPort.getText().toString().trim();

        if (TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(serverPort)) {
            ToastUtil.showToast(this, "ip或port不能为空!");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                mClient.connectServer(serverIp, Integer.parseInt(serverPort));
            }
        }).start();
    }

    public void onClickDisConnectServer(View view) {
        try {
            mClient.closeConnection();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void onClickStartStatistic(View view) {
        int count = 0;
        int dataSize = 0;
        try {
            count = Integer.parseInt(this.mEtCount.getText().toString().trim());
            dataSize = Integer.parseInt(this.mEtDataSize.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (count <= 0) {
            showToast("次数输入有误");
            return;
        }

        if (dataSize <= 0) {
            showToast("数据量输入有误");
            return;
        }

        if(mClient == null){
            showToast("请点击初始化按钮");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<dataSize; i++){
            stringBuilder.append("a");
        }
        mTotalSendCount = count;
        mCurrentSendCount = 0;
        mTransmitTotalTime = 0;
        mSendMessage = stringBuilder.toString();

        sendMsgForStatistic();
    }

    private void showToast(String text) {
        ToastUtil.showToast(this, text);
    }

    private void sendMsgForStatistic() {
        updateStatisticStateBtn();

        if (mCurrentSendCount == (mTotalSendCount)) {
            showInfoNotAdd("总耗时："+mTransmitTotalTime+"\n 平均耗时："+(mTransmitTotalTime/mTotalSendCount/2));
            return;
        }

        final long currentTime = System.currentTimeMillis();
        mCurrentSendCount++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mClient.sendMessage(currentTime + "#" + mSendMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateStatisticStateBtn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCurrentSendCount >= (mTotalSendCount)) {
                    mBtnStatisticState.setText("开始统计");
                }

                if (mCurrentSendCount < mTotalSendCount) {
                    mBtnStatisticState.setText("统计中");
                }
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
}