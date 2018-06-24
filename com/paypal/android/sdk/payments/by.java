package com.paypal.android.sdk.payments;

import android.content.Context;
import android.content.Intent;
import com.paypal.android.sdk.C0438a;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.di;
import com.paypal.android.sdk.dl;
import com.paypal.android.sdk.eh;
import com.paypal.android.sdk.em;

final class by implements Runnable {
    private /* synthetic */ Context f816a;

    by(Context context) {
        this.f816a = context;
    }

    public final void run() {
        PayPalService.f669c;
        Context context = this.f816a;
        C0905d c0905d = new C0905d();
        C0438a c0438a = new C0438a(context, "AndroidBasePrefs", new C0441d());
        eh.m329a(c0438a);
        em.m339a(c0438a);
        for (String str : new bz(this)) {
            dl dlVar = new dl(c0438a, str);
            dlVar.m290b();
            dlVar.m291c();
            di.m284b(str);
        }
        aa.m670a(this.f816a).m674a(new Intent("com.paypal.android.sdk.clearAllUserData"));
        PayPalService.f669c;
    }
}
