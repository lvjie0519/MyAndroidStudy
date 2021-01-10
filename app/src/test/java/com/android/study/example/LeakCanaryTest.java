package com.android.study.example;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class LeakCanaryTest {


    /**
     *
     */
    @Test
    public void testWeakReference(){
        ReferenceQueue referenceQueue = new ReferenceQueue();
        // 强引用
        Object obj = new Object();

        // 创建一个弱引用，并放到弱引用队列里面去
        WeakReference weakReference = new WeakReference(obj, referenceQueue);
        System.out.println("weakReference = "+weakReference);

//        obj = null;

        Runtime.getRuntime().gc();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Reference findRef = null;
        do {
            findRef = referenceQueue.poll();
            // 如果能找到上面的 weakReference, 说明 obj被gc回收了
            System.out.println("findRef = "+findRef +", 是否等于上面的weakReference = "+(findRef == weakReference));
        }while (findRef != null);

    }

}
