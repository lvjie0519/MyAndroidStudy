package com.android.study.example.androidapi.customstatusbar;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电池
 * https://blog.csdn.net/L25000/article/details/46502787
 * wifi
 * https://www.jianshu.com/p/a0fbb4644b84?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes
 */
public class SystemBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemBroadcastReceiver";
    private final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";

    // 24小时制
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    private static SystemBroadcastReceiver sInstance;
    private Context mContext;
    private OnTimeChangeListener mOnTimeChangeListener;
    private OnBatteryChangeListener mOnBatteryChangeListener;

    private List<SimSignalStrengthsListener> mSimSignalStrengthsListeners = new ArrayList<>();
    private SimSignalStrengthsListener.SignalStrengthsChangedListener mPhoneStateListener = new SimSignalStrengthsListener.SignalStrengthsChangedListener() {
        @Override
        public void onSignalStrengthsChanged(int simId, SignalStrength signalStrength) {
            // 获取当前的信号强度
            int gsmSignalStrength = signalStrength.getGsmSignalStrength(); // 对于GSM网络
            int dbm = -113 + 2 * gsmSignalStrength;
            int level = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                level = signalStrength.getLevel();
            }
            Log.i(TAG, "onSignalStrengthsChanged call, gsmSignalStrength=" + gsmSignalStrength + ", level: " + level + ", signalStrength: " + signalStrength.toString());
        }
    };

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

    /**
     * 注册广播
     * @param activity
     */
    public void registerReceiver(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "registerReceiver error, activity is null.");
            return;
        }

        mContext = activity.getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        // 时间
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);         // 时间修改
        intentFilter.addAction(Intent.ACTION_TIME_TICK);            // 系统每1分钟发送一次广播

        // 电池
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);      // 电量改变
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);          // 表示当前电池电量低
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);         // 表示当前电池已经从电量低恢复为正常
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);      // 监听充电状态的改变 插入充电器
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);   // 监听充电状态的改变 拔出充电器

        // wifi
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);            // 监听WiFi信号强度的变化
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);      // WiFi模块硬件状态改变的广播,WiFi开启, WiFi关闭
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);   // 连接状态发生改变的广播

        // sim
        intentFilter.addAction(ACTION_SIM_STATE_CHANGED);

        activity.registerReceiver(sInstance, intentFilter);
