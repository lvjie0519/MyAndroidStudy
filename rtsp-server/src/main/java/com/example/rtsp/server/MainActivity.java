package com.example.rtsp.server;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addView();
    }

    public void onClickOpenRstpServerPage(View view) {
        RtspServerActivity.startActivity(this);
    }

    private void addView(){
        LinearLayout layout = findViewById(R.id.layout_warraper);


        View view1 = new View(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, 0.4f);
        view1.setLayoutParams(layoutParams1);
        view1.setBackgroundColor(Color.parseColor("#333333"));

        View view2 = new View(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, 0.6f);
        view2.setLayoutParams(layoutParams2);
        view2.setBackgroundColor(Color.parseColor("#999999"));

        layout.addView(view1);
        layout.addView(view2);
    }
}