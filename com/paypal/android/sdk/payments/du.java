package com.paypal.android.sdk.payments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.paypal.android.sdk.fk;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class du implements Parcelable {
    public static final Creator CREATOR = new dv();
    private List f860a;
    private String f861b;
    private String f862c;
    private String f863d;

    du() {
        this.f860a = new ArrayList();
    }

    public du(Parcel parcel) {
        this();
        if (parcel != null) {
            this.f861b = parcel.readString();
            this.f862c = parcel.readString();
            this.f863d = parcel.readString();
            this.f860a = new ArrayList();
            parcel.readList(this.f860a, String.class.getClassLoader());
        }
    }

    du(fk fkVar) {
        this.f860a = m694a(fkVar.m1066t());
        this.f861b = fkVar.m1067u();
        this.f862c = fkVar.m1068v();
        this.f863d = fkVar.m1069w();
    }

    private static List m694a(Map map) {
        List arrayList = new ArrayList();
        for (String str : map.keySet()) {
            if (((String) map.get(str)).toUpperCase().equals("Y")) {
                arrayList.add(str.toLowerCase(Locale.US));
            }
        }
        return arrayList;
    }

    public final List m695a() {
        return this.f860a;
    }

    public final String m696b() {
        return this.f861b;
    }

    public final String m697c() {
        return this.f862c;
    }

    public final String m698d() {
        return this.f863d;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f861b);
        parcel.writeString(this.f862c);
        parcel.writeString(this.f863d);
        parcel.writeList(this.f860a);
    }
}
