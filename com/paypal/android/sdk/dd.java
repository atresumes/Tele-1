package com.paypal.android.sdk;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

final class dd implements Executor {
    private dd() {
    }

    public final void execute(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
