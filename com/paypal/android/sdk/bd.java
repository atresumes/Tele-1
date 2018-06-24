package com.paypal.android.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class bd extends bh {
    private static final String f968a = bd.class.getSimpleName();
    private Context f969b;
    private String f970c;
    private Handler f971d;

    public bd(Context context, String str, Handler handler) {
        this.f969b = context;
        this.f970c = str;
        this.f971d = handler;
    }

    public void run() {
        bn.m141a(f968a, "entering LoadConfigurationRequest.");
        if (this.f971d != null) {
            try {
                this.f971d.sendMessage(Message.obtain(this.f971d, 10, this.f970c));
                this.f971d.sendMessage(Message.obtain(this.f971d, 12, new ap(this.f969b, this.f970c)));
            } catch (Throwable e) {
                bn.m142a(f968a, "LoadConfigurationRequest loading remote config failed.", e);
                this.f971d.sendMessage(Message.obtain(this.f971d, 11, e));
            } finally {
                bi.m121a().m124b(this);
            }
            bn.m141a(f968a, "leaving LoadConfigurationRequest.");
        }
    }
}
