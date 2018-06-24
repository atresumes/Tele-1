package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.natives.PubSubscriber;
import java.util.List;
import java.util.concurrent.Executor;

public class PubNubSubscriberFactory {
    public PubNubSubscriber createSubscriber(String str, String str2, List<String> list, String str3, int i, PubSubscriber pubSubscriber, Executor executor) {
        return new PubNubSubscriber(str, str2, list, str3, i, pubSubscriber, executor);
    }
}
