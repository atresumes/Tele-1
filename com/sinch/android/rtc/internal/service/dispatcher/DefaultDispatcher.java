package com.sinch.android.rtc.internal.service.dispatcher;

import com.sinch.android.rtc.internal.CallbackHandler;
import com.sinch.android.rtc.internal.natives.Dispatchable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DefaultDispatcher implements Dispatcher {
    private final CallbackHandler callbackHandler;
    private final Set<Dispatchable> dispatchables = Collections.synchronizedSet(new HashSet());
    private boolean mStopped;

    class Runner implements Runnable {
        private Dispatchable function;
        private final boolean mShouldRun;

        public Runner(Dispatchable dispatchable, boolean z) {
            this.function = dispatchable;
            this.mShouldRun = z;
        }

        public void run() {
            DefaultDispatcher.this.dispatchables.remove(this.function);
            if (this.mShouldRun) {
                this.function.run();
            }
            DefaultDispatcher.this.disposeDispatchable(this.function);
        }
    }

    public DefaultDispatcher(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    private void disposeDispatchable(Dispatchable dispatchable) {
        dispatchable.dispose();
        dispatchable.invalidate();
    }

    private void removeAllDispatchables() {
        this.callbackHandler.removeCallbacksAndMessages(null);
        Iterator it = this.dispatchables.iterator();
        while (it.hasNext()) {
            disposeDispatchable((Dispatchable) it.next());
            it.remove();
        }
    }

    public void dispatchOnMainThread(Dispatchable dispatchable) {
        this.dispatchables.add(dispatchable);
        if (this.mStopped) {
            this.callbackHandler.post(new Runner(dispatchable, false));
        } else {
            this.callbackHandler.post(new Runner(dispatchable, true));
        }
    }

    public void stop() {
        this.mStopped = true;
        removeAllDispatchables();
    }
}
