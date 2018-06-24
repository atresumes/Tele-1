package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.fk;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;

final class C0911s implements cc {
    private /* synthetic */ C0459m f1067a;

    C0911s(C0459m c0459m) {
        this.f1067a = c0459m;
    }

    public final void mo2182a(cf cfVar) {
        this.f1067a.dismissDialog(2);
        if (cfVar.f820b.equals("invalid_nonce")) {
            this.f1067a.f888c.f481h.setEnabled(false);
            C0905d.m973a(this.f1067a, fu.m369a(fw.SESSION_EXPIRED_MESSAGE), 4);
            return;
        }
        this.f1067a.f888c.f481h.setEnabled(true);
        C0905d.m973a(this.f1067a, fu.m370a(cfVar.f820b), 1);
    }

    public final void mo2183a(Object obj) {
        if (obj instanceof fk) {
            C0459m.m731a(this.f1067a, (fk) obj);
        } else {
            this.f1067a.m745i();
        }
    }
}
