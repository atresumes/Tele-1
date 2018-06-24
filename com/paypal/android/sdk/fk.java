package com.paypal.android.sdk;

import com.payUMoney.sdk.walledSdk.SharedPrefsUtils.Keys;
import com.payu.custombrowser.util.CBConstant;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class fk extends cv {
    private static final String f1113a = fk.class.getSimpleName();
    private Map f1114b;
    private String f1115c;
    private String f1116d;
    private String f1117e;

    public fk(cx cxVar, C0439b c0439b, String str, String str2, String str3) {
        cu dcVar = new dc(df.GetAppInfoRequest);
        StringBuilder stringBuilder = new StringBuilder("Bearer ");
        if (bp.m160a(str) && str2 == null) {
            str2 = "mock_euat";
        }
        super(dcVar, cxVar, c0439b, stringBuilder.append(str2).toString(), "/" + str3);
        this.f1114b = new HashMap();
        m191a("Content-Type", CBConstant.HTTP_URLENCODED);
    }

    private static void m1061a(JSONArray jSONArray, Map map) {
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    CharSequence optString = jSONObject.optString("name");
                    CharSequence optString2 = jSONObject.optString("value");
                    if (C0441d.m269d(optString) && C0441d.m269d(optString2)) {
                        map.put(optString, optString2);
                    }
                }
            }
        }
    }

    public final String mo2931b() {
        return "";
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        JSONArray optJSONArray = m.optJSONArray("capabilities");
        if (optJSONArray != null) {
            JSONObject jSONObject;
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                if (jSONObject2 != null && "PAYPAL_ACCESS".equals(jSONObject2.optString("name"))) {
                    jSONObject = jSONObject2;
                    break;
                }
            }
            jSONObject = null;
            if (jSONObject != null) {
                m1061a(jSONObject.optJSONArray("attributes"), this.f1114b);
                new StringBuilder("Attributes: ").append(this.f1114b.toString());
            }
        }
        JSONArray optJSONArray2 = m.optJSONArray("attributes");
        if (optJSONArray2 != null) {
            Map hashMap = new HashMap();
            m1061a(optJSONArray2, hashMap);
            this.f1115c = (String) hashMap.get("privacy_policy_url");
            this.f1116d = (String) hashMap.get("user_agreement_url");
            this.f1117e = (String) hashMap.get(Keys.DISPLAY_NAME);
        }
    }

    public final void mo2933d() {
    }

    public final String mo2934e() {
        return " {\n     \"attributes\": [\n         {\n             \"name\": \"display_name\",\n             \"value\": \"Example Merchant\"\n         },\n         {\n             \"name\": \"privacy_policy_url\",\n             \"value\": \"http://www.example.com/privacy-policy\"\n         },\n         {\n             \"name\": \"user_agreement_url\",\n             \"value\": \"http://www.example.com/user-agreement\"\n         }\n     ],\n     \"name\": \"LiveTestApp\",\n     \"capabilities\": [\n         {\n             \"scopes\": [],\n             \"name\": \"PAYPAL_ACCESS\",\n             \"attributes\": [\n                 {\n                     \"name\": \"openid_connect\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_date_of_birth\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_fullname\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_gender\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_zip\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_language\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_city\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_country\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_timezone\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_email\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_street_address1\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_street_address2\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_phone_number\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_locale\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_state\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_age_range\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_account_verified\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_account_creation_date\",\n                     \"value\": \"Y\"\n                 },\n                 {\n                     \"name\": \"oauth_account_type\",\n                     \"value\": \"Y\"\n                 }\n             ]\n         },\n         {\n             \"scopes\": [\n                 \"https://api.paypal.com/v1/payments/.*\",\n                 \"https://api.paypal.com/v1/vault/credit-card\",\n                 \"https://api.paypal.com/v1/vault/credit-card/.*\"\n             ],\n             \"name\": \"PAYMENT\",\n             \"features\": [\n                 {\n                     \"status\": \"ACTIVE\",\n                     \"name\": \"ACCEPT_CARD\"\n                 },\n                 {\n                     \"status\": \"ACTIVE\",\n                     \"name\": \"ACCEPT_PAYPAL\"\n                 }\n             ]\n         }\n     ]\n }    ";
    }

    public final Map m1066t() {
        return this.f1114b;
    }

    public final String m1067u() {
        return this.f1115c;
    }

    public final String m1068v() {
        return this.f1116d;
    }

    public final String m1069w() {
        return this.f1117e;
    }
}
