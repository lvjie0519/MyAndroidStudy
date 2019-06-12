package com.android.study.example.androidapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.study.example.MainActivity;
import com.android.study.example.R;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.functions.Action1;

public class AndroidApiTestActivity extends AppCompatActivity {

    private TextView tvShowInfo;

    private RxPermissions rxPermissions;

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
                writeFile();
            }
        });

        findViewById(R.id.btn_read_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
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
        PackageInfo info = null;
        try {
            info = AndroidApiTestActivity.this.getPackageManager().getPackageInfo(AndroidApiTestActivity.this.getPackageName(),
                    0);

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
                + "\n  this.getApplicationInfo().dataDir: " + this.getApplicationInfo().dataDir;   // /data/user/0/com.android.study.example

        Log.i("lvjie", fileInfo);
        tvShowInfo.setText(fileInfo);


    }

    public void testHasNFCFunc() {
        PackageManager pm = getPackageManager();
        boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
        Toast.makeText(this, String.format("NFC支持%s", nfc), Toast.LENGTH_SHORT).show();
    }

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


                }

            }
        });

    }


}
