package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class dm implements Parcelable {
    public static final Creator CREATOR = new dn();
    private ev f320a;
    private String f321b;
    private C0442do f322c;

    public dm(Parcel parcel) {
        this.f320a = (ev) parcel.readParcelable(ev.class.getClassLoader());
        this.f321b = parcel.readString();
        this.f322c = (C0442do) parcel.readSerializable();
    }

    public dm(String str, ev evVar, C0442do c0442do) {
        this.f321b = str;
        this.f320a = evVar;
        this.f322c = c0442do;
    }

    public final ev m292a() {
        return this.f320a;
    }

    public final void m293a(C0442do c0442do) {
        this.f322c = c0442do;
    }

    public final void m294a(ev evVar) {
        this.f320a = evVar;
    }

    public final void m295a(String str) {
        this.f321b = str;
    }

    public final String m296b() {
        return this.f321b;
    }

    public final C0442do m297c() {
        return this.f322c;
    }

    public final boolean m298d() {
        boolean z = this.f322c == null || ((this.f320a == null && this.f322c.equals(C0442do.PHONE)) || (C0441d.m262a(this.f321b) && this.f322c.equals(C0442do.EMAIL)));
        return !z;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.f320a, 0);
        parcel.writeString(this.f321b);
        parcel.writeSerializable(this.f322c);
    }
}
