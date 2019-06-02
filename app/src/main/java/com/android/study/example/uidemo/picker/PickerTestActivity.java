package com.android.study.example.uidemo.picker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

public class PickerTestActivity extends AppCompatActivity {

    private NumberPicker mNumberPicker;
    private NumberPicker mNumberPicker2;
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
        String []dispalyValues1 = {"1-aaa", "2-bbbb","3-cc"};
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(dispalyValues.length-1);
        mNumberPicker.setDisplayedValues(dispalyValues);
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setValue(2);
        mNumberPicker.setSelectTextColor(Color.BLUE);
        mNumberPicker.setUnSelectTextColor(Color.RED);
        mNumberPicker.setSelectItemBgColor(Color.GRAY);
        mNumberPicker.setSelectTextSize(DisplayUtil.sp2px(this, 25));
        mNumberPicker.setUnSelectTextSize(DisplayUtil.sp2px(this, 15));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(0.5f);
        mNumberPicker.mLinePaint = paint;

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }

            @Override
            public void onLastValueChange(NumberPicker picker, int newVal) {
                Toast.makeText(PickerTestActivity.this, ""+newVal, Toast.LENGTH_SHORT).show();
            }
        });


        mNumberPicker2 = (NumberPicker) findViewById(R.id.number_picker_2);
        mNumberPicker2.setMinValue(0);
        mNumberPicker2.setMaxValue(dispalyValues.length-1);
        mNumberPicker2.setDisplayedValues(dispalyValues);
        mNumberPicker2.setWrapSelectorWheel(true);
        mNumberPicker2.setValue(2);

        mNumberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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
        mNativeNumberPicker.setMaxValue(dispalyValues1.length-1);
        mNativeNumberPicker.setDisplayedValues(dispalyValues1);
        mNativeNumberPicker.setWrapSelectorWheel(true);
        mNativeNumberPicker.setValue(2);

        mNumberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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
    }

}
