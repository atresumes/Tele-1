package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class C0453f implements OnClickListener {
    private /* synthetic */ Activity f875a;
    private /* synthetic */ int f876b;

    C0453f(Activity activity, int i) {
        this.f875a = activity;
        this.f876b = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f875a.removeDialog(this.f876b);
        this.f875a.finish();
    }
}
