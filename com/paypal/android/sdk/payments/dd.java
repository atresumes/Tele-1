package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class dd implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f841a;

    dd(PaymentConfirmActivity paymentConfirmActivity) {
        this.f841a = paymentConfirmActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f841a.m620a(true);
    }
}
