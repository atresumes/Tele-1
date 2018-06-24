package com.paypal.android.sdk;

import android.os.Build;
import java.io.File;

public final class az {
    private static ar f125a = new ar();

    public static boolean m110a() {
        boolean z = Build.TAGS != null && Build.TAGS.contains("test-keys");
        return z || m111b() || m112c();
    }

    private static boolean m111b() {
        try {
            return new File(ar.m68a("suFileName")).exists();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean m112c() {
        try {
            return new File(ar.m68a("superUserApk")).exists();
        } catch (Exception e) {
            return false;
        }
    }
}
