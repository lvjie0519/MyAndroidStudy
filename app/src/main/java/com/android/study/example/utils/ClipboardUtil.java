package com.android.study.example.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtil {

    public static void writeDataToClipboard(Activity activity, String content){
        ClipboardManager cm=(ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("data", content));
    }

    public static String readDataFromClipboard(Activity activity){
        ClipboardManager cm=(ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd=cm.getPrimaryClip();
        String content=cd.getItemAt(0).getText().toString();
        return content;
    }

}
