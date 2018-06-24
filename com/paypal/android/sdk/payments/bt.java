package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class bt implements OnClickListener {
    private /* synthetic */ PayPalProfileSharingActivity f813a;

    bt(PayPalProfileSharingActivity payPalProfileSharingActivity) {
        this.f813a = payPalProfileSharingActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f813a.f665d.m567a(new bv(this.f813a), true);
    }
}
