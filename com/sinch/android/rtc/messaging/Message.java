package com.sinch.android.rtc.messaging;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Message {
    Map<String, String> getHeaders();

    String getMessageId();

    List<String> getRecipientIds();

    String getSenderId();

    String getTextBody();

    Date getTimestamp();
}
