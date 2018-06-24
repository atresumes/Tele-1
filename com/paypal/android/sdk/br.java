package com.paypal.android.sdk;

import android.os.Message;

public class br implements cx {
    private static final String f983a = br.class.getSimpleName();
    private final C0438a f984b;
    private final bq f985c;
    private final bv f986d = new bv();
    private final bs f987e = new bs(this);
    private cs f988f;

    public br(C0438a c0438a, bq bqVar, C0439b c0439b) {
        this.f984b = c0438a;
        this.f985c = bqVar;
    }

    public final String mo2156a(cu cuVar) {
        new StringBuilder("mEnvironment:").append(this.f985c).append(" mEnvironment.getEndpoints():").append(this.f985c.m167c());
        if (this.f985c == null || this.f985c.m167c() == null) {
            return null;
        }
        String str = (String) this.f985c.m167c().get(cuVar.mo2164a());
        new StringBuilder("returning:").append(str);
        return str;
    }

    public final void m823a() {
        this.f988f.m179a();
    }

    public final void m824a(bx bxVar) {
        this.f986d.m169a(bxVar);
    }

    public final void m825a(cs csVar) {
        if (this.f988f != null) {
            throw new IllegalStateException();
        }
        this.f988f = csVar;
    }

    public final void mo2157a(cw cwVar) {
        cwVar.mo3110l();
        at.m75a().m102f();
        if (!cwVar.mo2930a()) {
            Message message = new Message();
            message.what = 2;
            message.obj = cwVar;
            this.f987e.sendMessage(message);
        }
    }

    public final void m827b() {
        this.f986d.m168a();
    }

    public final void m828b(cw cwVar) {
        this.f988f.m180a(cwVar);
    }

    public final String mo2158c() {
        return this.f985c.m165a();
    }

    public final C0438a mo2159d() {
        return this.f984b;
    }

    public final String mo2160e() {
        return this.f985c.m166b();
    }
}
