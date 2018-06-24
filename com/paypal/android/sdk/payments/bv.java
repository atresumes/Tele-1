package com.paypal.android.sdk.payments;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

final class bv implements ce {
    final /* synthetic */ PayPalProfileSharingActivity f1053a;

    bv(PayPalProfileSharingActivity payPalProfileSharingActivity) {
        this.f1053a = payPalProfileSharingActivity;
    }

    public final void mo2185a() {
        Date time = Calendar.getInstance().getTime();
        if (this.f1053a.f663b.compareTo(time) > 0) {
            long time2 = this.f1053a.f663b.getTime() - time.getTime();
            PayPalProfileSharingActivity.f662a;
            new StringBuilder("Delaying ").append(time2).append(" miliseconds so user doesn't see flicker.");
            this.f1053a.f664c = new Timer();
            this.f1053a.f664c.schedule(new bw(this), time2);
            return;
        }
        ProfileSharingConsentActivity.m909a(this.f1053a, 1, this.f1053a.f665d.m576d());
    }

    public final void mo2186a(cf cfVar) {
        C0905d.m972a(this.f1053a, cfVar, 1, 2, 3);
    }
}
