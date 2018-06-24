package com.paypal.android.sdk;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class gr {
    private static final List f590a = Arrays.asList(new String[]{"android.permission.ACCESS_NETWORK_STATE", "android.permission.INTERNET"});
    private final Context f591b;

    static {
        gr.class.getSimpleName();
    }

    public gr(Context context) {
        this.f591b = context;
    }

    public final void m435a() {
        try {
            Set hashSet = new HashSet(f590a);
            String[] strArr = this.f591b.getPackageManager().getPackageInfo(this.f591b.getPackageName(), 4096).requestedPermissions;
            if (strArr != null) {
                for (Object remove : strArr) {
                    hashSet.remove(remove);
                }
            }
            if (!hashSet.isEmpty()) {
                throw new RuntimeException("Missing required permissions in manifest:" + hashSet);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Exception loading manifest" + e.getMessage());
        }
    }
}
