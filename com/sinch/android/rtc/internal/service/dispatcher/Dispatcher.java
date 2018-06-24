package com.sinch.android.rtc.internal.service.dispatcher;

import com.sinch.android.rtc.internal.natives.Dispatchable;

public interface Dispatcher {
    void dispatchOnMainThread(Dispatchable dispatchable);
}
