package com.paypal.android.sdk;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

public final class gi {
    public LinearLayout f556a;
    public RelativeLayout f557b;
    public final Button f558c;
    private final TableLayout f559d;
    private ImageView f560e;
    private TextView f561f;
    private TextView f562g;

    public gi(Context context) {
        this.f556a = new LinearLayout(context);
        this.f556a.setLayoutParams(cz.m227a());
        this.f556a.setOrientation(1);
        cz.m226a(this.f556a);
        this.f559d = new TableLayout(context);
        this.f559d.setColumnShrinkable(0, false);
        this.f559d.setColumnStretchable(0, false);
        this.f559d.setColumnStretchable(1, false);
        this.f559d.setColumnShrinkable(1, false);
        View tableRow = new TableRow(context);
        this.f559d.addView(tableRow);
        this.f556a.addView(this.f559d);
        this.f557b = new RelativeLayout(context);
        tableRow.addView(this.f557b);
        cz.m235a(this.f557b, 19, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        cz.m248b(this.f557b, null, null, "10dip", null);
        this.f562g = new TextView(context);
        cz.m253d(this.f562g, 0);
        this.f562g.setId(2301);
        this.f557b.addView(this.f562g);
        cz.m248b(this.f562g, "6dip", null, null, null);
        this.f561f = new TextView(context);
        cz.m250b(this.f561f, 0);
        this.f561f.setId(2302);
        this.f557b.addView(this.f561f, cz.m232a(-2, -2, 3, 2301));
        cz.m248b(this.f561f, "6dip", null, null, null);
        this.f560e = cz.m229a(context, "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAATZJREFUeNrsmMENgkAQRVnPUoAWQB0WoAVoAViA3tW7FEADUIDebUAaoAELwDv+TcaEkFUOsDhj5ieTTdgQ5jHLZ3aDQKVSqVSqAVTX9Q4RSUw8RKSIO+KGmI/5fNMzeZvsGdF88yVia4ypxgCY9Lx/ipi1rkUExb8CVIXFh4SvqMKRPQBBrDAcHFMJIHL2AG8XwrB2TJ0AcWEPQBB2ySwdUxtAlBIAQgxpy5WsKnKmkjVAAyJzuJMXezU+ykp/ZFuJsA0BgA17gAZE5ttevQF02GsOiIQ9AEHEGGJf9uodoMNe7UddsAcgiMyHvU4C4dIl9NcfsWgbFf0jE91KiG7mxLfTojc0oreUojf1oo9VvthlgeS3Y7QpfZu5J+LhsMu9mG7w14e7Q4LIPF5XqVQqlWi9BBgAacm2vqgEoPIAAAAASUVORK5CYII=", "go to selection");
        this.f560e.setId(2307);
        this.f560e.setColorFilter(cy.f252g);
        LayoutParams a = cz.m233a(context, "20dip", "20dip", 15);
        a.addRule(1, 2302);
        a.addRule(1, 2301);
        this.f557b.addView(this.f560e, a);
        this.f558c = new Button(context);
        this.f558c.setId(2305);
        cz.m244a(this.f558c, 21);
        this.f558c.setTextSize(18.0f);
        tableRow.addView(this.f558c);
        cz.m248b(this.f558c, null, null, "6dip", null);
        cz.m235a(this.f558c, 21, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        cz.m226a(this.f556a);
        this.f556a.setVisibility(0);
    }

    public final void m420a(String str) {
        this.f561f.setText(str);
        cz.m236a(this.f561f, -2, -1);
        this.f561f.setEllipsize(TruncateAt.START);
    }

    public final void m421a(boolean z) {
        this.f557b.setClickable(z);
        this.f560e.setVisibility(z ? 0 : 8);
    }

    public final void m422b(String str) {
        this.f562g.setText(str);
    }
}
