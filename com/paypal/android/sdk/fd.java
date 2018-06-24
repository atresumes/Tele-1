package com.paypal.android.sdk;

import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.payments.PayPalPayment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class fd extends fq {
    private final String f1144a;
    private final String f1145b;
    private final boolean f1146c;
    private final JSONObject f1147d;
    private final JSONObject f1148e;
    private JSONObject f1149f;
    private String f1150g;
    private String f1151h;
    private String f1152i;
    private String f1153j;

    public fd(cx cxVar, C0439b c0439b, String str, String str2, boolean z, String str3, String str4, String str5, String str6, JSONObject jSONObject, JSONObject jSONObject2) {
        super(df.ApproveAndExecuteSfoPaymentRequest, cxVar, c0439b, str);
        this.f1146c = z;
        this.f1144a = str5;
        this.f1145b = str6;
        this.f1147d = jSONObject;
        this.f1148e = jSONObject2;
        m191a("PayPal-Request-Id", str2);
        if (C0441d.m269d(str3)) {
            m191a("PayPal-Partner-Attribution-Id", str3);
        }
        if (C0441d.m269d(str4)) {
            m191a("PayPal-Client-Metadata-Id", str4);
        }
    }

    private static String m1094a(JSONArray jSONArray) {
        if (jSONArray == null) {
            return null;
        }
        try {
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            if (jSONObject == null) {
                return null;
            }
            JSONArray jSONArray2 = jSONObject.getJSONArray("related_resources");
            if (jSONArray2 == null) {
                return null;
            }
            jSONObject = jSONArray2.getJSONObject(0);
            if (jSONObject == null) {
                return null;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject("authorization");
            if (optJSONObject != null) {
                return optJSONObject.optString("id");
            }
            jSONObject = jSONObject.optJSONObject(PayPalPayment.PAYMENT_INTENT_ORDER);
            return jSONObject != null ? jSONObject.optString("id") : null;
        } catch (JSONException e) {
            return null;
        }
    }

    public final String mo2931b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("payment_id", this.f1144a);
        jSONObject.accumulate(SdkConstants.LAST_SESSION_ID, this.f1145b);
        if (this.f1148e != null) {
            jSONObject.accumulate("funding_option", this.f1148e);
        }
        if (this.f1147d != null) {
            jSONObject.accumulate("shipping_address", this.f1147d);
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.accumulate("device_info", C0441d.m257a(em.m338a().toString()));
        jSONObject2.accumulate("app_info", C0441d.m257a(eh.m328a().toString()));
        jSONObject2.accumulate("risk_data", C0441d.m257a(at.m75a().m100c().toString()));
        jSONObject.accumulate("client_info", jSONObject2);
        return jSONObject.toString();
    }

    public final void mo2932c() {
        try {
            this.f1149f = m206m().getJSONObject(SdkConstants.PAYMENT);
            this.f1150g = this.f1149f.optString("state");
            this.f1151h = this.f1149f.optString("create_time");
            this.f1152i = this.f1149f.optString("intent");
            this.f1153j = m1094a(this.f1149f.getJSONArray("transactions"));
        } catch (JSONException e) {
            mo2933d();
        }
    }

    public final void mo2933d() {
        m1086b(m206m());
    }

    public final String mo2934e() {
        return "{     \"payment\": {         \"id\": \"PAY-6PU626847B294842SKPEWXHY\",         \"transactions\": [             {                 \"amount\": {                     \"total\": \"2.85\",                     \"details\": {                         \"subtotal\": \"2.85\"                     },                     \"currency\": \"USD\"                 },                 \"description\": \"Awesome Sauce\",                 \"related_resources\": [                     {                         \"sale\": {                             \"amount\": {                                 \"total\": \"2.85\",                                 \"currency\": \"USD\"                             },                             \"id\": \"5LR21373K59921925\",                             \"parent_payment\": \"PAY-6PU626847B294842SKPEWXHY\",                             \"update_time\": \"2014-07-18T18:47:06Z\",                             \"state\": \"completed\",                             \"create_time\": \"2014-07-18T18:46:55Z\",                             \"links\": [                                 {                                     \"method\": \"GET\",                                     \"rel\": \"self\",                                     \"href\": \"https://www.stage2std019.stage.\"                                 },                                 {                                     \"method\": \"POST\",                                     \"rel\": \"refund\",                                     \"href\": \"https://www.stage2std019.stage. \"                                 },                                 {                                     \"method\": \"GET\",                                     \"rel\": \"parent_payment\",                                     \"href\": \"https://www.stage2std019.stage.PEWXHY \"                                 }                             ]                         }                     }                 ]             }         ],         \"update_time\": \"2014-07-18T18:47:06Z\",         \"payer\": {             \"payer_info\": {                 \"shipping_address\": {                                      }             },             \"payment_method\": \"paypal\"         },         \"state\": \"approved\",         \"create_time\": \"2014-07-18T18:46:55Z\",         \"links\": [             {                 \"method\": \"GET\",                 \"rel\": \"self\",                 \"href\": \"https://www.stage2std019.stage.paypal.\"             }         ],         \"intent\": \"sale\"     } } ";
    }

    public final void mo3110l() {
        at.m75a().m103g();
    }

    public final String m1100t() {
        return this.f1144a;
    }

    public final boolean m1101u() {
        return this.f1146c;
    }

    public final String m1102v() {
        return this.f1150g;
    }

    public final String m1103w() {
        return this.f1151h;
    }

    public final String m1104x() {
        return this.f1152i;
    }

    public final String m1105y() {
        return this.f1153j;
    }
}
