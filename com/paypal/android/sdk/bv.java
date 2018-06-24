package com.paypal.android.sdk;

import java.util.ArrayList;
import java.util.List;

public class bv {
    private static final String f197a = bv.class.getSimpleName();
    private final List f198b = new ArrayList();

    public final void m168a() {
        synchronized (this.f198b) {
            for (bw remove : this.f198b) {
                this.f198b.remove(remove);
            }
        }
    }

    public final void m169a(bx bxVar) {
        synchronized (this.f198b) {
            for (bw bwVar : this.f198b) {
                if (bwVar.f199a == bxVar) {
                    new StringBuilder("Ignoring attempt to re-register listener ").append(bxVar);
                    return;
                }
            }
            this.f198b.add(new bw(this, bxVar));
        }
    }

    public final void m170a(cw cwVar, long j) {
        new StringBuilder("dispatching ").append(cwVar.m207n());
        if (cwVar.m208o() < 0) {
            new StringBuilder("discarding ").append(cwVar.m207n());
            return;
        }
        List<bw> arrayList = new ArrayList();
        synchronized (this.f198b) {
            for (bw add : this.f198b) {
                arrayList.add(0, add);
            }
        }
        for (bw add2 : arrayList) {
            add2.f199a.mo2171a(cwVar);
        }
    }
}
