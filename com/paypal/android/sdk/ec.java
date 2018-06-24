package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class ec implements Creator {
    ec() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new eb(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new eb[i];
    }
}
