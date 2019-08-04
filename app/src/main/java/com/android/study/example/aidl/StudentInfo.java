package com.android.study.example.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentInfo implements Parcelable {

    private String userName;
    private int userAge;

    public StudentInfo() {
    }

    protected StudentInfo(Parcel in) {
        userName = in.readString();
        userAge = in.readInt();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public static final Creator<StudentInfo> CREATOR = new Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel in) {
            return new StudentInfo(in);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(userAge);
    }

    /**
     * 该方法不是Parcelable自动生成的，需要自己手动添加，
     * 如果不添加，则在使用AIDL时只支持 in 的定向tag
     * 如果添加了，则支持 in、out、inout
     *
     * @param dest 参数是一个Parcel,用它来存储与传输数据
     */
    public void readFromParcel(Parcel dest) {
        userName = dest.readString();
        userAge = dest.readInt();
    }

}
