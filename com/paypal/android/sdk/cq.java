package com.paypal.android.sdk;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

final class cq implements Callback {
    private final cw f992a;
    private /* synthetic */ cm f993b;

    private cq(cm cmVar, cw cwVar) {
        this.f993b = cmVar;
        this.f992a = cwVar;
    }

    private String m834a(String str) {
        Locale locale = Locale.US;
        String str2 = this.f992a.m207n() + " PayPal Debug-ID: %s [%s, %s]";
        Object[] objArr = new Object[3];
        objArr[0] = str;
        objArr[1] = this.f993b.f1105d;
        StringBuilder append = new StringBuilder().append(this.f993b.f1109h.mo2209a()).append(";");
        this.f993b.f1109h;
        objArr[2] = append.append("release").toString();
        return String.format(locale, str2, objArr);
    }

    public final void onFailure(Call call, IOException iOException) {
        try {
            this.f992a.m195b(iOException.getMessage());
            Object header = call.request().header("PayPal-Debug-Id");
            if (!TextUtils.isEmpty(header)) {
                Log.w("paypal.sdk", m834a(header));
            }
            cm.m1050a(this.f993b, this.f992a, null, iOException);
        } catch (Throwable th) {
            Log.e("paypal.sdk", "exception in response handler", th);
        }
    }

    public final void onResponse(Call call, Response response) {
        try {
            Object header = response.header("paypal-debug-id");
            this.f992a.m195b(response.body().string());
            if (response.isSuccessful()) {
                this.f992a.m197c(header);
                cm.f1102a;
                new StringBuilder().append(this.f992a.m207n()).append(" success. response: ").append(this.f992a.m201g());
                if (!TextUtils.isEmpty(header)) {
                    Log.w("paypal.sdk", m834a(header));
                }
                if (this.f992a.m210q()) {
                    ci.m832a(this.f992a);
                }
                this.f993b.f1106e.mo2157a(this.f992a);
                return;
            }
            if (!TextUtils.isEmpty(header)) {
                Log.w("paypal.sdk", m834a(header));
            }
            cm.m1050a(this.f993b, this.f992a, response, null);
        } catch (Throwable th) {
            Log.e("paypal.sdk", "exception in response handler", th);
        }
    }
}
