package com.paypal.android.sdk.payments;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

final class bn implements ce {
    final /* synthetic */ PayPalFuturePaymentActivity f1052a;

    bn(PayPalFuturePaymentActivity payPalFuturePaymentActivity) {
        this.f1052a = payPalFuturePaymentActivity;
    }

    public final void mo2185a() {
        Date time = Calendar.getInstance().getTime();
        if (this.f1052a.f633b.compareTo(time) > 0) {
            long time2 = this.f1052a.f633b.getTime() - time.getTime();
            PayPalFuturePaymentActivity.f632a;
            new StringBuilder("Delaying ").append(time2).append(" milliseconds so user doesn't see flicker.");
            this.f1052a.f634c = new Timer();
            this.f1052a.f634c.schedule(new bo(this), time2);
            return;
        }
        this.f1052a.m500b();
    }

    public final void mo2186a(cf cfVar) {
        C0905d.m972a(this.f1052a, cfVar, 1, 2, 3);
    }
}
