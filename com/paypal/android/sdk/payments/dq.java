package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class dq implements ServiceConnection {
    final /* synthetic */ PaymentMethodActivity f857a;

    dq(PaymentMethodActivity paymentMethodActivity) {
        this.f857a = paymentMethodActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(PaymentMethodActivity.f713a).append(".onServiceConnected");
        if (this.f857a.isFinishing()) {
            new StringBuilder().append(PaymentMethodActivity.f713a).append(".onServiceConnected exit - isFinishing");
            return;
        }
        this.f857a.f721i = ((cd) iBinder).f818a;
        if (this.f857a.f721i.m572a(new dr(this))) {
            PaymentMethodActivity.m659i(this.f857a);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f857a.f721i = null;
    }
}
