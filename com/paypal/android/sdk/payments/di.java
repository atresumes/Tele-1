package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class di implements Creator {
    di() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new PaymentConfirmation(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new PaymentConfirmation[i];
    }
}
