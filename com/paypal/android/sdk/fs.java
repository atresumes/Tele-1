package com.paypal.android.sdk;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public final class fs extends fq {
    public final String f1181a;
    private final String f1182b;
    private final String f1183c;
    private final String f1184d;
    private final int f1185e;
    private final int f1186f;
    private String f1187g;
    private String f1188h;
    private Date f1189i;

    public fs(cx cxVar, C0439b c0439b, String str, String str2, String str3, String str4, String str5, int i, int i2) {
        super(df.TokenizeCreditCardRequest, cxVar, c0439b, str);
        this.f1181a = str2;
        this.f1182b = str3;
        if (str4 == null) {
            throw new RuntimeException("cardNumber should not be null.  If it is, then you're probably trying to tokenize a card that's already been tokenized.");
        }
        this.f1183c = str4;
        this.f1184d = str5;
        this.f1185e = i;
        this.f1186f = i2;
    }

    public final String mo2931b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("payer_id", this.f1181a);
        jSONObject.accumulate("cvv2", this.f1184d);
        jSONObject.accumulate("expire_month", Integer.valueOf(this.f1185e));
        jSONObject.accumulate("expire_year", Integer.valueOf(this.f1186f));
        jSONObject.accumulate("number", this.f1183c);
        jSONObject.accumulate("type", this.f1182b);
        return jSONObject.toString();
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        try {
            this.f1187g = m.getString("id");
            String string = m.getString("number");
            if (this.f1188h == null || !this.f1188h.endsWith(string.substring(string.length() - 4))) {
                this.f1188h = string;
            }
            this.f1189i = ex.m363a(m.getString("valid_until"));
        } catch (JSONException e) {
            mo2933d();
        }
    }

    public final void mo2933d() {
        m1086b(m206m());
    }

    public final String mo2934e() {
        return "{\"id\":\"CARD-50Y58962PH1899901KFFBSDA\",\"valid_until\":\"2016-03-19T00:00:00.000Z\",\"state\":\"ok\",\"type\":\"visa\",\"number\":\"xxxxxxxxxxxx" + this.f1183c.substring(this.f1183c.length() - 4) + "\",\"expire_month\":\"" + this.f1185e + "\",\"expire_year\":\"" + this.f1186f + "\",\"links\":[" + "{\"href\":\"https://api.sandbox.paypal.com/v1/vault/credit-card/CARD-50Y58962PH1899901KFFBSDA\"," + "\"rel\":\"self\",\"method\":\"GET\"" + "}]" + "}";
    }

    public final String m1141t() {
        return this.f1187g;
    }

    public final String m1142u() {
        return this.f1188h;
    }

    public final Date m1143v() {
        return this.f1189i;
    }

    public final String m1144w() {
        return this.f1182b;
    }

    public final int m1145x() {
        return this.f1185e;
    }

    public final int m1146y() {
        return this.f1186f;
    }
}
