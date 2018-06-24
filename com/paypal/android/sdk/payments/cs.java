package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class cs implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f829a;

    cs(PaymentConfirmActivity paymentConfirmActivity) {
        this.f829a = paymentConfirmActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f829a.onBackPressed();
    }
}
