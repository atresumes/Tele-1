package com.paypal.android.sdk.payments;

import android.content.Intent;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.C0441d;
import com.paypal.android.sdk.ak;
import com.paypal.android.sdk.dj;
import java.net.MalformedURLException;
import java.net.URL;

final class bx extends C0466z {
    private boolean f1054c;

    bx(Intent intent, PayPalConfiguration payPalConfiguration, boolean z) {
        super(intent, payPalConfiguration);
        this.f1054c = z;
    }

    private static boolean m923a(String str) {
        try {
            URL url = new URL(str);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    protected final String mo2187a() {
        return PayPalFuturePaymentActivity.class.getSimpleName();
    }

    final boolean mo2188c() {
        boolean d = C0441d.m269d(this.f921b.m491l());
        m750a(d, SdkConstants.MERCHANT_NAME);
        boolean z = this.f921b.m492m() != null && C0441d.m263a(PayPalFuturePaymentActivity.class.getSimpleName(), this.f921b.m492m().toString(), "merchantPrivacyPolicyUrl") && m923a(this.f921b.m492m().toString());
        m750a(z, "merchantPrivacyPolicyUrl");
        boolean z2 = this.f921b.m493n() != null && C0441d.m263a(PayPalFuturePaymentActivity.class.getSimpleName(), this.f921b.m493n().toString(), "merchantUserAgreementUrl") && m923a(this.f921b.m493n().toString());
        m750a(z2, "merchantUserAgreementUrl");
        boolean z3 = !this.f1054c;
        if (this.f1054c) {
            PayPalOAuthScopes payPalOAuthScopes = (PayPalOAuthScopes) this.f920a.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES);
            if (payPalOAuthScopes == null) {
                z3 = false;
            } else if (payPalOAuthScopes.m506a() == null || payPalOAuthScopes.m506a().size() <= 0) {
                z3 = false;
            } else {
                for (String str : payPalOAuthScopes.m506a()) {
                    if (!ak.f21i.contains(str) && !dj.f314i.contains(str)) {
                        z3 = false;
                        break;
                    }
                }
                z3 = true;
            }
        }
        m750a(z3, "paypalScopes");
        return d && z && z2 && z3;
    }
}
