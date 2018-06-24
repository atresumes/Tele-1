package com.paypal.android.sdk.payments;

import android.content.Intent;
import android.util.Log;

abstract class C0466z {
    Intent f920a;
    PayPalConfiguration f921b;

    C0466z(Intent intent, PayPalConfiguration payPalConfiguration) {
        this.f920a = intent;
        this.f921b = payPalConfiguration;
        if (!this.f920a.hasExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION)) {
            Log.w(mo2187a(), "Please add PayPalService.EXTRA_PAYPAL_CONFIGURATION to this activity for restart resiliency.");
        }
    }

    abstract String mo2187a();

    protected final void m750a(boolean z, String str) {
        if (!z) {
            Log.e(mo2187a(), str + " is invalid.  Please see the docs.");
        }
    }

    protected final boolean m751b() {
        if (this.f921b.m494o()) {
            return true;
        }
        Log.e(mo2187a(), "Service extra invalid.");
        return false;
    }

    abstract boolean mo2188c();
}
