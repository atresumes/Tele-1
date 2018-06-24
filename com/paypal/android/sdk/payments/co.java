package com.paypal.android.sdk.payments;

import android.content.Intent;

final class co extends C0466z {
    co(Intent intent, PayPalConfiguration payPalConfiguration) {
        super(intent, payPalConfiguration);
    }

    protected final String mo2187a() {
        return PaymentActivity.class.getSimpleName();
    }

    final boolean mo2188c() {
        cp cpVar = new cp(this.f920a);
        boolean z = cpVar.m692a() != null && cpVar.m692a().isProcessable();
        m750a(z, "PaymentActivity.EXTRA_PAYMENT");
        return z;
    }
}
