package com.paypal.android.sdk;

import android.util.Log;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class cw {
    private static final String f227a = cw.class.getSimpleName();
    private static long f228b = 1;
    private final Map f229c = new LinkedHashMap();
    private final cx f230d;
    private final cu f231e;
    private final String f232f;
    private final long f233g;
    private String f234h;
    private String f235i;
    private by f236j;
    private Integer f237k;
    private String f238l;

    public cw(cu cuVar, cx cxVar, C0439b c0439b, String str) {
        long j = f228b;
        f228b = 1 + j;
        this.f233g = j;
        this.f231e = cuVar;
        this.f232f = str;
        this.f230d = cxVar;
    }

    public static void m186k() {
    }

    public String mo2929a(cu cuVar) {
        String a = this.f230d.mo2156a(cuVar);
        if (a != null) {
            return this.f232f != null ? a + this.f232f : a;
        } else {
            throw new RuntimeException("API " + cuVar.toString() + " has no record for server " + this.f230d.mo2158c());
        }
    }

    public final void m188a(by byVar) {
        if (this.f236j != null) {
            Throwable illegalStateException = new IllegalStateException("Multiple exceptions reported");
            Log.e(f227a, "first mError=" + this.f236j);
            Log.e(f227a, "second mError=" + byVar);
            Log.e(f227a, "", illegalStateException);
            throw illegalStateException;
        }
        this.f236j = byVar;
    }

    public final void m189a(Integer num) {
        this.f237k = num;
    }

    public final void m190a(String str) {
        this.f234h = str;
    }

    protected final void m191a(String str, String str2) {
        this.f229c.put(str, str2);
    }

    public final void m192a(String str, String str2, String str3) {
        m188a(new ca(str, str2, str3));
    }

    public boolean mo2930a() {
        return false;
    }

    public abstract String mo2931b();

    public final void m195b(String str) {
        this.f235i = str;
    }

    public abstract void mo2932c();

    public final void m197c(String str) {
        this.f238l = str;
    }

    public abstract void mo2933d();

    public abstract String mo2934e();

    public final String m200f() {
        return this.f234h;
    }

    public final String m201g() {
        return this.f235i;
    }

    public final cu m202h() {
        return this.f231e;
    }

    public final Map m203i() {
        return this.f229c;
    }

    public final String m204j() {
        return this.f238l;
    }

    public void mo3110l() {
    }

    protected final JSONObject m206m() {
        String str = this.f235i;
        Object nextValue = new JSONTokener(str).nextValue();
        if (nextValue instanceof JSONObject) {
            return (JSONObject) nextValue;
        }
        throw new JSONException("could not parse:" + str + "\nnextValue:" + nextValue);
    }

    public final String m207n() {
        return getClass().getSimpleName() + " SN:" + this.f233g;
    }

    public final long m208o() {
        return this.f233g;
    }

    public final by m209p() {
        return this.f236j;
    }

    public final boolean m210q() {
        return this.f236j == null;
    }

    public final Integer m211r() {
        return this.f237k;
    }

    public final cx m212s() {
        return this.f230d;
    }
}
