package com.example.mymgstudyapp;

import android.text.TextUtils;

import com.example.mymgstudyapp.localnet.sdk.mgtv.utils.NetworkUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIp() {
        // ipaddr 192.168.3.93 gateway 192.168.3.1 netmask 255.255.255.0
//        int ip = 1560520896;
//        int netmask = 16777215;
//        int prefix = 8;
//
//        System.out.println(NetworkUtil.ipIntToString(ip));
//        int []array = NetworkUtil.convertIpToIntArray(NetworkUtil.ipIntToString(ip));
//        for (int i : array) {
//            System.out.print(i+" ");
//        }
//        System.out.println();
//
//        int netIp = ip & netmask;
//        System.out.println("netmask: "+NetworkUtil.ipIntToString(netmask));
//        System.out.println("netIp: "+NetworkUtil.ipIntToString(netIp));
//        System.out.println("netIp: "+NetworkUtil.ipIntToString(netIp+1));
//
//        long validData = Long.MAX_VALUE>>31;
//        int broadcast = (int) (netIp | ((2<<8)-1));
//        System.out.println("broadcast: "+NetworkUtil.ipIntToString(broadcast));

//        printBroadcastAddress("192.168.3.93", "255.255.255.0");
//        System.out.println("192.168.3.93 isValidIp : "+isValidIp("192.168.3.93"));
//        System.out.println("255.255.3.93 isValidIp : "+isValidIp("255.255.3.93"));
//        System.out.println("255.255.255.255 isValidIp : "+isValidIp("255.255.255.255"));
//        System.out.println("192.168.3 isValidIp : "+isValidIp("192.168.3"));

        long ip = -1062730915;
        System.out.println((ip-92)+": "+intToIp(ip-92));
        System.out.println(intToIp(ip-1));
        System.out.println(intToIp(ip));
        System.out.println(intToIp(ip+1));
        System.out.println(intToIp(ip+10));
        System.out.println(intToIp(ip+100));
        System.out.println(intToIp(-1));
        System.out.println(intToIp(0));

        assertEquals(4, 2 + 2);
    }

    private static boolean isValidIp(String ip) {
        // 定义正则表达式
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        return ip.matches(regex);
    }

    private static long ipToInt(String ip){
        String [] parts = ip.split("\\.");
        long result = 0;
        for(int i=0; i<4; i++){
            result |= Integer.parseInt(parts[i]) << (24-8*i);
        }
        return result;
    }

    private static String intToIp(long ipInt) {
        return String.format("%d.%d.%d.%d", (ipInt >> 24) & 0xff, (ipInt >> 16) & 0xff, (ipInt >> 8) & 0xff, ipInt & 0xff);
    }

    private static void printBroadcastAddress(String ip, String subnetMask){
        System.out.println("printBroadcastAddress: "+ip+""+subnetMask);

        long ipInt = ipToInt(ip);
        long subnetMaskInt = ipToInt(subnetMask);

        long netwrokAddressInt = ipInt & subnetMaskInt;
        String networkAddress = intToIp(netwrokAddressInt);

        long inverseSubnetMaskInt = ~subnetMaskInt;
        // 计算subnetMaskInt 二进制有多少个1
        long subnetMaskLength = Long.bitCount(subnetMaskInt);
        long broadcastAddressInt = netwrokAddressInt | inverseSubnetMaskInt;
        String broadcastAddress = intToIp(broadcastAddressInt);

        System.out.println("ipInt: "+ipInt);
        System.out.println("networkAddress: "+networkAddress);
        System.out.println("subnetMaskLength: "+subnetMaskLength);
        System.out.println("broadcastAddress: "+broadcastAddress);
    }
}