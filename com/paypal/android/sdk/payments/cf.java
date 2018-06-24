package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.bz;

final class cf {
    Integer f819a;
    String f820b;

    cf(PayPalService payPalService, String str, Integer num, String str2) {
        this.f820b = str;
        this.f819a = num;
    }

    final boolean m681a() {
        return this.f819a != null && this.f819a.equals(Integer.valueOf(401));
    }

    final boolean m682b() {
        return m681a() && this.f820b.equals("2fa_required");
    }

    final boolean m683c() {
        return this.f820b.equals(bz.SERVER_COMMUNICATION_ERROR.toString());
    }
}
