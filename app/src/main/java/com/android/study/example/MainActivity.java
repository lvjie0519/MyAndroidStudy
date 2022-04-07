package com.android.study.example;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.study.example.androidapi.AndroidApiTestActivity;
import com.android.study.example.androidapi.AndroidOOMActivity;
import com.android.study.example.androidapi.AudioDemoTestActivity;
import com.android.study.example.androidapi.BlueMainTestActivity;
import com.android.study.example.androidapi.LifeCycleTestActivity;
import com.android.study.example.androidapi.NetworkTestActivity;
import com.android.study.example.androidapi.OpenOtherAppActivity;
import com.android.study.example.androidapi.OrientationTestActivity;
import com.android.study.example.androidapi.ScreenAdapterTestActivity;
import com.android.study.example.androidapi.ShareIsolationTestActivity;
import com.android.study.example.androidapi.SharedPreferencesTestActivity;
import com.android.study.example.androidapi.SurfaceViewDrawDemoActivity;
import com.android.study.example.androidapi.ViewTestActivity;
import com.android.study.example.androidapi.WuZhangAiTestActivity;
import com.android.study.example.books.BooksMainTestActivity;
import com.android.study.example.broadcast.LocalBroadcastRegisterDemoActivity;
import com.android.study.example.butterknife.MyButterKnifeTestActivity;
import com.android.study.example.leak.LeakTest1Activity;
import com.android.study.example.leak.LeakTestInstance;
import com.android.study.example.mainapp.MainAppTestActivity;
import com.android.study.example.performance_optimization.PerformOptMainTestActivity;
import com.android.study.example.picpro.PicProcessTestActivity;
import com.android.study.example.thirdlib.RenderScriptBlurDemoActivity;
import com.android.study.example.thirdlib.ThirdLibTestActivity;
import com.android.study.example.uidemo.darkmode.DarkModeTestActivity;
import com.android.study.example.uidemo.filedownload.FileDownLoadDemoActivity;
import com.android.study.example.uidemo.inputdialog.InputDialogDemoActivity;
import com.android.study.example.uidemo.mapsweeper.MapViewDemoActivity;
import com.android.study.example.uidemo.animation.AnimationDemoActivity;
import com.android.study.example.uidemo.dragging.FloatingActionBtnTestActivity;
import com.android.study.example.uidemo.eventtrans.ViewEventTransTestActivity;
import com.android.study.example.uidemo.picker.PickerTestActivity;
import com.android.study.example.uidemo.screenshot.ScreenshotTestActivity;
import com.android.study.example.uidemo.search.RvSearchAdapter;
import com.android.study.example.uidemo.search.SearchEditTestActivity;
import com.android.study.example.uidemo.webview.AgentwebTestActivity;
import com.android.study.example.uidemo.webview.WebViewDemoActivity;
import com.android.study.example.uidemo.webview.WebViewJsDemoActivity;
import com.android.study.example.utils.ClipboardUtil;
import com.android.study.example.utils.FileUtil;
import com.annotaions.example.MyAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@MyAnnotation("hello world")
public class MainActivity extends AppCompatActivity {

