package com.paypal.android.sdk;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class gc implements Iterable {
    private List f503a = new ArrayList();
    private int f504b;

    static {
        gc.class.getSimpleName();
    }

    public gc(JSONArray jSONArray, JSONObject jSONObject) {
        for (int i = 0; i < jSONArray.length(); i++) {
            Object a;
            try {
                a = gb.m381a(jSONArray.getJSONObject(i));
            } catch (JSONException e) {
                Log.w("paypal.sdk", "Error extracting funding source: " + e.getMessage());
                a = null;
            }
            if (a != null) {
                this.f503a.add(a);
            }
        }
        if (jSONObject != null) {
            Object a2;
            try {
                a2 = gb.m381a(jSONObject);
            } catch (JSONException e2) {
                Log.w("paypal.sdk", "Error parsing backup funding instrument: " + e2.getMessage());
                a2 = null;
            }
            if (a2 != null) {
                this.f503a.add(a2);
            }
        }
        this.f504b = m389f();
    }

    private int m389f() {
        Double valueOf = Double.valueOf(0.0d);
        int i = 0;
        for (int i2 = 0; i2 < this.f503a.size(); i2++) {
            if (((gb) this.f503a.get(i2)).m386d().doubleValue() > valueOf.doubleValue()) {
                valueOf = ((gb) this.f503a.get(i2)).m386d();
                i = i2;
            }
        }
        return i;
    }

    public final gb m390a(int i) {
        this.f503a.size();
        return (gb) this.f503a.get(0);
    }

    public final String m391a() {
        return ((gb) this.f503a.get(this.f504b)).m383a();
    }

    public final boolean m392b() {
        Object f = ((gb) this.f503a.get(this.f504b)).m388f();
        return C0441d.m269d(f) ? f.toUpperCase().equals("DELAYED_TRANSFER") : false;
    }

    public final String m393c() {
        return this.f503a.size() == 1 ? ((gb) this.f503a.get(0)).m384b() : fu.m369a(fw.AND_OTHER_FUNDING_SOURCES);
    }

    public final String m394d() {
        return ((gb) this.f503a.get(this.f504b)).m387e();
    }

    public final int m395e() {
        return this.f503a.size();
    }

    public final Iterator iterator() {
        return this.f503a.iterator();
    }
}
