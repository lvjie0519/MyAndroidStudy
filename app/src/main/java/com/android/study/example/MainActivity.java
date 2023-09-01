package com.android.study.example;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.study.example.androidapi.AndroidApiTestActivity;
import com.android.study.example.androidapi.AndroidOOMActivity;
import com.android.study.example.androidapi.AudioDemoTestActivity;
import com.android.study.example.androidapi.BlueMainTestActivity;
import com.android.study.example.androidapi.HandlerTestActivity;
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
import com.android.study.example.androidapi.utils.SystemSettingUtil;
import com.android.study.example.books.BooksMainTestActivity;
import com.android.study.example.books.ThreadTestActivity;
import com.android.study.example.broadcast.LocalBroadcastRegisterDemoActivity;
import com.android.study.example.butterknife.MyButterKnifeTestActivity;
import com.android.study.example.camera.Camera2DemoActivity;
import com.android.study.example.camera.camera1.Camera1DemoActivity;
import com.android.study.example.leak.LeakTest1Activity;
import com.android.study.example.leak.LeakTestInstance;
import com.android.study.example.mainapp.MainAppTestActivity;
import com.android.study.example.performance_optimization.PerformOptMainTestActivity;
import com.android.study.example.picpro.PicProcessTestActivity;
import com.android.study.example.sqlite.SqliteTestActivity;
import com.android.study.example.thirdlib.RenderScriptBlurDemoActivity;
import com.android.study.example.thirdlib.ThirdLibTestActivity;
import com.android.study.example.uidemo.darkmode.DarkModeTestActivity;
import com.android.study.example.uidemo.filedownload.FileDownLoadDemoActivity;
import com.android.study.example.uidemo.inputdialog.CommonMsgDialog;
import com.android.study.example.uidemo.inputdialog.InputDialogDemoActivity;
import com.android.study.example.uidemo.mapsweeper.MapViewDemoActivity;
import com.android.study.example.uidemo.animation.AnimationDemoActivity;
import com.android.study.example.uidemo.dragging.FloatingActionBtnTestActivity;
import com.android.study.example.uidemo.eventtrans.ViewEventTransTestActivity;
import com.android.study.example.uidemo.photoview.demo.PhotoViewTestActivity;
import com.android.study.example.uidemo.picker.PickerTestActivity;
import com.android.study.example.uidemo.screenshot.ScreenshotTestActivity;
import com.android.study.example.uidemo.search.RvSearchAdapter;
import com.android.study.example.uidemo.search.SearchEditTestActivity;
import com.android.study.example.uidemo.webview.AgentwebTestActivity;
import com.android.study.example.uidemo.webview.WebViewDemoActivity;
import com.android.study.example.uidemo.webview.WebViewJsDemoActivity;
import com.android.study.example.uidemo.webview.WebViewUploadFileActivity;
import com.android.study.example.utils.ClipboardUtil;
import com.android.study.example.utils.FileUtil;
import com.android.study.example.websocket.WebSocketServerActivity;
import com.android.study.example.websocket.client.WebSocketClientActivity;
import com.annotaions.example.MyAnnotation;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
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

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("lvjie","MainActivity onCreate...");
        initData();
        initView();
    }

    protected void setStatusBar(boolean isTransparent) {
        if (isTransparent) {
            StatusBarUtil.setTransparent(this);
        } else {
            StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        }
    }

    private static String getAppNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String Name;
        try {
            Name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Name = "";
        }
        return Name;
    }

    private void initView(){

//        TextView textView = findViewById(R.id.tv_test_info);
//        textView.setTextIsSelectable(true);

        findViewById(R.id.btn_books_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksMainTestActivity.startActivity(MainActivity.this);
//                ViewTestActivity.startActivity(MainActivity.this);
//                FileUtil.requestSDCardPermissions(MainActivity.this, 101);
//                ClipboardUtil.writeDataToClipboard(MainActivity.this, "lvjie-clipboard");
                String name = getAppNameByPackageName(MainActivity.this, "com.example.selfchildappdemo");
                Log.i("lvjie", name);
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
//                SystemSettingUtil.openHuawei(MainActivity.this);
//                SystemSettingUtil.openAppSetting(MainActivity.this);
//                SystemSettingUtil.openGpsPage(MainActivity.this);
                WebViewDemoActivity.startActivity(MainActivity.this);
//                AgentwebTestActivity.startActivity(MainActivity.this);
//                WebViewJsDemoActivity.startActivity(MainActivity.this);
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
//        ImagePicker.with(this)
//                .setFolderMode(true)
//                .setFolderTitle("选择文件夹")
//                .setDirectoryName("Sample")
//                .setMultipleMode(false)
//                .setShowCamera(true)
//                .setMaxSize(1)
//                .setLimitMessage("超出文件选择限制")
//                .setRequestCode(2)
//                .start();
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
        startActivity(new Intent());
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

    public void onClickApplicationDialogTest(View view) {

//        if(checkRequestPermissions()){
//            showWaiting();
//        }

        addView();
    }

    private void addView() {
        Button button = new Button(this);
        button.setText("Floating Window");
        button.setBackgroundColor(Color.BLUE);
        button.setTextColor(Color.parseColor("#ffffff"));

        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = Gravity.CENTER;

        this.addContentView(button, layoutParams1);
    }

    private final void showWaiting() {
        try {
            // 获取WindowManager服务
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // 新建悬浮窗控件
            Button button = new Button(getApplicationContext());
            button.setText("Floating Window");
            button.setBackgroundColor(Color.BLUE);

            // 设置LayoutParam
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.width = 500;
            layoutParams.height = 100;

            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(button, layoutParams);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private boolean checkRequestPermissions(){
        List<String> permissionsList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("lvjielvjie", "No READ_EXTERNAL_STORAGE permission");
            permissionsList.add(Manifest.permission.CAMERA);
        }

        if (permissionsList.isEmpty()) {
            Log.i("lvjielvjie", "has permission");
            return true;
        }
        String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
        ActivityCompat.requestPermissions(this, permissions, 1001);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private CommonMsgDialog commonMsgDialog;
    private void showCommonMsgDialog(){
        if(commonMsgDialog == null){
            commonMsgDialog = new CommonMsgDialog();
        }
        commonMsgDialog.show(this.getSupportFragmentManager(), "commonMsgDialog");
    }



    public void onClickTestHandler(View view) {
//        HandlerTestActivity.startActivity(this);
        checkRequestPermissions();
    }

    public void onClickTestWebViewUploadFile(View view) {
        WebViewUploadFileActivity.startActivity(this);
    }

    private void openPicSelectPage(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, 200);
    }

    private void openFileSelectPage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"),300);
    }

    private void openCamera(){
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imagePaths = Environment.getExternalStorageDirectory().getPath() +"/pbccrc/Images/" + (System.currentTimeMillis() +".jpg");
        File vFile =new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        }else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }

        Uri cameraUri = FileProvider.getUriForFile(this,getPackageName() +".fileprovider",vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, 400);
    }

    public void onClickTestPhotoView(View view) {
        PhotoViewTestActivity.startActivity(this);
    }

    public void onClickTestWebSocket(View view) {
        WebSocketServerActivity.startActivity(this);
    }

    public void onClickTestWebSocketClient(View view) {
        WebSocketClientActivity.startActivity(this);
    }

    public void onClickTestThreadLock(View view) {
        ThreadTestActivity.startActivity(this);
    }

    public void onClickTestCamera(View view) {
        Camera2DemoActivity.startActivity(this);
    }

    public void onClickTestCamera1(View view) {
        Camera1DemoActivity.startActivity(this);
    }

    public void onClickTestSqlite(View view) {
        SqliteTestActivity.startActivity(this);
    }
}
