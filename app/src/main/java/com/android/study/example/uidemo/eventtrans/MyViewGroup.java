package com.android.study.example.uidemo.eventtrans;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 自定义View 如果集成ViewGroup  必须实现的几个方法
 * 1、构造方法
 * 2、onLayout
 */
public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
