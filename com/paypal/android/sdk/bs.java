package com.paypal.android.sdk;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

final class bs extends Handler {
    private WeakReference f194a;

    public bs(br brVar) {
        this.f194a = new WeakReference(brVar);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 2:
                br brVar = (br) this.f194a.get();
                if (brVar != null) {
                    brVar.f986d.m170a((cw) message.obj, 0);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
