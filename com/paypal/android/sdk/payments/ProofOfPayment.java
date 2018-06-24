package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.paypal.android.sdk.C0441d;
import org.json.JSONObject;

public final class ProofOfPayment implements Parcelable {
    public static final Creator CREATOR = new dw();
    private static final String f724a = ProofOfPayment.class.getSimpleName();
    private String f725b;
    private String f726c;
    private String f727d;
    private String f728e;
    private String f729f;

    private ProofOfPayment(Parcel parcel) {
        this(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
    }

    ProofOfPayment(String str, String str2, String str3, String str4, String str5) {
        this.f725b = str;
        this.f726c = str2;
        this.f727d = str3;
        this.f728e = str4;
        this.f729f = str5;
        new StringBuilder("ProofOfPayment created: ").append(toString());
    }

    public final int describeContents() {
        return 0;
    }

    public final String getCreateTime() {
        return this.f727d;
    }

    public final String getIntent() {
        return this.f728e;
    }

    public final String getPaymentId() {
        return this.f726c;
    }

    public final String getState() {
        return this.f725b;
    }

    public final String getTransactionId() {
        return this.f729f;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("create_time", this.f727d);
            jSONObject.put("id", this.f726c);
            jSONObject.put("intent", this.f728e);
            jSONObject.put("state", this.f725b);
            if (!C0441d.m269d(this.f729f) || !C0441d.m269d(this.f728e)) {
                return jSONObject;
            }
            if (this.f728e.equals(PayPalPayment.PAYMENT_INTENT_AUTHORIZE)) {
                jSONObject.put("authorization_id", this.f729f);
                return jSONObject;
            } else if (!this.f728e.equals(PayPalPayment.PAYMENT_INTENT_ORDER)) {
                return jSONObject;
            } else {
                jSONObject.put("order_id", this.f729f);
                return jSONObject;
            }
        } catch (Throwable e) {
            Log.e(f724a, "error encoding JSON", e);
            return null;
        }
    }

    public final String toString() {
        return "{" + this.f728e + ": " + (C0441d.m269d(this.f729f) ? this.f729f : "no transactionId") + "}";
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f725b);
        parcel.writeString(this.f726c);
        parcel.writeString(this.f727d);
        parcel.writeString(this.f728e);
        parcel.writeString(this.f729f);
    }
}
