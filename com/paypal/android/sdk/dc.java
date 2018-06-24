package com.paypal.android.sdk;

import java.util.ArrayList;
import java.util.List;

public class dc implements cu {
    final df f995a;

    public dc(df dfVar) {
        this.f995a = dfVar;
    }

    public static List m835d() {
        List arrayList = new ArrayList();
        for (df dcVar : df.values()) {
            arrayList.add(new dc(dcVar));
        }
        return arrayList;
    }

    public String mo2164a() {
        return this.f995a.name();
    }

    public cb mo2165b() {
        return this.f995a.m277a();
    }

    public String mo2166c() {
        return this.f995a.m278b();
    }
}
