package com.android.study.example.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

    // For more information, see https://issuetracker.google.com/issues/36911528
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private boolean mShouldFitSoftKeyboard = true;

    private static AndroidBug5497Workaround instance;

    public static AndroidBug5497Workaround getInstance(){
        if(instance == null){
            synchronized (AndroidBug5497Workaround.class){
                if(instance == null){
                    instance = new AndroidBug5497Workaround();
                }
            }
        }
        return instance;
    }

    public void assistActivity (Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(mShouldFitSoftKeyboard){
                    possiblyResizeChildOfContent();
                }
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private AndroidBug5497Workaround() {
    }

    private void possiblyResizeChildOfContent() {

        if(mChildOfContent == null || frameLayoutParams == null){
            return;
        }

        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public void setShouldFitSoftKeybord(boolean shouldFitSoftKeyboard) {
        this.mShouldFitSoftKeyboard = shouldFitSoftKeyboard;
    }

    public void cleanInstance(){
        mChildOfContent = null;
        frameLayoutParams = null;
        instance = null;
    }
}
