package com.paypal.android.sdk;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

public final class fy {
    public LinearLayout f486a = this.f488c;
    public TextView f487b;
    private LinearLayout f488c;

    public fy(Context context) {
        this.f488c = new LinearLayout(context);
        this.f488c.setOrientation(0);
        this.f487b = new TextView(context);
        this.f487b.setText("server");
        this.f487b.setTextColor(-1);
        this.f487b.setBackgroundColor(cy.f250e);
        this.f487b.setGravity(17);
        this.f488c.addView(this.f487b);
        cz.m240a(this.f487b, "8dip", "8dip", "8dip", "8dip");
        cz.m236a(this.f487b, -2, -2);
        cz.m248b(this.f487b, null, "15dip", null, "15dip");
        cz.m235a(this.f487b, 1, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
