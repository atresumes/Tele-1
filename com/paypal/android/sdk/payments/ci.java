package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.br;
import com.paypal.android.sdk.bu;
import com.paypal.android.sdk.dg;
import java.util.GregorianCalendar;

final class ci {
    private final PayPalService f821a;
    private final String f822b = Integer.toString((new GregorianCalendar().getTimeZone().getRawOffset() / 1000) / 60);

    public ci(PayPalService payPalService) {
        this.f821a = payPalService;
    }

    public final dg m685a() {
        return this.f821a.m575c();
    }

    public final void m686a(bu buVar) {
        this.f821a.doTrackingRequest(buVar);
    }

    public final String m687b() {
        return this.f821a.m578f();
    }

    public final br m688c() {
        return this.f821a.m573b();
    }

    public final String m689d() {
        return this.f821a.m577e();
    }

    public final String m690e() {
        return this.f822b;
    }

    public final String m691f() {
        return this.f821a.m594v();
    }
}
