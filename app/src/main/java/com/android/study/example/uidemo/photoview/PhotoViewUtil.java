package com.android.study.example.uidemo.photoview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PhotoViewUtil {

    static void checkZoomLevels(float minZoom, float midZoom,
                                float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException(
                    "Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
        } else if (midZoom >= maxZoom) {
            throw new IllegalArgumentException(
                    "Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
        }
    }

    static boolean hasDrawable(ImageView imageView) {
        return imageView.getDrawable() != null;
    }

    static boolean isSupportedScaleType(final ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            return false;
        }
        switch (scaleType) {
            case MATRIX:
                throw new IllegalStateException("Matrix scale type is not supported");
        }
        return true;
    }

    static int getPointerIndex(int action) {
        return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

    /**
     * 图片尺寸是否大于屏幕尺寸
     * @param activity
     * @param bitmap
     * @return
     */
    public static boolean imageIsLargeScreen(Activity activity, Bitmap bitmap) {
        if(activity == null || bitmap == null){
            return false;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        // 图片宽高 都小于 屏幕的宽高， 则图片尺寸 小于 屏幕尺寸
        Log.i("lvjielvjie", "image width: " + bitmap.getWidth() + "  image height: " + bitmap.getHeight()
                + "  screen width: " + metrics.widthPixels + "  screen height:" + metrics.heightPixels);
        if (bitmap.getWidth() < metrics.widthPixels && bitmap.getHeight() < metrics.heightPixels) {
            return false;
        }

        return true;
    }

    /**
     * 状态栏和导航栏隐藏, 且界面直接延伸到状态栏和导航栏
     * https://www.jianshu.com/p/9f67aa9fa853
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        int FULLSCREEN_SYSTEM_UI_VISIBILITY = View.SYSTEM_UI_FLAG_FULLSCREEN        // 状态栏隐藏
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION                               // 导航栏隐藏
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE                                     // 避免某些用户交互造成系统自动清除全屏状态
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;                             // 状态栏和导航栏显示只显示一小段时间

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 刘海屏、水滴屏适配
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
            }

            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(FULLSCREEN_SYSTEM_UI_VISIBILITY);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 导航栏隐藏，状态栏不隐藏 且界面直接延伸到状态栏和导航栏
     * @param activity
     */
    public static void setStatusBarTranslucentStyle2(Activity activity) {
        int FULLSCREEN_SYSTEM_UI_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN        // 不会隐藏状态栏，但是页面布局会延伸到状态栏下面
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION                               // 导航栏隐藏
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE                                     // 避免某些用户交互造成系统自动清除全屏状态
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;                             // 状态栏和导航栏显示只显示一小段时间

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
            }

            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(FULLSCREEN_SYSTEM_UI_VISIBILITY);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 是否为图片文件
     * @param filePath
     * @return
     */
    public static boolean isPicFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }
}
