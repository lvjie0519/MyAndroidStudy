package com.android.study.example.uidemo.inputdialog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.android.study.example.R;

public class InputDialogDemoActivity extends AppCompatActivity {

    private InputDialog mInputDialog;
    private CommonMsgDialog commonMsgDialog;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, InputDialogDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_dialog_demo);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE , WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showDialog(){
        if(mInputDialog == null){
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            mInputDialog = new InputDialog(this, display.getWidth());
        }

        mInputDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mInputDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        mInputDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // 此方法行不通
//        mInputDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        mInputDialog.show();



    }

    public void onBtnShowDialogClick(View view){
        showDialog();
    }

    public void onBtnShowCommonDialogClick(View view){
        showCommonMsgDialog();
    }

    private void showCommonMsgDialog(){
        if(commonMsgDialog == null){
            commonMsgDialog = new CommonMsgDialog();
        }
        commonMsgDialog.show(this.getSupportFragmentManager(), "commonMsgDialog");
    }

}
