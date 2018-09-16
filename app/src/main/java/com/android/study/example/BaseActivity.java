package com.android.study.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/16 0016.
 */

public abstract class BaseActivity extends Activity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        afterCreate(savedInstanceState);
    }

    public abstract void afterCreate(Bundle savedInstanceState);

    public abstract int getLayoutId();
}
