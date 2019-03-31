package com.kkaysheva.countrycodeexample.manager;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

public class TelephoneManager {

    private final TelephonyManager telephonyManager;

    public TelephoneManager(@NonNull Context context) {
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private String getCountryIsoByNetwork() {
        return telephonyManager.getNetworkCountryIso();
    }

    private String getCountryIsoBySim() {
        return telephonyManager.getSimCountryIso();
    }
}
