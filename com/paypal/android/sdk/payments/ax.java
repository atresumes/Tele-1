package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class ax implements OnClickListener {
    private /* synthetic */ LoginActivity f783a;

    ax(LoginActivity loginActivity) {
        this.f783a = loginActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f783a.onBackPressed();
    }
}
