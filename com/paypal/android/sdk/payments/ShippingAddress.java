package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.paypal.android.sdk.C0441d;
import com.talktoangel.gts.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

public final class ShippingAddress implements Parcelable {
    public static final Creator CREATOR = new dz();
    private static final String f730a = ShippingAddress.class.getSimpleName();
    private String f731b;
    private String f732c;
    private String f733d;
    private String f734e;
    private String f735f;
    private String f736g;
    private String f737h;

    private ShippingAddress(Parcel parcel) {
        this.f731b = parcel.readString();
        this.f732c = parcel.readString();
        this.f733d = parcel.readString();
        this.f734e = parcel.readString();
        this.f735f = parcel.readString();
        this.f736g = parcel.readString();
        this.f737h = parcel.readString();
    }

    private static void m660a(boolean z, String str) {
        if (!z) {
            Log.e(f730a, str + " is invalid.  Please see the docs.");
        }
    }

    private static boolean m661a(String str) {
        return C0441d.m269d(str);
    }

    private static boolean m662a(String str, String str2) {
        return C0441d.m267c((CharSequence) str) ? C0441d.m267c((CharSequence) str2) : C0441d.m267c((CharSequence) str2) ? false : str.trim().equals(str2.trim());
    }

    final boolean m663a(JSONObject jSONObject) {
        return m662a(jSONObject.optString("recipient_name"), this.f731b) && m662a(jSONObject.optString("line1"), this.f732c) && m662a(jSONObject.optString("line2"), this.f733d) && m662a(jSONObject.optString("city"), this.f734e) && m662a(jSONObject.optString("state"), this.f735f) && m662a(jSONObject.optString(SessionManager.COUNTRY_CODE), this.f737h) && m662a(jSONObject.optString("postal_code"), this.f736g);
    }

    public final ShippingAddress city(String str) {
        this.f734e = str;
        return this;
    }

    public final ShippingAddress countryCode(String str) {
        this.f737h = str;
        return this;
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean isProcessable() {
        boolean d = C0441d.m269d(this.f731b);
        boolean d2 = C0441d.m269d(this.f732c);
        boolean d3 = C0441d.m269d(this.f734e);
        boolean z = C0441d.m269d(this.f737h) && this.f737h.length() == 2;
        m660a(d, "recipient_name");
        m660a(d2, "line1");
        m660a(d3, "city");
        m660a(z, SessionManager.COUNTRY_CODE);
        return d && d2 && d3 && z;
    }

    public final ShippingAddress line1(String str) {
        this.f732c = str;
        return this;
    }

    public final ShippingAddress line2(String str) {
        this.f733d = str;
        return this;
    }

    public final ShippingAddress postalCode(String str) {
        this.f736g = str;
        return this;
    }

    public final ShippingAddress recipientName(String str) {
        this.f731b = str;
        return this;
    }

    public final ShippingAddress state(String str) {
        this.f735f = str;
        return this;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.accumulate("recipient_name", this.f731b);
            jSONObject.accumulate("line1", this.f732c);
            jSONObject.accumulate("city", this.f734e);
            jSONObject.accumulate(SessionManager.COUNTRY_CODE, this.f737h);
            if (m661a(this.f733d)) {
                jSONObject.accumulate("line2", this.f733d);
            }
            if (m661a(this.f735f)) {
                jSONObject.accumulate("state", this.f735f);
            }
            if (m661a(this.f736g)) {
                jSONObject.accumulate("postal_code", this.f736g);
            }
        } catch (JSONException e) {
            Log.e(f730a, e.getMessage());
        }
        return jSONObject;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f731b);
        parcel.writeString(this.f732c);
        parcel.writeString(this.f733d);
        parcel.writeString(this.f734e);
        parcel.writeString(this.f735f);
        parcel.writeString(this.f736g);
        parcel.writeString(this.f737h);
    }
}
