package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBConstant;
import org.json.JSONObject;

public final class PaymentConfirmation implements Parcelable {
    public static final Creator CREATOR = new di();
    private static final String f709a = PaymentConfirmation.class.getSimpleName();
    private String f710b;
    private PayPalPayment f711c;
    private ProofOfPayment f712d;

    private PaymentConfirmation(Parcel parcel) {
        this.f710b = parcel.readString();
        this.f711c = (PayPalPayment) parcel.readParcelable(PayPalPayment.class.getClassLoader());
        this.f712d = (ProofOfPayment) parcel.readParcelable(ProofOfPayment.class.getClassLoader());
    }

    PaymentConfirmation(String str, PayPalPayment payPalPayment, ProofOfPayment proofOfPayment) {
        this.f710b = str;
        this.f711c = payPalPayment;
        this.f712d = proofOfPayment;
    }

    public final int describeContents() {
        return 0;
    }

    public final String getEnvironment() {
        return this.f710b;
    }

    public final PayPalPayment getPayment() {
        return this.f711c;
    }

    public final ProofOfPayment getProofOfPayment() {
        return this.f712d;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("environment", this.f710b);
            jSONObject2.put("paypal_sdk_version", "2.14.2");
            jSONObject2.put(SdkConstants.CALLING_PLATFORM_NAME, SdkConstants.OS_NAME_VALUE);
            jSONObject2.put("product_name", "PayPal-Android-SDK");
            jSONObject.put("client", jSONObject2);
            jSONObject.put(CBConstant.RESPONSE, this.f712d.toJSONObject());
            jSONObject.put("response_type", SdkConstants.PAYMENT);
            return jSONObject;
        } catch (Throwable e) {
            Log.e(f709a, "Error encoding JSON", e);
            return null;
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f710b);
        parcel.writeParcelable(this.f711c, 0);
        parcel.writeParcelable(this.f712d, 0);
    }
}
