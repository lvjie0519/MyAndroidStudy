package com.android.study.example.androidapi.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void writeLog(String str, final String logFilePath) {

        File file = FileUtils.createFileIfNotExists(logFilePath);
        if(file == null){
            Log.i("lvjie", "writeLog is error");
            return;
        }
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file,true));//true,则追加写入
            output.write(str);
            output.write("\r\n");//换行
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(output != null){
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File createFileIfNotExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return createFileIfNotExists(new File(filePath));
    }

    public static File createFileIfNotExists(File file) {


        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            //
        }

        return file;
    }

}
