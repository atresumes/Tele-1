package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.natives.PubSubscriber;

public interface CallbackHandlerFactory {
    void createCallback(PubSubscriber pubSubscriber);
}
