package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class cq implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f827a;

    cq(PaymentConfirmActivity paymentConfirmActivity) {
        this.f827a = paymentConfirmActivity;
    }

    public final void onClick(View view) {
        view.setEnabled(false);
        this.f827a.m636g();
    }
}
