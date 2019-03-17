package com.android.study.example.androidapi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 蓝牙相关
 * https://www.jianshu.com/p/3a372af38103
 */
public class BlueMainTestActivity extends AppCompatActivity {

    private final static String TAG = "Bluetooth-TAG";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private Map<String, BluetoothDevice>  scanBlueDeviceMap = new HashMap<>();

    public static void startActivity(Context context){
        Intent intent = new Intent(context, BlueMainTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_main_test);

        initBlue();
        initView();
    }

    private void initBlue(){
        // 获取 BluetoothAdapter
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void initView(){
        findViewById(R.id.btn_check_self_blue_isopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelfBlueOpen();
            }
        });

        findViewById(R.id.btn_scan_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "start scan blue...");
                startScanBlue();
            }
        });

        findViewById(R.id.btn_stop_scan_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "stop scan blue...");
                stopScanBlue();
            }
        });

        findViewById(R.id.btn_start_connec_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "start connec blue...");
                connectBlue();
            }
        });
    }

    private void checkSelfBlueOpen(){
        // 如果检测到蓝牙没有开启，尝试开启蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 10001);
        }
    }

    private BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i(TAG, "scan blue--> name: "+device.getName()+"  address: "+device.getAddress());
            scanBlueDeviceMap.put(device.getAddress(), device);
        }
    };

    // 开启蓝牙扫描
    private void startScanBlue(){
        mBluetoothAdapter.startLeScan(bleScanCallback);
    }

    // 停止扫描
    private void stopScanBlue(){
        mBluetoothAdapter.stopLeScan(bleScanCallback);
    }

    private void connectBlue(){
        Log.i(TAG, "scan blue size is "+scanBlueDeviceMap.size());
        for(BluetoothDevice device : scanBlueDeviceMap.values()){
            Log.i(TAG, "scan blue--> name: "+device.getName()+"  address: "+device.getAddress());
//            device.connectGatt(this, true, new BluetoothGattCallback() {
//                @Override
//                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//                    super.onConnectionStateChange(gatt, status, newState);
//                }
//
//                @Override
//                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//                    super.onServicesDiscovered(gatt, status);
//                }
//            });
        }

    }


}
