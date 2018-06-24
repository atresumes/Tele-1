package com.sinch.android.rtc.internal;

import android.os.Handler;
import android.os.Looper;

public class AndroidLooperCallbackHandler implements CallbackHandler {
    Handler mHandler;

    public AndroidLooperCallbackHandler() {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            throw new IllegalThreadStateException("A Looper must be associated with this thread.");
        }
        this.mHandler = new Handler(myLooper);
    }

    public boolean post(Runnable runnable) {
        return this.mHandler.post(runnable);
    }

    public void postDelayed(Runnable runnable, int i) {
        this.mHandler.postDelayed(runnable, (long) i);
    }

    public void removeCallbacksAndMessages(Object obj) {
        this.mHandler.removeCallbacksAndMessages(obj);
    }
}
