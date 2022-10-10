package com.android.study.example.uidemo.photoview.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.study.example.R;
import com.android.study.example.uidemo.photoview.OnOutsidePhotoTapListener;
import com.android.study.example.uidemo.photoview.OnPhotoTapListener;
import com.android.study.example.uidemo.photoview.OnScaleChangedListener;
import com.android.study.example.uidemo.photoview.OnSingleFlingListener;
import com.android.study.example.uidemo.photoview.OnViewTapListener;
import com.android.study.example.uidemo.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;

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
//        photoView.setImageResource(R.drawable.person1);
        photoView.setImageBitmap(getAssertImageFile());
//        photoView.setScaleType(ImageView.ScaleType.CENTER);

        addListener();

        isImage();
    }

    private void addListener(){
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.i("lvjie", "onPhotoTap...x: "+x+"   y: "+y);
            }
        });

        photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                Log.i("lvjie", "onOutsidePhotoTap...");
            }
        });

        photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Log.i("lvjie", "onViewTap...x: "+x+"   y: "+y);
            }
        });

        photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                Log.i("lvjie", "onScaleChange...scaleFactor: "+scaleFactor+"  focusX: "+focusX+"  focusY: "+focusY);
            }
        });

        photoView.setOnSingleFlingListener(new OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("lvjie", "onFling...velocityX: "+velocityX+"  velocityY: "+velocityY);
                return false;
            }
        });


    }

    public void onClickChangePic(View view) {
        currentSelect ++;
        int len = imgIds.length;
        currentSelect = currentSelect % len;
        photoView.setImageResource(imgIds[currentSelect]);
    }

    /**
     * 从本地获取图片资源
     * @return
     */
    private Bitmap getAssertImageFile(){
        InputStream inputStream = null;
        try {
            inputStream = this.getAssets().open("AIDL原理.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    private boolean isImage(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        InputStream inputStream = null;
        InputStream inputStream2 = null;
        try {
            inputStream = this.getAssets().open("AIDL原理.png");
            inputStream2 = this.getAssets().open("version.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Rect rect = new Rect();
        BitmapFactory.decodeStream(inputStream, rect, options);
        Log.i("lvjielvjie", "outWidth: "+options.outWidth+"  outHeight: "+options.outHeight);

        BitmapFactory.decodeStream(inputStream2, rect, options);
        Log.i("lvjielvjie", "outWidth: "+options.outWidth+"  outHeight: "+options.outHeight);



        if (options.outWidth == -1) {

            return false;

        }

        return true;
    }
}