package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class cj implements OnClickListener {
    private /* synthetic */ PaymentActivity f823a;

    cj(PaymentActivity paymentActivity) {
        this.f823a = paymentActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f823a.f693d.m567a(this.f823a.m603c(), true);
    }
}
