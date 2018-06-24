package com.paypal.android.sdk.payments;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

final class ad {
    final IntentFilter f753a;
    final BroadcastReceiver f754b;
    boolean f755c;

    ad(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
        this.f753a = intentFilter;
        this.f754b = broadcastReceiver;
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("Receiver{");
        stringBuilder.append(this.f754b);
        stringBuilder.append(" filter=");
        stringBuilder.append(this.f753a);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
