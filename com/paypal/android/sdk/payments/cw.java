package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class cw implements ServiceConnection {
    final /* synthetic */ PaymentConfirmActivity f832a;

    cw(PaymentConfirmActivity paymentConfirmActivity) {
        this.f832a = paymentConfirmActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(PaymentConfirmActivity.f696a).append(".onServiceConnected");
        this.f832a.f706k = ((cd) iBinder).f818a;
        if (this.f832a.f706k.m572a(new cx(this))) {
            PaymentConfirmActivity.m624b(this.f832a);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f832a.f706k = null;
    }
}
