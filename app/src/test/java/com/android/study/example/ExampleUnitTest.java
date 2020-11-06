package com.android.study.example;

import android.text.TextUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before
    public void startUnitTest(){
        System.out.println("=========== 单元测试开始 ===========");
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String aa = "aa";
        String bb = "单元";
        System.out.println(aa.length()+"   "+bb.length());

        String text = ";;abcdefadf; youpin_sessionid=; cUserId=mW3IhL-xjTEjT-m-DZMU2k_wZfY; userId=1090807515; yetAnotherServiceToken=EeTLKxykf4HB%2FFSgK98Ra7sLmGvAECif1jAn%2FODcPSWU3bfMpDyiccQu3gEvCEtEhV8LmODBojtgOzN%2B3QiwF2AJd0BTo6EUQi92YnflW%2FGJYaWeolkSeUscYq0xEtXsumk2qUgFP01zkz4%2B8pAiFjlO%2FZHCSZYA3x6Fgc595oI%3D; serviceToken=CKKW=Jh=UbfYD9sYgP7TT2%2BjZvpN2kWESeTnR%2B3gJHCJKVUjN4p1AzjeZRFyY6xTR5QnVFE%2FpjuED%2BXGXqZ74lMld9twP76bnBBgh8l1vTUDJcHCA6vW3e61Vv9btLQaHoMglWBtR8q1iof7wbiIvgMTzslPK9oocd%2B4MbxrwrMMo%3D; locale=zh_CN; channel=Anzhi";
        System.out.println(getServiceToken(text));

        System.out.println((true&&true||false&&true));
    }

    private String getServiceToken(String cookie){

        String result = null;
        String keyValues[] = cookie.split(";");
        for (String keyValue: keyValues){
            keyValue = keyValue.trim();
            if(keyValue.startsWith("abc")){
                String []tempSplit = keyValue.split("=", 2);
                if(tempSplit.length == 2){
                    result = tempSplit[1];
                }
                break;
            }
        }

        return result;
    }

    @After
    public void endUnitTest(){
        System.out.println("=========== 单元测试结束 ===========");
    }
}