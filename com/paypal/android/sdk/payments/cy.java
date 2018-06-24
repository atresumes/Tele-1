package com.paypal.android.sdk.payments;

import android.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.ga;
import java.util.ArrayList;

final class cy implements OnClickListener {
    final /* synthetic */ ga f833a;
    final /* synthetic */ ArrayList f834b;
    final /* synthetic */ PaymentConfirmActivity f835c;

    cy(PaymentConfirmActivity paymentConfirmActivity, ga gaVar, ArrayList arrayList) {
        this.f835c = paymentConfirmActivity;
        this.f833a = gaVar;
        this.f834b = arrayList;
    }

    public final void onClick(View view) {
        Builder builder = new Builder(view.getContext());
        builder.setTitle(fu.m369a(fw.PREFERRED_PAYMENT_METHOD)).setAdapter(this.f833a, new cz(this));
        builder.create().show();
    }
}
