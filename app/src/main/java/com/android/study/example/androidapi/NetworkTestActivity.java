package com.android.study.example.androidapi;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.androidapi.utils.DownloadManagerUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkTestActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, NetworkTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
    }

    public void onClickTestHttpUrlConnection(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                String fileUrl = "https://test.e-wework.com:8443/apps/attach/attachment/20210830/0d999a05fd498047a3b65a9c503b29c7/%E8%8A%AF%E5%8A%9E%E5%85%AC.apk";
//                String fileUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F18%2F08%2F24%2F05dbcc82c8d3bd356e57436be0922357.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1637395335&t=ed2acfdd183bc454f0b7e9698481499e";
                try {
                    url = new URL(fileUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("protocol", "http2");
//                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Accept-Encoding", "identity");
//                    urlConnection.setConnectTimeout(10000);
//                    urlConnection.connect();
                    int fileLen = urlConnection.getContentLength();
                    Log.i("lvjie", "文件大小 fileLen="+fileLen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
        }).start();

    }

    /**
     * https://www.jianshu.com/p/7ad92b3d9069
     * 系统下载器的使用
     * @param view
     */
    public void onClickTestSysDownloadManager(View view) {
//        String fileUrl = "https://test.e-wework.com:8443/apps/attach/attachment/20210830/0d999a05fd498047a3b65a9c503b29c7/%E8%8A%AF%E5%8A%9E%E5%85%AC.apk";
        String fileUrl = "http://mhhy.dl.gxpan.cn/apk/ml/MBGYD092101/Gardenscapes-ledou-MBGYD092101.apk";
        DownloadManagerUtil.download(this, fileUrl, "下载 Title", "下载 Description");
    }

    /**
     * Created by lenovo on 2018/6/17.
     * 下载成功广播类
     */
    private class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                installApk(context, id, PublicUtile.getApkDownPath(context) + PublicUtile.TestApkName);
            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //处理 如果还未完成下载，用户点击Notification ，跳转到下载中心
                Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(viewDownloadIntent);
            }else if(intent.getAction().equals(DownloadManager.ACTION_VIEW_DOWNLOADS)){

            }
        }

        /**
         * 启动安装
         *
         * @param context
         * @param downloadApkId
         * @param apkPath
         */
        private void installApk(Context context, long downloadApkId, String apkPath) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                Log.e("DownloadManager", downloadFileUri.toString());
                install.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Log.e("DownloadManager", "download error");
            }
        }
    }

}