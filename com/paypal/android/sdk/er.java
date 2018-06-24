package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.math.BigDecimal;
import java.util.Currency;

public class er implements Parcelable {
    public static final Creator CREATOR = new es();
    private static /* synthetic */ boolean f365c = (!er.class.desiredAssertionStatus());
    private BigDecimal f366a;
    private Currency f367b;

    public er(Parcel parcel) {
        this.f366a = new BigDecimal(parcel.readString());
        try {
            this.f367b = Currency.getInstance(parcel.readString());
        } catch (Throwable e) {
            Log.e("MoneySpec", "Exception reading currency code from parcel", e);
            throw new RuntimeException(e);
        }
    }

    public er(BigDecimal bigDecimal, String str) {
        this.f366a = bigDecimal;
        this.f367b = Currency.getInstance(str);
    }

    public final BigDecimal m345a() {
        return this.f366a;
    }

    public final Currency m346b() {
        return this.f367b;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (f365c || (obj instanceof er)) {
            er erVar = (er) obj;
            return erVar.f366a == this.f366a && erVar.f367b.equals(this.f367b);
        } else {
            throw new AssertionError();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f366a.toString());
        parcel.writeString(this.f367b.getCurrencyCode());
    }
}
