package com.android.study.example.uidemo.photoview.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.uidemo.photoview.PhotoView;

public class PhotoViewTestActivity extends AppCompatActivity {

    private PhotoView photoView;
    private int []imgIds = {
            R.drawable.person1, R.drawable.yao_qin_han, R.drawable.ic_scan_ing, R.drawable.icon_sweeper, R.drawable.ic_default_nfc_lock
    };
    private int currentSelect = 0;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PhotoViewTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_test);

        photoView = findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.person1);
    }

    public void onClickChangePic(View view) {
        currentSelect ++;
        int len = imgIds.length;
        currentSelect = currentSelect % len;
        photoView.setImageResource(imgIds[currentSelect]);
    }
}