package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.Iterator;

public class zzato extends zza implements Iterable<String> {
    public static final Creator<zzato> CREATOR = new zzatp();
    private final Bundle zzbrD;

    class C02861 implements Iterator<String> {
        Iterator<String> zzbrE = this.zzbrF.zzbrD.keySet().iterator();
        final /* synthetic */ zzato zzbrF;

        C02861(zzato com_google_android_gms_internal_zzato) {
            this.zzbrF = com_google_android_gms_internal_zzato;
        }

        public boolean hasNext() {
            return this.zzbrE.hasNext();
        }

        public String next() {
            return (String) this.zzbrE.next();
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    zzato(Bundle bundle) {
        this.zzbrD = bundle;
    }

    Object get(String str) {
        return this.zzbrD.get(str);
    }

    public Iterator<String> iterator() {
        return new C02861(this);
    }

    public int size() {
        return this.zzbrD.size();
    }

    public String toString() {
        return this.zzbrD.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzatp.zza(this, parcel, i);
    }

    public Bundle zzLW() {
        return new Bundle(this.zzbrD);
    }
}
