package com.android.study.example.game.dadishu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.game.dadishu.stickfigure.StickFigureUtils;
import com.android.study.example.game.dadishu.stickfigure.StickFigureView;

public class DaDiShuActivity extends Activity {
    private static final String TAG = "DaDiShuActivity";

    private WhackMoleView mWhackMoleView;
    private StickFigureView mStickFigureView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DaDiShuActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_di_shu);

        initView();
    }

    private void initView(){
        mWhackMoleView = findViewById(R.id.whackMoleView);
        mStickFigureView = findViewById(R.id.stickFigureView);
    }

    public void btnOnClickMouseOut(View view) {
        Log.i(TAG, "btnOnClickMouseOut");
//        mWhackMoleView.startBulletViewAnimatorOut();

        String data = "[0.511876702,0.642032683,0.496820658,0.632919192,0.504869699,0.634242237,0.440754563,0.632665634,0.499058425,0.635958076,0.405162543,0.682202816,0.529333353,0.676811576,0.398598284,0.762453973,0.612915277,0.719937682,0.470043123,0.745417297,0.572672665,0.729987085,0.44564113,0.833958566,0.525378168,0.823441625,0.639304578,0.818477392,0.634486556,0.821589589,0.617596447,0.927929819,0.60576874,0.93783319,-0.0857459828,4.86532872e-06,0.566003203,0.734659493,-0.0857643709,-4.99999999e-07,-0.0857643709,-4.99999999e-07,-0.0857643709,-4.99999999e-07,-0.0857643709,-4.99999999e-07]";
        mStickFigureView.updateCoordinates(StickFigureUtils.convertToPoints(data));
    }

    public void btnOnClickBeatMouse(View view) {
        Log.i(TAG, "btnOnClickBeatMouse");
//        mWhackMoleView.startBulletViewAnimatorIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWhackMoleView.destory();
    }
}