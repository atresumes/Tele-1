package com.paypal.android.sdk.payments;

import android.os.AsyncTask;

final class ds extends AsyncTask {
    final /* synthetic */ PaymentMethodActivity f858a;

    private ds(PaymentMethodActivity paymentMethodActivity) {
        this.f858a = paymentMethodActivity;
    }

    protected final /* synthetic */ Object doInBackground(Object[] objArr) {
        long currentTimeMillis = System.currentTimeMillis();
        PaymentMethodActivity.f713a;
        this.f858a.f717e = C0905d.m980e();
        PaymentMethodActivity.f713a;
        new StringBuilder("cameraAvailable:").append(this.f858a.f717e).append(" time elapsed = ").append(Long.toString(System.currentTimeMillis() - currentTimeMillis));
        this.f858a.runOnUiThread(new dt(this));
        return null;
    }
}
