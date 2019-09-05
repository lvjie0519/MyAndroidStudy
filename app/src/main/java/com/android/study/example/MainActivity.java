package com.android.study.example;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.study.example.androidapi.AndroidApiTestActivity;
import com.android.study.example.androidapi.AudioDemoTestActivity;
import com.android.study.example.androidapi.BlueMainTestActivity;
import com.android.study.example.androidapi.LifeCycleTestActivity;
import com.android.study.example.androidapi.SurfaceViewDrawDemoActivity;
import com.android.study.example.books.BooksMainTestActivity;
import com.android.study.example.butterknife.ButterKnifeTestActivity;
import com.android.study.example.butterknife.MyButterKnifeTestActivity;
import com.android.study.example.leak.LeakTest1Activity;
import com.android.study.example.leak.LeakTestInstance;
import com.android.study.example.thirdlib.RenderScriptBlurDemoActivity;
import com.android.study.example.uidemo.dragging.FloatingActionBtnTestActivity;
import com.android.study.example.uidemo.picker.PickerTestActivity;
import com.annotaions.example.MyAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

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

        initData();
        initView();
    }

    private void initView(){
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
                LifeCycleTestActivity.startActivity(MainActivity.this);
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

        findViewById(R.id.btn_books_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksMainTestActivity.startActivity(MainActivity.this);
            }
        });
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


    class A implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            return null;
        }
    }

}
