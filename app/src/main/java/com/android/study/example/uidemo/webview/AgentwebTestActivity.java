package com.android.study.example.uidemo.webview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.study.example.R;
import com.just.agentweb.AgentWeb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AgentwebTestActivity extends AppCompatActivity {

    private String mContent;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AgentwebTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agentweb_test);

        initData();
        initWebView();
    }


    private void initWebView(){

        String fileName = "webview-test.html";
        copyAssetAndWrite(fileName);

        AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.layout_webview), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .get()
//                .go(new File(getCacheDir(), fileName).getAbsolutePath())
                .getUrlLoader().loadData(mContent, "text/html", "charset=utf-8");
//                .go("http://www.jd.com");

    }


    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName){
        try {
            File cacheDir=getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return false;
                }
            }else {
                if (outFile.length()>10){//表示已经写入一次
                    return true;
                }
            }
            InputStream is=getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void initData(){
        long time = System.currentTimeMillis();
        Log.i("lvjie", "start read  "+time);
        mContent = getDataFromAssert();
        Log.i("lvjie", "end read  time is cost "+(System.currentTimeMillis()-time));
    }

    private String getDataFromAssert(){

        StringBuffer stringBuffer = new StringBuffer();

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("webview-test.html");
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            // 分行读取
            while ((line = buffreader.readLine()) != null) {
                stringBuffer.append(line);
            }
            buffreader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

}
