package com.android.example.myaidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.study.example.IMyAidlInterface;

public class AidlClientSecondTestActivity extends AppCompatActivity {

    private EditText mEtData1;
    private EditText mEtData2;
    private TextView mTvShowResult;

    private IMyAidlInterface iMyAidlInterface;

    private ServiceConnection conn = new ServiceConnection() {

        // 绑定上服务
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.i("lvjie", "onServiceConnected()...");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        // 断开服务
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("lvjie", "onServiceDisconnected()...");
            iMyAidlInterface = null;
        }
    };

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AidlClientSecondTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client_second_test);

        initView();
    }

    private void initView(){
        this.mEtData1 = (EditText) findViewById(R.id.et_data_1);
        this.mEtData2 = (EditText) findViewById(R.id.et_data_2);
        this.mTvShowResult = (TextView) findViewById(R.id.tv_show_result);
    }

    public void onClickConnectService(View view){
        bindAidlService();
    }

    public void onClickAidlTestBtn(View view){

        int num1 = 0;
        int num2 = 0;
        try {
            num1 = Integer.parseInt(this.mEtData1.getText().toString());
            num2 = Integer.parseInt(this.mEtData2.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            int result = iMyAidlInterface.add(num1, num2);

            mTvShowResult.setText(""+num1+" + "+num2+" = "+result);
        } catch (RemoteException e) {
            Log.i("lvjie", e.toString());
            mTvShowResult.setText("出错了...");
        }
    }

    private void bindAidlService() {
        // 获取服务
        Intent intent = new Intent();
        // 显示intent启动服务， 需要注意此处的包名和MyAidlService类的全名
        intent.setComponent(new ComponentName("com.android.study.example", "com.android.study.example.aidl.MyAidlService"));

        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn!=null){
            unbindService(conn);
        }
    }

}
