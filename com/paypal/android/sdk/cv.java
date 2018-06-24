package com.paypal.android.sdk;

import android.text.TextUtils;

public abstract class cv extends cw {
    static {
        cv.class.getSimpleName();
    }

    public cv(cu cuVar, cx cxVar, C0439b c0439b, String str) {
        this(cuVar, cxVar, c0439b, str, null);
    }

    public cv(cu cuVar, cx cxVar, C0439b c0439b, String str, String str2) {
        super(cuVar, cxVar, c0439b, str2);
        if (!TextUtils.isEmpty(str)) {
            m191a("Authorization", str);
        }
    }
}
