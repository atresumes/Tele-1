package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class dp implements OnClickListener {
    private /* synthetic */ PaymentMethodActivity f856a;

    dp(PaymentMethodActivity paymentMethodActivity) {
        this.f856a = paymentMethodActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f856a.f721i.m592t();
        this.f856a.m651c();
    }
}
