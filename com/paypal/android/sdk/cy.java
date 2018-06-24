package com.paypal.android.sdk;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import com.android.volley.toolbox.ImageRequest;

public final class cy {
    private static int f239A = f270y;
    private static int f240B = f246a;
    private static int f241C = Color.parseColor("#c5ddeb");
    private static int f242D = Color.parseColor("#717074");
    private static int f243E = Color.parseColor("#aa717074");
    private static int f244F = Color.parseColor("#5a5a5d");
    private static int f245G = Color.parseColor("#f5f5f5");
    public static final int f246a = Color.parseColor("#003087");
    public static final int f247b = Color.parseColor("#009CDE");
    public static final Drawable f248c = new ColorDrawable(Color.parseColor("#717074"));
    public static final int f249d = Color.parseColor("#f5f5f5");
    public static final int f250e = Color.parseColor("#c4dceb");
    public static final int f251f = Color.parseColor("#e5e5e5");
    public static final int f252g = Color.parseColor("#515151");
    public static final int f253h = Color.parseColor("#797979");
    public static final int f254i = f252g;
    public static final int f255j = f252g;
    public static final int f256k = f252g;
    public static final int f257l = f253h;
    public static final Typeface f258m = Typeface.create("sans-serif-light", 0);
    public static final Typeface f259n = Typeface.create("sans-serif-light", 0);
    public static final Typeface f260o = Typeface.create("sans-serif-bold", 0);
    public static final Typeface f261p = Typeface.create("sans-serif", 2);
    public static final Typeface f262q = Typeface.create("sans-serif-light", 0);
    public static final Typeface f263r = Typeface.create("sans-serif", 0);
    public static final Typeface f264s = Typeface.create("sans-serif-light", 0);
    public static final ColorStateList f265t = new ColorStateList(new int[][]{f266u, f267v}, new int[]{f240B, f271z});
    private static int[] f266u = new int[]{16842919, 16842910};
    private static int[] f267v = new int[]{16842910};
    private static int[] f268w = new int[]{-16842910};
    private static int[] f269x = new int[]{16842908};
    private static int f270y = Color.parseColor("#aa009CDE");
    private static int f271z = f247b;

    static {
        Color.parseColor("#aa003087");
        Color.parseColor("#333333");
        Color.parseColor("#b32317");
        Typeface.create("sans-serif-light", 0);
    }

    private static Drawable m218a(int i, float f) {
        Drawable[] drawableArr = new Drawable[2];
        drawableArr[0] = new ColorDrawable(i);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setStrokeWidth(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT * f);
        shapeDrawable.getPaint().setStyle(Style.STROKE);
        shapeDrawable.getPaint().setColor(f249d);
        drawableArr[1] = shapeDrawable;
        return new LayerDrawable(drawableArr);
    }

    private static Drawable m219a(int i, int i2, float f) {
        Drawable[] drawableArr = new Drawable[3];
        drawableArr[0] = new ColorDrawable(i);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setStrokeWidth(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT * f);
        shapeDrawable.getPaint().setStyle(Style.STROKE);
        shapeDrawable.getPaint().setColor(f249d);
        drawableArr[1] = shapeDrawable;
        shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setStrokeWidth(f);
        shapeDrawable.getPaint().setStyle(Style.STROKE);
        shapeDrawable.getPaint().setColor(i2);
        drawableArr[2] = shapeDrawable;
        return new LayerDrawable(drawableArr);
    }

    public static Drawable m220a(Context context) {
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(f266u, new ColorDrawable(f240B));
        stateListDrawable.addState(f268w, new ColorDrawable(f241C));
        stateListDrawable.addState(f269x, m219a(f271z, f239A, m223d(context)));
        stateListDrawable.addState(f267v, m218a(f271z, m223d(context)));
        return stateListDrawable;
    }

    public static Drawable m221b(Context context) {
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(f266u, new ColorDrawable(f244F));
        stateListDrawable.addState(f268w, new ColorDrawable(f245G));
        stateListDrawable.addState(f269x, m219a(f242D, f243E, m223d(context)));
        stateListDrawable.addState(f267v, m218a(f242D, m223d(context)));
        return stateListDrawable;
    }

    protected static Drawable m222c(Context context) {
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(f269x, m219a(0, f239A, m223d(context)));
        stateListDrawable.addState(f267v, new ColorDrawable(0));
        return stateListDrawable;
    }

    private static float m223d(Context context) {
        return context.getResources().getDisplayMetrics().density * (cz.m245b("4dip", context) / ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
    }
}
