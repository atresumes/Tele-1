package com.paypal.android.sdk;

import android.util.Log;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class cs extends Thread {
    private static final String f222a = cs.class.getSimpleName();
    private final cx f223b;
    private final List f224c = Collections.synchronizedList(new LinkedList());
    private boolean f225d;
    private final ct f226e;

    public cs(cx cxVar, ct ctVar) {
        this.f223b = cxVar;
        this.f226e = ctVar;
        start();
    }

    public final void m179a() {
        if (!this.f225d) {
            this.f226e.mo2935a();
            this.f225d = true;
            synchronized (this.f224c) {
                this.f224c.notifyAll();
            }
            interrupt();
            while (isAlive()) {
                try {
                    Thread.sleep(10);
                    new StringBuilder("Waiting for ").append(getClass().getSimpleName()).append(" to die");
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public final void m180a(cw cwVar) {
        synchronized (this.f224c) {
            this.f224c.add(cwVar);
            new StringBuilder("Queued ").append(cwVar.m207n());
            this.f224c.notifyAll();
        }
    }

    public void run() {
        cw cwVar;
        new StringBuilder("Starting ").append(getClass().getSimpleName());
        while (!this.f225d) {
            synchronized (this.f224c) {
                if (this.f224c.isEmpty()) {
                    try {
                        this.f224c.wait();
                        cwVar = null;
                    } catch (InterruptedException e) {
                        cwVar = null;
                    }
                } else {
                    cwVar = (cw) this.f224c.remove(0);
                }
            }
            if (cwVar != null) {
                try {
                    cwVar.m190a(cwVar.mo2931b());
                } catch (Throwable e2) {
                    Log.e("paypal.sdk", "Exception computing request", e2);
                    cwVar.m188a(new ca(bz.PARSE_RESPONSE_ERROR.toString(), "JSON Exception in computeRequest", e2.getMessage()));
                } catch (Throwable e22) {
                    Log.e("paypal.sdk", "Exception computing request", e22);
                    cwVar.m188a(new ca(bz.PARSE_RESPONSE_ERROR.toString(), "Unsupported encoding", e22.getMessage()));
                }
                if (!this.f226e.mo2936b(cwVar)) {
                    this.f223b.mo2157a(cwVar);
                }
            }
        }
        new StringBuilder().append(getClass().getSimpleName()).append(" exiting");
    }
}
