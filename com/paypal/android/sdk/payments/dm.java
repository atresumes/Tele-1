package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class dm implements OnClickListener {
    private /* synthetic */ PaymentMethodActivity f853a;

    dm(PaymentMethodActivity paymentMethodActivity) {
        this.f853a = paymentMethodActivity;
    }

    public final void onClick(View view) {
        this.f853a.showDialog(2);
    }
}
