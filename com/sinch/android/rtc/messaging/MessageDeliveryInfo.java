package com.sinch.android.rtc.messaging;

import java.util.Date;

public interface MessageDeliveryInfo {
    String getMessageId();

    String getRecipientId();

    Date getTimestamp();
}
