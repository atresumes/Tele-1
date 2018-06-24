package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Date;

public final class dt extends dp implements Parcelable {
    public static final Creator CREATOR = new du();
    private String f999b;
    private Date f1000c;
    private String f1001d;
    private dv f1002e;
    private int f1003f;
    private int f1004g;

    private dt(Parcel parcel) {
        this.a = parcel.readString();
        this.f999b = parcel.readString();
        this.f1001d = parcel.readString();
        this.f1000c = (Date) parcel.readSerializable();
        this.f1002e = (dv) parcel.readSerializable();
        this.f1003f = parcel.readInt();
        this.f1004g = parcel.readInt();
    }

    public dt(C0438a c0438a, String str, String str2, Date date, String str3, String str4, int i, int i2) {
        this.a = c0438a.m31b(str2);
        this.f999b = str;
        this.f1000c = date;
        m846b(str3);
        m847c(str4);
        this.f1003f = i;
        this.f1004g = i2;
    }

    public dt(String str, String str2, String str3, String str4, String str5, int i, int i2) {
        this.a = str2;
        this.f999b = str;
        this.f1000c = ex.m363a(str3);
        m846b(str4);
        m847c(str5);
        this.f1003f = i;
        this.f1004g = i2;
    }

    public static String m845a(String str) {
        return str == null ? null : "x-" + str.substring(str.length() - 4);
    }

    private void m846b(String str) {
        if (str != null) {
            this.f1001d = str.substring(str.length() - 4);
        } else {
            this.f1001d = null;
        }
    }

    private void m847c(String str) {
        this.f1002e = dv.m300a(str);
    }

    public final boolean m848b() {
        return (C0441d.m262a(this.f999b) || C0441d.m262a(this.f1001d) || C0441d.m262a(this.a) || this.f1000c == null || this.f1000c.before(new Date()) || this.f1002e == null || this.f1002e == dv.UNKNOWN || this.f1003f <= 0 || this.f1003f > 12 || this.f1004g < 0 || this.f1004g > 9999) ? false : true;
    }

    public final Date m849c() {
        return this.f1000c;
    }

    public final String m850d() {
        return m845a(this.f1001d);
    }

    public final int describeContents() {
        return 0;
    }

    public final String m851e() {
        return this.f999b;
    }

    public final int m852f() {
        return this.f1003f;
    }

    public final int m853g() {
        return this.f1004g;
    }

    public final dv m854h() {
        return this.f1002e;
    }

    public final String toString() {
        return "TokenizedCreditCard(token=" + this.f999b + ",lastFourDigits=" + this.f1001d + ",payerId=" + this.a + ",tokenValidUntil=" + this.f1000c + ",cardType=" + this.f1002e + ",expiryMonth/year=" + this.f1003f + "/" + this.f1004g + ")";
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeString(this.f999b);
        parcel.writeString(this.f1001d);
        parcel.writeSerializable(this.f1000c);
        parcel.writeSerializable(this.f1002e);
        parcel.writeInt(this.f1003f);
        parcel.writeInt(this.f1004g);
    }
}
