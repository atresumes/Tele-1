package com.paypal.android.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

public final class ge {
    private ViewGroup f523a;
    private ViewGroup f524b;
    private gk f525c;
    private LinearLayout f526d = cz.m231a(this.f524b);
    private TextView f527e;
    private LinearLayout f528f;
    private TextView f529g;
    private TextView f530h;
    private gj f531i;
    private fy f532j;
    private gk f533k;
    private gp f534l;
    private gp f535m;
    private gg f536n;

    public ge(Context context, boolean z) {
        this.f524b = cz.m228a(context);
        LinearLayout b = cz.m246b(this.f526d);
        this.f527e = new TextView(context);
        cz.m240a(this.f527e, "0dip", "0dip", "0dip", "14dip");
        this.f527e.setTextSize(24.0f);
        this.f527e.setTextColor(cy.f246a);
        b.addView(this.f527e);
        cz.m236a(this.f527e, -2, -2);
        this.f525c = new gk(context, "description");
        this.f525c.f570d.setTypeface(cy.f263r);
        b.addView(this.f525c.f567a);
        cz.m234a(this.f525c.f567a);
        cz.m226a(b);
        if (z) {
            this.f536n = new gg(context);
            b.addView(this.f536n.m415a());
            cz.m226a(b);
            this.f534l = new gp(context);
            b.addView(this.f534l.m431a());
        } else {
            this.f531i = new gj(context);
            b.addView(this.f531i.f563a);
            cz.m234a(this.f531i.f563a);
            cz.m226a(b);
            this.f533k = new gk(context, "00 / 0000");
            b.addView(this.f533k.f567a);
            cz.m234a(this.f533k.f567a);
        }
        this.f535m = new gp(context);
        this.f535m.m432a(context, new gl());
        b.addView(this.f535m.m431a());
        this.f529g = new TextView(context);
        this.f529g.setId(43002);
        cz.m249b(this.f529g);
        b.addView(this.f529g);
        cz.m236a(this.f529g, -1, -2);
        cz.m248b(this.f529g, null, "20dip", null, "10dip");
        this.f529g.setVisibility(8);
        this.f528f = cz.m230a(context, true, 43001, b);
        this.f530h = new TextView(context);
        cz.m243a(this.f530h);
        this.f530h.setText("init");
        this.f528f.addView(this.f530h);
        this.f532j = new fy(context);
        this.f526d.addView(this.f532j.f486a);
        cz.m236a(this.f532j.f486a, -2, -2);
        cz.m235a(this.f532j.f486a, 17, (float) DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.f523a = this.f524b;
    }

    public final View m397a() {
        return this.f523a;
    }

    public final void m398a(Context context, fz fzVar) {
        if (this.f534l != null) {
            this.f534l.m432a(context, fzVar);
        }
    }

    public final void m399a(Context context, gl glVar) {
        if (this.f535m != null) {
            this.f535m.m432a(context, glVar);
        }
    }

    public final void m400a(SpannableString spannableString) {
        if (C0441d.m269d(spannableString)) {
            this.f529g.setText(spannableString);
            this.f529g.setVisibility(0);
            return;
        }
        this.f529g.setVisibility(8);
    }

    public final void m401a(OnClickListener onClickListener) {
        if (this.f536n != null) {
            this.f536n.m416a(onClickListener);
        }
    }

    public final void m402a(String str) {
        this.f536n.m417a(str);
    }

    public final void m403a(String str, Bitmap bitmap, String str2) {
        this.f531i.f564b.setText(str);
        this.f531i.f565c.setImageBitmap(bitmap);
        this.f533k.f569c.setText(str2);
    }

    public final void m404a(String str, String str2) {
        this.f525c.f570d.setText(str);
        this.f525c.f569c.setText(str2);
    }

    public final void m405a(boolean z) {
        if (z) {
            if (C0441d.m265b()) {
                this.f530h.setText(fu.m369a(fw.AGREE_AND_PAY));
            } else {
                this.f530h.setText(fu.m369a(fw.CONFIRM_SEND_PAYMENT));
            }
            this.f536n.m418b();
            return;
        }
        this.f530h.setText(fu.m369a(fw.CONFIRM_CHARGE_CREDIT_CARD));
        this.f531i.f563a.setVisibility(0);
        this.f533k.f567a.setVisibility(0);
        this.f533k.f570d.setText(fu.m369a(fw.EXPIRES_ON_DATE));
    }

    public final TextView m406b() {
        return this.f527e;
    }

    public final void m407b(OnClickListener onClickListener) {
        this.f528f.setOnClickListener(onClickListener);
    }

    public final void m408b(boolean z) {
        if (this.f528f != null) {
            this.f528f.setEnabled(z);
        }
    }

    public final void m409c() {
        this.f525c.m423a();
    }

    public final void m410c(OnClickListener onClickListener) {
        if (this.f534l != null) {
            this.f534l.m433a(onClickListener);
        }
    }

    public final TextView m411d() {
        return this.f532j.f487b;
    }

    public final void m412d(OnClickListener onClickListener) {
        if (this.f535m != null) {
            this.f535m.m433a(onClickListener);
        }
    }

    public final View m413e() {
        return this.f534l != null ? this.f534l.m431a() : null;
    }

    public final View m414f() {
        return this.f535m != null ? this.f535m.m431a() : null;
    }
}
