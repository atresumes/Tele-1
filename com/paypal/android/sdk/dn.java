package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class dn implements Creator {
    dn() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new dm(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new dm[i];
    }
}
