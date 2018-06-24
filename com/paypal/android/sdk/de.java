package com.paypal.android.sdk;

import java.util.Locale;

public final class de implements eu {
    private static volatile de f996a;

    private de() {
    }

    public static de m839a() {
        if (f996a == null) {
            synchronized (de.class) {
                if (f996a == null) {
                    f996a = new de();
                }
            }
        }
        return f996a;
    }

    public final String mo2167a(String str) {
        return str;
    }

    public final Locale mo2168b() {
        return Locale.getDefault();
    }

    public final ei mo2169c() {
        return new ei(Locale.getDefault().getCountry());
    }

    public final ei mo2170d() {
        return mo2169c();
    }
}
