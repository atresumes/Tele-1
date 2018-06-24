package com.paypal.android.sdk;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import java.util.ArrayList;
import java.util.Iterator;

public final class ga extends ArrayAdapter {
    private int f496a;

    public ga(Context context, ArrayList arrayList, int i) {
        super(context, 0, arrayList);
        this.f496a = i;
    }

    private static void m379a(Context context, RelativeLayout relativeLayout, fz fzVar) {
        View linearLayout = new LinearLayout(context);
        linearLayout.setId(2304);
        int i = 2304;
        linearLayout.setOrientation(0);
        LayoutParams a = cz.m232a(-2, -2, 1, 2301);
        a.addRule(0, 2303);
        relativeLayout.addView(linearLayout, a);
        TextView textView = new TextView(context);
        textView.setId(2302);
        cz.m250b(textView, 83);
        linearLayout.addView(textView);
        cz.m248b(textView, "6dip", null, null, null);
        View textView2 = new TextView(context);
        cz.m253d(textView2, 83);
        linearLayout.addView(textView2);
        cz.m248b(textView2, "6dip", null, "6dip", null);
        if (fzVar.mo2178e()) {
            TextView textView3 = new TextView(context);
            textView3.setId(2306);
            i = 2306;
            cz.m252c(textView3, 83);
            a = cz.m232a(-2, -2, 1, 2301);
            a.addRule(0, 2303);
            a.addRule(3, 2304);
            relativeLayout.addView(textView3, a);
            cz.m248b(textView3, "6dip", null, null, null);
            textView3.setText(fu.m369a(fw.PAY_AFTER_DELIVERY));
        }
        int i2 = i;
        Iterator it = fzVar.m867f().iterator();
        int i3 = 2400;
        int i4 = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
        while (it.hasNext()) {
            gb gbVar = (gb) it.next();
            View textView4 = new TextView(context);
            textView4.setId(i3);
            textView4.setText(gbVar.m383a() + " " + gbVar.m384b());
            LayoutParams a2 = cz.m232a(-2, -2, 1, 2301);
            a2.addRule(0, i4);
            if (textView4.getId() == 2400) {
                a2.addRule(3, i2);
            } else {
                a2.addRule(3, textView4.getId() - 1);
            }
            relativeLayout.addView(textView4, a2);
            cz.m254e(textView4, 83);
            cz.m248b(textView4, "6dip", null, null, null);
            textView4.setEllipsize(TruncateAt.END);
            textView4 = new TextView(context);
            textView4.setId(i4);
            textView4.setText(gbVar.m385c());
            LayoutParams a3 = cz.m232a(-2, -2, 0, 2303);
            a3.addRule(8, i3);
            relativeLayout.addView(textView4, a3);
            cz.m253d(textView4, 85);
            cz.m248b(textView4, "6dip", null, "6dip", null);
            i3++;
            i4++;
        }
        textView.setText(fzVar.mo2176c());
        textView.setEllipsize(TruncateAt.END);
        textView2.setText(fzVar.mo2177d());
        textView2.setEllipsize(TruncateAt.END);
    }

    public final void m380a(int i) {
        this.f496a = i;
    }

    public final View getView(int i, View view, ViewGroup viewGroup) {
        fz fzVar = (fz) getItem(i);
        View linearLayout = new LinearLayout(viewGroup.getContext());
        View relativeLayout = new RelativeLayout(viewGroup.getContext());
        linearLayout.addView(relativeLayout);
        cz.m240a(relativeLayout, null, "6dip", null, "6dip");
        View a = cz.m229a(viewGroup.getContext(), fzVar.mo2174a(), "");
        a.setId(2301);
        LayoutParams a2 = cz.m233a(viewGroup.getContext(), "30dip", "30dip", 9);
        a2.addRule(10);
        relativeLayout.addView(a, a2);
        cz.m240a(a, "4dip", null, null, null);
        a = cz.m229a(viewGroup.getContext(), "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", "checked");
        a.setId(2303);
        a2 = cz.m233a(viewGroup.getContext(), "20dip", "20dip", 11);
        a2.addRule(10);
        relativeLayout.addView(a, a2);
        a.setColorFilter(cy.f247b);
        cz.m240a(a, null, null, "8dip", null);
        if (i != this.f496a) {
            a.setVisibility(4);
        }
        if (fzVar.m868g()) {
            LayoutParams a3;
            Context context = viewGroup.getContext();
            TextView textView = new TextView(context);
            textView.setId(2302);
            int i2 = 2302;
            cz.m250b(textView, 83);
            LayoutParams a4 = cz.m232a(-2, -2, 1, 2301);
            a4.addRule(0, 2303);
            relativeLayout.addView(textView, a4);
            cz.m248b(textView, "6dip", null, null, null);
            if (fzVar.mo2178e()) {
                TextView textView2 = new TextView(context);
                textView2.setId(2306);
                i2 = 2306;
                cz.m252c(textView2, 83);
                a3 = cz.m232a(-2, -2, 1, 2301);
                a3.addRule(0, 2303);
                a3.addRule(3, 2302);
                relativeLayout.addView(textView2, a3);
                cz.m248b(textView2, "6dip", null, null, null);
                textView2.setText(fu.m369a(fw.PAY_AFTER_DELIVERY));
            }
            View textView3 = new TextView(context);
            textView3.setId(2305);
            cz.m253d(textView3, 83);
            a3 = cz.m232a(-2, -2, 1, 2301);
            a3.addRule(3, i2);
            relativeLayout.addView(textView3, a3);
            cz.m248b(textView3, "6dip", null, null, null);
            a = new TextView(context);
            a2 = cz.m232a(-2, -2, 0, 2303);
            a2.addRule(8, 2305);
            relativeLayout.addView(a, a2);
            cz.m253d(a, 85);
            cz.m248b(a, null, null, "6dip", null);
            textView.setText(fzVar.mo2176c());
            textView3.setText(fzVar.mo2177d());
            a.setText(fzVar.m867f().m390a(0).m385c());
        } else {
            m379a(viewGroup.getContext(), relativeLayout, fzVar);
        }
        return linearLayout;
    }
}
