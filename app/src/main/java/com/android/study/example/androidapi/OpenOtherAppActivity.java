package com.android.study.example.androidapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;

import java.util.List;

public class OpenOtherAppActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, OpenOtherAppActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_other_app);
    }

    /**
     * 通过 scheme 可以跳转到指定某个activity
     *
     * example:
     * <p>
     * <activity
     *  android:name=".outapp.OutCenterTransferActivity"
     *  android:launchMode="singleInstance">
     *  <intent-filter android:autoVerify="true">
     *      <action android:name="android.intent.action.VIEW" />
     *      <p>
     *          <category android:name="android.intent.category.DEFAULT" />
     *      <p>
     *      <data
     *          android:host="com.jack.demo"
     *          android:pathPattern=".*"
     *          android:scheme="https" />
     *  </intent-filter>
     * </activity>
     *
     * @param view
     */
    public void onClickTestScheme(View view) {
        //        String url = "git=133165651";
//                String url = "https://g.home.mi.com/otherPlatform?target=MiHomePlugin&action=ACTIVATE_NFC_FOR_LOCK&uid=xxxx";
//                String url = "mihome://plugin?pageName=feedBack&action=ACTIVATE_NFC_FOR_LOCK&uid=8941581051";
//                String url = "https://com.jack.demo/otherPlatform?target=MiHomePlugin&action=ACTIVATE_NFC_FOR_LOCK&uid=8941581051";
        String url = "sfapp.com.sangfor.testurlscheme://auth_helper";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if(activities.size()>0){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Log.i("lvjie", "cannot find this uri");
        }


        Log.i("lvjie", "app main activity task id is "+getTaskId());
    }

    /**
     * 通过 Exported 可以跳转到指定某个activity
     *
     * <activity
     *  android:name=".outapp.ExportedActivity"
     *  android:exported="true"
     * />
     *
     * @param view
     */
    public void onClickTestExported(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.android.example.myaidlclient", "com.android.example.myaidlclient.outapp.ExportedActivity");
        startActivity(intent);
    }

    /**
     * 通过 Action 可以跳转到指定某个activity
     * @param view
     */
    public void onClickTestAction(View view) {
        Intent intent = new Intent("com.android.study.action.action_activity");
        intent.setPackage("com.android.example.myaidlclient");
        startService(intent);
    }

    /**
     * 通过 PackageName 可以跳转到android.intent.action.MAIN
     * @param view
     */
    public void onClickTestPackageName(View view) {
        String packageName = "com.android.example.myaidlclient";
        Intent intent =getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }
}