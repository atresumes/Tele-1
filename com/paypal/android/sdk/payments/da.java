package com.paypal.android.sdk.payments;

import android.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import com.paypal.android.sdk.fu;
import com.paypal.android.sdk.fw;
import com.paypal.android.sdk.gm;
import java.util.ArrayList;

final class da implements OnClickListener {
    final /* synthetic */ gm f837a;
    final /* synthetic */ ArrayList f838b;
    final /* synthetic */ PaymentConfirmActivity f839c;

    da(PaymentConfirmActivity paymentConfirmActivity, gm gmVar, ArrayList arrayList) {
        this.f839c = paymentConfirmActivity;
        this.f837a = gmVar;
        this.f838b = arrayList;
    }

    public final void onClick(View view) {
        Builder builder = new Builder(view.getContext());
        builder.setTitle(fu.m369a(fw.SHIPPING_ADDRESS)).setAdapter(this.f837a, new db(this));
        builder.create().show();
    }
}
