package com.android.study.example.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class ShareUtil {

    private static final String TAG = "ShareUtil";

    /**
     * 获取分享内容
     * 需要等到被分享的也没打开后才能调用该方法
     *
     * @param intent
     * @return
     */
    public static String getShareContent(Intent intent) {
        if (intent == null) {
            return "intent can not null";
        }

        if (intent.getExtras() == null) {
            return "intent extras is null, not get share content";
        }

        return intent.getExtras().getString(Intent.EXTRA_TEXT);
    }

    /**
     * 将内容分享到另外一个包名的app，前提是另外一个应用可以接收分享
     *
     * @param activity    当前app activity
     * @param packageName 分享给另外一个app的包名, 如果包名为空，则会弹出当前已安装且支持接收分享的app列表
     * @param content     分享的内容
     */
    public static void shareContentToApp(Activity activity, String packageName, String content) {

        if (activity == null) {
            Log.e(TAG, "shareContentToApp, activity is null");
            return;
        }

        Intent textIntent = new Intent(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(packageName)) {
            textIntent.setPackage(packageName);
        }
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(textIntent, "分享"));
    }

}
