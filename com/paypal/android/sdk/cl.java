package com.paypal.android.sdk;

import android.text.TextUtils;

final class cl implements Runnable {
    private /* synthetic */ cw f215a;
    private /* synthetic */ ck f216b;

    cl(ck ckVar, cw cwVar) {
        this.f216b = ckVar;
        this.f215a = cwVar;
    }

    public final void run() {
        ck.f1098a;
        new StringBuilder("Mock executing ").append(this.f215a.m207n()).append(" request: ").append(this.f215a.m200f());
        String b;
        if (this.f216b.mo3082c(this.f215a)) {
            b = this.f216b.mo3080b();
            this.f215a.m202h().mo2164a();
            new StringBuilder("mock failure:\n").append(b);
            this.f215a.m195b(b);
            ci.m833a(this.f215a, this.f216b.mo3081c());
        } else {
            b = this.f215a.mo2934e();
            if (TextUtils.isEmpty(b)) {
                throw new RuntimeException("Empty mock value for " + this.f215a.m202h());
            }
            this.f215a.m202h().mo2164a();
            new StringBuilder("mock response:").append(b);
            this.f215a.m195b(b);
            ci.m832a(this.f215a);
        }
        if (!this.f215a.mo2930a()) {
            try {
                ck.f1098a;
                new StringBuilder("sleep for [").append(this.f216b.f1101d).append(" ms].");
                Thread.sleep((long) this.f216b.f1101d);
                ck.f1098a;
                new StringBuilder("end [").append(this.f216b.f1101d).append(" ms] sleep");
            } catch (InterruptedException e) {
                this.f215a.m202h().mo2164a();
            }
            this.f216b.f1099b.mo2157a(this.f215a);
        }
    }
}
