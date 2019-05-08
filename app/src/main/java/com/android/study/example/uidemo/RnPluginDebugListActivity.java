package com.android.study.example.uidemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.study.example.R;
import com.android.study.example.butterknife.ButterKnifeTestActivity;

public class RnPluginDebugListActivity extends AppCompatActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RnPluginDebugListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rn_plugin_debug_list);
    }
}
