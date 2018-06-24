package com.paypal.android.sdk.payments;

import android.content.Context;
import com.paypal.android.sdk.bp;
import com.paypal.android.sdk.dq;

class C0457k {
    private static final String f882a = C0457k.class.getSimpleName();

    C0457k() {
    }

    static boolean m723a(Context context, PayPalService payPalService) {
        boolean z = false;
        if (bp.m164e(payPalService.m577e())) {
            new StringBuilder("Is mock or sandbox:").append(payPalService.m577e());
        } else if (payPalService.m595w()) {
            dq dqVar = new dq();
            boolean x = payPalService.m596x();
            boolean a = dqVar.m813a(context, "com.paypal.android.p2pmobile.Sdk", "com.paypal.android.lib.authenticator.activity.SdkActivity");
            if (dqVar.m814a(context, x) && a) {
                z = true;
            }
        }
        new StringBuilder("returning isValid:").append(z);
        return z;
    }
}
