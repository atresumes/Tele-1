package io.card.payment.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtil {
    private static final Map<String, Integer> DIMENSION_STRING_CONSTANT = initDimensionStringConstantMap();
    static Pattern DIMENSION_VALUE_PATTERN = Pattern.compile("^\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$");
    static HashMap<String, Float> pxDimensionLookupTable = new HashMap();

    @TargetApi(16)
    public static void setBackground(View view, Drawable drawable) {
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    static Map<String, Integer> initDimensionStringConstantMap() {
        Map<String, Integer> m = new HashMap();
        m.put("px", Integer.valueOf(0));
        m.put("dip", Integer.valueOf(1));
        m.put("dp", Integer.valueOf(1));
        m.put("sp", Integer.valueOf(2));
        m.put("pt", Integer.valueOf(3));
        m.put("in", Integer.valueOf(4));
        m.put("mm", Integer.valueOf(5));
        return Collections.unmodifiableMap(m);
    }

    public static int typedDimensionValueToPixelsInt(String dimensionValueString, Context context) {
        if (dimensionValueString == null) {
            return 0;
        }
        return (int) typedDimensionValueToPixels(dimensionValueString, context);
    }

    @SuppressLint({"DefaultLocale"})
    public static float typedDimensionValueToPixels(String dimensionValueString, Context context) {
        if (dimensionValueString == null) {
            return 0.0f;
        }
        dimensionValueString = dimensionValueString.toLowerCase();
        if (pxDimensionLookupTable.containsKey(dimensionValueString)) {
            return ((Float) pxDimensionLookupTable.get(dimensionValueString)).floatValue();
        }
        Matcher m = DIMENSION_VALUE_PATTERN.matcher(dimensionValueString);
        if (m.matches()) {
            float value = Float.parseFloat(m.group(1));
            Integer unit = (Integer) DIMENSION_STRING_CONSTANT.get(m.group(3).toLowerCase());
            if (unit == null) {
                unit = Integer.valueOf(1);
            }
            float ret = TypedValue.applyDimension(unit.intValue(), value, context.getResources().getDisplayMetrics());
            pxDimensionLookupTable.put(dimensionValueString, Float.valueOf(ret));
            return ret;
        }
        throw new NumberFormatException();
    }

    public static void setPadding(View view, String left, String top, String right, String bottom) {
        Context context = view.getContext();
        view.setPadding(typedDimensionValueToPixelsInt(left, context), typedDimensionValueToPixelsInt(top, context), typedDimensionValueToPixelsInt(right, context), typedDimensionValueToPixelsInt(bottom, context));
    }

    public static void setMargins(View view, String left, String top, String right, String bottom) {
        Context context = view.getContext();
        LayoutParams params = view.getLayoutParams();
        if (params instanceof MarginLayoutParams) {
            ((MarginLayoutParams) params).setMargins(typedDimensionValueToPixelsInt(left, context), typedDimensionValueToPixelsInt(top, context), typedDimensionValueToPixelsInt(right, context), typedDimensionValueToPixelsInt(bottom, context));
        }
    }

    public static void setDimensions(View view, int width, int height) {
        LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
    }

    public static void styleAsButton(Button button, boolean primary, Context context, boolean useApplicationTheme) {
        setDimensions(button, -1, -2);
        button.setFocusable(true);
        setPadding(button, "10dip", "0dip", "10dip", "0dip");
        if (!useApplicationTheme) {
            Drawable buttonBackgroundPrimary;
            if (primary) {
                buttonBackgroundPrimary = Appearance.buttonBackgroundPrimary(context);
            } else {
                buttonBackgroundPrimary = Appearance.buttonBackgroundSecondary(context);
            }
            setBackground(button, buttonBackgroundPrimary);
            button.setGravity(17);
            button.setMinimumHeight(typedDimensionValueToPixelsInt("54dip", context));
            button.setTextColor(-1);
            button.setTextSize(20.0f);
            button.setTypeface(Appearance.TYPEFACE_BUTTON);
        }
    }

    public static Bitmap base64ToBitmap(String base64Data, Context context) {
        return base64ToBitmap(base64Data, context, 240);
    }

    public static Bitmap base64ToBitmap(String base64Data, Context context, int displayMetricsDensity) {
        Options options = new Options();
        if (context != null) {
            options.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        } else {
            options.inTargetDensity = 160;
        }
        options.inDensity = displayMetricsDensity;
        options.inScaled = false;
        byte[] imageBytes = Base64.decode(base64Data, 0);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
    }
}
