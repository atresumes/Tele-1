package com.paypal.android.sdk;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.payments.PayPalPayment;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class fp extends fq {
    public String f1170a;
    private er f1171b;
    private Map f1172c;
    private fo[] f1173d;
    private String f1174e;
    private boolean f1175f;
    private String f1176g;
    private String f1177h;
    private String f1178i;
    private String f1179j;
    private String f1180k;

    public fp(df dfVar, cx cxVar, C0439b c0439b, String str, String str2, String str3, er erVar, Map map, fo[] foVarArr, String str4, boolean z, String str5, String str6, String str7) {
        super(dfVar, cxVar, c0439b, str);
        this.f1170a = str3;
        this.f1171b = erVar;
        this.f1172c = map;
        this.f1173d = foVarArr;
        this.f1174e = str4;
        this.f1175f = z;
        this.f1176g = str7;
        if (C0441d.m267c(this.f1176g)) {
            this.f1176g = PayPalPayment.PAYMENT_INTENT_SALE;
        }
        this.f1176g = this.f1176g.toLowerCase(Locale.US);
        m191a("PayPal-Request-Id", str2);
        if (C0441d.m269d(str5)) {
            m191a("PayPal-Partner-Attribution-Id", str5);
        }
        if (C0441d.m269d(str6)) {
            m191a("PayPal-Client-Metadata-Id", str6);
        }
    }

    private static String m1123a(JSONArray jSONArray) {
        String str = null;
        if (jSONArray != null) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(0);
                if (jSONObject != null) {
                    JSONArray jSONArray2 = jSONObject.getJSONArray("related_resources");
                    if (jSONArray2 != null) {
                        jSONObject = jSONArray2.getJSONObject(0);
                        if (jSONObject != null) {
                            jSONObject = jSONObject.getJSONObject("authorization");
                            if (jSONObject != null) {
                                str = jSONObject.optString("id");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
            }
        }
        return str;
    }

    public final boolean m1124A() {
        return this.f1175f;
    }

    protected final er m1125B() {
        return this.f1171b;
    }

    public final String m1126C() {
        return this.f1177h;
    }

    public final String m1127D() {
        return this.f1178i;
    }

    public final String m1128E() {
        return this.f1176g;
    }

    public final String m1129F() {
        return this.f1179j;
    }

    public final String m1130G() {
        return this.f1180k;
    }

    abstract void mo3135a(JSONObject jSONObject);

    public final String mo2931b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("intent", this.f1176g);
        JSONObject jSONObject2 = new JSONObject();
        JSONArray y = mo3136y();
        if (y != null) {
            jSONObject2.accumulate("funding_instruments", y);
        }
        jSONObject2.accumulate("payment_method", mo3137z());
        jSONObject.accumulate("payer", jSONObject2);
        er erVar = this.f1171b;
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.accumulate(Param.CURRENCY, erVar.m346b().getCurrencyCode());
        jSONObject3.accumulate("total", erVar.m345a().toPlainString());
        if (!(this.f1172c == null || this.f1172c.isEmpty())) {
            Object obj;
            String str = "details";
            if (this.f1172c == null || this.f1172c.isEmpty()) {
                obj = null;
            } else {
                obj = new JSONObject();
                if (this.f1172c.containsKey(Param.SHIPPING)) {
                    obj.accumulate(Param.SHIPPING, this.f1172c.get(Param.SHIPPING));
                }
                if (this.f1172c.containsKey("subtotal")) {
                    obj.accumulate("subtotal", this.f1172c.get("subtotal"));
                }
                if (this.f1172c.containsKey(Param.TAX)) {
                    obj.accumulate(Param.TAX, this.f1172c.get(Param.TAX));
                }
            }
            jSONObject3.accumulate(str, obj);
        }
        jSONObject2 = new JSONObject();
        jSONObject2.accumulate(SdkConstants.AMOUNT, jSONObject3);
        jSONObject2.accumulate("description", this.f1174e);
        if (this.f1173d != null && this.f1173d.length > 0) {
            jSONObject3 = new JSONObject();
            jSONObject3.accumulate("items", fo.m368a(this.f1173d));
            jSONObject2.accumulate("item_list", jSONObject3);
        }
        y = new JSONArray();
        y.put(jSONObject2);
        mo3135a(jSONObject2);
        jSONObject.accumulate("transactions", y);
        return jSONObject.toString();
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        try {
            this.f1177h = m.getString("state");
            this.f1178i = m.optString("id");
            this.f1179j = m.optString("create_time");
            this.f1180k = m1123a(m.getJSONArray("transactions"));
        } catch (JSONException e) {
            mo2933d();
        }
    }

    public final void mo2933d() {
        m1086b(m206m());
    }

    protected JSONArray mo3136y() {
        return null;
    }

    protected abstract String mo3137z();
}
