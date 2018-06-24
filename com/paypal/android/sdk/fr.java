package com.paypal.android.sdk;

import com.payUMoney.sdk.SdkConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class fr extends fn {
    public String f1138a;
    public String f1139b;
    public long f1140c;
    public boolean f1141d;

    public fr(String str, cx cxVar, C0439b c0439b, String str2) {
        super(df.PreAuthRequest, cxVar, c0439b, fn.m1071b(str, str2));
    }

    public final String mo2931b() {
        Map hashMap = new HashMap();
        hashMap.put("response_type", SdkConstants.TOKEN);
        hashMap.put(SdkConstants.GRANT_TYPE, "client_credentials");
        hashMap.put("return_authn_schemes", "true");
        hashMap.put("device_info", C0441d.m257a(em.m338a().toString()));
        hashMap.put("app_info", C0441d.m257a(eh.m328a().toString()));
        hashMap.put("risk_data", C0441d.m257a(at.m75a().m100c().toString()));
        return C0441d.m258a(hashMap);
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        try {
            this.f1138a = m.getString("access_token");
            this.f1139b = m.getString("scope");
            this.f1140c = m.getLong("expires_in");
            JSONArray jSONArray = m.getJSONArray("supported_authn_schemes");
            for (int i = 0; i < jSONArray.length(); i++) {
                if (jSONArray.get(i).equals("phone_pin")) {
                    this.f1141d = true;
                }
            }
        } catch (JSONException e) {
            m1072b(m);
        }
    }

    public final void mo2933d() {
        m1072b(m206m());
    }

    public final String mo2934e() {
        return "{\"scope\":\"https://api.paypal.com/v1/payments/.* https://api.paypal.com/v1/vault/credit-card https://api.paypal.com/v1/vault/credit-card/.* openid\",\"access_token\":\"iPVzWUwTo1fEG6n.2sTZCahv8wa2b8uGpBs1KZ-6XxA\",\"token_type\":\"Bearer\",\"expires_in\":900,\"supported_authn_schemes\": [ \"email_password\", \"phone_pin\" ]}";
    }
}
