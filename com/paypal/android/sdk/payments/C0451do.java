package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class C0451do implements OnClickListener {
    private /* synthetic */ PaymentMethodActivity f855a;

    C0451do(PaymentMethodActivity paymentMethodActivity) {
        this.f855a = paymentMethodActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f855a.f721i.m580h();
        this.f855a.f721i.m575c().m279a();
        this.f855a.m651c();
    }
}
