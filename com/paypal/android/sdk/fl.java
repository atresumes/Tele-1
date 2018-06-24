package com.paypal.android.sdk;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public final class fl extends fn {
    private final String f1125a;
    private final String f1126b;

    public fl(cx cxVar, C0439b c0439b, String str, String str2, String str3, String str4) {
        super(df.LoginChallengeRequest, cxVar, c0439b, fn.m1071b(str, str2));
        this.f1125a = str3;
        this.f1126b = str4;
    }

    public final String mo2931b() {
        Map hashMap = new HashMap();
        hashMap.put("authn_schemes", "2fa_pre_login");
        hashMap.put("nonce", this.f1125a);
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("authn_scheme", "security_key_sms_token");
        jSONObject.accumulate("token_identifier", this.f1126b);
        hashMap.put("2fa_token_identifiers", fn.m1070a(jSONObject));
        return C0441d.m258a(hashMap);
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        try {
            this.g = m.getString("nonce");
        } catch (JSONException e) {
            m1072b(m);
        }
    }

    public final void mo2933d() {
        m1072b(m206m());
    }

    public final String mo2934e() {
        return "{\"nonce\": \"mock-login-challenge-nonce\"}";
    }
}
