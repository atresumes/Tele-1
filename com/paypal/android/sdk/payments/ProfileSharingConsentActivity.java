package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.content.Intent;

public final class ProfileSharingConsentActivity extends C0459m {
    private static final String f1046d = ProfileSharingConsentActivity.class.getSimpleName();

    static void m909a(Activity activity, int i, PayPalConfiguration payPalConfiguration) {
        Intent intent = new Intent(activity, ProfileSharingConsentActivity.class);
        intent.putExtras(activity.getIntent());
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        activity.startActivityForResult(intent, 1);
    }

    protected final void mo2179a() {
        this.b = (PayPalOAuthScopes) getIntent().getParcelableExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES);
    }

    public final /* bridge */ /* synthetic */ void finish() {
        super.finish();
    }

    public final /* bridge */ /* synthetic */ void onBackPressed() {
        super.onBackPressed();
    }
}
