package com.paypal.android.sdk.payments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class ao implements OnClickListener {
    private /* synthetic */ an f775a;

    ao(an anVar) {
        this.f775a = anVar;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f775a.f774c.f609o = i;
        this.f775a.f772a.m419a(i);
        this.f775a.f774c.f610p.f519o.m420a((String) this.f775a.f773b.get(i));
    }
}
