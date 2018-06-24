package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.paypal.android.sdk.fg;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class dx implements Parcelable {
    public static final Creator CREATOR = new dy();
    private JSONArray f864a;
    private ShippingAddress f865b;
    private int f866c = -1;
    private int f867d;
    private JSONObject f868e;
    private JSONArray f869f;
    private int f870g = -1;
    private String f871h;
    private String f872i;

    public dx(Parcel parcel) {
        if (parcel != null) {
            String readString;
            try {
                readString = parcel.readString();
                if (readString != null) {
                    this.f864a = new JSONArray(readString);
                } else {
                    this.f864a = null;
                }
            } catch (JSONException e) {
                this.f864a = null;
            }
            this.f865b = (ShippingAddress) parcel.readParcelable(ShippingAddress.class.getClassLoader());
            try {
                readString = parcel.readString();
                if (readString != null) {
                    this.f868e = new JSONObject(readString);
                } else {
                    this.f868e = null;
                }
            } catch (JSONException e2) {
                this.f868e = null;
            }
            try {
                readString = parcel.readString();
                if (readString != null) {
                    this.f869f = new JSONArray(readString);
                } else {
                    this.f869f = null;
                }
            } catch (JSONException e3) {
                this.f869f = null;
            }
            this.f871h = parcel.readString();
            this.f872i = parcel.readString();
            this.f870g = parcel.readInt();
            this.f866c = parcel.readInt();
            this.f867d = parcel.readInt();
        }
    }

    public dx(fg fgVar, ShippingAddress shippingAddress) {
        this.f864a = fgVar.m1116v();
        this.f868e = fgVar.m1117w();
        this.f869f = fgVar.m1118x();
        this.f871h = fgVar.m1114t();
        this.f872i = fgVar.m1115u();
        this.f865b = shippingAddress;
        if (this.f865b != null) {
            this.f866c = 0;
            this.f867d = m699a(this.f865b, this.f864a);
            return;
        }
        this.f866c = m700a(this.f864a);
        this.f867d = -1;
    }

    private static int m699a(ShippingAddress shippingAddress, JSONArray jSONArray) {
        if (!(shippingAddress == null || jSONArray == null)) {
            for (int i = 0; i < jSONArray.length(); i++) {
                if (shippingAddress.m663a(jSONArray.optJSONObject(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int m700a(JSONArray jSONArray) {
        if (jSONArray == null) {
            return -1;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            if (jSONArray.optJSONObject(i).optBoolean("default_address", false)) {
                return i;
            }
        }
        return 0;
    }

    public final JSONArray m701a() {
        return this.f864a;
    }

    public final void m702a(int i) {
        this.f870g = i;
    }

    public final ShippingAddress m703b() {
        return this.f865b;
    }

    public final void m704b(int i) {
        this.f866c = i;
    }

    public final JSONObject m705c() {
        return this.f868e;
    }

    public final JSONArray m706d() {
        return this.f869f;
    }

    public final int describeContents() {
        return 0;
    }

    public final String m707e() {
        return this.f871h;
    }

    public final String m708f() {
        return this.f872i;
    }

    public final int m709g() {
        return this.f870g < 0 ? 0 : this.f870g;
    }

    public final int m710h() {
        return this.f866c < 0 ? 0 : this.f866c;
    }

    public final int m711i() {
        return this.f867d;
    }

    public final boolean m712j() {
        return this.f870g != -1;
    }

    public final boolean m713k() {
        return this.f866c != -1;
    }

    public final JSONObject m714l() {
        return this.f870g <= 0 ? null : this.f869f.optJSONObject(this.f870g - 1);
    }

    public final JSONObject m715m() {
        if (this.f866c < 0) {
            return null;
        }
        if (this.f865b == null) {
            return this.f864a.optJSONObject(this.f866c);
        }
        if (this.f866c == 0) {
            return this.f867d < 0 ? this.f865b.toJSONObject() : this.f864a.optJSONObject(this.f867d);
        } else {
            int i = this.f866c - 1;
            if (this.f867d >= 0 && i >= this.f867d) {
                i++;
            }
            return this.f864a.optJSONObject(i);
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        String str = null;
        parcel.writeString(this.f864a != null ? this.f864a.toString() : null);
        parcel.writeParcelable(this.f865b, 0);
        parcel.writeString(this.f868e != null ? this.f868e.toString() : null);
        if (this.f869f != null) {
            str = this.f869f.toString();
        }
        parcel.writeString(str);
        parcel.writeString(this.f871h);
        parcel.writeString(this.f872i);
        parcel.writeInt(this.f870g);
        parcel.writeInt(this.f866c);
        parcel.writeInt(this.f867d);
    }
}
