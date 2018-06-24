package com.paypal.android.sdk;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.UUID;

public class C0438a {
    private static final String f8a = C0438a.class.getSimpleName();
    private final Context f9b;
    private final String f10c;
    private final C0440c f11d;

    public C0438a(Context context, String str, C0441d c0441d) {
        if (context == null) {
            throw new NullPointerException("context == null");
        } else if (str == null) {
            throw new NullPointerException("prefs == null");
        } else {
            this.f9b = context;
            this.f10c = str;
            this.f11d = c0441d.m271a(this);
        }
    }

    public final String m27a(String str) {
        return this.f11d.mo2173b(this.f9b.getSharedPreferences(this.f10c, 0).getString(str, null));
    }

    public final void m28a(String str, String str2) {
        Editor edit = this.f9b.getSharedPreferences(this.f10c, 0).edit();
        edit.putString(str, this.f11d.mo2172a(str2));
        edit.commit();
    }

    public final boolean m29a() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f9b.getSystemService("connectivity");
        if (connectivityManager == null) {
            Log.w("paypal.sdk", "Unable to retrieve Context.CONNECTIVITY_SERVICE. Ignoring.");
            return true;
        } else if (connectivityManager.getAllNetworkInfo() == null) {
            Log.w("paypal.sdk", "ConnectivityManager.getAllNetworkInfo() returned null. Ignoring.");
            return true;
        } else {
            int i = 0;
            for (NetworkInfo isConnectedOrConnecting : connectivityManager.getAllNetworkInfo()) {
                if (isConnectedOrConnecting.isConnectedOrConnecting()) {
                    i++;
                }
            }
            return i > 0;
        }
    }

    public final int m30b() {
        return ((TelephonyManager) this.f9b.getSystemService("phone")).getPhoneType();
    }

    public final String m31b(String str) {
        return this.f11d.mo2172a(str);
    }

    public final String m32c() {
        try {
            PackageManager packageManager = this.f9b.getPackageManager();
            return packageManager.getPackageInfo(this.f9b.getPackageName(), 0).applicationInfo.loadLabel(packageManager).toString();
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public final String m33c(String str) {
        return this.f11d.mo2173b(str);
    }

    public final String m34d() {
        try {
            return ((TelephonyManager) this.f9b.getSystemService("phone")).getSimOperatorName();
        } catch (SecurityException e) {
            e.toString();
            return null;
        }
    }

    public final String m35e() {
        String string = this.f9b.getSharedPreferences(this.f10c, 0).getString("InstallationGUID", null);
        if (string != null) {
            return string;
        }
        string = UUID.randomUUID().toString();
        Editor edit = this.f9b.getSharedPreferences(this.f10c, 0).edit();
        edit.putString("InstallationGUID", string);
        edit.commit();
        return string;
    }

    public final Context m36f() {
        return this.f9b;
    }
}
