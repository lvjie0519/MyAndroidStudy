package com.android.study.example.books.jjzg.chapter2;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.study.example.R;
import com.android.study.example.utils.ToastUtil;

public class Chapter2TestActivity extends AppCompatActivity {

    private View snackbarContent;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, Chapter2TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charpter2_test);

        initView();
    }

    private void initView(){
        snackbarContent = findViewById(R.id.layout_snackbar_content);
    }

    /*****************   onClick begin *********************/
    public void snackbarOnClick(View view){
        showSnackbar();
    }

    /*****************   onClick end *********************/



    private void showSnackbar(){
        Snackbar.make(snackbarContent, "标题", Snackbar.LENGTH_LONG)
                .setAction("点击事件", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(Chapter2TestActivity.this, "点击 Snackbar");
                    }
                }).setDuration(Snackbar.LENGTH_LONG)
                .show();
    }
}
