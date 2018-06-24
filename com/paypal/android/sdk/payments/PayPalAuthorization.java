package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBConstant;
import org.json.JSONObject;

public final class PayPalAuthorization implements Parcelable {
    public static final Creator CREATOR = new bi();
    private final String f614a;
    private final String f615b;
    private final String f616c;

    static {
        PayPalAuthorization.class.getSimpleName();
    }

    private PayPalAuthorization(Parcel parcel) {
        this.f614a = parcel.readString();
        this.f615b = parcel.readString();
        this.f616c = parcel.readString();
    }

    PayPalAuthorization(String str, String str2, String str3) {
        this.f614a = str;
        this.f615b = str2;
        if ("partner".equals(BuildConfig.FLAVOR)) {
            this.f616c = str3;
        } else {
            this.f616c = null;
        }
    }

    public final int describeContents() {
        return 0;
    }

    public final String getAuthorizationCode() {
        return this.f615b;
    }

    public final String getEnvironment() {
        return this.f614a;
    }

    public final JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("environment", this.f614a);
            jSONObject2.put("paypal_sdk_version", "2.14.2");
            jSONObject2.put(SdkConstants.CALLING_PLATFORM_NAME, SdkConstants.OS_NAME_VALUE);
            jSONObject2.put("product_name", "PayPal-Android-SDK");
            jSONObject.put("client", jSONObject2);
            jSONObject2 = new JSONObject();
            jSONObject2.put("code", this.f615b);
            jSONObject.put(CBConstant.RESPONSE, jSONObject2);
            if ("partner".equals(BuildConfig.FLAVOR)) {
                jSONObject2 = new JSONObject();
                jSONObject2.put("display_string", this.f616c);
                jSONObject.put(SdkConstants.USER, jSONObject2);
            }
            jSONObject.put("response_type", "authorization_code");
            return jSONObject;
        } catch (Throwable e) {
            Log.e("paypal.sdk", "Error encoding JSON", e);
            return null;
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f614a);
        parcel.writeString(this.f615b);
        parcel.writeString(this.f616c);
    }
}
