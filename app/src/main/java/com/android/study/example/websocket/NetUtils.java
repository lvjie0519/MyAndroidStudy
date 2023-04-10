package com.android.study.example.websocket;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetUtils {
    private final static String TAG = "NetUtils";
    public static final int INVALIED_PORT = -1;
    private static final int BEGIN_PORT = 8887;
    private static final int END_PORT = 65535;

    public static int getAvailablePort() {
        for (int port = BEGIN_PORT; port <= END_PORT; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        return INVALIED_PORT;
    }

    private static boolean isPortAvailable(int port) {
        Socket s = null;
        boolean retVal = false;
        try {
            s = new Socket();
            s.bind(new InetSocketAddress(port));
            retVal = true;
        } catch (IOException e) {
            Log.e(TAG, "isPortAvailable: socket bind port fail.");
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }
}
