/*
 Copyright 2011, 2012 Chris Banes.
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.android.study.example.uidemo.photoview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Does a whole lot of gesture detecting.
 * 处理 处理 双指缩放、拖曳、惯性滑动
 * 双指缩放 使用ScaleGestureDetector 完成检测
 */
class CustomGestureDetector {

    private static final int INVALID_POINTER_ID = -1;

    private int mActivePointerId = INVALID_POINTER_ID;
    private int mActivePointerIndex = 0;
    private final ScaleGestureDetector mDetector;

    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;
    private float mLastTouchX;
    private float mLastTouchY;
    private final float mTouchSlop;
    private final float mMinimumVelocity;
    private OnGestureListener mListener;

    CustomGestureDetector(Context context, OnGestureListener listener) {
        final ViewConfiguration configuration = ViewConfiguration
                .get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        // 按根据设备密度（density）来获取的最小滑动距离
        mTouchSlop = configuration.getScaledTouchSlop();

        mListener = listener;
        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
            private float lastFocusX, lastFocusY = 0;

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                /**
                 * 定义了两个成员变量分别记录x轴和y轴的中心点，两次回调的中心点差值就是中心点移动的距离。
                 * scaleFactor 是缩放因子，相对于当前大小的缩放比例
                 */
                float scaleFactor = detector.getScaleFactor();

                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor))
                    return false;
             
                if (scaleFactor >= 0) {
                    mListener.onScale(scaleFactor,
                            detector.getFocusX(),
                            detector.getFocusY(),
                            detector.getFocusX() - lastFocusX,
                            detector.getFocusY() - lastFocusY
                    );
                    lastFocusX = detector.getFocusX();
                    lastFocusY = detector.getFocusY();
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                lastFocusX = detector.getFocusX();
                lastFocusY = detector.getFocusY();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                // NO-OP
            }
        };
        // 处理缩放, 缩放的检测使用了原生的 ScaleGestureDetector 来处理
        mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    private float getActiveX(MotionEvent ev) {
        try {
            return ev.getX(mActivePointerIndex);
        } catch (Exception e) {
            return ev.getX();
        }
    }

    private float getActiveY(MotionEvent ev) {
        try {
            return ev.getY(mActivePointerIndex);
        } catch (Exception e) {
            return ev.getY();
        }
    }

    public boolean isScaling() {
        return mDetector.isInProgress();
    }

    public boolean isDragging() {
        return mIsDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        try {
            mDetector.onTouchEvent(ev);
            return processTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            // Fix for support lib bug, happening when onDestroy is called
            return true;
        }
    }

    private boolean processTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 获取第一个手指的索引
                mActivePointerId = ev.getPointerId(0);

                // 初始化 VelocityTracker ，VelocityTracker 是一个速度检测类
                mVelocityTracker = VelocityTracker.obtain();
                if (null != mVelocityTracker) {
                    mVelocityTracker.addMovement(ev);
                }

                // 保存第一个手指触摸的位置
                mLastTouchX = getActiveX(ev);
                mLastTouchY = getActiveY(ev);
                mIsDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = getActiveX(ev);
                final float y = getActiveY(ev);
                final float dx = x - mLastTouchX, dy = y - mLastTouchY;

                if (!mIsDragging) {
                    // Use Pythagoras to see if drag length is larger than
                    // touch slop
                    // 移动距离超过mTouchSlop， 表示是拖动
                    mIsDragging = Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
                }

                if (mIsDragging) {
                    // 如果当前可以拖动，则会触发拖动回调，并且记录当前x、y坐标，给 VelocityTracker 添加事件
                    mListener.onDrag(dx, dy);
                    mLastTouchX = x;
                    mLastTouchY = y;

                    if (null != mVelocityTracker) {
                        mVelocityTracker.addMovement(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                // Recycle Velocity Tracker
                if (null != mVelocityTracker) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 处理松开手后的惯性滑动以及释放 VelocityTracker ，判断是否要惯性滑动，要看 x 轴和 y 轴的速度，
                 * VelocityTracker 在获取速度前要先调用 computeCurrentVelocity(int units) 计算速度，
                 * computeCurrentVelocity(int units) 方法的参数是单位，1表示1ms，1000表示1s，
                 * 这个值决定了下面 getXVelocity() 和 getYVelocity() 的单位，如果传入的是1000，那速度单位就是 px/s ，
                 * 只要 x 轴或者 y 轴有任何一个大于最小速度的，就会触发惯性滑动。这个最小速度跟上面的 TouchSlop 类似，也是从 ViewConfiguration 中获取的.
                 */
                mActivePointerId = INVALID_POINTER_ID;
                if (mIsDragging) {
                    if (null != mVelocityTracker) {
                        mLastTouchX = getActiveX(ev);
                        mLastTouchY = getActiveY(ev);

                        // Compute velocity within the last 1000ms
                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        final float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker
                                .getYVelocity();

                        // If the velocity is greater than minVelocity, call
                        // listener
                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity) {
                            // 惯性滑动回调
                            mListener.onFling(mLastTouchX, mLastTouchY, -vX,
                                    -vY);
                        }
                    }
                }

                // Recycle Velocity Tracker
                // 一次触摸流程接受，回收mVelocityTracker
                if (null != mVelocityTracker) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 多个手指长按时抬起其中一个手指，注意松开后还有手指在屏幕上
                final int pointerIndex = PhotoViewUtil.getPointerIndex(ev.getAction());
                final int pointerId = ev.getPointerId(pointerIndex);
                // 抬起的手指是第一个手指
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                }
                break;
        }

        mActivePointerIndex = ev
                .findPointerIndex(mActivePointerId != INVALID_POINTER_ID ? mActivePointerId
                        : 0);
        return true;
    }
}
