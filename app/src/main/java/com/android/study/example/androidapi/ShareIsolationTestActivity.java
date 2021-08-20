package com.android.study.example.androidapi;

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


public class ShareIsolationTestActivity extends Activity {

    private TextView mTvShowInfo;
    private EditText mEtInputInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ShareIsolationTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_isolation_test);

        initView();
    }

    private void initView(){
        this.mEtInputInfo = findViewById(R.id.et_input_text);
        this.mTvShowInfo = findViewById(R.id.tv_show_info);

        if(getIntent() != null && getIntent().getExtras() != null){
            String shareInfo = getIntent().getExtras().getString(Intent.EXTRA_TEXT);
            this.mTvShowInfo.setText("接收到分享内容："+shareInfo);
        }
    }

    public void onClickTestShare1(View view) {
        String shareContent = mEtInputInfo.getText().toString();
        if (TextUtils.isEmpty(shareContent)) {
            showToast("内容不能为空");
            return;
        }
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(textIntent, "分享"));
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}