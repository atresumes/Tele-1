package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class C0454g implements OnClickListener {
    private /* synthetic */ Activity f877a;

    C0454g(Activity activity) {
        this.f877a = activity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f877a.finish();
    }
}
