package com.paypal.android.sdk;

import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class em {
    private static final HashMap f359a = new en();
    private static Map f360b;

    static {
        em.class.getSimpleName();
    }

    public static JSONObject m338a() {
        JSONObject jSONObject = new JSONObject();
        try {
            for (String str : f360b.keySet()) {
                jSONObject.put(str, f360b.get(str));
            }
            return jSONObject;
        } catch (Throwable e) {
            Log.e("paypal.sdk", "Error encoding JSON", e);
            return null;
        }
    }

    public static void m339a(C0438a c0438a) {
        if (f360b == null) {
            Map hashMap = new HashMap();
            f360b = hashMap;
            hashMap.put("device_identifier", C0441d.m257a(c0438a.m35e()));
            f360b.put("device_type", SdkConstants.OS_NAME_VALUE);
            f360b.put("device_name", C0441d.m257a(Build.DEVICE));
            f360b.put(CBAnalyticsConstant.DEVICE_MODEL, C0441d.m257a(Build.MODEL));
            Map map = f360b;
            String str = "device_key_type";
            Object obj = (String) f359a.get(Integer.valueOf(c0438a.m30b()));
            if (TextUtils.isEmpty(obj)) {
                obj = "ANDROIDGSM_UNDEFINED";
            }
            map.put(str, obj);
            f360b.put("device_os", SdkConstants.OS_NAME_VALUE);
            f360b.put(CBAnalyticsConstant.DEVICE_OS_VERSION, C0441d.m257a(VERSION.RELEASE));
            map = f360b;
            str = "is_device_simulator";
            boolean z = Build.PRODUCT.equals("sdk") || Build.PRODUCT.equals("google_sdk") || Build.FINGERPRINT.contains("generic");
            map.put(str, Boolean.toString(z));
        }
    }
}
