package com.android.study.example.uidemo.picker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.study.example.R;

public class PickerTestActivity extends AppCompatActivity {

    private NumberPicker mNumberPicker;

    private android.widget.NumberPicker mNativeNumberPicker;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PickerTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_test);

        initView();
    }

    private void initView(){
        mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);

        String[] dispalyValues = {"1-aaa", "2-bbbb","3-cc","4-ddddddddd", "5-eee", "6-ffffffffffff", "7-ggg", "8-hhh", "9-iiiiiiiiiiiiiiiiiii","10-jjj"};
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(dispalyValues.length-1);
        mNumberPicker.setDisplayedValues(dispalyValues);
        mNumberPicker.setWrapSelectorWheel(true);
//        mNumberPicker.setLabel("最大值");
        mNumberPicker.setValue(2);

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("lvjie", "oldVal: "+oldVal+"   newVal: "+newVal+"   getScrollY="+picker.getScrollY());
            }

            @Override
            public void onLastValueChange(NumberPicker picker, int newVal) {
                Log.i("lvjie", "onLastValueChange-->newVal="+newVal);
                Toast.makeText(PickerTestActivity.this, ""+newVal, Toast.LENGTH_SHORT).show();
            }
        });


        mNativeNumberPicker = (android.widget.NumberPicker) findViewById(R.id.native_number_picker);
        mNativeNumberPicker.setMinValue(0);
        mNativeNumberPicker.setMaxValue(dispalyValues.length-1);
//        mNumberPicker.setDisplayedValues(dispalyValues);
        mNativeNumberPicker.setWrapSelectorWheel(true);
        mNativeNumberPicker.setValue(3);

        mNativeNumberPicker.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                Log.i("lvjie", "oldVal: "+oldVal+"   newVal: "+newVal+"   getScrollY="+picker.getScrollY());
            }
        });

        mNativeNumberPicker.setOnScrollListener(new android.widget.NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(android.widget.NumberPicker view, int scrollState) {
                Log.i("lvjie", "onScrollStateChange-->scrollState="+scrollState+"   getValue="+view.getValue());
            }
        });

    }

}
