package com.paypal.android.sdk.payments;

import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.fg;
import com.paypal.android.sdk.fk;

class C0448a {
    private static final String f738a = C0448a.class.getSimpleName();
    private cf f739b;
    private Object f740c;
    private cc f741d;

    C0448a() {
    }

    private void m664b(cc ccVar) {
        ccVar.mo2183a(this.f740c);
        Object obj = ((this.f740c instanceof fg) || (this.f740c instanceof fk)) ? null : 1;
        this.f740c = null;
        if (obj != null) {
            this.f741d = null;
        }
    }

    final void m665a() {
        if (this.f740c == null) {
            this.f740c = SdkConstants.SUCCESS_STRING;
        }
        if (this.f741d != null) {
            m664b(this.f741d);
        }
    }

    final void m666a(cc ccVar) {
        if (this.f740c != null) {
            m664b(ccVar);
        } else if (this.f739b != null) {
            ccVar.mo2182a(this.f739b);
            this.f739b = null;
            this.f741d = null;
        } else {
            this.f741d = ccVar;
        }
    }

    final void m667a(cf cfVar) {
        if (this.f741d != null) {
            this.f741d.mo2182a(cfVar);
        } else {
            this.f739b = cfVar;
        }
    }

    final void m668a(Object obj) {
        this.f740c = obj;
    }

    final void m669b() {
        this.f741d = null;
    }
}
