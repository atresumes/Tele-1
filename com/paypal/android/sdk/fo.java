package com.paypal.android.sdk;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONObject;

public class fo {
    private String f414a;
    private Integer f415b;
    private BigDecimal f416c;
    private String f417d;
    private String f418e;

    static {
        fo.class.getSimpleName();
    }

    public fo(String str, Integer num, BigDecimal bigDecimal, String str2, String str3) {
        this.f414a = str;
        this.f415b = num;
        this.f416c = bigDecimal;
        this.f417d = str2;
        this.f418e = str3;
    }

    public static JSONArray m368a(fo[] foVarArr) {
        if (foVarArr == null) {
            return null;
        }
        JSONArray jSONArray = new JSONArray();
        for (fo foVar : foVarArr) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.accumulate(Param.QUANTITY, Integer.toString(foVar.f415b.intValue()));
            jSONObject.accumulate("name", foVar.f414a);
            jSONObject.accumulate(Param.PRICE, foVar.f416c.toString());
            jSONObject.accumulate(Param.CURRENCY, foVar.f417d);
            jSONObject.accumulate("sku", foVar.f418e);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }
}
