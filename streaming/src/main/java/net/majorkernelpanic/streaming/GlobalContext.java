package net.majorkernelpanic.streaming;

import android.content.Context;
import android.os.Handler;

public class GlobalContext {

    private static GlobalContext sInstance;

    private Context mAppContext;

    private GlobalContext() {
    }

    public static GlobalContext getInstance() {
        if (sInstance == null) {
            synchronized (GlobalContext.class) {
                if (sInstance == null) {
                    sInstance = new GlobalContext();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context) {
        Context appContext = context.getApplicationContext();
        mAppContext = appContext == null ? context : appContext;
    }

    public Context getAppContext() {
        return mAppContext;
    }

}
