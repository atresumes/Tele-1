package com.sinch.android.rtc.internal.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityListener extends BroadcastReceiver {
    private ConnectivityListenerCallback callback;

    public IntentFilter createIntentFilter() {
        return new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    }

    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && !activeNetworkInfo.isFailover()) {
            this.callback.resendFailedRequests();
        }
    }

    public void setCallback(ConnectivityListenerCallback connectivityListenerCallback) {
        this.callback = connectivityListenerCallback;
    }
}
