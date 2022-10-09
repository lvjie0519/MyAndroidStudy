package com.android.study.example.uidemo.zoomimageview;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.uidemo.zoomimageview.views.ZoomImageVIew;

public class ZoomImageViewTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ZoomImageViewTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_view_test);

        initView();
    }

    private void initView(){
//        PhotoView
    }
}