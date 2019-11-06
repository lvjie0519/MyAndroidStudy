package com.android.study.example.uidemo.inputdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.study.example.R;

public class InputDialog extends Dialog {


    private View mRootView;
    private int mScreenWidth;

    public InputDialog(@NonNull Context context,int screenWidth) {
        this(context, R.style.AlertDialog, screenWidth);
    }

    public InputDialog(@NonNull Context context, @StyleRes int themeResId, int screenWidth) {
        super(context, themeResId);

        this.mScreenWidth = screenWidth;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(){
        initDialog();
        initView();
    }

    private void initDialog(){
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);// 显示在底部
//        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        this.mRootView = getLayoutInflater().inflate(R.layout.dialog_input, null);
        this.setContentView(this.mRootView);

        // 设置触摸其他地方  弹窗消失
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = this.mScreenWidth; //设置宽度
        this.getWindow().setAttributes(lp);
    }

    private void initView(){
        EditText editText = (EditText) mRootView.findViewById(R.id.et_dialog_input);
        editText.requestFocus();
        editText.onCheckIsTextEditor();

    }


}
