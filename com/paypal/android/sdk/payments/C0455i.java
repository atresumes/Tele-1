package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.fb;
import com.paypal.android.sdk.fc;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

abstract class C0455i {
    private final ci f878a;

    public C0455i(ci ciVar) {
        this.f878a = ciVar;
    }

    private String m717a(fc fcVar, boolean z) {
        String str = fb.f383b + ":" + mo2212a() + ":" + fcVar.m365a();
        return z ? str + "|error" : str;
    }

    protected abstract String mo2212a();

    public void m719a(fc fcVar, boolean z, String str, String str2, String str3) {
        Object obj;
        String str4 = "2.14.2";
        de.m839a();
        String locale = Locale.getDefault().toString();
        Map hashMap = new HashMap();
        boolean z2 = !C0441d.m262a((CharSequence) str);
        hashMap.put("gn", m717a(fcVar, z2));
        hashMap.put("v31", m717a(fcVar, z2));
        String str5 = "c25";
        String str6 = m717a(fcVar, z2) + ":" + fcVar.m366a(this.f878a.m689d(), z);
        if (z2) {
            obj = str6 + "|error";
        } else {
            String str7 = str6;
        }
        hashMap.put(str5, obj);
        hashMap.put("v25", "D=c25");
        hashMap.put("c37", fb.f382a + "::");
        hashMap.put("c50", locale);
        hashMap.put("c35", "out");
        mo2214a(hashMap, fcVar, str2, str3);
        if (str != null) {
            hashMap.put("c29", str);
        }
        mo2213a(str4, hashMap);
    }

    abstract void mo2213a(String str, Map map);

    protected void mo2214a(Map map, fc fcVar, String str, String str2) {
    }

    protected ci m722b() {
        return this.f878a;
    }
}
