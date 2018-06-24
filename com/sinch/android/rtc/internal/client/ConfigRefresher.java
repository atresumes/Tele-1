package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import com.sinch.android.rtc.internal.service.dispatcher.Dispatcher;

public class ConfigRefresher extends UserAgentMethodInvocationScheduler {
    public ConfigRefresher(Dispatcher dispatcher, UserAgent userAgent) {
        super(dispatcher, userAgent, "Sinch SDK ConfigRefresher");
    }

    protected void onRun(UserAgent userAgent) {
        userAgent.refreshConfig();
    }
}
