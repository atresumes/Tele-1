package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class bl implements ServiceConnection {
    final /* synthetic */ PayPalFuturePaymentActivity f811a;

    bl(PayPalFuturePaymentActivity payPalFuturePaymentActivity) {
        this.f811a = payPalFuturePaymentActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(PayPalFuturePaymentActivity.f632a).append(".onServiceConnected");
        if (this.f811a.isFinishing()) {
            new StringBuilder().append(PayPalFuturePaymentActivity.f632a).append(".onServiceConnected exit - isFinishing");
            return;
        }
        this.f811a.f635d = ((cd) iBinder).f818a;
        if (this.f811a.f635d.m572a(new bm(this))) {
            PayPalFuturePaymentActivity.m502c(this.f811a);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f811a.f635d = null;
        PayPalFuturePaymentActivity.f632a;
    }
}
