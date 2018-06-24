package com.paypal.android.sdk;

import com.payu.custombrowser.util.CBConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class C0445g {
    private static final String f489a = C0445g.class.getSimpleName();
    private static final Map f490b = new HashMap();
    private static final Set f491c = new HashSet();
    private static /* synthetic */ boolean f492g = (!C0445g.class.desiredAssertionStatus());
    private Map f493d = new LinkedHashMap();
    private C0446h f494e;
    private Class f495f;

    static {
        f490b.put("zh_CN", "zh-Hans");
        f490b.put("zh_TW", "zh-Hant_TW");
        f490b.put("zh_HK", "zh-Hant");
        f490b.put("en_UK", "en_GB");
        f490b.put("en_IE", "en_GB");
        f490b.put("iw_IL", "he");
        f490b.put("no", CBConstant.NB);
        f491c.add("he");
        f491c.add("ar");
    }

    public C0445g(Class cls, List list) {
        this.f495f = cls;
        for (C0446h c0446h : list) {
            String a = c0446h.mo2147a();
            if (a == null) {
                throw new RuntimeException("Null localeName");
            } else if (this.f493d.containsKey(a)) {
                throw new RuntimeException("Locale " + a + " already added");
            } else {
                this.f493d.put(a, c0446h);
                m373b(a);
            }
        }
        m378a(null);
    }

    private void m373b(String str) {
        C0446h c0446h = (C0446h) this.f493d.get(str);
        List arrayList = new ArrayList();
        new StringBuilder("Checking locale ").append(str);
        for (Enum enumR : (Enum[]) this.f495f.getEnumConstants()) {
            String str2 = "[" + str + "," + enumR + "]";
            if (c0446h.mo2148a(enumR, null) == null) {
                arrayList.add("Missing " + str2);
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            it.next();
        }
    }

    private C0446h m374c(String str) {
        C0446h c0446h = null;
        if (str == null || str.length() < 2) {
            return null;
        }
        if (f490b.containsKey(str)) {
            String str2 = (String) f490b.get(str);
            C0446h c0446h2 = (C0446h) this.f493d.get(str2);
            new StringBuilder("Overriding locale specifier ").append(str).append(" with ").append(str2);
            c0446h = c0446h2;
        }
        if (c0446h == null) {
            c0446h = (C0446h) this.f493d.get(str.contains("_") ? str : str + "_" + Locale.getDefault().getCountry());
        }
        if (c0446h == null) {
            c0446h = (C0446h) this.f493d.get(str);
        }
        if (c0446h != null) {
            return c0446h;
        }
        return (C0446h) this.f493d.get(str.substring(0, 2));
    }

    public final String m375a() {
        return this.f494e.mo2147a();
    }

    public final String m376a(Enum enumR) {
        C0446h c0446h = this.f494e;
        String toUpperCase = Locale.getDefault().getCountry().toUpperCase(Locale.US);
        String a = c0446h.mo2148a(enumR, toUpperCase);
        if (a == null) {
            new StringBuilder("Missing localized string for [").append(this.f494e.mo2147a()).append(",Key.").append(enumR.toString()).append("]");
            a = ((C0446h) this.f493d.get("en")).mo2148a(enumR, toUpperCase);
        }
        if (a != null) {
            return a;
        }
        new StringBuilder("Missing localized string for [en,Key.").append(enumR.toString()).append("], so defaulting to keyname");
        return enumR.toString();
    }

    public final String m377a(String str, Enum enumR) {
        String a = this.f494e.mo2149a(str);
        if (a != null) {
            return a;
        }
        return String.format(m376a(enumR), new Object[]{str});
    }

    public final void m378a(String str) {
        C0446h c0446h = null;
        new StringBuilder("setLanguage(").append(str).append(")");
        this.f494e = null;
        if (str != null) {
            c0446h = m374c(str);
        }
        if (c0446h == null) {
            String locale = Locale.getDefault().toString();
            new StringBuilder().append(str).append(" not found.  Attempting to look for ").append(locale);
            c0446h = m374c(locale);
        }
        if (c0446h == null) {
            c0446h = (C0446h) this.f493d.get("en");
        }
        if (f492g || c0446h != null) {
            this.f494e = c0446h;
            if (f492g || this.f494e != null) {
                new StringBuilder("setting locale to:").append(this.f494e.mo2147a());
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }
}
