package com.android.study.example.androidapi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.study.example.MainActivity;
import com.android.study.example.R;
import com.android.study.example.androidapi.utils.FileUtils;
import com.android.study.example.androidapi.utils.MimeTypeUtils;
import com.android.study.example.androidapi.utils.ProcessUtils;
import com.android.study.example.androidapi.views.MyFpsTestView;
import com.android.study.example.books.TaskStackTestActivity;
import com.android.study.example.uidemo.dragging.DraggingButton;
import com.android.study.example.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import rx.functions.Action1;

public class AndroidApiTestActivity extends AppCompatActivity {

    private TextView tvShowInfo;

    private RxPermissions rxPermissions;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private IntentFilter intentFilter;

    private DraggingButton mDraggintTextView;
    private MyFpsTestView myFpsTestView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AndroidApiTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alterDisplayMetrics();
        setContentView(R.layout.activity_android_api_test);

        rxPermissions = RxPermissions.getInstance(this);
        initView();

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, intentFilter);
    }

    private void initView() {
        tvShowInfo = (TextView) findViewById(R.id.tv_show_info);

        findViewById(R.id.btn_get_screen_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("lvjie", getHeight(AndroidApiTestActivity.this) + "   " + getRealHeight(AndroidApiTestActivity.this));
                QuanMianPingUtils.hasNavBar(AndroidApiTestActivity.this);
                QuanMianPingUtils.getNavigationBarHeight(AndroidApiTestActivity.this);
                boolean hs = QuanMianPingUtils.hasNavigationBar1(AndroidApiTestActivity.this);
                getScreenInfos();
            }
        });

        findViewById(R.id.btn_alter_DisplayMetrics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterDisplayMetrics();
            }
        });

        findViewById(R.id.btn_go_next_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AndroidApiTestActivity.this, AndroidApiTestActivity.class));
