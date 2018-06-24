package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class dv implements Creator {
    dv() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new du(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new du[i];
    }
}
