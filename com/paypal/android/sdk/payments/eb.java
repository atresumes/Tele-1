package com.paypal.android.sdk.payments;

import android.text.style.URLSpan;
import android.view.View;

final class eb extends URLSpan {
    private C0450c f874a;

    eb(URLSpan uRLSpan, C0450c c0450c) {
        super(uRLSpan.getURL());
        this.f874a = c0450c;
    }

    public final void onClick(View view) {
        this.f874a.mo2215a();
        super.onClick(view);
    }
}
