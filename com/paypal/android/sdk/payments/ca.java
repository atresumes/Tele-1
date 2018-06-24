package com.paypal.android.sdk.payments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

final class ca extends BroadcastReceiver {
    private /* synthetic */ PayPalService f817a;

    ca(PayPalService payPalService) {
        this.f817a = payPalService;
    }

    public final void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.paypal.android.sdk.clearAllUserData")) {
            this.f817a.m579g();
            Log.w("paypal.sdk", "active service user data cleared");
        }
    }
}
