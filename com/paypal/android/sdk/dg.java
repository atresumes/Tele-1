package com.paypal.android.sdk;

import java.util.UUID;

public final class dg {
    public ey f294a = new ey();
    public dw f295b;
    public String f296c;
    public dm f297d;
    public dy f298e;
    public ed f299f;
    public dw f300g;
    public boolean f301h;
    public String f302i;
    private String f303j;

    public dg() {
        eb ebVar = new eb();
        m279a();
    }

    public final void m279a() {
        this.f303j = UUID.randomUUID().toString();
    }

    public final String m280b() {
        return this.f303j;
    }

    public final boolean m281c() {
        return this.f295b != null && this.f295b.m305b();
    }

    public final String toString() {
        return "BackendState(accessTokenState:" + this.f295b + " loginAccessToken:" + this.f300g + ")";
    }
}
