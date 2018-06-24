package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class dl implements OnClickListener {
    private /* synthetic */ PaymentMethodActivity f852a;

    dl(PaymentMethodActivity paymentMethodActivity) {
        this.f852a = paymentMethodActivity;
    }

    public final void onClick(View view) {
        PaymentMethodActivity.m652c(this.f852a);
    }
}
