package com.paypal.android.sdk;

import java.util.Set;

public final class fu {
    public static boolean f419a;
    private static final C0445g f420b = new C0445g(fw.class, C0467v.f922a);
    private static Set f421c = new fv();

    public static String m369a(fw fwVar) {
        return f420b.m376a((Enum) fwVar);
    }

    public static String m370a(String str) {
        return str.equals(bz.DEVICE_OS_TOO_OLD.toString()) ? f420b.m376a(fw.ANDROID_OS_TOO_OLD) : str.equals(bz.SERVER_COMMUNICATION_ERROR.toString()) ? f420b.m376a(fw.SERVER_PROBLEM) : str.equals(bz.INTERNAL_SERVER_ERROR.toString()) ? f420b.m377a("INTERNAL_SERVICE_ERROR", fw.SYSTEM_ERROR_WITH_CODE) : str.equals(bz.PARSE_RESPONSE_ERROR.toString()) ? f420b.m376a(fw.PP_SERVICE_ERROR_JSON_PARSE_ERROR) : f420b.m377a(str, fw.SYSTEM_ERROR_WITH_CODE);
    }

    public static void m371b(String str) {
        f420b.m378a(str);
        boolean z = C0441d.m269d(str) && f421c.contains(str);
        f419a = z;
    }

    public static String m372c(String str) {
        String a = f420b.m375a();
        return !a.contains("_") ? a + "_" + str : a;
    }
}
