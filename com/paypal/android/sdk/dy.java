package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class dy implements Parcelable {
    public static final Creator CREATOR = new dz();
    private String f344a;
    private String f345b;

    static {
        dy.class.getSimpleName();
    }

    public dy(Parcel parcel) {
        this.f344a = parcel.readString();
        this.f345b = parcel.readString();
    }

    public dy(String str, String str2) {
        this.f344a = str;
        this.f345b = str2;
    }

    public final String m301a() {
        return this.f344a;
    }

    public final String m302b() {
        return this.f345b;
    }

    public final int describeContents() {
        return 0;
    }

    public final String toString() {
        return dy.class.getSimpleName() + "(authCode:" + this.f344a + ", scope:" + this.f345b + ")";
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f344a);
        parcel.writeString(this.f345b);
    }
}
