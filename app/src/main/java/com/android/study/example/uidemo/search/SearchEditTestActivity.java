package com.android.study.example.uidemo.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchEditTestActivity extends AppCompatActivity {

    private static String TAG = "SearchEditTestActivity";
    private EditText etSearch;
    private RecyclerView rvSearchData;
    private RvSearchAdapter mAdapter;

    private List<String> mAllData ;
    private List<String> mSearchData;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SearchEditTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_edit_test);

        initData();
        initView();
    }

    private void initData(){
        mAllData = new ArrayList<>();
        mSearchData = new ArrayList<>();

        for(int i=1; i<99; i++){
            mAllData.add(""+i);
        }
    }

    private void initView(){
        this.etSearch = findViewById(R.id.et_search);

        this.rvSearchData = findViewById(R.id.rv_search_data);
        this.mAdapter = new RvSearchAdapter(this, mSearchData);
        this.rvSearchData.setLayoutManager(new GridLayoutManager(this, 2));
        this.rvSearchData.addItemDecoration(new MyItemDecoration(this, DisplayUtil.dip2px(this, 5)));
        this.rvSearchData.setAdapter(this.mAdapter);

        this.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "etSearch  onClick...");
                etSearch.setFocusable(true);
                etSearch.requestFocus();
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocusFromTouch();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etSearch, 0);
                }
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "etSearch  onTextChanged...");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "etSearch  afterTextChanged...");
                mSearchData.clear();
                String searchKey = s.toString();
                if(!TextUtils.isEmpty(searchKey)){
                    int len = mAllData.size();
                    for(int i=0; i<len; i++){
                        if(mAllData.get(i).startsWith(searchKey)){
                            mSearchData.add(mAllData.get(i));
                        }
                    }
                }


                mAdapter.setDatas(mSearchData);
                mAdapter.notifyDataSetChanged();
                return;
            }
        });


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setFocusable(false);
//                etSearch.requestFocus();
                etSearch.setFocusableInTouchMode(false);
//                etSearch.requestFocusFromTouch();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                }
            }
        });
    }



}
