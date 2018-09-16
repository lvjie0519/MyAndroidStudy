package com.android.study.example.butterknife;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/9/16 0016.
 */

public class MyButterKnife {

    public static void bind(Activity activity){
        Class c = activity.getClass();

        Field[] fields = c.getFields();

        for (Field field:fields) {
            MyBindView myBindView = field.getAnnotation(MyBindView.class);
            if(myBindView != null){
                try {
                    field.set(activity, activity.findViewById(myBindView.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
