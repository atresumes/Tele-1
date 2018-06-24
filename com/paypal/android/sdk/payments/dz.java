package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class dz implements Creator {
    dz() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new ShippingAddress(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new ShippingAddress[i];
    }
}
