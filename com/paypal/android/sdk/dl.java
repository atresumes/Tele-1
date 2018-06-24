package com.paypal.android.sdk;

import com.payUMoney.sdk.SdkConstants;

public final class dl {
    private static /* synthetic */ boolean f317c = (!dl.class.desiredAssertionStatus());
    private C0438a f318a;
    private String f319b;

    public dl(C0438a c0438a, String str) {
        if (f317c || c0438a != null) {
            this.f318a = c0438a;
            this.f319b = "com.paypal.android.sdk.encr." + str + ".";
            return;
        }
        throw new AssertionError();
    }

    public final dm m286a() {
        ev a;
        String a2;
        CharSequence a3;
        dm dmVar;
        String a4 = this.f318a.m27a(this.f319b + "loginPhoneNumber");
        if (a4 != null) {
            try {
                a = ev.m357a(de.m839a(), a4);
            } catch (eo e) {
            }
            a2 = this.f318a.m27a(this.f319b + "loginEmail");
            a3 = this.f318a.m27a(this.f319b + "loginTypePrevious");
            dmVar = new dm(a2, a, C0441d.m262a(a3) ? null : C0442do.valueOf(a3));
            return dmVar.m298d() ? dmVar : null;
        }
        a = null;
        a2 = this.f318a.m27a(this.f319b + "loginEmail");
        a3 = this.f318a.m27a(this.f319b + "loginTypePrevious");
        if (C0441d.m262a(a3)) {
        }
        dmVar = new dm(a2, a, C0441d.m262a(a3) ? null : C0442do.valueOf(a3));
        if (dmVar.m298d()) {
        }
    }

    public final dt m287a(String str) {
        String a = this.f318a.m27a(this.f319b + "tokenizedRedactedCardNumber");
        String a2 = this.f318a.m27a(this.f319b + SdkConstants.TOKEN);
        String a3 = this.f318a.m27a(this.f319b + "tokenPayerID");
        String a4 = this.f318a.m27a(this.f319b + "tokenValidUntil");
        String a5 = this.f318a.m27a(this.f319b + "tokenizedCardType");
        String a6 = this.f318a.m27a(this.f319b + "tokenizedCardExpiryMonth");
        int parseInt = a6 != null ? Integer.parseInt(a6) : 0;
        String a7 = this.f318a.m27a(this.f319b + "tokenizedCardExpiryYear");
        int parseInt2 = a7 != null ? Integer.parseInt(a7) : 0;
        CharSequence c = this.f318a.m33c(this.f318a.m27a(this.f319b + "tokenClientId"));
        if (C0441d.m267c(c) || !c.equals(str)) {
            return null;
        }
        dt dtVar = new dt(a2, a3, a4, a, a5, parseInt, parseInt2);
        return !dtVar.m848b() ? null : dtVar;
    }

    public final void m288a(dm dmVar) {
        String str = null;
        this.f318a.m28a(this.f319b + "loginPhoneNumber", dmVar.m292a() != null ? dmVar.m292a().m361b() : null);
        this.f318a.m28a(this.f319b + "loginEmail", dmVar.m296b());
        if (dmVar.m297c() != null) {
            str = dmVar.m297c().toString();
        }
        this.f318a.m28a(this.f319b + "loginTypePrevious", str);
    }

    public final void m289a(dt dtVar, String str) {
        String str2 = null;
        this.f318a.m28a(this.f319b + SdkConstants.TOKEN, dtVar.m851e());
        this.f318a.m28a(this.f319b + "tokenPayerID", dtVar.m299a());
        this.f318a.m28a(this.f319b + "tokenValidUntil", dtVar.m849c() != null ? new ex().format(dtVar.m849c()) : null);
        this.f318a.m28a(this.f319b + "tokenizedRedactedCardNumber", dtVar.m850d());
        if (dtVar.m854h() != null) {
            str2 = dtVar.m854h().toString();
        }
        this.f318a.m28a(this.f319b + "tokenizedCardType", str2);
        this.f318a.m28a(this.f319b + "tokenizedCardExpiryMonth", String.valueOf(dtVar.m852f()));
        this.f318a.m28a(this.f319b + "tokenizedCardExpiryYear", String.valueOf(dtVar.m853g()));
        this.f318a.m28a(this.f319b + "tokenClientId", this.f318a.m31b(str));
    }

    public final void m290b() {
        m288a(new dm());
    }

    public final void m291c() {
        m289a(new dt(), null);
    }
}
