package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class cz implements OnClickListener {
    private /* synthetic */ cy f836a;

    cz(cy cyVar) {
        this.f836a = cyVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f836a.f833a.m380a(i);
        PaymentConfirmActivity.m617a(this.f836a.f835c, this.f836a.f834b, i);
    }
}
