package com.paypal.android.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

public final class gk {
    public TableLayout f567a;
    public TableLayout f568b;
    public TextView f569c;
    public TextView f570d;
    private int f571e;
    private boolean f572f = false;

    public gk(Context context, String str) {
        this.f568b = new TableLayout(context);
        this.f568b.setColumnShrinkable(0, false);
        this.f568b.setColumnStretchable(0, false);
        this.f568b.setColumnStretchable(1, false);
        this.f568b.setColumnShrinkable(1, false);
        View tableRow = new TableRow(context);
        this.f568b.addView(tableRow);
        this.f570d = new TextView(context);
        this.f570d.setTextColor(cy.f254i);
        this.f570d.setText("Item");
        this.f570d.setSingleLine(true);
        this.f570d.setGravity(83);
        this.f570d.setTextSize(18.0f);
        this.f570d.setTextColor(cy.f254i);
        this.f570d.setTypeface(cy.f262q);
        tableRow.addView(this.f570d);
        cz.m235a(this.f570d, 16, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.f571e = cz.m224a("10dip", context);
        cz.m248b(this.f570d, null, null, "10dip", null);
        this.f569c = new TextView(context);
        this.f569c.setTextSize(18.0f);
        this.f569c.setTypeface(cy.f263r);
        this.f569c.setText(str);
        this.f569c.setSingleLine(true);
        this.f569c.setGravity(85);
        this.f569c.setTextColor(cy.f255j);
        tableRow.addView(this.f569c);
        cz.m235a(this.f569c, 5, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.f567a = this.f568b;
    }

    public final void m423a() {
        TextView textView = this.f569c;
        TextView textView2 = this.f570d;
        int width = (this.f568b.getWidth() - ((int) textView.getPaint().measureText(textView.getText().toString()))) - this.f571e;
        CharSequence ellipsize = TextUtils.ellipsize(textView2.getText(), textView2.getPaint(), (float) width, TruncateAt.END);
        textView2.setWidth(width);
        textView2.setText(ellipsize);
    }
}
