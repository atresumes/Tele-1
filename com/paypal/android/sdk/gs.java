package com.paypal.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import com.paypal.android.sdk.payments.PayPalService;

public class gs {
    private static final String f592a = gs.class.getSimpleName();
    private final Context f593b;

    public gs(Context context) {
        this.f593b = context;
    }

    private boolean m436b() {
        try {
            PackageManager packageManager = this.f593b.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.f593b.getPackageName(), 4);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.f593b.getPackageName(), 128);
            if (packageInfo.services == null) {
                return false;
            }
            for (ServiceInfo serviceInfo : packageInfo.services) {
                if (serviceInfo.name.equals(PayPalService.class.getName())) {
                    new StringBuilder("context.getPackageName()=").append(this.f593b.getPackageName());
                    new StringBuilder("serviceInfo.exported=").append(serviceInfo.exported);
                    new StringBuilder("serviceInfo.processName=").append(serviceInfo.processName);
                    new StringBuilder("applicationInfo.processName=").append(applicationInfo.processName);
                    if (!serviceInfo.exported && applicationInfo.processName.equals(serviceInfo.processName)) {
                        new StringBuilder("Found ").append(PayPalService.class.getName()).append(" in manifest.");
                        return true;
                    }
                }
            }
            return false;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Exception loading manifest" + e.getMessage());
        }
    }

    public final void m437a() {
        if (!m436b()) {
            throw new RuntimeException("Please declare the following in your manifest, and ensure it runs in the same process as your application:<service android:name=\"com.paypal.android.sdk.payments.PayPalService\" android:exported=\"false\" />");
        }
    }
}
