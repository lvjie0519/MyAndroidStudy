package com.example.mymgstudyapp.localnet.sdk.mgtv.utils;

import android.util.Log;

import com.example.mymgstudyapp.localnet.sdk.mgtv.DeviceInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ArpUtil {

    public static List<DeviceInfo> getLocalNetDeviceFromArp() {
        List<DeviceInfo> deviceInfos = getLocalNetDeviceFromReadArp();

        if (deviceInfos.isEmpty()) {
            deviceInfos = getLocalNetDeviceFromIpCmd();
        }

        return deviceInfos;
    }

    private static List<DeviceInfo> getLocalNetDeviceFromIpCmd() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        Set<String> ipSets = new HashSet<>();

        try {
            Process exec = Runtime.getRuntime().exec("ip neigh show");
            exec.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }

                if (!(readLine.contains("REACHABLE") || readLine.contains("DELAY") || readLine.contains("STALE"))) {
                    continue;
                }

                String[] split = readLine.split(" ");
                String ip = split[0];
                String mac = split[4].toUpperCase();

                if (!ipSets.contains(ip)) {
                    ipSets.add(ip);
                    deviceInfos.add(new DeviceInfo(ip, mac));
                }
            }
            bufferedReader.close();
            exec.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return deviceInfos;
    }

    private static List<DeviceInfo> getLocalNetDeviceFromReadArp() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        Set<String> ipSets = new HashSet<>();

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

                    if (!ipSets.contains(ip)) {
                        ipSets.add(ip);
                        deviceInfos.add(new DeviceInfo(ip, mac));
                    }
                } catch (Exception e) {
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceInfos;
    }


}
