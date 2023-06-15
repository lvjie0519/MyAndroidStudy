package com.example.rtsp.server;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addView();
    }

    public void onClickOpenRstpServerPage(View view) {
        RtspServerActivity.startActivity(this);
    }

    private void addView(){
        LinearLayout layout = findViewById(R.id.layout_warraper);


        View view1 = new View(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, 0.4f);
        view1.setLayoutParams(layoutParams1);
        view1.setBackgroundColor(Color.parseColor("#333333"));

        View view2 = new View(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, 0.6f);
        view2.setLayoutParams(layoutParams2);
        view2.setBackgroundColor(Color.parseColor("#999999"));

        layout.addView(view1);
        layout.addView(view2);
    }

    public void onClickOpenQrPage(View view) {
        new IntentIntegrator(MainActivity.this)
                .setCaptureActivity(CaptureActivity.class)
                .setOrientationLocked(false)
                .setPrompt("请对准二维码")  // 设置提示语
                .setCameraId(0)  // 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)  // 是否开启声音,扫完码之后会"哔"的一声
                .setBarcodeImageEnabled(true)  // 扫完码之后生成二维码的图片
                .initiateScan();  // 初始化扫码
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                processQrScan(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processQrScan(String qrData){
        try {
            JSONObject jsonObject = new JSONObject(qrData);
            String ip = jsonObject.getString("ip");
            int port = jsonObject.getInt("port");
            MGWebSocketClientManager.getInstance().connectServer(ip, port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}