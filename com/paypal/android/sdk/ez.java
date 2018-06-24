package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class ez implements Creator {
    ez() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new ey(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new ey[i];
    }
}
