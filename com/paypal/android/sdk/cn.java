package com.paypal.android.sdk;

import java.io.IOException;

final class cn implements Runnable {
    private /* synthetic */ cw f217a;
    private /* synthetic */ String f218b;
    private /* synthetic */ cm f219c;

    cn(cm cmVar, cw cwVar, String str) {
        this.f219c = cmVar;
        this.f217a = cwVar;
        this.f218b = str;
    }

    public final void run() {
        try {
            this.f219c.m1051a(this.f217a, this.f218b, this.f219c.f1108g, new cr(this.f219c, this.f217a, (byte) 0));
        } catch (IOException e) {
        }
    }
}
