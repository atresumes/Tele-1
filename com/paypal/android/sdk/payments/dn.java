package com.paypal.android.sdk.payments;

import java.util.TimerTask;

final class dn extends TimerTask {
    private /* synthetic */ PaymentMethodActivity f854a;

    dn(PaymentMethodActivity paymentMethodActivity) {
        this.f854a = paymentMethodActivity;
    }

    public final void run() {
        this.f854a.removeDialog(3);
        PaymentConfirmActivity.m612a(this.f854a, 2, dh.PayPal, null, this.f854a.f721i.m576d());
    }
}
