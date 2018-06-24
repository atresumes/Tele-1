package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.internal.natives.Dispatchable;
import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import com.sinch.android.rtc.internal.service.dispatcher.Dispatcher;

abstract class UserAgentMethodInvocationScheduler extends Scheduler {
    private Dispatcher dispatcher;
    private UserAgent userAgent;

    class C09221 implements Dispatchable {
        C09221() {
        }

        public void dispose() {
        }

        public void invalidate() {
        }

        public void run() {
            if (UserAgentMethodInvocationScheduler.this.isRunning() && UserAgentMethodInvocationScheduler.this.userAgent != null) {
                UserAgentMethodInvocationScheduler.this.onRun(UserAgentMethodInvocationScheduler.this.userAgent);
            }
        }
    }

    public UserAgentMethodInvocationScheduler(Dispatcher dispatcher, UserAgent userAgent) {
        this.dispatcher = dispatcher;
        this.userAgent = userAgent;
    }

    public UserAgentMethodInvocationScheduler(Dispatcher dispatcher, UserAgent userAgent, String str) {
        super(str);
        this.dispatcher = dispatcher;
        this.userAgent = userAgent;
    }

    protected void onDispose() {
        this.dispatcher = null;
        this.userAgent = null;
    }

    protected void onRun() {
        if (this.dispatcher != null) {
            this.dispatcher.dispatchOnMainThread(new C09221());
        }
    }

    protected abstract void onRun(UserAgent userAgent);
}
