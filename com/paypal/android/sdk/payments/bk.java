package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class bk implements OnClickListener {
    private /* synthetic */ PayPalFuturePaymentActivity f810a;

    bk(PayPalFuturePaymentActivity payPalFuturePaymentActivity) {
        this.f810a = payPalFuturePaymentActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f810a.f635d.m567a(this.f810a.m501c(), true);
    }
}
