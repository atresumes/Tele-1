package com.paypal.android.sdk;

import android.util.Base64;
import com.payu.custombrowser.util.CBConstant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class fn extends cv {
    private static final String f1118a = fn.class.getSimpleName();
    public String f1119g;
    public boolean f1120h;
    public Map f1121i;

    public fn(df dfVar, cx cxVar, C0439b c0439b, String str) {
        super(new dc(dfVar), cxVar, c0439b, str);
        m191a("Accept", "application/json; charset=utf-8");
        m191a("Accept-Language", "en_US");
        m191a("Content-Type", CBConstant.HTTP_URLENCODED);
    }

    protected static String m1070a(JSONObject jSONObject) {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(jSONObject);
        return C0441d.m257a(jSONArray.toString());
    }

    protected static String m1071b(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder("Basic ");
        String str3 = (bp.m160a(str) && str2 == null) ? "mock:" : new String(Base64.encode(str2.getBytes(), 2)) + ":";
        return stringBuilder.append(str3).toString();
    }

    protected final void m1072b(JSONObject jSONObject) {
        String string = jSONObject.getString("error");
        String optString = jSONObject.optString("error_description");
        if (jSONObject.has("nonce")) {
            this.f1119g = jSONObject.getString("nonce");
        }
        if (jSONObject.has("2fa_enabled") && jSONObject.getBoolean("2fa_enabled")) {
            this.f1120h = true;
            this.f1121i = new LinkedHashMap();
            if (jSONObject.has("2fa_token_identifier")) {
                JSONArray jSONArray = jSONObject.getJSONArray("2fa_token_identifier");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    String string2 = jSONObject2.getString("type");
                    String string3 = jSONObject2.getString("token_identifier");
                    String string4 = jSONObject2.getString("token_identifier_display");
                    if ("sms_otp".equals(string2)) {
                        this.f1121i.put(string3, string4);
                        new StringBuilder("adding token [").append(string3).append(",").append(string4).append("]");
                    } else {
                        new StringBuilder("skipping token [").append(string3).append(",").append(string4).append("], as the type is not supported:").append(string2);
                    }
                }
            }
        }
        m192a(string, optString, null);
    }
}
