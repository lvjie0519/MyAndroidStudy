package com.tools.netgel.netx;

import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity {
    public static class DiscoveryDevicesAsyncTask {
        static {
            System.loadLibrary("ipneigh");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void readIpNeigh(){
            try {
                ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
                ParcelFileDescriptor parcelFileDescriptor = createPipe[0];
                ParcelFileDescriptor parcelFileDescriptor2 = createPipe[1];
                ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
                if (nativeIPNeigh(parcelFileDescriptor2.detachFd()) != 0) {
//                b(newCachedThreadPool, bVar, null);
                    return;
                }

                List<Pair> arrayList = new ArrayList<>();

                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(autoCloseInputStream, StandardCharsets.UTF_8));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        Log.i("lvjie", "readLine: "+readLine);
                        if (readLine == null) {
                            break;
                        }
                        String[] split = readLine.split("\\s+");
                        if (split.length > 4) {
                            String str = split[0];
                            try {
                                InetAddress byName = InetAddress.getByName(str);
                                if (!byName.isLinkLocalAddress() && !byName.isLoopbackAddress()) {
                                    String str2 = split[4];
                                    String str3 = split[split.length - 1];
                                    if (!"FAILED".equals(str3) && !"INCOMPLETE".equals(str3)) {
                                        arrayList.add(new Pair(str, str2));
                                    }
                                }
                            } catch (UnknownHostException unused) {
//                            b(newCachedThreadPool, bVar, bufferedReader);
                                return;
                            }
                        }
                    } catch (IOException unused2) {
//                    b(newCachedThreadPool, bVar, bufferedReader);
                        return;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public native int nativeIPNeigh(int ip);
    }
}
