package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.content.Intent;
import java.util.Arrays;
import java.util.HashSet;

public final class FuturePaymentConsentActivity extends C0459m {
    private static final String f1045d = FuturePaymentConsentActivity.class.getSimpleName();

    static void m907a(Activity activity, int i, PayPalConfiguration payPalConfiguration) {
        Intent intent = new Intent(activity, FuturePaymentConsentActivity.class);
        intent.putExtras(activity.getIntent());
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        activity.startActivityForResult(intent, 1);
    }

    protected final void mo2179a() {
        this.b = new PayPalOAuthScopes(new HashSet(Arrays.asList(new String[]{PayPalOAuthScopes.PAYPAL_SCOPE_FUTURE_PAYMENTS})));
    }

    public final /* bridge */ /* synthetic */ void finish() {
        super.finish();
    }

    public final /* bridge */ /* synthetic */ void onBackPressed() {
        super.onBackPressed();
    }
}
