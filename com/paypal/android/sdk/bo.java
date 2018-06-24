package com.paypal.android.sdk;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

final class bo implements Runnable {
    private /* synthetic */ Context f189a;
    private /* synthetic */ as f190b;

    bo(Context context, as asVar) {
        this.f189a = context;
        this.f190b = asVar;
    }

    public final void run() {
        try {
            Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.f189a);
            this.f190b.f59Y = advertisingIdInfo.getId();
        } catch (Throwable th) {
            bn.m142a(bn.f187c, th.getLocalizedMessage(), th);
        }
    }
}
