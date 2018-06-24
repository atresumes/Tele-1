package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.bu;
import com.paypal.android.sdk.ey;
import com.paypal.android.sdk.fb;
import com.paypal.android.sdk.fc;
import java.util.Map;

final class C0906h extends C0455i {
    public C0906h(PayPalService payPalService) {
        super(new ci(payPalService));
    }

    protected final String mo2212a() {
        return "msdk";
    }

    protected final void mo2213a(String str, Map map) {
        if (!m722b().m685a().f294a.m305b()) {
            m722b().m685a().f294a = new ey();
        }
        map.put("v49", str);
        map.put("v51", m722b().m688c().mo2159d().m34d());
        map.put("v52", fb.f382a + " " + fb.f384c);
        map.put("v53", fb.f385d);
        map.put("clid", m722b().m687b());
        map.put("apid", m722b().m688c().mo2159d().m32c() + "|" + str + "|" + m722b().m691f());
        m722b().m686a(new bu(m722b().m685a().f294a.m306c(), map));
    }

    protected final void mo2214a(Map map, fc fcVar, String str, String str2) {
        map.put("g", m722b().m690e());
        map.put("vers", fb.f382a + ":" + m722b().m689d() + ":");
        map.put("srce", "msdk");
        map.put("sv", "mobile");
        map.put("bchn", "msdk");
        map.put("adte", "FALSE");
        map.put("bzsr", "mobile");
        if (C0441d.m269d(str)) {
            map.put("calc", str);
        }
        if (C0441d.m269d(str2)) {
            map.put("prid", str2);
        }
        map.put("e", fcVar.m367b() ? "cl" : "im");
    }
}