//        registerSimSignalChange(activity);
    }

    /**
     * 注册sim卡信号监听
     * @param context
     */
    private void registerSimSignalChange(Context context) {
        Log.i(TAG, "registerSimSignalChange call.");
        // 清除之前已经注册的
        unregisterSimSignalChange(context);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(mContext);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Manifest.permission.READ_PHONE_STATE not granted.");
                return;
            }

            List<SubscriptionInfo> subscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
            Log.i(TAG, "sim size: " + subscriptionInfoList.size());
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                SimSignalStrengthsListener listener = new SimSignalStrengthsListener(subscriptionInfo.getSubscriptionId());
                listener.setListener(mPhoneStateListener);
                mSimSignalStrengthsListeners.add(listener);
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        } else {
            // android 5.1 没提供标准的获取sim卡的信息
            Log.i(TAG, "will listener sim signal, build version is less than Android 5.1.");
            registerSimSignalChangeCommon(telephonyManager);
        }
    }

    private void registerSimSignalChangeCommon(TelephonyManager telephonyManager) {
        SimSignalStrengthsListener listener = new SimSignalStrengthsListener(SimSignalStrengthsListener.SIM_ID_NONE);
        listener.setListener(mPhoneStateListener);
        mSimSignalStrengthsListeners.add(listener);
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void unregisterReceiver(Activity activity) {
        if(activity == null){
            Log.e(TAG, "unregisterReceiver error, activity is null.");
            return;
        }

        activity.unregisterReceiver(sInstance);
//        unregisterSimSignalChange(activity);
    }

    private void unregisterSimSignalChange(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        for (SimSignalStrengthsListener listener : mSimSignalStrengthsListeners) {
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        }
        mSimSignalStrengthsListeners.clear();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action:" + action + ", intent: " + intent);

        switch (action) {
            // **********************  时间变化相关 start ****************************
            case Intent.ACTION_TIME_CHANGED:    // 时间修改
            case Intent.ACTION_TIME_TICK:       // 系统每1分钟发送一次广播
                doOnTimeChange(intent);
                break;

            // **********************  电量相关 start ****************************
            case Intent.ACTION_BATTERY_CHANGED:         // 电量改变
                doOnBatteryChanged(intent);
                break;
            case Intent.ACTION_BATTERY_LOW:             // 表示当前电池电量低
            case Intent.ACTION_BATTERY_OKAY:            // 表示当前电池已经从电量低恢复为正常
                // TODO lvjie 后续再做能力支持
                break;
            // 监听充电状态的改变 插入/拔出充电器
            case Intent.ACTION_POWER_CONNECTED:
            case Intent.ACTION_POWER_DISCONNECTED:
                break;

            // **********************  wifi相关 start ****************************
            case WifiManager.RSSI_CHANGED_ACTION:       // 监听WiFi信号强度的变化
                doOnWifiSignalChanged(intent);
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:  // WiFi模块硬件状态改变的广播,WiFi开启, WiFi关闭
                doOnWifiStateChanged(intent);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:  // 连接状态发生改变的广播
                break;
            default:
                break;

            // **********************  sim卡相关 start ****************************
            case ACTION_SIM_STATE_CHANGED:
                Log.i(TAG, "action: " + intent.getAction() + ", intent: " + intent);
                doOnSimStateChanged(intent);
                break;
        }
    }

    private void doOnTimeChange(Intent intent) {
        if (mOnTimeChangeListener != null) {
            mOnTimeChangeListener.onTimeChange(formatter.format(new Date()));
        }
    }

    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.mOnTimeChangeListener = onTimeChangeListener;
    }

    /**
     * 返回当前  时:分
     * @return
     */
    public String getCurrentTime() {
        return formatter.format(new Date());
    }

    private void doOnBatteryChanged(Intent intent) {
        // 当前电量百分比
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        // 电池电量总刻度
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        // 计算百分比
        float batteryPct = level / (float) scale * 100;
        Log.i(TAG, "doOnBatteryChanged Battery Level: " + batteryPct + "%");
        if (mOnBatteryChangeListener != null) {
            mOnBatteryChangeListener.onBatteryStatusChange(batteryPct, false);
        }
    }

    public void setOnBatteryChangeListener(OnBatteryChangeListener onBatteryChangeListener) {
        this.mOnBatteryChangeListener = onBatteryChangeListener;
    }

    private void doOnWifiSignalChanged(Intent intent) {
        int rssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -1000);
        int level = WifiManager.calculateSignalLevel(rssi, 4);

        int level2 = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            level2 = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE)).calculateSignalLevel(rssi);
        }

        Log.i(TAG, "doOnWifiSignalChanged Current RSSI: " + rssi + ", level: " + level + ", level2: " + level2);
    }

    /**
     * 获取sim卡信号
     * @param signalStrength
     * @return
     */
    private int getSimSingleLevel(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }

        int level = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            level = signalStrength.getLevel();
        }else{
            level = signalStrength.getGsmSignalStrength();
        }
        if (signalStrength.isGsm()) {
            // 移动或联通卡
        }

        return level;
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
    private void doOnWifiStateChanged(Intent intent) {
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        Log.i(TAG, "doOnWifiStateChanged Current state: " + state);
    }

    private void doOnSimStateChanged(Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        Log.i(TAG, "doOnSimStateChanged call, simState:" + simState);
        switch (simState) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                // SIM卡未知状态
                break;
            case TelephonyManager.SIM_STATE_ABSENT:
                // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // SIM卡已插入，需要PIN码解锁
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // SIM卡已插入，需要PUK码解锁
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                // SIM卡已插入，网络被锁定
                break;
            case TelephonyManager.SIM_STATE_READY:
                // SIM卡已插入，并且已经准备好使用
                break;
            default:
                break;
        }
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
         *
         * 电池状态变化事件
         * @param batteryPct  电池电量变 0-100
         * @param isCharging  是否充电状态中
         */
        void onBatteryStatusChange(float batteryPct, boolean isCharging);
    }
}
