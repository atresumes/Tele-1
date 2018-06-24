package com.paypal.android.sdk.payments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import java.util.Calendar;

final class bu implements ServiceConnection {
    private /* synthetic */ PayPalProfileSharingActivity f814a;

    bu(PayPalProfileSharingActivity payPalProfileSharingActivity) {
        this.f814a = payPalProfileSharingActivity;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        new StringBuilder().append(PayPalProfileSharingActivity.class.getSimpleName()).append(".onServiceConnected");
        if (this.f814a.isFinishing()) {
            new StringBuilder().append(PayPalProfileSharingActivity.class.getSimpleName()).append(".onServiceConnected exit - isFinishing");
            return;
        }
        this.f814a.f665d = ((cd) iBinder).f818a;
        if (this.f814a.f665d.m576d() == null) {
            Log.e(PayPalProfileSharingActivity.f662a, "Service state invalid.  Did you start the PayPalService?");
            this.f814a.setResult(2);
            this.f814a.finish();
            return;
        }
        bx bxVar = new bx(this.f814a.getIntent(), this.f814a.f665d.m576d(), true);
        if (!bxVar.m751b()) {
            Log.e(PayPalProfileSharingActivity.f662a, "Service extras invalid.  Please see the docs.");
            this.f814a.setResult(2);
            this.f814a.finish();
        } else if (!bxVar.mo2188c()) {
            Log.e(PayPalProfileSharingActivity.f662a, "Extras invalid.  Please see the docs.");
            this.f814a.setResult(2);
            this.f814a.finish();
        } else if (this.f814a.f665d.m581i()) {
            ProfileSharingConsentActivity.m909a(this.f814a, 1, this.f814a.f665d.m576d());
        } else {
            Calendar instance = Calendar.getInstance();
            instance.add(13, 1);
            this.f814a.f663b = instance.getTime();
            this.f814a.f665d.m567a(new bv(this.f814a), false);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.f814a.f665d = null;
        PayPalProfileSharingActivity.f662a;
    }
}
