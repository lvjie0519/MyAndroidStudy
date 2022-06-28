package com.android.study.example.uidemo.inputdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.study.example.R;

public class CommonMsgDialog extends DialogFragment {

    private TextView tvShowInfo;
    private Button btnLeft;
    private Button btnRight;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (null != getActivity()) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_common_message, null);
            initView(view);
            builder.setView(view);
        }
        /**
         * false： 点击屏幕 不消失, 按返回按钮也不会消失
         */
        setCancelable(false);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawableResource(R.color.transparent);
        }
        /**
         * false： 点击屏幕 不消失, 但是按返回按钮 会消失
         */
//        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (null != window) {
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    private void initView(View rootView){
        this.tvShowInfo = rootView.findViewById(R.id.tv_show_info);
        this.btnLeft = rootView.findViewById(R.id.btn_dialog_left);
        this.btnRight = rootView.findViewById(R.id.btn_dialog_right);

        this.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                InputDialogDemoActivity.startActivity(v.getContext());
            }
        });

        this.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnClickListener{

    }
}
