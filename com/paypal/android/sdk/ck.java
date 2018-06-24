package com.paypal.android.sdk;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class ck extends ci {
    private static final String f1098a = ck.class.getSimpleName();
    private final cx f1099b;
    private final ThreadPoolExecutor f1100c = ((ThreadPoolExecutor) Executors.newCachedThreadPool());
    private final int f1101d;

    public ck(int i, cx cxVar) {
        this.f1101d = i;
        this.f1099b = cxVar;
    }

    public final void mo2935a() {
    }

    protected abstract String mo3080b();

    public final boolean mo2936b(cw cwVar) {
        this.f1100c.submit(new cl(this, cwVar));
        return true;
    }

    protected abstract int mo3081c();

    protected abstract boolean mo3082c(cw cwVar);
}
