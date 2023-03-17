package com.example.mymgstudyapp.localnet.sdk.wukongtv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PingUtil {


    public static String pingIp(String str) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec("ping -c 2 -w 100 " + str);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append("\n");
                } else {
                    int waitFor = exec.waitFor();
                    return "Status is " + waitFor + "\n" + sb.toString();
                }
            }
        } catch (IOException e) {
            return e.toString();
        } catch (InterruptedException e2) {
            return e2.toString();
        }
    }


}
