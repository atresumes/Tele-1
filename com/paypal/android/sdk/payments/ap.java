package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class ap implements ServiceConnection {
    final /* synthetic */ LoginActivity f776a;

    ap(LoginActivity loginActivity) {
        this.f776a = loginActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(LoginActivity.class.getSimpleName()).append(".onServiceConnected");
        this.f776a.f612r = ((cd) iBinder).f818a;
        if (this.f776a.f612r.m572a(new aq(this))) {
            this.f776a.m478a();
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f776a.f612r = null;
    }
}
