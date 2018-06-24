package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.internal.natives.jni.NativeMessage;
import java.util.List;

public interface MessagingEventListener {
    void onIncomingMessage(NativeMessage nativeMessage);

    void onMessageDelivered(MessageInfo messageInfo);

    void onMessageFailed(NativeMessage nativeMessage, MessageError messageError);

    void onMessageSent(NativeMessage nativeMessage, MessageInfo messageInfo);

    void onShouldSendPushData(NativeMessage nativeMessage, List<PushPayloadDataPair> list);
}
