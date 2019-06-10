package com.android.study.example;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String action = "issue_mifare", type="1", product_id="66666-00211", source_channel=null;
        String url = "https://tsmclient.miui.com?action=%1$s&type=%2$s&product_id=%3$s&source_channel=%4$s";
        System.out.println(String.format(url, action, type, product_id, source_channel));
    }
}