package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class C0462u implements ServiceConnection {
    final /* synthetic */ C0459m f897a;

    C0462u(C0459m c0459m) {
        this.f897a = c0459m;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(C0459m.f884d).append(".onServiceConnected");
        this.f897a.f886a = ((cd) iBinder).f818a;
        if (this.f897a.f886a.m572a(new C0912v(this))) {
            C0459m.m738d(this.f897a);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f897a.f886a = null;
    }
}
