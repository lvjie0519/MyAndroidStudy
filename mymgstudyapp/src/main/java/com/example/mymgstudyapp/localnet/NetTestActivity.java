package com.example.mymgstudyapp.localnet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymgstudyapp.R;
import com.example.mymgstudyapp.localnet.sdk.mgtv.utils.ArpUtil;
import com.example.mymgstudyapp.localnet.sdk.mgtv.utils.NetworkUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * android 4.1
 * android 4.4
 * android 6.x
 * android 8.x
 * android 10.x
 * android 12
 * android 13
 */
public class NetTestActivity extends AppCompatActivity {

    private EditText mEtIp;
    private TextView mTvShowInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);

        initView();
    }

    private void initView(){
        mEtIp = findViewById(R.id.et_ip);
        mTvShowInfo = findViewById(R.id.tv_showInfo);
    }

    public void onClickPingTest(View view) {
        String ip = mEtIp.getText().toString().trim();
        if(TextUtils.isEmpty(ip)){
            showToast("请输入IP地址");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showInfoToView("ping...");
                    InetAddress address = InetAddress.getByName(ip);
                    if(address.isReachable(2000)){
                        showInfoToView("ping "+ip+" success, is reachable");
                    }else{
                        showInfoToView("ping "+ip+" success, not reachable");
                    }
                } catch (IOException e) {
                    showInfoToView("ping "+ip+" failed, "+e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClickReadArpFile(View view) {
        NetworkUtil.getCurrentWifiInfo(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                showInfoToView(ArpUtil.getArpInfoFromReadFile());
            }
        }).start();
    }

    public void onClickCmdReadArpFile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                showInfoToView(ArpUtil.getArpInfoFromCmd());
            }
        }).start();
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showInfoToView(String info){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvShowInfo.setText(info);
            }
        });
    }
}