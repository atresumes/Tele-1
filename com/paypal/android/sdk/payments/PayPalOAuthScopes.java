package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.paypal.android.sdk.ak;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class PayPalOAuthScopes implements Parcelable {
    public static final Creator CREATOR = new bq();
    public static final String PAYPAL_SCOPE_ADDRESS = ak.ADDRESS.m40a();
    public static final String PAYPAL_SCOPE_EMAIL = ak.EMAIL.m40a();
    public static final String PAYPAL_SCOPE_FUTURE_PAYMENTS = ak.FUTURE_PAYMENTS.m40a();
    public static final String PAYPAL_SCOPE_OPENID = ak.OPENID.m40a();
    public static final String PAYPAL_SCOPE_PAYPAL_ATTRIBUTES = ak.PAYPAL_ATTRIBUTES.m40a();
    public static final String PAYPAL_SCOPE_PHONE = ak.PHONE.m40a();
    public static final String PAYPAL_SCOPE_PROFILE = ak.PROFILE.m40a();
    private final List f644a;

    public PayPalOAuthScopes() {
        this.f644a = new ArrayList();
    }

    private PayPalOAuthScopes(Parcel parcel) {
        this.f644a = new ArrayList();
        int readInt = parcel.readInt();
        for (int i = 0; i < readInt; i++) {
            this.f644a.add(parcel.readString());
        }
    }

    public PayPalOAuthScopes(Set set) {
        this();
        for (String add : set) {
            this.f644a.add(add);
        }
    }

    final List m506a() {
        return this.f644a;
    }

    final String m507b() {
        return TextUtils.join(" ", this.f644a);
    }

    public final int describeContents() {
        return 0;
    }

    public final String toString() {
        return String.format(PayPalOAuthScopes.class.getSimpleName() + ": {%s}", new Object[]{this.f644a});
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f644a.size());
        for (int i2 = 0; i2 < this.f644a.size(); i2++) {
            parcel.writeString((String) this.f644a.get(i2));
        }
    }
}
