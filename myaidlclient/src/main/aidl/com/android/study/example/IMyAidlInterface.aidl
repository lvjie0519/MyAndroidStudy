// IMyAidlInterface.aidl
package com.android.study.example;

import  com.android.study.example.StudentInfo;

interface IMyAidlInterface {

    // 计算两个数的和
    int add(int num1, int num2);
    String getString();
    StudentInfo getStudentInfo();

}
