package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.math.BigDecimal;
import org.json.JSONObject;

public final class PayPalPaymentDetails implements Parcelable {
    public static final Creator CREATOR = new bs();
    private static final String f658a = PayPalPaymentDetails.class.getSimpleName();
    private BigDecimal f659b;
    private BigDecimal f660c;
    private BigDecimal f661d;

    private PayPalPaymentDetails(Parcel parcel) {
        BigDecimal bigDecimal = null;
        try {
            String readString = parcel.readString();
            this.f660c = readString == null ? null : new BigDecimal(readString);
            readString = parcel.readString();
            this.f659b = readString == null ? null : new BigDecimal(readString);
            String readString2 = parcel.readString();
            if (readString2 != null) {
                bigDecimal = new BigDecimal(readString2);
            }
            this.f661d = bigDecimal;
        } catch (NumberFormatException e) {
            throw new RuntimeException("error unparceling PayPalPaymentDetails");
        }
    }

    public PayPalPaymentDetails(BigDecimal bigDecimal, BigDecimal bigDecimal2, BigDecimal bigDecimal3) {
        this.f660c = bigDecimal;
        this.f659b = bigDecimal2;
        this.f661d = bigDecimal3;
    }

    public final int describeContents() {
        return 0;
    }

    public final BigDecimal getShipping() {
        return this.f660c;
    }

    public final BigDecimal getSubtotal() {
        return this.f659b;
    }

    public final BigDecimal getTax() {
        return this.f661d;
    }

    public final boolean isProcessable() {
        return this.f659b != null;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.f660c != null) {
                jSONObject.put(Param.SHIPPING, this.f660c.toPlainString());
            }
            if (this.f659b != null) {
                jSONObject.put("subtotal", this.f659b.toPlainString());
            }
            if (this.f661d == null) {
                return jSONObject;
            }
            jSONObject.put(Param.TAX, this.f661d.toPlainString());
            return jSONObject;
        } catch (Throwable e) {
            Log.e(f658a, "error encoding JSON", e);
            return null;
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        String str = null;
        parcel.writeString(this.f660c == null ? null : this.f660c.toString());
        parcel.writeString(this.f659b == null ? null : this.f659b.toString());
        if (this.f661d != null) {
            str = this.f661d.toString();
        }
        parcel.writeString(str);
    }
}
