package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;

/**
 * 美团技术学习-Probe：Android 线上OOM 问题定位组件
 */
public class AndroidOOMActivity extends AppCompatActivity {

    private TextView tvShowInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AndroidOOMActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_oom);

        initView();
    }


    private void initView(){

        tvShowInfo = findViewById(R.id.tv_show_info);

        findViewById(R.id.btn_current_memory_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemoryInfo();
            }
        });

        findViewById(R.id.btn_thread_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemoryInfo();
            }
        });
    }

    /**
     * APP 内存暂用相关信息
     */
    private void showMemoryInfo(){
        /**
         * https://www.cnblogs.com/lyysz/p/5895291.html
         * maxMemory: 返回的是java虚拟机（这个进程）能构从操作系统那里挖到的最大的内存，以字节为单位
         * totalMemory:返回的是java虚拟机现在已经从操作系统那里挖过来的内存大小，也就是java虚拟机这个进程当时所占用的所有 内存。
         * 数据会根据app使用内存情况发生变化
         * freeMemory: 挖过来而又没有用上的内存，实际上就是 freeMemory()
         */
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();

        String info = "totalMemory="+totalMemory/1024.0f/1024 +
                "\n freeMemory="+freeMemory/1024.0f/1024 +
                "\n maxMemory="+maxMemory/1024.0f/1024+
                "\n costMemory="+(totalMemory-freeMemory)/1024.0f/1024;

        tvShowInfo.setText(info);
    }

}
