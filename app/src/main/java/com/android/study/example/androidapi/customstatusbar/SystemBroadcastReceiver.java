package com.android.study.example.androidapi.customstatusbar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 电池
 * https://blog.csdn.net/L25000/article/details/46502787
 * wifi
 * https://www.jianshu.com/p/a0fbb4644b84?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes
 */
public class SystemBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemTimeChange";

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm"); // 24小时制

    private static SystemBroadcastReceiver sInstance;
    private Context mContext;

    private SystemBroadcastReceiver() {
    }

    public static SystemBroadcastReceiver getInstance() {
        if (sInstance == null) {
            synchronized (SystemBroadcastReceiver.class) {
                if (sInstance == null) {
                    sInstance = new SystemBroadcastReceiver();
                }
            }
        }

        return sInstance;
    }

    public void registerReceiver(Activity activity) {
        mContext = activity.getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        // 时间
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        // 电量
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        // wifi
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        activity.registerReceiver(sInstance, intentFilter);
    }

    public void unregisterReceiver(Activity activity) {
        activity.unregisterReceiver(sInstance);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action:" + action + ", intent: " + intent);

        switch (action) {
            // 时间变化
            case Intent.ACTION_TIME_CHANGED:    // 时间修改
            case Intent.ACTION_TIME_TICK:       // 系统每1分钟发送一次广播
                Log.i(TAG, "action:" + action + ", time: " + formatter.format(new Date()));
                break;
            // **********************  电量相关 start ****************************
            case Intent.ACTION_BATTERY_CHANGED:         // 电量改变
                doOnBatteryChanged(intent);
                break;
            case Intent.ACTION_BATTERY_LOW:             // 表示当前电池电量低
            case Intent.ACTION_BATTERY_OKAY:            // 表示当前电池已经从电量低恢复为正常

             // 监听充电状态的改变 插入/拔出充电器
            case Intent.ACTION_POWER_CONNECTED:
            case Intent.ACTION_POWER_DISCONNECTED:

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

                int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                break;
            // **********************  电量相关 end ****************************

            // **********************  wifi相关 start ****************************
            case WifiManager.RSSI_CHANGED_ACTION:       // 监听WiFi信号强度的变化
                doOnWifiSignalChanged(intent);
                break;

            case WifiManager.WIFI_STATE_CHANGED_ACTION:  // WiFi模块硬件状态改变的广播,WiFi开启, WiFi关闭
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:  // 连接状态发生改变的广播, 可以从 intent 中取得 NetworkInfo, 此时 NetworkInfo 中提供了连接的新状态, 如果连接成功, 可以获取当前连接网络的 BSSID, 和 WifiInfo.
                break;

            // **********************  wifi相关 end ****************************

            default:
                break;
        }
    }

    private void doOnBatteryChanged(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); // 当前电量百分比
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1); // 电池电量总刻度
        float batteryPct = level / (float) scale * 100; // 计算百分比

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) || (status == BatteryManager.BATTERY_STATUS_FULL);

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1); // 获取充电状态：是否正在充电（电源插头或USB）

        Log.i(TAG, "doOnBatteryChanged Battery Level: " + batteryPct + "%, Charging: " + isCharging + ", chargePlug: " + chargePlug);
    }

    private void doOnWifiSignalChanged(Intent intent){
//        WifiInfo wifiInfo = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
//        int rssi = wifiInfo.getRssi(); // 获取当前WiFi信号强度（RSSI）

        int rssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -1000);
        int level = WifiManager.calculateSignalLevel(rssi, 4);

        Log.d(TAG, "doOnWifiSignalChanged Current RSSI: " + rssi+", intent: "+intent);
    }

    /**
     * 通过 intent 获取当前WIFI状态.
     * @param intent
     * @return
     * 0 --> WiFiManager.WIFI_STATE_DISABLING, 表示 WiFi 正关闭的瞬间状态;
     *  1 --> WifiManager.WIFI_STATE_DISABLED, 表示 WiFi 模块已经完全关闭的状态;
     *  2 --> WifiManager.WIFI_STATE_ENABLING, 表示 WiFi 模块正在打开中瞬间的状态;
     *  3 --> WiFiManager.WIFI_STATE_ENABLED, 表示 WiFi 模块已经完全开启的状态;
     *  4 --> WiFiManager.WIFI_STATE_UNKNOWN, 表示 WiFi 处于一种未知状态; 通常是在开启或关闭WiFi的过程中出现不可预知的错误, 通常是底层状态机可能跑的出现故障了, 会到这种情况, 与底层控制相关;
     */
    private static int getWifiStateByIntent(Intent intent) {
        return intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
    }

    // 通过 WifiManager 获取当前WIFI状态.
    private static int getWifiStateByWifiManager(WifiManager manager) {
        return manager == null ? WifiManager.WIFI_STATE_UNKNOWN : manager.getWifiState();
    }

    public interface OnTimeChangeListener {
        /**
         * 每分钟回调一次, 返回当前  时:分
         * @param time  eg: 12:21
         */
        void onTimeChange(String time);
    }

    public interface OnBatteryChangeListener {
        /**
         * 电池电量变化事件
         * @param batteryPct  0-100
         */
        void onBatteryLevelChange(float batteryPct);

        /**
         * 是否充电状态中
         * @param isCharging
         */
        void onBatteryStatusChange(boolean isCharging);
    }

    public interface OnWifiChangeListener {
        /**
         * wifi 信号变化
         * @param rssi   信号值
         * @param level  信号等级  取值范围: 1-4
         */
        void onWifiSignalChanged(int rssi, int level);
    }
}
