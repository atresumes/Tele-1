package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class du implements Creator {
    du() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new dt(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new dt[i];
    }
}
