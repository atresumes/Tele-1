package com.paypal.android.sdk.payments;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

final class cm implements ce {
    final /* synthetic */ PaymentActivity f1058a;

    cm(PaymentActivity paymentActivity) {
        this.f1058a = paymentActivity;
    }

    public final void mo2185a() {
        Date time = Calendar.getInstance().getTime();
        if (this.f1058a.f692c.compareTo(time) > 0) {
            long time2 = this.f1058a.f692c.getTime() - time.getTime();
            PaymentActivity.f690a;
            new StringBuilder("Delaying ").append(time2).append(" miliseconds so user doesn't see flicker.");
            this.f1058a.f691b = new Timer();
            this.f1058a.f691b.schedule(new cn(this), time2);
            return;
        }
        this.f1058a.m602b();
    }

    public final void mo2186a(cf cfVar) {
        C0905d.m972a(this.f1058a, cfVar, 1, 2, 3);
    }
}