    private OkHttpClient mClient;
    private Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("lvjie","MainActivity onCreate...");
        initData();
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_books_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksMainTestActivity.startActivity(MainActivity.this);
//                ViewTestActivity.startActivity(MainActivity.this);
//                FileUtil.requestSDCardPermissions(MainActivity.this, 101);
//                ClipboardUtil.writeDataToClipboard(MainActivity.this, "lvjie-clipboard");
            }
        });


        findViewById(R.id.btn_butterknife_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
//                ButterKnifeTestActivity.startActivity(MainActivity.this);
                PickerTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_mybutterknife_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyButterKnifeTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_androidapi_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidApiTestActivity.startActivity(MainActivity.this);
            }
        });

        // okhttp 同步
        findViewById(R.id.btn_okhttp_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synOkHttpTest();
            }
        });

        // okhttp 异步
        findViewById(R.id.btn_okhttp_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asynOkHttpTest();
            }
        });

        findViewById(R.id.btn_blu_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlueMainTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_leak_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeakTestInstance.getInstance().clean();
                LeakTest1Activity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_dragging_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingActionBtnTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_audio_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioDemoTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_life_cycle_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                overridePendingTransition(0, 0);
                LifeCycleTestActivity.startActivity(MainActivity.this);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("lvjie", "overridePendingTransition...");
//                overridePendingTransition(0, 0);

//                Intent intent = new Intent(MainActivity.this, LifeCycleTestActivity.class);
//                intent.putExtra("startTime", System.currentTimeMillis());
//                startActivity(intent);
//                overridePendingTransition(0, 0);
            }
        });
        findViewById(R.id.btn_render_script_blur_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RenderScriptBlurDemoActivity.startActivity(MainActivity.this);
            }
        });
        findViewById(R.id.btn_surface_view_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurfaceViewDrawDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_view_event_trans_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewEventTransTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_animation_demo_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_map_sweeper_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapViewDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_input_dialog_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialogDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_webview_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebViewDemoActivity.startActivity(MainActivity.this);
//                AgentwebTestActivity.startActivity(MainActivity.this);
                WebViewJsDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_oom_api_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidOOMActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_oritentation_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrientationTestActivity.startActivity(MainActivity.this);

//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
//
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);

            }
        });

        findViewById(R.id.btn_activity_screen_shot_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenshotTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_download_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownLoadDemoActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_rv_search_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEditTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_screen_font_adapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenAdapterTestActivity.startActivity(MainActivity.this);
            }
        });

        findViewById(R.id.btn_activity_goto_mijia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForscheme();
//                    startActivityForExported();
            }
        });

        findViewById(R.id.btn_activity_dark_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DarkModeTestActivity.startActivity(MainActivity.this);
            }
        });
    }


    public void onClickShareTest(View view) {
        ShareIsolationTestActivity.startActivity(this);
//        String filePath = "/sdcard/lvjie/test/lvjie_test.txt";
//        FileUtil.writeContentToFile(filePath, "aaa");
//        String content = FileUtil.readContentFromFile(filePath);
//        Log.i("lvjie", content);
    }

    /**
     * 跨app跳转方式一
     */
    private void startActivityForscheme(){
//        String url = "git=133165651";
//                String url = "https://g.home.mi.com/otherPlatform?target=MiHomePlugin&action=ACTIVATE_NFC_FOR_LOCK&uid=xxxx";
//                String url = "mihome://plugin?pageName=feedBack&action=ACTIVATE_NFC_FOR_LOCK&uid=8941581051";
//                String url = "https://com.jack.demo/otherPlatform?target=MiHomePlugin&action=ACTIVATE_NFC_FOR_LOCK&uid=8941581051";
                String url = "sfapp.com.sangfor.testurlscheme://auth_helper";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if(activities.size()>0){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Log.i("lvjie", "cannot find this uri");
        }


        Log.i("lvjie", "app main activity task id is "+getTaskId());
    }

    /**
     * 跨app跳转方式二
     */
    private void startActivityForExported(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.android.example.myaidlclient", "com.android.example.myaidlclient.outapp.ExportedActivity");
        startActivity(intent);
    }

    private void startActivityForAction(){
        Intent intent = new Intent();
        intent.setPackage("com.ang.chapter_2_service");
        intent.setAction("com.ang.poolBinder");
        startService(intent);
    }

    private void initData(){
        mClient = new OkHttpClient.Builder()
                .cache(new Cache(new File("cache"), 24*1024))
                .build();
        mRequest = new Request.Builder().url("http://www.baidu.com").get().build();
    }

    public static String getAppFilesDir(Context context) {

        String filesDir;
        try {
            filesDir = context.getFilesDir().getPath();
        } catch (Throwable e) {
            try {
                filesDir = File.separator + "data" + File.separator + "data" + File.separator + context.getPackageName() + File.separator + "files";
                File file = new File(filesDir);
                file.mkdirs();
            } catch (Throwable ep) {
                File files = context.getDir("files", Context.MODE_PRIVATE);
                files.mkdirs();
                filesDir = files.getPath();
            }
        }
        return filesDir;
    }

    private void synOkHttpTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call mCall = mClient.newCall(mRequest);
                try {
                    Response response = mCall.execute();
                    System.out.println(response.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void asynOkHttpTest(){
        System.out.println(Thread.currentThread().getName());
        Call mCall = mClient.newCall(mRequest);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 在子线程中执行， 线程名为：OkHttp http://www.baidu.com/
                System.out.println(Thread.currentThread().getName());
                System.out.println(response.body().string());
            }
        });

    }

    public void onClickThirdLibTestActivity(View view) {
        ThirdLibTestActivity.startActivity(this);
    }

    public void openPerformOptActivity(View view) {
        PerformOptMainTestActivity.startActivity(this);
    }

    public void openWuZhangAiTestPage(View view) {
        WuZhangAiTestActivity.startActivity(this);
    }

    public void openSharedPreferencesTestPage(View view) {
        SharedPreferencesTestActivity.startActivity(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lvjie","MainActivity onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie","MainActivity onResume..."+this.getTaskId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lvjie","MainActivity onNewIntent...");
        mCallbackIntent = intent.getParcelableExtra("EXTRA_HOSTAPP_GO_BACK_BASE_INTENT");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lvjie","MainActivity onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie","MainActivity onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lvjie","MainActivity onDestroy...");

    }

    public void onClickOpenOtherApp(View view) {
        OpenOtherAppActivity.startActivity(this);
    }

    private Intent mCallbackIntent = null;
    public void onClickPullSubApp(View view) {
        startActivity(getIntent2());
    }

    private Intent getIntent1(){
        if(mCallbackIntent == null){
            mCallbackIntent = getIntent().getParcelableExtra("EXTRA_HOSTAPP_GO_BACK_BASE_INTENT");
        }
        String subAppPackageName = mCallbackIntent.getStringExtra("EXTRA_HOSTAPP_GO_BACK_PACKAGE_NAME");
        String subAppClassName = mCallbackIntent.getStringExtra("EXTRA_HOSTAPP_GO_BACK_CLASS_NAME");

        Intent intent;
        if(!TextUtils.isEmpty(subAppClassName) && !TextUtils.isEmpty(subAppPackageName)){
            intent = new Intent();
            intent.setComponent(new ComponentName(subAppPackageName, subAppClassName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }else{
            intent = mCallbackIntent;
        }
        intent.putExtra("host_key", "host_value");

        Log.i("lvjie", intent.toString());
        return intent;
    }

    private Intent getIntent2() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.example.myaidlclient", "com.android.example.myaidlclient.Main2Activity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return intent;
    }

    public void onClickOpenBroadcastTestPage(View view) {
        LocalBroadcastRegisterDemoActivity.startActivity(this);
    }

    public void onClickNetworkTest(View view) {
        NetworkTestActivity.startActivity(this);
    }

    public void btnOnClickMainSub(View view) {
        MainAppTestActivity.startActivity(this);
    }

    public void btnOnClickPicProcessTest(View view) {
        PicProcessTestActivity.startActivity(this);
    }
}
