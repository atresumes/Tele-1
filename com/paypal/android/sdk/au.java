package com.paypal.android.sdk;

import java.util.TimerTask;

final class au extends TimerTask {
    private /* synthetic */ at f113a;

    au(at atVar) {
        this.f113a = atVar;
    }

    public final void run() {
        this.f113a.f96g = this.f113a.f96g + 1;
        bn.m141a(at.f88b, "****** LogRiskMetadataTask #" + this.f113a.f96g);
        if (at.m82c(this.f113a)) {
            bn.m141a(at.f88b, "No host activity in the last " + (this.f113a.f95f / 1000) + " seconds. Stopping update interval.");
            this.f113a.f106q.cancel();
            return;
        }
        try {
            at.m86f(this.f113a);
        } catch (Throwable e) {
            bn.m142a(at.f88b, "Error in logRiskMetadataTask: ", e);
        }
    }
}
