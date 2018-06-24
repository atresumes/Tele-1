package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class bh implements Creator {
    bh() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return (bg) parcel.readSerializable();
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new bg[i];
    }
}
