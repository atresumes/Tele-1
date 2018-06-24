package com.sinch.android.rtc.messaging;

public interface MessageClient {
    void addMessageClientListener(MessageClientListener messageClientListener);

    void removeMessageClientListener(MessageClientListener messageClientListener);

    void send(WritableMessage writableMessage);
}
