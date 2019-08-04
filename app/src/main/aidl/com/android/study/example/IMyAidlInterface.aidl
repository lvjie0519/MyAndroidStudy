// IMyAidlInterface.aidl
package com.android.study.example;

// Declare any non-default types here with import statements

import  com.android.study.example.aidl.StudentInfo;

interface IMyAidlInterface {

    // 计算两个数的和
    int add(int num1, int num2);

    String getString();

    StudentInfo getStudentInfo();

}
