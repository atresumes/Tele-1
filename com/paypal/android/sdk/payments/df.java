package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class df implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f843a;

    df(PaymentConfirmActivity paymentConfirmActivity) {
        this.f843a = paymentConfirmActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f843a.onBackPressed();
    }
}
