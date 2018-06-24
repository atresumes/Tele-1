package com.paypal.android.sdk.payments;

import android.app.Activity;
import android.content.Intent;
import android.text.style.URLSpan;
import android.view.View;

final class C0449b extends URLSpan {
    private Activity f787a;
    private Class f788b;
    private C0450c f789c;
    private ag f790d;

    C0449b(URLSpan uRLSpan, Activity activity, Class cls, C0450c c0450c, ag agVar) {
        super(uRLSpan.getURL());
        this.f787a = activity;
        this.f788b = cls;
        this.f789c = c0450c;
        this.f790d = agVar;
    }

    public final void onClick(View view) {
        Intent intent = new Intent(this.f787a, this.f788b);
        intent.putExtra("com.paypal.details.scope", this.f790d);
        this.f789c.mo2215a();
        this.f787a.startActivity(intent);
    }
}
