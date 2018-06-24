package com.paypal.android.sdk;

import java.util.HashMap;
import java.util.Map;

public final class bq {
    private String f191a;
    private String f192b;
    private Map f193c = new HashMap();

    public bq(String str, String str2) {
        this.f191a = str;
        this.f192b = str2;
    }

    public final String m165a() {
        return this.f191a;
    }

    public final String m166b() {
        return this.f192b;
    }

    public final Map m167c() {
        return this.f193c;
    }

    public final String toString() {
        return getClass().getSimpleName() + "(" + this.f191a + ",mEndpoints=" + this.f193c + ")";
    }
}
