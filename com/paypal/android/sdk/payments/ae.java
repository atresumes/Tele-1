package com.paypal.android.sdk.payments;

import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.de;
import com.paypal.android.sdk.fu;

class ae {
    private static final String f756a = ae.class.getSimpleName();

    ae() {
    }

    static String m675a() {
        CharSequence a = de.m839a().mo2169c().m330a();
        if (C0441d.m267c(a) || a.equalsIgnoreCase("US")) {
            return "https://www.paypal.com/webapps/accountrecovery/passwordrecovery";
        }
        String c = fu.m372c(a);
        return String.format("https://www.paypal.com/%s/cgi-bin/webscr?cmd=_account-recovery&from=%s&locale.x=%s", new Object[]{a.toLowerCase(), "PayPalMPL", c});
    }
}
