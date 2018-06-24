package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ei implements Parcelable {
    public static final Creator CREATOR = new ej();
    private final String f352a;

    public ei(Parcel parcel) {
        this.f352a = parcel.readString();
    }

    public ei(String str) {
        if (str.equals("OTHER") || str.length() == 2) {
            this.f352a = str;
        } else {
            this.f352a = "US";
        }
    }

    public final String m330a() {
        return this.f352a;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.f352a.equals(((ei) obj).f352a);
    }

    public int hashCode() {
        return this.f352a.hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f352a);
    }
}
