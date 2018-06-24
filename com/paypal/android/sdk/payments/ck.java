package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class ck implements ServiceConnection {
    final /* synthetic */ PaymentActivity f824a;

    ck(PaymentActivity paymentActivity) {
        this.f824a = paymentActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(PaymentActivity.f690a).append(".onServiceConnected");
        if (this.f824a.isFinishing()) {
            new StringBuilder().append(PaymentActivity.f690a).append(".onServiceConnected exit - isFinishing");
            return;
        }
        this.f824a.f693d = ((cd) iBinder).f818a;
        if (this.f824a.f693d.m572a(new cl(this))) {
            PaymentActivity.m604c(this.f824a);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f824a.f693d = null;
        PaymentActivity.f690a;
    }
}
