package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class br implements Creator {
    br() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new PayPalPayment(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new PayPalPayment[i];
    }
}
