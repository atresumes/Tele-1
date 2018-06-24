package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class eq implements Creator {
    eq() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new ep(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new ep[i];
    }
}