//                keepScreenNotLock(true);
                showPhoneInfo();
            }
        });

        findViewById(R.id.btn_write_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "plugin.log";
                int tag = new Random().nextInt();
//                FileUtils.writeLog(tag+"、  aaaaaaa", filePath);
                writeFile();
            }
        });

        findViewById(R.id.btn_read_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
            }
        });

        findViewById(R.id.btn_file_softLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 软连接
                String srcFilePath = getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "linked";
                String targetFilePath = getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "linking";

                if(new File(srcFilePath).exists()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.createSymbolicLink(FileSystems.getDefault().getPath(targetFilePath), FileSystems.getDefault().getPath(srcFilePath));
                        } catch (IOException e) {
                            Log.e("lvjie", Log.getStackTraceString(e));
                        }
                    }
                }

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Os.symlink(srcFilePath, targetFilePath);
                        } catch (Exception e) {
                            Log.e("lvjie", Log.getStackTraceString(e));
                        }
                    }else{
                        // 反射来解决
                        final Class<?> libcore = Class.forName("libcore.io.Libcore");
                        final java.lang.reflect.Field fOs = libcore.getDeclaredField("os");
                        fOs.setAccessible(true);
                        final Object os = fOs.get(null);
                        final java.lang.reflect.Method method = os.getClass().getMethod("symlink", String.class, String.class);
                        method.invoke(os, srcFilePath, targetFilePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_file_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 硬连接

                String srcFilePath = getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "linked";
                String targetFilePath = getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "linking";

                if(new File(srcFilePath).exists()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.createLink(FileSystems.getDefault().getPath(targetFilePath), FileSystems.getDefault().getPath(srcFilePath));
                        } catch (IOException e) {
                            Log.e("lvjie", Log.getStackTraceString(e));
                        }
                    }
                }


            }
        });

        findViewById(R.id.btn_package_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPackageInfo();
            }
        });

        findViewById(R.id.btn_app_file_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAppFileInfo();
            }
        });

        findViewById(R.id.btn_test_nfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHasNFCFunc();
            }
        });

        findViewById(R.id.btn_test_operators).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取运营商
                getOperator(AndroidApiTestActivity.this);
            }
        });

        findViewById(R.id.btn_test_wifi_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取wifi信息
                printWifiInfo();
            }
        });

        findViewById(R.id.btn_test_build_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取wifi信息
                printBuildInfo();
            }
        });

        findViewById(R.id.btn_get_gps_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGpsOpen();
            }
        });

        findViewById(R.id.btn_start_system_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开系统gps设置页
                openGpsSettingPage();
            }
        });

        mDraggintTextView = (DraggingButton) findViewById(R.id.tv_dragging);
        mDraggintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AndroidApiTestActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        myFpsTestView = findViewById(R.id.view_myfps);
    }

    public static int getHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }


    private void getScreenInfos() {
        StringBuilder stringBuilder = new StringBuilder();

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int pxWidth = dm.widthPixels;
        int pxHeight = dm.heightPixels;
        int dpi = dm.densityDpi;
        float density = dm.density;

        stringBuilder.append("屏幕像素宽度： ").append(pxWidth).append(" px\n")
                .append("屏幕像素高度： ").append(pxHeight).append("px \n")
                .append("屏幕dpi值： ").append(dpi).append("\n")
                .append("屏幕density(dpi/160)值： ").append(density).append("\n");

        tvShowInfo.setText(stringBuilder.toString());
    }

    public void alterDisplayMetrics() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.density = 2.13333334f;
        displayMetrics.densityDpi = 341;
        displayMetrics.scaledDensity = 2.13333334f;
        displayMetrics.xdpi = 320;
    }

    /**
     * 屏幕常亮
     * @param light
     */
    public void keepScreenNotLock(boolean light) {
        if (light) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void showPhoneInfo() {
        String language = Locale.getDefault().getLanguage();    // 获取当前手机系统语言
        String sysVersion = android.os.Build.VERSION.RELEASE;   // 获取当前手机系统版本号
        String phoneModel = android.os.Build.MODEL;             // 获取手机型号
        String phoneCatory = android.os.Build.BRAND;            // 获取手机厂商

        Log.i("lvjie", "" + language + "    " + sysVersion + "    " + phoneModel + "    " + phoneCatory);
    }

    public static boolean isAllScreenDevice(Context context) {

        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                return true;
            }
        }
        return false;
    }

    private void writeFile() {
        String filePath = getFilesDir().getPath() + File.separator + "debug_list.txt";
        Log.i("lvjie", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<JSONObject> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("pluginName", "com.xiaomi.demo" + i);
                jsonObject.put("devicModel", "chuangmi.plugin.m" + i);
                jsonObject.put("isCheck", true);
            } catch (JSONException e) {

            }
            datas.add(jsonObject);
        }

        try {
            OutputStream outputStream = new FileOutputStream(file);
            if (outputStream != null) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String line;
                for (int i = 0; i < datas.size(); i++) {
                    bufferedWriter.write(datas.get(i).toString());
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
                outputStreamWriter.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initData() {

    }

    private void readFile() {

        String filePath = getFilesDir().getPath() + File.separator + "debug_list.txt";
//        String filePath = this.getExternalFilesDir("logs12").getAbsolutePath() + File.separator + "plugin.log";
        File file = new File(filePath);
        if (!file.exists()) {
            Log.i("lvjie", filePath + " file is not exit");
            return;
        }

        List<String> listStr = new ArrayList<>();

        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    listStr.add(line);
                }
                buffreader.close();
                instream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listStr.size(); i++) {
            Log.i("lvjie", listStr.get(i));
            try {
                JSONObject jsonObject = new JSONObject(listStr.get(i));
                Log.i("lvjie", jsonObject.getString("pluginName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void testPackageInfo() {
//        PackageInfo info = null;
//        try {
//            info = AndroidApiTestActivity.this.getPackageManager().getPackageInfo(AndroidApiTestActivity.this.getPackageName(),
//                    0);
//
//            String packageInfo = "this.getPackageName(): " + AndroidApiTestActivity.this.getPackageName()
//                    + "\n   info.packageName: " + info.packageName             // 应用程序包名
//                    + "\n  info.versionName: " + info.versionName              // 版本名称， 对应build.gradle versionName
//                    + "\n  info.versionCode: " + info.versionCode + "";          // 版本号， 对应build.gradle  versionCode
//            tvShowInfo.setText(packageInfo);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        PackageInfo info = null;
        try {
            info = AndroidApiTestActivity.this.getPackageManager().getPackageInfo("com.example.selfchildappdemo",
                    PackageManager.GET_META_DATA | PackageManager.GET_UNINSTALLED_PACKAGES);

            String packageInfo = "this.getPackageName(): " + AndroidApiTestActivity.this.getPackageName()
                    + "\n   info.packageName: " + info.packageName             // 应用程序包名
                    + "\n  info.versionName: " + info.versionName              // 版本名称， 对应build.gradle versionName
                    + "\n  info.versionCode: " + info.versionCode + "";          // 版本号， 对应build.gradle  versionCode
            tvShowInfo.setText(packageInfo);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testAppFileInfo() {

        String fileInfo = "this.getFilesDir().getPath():  " + this.getFilesDir().getPath()        // /data/user/0/com.android.study.example/files
                + "\n  this.getFilesDir().getAbsolutePath(): " + this.getFilesDir().getAbsolutePath()  // /data/user/0/com.android.study.example/files
                + "\n  this.getCacheDir().getPath(): " + this.getCacheDir().getPath()      // /data/user/0/com.android.study.example/cache
                + "\n  this.getCacheDir().getAbsolutePath(): " + this.getCacheDir().getAbsolutePath() // /data/user/0/com.android.study.example/cache
                + "\n  this.getApplicationInfo().dataDir: " + this.getApplicationInfo().dataDir   // /data/user/0/com.android.study.example
                + "\n  this.getExternalFilesDir(\"logs\").getAbsolutePath(): " + this.getExternalFilesDir("logs").getAbsolutePath()   // /storage/emulated/0/Android/data/com.android.study.example/files/logs
                + "\n  Environment.getExternalStorageDirectory(): "+ Environment.getExternalStorageDirectory()  // /storage/emulated/0
                + "\n  this.getExternalCacheDir(): "+ this.getExternalCacheDir()  // /storage/emulated/0/Android/data/com.android.study.example/cache
                + "\n  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES): "+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) // /storage/emulated/0/Pictures
                + "\n  this.getExternalFilesDir(Environment.DIRECTORY_PICTURES): "+ this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)  // /storage/emulated/0/Android/data/com.android.study.example/files/Pictures
                + "\n  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM): "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);  // /storage/emulated/0/DCIM
        Log.i("lvjie", fileInfo);
        tvShowInfo.setText(fileInfo);


    }

    public void testHasNFCFunc() {
        PackageManager pm = getPackageManager();
        boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
        Toast.makeText(this, String.format("NFC支持%s", nfc), Toast.LENGTH_SHORT).show();
    }

    // 获取运营商
    public void getOperator(final Context context) {


        rxPermissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {

                if (granted) {
                    // 权限申请成功

                    String providerName = "";
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        String IMSI = telephonyManager.getSubscriberId();
                        Log.i("qweqwes", "运营商代码" + IMSI);
                        if (IMSI != null) {
                            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                                providerName = "中国移动";
                            } else if (IMSI.startsWith("46001")  || IMSI.startsWith("46006")) {
                                providerName = "中国联通";
                            } else if (IMSI.startsWith("46003")) {
                                providerName = "中国电信";
                            }
                        }
                    }

                    // 参考网址  http://cn.voidcc.com/question/p-cjhgdmrt-bkk.html
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                        SubscriptionManager subscriptionManager=(SubscriptionManager)getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                        List<SubscriptionInfo> subscriptionInfoList=subscriptionManager.getActiveSubscriptionInfoList();
                        if(subscriptionInfoList!=null && subscriptionInfoList.size()>0){
                            for(SubscriptionInfo info:subscriptionInfoList){
                                String carrierName = info.getCarrierName().toString();
                                String mobileNo=info.getNumber();
                                String countyIso=info.getCountryIso();
                                int dataRoaming=info.getDataRoaming();
                                Log.i("lvjie", "carrierName: "+carrierName+"  mobileNo: "+mobileNo
                                        +"  countyIso: "+countyIso+"   dataRoaming: "+dataRoaming);
                            }
                        }
                    }



                }

            }
        });

    }


    private void printWifiInfo() {

        // 获取wifi的SSID， 在Android9.0之前，可以直接获取，9.0及之后，需要手机先开启位置服务，才能获取. Android 9（API 级别 28）
        if(Build.VERSION.SDK_INT < 28){
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.i("lvjie", "BSSID: "+wifiInfo.getBSSID()+"  SSID: "+wifiInfo.getSSID());
        }else{
            if(isLocationEnabled()){
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                Log.i("lvjie", "BSSID: "+wifiInfo.getBSSID()+"  SSID: "+wifiInfo.getSSID());
            }else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }

    }

    /**
     * 获取广播地址
     * @param view
     */
    public void onClickGetWifiBroadcast(View view) {
        InetAddress address = getWifiBroadcastAddressInner(this);
        Log.i("lvjie", address.getHostAddress());
    }

    public static InetAddress getWifiBroadcastAddressInner(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isWifi = (activeNetwork != null) && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
        if (isWifi) {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcp = wifi.getDhcpInfo();
            if (dhcp != null) {
                int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
                byte[] quads = new byte[4];
                for (int k = 0; k < 4; k++) {
                    quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
                }
                try {
                    return InetAddress.getByAddress(quads);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 系统服务是否开启定位功能
     *
     * @return
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(this.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;
            } else {
                locationProviders = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void printBuildInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Android系统定制商 Build.BRAND: "+Build.BRAND+"\n");
        stringBuffer.append("设备参数 Build.DEVICE: "+Build.DEVICE+"\n");
        stringBuffer.append("显示屏参数 Build.DISPLAY: "+Build.DISPLAY+"\n");
        stringBuffer.append("手机制造商 Build.PRODUCT: "+Build.PRODUCT+"\n");
        stringBuffer.append("用户 Build.USER: "+Build.USER+"\n");
        stringBuffer.append("当前开发代号 Build.VERSION.CODENAME: "+Build.VERSION.CODENAME+"\n");
        stringBuffer.append("源码控制版本号 Build.VERSION.INCREMENTAL: "+Build.VERSION.INCREMENTAL+"\n");
        stringBuffer.append("运行系统版本号 Build.VERSION.SDK_INT: "+Build.VERSION.SDK_INT+"\n");
        stringBuffer.append("Build.VERSION.PREVIEW_SDK_INT: "+Build.VERSION.PREVIEW_SDK_INT+"\n");
        stringBuffer.append("Build.VERSION_CODES.M: "+Build.VERSION_CODES.M+"\n");

        tvShowInfo.setText(stringBuffer.toString());
    }

    private void checkGpsOpen(){
        boolean result = false;

        // 获取gps 是否开启状态
        LocationManager locationManager
                = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps|| network) {
            result = true;
        }

        ToastUtil.showToast(this, "gps 是否打开： "+result);
    }

    private void openGpsSettingPage(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }


    /**
     * 显示界面刷新 帧率
     * @param view
     */
    public void onClickShowPageFps(View view){
        double data = ((double) (1) * 1e9);
        Log.i("lvjie", "1e9="+((long)data));
        subscriberFrame = !subscriberFrame;
        lastFrameTimeNanos = System.currentTimeMillis();
        Choreographer.getInstance().postFrameCallback(frameCallback);
        mHandler.sendMessage(new Message());
    }

    /**
     * Choreographer.getInstance().postFrameCallback   仅仅只是监听即将显示下一帧的时间，frameTimeNanos 纳秒
     * doFrame  回调时机受到view绘制的影响
     */
    private int times = 0;
    private long lastFrameTimeNanos = 0;
    private boolean subscriberFrame = false;
    private Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {

            Log.i("lvjie", "times: "+times+"  frameTimeNanos: "+frameTimeNanos+"  divide: "+((frameTimeNanos-lastFrameTimeNanos)/1000000));
            times++;
            lastFrameTimeNanos = frameTimeNanos;
            if(subscriberFrame){
                Choreographer.getInstance().postFrameCallback(frameCallback);
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            myFpsTestView.invalidate();
            if(subscriberFrame){
                mHandler.sendMessageDelayed(new Message(), 5);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mNetworkChangeReceiver);
    }

    public void onClickOpenSysFileDialog(View view) {
//        String filePath = getFilesDir().getPath() + File.separator + "debug_list.txt";
        String filePath = getFilesDir().getPath() + File.separator +"myfile"+File.separator+ "debug_list.txt";
        File file = new File(filePath);
        openFileWithSysDialog(this, file);
    }

    private void openFileWithSysDialog(Context context, File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        }else{

            final String packageName = context.getApplicationContext().getPackageName();
            final String authority =  new StringBuilder(packageName).append(".provider").toString();
//            intent.setDataAndType(FileProvider.getUriForFile(this, authority, file), type);
            String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1586433650723&di=44f673bd1ccbde2af32e1f7565de4484&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw640h759%2F20180111%2F9ee0-fyqnick6733536.jpg";
            intent.setDataAndType(Uri.parse(url), MimeTypeUtils.getMimeType("jpg"));
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //跳转
        context.startActivity(intent);
    }


    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();

        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        /* 获取文件的后缀名*/
        String extension = fName.substring(dotIndex+1, fName.length()).toLowerCase();
        if (TextUtils.isEmpty(extension)) {
            return type;
        }
        type = MimeTypeUtils.getMimeType(extension);
        return type;
    }

    public void onClickAssertFile(View view) {
        try {
            // 只能列出文件
            String[] strings = this.getAssets().list("my_config3"+File.separator+"config1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickCheckAppRunning(View view) {
        ProcessUtils.checkAppRunningWithPkg(this, "");
    }

    public void onClickBackSystemHome(View view) {
        // 返回系统桌面
        goBackSystemHome();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBackSystemHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBackSystemHome(){
        Intent mIntent = new Intent(Intent.ACTION_MAIN);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(mIntent);
    }

    // 监听应用卸载安装
    public void onClickListenerAppInstall(View view) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(mPackageMonitor, filter);
    }

    private final BroadcastReceiver mPackageMonitor = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent != null ? intent.getAction() : null;

            if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                // 应用安装，会走到这里
                Uri data = intent.getData();
                data.getSchemeSpecificPart();

            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                Uri data = intent.getData();
                data.getSchemeSpecificPart();

                /**
                 * 当覆盖安装的时候，先会发起卸载事件，但是intent.getBooleanExtra(Intent.EXTRA_REPLACING, false) 会返回true
                 * 非覆盖安装的情况下，intent.getBooleanExtra(Intent.EXTRA_REPLACING, false) 返回false
                 */
                if (!intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {

                }

                TaskStackTestActivity.startActivityForClearTask(AndroidApiTestActivity.this);
            }
        }
    };

    public void onClickChoicePic(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<ResolveInfo> datas = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY | PackageManager.GET_RESOLVED_FILTER);
        Log.i("lvjie", "size: "+datas.size());   // 数据为2

        intent.setData(Uri.parse("content://com.sangfor.emm.providers.media/external/images/media"));
        datas = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        Log.i("lvjie", "size: "+datas.size());   // 数据为1
    }

    public void onClickGetAndroidId(View view) {

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // 没有权限，申请权限
//            String [] permissions = {Manifest.permission.READ_PHONE_STATE};
//            ActivityCompat.requestPermissions(this, permissions,1);
//        } else {
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            String deviceId = telephonyManager.getDeviceId();
//            Log.i("lvjie", "1-deviceId: "+deviceId);
//        }


        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
            }
        }
    }

    private static class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            NetworkInfo networkInfo = null;
            int networkType = -1;
            if(intent.getExtras() != null){
                networkInfo = (NetworkInfo) intent.getExtras().get("networkInfo");
                networkType = (int) intent.getExtras().get("networkType");
            }
            Log.i("lvjie", "networkType="+networkType
                    +"  getType="+networkInfo.getType()
                    +"  getTypeName="+networkInfo.getTypeName()
                    +"  getState().name()="+networkInfo.getState().name());
            Toast.makeText(context, "网络状态改变", Toast.LENGTH_SHORT).show();
        }
    }

}
