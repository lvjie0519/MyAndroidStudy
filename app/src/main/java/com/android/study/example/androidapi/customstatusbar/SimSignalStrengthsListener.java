package com.android.study.example.androidapi.customstatusbar;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class SimSignalStrengthsListener extends PhoneStateListener {
    public static final int SIM_ID_NONE = -1;

    private int mSimId;
    private SignalStrengthsChangedListener mListener;

    public SimSignalStrengthsListener(int simId) {
        super();
        this.mSimId = simId;

        if(simId != SIM_ID_NONE){
            ReflectionUtil.setFieldValue(this, "mSubId", simId);
        }
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        if (this.mListener != null) {
            this.mListener.onSignalStrengthsChanged(this.mSimId, signalStrength);
        }
    }

    public void setListener(SignalStrengthsChangedListener listener) {
        this.mListener = listener;
    }

    public interface SignalStrengthsChangedListener {
        void onSignalStrengthsChanged(int simId, SignalStrength signalStrength);
    }
}
