package com.paypal.android.sdk.payments;

import android.view.View;
import android.view.View.OnClickListener;

final class C0464x implements OnClickListener {
    private /* synthetic */ C0459m f899a;

    C0464x(C0459m c0459m) {
        this.f899a = c0459m;
    }

    public final void onClick(View view) {
        view.setEnabled(false);
        C0459m.m742f(this.f899a);
    }
}
