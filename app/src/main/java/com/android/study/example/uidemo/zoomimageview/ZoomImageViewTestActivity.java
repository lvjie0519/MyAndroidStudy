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

    private ZoomImageVIew zoomImageVIew;

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
        zoomImageVIew = findViewById(R.id.zoom_imageview);
    }

    public void onClickChoosePicFromPhoto(View view) {
        startActivityForResult(createPhotoChooseIntent(), 10002);
    }

    private Intent createPhotoChooseIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }
}