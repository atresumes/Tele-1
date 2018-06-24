package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class cv implements OnClickListener {
    private /* synthetic */ PaymentConfirmActivity f831a;

    cv(PaymentConfirmActivity paymentConfirmActivity) {
        this.f831a = paymentConfirmActivity;
    }

    public final void onClick(View view) {
        this.f831a.onBackPressed();
    }
}
