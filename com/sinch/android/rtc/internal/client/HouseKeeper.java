package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import com.sinch.android.rtc.internal.service.dispatcher.DefaultDispatcher;

public class HouseKeeper extends UserAgentMethodInvocationScheduler {
    public HouseKeeper(DefaultDispatcher defaultDispatcher, UserAgent userAgent) {
        super(defaultDispatcher, userAgent, "Sinch SDK HouseKeeper");
    }

    protected void onRun(UserAgent userAgent) {
        userAgent.doHouseKeeping();
    }
}
