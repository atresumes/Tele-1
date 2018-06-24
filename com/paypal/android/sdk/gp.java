package com.paypal.android.sdk;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class gp {
    private LinearLayout f581a;
    private RelativeLayout f582b;
    private ImageView f583c;
    private ImageView f584d;
    private TextView f585e;
    private TextView f586f;
    private TextView f587g;
    private TextView f588h;

    public gp(Context context) {
        this.f581a = new LinearLayout(context);
        this.f581a.setOrientation(1);
        this.f582b = new RelativeLayout(context);
        this.f581a.addView(this.f582b);
        this.f583c = new ImageView(context);
        this.f583c.setId(2301);
        this.f582b.addView(this.f583c);
        cz.m247b(this.f583c, "35dip", "35dip");
        cz.m248b(this.f583c, null, "4dip", null, null);
        this.f584d = cz.m229a(context, "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAATZJREFUeNrsmMENgkAQRVnPUoAWQB0WoAVoAViA3tW7FEADUIDebUAaoAELwDv+TcaEkFUOsDhj5ieTTdgQ5jHLZ3aDQKVSqVSqAVTX9Q4RSUw8RKSIO+KGmI/5fNMzeZvsGdF88yVia4ypxgCY9Lx/ipi1rkUExb8CVIXFh4SvqMKRPQBBrDAcHFMJIHL2AG8XwrB2TJ0AcWEPQBB2ySwdUxtAlBIAQgxpy5WsKnKmkjVAAyJzuJMXezU+ykp/ZFuJsA0BgA17gAZE5ttevQF02GsOiIQ9AEHEGGJf9uodoMNe7UddsAcgiMyHvU4C4dIl9NcfsWgbFf0jE91KiG7mxLfTojc0oreUojf1oo9VvthlgeS3Y7QpfZu5J+LhsMu9mG7w14e7Q4LIPF5XqVQqlWi9BBgAacm2vqgEoPIAAAAASUVORK5CYII=", "go to selection");
        this.f584d.setId(2304);
        this.f584d.setColorFilter(cy.f252g);
        LayoutParams a = cz.m233a(context, "20dip", "20dip", 11);
        a.addRule(15);
        this.f582b.addView(this.f584d, a);
        this.f585e = new TextView(context);
        cz.m253d(this.f585e, 83);
        this.f585e.setId(2302);
        a = cz.m232a(-2, -2, 1, 2301);
        a.addRule(0, 2304);
        this.f582b.addView(this.f585e, a);
        cz.m248b(this.f585e, "6dip", null, null, null);
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setId(2306);
        LayoutParams a2 = cz.m232a(-2, -2, 1, 2301);
        a2.addRule(3, 2302);
        a2.addRule(0, 2304);
        this.f582b.addView(linearLayout, a2);
        this.f586f = new TextView(context);
        cz.m250b(this.f586f, 83);
        linearLayout.addView(this.f586f);
        cz.m248b(this.f586f, "6dip", null, null, null);
        this.f587g = new TextView(context);
        this.f587g.setId(2305);
        cz.m253d(this.f587g, 83);
        linearLayout.addView(this.f587g);
        cz.m248b(this.f587g, "6dip", null, null, null);
        this.f588h = new TextView(context);
        this.f588h.setId(2307);
        cz.m252c(this.f588h, 83);
        a = cz.m232a(-2, -2, 1, 2301);
        a.addRule(3, 2306);
        a.addRule(0, 2304);
        this.f582b.addView(this.f588h, a);
        this.f588h.setText(fu.m369a(fw.PAY_AFTER_DELIVERY));
        cz.m248b(this.f588h, "6dip", null, null, null);
        this.f588h.setVisibility(8);
        cz.m226a(this.f581a);
        this.f581a.setVisibility(8);
    }

    public final View m431a() {
        return this.f581a;
    }

    public final void m432a(Context context, go goVar) {
        this.f583c.setImageBitmap(cz.m251c(goVar.mo2174a(), context));
        this.f585e.setText(goVar.mo2175b());
        cz.m236a(this.f585e, -2, -1);
        this.f586f.setText(goVar.mo2176c());
        cz.m236a(this.f586f, -2, -1);
        this.f586f.setEllipsize(TruncateAt.END);
        this.f587g.setText(goVar.mo2177d());
        cz.m236a(this.f587g, -2, -1);
        this.f587g.setEllipsize(TruncateAt.END);
        if (goVar.mo2178e()) {
            this.f588h.setVisibility(0);
        } else {
            this.f588h.setVisibility(8);
        }
    }

    public final void m433a(OnClickListener onClickListener) {
        this.f581a.setOnClickListener(onClickListener);
    }
}
