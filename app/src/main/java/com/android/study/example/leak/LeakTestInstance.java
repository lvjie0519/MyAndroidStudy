package com.android.study.example.leak;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 用来测试  匿名内部类是否持有外部类对象
 */
public class LeakTestInstance {

    // 直接保存匿名内部类对象, 这种方式存在内存泄漏
    private OnBtnClick mOnBtnClick;

    // 通过弱引用方式保存匿名内部类
    private WeakReference<OnBtnClick> mSoftReference;

    private static LeakTestInstance instance;

    private LeakTestInstance(){}

    public static LeakTestInstance getInstance(){
        if(instance == null){
            instance = new LeakTestInstance();
        }

        return instance;
    }

    public OnBtnClick getOnBtnClick() {
        return mOnBtnClick;
    }

    public void setOnBtnClick(OnBtnClick mOnBtnClick) {
        this.mOnBtnClick = mOnBtnClick;
    }

    public OnBtnClick getSoftReference() {
        return mSoftReference.get();
    }

    public void setSoftReference(OnBtnClick mOnBtnClick) {
        this.mSoftReference = new WeakReference<>(mOnBtnClick);
    }

    public void clean(){
        this.mOnBtnClick = null;
        if(this.mSoftReference != null){
            this.mSoftReference.clear();
        }
        this.mSoftReference = null;
    }
}
