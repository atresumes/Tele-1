package com.paypal.android.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.ColorDrawable;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class cz {
    private static final Map f272a;
    private static Pattern f273b = Pattern.compile("^\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$");
    private static HashMap f274c = new HashMap();

    static {
        Map hashMap = new HashMap();
        hashMap.put("px", Integer.valueOf(0));
        hashMap.put("dip", Integer.valueOf(1));
        hashMap.put("dp", Integer.valueOf(1));
        hashMap.put("sp", Integer.valueOf(2));
        hashMap.put("pt", Integer.valueOf(3));
        hashMap.put("in", Integer.valueOf(4));
        hashMap.put("mm", Integer.valueOf(5));
        f272a = Collections.unmodifiableMap(hashMap);
    }

    public static int m224a(String str, Context context) {
        return str == null ? 0 : (int) m245b(str, context);
    }

    public static Bitmap m225a(String str, Context context, int i) {
        Options options = new Options();
        if (context != null) {
            options.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        } else {
            options.inTargetDensity = 160;
        }
        options.inDensity = 240;
        options.inScaled = false;
        byte[] decode = Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
    }

    public static View m226a(LinearLayout linearLayout) {
        View view = new View(linearLayout.getContext());
        linearLayout.addView(view);
        view.setBackground(new ColorDrawable(cy.f251f));
        m237a(view, -1, "1dip");
        m248b(view, null, "12dip", null, "12dip");
        return view;
    }

    public static LayoutParams m227a() {
        return new LayoutParams(-1, -2);
    }

    public static ViewGroup m228a(Context context) {
        ViewGroup scrollView = new ScrollView(context);
        scrollView.setBackgroundColor(cy.f249d);
        return scrollView;
    }

    public static ImageView m229a(Context context, String str, String str2) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(m251c(str, context));
        imageView.setAdjustViewBounds(true);
        imageView.setContentDescription(str2);
        return imageView;
    }

    public static LinearLayout m230a(Context context, boolean z, int i, LinearLayout linearLayout) {
        View linearLayout2 = new LinearLayout(context);
        if (i != 0) {
            linearLayout2.setId(i);
        }
        linearLayout.addView(linearLayout2);
        linearLayout2.setGravity(17);
        linearLayout2.setOrientation(0);
        m241a(linearLayout2, z, context);
        m237a(linearLayout2, -1, "58dip");
        m248b(linearLayout2, null, null, null, "4dip");
        return linearLayout2;
    }

    public static LinearLayout m231a(ViewGroup viewGroup) {
        View linearLayout = new LinearLayout(viewGroup.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(cy.f249d);
        viewGroup.addView(linearLayout);
        m236a(linearLayout, -1, -2);
        return linearLayout;
    }

    public static RelativeLayout.LayoutParams m232a(int i, int i2, int i3, int i4) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(i3, i4);
        return layoutParams;
    }

    public static RelativeLayout.LayoutParams m233a(Context context, String str, String str2, int i) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(m224a(str, context), m224a(str2, context));
        layoutParams.addRule(i);
        return layoutParams;
    }

    public static void m234a(View view) {
        m248b(view, "4dip", null, "4dip", null);
    }

    public static void m235a(View view, int i, float f) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.gravity = i;
            layoutParams2.weight = f;
        }
    }

    public static void m236a(View view, int i, int i2) {
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
    }

    public static void m237a(View view, int i, String str) {
        m236a(view, i, m224a(str, view.getContext()));
    }

    public static void m238a(View view, String str, int i) {
        m236a(view, m224a(str, view.getContext()), -2);
    }

    public static void m239a(View view, String str, String str2) {
        m248b(view, "4dip", str, "4dip", str2);
    }

    public static void m240a(View view, String str, String str2, String str3, String str4) {
        Context context = view.getContext();
        view.setPadding(m224a(str, context), m224a(str2, context), m224a(str3, context), m224a(str4, context));
    }

    public static void m241a(View view, boolean z, Context context) {
        m236a(view, -1, -2);
        m240a(view, "10dip", "0dip", "10dip", "0dip");
        view.setBackground(z ? cy.m220a(context) : cy.m221b(context));
        view.setFocusable(true);
        view.setMinimumHeight(m224a("54dip", context));
        if (view instanceof TextView) {
            m243a((TextView) view);
        }
        if (!(view instanceof Button)) {
            view.setClickable(true);
        }
    }

    public static void m242a(Button button) {
        m244a((TextView) button, 17);
    }

    public static void m243a(TextView textView) {
        textView.setGravity(17);
        textView.setTextColor(-1);
        textView.setTextSize(20.0f);
        textView.setTypeface(cy.f258m);
    }

    public static void m244a(TextView textView, int i) {
        m240a(textView, "2dip", "2dip", "2dip", "2dip");
        textView.setTypeface(cy.f259n);
        textView.setTextColor(cy.f265t);
        textView.setBackground(cy.m222c(textView.getContext()));
        textView.setAutoLinkMask(15);
        textView.setTextSize(14.0f);
        textView.setTextColor(cy.f265t);
        textView.setGravity(i);
    }

    public static float m245b(String str, Context context) {
        if (str == null) {
            return 0.0f;
        }
        CharSequence toLowerCase = str.toLowerCase();
        if (f274c.containsKey(toLowerCase)) {
            return ((Float) f274c.get(toLowerCase)).floatValue();
        }
        Matcher matcher = f273b.matcher(toLowerCase);
        if (matcher.matches()) {
            float parseFloat = Float.parseFloat(matcher.group(1));
            Integer num = (Integer) f272a.get(matcher.group(3).toLowerCase());
            if (num == null) {
                num = Integer.valueOf(1);
            }
            float applyDimension = TypedValue.applyDimension(num.intValue(), parseFloat, context.getResources().getDisplayMetrics());
            f274c.put(toLowerCase, Float.valueOf(applyDimension));
            return applyDimension;
        }
        throw new NumberFormatException();
    }

    public static LinearLayout m246b(ViewGroup viewGroup) {
        View linearLayout = new LinearLayout(viewGroup.getContext());
        linearLayout.setOrientation(1);
        m240a(linearLayout, "10dip", "14dip", "10dip", "14dip");
        viewGroup.addView(linearLayout, m227a());
        return linearLayout;
    }

    public static void m247b(View view, String str, String str2) {
        Context context = view.getContext();
        m236a(view, m224a(str, context), m224a(str2, context));
    }

    public static void m248b(View view, String str, String str2, String str3, String str4) {
        Context context = view.getContext();
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).setMargins(m224a(str, context), m224a(str2, context), m224a(str3, context), m224a(str4, context));
        }
    }

    public static void m249b(TextView textView) {
        textView.setTextColor(cy.f256k);
        textView.setLinkTextColor(cy.f265t);
        textView.setTypeface(cy.f264s);
        textView.setTextSize(13.0f);
        textView.setSingleLine(false);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void m250b(TextView textView, int i) {
        textView.setTextSize(18.0f);
        textView.setTypeface(cy.f260o);
        textView.setSingleLine(true);
        textView.setGravity(i);
        textView.setTextColor(cy.f252g);
    }

    public static Bitmap m251c(String str, Context context) {
        return m225a(str, context, 240);
    }

    public static void m252c(TextView textView, int i) {
        textView.setTextSize(16.0f);
        textView.setTypeface(cy.f261p);
        textView.setSingleLine(true);
        textView.setGravity(83);
        textView.setTextColor(cy.f252g);
    }

    public static void m253d(TextView textView, int i) {
        textView.setTextSize(14.0f);
        textView.setTypeface(cy.f262q);
        textView.setSingleLine(true);
        textView.setGravity(i);
        textView.setTextColor(cy.f252g);
    }

    public static void m254e(TextView textView, int i) {
        textView.setTextSize(13.0f);
        textView.setTypeface(cy.f262q);
        textView.setSingleLine(true);
        textView.setGravity(83);
        textView.setTextColor(cy.f252g);
    }
}
