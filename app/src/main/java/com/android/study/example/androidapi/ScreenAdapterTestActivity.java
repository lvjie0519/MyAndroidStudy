package com.android.study.example.androidapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

/**
 * 屏幕适配 和 及巨无霸文字适配等
 */
public class ScreenAdapterTestActivity extends Activity {


    public static void startActivity(Context context){
        Intent intent = new Intent(context, ScreenAdapterTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_adapter_test);

        Log.i("lvjie", "onCreate...");
    }


    public void onClickAlterFontSize(View view){
        Resources resources = this.getResources();
        if (resources != null) {
            android.content.res.Configuration configuration = resources.getConfiguration();
//            configuration.fontScale = fontScale;
            Log.i("lvjie", "fontScale="+configuration.fontScale+"  \n"+configuration.toString());
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            this.recreate();
        }
    }

//    public static void setAppFontSize(float fontScale) {
//        if (mainApplication != null) {
//            List<Activity> activityList = mainApplication.activityList;
//            if (activityList != null) {
//                for (Activity activity : activityList) {
//                    if (activity instanceof SettingActivity) {
//                        continue;
//                    }
//                    Resources resources = activity.getResources();
//                    if (resources != null) {
//                        android.content.res.Configuration configuration = resources.getConfiguration();
//                        configuration.fontScale = fontScale;
//                        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//                        activity.recreate();
//                        if (fontScale != mainApplication.fontScale) {
//                            mainApplication.fontScale = fontScale;
//                            mainApplication.preferences.edit().putFloat("fontScale", fontScale).apply();
//                        }
//                    }
//                }
//            }
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("lvjie", "onRestart...");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lvjie", "onSaveInstanceState...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lvjie", "onResume...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lvjie", "onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lvjie", "onStop...");
    }
}
