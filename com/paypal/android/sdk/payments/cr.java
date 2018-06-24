package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class cr implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f828a;

    cr(PaymentConfirmActivity paymentConfirmActivity) {
        this.f828a = paymentConfirmActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f828a.m640i();
    }
}
