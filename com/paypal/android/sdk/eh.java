package com.paypal.android.sdk;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class eh {
    private static Map f351a;

    static {
        eh.class.getSimpleName();
    }

    private static String m327a(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 128).versionName;
        } catch (NameNotFoundException e) {
            return "unknown versionName";
        }
    }

    public static JSONObject m328a() {
        JSONObject jSONObject = new JSONObject();
        try {
            for (String str : f351a.keySet()) {
                jSONObject.put(str, f351a.get(str));
            }
            return jSONObject;
        } catch (Throwable e) {
            Log.e("paypal.sdk", "Error encoding JSON", e);
            return null;
        }
    }

    public static void m329a(C0438a c0438a) {
        if (f351a == null) {
            Map hashMap = new HashMap();
            f351a = hashMap;
            hashMap.put("app_version", m327a(c0438a.m36f()));
            f351a.put("app_category", "1");
            if (c0438a.m30b() == 1) {
                f351a.put("client_platform", "AndroidGSM");
            } else if (c0438a.m30b() == 2) {
                f351a.put("client_platform", "AndroidCDMA");
            } else {
                f351a.put("client_platform", "AndroidOther");
            }
            f351a.put("device_app_id", c0438a.m32c());
        }
    }
}
