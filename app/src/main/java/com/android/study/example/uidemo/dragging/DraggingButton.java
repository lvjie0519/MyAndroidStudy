package com.android.study.example.uidemo.dragging;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

public class DraggingButton extends android.support.v7.widget.AppCompatButton {

    private int lastX = 0;
    private int lastY = 0;
    private int beginX = 0;
    private int beginY = 0;

    private  int screenWidth = 720;
    private  int screenHeight = 1280;


    public DraggingButton(Context context) {
        this(context, null);
    }

    public DraggingButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData(context);
    }

    private void initData(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Log.i("lvjie", "screenWidth="+screenWidth+"   screenHeight="+screenHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();      // 触摸点与屏幕左边的距离
                lastY = (int) event.getRawY();      // 触摸点与屏幕上边的距离
                beginX = lastX;
                beginY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx =(int)event.getRawX() - lastX;       // x轴拖动的绝对距离
                int dy =(int)event.getRawY() - lastY;       // y轴拖动的绝对距离

                // getLeft(): 子View的左边界到父View的左边界的距离, getRight():子View的右边界到父View的左边界的距离
                // 如下几个数据表示view应该在布局中的位置
                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if(left < 0){
                    left = 0;
                    right = left + getWidth();
                }
                if(right > screenWidth){
                    right = screenWidth;
                    left = right - getWidth();
                }
                if(top < 0){
                    top = 0;
                    bottom = top + getHeight();
                }
                if(event.getRawY()+getHeight()>screenHeight){
                    bottom = screenHeight-getHeight();
                    top = bottom - getHeight();
                }
                Log.i("lvjie", "  bottom="+bottom+"  event.getRawY()="+event.getRawY()+"  screenHeight="+screenHeight);
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(lastX - beginX) < 10 && Math.abs(lastY - beginY) < 10)
                    return super.onTouchEvent(event);
                else
                    return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


}
