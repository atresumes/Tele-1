package com.paypal.android.sdk.payments;

final class cb implements ce {
    private /* synthetic */ PayPalService f1055a;

    cb(PayPalService payPalService) {
        this.f1055a = payPalService;
    }

    public final void mo2185a() {
        if (!this.f1055a.f674f.m489j() && this.f1055a.f671b != null) {
            this.f1055a.doDeleteTokenizedCreditCard(this.f1055a.m575c().f295b.m306c(), this.f1055a.f671b.m851e());
            this.f1055a.f671b = null;
            this.f1055a.m592t();
        }
    }

    public final void mo2186a(cf cfVar) {
    }
}
