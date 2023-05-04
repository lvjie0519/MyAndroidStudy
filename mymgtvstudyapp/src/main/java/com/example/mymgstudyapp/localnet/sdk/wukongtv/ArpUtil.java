package com.example.mymgstudyapp.localnet.sdk.wukongtv;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArpUtil {
    /* renamed from: a  reason: collision with root package name */
    public static final String f14818a = "00:00:00:00:00:00";

    /* renamed from: b  reason: collision with root package name */
    private static final String f14819b = "HardwareAddress";
    private static final String c = "select vendor from oui where mac=?";
    private static final String d = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
    private static final int e = 8192;

    public static String a(String str) {
        String str2 = f14818a;
        try {
            if (str != null) {
                Pattern compile = Pattern.compile(String.format(d, str.replace(".", "\\.")));
                BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), 8192);
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    Matcher matcher = compile.matcher(readLine);
                    if (matcher.matches()) {
                        str2 = matcher.group(1);
                        break;
                    }
                }
                bufferedReader.close();
            } else {
                Log.e(f14819b, "ip is null");
            }
            return str2;
        } catch (IOException e2) {
            Log.e(f14819b, "Can't open/read file ARP: " + e2.getMessage());
            return f14818a;
        }
    }

    public static void readArp() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("/proc/net/arp"));
            String line = "";
            String ip = "";
            String flag = "";
            String mac = "";

            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.length() < 63) continue;
                    if (line.toUpperCase(Locale.US).contains("IP")) continue;
                    ip = line.substring(0, 17).trim();
                    flag = line.substring(29, 32).trim();
                    mac = line.substring(41, 63).trim();
                    if (mac.contains("00:00:00:00:00:00")) continue;
                    Log.e("scanner", "readArp: mac= "+mac+" ; ip= "+ip+" ;flag= "+flag);


                } catch (Exception e) {
                }
            }
            br.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
