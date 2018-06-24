package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class bj implements Creator {
    bj() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new PayPalConfiguration(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new PayPalConfiguration[i];
    }
}
