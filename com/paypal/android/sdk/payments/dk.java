package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class dk implements OnClickListener {
    private /* synthetic */ PaymentMethodActivity f851a;

    dk(PaymentMethodActivity paymentMethodActivity) {
        this.f851a = paymentMethodActivity;
    }

    public final void onClick(View view) {
        this.f851a.showDialog(1);
    }
}
