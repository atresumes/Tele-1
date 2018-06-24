package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class af implements OnClickListener {
    private /* synthetic */ FuturePaymentInfoActivity f757a;

    af(FuturePaymentInfoActivity futurePaymentInfoActivity) {
        this.f757a = futurePaymentInfoActivity;
    }

    public final void onClick(View view) {
        this.f757a.finish();
    }
}
