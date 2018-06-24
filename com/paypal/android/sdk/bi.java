package com.paypal.android.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class bi {
    private List f126a;
    private List f127b;

    static {
        bi.class.getSimpleName();
    }

    private bi() {
        this.f126a = Collections.synchronizedList(new ArrayList());
        this.f127b = Collections.synchronizedList(new ArrayList());
    }

    public static bi m121a() {
        return bj.f128a;
    }

    private void m122b() {
        if (!this.f127b.isEmpty()) {
            synchronized (this) {
                if (!this.f127b.isEmpty()) {
                    bh bhVar = (bh) this.f127b.get(0);
                    this.f127b.remove(0);
                    this.f126a.add(bhVar);
                    new Thread(bhVar).start();
                }
            }
        }
    }

    public final void m123a(bh bhVar) {
        this.f127b.add(bhVar);
        if (this.f126a.size() < 3) {
            m122b();
        }
    }

    public final void m124b(bh bhVar) {
        this.f126a.remove(bhVar);
        m122b();
    }
}
