package com.paypal.android.sdk.payments;

import android.os.Bundle;
import com.paypal.android.sdk.dy;

class C0458l {
    private static final String f883a = C0458l.class.getSimpleName();

    C0458l() {
    }

    public static C0456j m724a(Bundle bundle) {
        String string = bundle.getString("authAccount");
        String string2 = bundle.getString("code");
        String string3 = bundle.getString("nonce");
        for (String str : bundle.keySet()) {
            if (bundle.get(str) == null) {
                String.format("%s:null", new Object[]{(String) r4.next()});
            } else {
                String.format("%s:%s (%s)", new Object[]{(String) r4.next(), bundle.get(str).toString(), bundle.get(str).getClass().getName()});
            }
        }
        return new C0456j(string3, new dy(string2, null), string);
    }
}
