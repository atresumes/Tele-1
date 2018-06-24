package com.paypal.android.sdk;

import android.os.Build;
import com.payUMoney.sdk.SdkConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public final class fm extends fn {
    public ep f1127a;
    public boolean f1128b;
    public String f1129c;
    public String f1130d;
    public String f1131e;
    public long f1132f;
    private String f1133j;
    private final boolean f1134k;
    private final String f1135l;
    private String f1136m;
    private String f1137n;

    public fm(cx cxVar, C0439b c0439b, String str, String str2, ep epVar, String str3, String str4, boolean z, String str5, boolean z2, String str6) {
        this(cxVar, c0439b, str, str2, epVar, z, str5, z2, str6);
        this.f1136m = str3;
        this.f1137n = str4;
    }

    public fm(cx cxVar, C0439b c0439b, String str, String str2, ep epVar, boolean z, String str3, boolean z2, String str4) {
        super(df.LoginRequest, cxVar, c0439b, fn.m1071b(str, str2));
        this.f1127a = epVar;
        this.f1128b = z;
        this.f1133j = str3;
        this.f1134k = z2;
        this.f1135l = str4;
    }

    public final String mo2931b() {
        Map hashMap = new HashMap();
        hashMap.put("response_type", this.f1133j);
        if (this.f1133j != null && this.f1133j.equals(SdkConstants.TOKEN)) {
            hashMap.put("scope_consent_context", "access_token");
            if (!C0441d.m267c(this.f1135l)) {
                hashMap.put("scope", this.f1135l);
            }
        }
        hashMap.put("risk_data", C0441d.m257a(at.m75a().m100c().toString()));
        if (this.f1136m != null) {
            hashMap.put(SdkConstants.GRANT_TYPE, "urn:paypal:params:oauth2:grant_type:otp");
            hashMap.put("nonce", this.f1137n);
            JSONObject jSONObject = new JSONObject();
            jSONObject.accumulate("token_identifier", SdkConstants.OTP_STRING);
            jSONObject.accumulate("token_value", this.f1136m);
            hashMap.put("2fa_token_claims", fn.m1070a(jSONObject));
        } else if (this.f1127a.m340a()) {
            hashMap.put(SdkConstants.GRANT_TYPE, SdkConstants.PASSWORD);
            hashMap.put("email", C0441d.m257a(this.f1127a.m341b()));
            hashMap.put(SdkConstants.PASSWORD, C0441d.m257a(this.f1127a.m342c()));
        } else {
            hashMap.put(SdkConstants.GRANT_TYPE, SdkConstants.PASSWORD);
            this.f1127a.m343d().m362c();
            hashMap.put("phone", C0441d.m257a("+" + this.f1127a.m343d().m362c() + " " + this.f1127a.m343d().m359a()));
            hashMap.put("pin", this.f1127a.m344e());
        }
        hashMap.put("device_name", C0441d.m257a(Build.DEVICE));
        hashMap.put("redirect_uri", C0441d.m257a("urn:ietf:wg:oauth:2.0:oob"));
        return C0441d.m258a(hashMap);
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        try {
            m.getString("scope");
            this.f1131e = m.getString("scope");
            if (this.f1134k) {
                this.f1129c = m.getString("code");
                this.g = m.getString("nonce");
                return;
            }
            this.f1130d = m.getString("access_token");
            this.f1132f = m.getLong("expires_in");
        } catch (JSONException e) {
            m1072b(m);
        }
    }

    public final void mo2933d() {
        m1072b(m206m());
    }

    public final String mo2934e() {
        return "{ \"access_token\": \"mock_access_token\", \"code\": \"mock_code_EJhi9jOPswug9TDOv93qg4Y28xIlqPDpAoqd7biDLpeGCPvORHjP1Fh4CbFPgKMGCHejdDwe9w1uDWnjPCp1lkaFBjVmjvjpFtnr6z1YeBbmfZYqa9faQT_71dmgZhMIFVkbi4yO7hk0LBHXt_wtdsw\", \"scope\": \"https://api.paypal.com/v1/payments/.*\", \"nonce\": \"mock_nonce\", \"token_type\": \"Bearer\",\"expires_in\":28800,\"visitor_id\":\"zVxjDBTRRNfYXdOb19-lcTblxm-6bzXGvSlP76ZiHOudKaAvoxrW8Cg5pA6EjIPpiz4zlw\" }";
    }

    public final boolean m1085t() {
        return this.f1136m != null;
    }
}
