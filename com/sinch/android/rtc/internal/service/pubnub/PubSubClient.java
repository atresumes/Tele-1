package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.natives.PubPublisher;
import com.sinch.android.rtc.internal.natives.PubSubHistoryConsumer;
import com.sinch.android.rtc.internal.natives.PubSubscriber;

public interface PubSubClient {
    void beginBroadcastHistoryGet(PubSubHistoryConsumer pubSubHistoryConsumer, double d);

    void getHistory(PubSubHistoryConsumer pubSubHistoryConsumer, int i, String str, String str2, boolean z);

    String getHost();

    void publish(String str, String str2, String str3, String str4, PubPublisher pubPublisher);

    void setHost(String str);

    void setUseSsl(boolean z);

    void startPubSubClient();

    void startSubscribe(PubSubscriber pubSubscriber, double d);

    void startSubscribe(PubSubscriber pubSubscriber, String str);

    void stopAllHistoryGet();

    void stopPubSubClient();

    void stopSubscribe(PubSubscriber pubSubscriber);

    void stopSubscribersAndHistory();
}
