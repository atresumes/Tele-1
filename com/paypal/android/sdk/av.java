package com.paypal.android.sdk;

import java.util.TimerTask;

final class av extends TimerTask {
    private /* synthetic */ at f114a;

    av(at atVar) {
        this.f114a = atVar;
    }

    public final void run() {
        this.f114a.f97h = this.f114a.f97h + 1;
        bn.m141a(at.f88b, "****** LoadConfigurationTask #" + this.f114a.f97h);
        bi.m121a().m123a(new bd(this.f114a.f92c, this.f114a.f99j, this.f114a.f107r));
    }
}
