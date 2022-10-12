package com.android.study.example.uidemo.photoview.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.study.example.R;
import com.android.study.example.uidemo.photoview.OnOutsidePhotoTapListener;
import com.android.study.example.uidemo.photoview.OnPhotoTapListener;
import com.android.study.example.uidemo.photoview.OnScaleChangedListener;
import com.android.study.example.uidemo.photoview.OnSingleFlingListener;
import com.android.study.example.uidemo.photoview.OnViewTapListener;
import com.android.study.example.uidemo.photoview.PhotoView;
import com.android.study.example.uidemo.photoview.PhotoViewUtil;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.io.InputStream;


public class PhotoViewTestActivity extends Activity {

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
        PhotoViewUtil.setStatusBarTranslucent(this);

        setContentView(R.layout.activity_photo_view_test);

        photoView = findViewById(R.id.photo_view);
//        photoView.setImageResource(R.drawable.person1);

//        photoView.setScaleType(ImageView.ScaleType.CENTER);
//        photoView.setImageResource(R.drawable.icon_sweeper);

        photoView.setImageBitmap(getAssertImageFile());
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addListener();
    }

    private void addListener(){
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
//                Log.i("lvjie", "onPhotoTap...x: "+x+"   y: "+y);
            }
        });

        photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
//                Log.i("lvjie", "onOutsidePhotoTap...");
            }
        });

        photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
//                Log.i("lvjie", "onViewTap...x: "+x+"   y: "+y);
            }
        });

        photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
//                Log.i("lvjie", "onScaleChange...scaleFactor: "+scaleFactor+"  focusX: "+focusX+"  focusY: "+focusY);
            }
        });

        photoView.setOnSingleFlingListener(new OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                Log.i("lvjie", "onFling...velocityX: "+velocityX+"  velocityY: "+velocityY);
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjielvjie", "onResume...");
        PhotoViewUtil.setStatusBarTranslucent(this);
    }

    public void onClickChangePic(View view) {
//        currentSelect ++;
//        int len = imgIds.length;
//        currentSelect = currentSelect % len;
//        photoView.setImageResource(imgIds[currentSelect]);

        finish();
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

    public void onClickBack(View view) {
        finish();
    }

    public void onClickStatusBarTest(View view) {
//        hideSystemBars(getWindow().getDecorView(), true, false);
//        PhotoViewUtil.setStatusBarTranslucent(this);
//        setStatusBarTranslucent(this);

        PhotoViewUtil.setStatusBarTranslucentStyle2(this);

//        FullScreenDialog dialog = new FullScreenDialog(this);
//        dialog.show();
    }
}