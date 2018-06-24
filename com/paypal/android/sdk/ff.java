package com.paypal.android.sdk;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ff extends fn {
    public final String f1122a;
    private List f1123b;
    private final String f1124c;

    public ff(cx cxVar, C0439b c0439b, String str, String str2, String str3, String str4, List list) {
        super(df.ConsentRequest, cxVar, c0439b, fn.m1071b(str, str2));
        this.f1122a = str3;
        this.f1124c = str4;
        this.f1123b = list;
    }

    public final String mo2931b() {
        Map hashMap = new HashMap();
        hashMap.put("code", this.f1122a);
        hashMap.put("nonce", this.f1124c);
        hashMap.put("scope", TextUtils.join(" ", this.f1123b));
        return C0441d.m258a(hashMap);
    }

    public final void mo2932c() {
    }

    public final void mo2933d() {
        m1072b(m206m());
    }

    public final String mo2934e() {
        return "{\"code\":\"EOTHbvqh0vwM2ldM2QIXbjVw0hZNuZEJLqdWmfTBLLSvGfqgyy9GKvjGybIxyGMd7gHXCXVtymqFQHS-J-4-Ir6u2LUVVdyLKonwTtdFw9qhBaMb4NZuZHKS0bGxdZlRAB3_Fk8HX2r3z8j03xScx4M\",\"scope\":\"https://uri.paypal.com/services/payments/futurepayments\"}";
    }
}
