package com.android.study.example;

public class MyAlgorithmUtils {

    /**
     * 指定数据大小  数组排序
     * @param array
     * @return
     */
    public static int[] reduceArray(int[] array){
        int[] result = new int[500];
        for(int i=0; i<result.length; i++){
            result[i] = 0;
        }

        for(int i=0; i<array.length; i++){
            if (array[i] > 500 || array[i]<1) {
                continue;
            }
            result[array[i]-1] = array[i];
        }
        return result;
    }


}
