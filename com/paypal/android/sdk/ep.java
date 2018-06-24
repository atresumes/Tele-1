package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ep implements Parcelable {
    public static final Creator CREATOR = new eq();
    private String f361a;
    private String f362b;
    private ev f363c;
    private String f364d;

    public ep(Parcel parcel) {
        this.f361a = parcel.readString();
        this.f362b = parcel.readString();
        this.f363c = (ev) parcel.readParcelable(ev.class.getClassLoader());
        this.f364d = parcel.readString();
    }

    public ep(ev evVar, String str) {
        this.f363c = evVar;
        this.f364d = str;
    }

    public ep(String str, String str2) {
        this.f361a = str;
        this.f362b = str2;
    }

    public final boolean m340a() {
        return this.f361a != null;
    }

    public final String m341b() {
        return this.f361a;
    }

    public final String m342c() {
        return this.f362b;
    }

    public final ev m343d() {
        return this.f363c;
    }

    public final int describeContents() {
        return 0;
    }

    public final String m344e() {
        return this.f364d;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f361a);
        parcel.writeString(this.f362b);
        parcel.writeParcelable(this.f363c, 0);
        parcel.writeString(this.f364d);
    }
}
