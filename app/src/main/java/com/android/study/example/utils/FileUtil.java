package com.android.study.example.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AndroidManifest.xml application需要配置如下内容：
 * android:preserveLegacyExternalStorage="true"
 * android:requestLegacyExternalStorage="true"
 * <p>
 * 还需要增加如下权限
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 文件sdcard 读写需要先获取读写权限
     *
     * @param activity
     * @param requestCode
     */
    public static void requestSDCardPermissions(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        List<String> permissionsList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "No WRITE_EXTERNAL_STORAGE permission");
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "No READ_EXTERNAL_STORAGE permission");
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionsList.isEmpty()) {
            return;
        }
        String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 向文件中写入内容
     *
     * @param fileAbsolutePath /sdcard/lvjie/test/lvjie_test.txt
     * @param content          aaa
     */
    public static void writeContentToFile(String fileAbsolutePath, String content) {

        if (TextUtils.isEmpty(fileAbsolutePath) || TextUtils.isEmpty(content)) {
            Log.e(TAG, "path or content is empty, not can write content to file.");
            return;
        }

        File file = new File(fileAbsolutePath);

        // 删除之前已经写好的文件
        if (file.exists()) {
            file.delete();
        }

        // 目录不存在，则创建目录
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 创建文件
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeFile(file, content);
    }

    /**
     * 读取文件内容并返回
     *
     * @param fileAbsolutePath /sdcard/lvjie/test/lvjie_test.txt
     * @return
     */
    public static String readContentFromFile(String fileAbsolutePath) {

        if (TextUtils.isEmpty(fileAbsolutePath)) {
            Log.e(TAG, "fileAbsolutePath is empty");
            return "fileAbsolutePath is empty";
        }

        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            String str = String.format("file:[%s] not exits", file.getAbsolutePath());
            Log.e(TAG, str);
            return str;
        }

        StringBuilder result = new StringBuilder();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private static void writeFile(File testFile, String content) {
        if (!testFile.exists()) {
            Log.i(TAG, String.format("file:[%s] not exits", testFile.getAbsolutePath()));
            return;
        }

        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }


}
