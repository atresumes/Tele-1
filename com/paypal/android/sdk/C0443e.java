package com.paypal.android.sdk;

import android.content.Context;
import android.util.Log;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

public final class C0443e {
    private static final String f346a = C0443e.class.getSimpleName();
    private static da f347b;

    public static final synchronized String m304a(ExecutorService executorService, Context context, String str, String str2, String str3) {
        String a;
        synchronized (C0443e.class) {
            if (f347b == null) {
                try {
                    f347b = new da();
                    a = f347b.m272a(context, str, str2, Collections.emptyMap());
                    executorService.submit(new C0444f());
                    new StringBuilder("Init risk component: ").append(f347b.m275c()).append(" returning new clientMetadataId:").append(a);
                } catch (Throwable th) {
                    Log.e("paypal.sdk", "An internal component failed to initialize: " + th.getMessage());
                    a = null;
                }
            } else {
                a = f347b.m274b();
                new StringBuilder("risk component already initialized, returning new clientMetadataId:").append(a);
            }
        }
        return a;
    }
}
