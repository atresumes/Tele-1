package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.fu;

final class ct implements ce {
    private /* synthetic */ PaymentConfirmActivity f1059a;

    ct(PaymentConfirmActivity paymentConfirmActivity) {
        this.f1059a = paymentConfirmActivity;
    }

    public final void mo2185a() {
        PaymentConfirmActivity.f696a;
        this.f1059a.m636g();
    }

    public final void mo2186a(cf cfVar) {
        this.f1059a.m641j();
        C0905d.m973a(this.f1059a, fu.m370a(cfVar.f820b), 1);
        if (this.f1059a.f704i != dh.PayPal) {
            this.f1059a.f702g.m408b(true);
        }
    }
}
