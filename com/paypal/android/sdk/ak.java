package com.paypal.android.sdk;

import com.google.android.gms.common.Scopes;
import com.talktoangel.gts.utils.SessionManager;
import java.util.Collection;

public enum ak {
    FUTURE_PAYMENTS("https://uri.paypal.com/services/payments/futurepayments", false),
    PROFILE(Scopes.PROFILE, true),
    PAYPAL_ATTRIBUTES("https://uri.paypal.com/services/paypalattributes", true),
    OPENID("openid", true),
    EMAIL("email", true),
    ADDRESS(SessionManager.KEY_ADDRESS, true),
    PHONE("phone", true);
    
    public static final Collection f20h = null;
    public static final Collection f21i = null;
    private String f23j;
    private boolean f24k;

    static {
        f20h = new al();
        f21i = new am();
    }

    private ak(String str, boolean z) {
        this.f23j = str;
        this.f24k = z;
    }

    public final String m40a() {
        return this.f23j;
    }
}
