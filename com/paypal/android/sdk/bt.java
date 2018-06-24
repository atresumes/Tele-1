package com.paypal.android.sdk;

import java.util.GregorianCalendar;
import java.util.Map;
import org.json.JSONObject;

public class bt extends cv {
    private final bu f1097a;

    static {
        bt.class.getSimpleName();
    }

    public bt(cu cuVar, cx cxVar, C0439b c0439b, bu buVar) {
        super(cuVar, cxVar, c0439b, null);
        this.f1097a = buVar;
        m191a("Accept", "application/json; charset=utf-8");
        m191a("Accept-Language", "en_US");
        m191a("Content-Type", "application/json");
    }

    private static JSONObject m1032a(Map map) {
        JSONObject jSONObject = new JSONObject();
        for (String str : map.keySet()) {
            jSONObject.accumulate(str, map.get(str));
        }
        return jSONObject;
    }

    public final String mo2929a(cu cuVar) {
        return "https://api.paypal.com/v1/tracking/events";
    }

    public final boolean mo2930a() {
        return true;
    }

    public final String mo2931b() {
        String a = C0441d.m257a(m212s().mo2159d().m35e());
        String str = this.f1097a.f195a;
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("tracking_visitor_id", a);
        jSONObject.accumulate("tracking_visit_id", str);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.accumulate("actor", jSONObject);
        jSONObject2.accumulate("channel", "mobile");
        long currentTimeMillis = System.currentTimeMillis();
        jSONObject2.accumulate("tracking_event", Long.toString(currentTimeMillis));
        this.f1097a.f196b.put("t", Long.toString(currentTimeMillis - ((long) new GregorianCalendar().getTimeZone().getRawOffset())));
        this.f1097a.f196b.put("dsid", a);
        this.f1097a.f196b.put("vid", str);
        jSONObject2.accumulate("event_params", m1032a(this.f1097a.f196b));
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.accumulate("events", jSONObject2);
        return jSONObject3.toString();
    }

    public final void mo2932c() {
    }

    public final void mo2933d() {
    }

    public final String mo2934e() {
        return "mockResponse";
    }
}
