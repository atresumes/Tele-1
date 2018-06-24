package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class db implements OnClickListener {
    private /* synthetic */ da f840a;

    db(da daVar) {
        this.f840a = daVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f840a.f837a.m425a(i);
        PaymentConfirmActivity.m625b(this.f840a.f839c, this.f840a.f838b, i);
    }
}
