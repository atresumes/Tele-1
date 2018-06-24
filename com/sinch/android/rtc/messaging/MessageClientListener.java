package com.sinch.android.rtc.messaging;

import com.sinch.android.rtc.PushPair;
import java.util.List;

public interface MessageClientListener {
    void onIncomingMessage(MessageClient messageClient, Message message);

    void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo);

    void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo);

    void onMessageSent(MessageClient messageClient, Message message, String str);

    void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list);
}
