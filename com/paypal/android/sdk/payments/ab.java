package com.paypal.android.sdk.payments;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class ab extends Handler {
    private /* synthetic */ aa f750a;

    ab(aa aaVar, Looper looper) {
        this.f750a = aaVar;
        super(looper);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                aa.m671a(this.f750a);
                return;
            default:
                super.handleMessage(message);
                return;
        }
    }
}
