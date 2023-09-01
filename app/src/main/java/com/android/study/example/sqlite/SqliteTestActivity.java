package com.android.study.example.sqlite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.study.example.R;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SqliteTestActivity extends AppCompatActivity {

    private EditText mEtTable;
    private EditText mEtKey;
    private EditText mEtValue;
    private TextView mTvShowInfo;

    private MySqliteHelper mSqliteHelper;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SqliteTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_test);

        initView();
        mSqliteHelper = new MySqliteHelper(this);
    }

    private void initView(){
        mEtTable = findViewById(R.id.et_table_name);
        mEtKey = findViewById(R.id.et_key);
        mEtValue = findViewById(R.id.et_value);
        mTvShowInfo = findViewById(R.id.tv_show_info);
    }

    public void onClickCreateTable(View view) {

    }

    public void onClickAddData(View view) {
        String tableName = mEtTable.getText().toString().trim();
        String key = mEtKey.getText().toString().trim();
        String value = mEtValue.getText().toString().trim();

        if(TextUtils.isEmpty(tableName)){
            showToast("表名不能为空");
            return;
        }

        if(TextUtils.isEmpty(key)){
            showToast("key不能为空");
            return;
        }

        mSqliteHelper.addDataToTable(tableName, key, value);
    }

    public void onClickGetData(View view) {
        String tableName = mEtTable.getText().toString().trim();
        String key = mEtKey.getText().toString().trim();

        if(TextUtils.isEmpty(tableName)){
            showToast("表名不能为空");
            return;
        }

        if(TextUtils.isEmpty(key)){
            showToast("key不能为空");
            return;
        }

        String value = mSqliteHelper.getDataFromTable(tableName, key);
        showMessageToView(value);
    }

    public void onClickGetAllKeys(View view) {
        String tableName = mEtTable.getText().toString().trim();

        if(TextUtils.isEmpty(tableName)){
            showToast("表名不能为空");
            return;
        }

        showMessageToView(mSqliteHelper.getAllKeysFromTable(tableName).toString());
    }

    public void onClickDeleteTable(View view) {
        String tableName = mEtTable.getText().toString().trim();

        if(TextUtils.isEmpty(tableName)){
            showToast("表名不能为空");
            return;
        }

        boolean success = mSqliteHelper.deleteTable(tableName);
        showToast(tableName+" delete "+ success);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showMessageToView(String message){
        mTvShowInfo.setText(message);
    }

    public void onClickOtherTest(View view) {
        int size = 100;
        for(int i=0; i<size; i++){
            String tableName = "table_"+i;
            String key = "key_"+i;
            String value = "value_"+i;

            mSqliteHelper.addDataToTable(tableName, key, value);
        }
    }

    private String tbName = "tbName";
    private String tbKey = "tbKey";
    private String encodeDada = "";
    public void onClickGenerateSeckey(View view) {
        String tbValue = mSqliteHelper.getDataFromTable(tbName, tbKey);

        if(TextUtils.isEmpty(tbValue)){
            tbValue = DesUtil.generateKey();
            mSqliteHelper.addDataToTable(tbName, tbKey, tbValue);
        }

        showToast(tbValue);
        Log.i("lvjie", "secretKey: "+tbValue);
    }

    public void onClickEncryptData(View view) {
        String originData = "410028110@qq.com";
        String secretKey = mSqliteHelper.getDataFromTable(tbName, tbKey);

        encodeDada = DesUtil.encode(secretKey, originData);
        Log.i("lvjie", "onClickEncryptData originData: "+originData+",  encodeDada: "+encodeDada);
    }

    public void onClickDecryptData(View view) {
        String secretKey = mSqliteHelper.getDataFromTable(tbName, tbKey);
        String originData = DesUtil.decode(secretKey, encodeDada);
        Log.i("lvjie", "onClickDecryptData originData: "+originData+",  encodeDada: "+encodeDada);
    }


}