package com.paypal.android.sdk;

import android.location.Location;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class as {
    public String f35A;
    public String f36B;
    public Boolean f37C;
    public String f38D;
    public String f39E;
    public Boolean f40F;
    public String f41G;
    public String f42H;
    public long f43I;
    public long f44J;
    public String f45K;
    public Boolean f46L;
    public Integer f47M;
    public int f48N = -1;
    public int f49O = -1;
    public String f50P;
    public int f51Q = -1;
    public String f52R;
    public Boolean f53S;
    public Boolean f54T;
    public String f55U;
    public long f56V;
    public long f57W;
    public String f58X;
    public String f59Y;
    public String f60Z;
    public String f61a;
    public String aa;
    public String ab;
    public String ac;
    public String ad;
    public String ae;
    public String af;
    public String ag;
    public Map ah;
    private String ai = "full";
    public String f62b;
    public String f63c;
    public int f64d = -1;
    public String f65e;
    public int f66f = -1;
    public String f67g;
    public String f68h;
    public String f69i;
    public String f70j;
    public String f71k;
    public String f72l;
    public String f73m;
    public long f74n = -1;
    public String f75o;
    public List f76p;
    public List f77q;
    public String f78r;
    public String f79s;
    public String f80t;
    public String f81u;
    public Location f82v;
    public int f83w = -1;
    public String f84x;
    public String f85y = SdkConstants.OS_NAME_VALUE;
    public String f86z;

    private static JSONObject m69a(Location location) {
        if (location == null) {
            return null;
        }
        try {
            return new JSONObject("{\"lat\":" + location.getLatitude() + ",\"lng\":" + location.getLongitude() + ",\"acc\":" + location.getAccuracy() + ",\"timestamp\":" + location.getTime() + "}");
        } catch (JSONException e) {
            return null;
        }
    }

    private void m70a(JSONObject jSONObject) {
        if (this.ah != null) {
            for (Entry entry : this.ah.entrySet()) {
                try {
                    jSONObject.put((String) entry.getKey(), entry.getValue());
                } catch (Throwable e) {
                    bn.m142a(null, null, e);
                }
            }
        }
    }

    public final JSONObject m71a() {
        Object obj = null;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("app_guid", this.f61a);
            jSONObject.put("app_id", this.f62b);
            jSONObject.put("app_version", this.f63c);
            jSONObject.put("base_station_id", this.f64d == -1 ? null : Integer.valueOf(this.f64d));
            jSONObject.put("bssid", this.f65e);
            jSONObject.put("cell_id", this.f66f == -1 ? null : Integer.valueOf(this.f66f));
            jSONObject.put("comp_version", this.f67g);
            jSONObject.put("conf_url", this.f68h);
            jSONObject.put("conf_version", this.f69i);
            jSONObject.put("conn_type", this.f70j);
            jSONObject.put("device_id", this.f71k);
            jSONObject.put("dc_id", this.af);
            jSONObject.put(CBAnalyticsConstant.DEVICE_MODEL, this.f72l);
            jSONObject.put("device_name", this.f73m);
            jSONObject.put("device_uptime", this.f74n == -1 ? null : Long.valueOf(this.f74n));
            jSONObject.put("ip_addrs", this.f75o);
            jSONObject.put("ip_addresses", this.f76p == null ? null : new JSONArray(this.f76p));
            jSONObject.put("known_apps", this.f77q == null ? null : new JSONArray(this.f77q));
            jSONObject.put("line_1_number", "".equals(this.f78r) ? null : this.f78r);
            jSONObject.put("linker_id", this.f79s);
            jSONObject.put("locale_country", this.f80t);
            jSONObject.put("locale_lang", this.f81u);
            jSONObject.put(Param.LOCATION, m69a(this.f82v));
            jSONObject.put("location_area_code", this.f83w == -1 ? null : Integer.valueOf(this.f83w));
            jSONObject.put("mac_addrs", this.f84x);
            jSONObject.put("os_type", this.f85y);
            jSONObject.put("os_version", this.f86z);
            jSONObject.put("payload_type", this.ai);
            jSONObject.put("phone_type", this.f35A);
            jSONObject.put("risk_comp_session_id", this.f36B);
            jSONObject.put("roaming", this.f37C);
            jSONObject.put("sim_operator_name", "".equals(this.f38D) ? null : this.f38D);
            jSONObject.put("sim_serial_number", this.f39E);
            jSONObject.put("sms_enabled", this.f40F);
            jSONObject.put("ssid", this.f41G);
            jSONObject.put("cdma_network_id", this.f49O == -1 ? null : Integer.valueOf(this.f49O));
            jSONObject.put("cdma_system_id", this.f48N == -1 ? null : Integer.valueOf(this.f48N));
            jSONObject.put("subscriber_id", this.f42H);
            jSONObject.put(SdkConstants.LAST_USED_SESSION_TIMESTAMP, this.f43I);
            jSONObject.put("total_storage_space", this.f44J);
            jSONObject.put("tz_name", this.f45K);
            jSONObject.put("ds", this.f46L);
            jSONObject.put("tz", this.f47M);
            jSONObject.put("network_operator", this.f50P);
            String str = "source_app";
            if (this.f51Q != -1) {
                obj = Integer.valueOf(this.f51Q);
            }
            jSONObject.put(str, obj);
            jSONObject.put("source_app_version", this.f52R);
            jSONObject.put("is_emulator", this.f53S);
            jSONObject.put("is_rooted", this.f54T);
            jSONObject.put("pairing_id", this.f55U);
            jSONObject.put("app_first_install_time", this.f56V);
            jSONObject.put("app_last_update_time", this.f57W);
            jSONObject.put("android_id", this.f58X);
            jSONObject.put("serial_number", this.aa);
            jSONObject.put("advertising_identifier", this.f59Y);
            jSONObject.put("notif_token", this.f60Z);
            jSONObject.put("bluetooth_mac_addrs", null);
            jSONObject.put("gsf_id", this.ab);
            jSONObject.put("VPN_setting", this.ad);
            jSONObject.put("proxy_setting", this.ac);
            jSONObject.put("c", this.ae);
            jSONObject.put("pm", this.ag);
            m70a(jSONObject);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public final JSONObject m72a(as asVar) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("is_emulator", asVar.f53S);
            jSONObject.put("is_rooted", asVar.f54T);
            jSONObject.put("app_guid", asVar.f61a);
            jSONObject.put("risk_comp_session_id", asVar.f36B);
            jSONObject.put(SdkConstants.LAST_USED_SESSION_TIMESTAMP, asVar.f43I);
            jSONObject.put("payload_type", "incremental");
            jSONObject.put("source_app", asVar.f51Q);
            jSONObject.put("pairing_id", asVar.f55U);
            m70a(jSONObject);
            if (!(this.f62b == null || this.f62b.equals(asVar.f62b))) {
                jSONObject.put("app_id", asVar.f62b);
            }
            if (!(this.f63c == null || this.f63c.equals(asVar.f63c))) {
                jSONObject.put("app_version", asVar.f63c);
            }
            if (this.f64d != asVar.f64d) {
                jSONObject.put("base_station_id", asVar.f64d);
            }
            if (!(this.f65e == null || this.f65e.equals(asVar.f65e))) {
                jSONObject.put("bssid", asVar.f65e);
            }
            if (this.f66f != asVar.f66f) {
                jSONObject.put("cell_id", asVar.f66f);
            }
            if (!(this.f67g == null || this.f67g.equals(asVar.f67g))) {
                jSONObject.put("comp_version", asVar.f67g);
            }
            if (!(this.f69i == null || this.f69i.equals(asVar.f69i))) {
                jSONObject.put("conf_version", asVar.f69i);
            }
            if (!(this.f68h == null || this.f68h.equals(asVar.f68h))) {
                jSONObject.put("conf_url", asVar.f68h);
            }
            if (!(this.f70j == null || this.f70j.equals(asVar.f70j))) {
                jSONObject.put("conn_type", asVar.f70j);
            }
            if (!(this.f71k == null || this.f71k.equals(asVar.f71k))) {
                jSONObject.put("device_id", asVar.f71k);
            }
            if (!(this.f72l == null || this.f72l.equals(asVar.f72l))) {
                jSONObject.put(CBAnalyticsConstant.DEVICE_MODEL, asVar.f72l);
            }
            if (!(this.f73m == null || this.f73m.equals(asVar.f73m))) {
                jSONObject.put("device_name", asVar.f73m);
            }
            if (this.f74n != asVar.f74n) {
                jSONObject.put("device_uptime", asVar.f74n);
            }
            if (!(this.f75o == null || this.f75o.equals(asVar.f75o))) {
                jSONObject.put("ip_addrs", asVar.f75o);
            }
            if (!(this.f76p == null || asVar.f76p == null || this.f76p.toString().equals(asVar.f76p.toString()))) {
                jSONObject.put("ip_addresses", new JSONArray(asVar.f76p));
            }
            if (!(this.f77q == null || asVar.f77q == null || this.f77q.toString().equals(asVar.f77q.toString()))) {
                jSONObject.put("known_apps", new JSONArray(asVar.f77q));
            }
            if (!(this.f78r == null || this.f78r.equals(asVar.f78r))) {
                jSONObject.put("line_1_number", asVar.f78r);
            }
            if (!(this.f79s == null || this.f79s.equals(asVar.f79s))) {
                jSONObject.put("linker_id", asVar.f79s);
            }
            if (!(this.f80t == null || this.f80t.equals(asVar.f80t))) {
                jSONObject.put("locale_country", asVar.f80t);
            }
            if (!(this.f81u == null || this.f81u.equals(asVar.f81u))) {
                jSONObject.put("locale_lang", asVar.f81u);
            }
            if (!(this.f82v == null || asVar.f82v == null || this.f82v.toString().equals(asVar.f82v.toString()))) {
                jSONObject.put(Param.LOCATION, m69a(asVar.f82v));
            }
            if (this.f83w != asVar.f83w) {
                jSONObject.put("location_area_code", asVar.f83w);
            }
            if (!(this.f84x == null || this.f84x.equals(asVar.f84x))) {
                jSONObject.put("mac_addrs", asVar.f84x);
            }
            if (!(this.f85y == null || this.f85y.equals(asVar.f85y))) {
                jSONObject.put("os_type", asVar.f85y);
            }
            if (!(this.f86z == null || this.f86z.equals(asVar.f86z))) {
                jSONObject.put("os_version", asVar.f86z);
            }
            if (!(this.f35A == null || this.f35A.equals(asVar.f35A))) {
                jSONObject.put("phone_type", asVar.f35A);
            }
            if (this.f37C != null && this.f37C.equals(asVar.f37C)) {
                jSONObject.put("roaming", asVar.f37C);
            }
            if (!(this.f38D == null || this.f38D.equals(asVar.f38D))) {
                jSONObject.put("sim_operator_name", asVar.f38D);
            }
            if (!(this.f39E == null || this.f39E.equals(asVar.f39E))) {
                jSONObject.put("sim_serial_number", asVar.f39E);
            }
            if (this.f40F != null && this.f40F.equals(asVar.f40F)) {
                jSONObject.put("sms_enabled", asVar.f40F);
            }
            if (!(this.f41G == null || this.f41G.equals(asVar.f41G))) {
                jSONObject.put("ssid", asVar.f41G);
            }
            if (this.f49O != asVar.f49O) {
                jSONObject.put("cdma_network_id", asVar.f49O);
            }
            if (this.f48N != asVar.f48N) {
                jSONObject.put("cdma_system_id", asVar.f48N);
            }
            if (!(this.f42H == null || this.f42H.equals(asVar.f42H))) {
                jSONObject.put("subscriber_id", asVar.f42H);
            }
            if (this.f44J != asVar.f44J) {
                jSONObject.put("total_storage_space", asVar.f44J);
            }
            if (!(this.f45K == null || this.f45K.equals(asVar.f45K))) {
                jSONObject.put("tz_name", asVar.f45K);
            }
            if (!(this.f46L == null || this.f46L.equals(asVar.f46L))) {
                jSONObject.put("ds", asVar.f46L);
            }
            if (!(this.f47M == null || this.f47M.equals(asVar.f47M))) {
                jSONObject.put("tz", asVar.f47M);
            }
            if (!(this.f50P == null || this.f50P.equals(asVar.f50P))) {
                jSONObject.put("network_operator", asVar.f50P);
            }
            if (!(this.f52R == null || this.f52R.equals(asVar.f52R))) {
                jSONObject.put("source_app_version", asVar.f52R);
            }
            if (this.f56V != asVar.f56V) {
                jSONObject.put("app_first_install_time", asVar.f56V);
            }
            if (this.f57W != asVar.f57W) {
                jSONObject.put("app_last_update_time", asVar.f57W);
            }
            if (!(this.f58X == null || this.f58X.equals(asVar.f58X))) {
                jSONObject.put("android_id", asVar.f58X);
            }
            if (!(this.aa == null || this.aa.equals(asVar.aa))) {
                jSONObject.put("serial_number", asVar.aa);
            }
            if (!(this.f59Y == null || this.f59Y.equals(asVar.f59Y))) {
                jSONObject.put("advertising_identifier", asVar.f59Y);
            }
            if (!(this.f60Z == null || this.f60Z.equals(asVar.f60Z))) {
                jSONObject.put("notif_token", asVar.f60Z);
            }
            if (!(this.ab == null || this.ab.equals(asVar.ab))) {
                jSONObject.put("gsf_id", asVar.ab);
            }
            if (!(this.ad == null || this.ad.equals(asVar.ad))) {
                jSONObject.put("VPN_setting", asVar.ad);
            }
            if (!(this.ac == null || this.ac.equals(asVar.ac))) {
                jSONObject.put("proxy_setting", asVar.ac);
            }
            if (!(this.ae == null || this.ae.equals(asVar.ae))) {
                jSONObject.put("c", asVar.ae);
            }
            if (!(this.ag == null || this.ag.equals(asVar.ag))) {
                jSONObject.put("pm", asVar.ag);
            }
        } catch (JSONException e) {
        }
        return jSONObject;
    }
}
