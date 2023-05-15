package com.android.study.example.books;

import android.util.Log;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁的使用
 */
public class ObjectLockTest {
    private static final String TAG = "ObjectLockTest";
    private Object mLock = new Object();

    private ReentrantLock mReentrantLock = new ReentrantLock();

    public void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mLock){
                    Log.i(TAG, "test1 call start, will sleep 3s");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {

                    }
                    Log.i(TAG, "test1 call end, sleep 3s end");
                }
            }
        }).start();
    }

    public void test2(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (mLock){
            Log.i(TAG, "test2 call start, will sleep 3s");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "test2 call end, sleep 3s end");
        }
    }

    public void test3(){
        mReentrantLock.lock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "test1 call start, will sleep 3s");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mReentrantLock.unlock();
                }
                Log.i(TAG, "test1 call end, sleep 3s end");
            }
        }).start();
    }

    public void test4(){
        mReentrantLock.lock();
        Log.i(TAG, "test2 call start, will sleep 3s");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mReentrantLock.unlock();
        }
        Log.i(TAG, "test2 call end, sleep 3s end");
    }
}
