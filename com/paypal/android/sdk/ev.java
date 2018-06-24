package com.paypal.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.PhoneNumberUtils;
import java.util.HashMap;
import java.util.Locale;

public class ev implements Parcelable {
    public static final Creator CREATOR = new ew();
    private static HashMap f375c;
    private ei f376a;
    private String f377b;

    static {
        HashMap hashMap = new HashMap();
        f375c = hashMap;
        hashMap.put("US", "1");
        f375c.put("CA", "1");
        f375c.put("GB", "44");
        f375c.put("FR", "33");
        f375c.put("IT", "39");
        f375c.put("ES", "34");
        f375c.put("AU", "61");
        f375c.put("MY", "60");
        f375c.put("SG", "65");
        f375c.put("AR", "54");
        f375c.put("UK", "44");
        f375c.put("ZA", "27");
        f375c.put("GR", "30");
        f375c.put("NL", "31");
        f375c.put("BE", "32");
        f375c.put("SG", "65");
        f375c.put("PT", "351");
        f375c.put("LU", "352");
        f375c.put("IE", "353");
        f375c.put("IS", "354");
        f375c.put("MT", "356");
        f375c.put("CY", "357");
        f375c.put("FI", "358");
        f375c.put("HU", "36");
        f375c.put("LT", "370");
        f375c.put("LV", "371");
        f375c.put("EE", "372");
        f375c.put("SI", "386");
        f375c.put("CH", "41");
        f375c.put("CZ", "420");
        f375c.put("SK", "421");
        f375c.put("AT", "43");
        f375c.put("DK", "45");
        f375c.put("SE", "46");
        f375c.put("NO", "47");
        f375c.put("PL", "48");
        f375c.put("DE", "49");
        f375c.put("MX", "52");
        f375c.put("BR", "55");
        f375c.put("NZ", "64");
        f375c.put("TH", "66");
        f375c.put("JP", "81");
        f375c.put("KR", "82");
        f375c.put("HK", "852");
        f375c.put("CN", "86");
        f375c.put("TW", "886");
        f375c.put("TR", "90");
        f375c.put("IN", "91");
        f375c.put("IL", "972");
        f375c.put("MC", "377");
        f375c.put("CR", "506");
        f375c.put("CL", "56");
        f375c.put("VE", "58");
        f375c.put("EC", "593");
        f375c.put("UY", "598");
    }

    public ev(Parcel parcel) {
        this.f376a = (ei) parcel.readParcelable(ei.class.getClassLoader());
        this.f377b = parcel.readString();
    }

    public ev(eu euVar, ei eiVar, String str) {
        m358a(eiVar, euVar.mo2167a(et.m351e(str)));
    }

    public ev(eu euVar, String str) {
        m358a(euVar.mo2170d(), euVar.mo2167a(et.m351e(str)));
    }

    public static ev m357a(eu euVar, String str) {
        String[] split = str.split("[|]");
        if (split.length == 2) {
            return new ev(euVar, new ei(split[0]), split[1]);
        }
        throw new eo("");
    }

    private void m358a(ei eiVar, String str) {
        this.f376a = eiVar;
        this.f377b = str;
    }

    public final String m359a() {
        return this.f377b;
    }

    public final String m360a(eu euVar) {
        return euVar.mo2168b().equals(Locale.US) ? PhoneNumberUtils.formatNumber(this.f377b) : this.f377b;
    }

    public final String m361b() {
        return this.f376a.m330a() + "|" + this.f377b;
    }

    public final String m362c() {
        return (String) f375c.get(this.f376a.m330a());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.f376a, 0);
        parcel.writeString(this.f377b);
    }
}
