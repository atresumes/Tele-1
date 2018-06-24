package com.paypal.android.sdk;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

final class aw extends Handler {
    private final WeakReference f115a;

    public aw(at atVar) {
        this.f115a = new WeakReference(atVar);
    }

    public final void handleMessage(Message message) {
        at atVar = (at) this.f115a.get();
        if (atVar != null) {
            atVar.m98a(message);
        }
    }
}
