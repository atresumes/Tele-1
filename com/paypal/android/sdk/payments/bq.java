package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class bq implements Creator {
    bq() {
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new PayPalOAuthScopes(parcel);
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new PayPalOAuthScopes[i];
    }
}
