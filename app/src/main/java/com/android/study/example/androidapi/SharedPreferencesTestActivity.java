package com.android.study.example.androidapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.study.example.R;

/**
 * 笔记：
 * 1、通过getSharedPreferences("data-test",Context.MODE_PRIVATE);  背后根据name不同，对应着不同的文件；
 * 2、首次调用如上方法，都会将文件内容保存到内存中，同时也会保存File对象，用来操作文件，下次获取直接从内存中获取；
 * 3、editor.commit() 是同步更新内存数据并更新到本地文件，都是全量更新到本地文件；
 * 4、editor.apply() 是同步更新到内存，异步更新到本地文件，使用了handler，延迟发送消息更新文件，延迟时间默认是100ms；
 * 5、写入文件时，会有一个备份文件，防止在写入文件时出现异常无法恢复原有的数据；
 * 6、SharedPreferences 初始化从本地读取文件时，读取的数据量有大小限制，默认值为16*1024字节；
 *
 * 腾讯MMKV库
 * MMKV 分别代表的是 Memory Mapping Key Value，是基于 mmap 内存映射的 key-value 组件，
 * 底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强；
 *
 * MMKV 优势
 * 1、数据格式及更新范围优化
 * SharedPreferences 采用 xml 数据存储，每次读写操作都会全局更新；MMKV 采用 protobuf 数据存储，更紧密，支持局部更新
 * 2、文件耗时操作优化
 * MMKV 采用 MMap 内存映射的方式取代 I/O 操作，使用 一次拷贝技术提高更新速度；
 *
 * Memory Mapping 内存映射
 * 一种将磁盘上文件的一部分或整个文件映射到应用程序地址空间的一系列地址机制，从而应用程序可以用访问内存的方式访问磁盘文件， bundle也是采用这种技术；
 * 相较于 I/O 对文件的读写操作只需要从磁盘到用户主存的一次数据拷贝过程，减少了数据的拷贝次数，提高了文件的操作效率；
 * 普通的文件的读写过程是： 磁盘-->内核空间-->用户空间  ，  至少两次拷贝
 *
 * Protocol Buffers 编码结构
 * 是 Google 出品的一种可扩展的序列化数据的编码格式，protobuf 在更新文件时，虽然也不方便局部更新，
 * 但是可以做增量更新，即不管之前是否有相同的 key，一旦有新的数据便添加到文件最后，待最终文件读取时，后面新的数据会覆盖之前老旧的数据；
 *
 */
public class SharedPreferencesTestActivity extends Activity {

    private TextView mTvShowInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SharedPreferencesTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences_test);

        mTvShowInfo = findViewById(R.id.tv_show_info);
    }

    public void onClickWriteData(View view) {

        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences= getSharedPreferences("data-test",Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString("name", "jack");
        editor.putInt("age", 28);
        //步骤4：提交
        editor.commit();

    }

    public void onClickReadData(View view) {
        SharedPreferences sharedPreferences= getSharedPreferences("data-test", Context .MODE_PRIVATE);
        String name=sharedPreferences.getString("name","");
        int age=sharedPreferences.getInt("age",0);
        this.mTvShowInfo.setText("name="+name+",  age="+age);
    }

    public void onClickDeleteData(View view) {
    }
}
