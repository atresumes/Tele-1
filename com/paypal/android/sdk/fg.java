package com.paypal.android.sdk;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.payments.PayPalPayment;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class fg extends fq {
    private String f1154a;
    private String f1155b;
    private JSONArray f1156c;
    private JSONObject f1157d;
    private JSONArray f1158e;
    private er f1159f;
    private Map f1160g;
    private fo[] f1161h;
    private String f1162i;
    private boolean f1163j;
    private String f1164k;
    private boolean f1165l;
    private String f1166m;
    private String f1167n;
    private String f1168o;

    public fg(cx cxVar, C0439b c0439b, String str, String str2, String str3, er erVar, Map map, fo[] foVarArr, String str4, boolean z, String str5, String str6, String str7, boolean z2) {
        super(df.CreateSfoPaymentRequest, cxVar, c0439b, str);
        this.f1159f = erVar;
        this.f1160g = map;
        this.f1161h = foVarArr;
        this.f1162i = str4;
        this.f1165l = z2;
        this.f1164k = str7;
        if (C0441d.m267c(this.f1164k)) {
            this.f1164k = PayPalPayment.PAYMENT_INTENT_SALE;
        }
        this.f1164k = this.f1164k.toLowerCase(Locale.US);
        m191a("PayPal-Request-Id", str2);
        if (C0441d.m269d(str5)) {
            m191a("PayPal-Partner-Attribution-Id", str5);
        }
        if (C0441d.m269d(str6)) {
            m191a("PayPal-Client-Metadata-Id", str6);
        }
    }

    public final fg m1106a(boolean z) {
        this.f1163j = z;
        return this;
    }

    public final String mo2931b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("intent", this.f1164k);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.accumulate("payment_method", "paypal");
        jSONObject.accumulate("payer", jSONObject2);
        jSONObject2 = new JSONObject();
        jSONObject2.accumulate("cancel_url", "http://cancelurl");
        jSONObject2.accumulate("return_url", "http://returnurl");
        jSONObject.accumulate("redirect_urls", jSONObject2);
        er erVar = this.f1159f;
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.accumulate(Param.CURRENCY, erVar.m346b().getCurrencyCode());
        jSONObject3.accumulate("total", erVar.m345a().toPlainString());
        if (!(this.f1160g == null || this.f1160g.isEmpty())) {
            Object obj;
            String str = "details";
            if (this.f1160g == null || this.f1160g.isEmpty()) {
                obj = null;
            } else {
                obj = new JSONObject();
                if (this.f1160g.containsKey(Param.SHIPPING)) {
                    obj.accumulate(Param.SHIPPING, this.f1160g.get(Param.SHIPPING));
                }
                if (this.f1160g.containsKey("subtotal")) {
                    obj.accumulate("subtotal", this.f1160g.get("subtotal"));
                }
                if (this.f1160g.containsKey(Param.TAX)) {
                    obj.accumulate(Param.TAX, this.f1160g.get(Param.TAX));
                }
            }
            jSONObject3.accumulate(str, obj);
        }
        jSONObject2 = new JSONObject();
        jSONObject2.accumulate(SdkConstants.AMOUNT, jSONObject3);
        jSONObject2.accumulate("description", this.f1162i);
        if (this.f1161h != null && this.f1161h.length > 0) {
            jSONObject3 = new JSONObject();
            jSONObject3.accumulate("items", fo.m368a(this.f1161h));
            jSONObject2.accumulate("item_list", jSONObject3);
        }
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(jSONObject2);
        jSONObject.accumulate("transactions", jSONArray);
        if (C0441d.m269d(this.f1166m)) {
            jSONObject2.accumulate("invoice_number", this.f1166m);
        }
        if (C0441d.m269d(this.f1167n)) {
            jSONObject2.accumulate("custom", this.f1167n);
        }
        if (C0441d.m269d(this.f1168o)) {
            jSONObject2.accumulate("soft_descriptor", this.f1168o);
        }
        jSONObject2 = new JSONObject();
        jSONObject2.accumulate("device_info", C0441d.m257a(em.m338a().toString()));
        jSONObject2.accumulate("app_info", C0441d.m257a(eh.m328a().toString()));
        jSONObject2.accumulate("risk_data", C0441d.m257a(at.m75a().m100c().toString()));
        jSONObject3 = new JSONObject();
        jSONObject3.accumulate(SdkConstants.PAYMENT, jSONObject);
        jSONObject3.accumulate("client_info", jSONObject2);
        if (this.f1165l) {
            jSONObject3.accumulate("retrieve_shipping_addresses", Boolean.valueOf(true));
        }
        jSONObject3.accumulate("no_shipping", Boolean.valueOf(this.f1163j));
        return jSONObject3.toString();
    }

    public final void mo2932c() {
        JSONObject m = m206m();
        this.f1154a = m.optString("payment_id");
        this.f1155b = m.getString(SdkConstants.LAST_SESSION_ID);
        this.f1156c = m.optJSONArray("addresses");
        m = m.optJSONObject("funding_options");
        if (m != null) {
            this.f1157d = m.optJSONObject("default_option");
            this.f1158e = m.optJSONArray("alternate_options");
        }
    }

    public final fg m1109d(String str) {
        this.f1166m = str;
        return this;
    }

    public final void mo2933d() {
        m1086b(m206m());
    }

    public final fg m1111e(String str) {
        this.f1167n = str;
        return this;
    }

    public final String mo2934e() {
        return "{    \"session_id\":\"7N0112287V303050T\",    \"payment_id\":\"PAY-18X32451H0459092JKO7KFUI\",    \"addresses\": [          {             \"city\": \"Columbia\",              \"line2\": \"6073 2nd Street\",              \"line1\": \"Suite 222\",              \"recipient_name\": \"Beverly Jello\",             \"state\": \"MD\",              \"postal_code\": \"21045\",             \"default_address\": false,              \"country_code\": \"US\",              \"type\": \"HOME_OR_WORK\",              \"id\": \"366853\"          },          {             \"city\": \"Austin\",              \"line2\": \"Apt. 222\",              \"line1\": \"52 North Main St. \",              \"recipient_name\": \"Michael Chassen\",             \"state\": \"TX\",              \"postal_code\": \"78729\",             \"default_address\": true,              \"country_code\": \"US\",              \"type\": \"HOME_OR_WORK\",              \"id\": \"366852\"          },          {             \"city\": \"Austin\",              \"line1\": \"202 South State St. \",              \"recipient_name\": \"Sam Stone\",             \"state\": \"TX\",              \"postal_code\": \"78729\",             \"default_address\": true,              \"country_code\": \"US\",              \"type\": \"HOME_OR_WORK\",              \"id\": \"366852\"          }     ],     \"funding_options\":{       \"default_option\":{          \"id\":\"1\",          \"backup_funding_instrument\":{             \"payment_card\":{                \"number\":\"8029\",                \"type\":\"VISA\"             }          },          \"funding_sources\":[             {                \"amount\":{                   \"value\":\"216.85\",                   \"currency\":\"USD\"                },                \"funding_instrument_type\":\"BANK_ACCOUNT\",                \"funding_mode\":\"INSTANT_TRANSFER\",                \"bank_account\":{                   \"bank_name\":\"SunTrust\",                   \"account_number\":\"7416\",                   \"account_number_type\":\"BBAN\",                   \"country_code\":\"US\",                   \"account_type\":\"CHECKING\"                }             },             {                \"amount\":{                   \"value\":\"6.00\",                   \"currency\":\"USD\"                },                \"funding_instrument_type\":\"CREDIT\",                \"funding_mode\":\"INSTANT_TRANSFER\",                \"credit\":{                   \"type\":\"BILL_ME_LATER\",                   \"id\":\"mock-id\"                }             }          ]       },       \"alternate_options\":[          {             \"id\":\"2\",             \"funding_sources\":[                {                   \"amount\":{                      \"value\":\"216.85\",                      \"currency\":\"USD\"                   },                   \"funding_instrument_type\":\"PAYMENT_CARD\",                   \"payment_card\":{                      \"number\":\"8029\",                      \"type\":\"VISA\"                   },                   \"funding_mode\":\"INSTANT_TRANSFER\"                },                {                   \"amount\":{                      \"value\":\"6.00\",                      \"currency\":\"USD\"                   },                   \"funding_instrument_type\":\"BALANCE\",                   \"funding_mode\":\"INSTANT_TRANSFER\"                }             ]          },          {             \"id\":\"3\",             \"funding_sources\":[                {                   \"amount\":{                      \"value\":\"216.85\",                      \"currency\":\"USD\"                   },                   \"funding_instrument_type\":\"PAYMENT_CARD\",                   \"payment_card\":{                      \"number\":\"8011\",                      \"type\":\"VISA\"                   },                   \"funding_mode\":\"INSTANT_TRANSFER\"                },                {                   \"amount\":{                      \"value\":\"6.00\",                      \"currency\":\"USD\"                   },                   \"funding_instrument_type\":\"BALANCE\",                   \"funding_mode\":\"INSTANT_TRANSFER\"                }             ]          }       ]    } }";
    }

    public final fg m1113f(String str) {
        this.f1168o = str;
        return this;
    }

    public final String m1114t() {
        return this.f1154a;
    }

    public final String m1115u() {
        return this.f1155b;
    }

    public final JSONArray m1116v() {
        return this.f1156c;
    }

    public final JSONObject m1117w() {
        return this.f1157d;
    }

    public final JSONArray m1118x() {
        return this.f1158e;
    }
}
