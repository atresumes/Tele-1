package com.paypal.android.sdk;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class fh extends fp {
    private String f1190b;
    private String f1191c;
    private String f1192d;
    private int f1193e;
    private int f1194f;
    private String f1195g;
    private String f1196h;
    private String f1197i;
    private String f1198j;

    public fh(cx cxVar, C0439b c0439b, String str, String str2, String str3, String str4, er erVar, Map map, fo[] foVarArr, String str5, boolean z, String str6, String str7, String str8) {
        super(df.CreditCardPaymentRequest, cxVar, c0439b, str, str2, str4, erVar, map, foVarArr, str5, z, str6, str7, str8);
        this.f1195g = str3;
    }

    public fh(cx cxVar, C0439b c0439b, String str, String str2, String str3, String str4, String str5, int i, int i2, String str6, er erVar, Map map, fo[] foVarArr, String str7, boolean z, String str8, String str9, String str10) {
        super(df.CreditCardPaymentRequest, cxVar, c0439b, str, str2, null, erVar, map, foVarArr, str7, z, str8, str9, str10);
        this.f1190b = str3;
        this.f1191c = str4;
        this.f1192d = str5;
        this.f1193e = i;
        this.f1194f = i2;
    }

    final void mo3135a(JSONObject jSONObject) {
        if (C0441d.m269d(this.f1196h)) {
            jSONObject.accumulate("invoice_number", this.f1196h);
        }
        if (C0441d.m269d(this.f1197i)) {
            jSONObject.accumulate("custom", this.f1197i);
        }
        if (C0441d.m269d(this.f1198j)) {
            jSONObject.accumulate("soft_descriptor", this.f1198j);
        }
    }

    public final fh m1148d(String str) {
        this.f1196h = str;
        return this;
    }

    public final fh m1149e(String str) {
        this.f1197i = str;
        return this;
    }

    public final String mo2934e() {
        String a = ek.m333a(m1125B().m345a().doubleValue(), m1125B().m346b());
        return "{\"id\":\"PAY-6RV70583SB702805EKEYSZ6Y\",\"intent\":\"sale\",\"create_time\": \"2014-02-12T22:29:49Z\",\"update_time\": \"2014-02-12T22:29:50Z\",\"payer\":{\"funding_instruments\":[{\"credit_card\":{\"expire_month\":\"" + this.f1193e + "\",\"expire_year\":\"" + this.f1194f + "\",\"number\":\"" + (this.f1190b != null ? this.f1191c.substring(this.f1191c.length() - 4) : "xxxxxxxxxx1111") + "\",\"type\":\"VISA\"}" + "}]," + "\"payment_method\":\"credit_card\"}," + "\"state\":\"approved\",\"transactions\":" + "[{" + "\"amount\":{\"currency\":\"USD\"," + "\"total\":\"" + a + "\"},\"description\":\"I am a sanity check payment.\"," + "\"item_list\":{},\"payee\":" + "{\"merchant_id\":\"PKKPTJKL75YDS\"},\"related_resources\":" + "[{\"sale\":{\"amount\":{\"currency\":\"USD\",\"total\":\"" + a + "\"},\"id\":\"0EW02334X44816642\"," + "\"parent_payment\":\"PAY-123456789012345689\",\"state\":\"completed\"}}]}]}";
    }

    public final fh m1151f(String str) {
        this.f1198j = str;
        return this;
    }

    public final String m1152t() {
        return this.f1191c;
    }

    public final String m1153u() {
        return this.f1190b;
    }

    public final String m1154v() {
        return this.f1192d;
    }

    public final int m1155w() {
        return this.f1193e;
    }

    public final int m1156x() {
        return this.f1194f;
    }

    protected final JSONArray mo3136y() {
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject;
        JSONObject jSONObject2;
        if (this.f1190b != null) {
            jSONObject = new JSONObject();
            jSONObject.accumulate("cvv2", this.f1192d);
            jSONObject.accumulate("expire_month", Integer.valueOf(this.f1193e));
            jSONObject.accumulate("expire_year", Integer.valueOf(this.f1194f));
            jSONObject.accumulate("number", this.f1191c);
            jSONObject.accumulate("type", this.f1190b);
            jSONObject2 = new JSONObject();
            jSONObject2.accumulate("credit_card", jSONObject);
            jSONArray.put(jSONObject2);
        } else {
            jSONObject = new JSONObject();
            jSONObject.accumulate("payer_id", this.f1170a);
            jSONObject.accumulate("credit_card_id", this.f1195g);
            jSONObject2 = new JSONObject();
            jSONObject2.accumulate("credit_card_token", jSONObject);
            jSONArray.put(jSONObject2);
        }
        return jSONArray;
    }

    protected final String mo3137z() {
        return "credit_card";
    }
}
