package com.paypal.android.sdk;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class fz implements go {
    private String f1010a = fu.m369a(fw.PREFERRED_PAYMENT_METHOD);
    private gc f1011b;

    private fz(JSONObject jSONObject) {
        this.f1011b = new gc(jSONObject.optJSONArray("funding_sources"), jSONObject.optJSONObject("backup_funding_instrument"));
    }

    public static ArrayList m860a(JSONObject jSONObject, JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONObject != null) {
            fz fzVar = new fz(jSONObject);
            if (fzVar.m861h()) {
                arrayList.add(fzVar);
            }
        }
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    fz fzVar2 = new fz(jSONArray.getJSONObject(i));
                    if (fzVar2.m861h()) {
                        arrayList.add(fzVar2);
                    }
                } catch (JSONException e) {
                }
            }
        }
        return arrayList;
    }

    private boolean m861h() {
        return this.f1011b.m395e() > 0;
    }

    public final String mo2174a() {
        return this.f1011b.m394d();
    }

    public final String mo2175b() {
        return this.f1010a;
    }

    public final String mo2176c() {
        return this.f1011b.m391a();
    }

    public final String mo2177d() {
        return this.f1011b.m393c();
    }

    public final boolean mo2178e() {
        return this.f1011b.m392b();
    }

    public final gc m867f() {
        return this.f1011b;
    }

    public final boolean m868g() {
        return this.f1011b.m395e() == 1;
    }
}
