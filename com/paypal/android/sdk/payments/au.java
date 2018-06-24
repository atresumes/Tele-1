package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class au implements OnClickListener {
    private /* synthetic */ LoginActivity f780a;

    au(LoginActivity loginActivity) {
        this.f780a = loginActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        LoginActivity.m470h(this.f780a);
    }
}
