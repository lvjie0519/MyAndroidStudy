package com.android.study.example.game.dadishu.stickfigure;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StickFigureUtils {
    private static final String TAG = "StickFigureUtils";

    public static float convertToFloat(String number) {
        return convertToFloat(number, 0);
    }

    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static ArrayList convertToPoints(String mssage) {
        List<String> myList = new ArrayList<String>(Arrays.asList(mssage.split(",")));
        Log.i(TAG, "myList SIZE:" + myList.size());
        ArrayList<Point> points = new ArrayList<>();
        for(int idx = 0; idx < (myList.size()/2); idx++) {
            points.add(new Point(convertToFloat(myList.get(idx*2)), convertToFloat(myList.get(idx*2 + 1))));
        }
        Log.i(TAG, "points SIZE:" + points.size());

        return points;
    }
}
