package com.paypal.android.sdk;

import com.paypal.android.sdk.payments.PayPalService;
import java.util.HashMap;
import java.util.Map;

public final class di {
    private static final String f304a = PayPalService.class.getSimpleName();
    private static Map f305b = new HashMap();

    public static dw m282a(String str) {
        dw dwVar = (dw) f305b.get(str);
        new StringBuilder("getLoginAccessToken(").append(str).append(") returns String:").append(dwVar);
        return dwVar;
    }

    public static void m283a(dw dwVar, String str) {
        f305b.put(str, dwVar);
        new StringBuilder("setLoginAccessToken(").append(dwVar).append(",").append(str).append(")");
    }

    public static void m284b(String str) {
        f305b.remove(str);
    }
}
