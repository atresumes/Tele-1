package com.paypal.android.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class fq extends fe {
    public fq(df dfVar, cx cxVar, C0439b c0439b, String str) {
        super(dfVar, cxVar, c0439b, str);
    }

    protected final void m1086b(JSONObject jSONObject) {
        String string;
        String string2;
        String string3 = jSONObject.getString("name");
        String string4 = jSONObject.getString("message");
        try {
            JSONObject jSONObject2 = jSONObject.getJSONArray("details").getJSONObject(0);
            string = jSONObject2.getString("field");
            string2 = jSONObject2.getString("issue");
            if (string3.equals("VALIDATION_ERROR") && string.contains("amount.currency")) {
                string3 = "pp_service_error_bad_currency";
            }
            String str = string2;
            string2 = string;
            string = string3;
            string3 = str;
        } catch (JSONException e) {
            string2 = "";
            string = string3;
            string3 = "";
        }
        m192a(string, string4, "field:" + string2 + ", issue:" + string3);
    }
}
