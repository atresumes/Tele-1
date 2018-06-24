package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class dw extends ea implements Parcelable {
    public static final Creator CREATOR = new dx();
    private boolean f1005c;
    private String f1006d;

    static {
        dw.class.getSimpleName();
    }

    public dw(Parcel parcel) {
        super(parcel);
        this.f1006d = parcel.readString();
        this.f1005c = parcel.readByte() != (byte) 0;
    }

    public dw(String str, String str2, long j, boolean z) {
        this.f348a = str;
        this.f349b = j;
        this.f1006d = str2;
        this.f1005c = z;
    }

    public final boolean m855a() {
        return this.f1005c;
    }

    public final int describeContents() {
        return 0;
    }

    public final String toString() {
        return dw.class.getSimpleName() + "(token:" + this.a + ", mGoodUntil:" + this.b + ", isCreatedInternally:" + this.f1005c + ")";
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeLong(this.b);
        parcel.writeString(this.f1006d);
        parcel.writeByte((byte) (this.f1005c ? 1 : 0));
    }
}
