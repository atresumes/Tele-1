package com.paypal.android.sdk;

import android.os.Parcel;

public abstract class ea {
    protected String f348a;
    protected long f349b;

    public ea(Parcel parcel) {
        this.f348a = parcel.readString();
        this.f349b = parcel.readLong();
    }

    public final boolean m305b() {
        return this.f349b > System.currentTimeMillis();
    }

    public final String m306c() {
        return this.f348a;
    }
}
