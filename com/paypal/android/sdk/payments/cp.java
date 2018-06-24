package com.paypal.android.sdk.payments;

import android.content.Intent;

final class cp {
    private Intent f826a;

    cp(Intent intent) {
        this.f826a = intent;
    }

    final PayPalPayment m692a() {
        return (PayPalPayment) this.f826a.getParcelableExtra(PaymentActivity.EXTRA_PAYMENT);
    }

    public final dx m693b() {
        return (dx) this.f826a.getParcelableExtra("com.paypal.android.sdk.payments.PaymentConfirmActivity.EXTRA_PAYMENT_INFO");
    }
}
