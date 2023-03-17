package com.example.mymgstudyapp.localnet.sdk;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WIFI管家实现原理：局域网设备扫描
 * https://blog.csdn.net/alijiahua/article/details/52444289
 *
 * 二：主要原理
 * 1.ping命令：
 * 使用ping命令轮询本网内可能存在的IP地址(192.168.1.1~192.168.1.255)。如果ping命令返回值正常，记录当前IP地址;
 *
 * 2.MAC地址：
 * 主要通过arp表查询IP所对应的MAC地址，具体可以参考
 * NetworkInterface. getHardwareAddress ()；
 *
 * 3.设备制作商：
 * 通过局域网搜索的设备是无法知道设备名的，不像计算机可以通过nbtstas命令得到计算机名称，所以只能通过mac地址查询对应厂商列表
 * 点击打开：MAC地址对应厂商
 */
public class ScanDeviceTool {
    private static final String TAG = ScanDeviceTool.class.getSimpleName();

    /**
     * 核心池大小
     **/
    private static final int CORE_POOL_SIZE = 1;
    /**
     * 线程池最大线程数
     **/
    private static final int MAX_IMUM_POOL_SIZE = 255;

    private String mDevAddress;// 本机IP地址-完整
    private String mLocAddress;// 局域网IP地址头,如：192.168.1.
    private Runtime mRun = Runtime.getRuntime();// 获取当前运行环境，来执行ping，相当于windows的cmd
    private Process mProcess = null;// 进程
    private String mPing = "ping -c 1 -w 3 ";// 其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private static List<String> mIpList = new ArrayList<String>();// ping成功的IP地址
    private ThreadPoolExecutor mExecutor;// 线程池对象
    private boolean mFlag = true;

    /**
     * TODO<扫描局域网内ip，找到对应服务器>
     *
     * @return void
     */
    public synchronized void scan(final TextView textView) {


        if (mFlag) {
            mIpList.clear();
            mFlag = false;
            mDevAddress = getLocAddress();// 获取本机IP地址
            mLocAddress = getLocAddrIndex(mDevAddress);// 获取本地ip前缀
            Log.d(TAG, "开始扫描设备,本机Ip为：" + mDevAddress);

            if (TextUtils.isEmpty(mLocAddress)) {
                Log.e(TAG, "扫描失败，请检查wifi网络");
                return;
            }

            /**
             * 1.核心池大小 2.线程池最大线程数 3.表示线程没有任务执行时最多保持多久时间会终止
             * 4.参数keepAliveTime的时间单位，有7种取值,当前为毫秒
             * 5.一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响
             * ，一般来说，这里的阻塞队列有以下几种选择：
             */
            mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE,
                    2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
                    CORE_POOL_SIZE));

            // 新建线程池
            for (int i = 1; i < 255; i++) {// 创建256个线程分别去ping
                final int lastAddress = i;// 存放ip最后一位地址 1-255

                Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String ping = ScanDeviceTool.this.mPing + mLocAddress
                                + lastAddress;
                        String currnetIp = mLocAddress + lastAddress;
                        /*if (mDevAddress.equals(currnetIp)) // 如果与本机IP地址相同,跳过
                            return;*/

                        try {
                            mProcess = mRun.exec(ping);

                            int result = mProcess.waitFor();
                            Log.d(TAG, "正在扫描的IP地址为：" + currnetIp + "返回值为：" + result);
                            if (result == 0) {

                                mIpList.add(currnetIp);

                                Log.d(TAG, "扫描成功,Ip地址为：" + mIpList.size() + "个设备:" + currnetIp + ":" + getHardwareAddress(currnetIp));
                                textView.setText("发现" + mIpList.size() + "个设备");

                            } else {
                                // 扫描失败
                                Log.d(TAG, "扫描失败");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "扫描异常" + e.toString());
                        } finally {
                            if (mProcess != null)
                                mProcess.destroy();
                        }
                    }
                };

                try {
                    mExecutor.execute(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mExecutor.shutdown();

            while (true) {
                try {
                    if (mExecutor.isTerminated()) {// 扫描结束,开始验证
                        Log.d(TAG, "扫描结束,总共成功扫描到" + mIpList.size() + "个设备.");
                        textView.setText("发现" + mIpList.size() + "个设备");
                        break;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * TODO<销毁正在执行的线程池>
     *
     * @return void
     */
    public void destory() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mFlag = true;
        }
    }

    public static List<String> getGetList() {
        return mIpList;
    }


    /**
     * TODO<获取本地ip地址>
     *
     * @return String
     */
    private String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {

                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("", "获取本地ip地址失败");
            e.printStackTrace();
        }

        Log.i(TAG, "本机IP:" + ipaddress);
        return ipaddress;
    }

    /**
     * TODO<获取本机IP前缀>
     *
     * @param devAddress // 本机IP地址
     * @return String
     */
    private String getLocAddrIndex(String devAddress) {
        if (!devAddress.equals("")) {
            return devAddress.substring(0, devAddress.lastIndexOf(".") + 1);
        }
        return null;
    }

    public static String getHardwareAddress(String ip) {
        String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
        int BUF = 8 * 1024;
        String hw = "00:00:00:00:00:00";
        try {
            if (ip != null) {
                String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
                Pattern pattern = Pattern.compile(ptrn);
                BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), BUF);
                String line;
                Matcher matcher;
                while ((line = bufferedReader.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        hw = matcher.group(1);
                        break;
                    }
                }
                bufferedReader.close();
            } else {
                Log.e(TAG, "ip is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't open/read file ARP: " + e.getMessage());
            return hw;
        }
        return hw;
    }


}
