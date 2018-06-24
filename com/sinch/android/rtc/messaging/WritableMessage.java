package com.sinch.android.rtc.messaging;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public final class WritableMessage {
    private static final int HEADER_MAX_SIZE = 1024;
    private int headerSize;
    private Map<String, String> headers;
    private final String messageId;
    private List<String> recipientIds;
    private String textBody;

    public WritableMessage() {
        this.messageId = UUID.randomUUID().toString();
        this.recipientIds = new ArrayList();
        this.headers = new HashMap();
        this.headerSize = 0;
    }

    public WritableMessage(Message message) {
        this();
        this.recipientIds = message.getRecipientIds();
        this.textBody = message.getTextBody();
        for (Entry entry : message.getHeaders().entrySet()) {
            addHeader((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public WritableMessage(String str, String str2) {
        this();
        addRecipient(str);
        setTextBody(str2);
    }

    public WritableMessage(List<String> list, String str) {
        this();
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List most not be null nor empty.");
        }
        this.recipientIds = list;
        setTextBody(str);
    }

    public void addHeader(String str, String str2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Must have non-empty key.");
        } else if (str2 == null) {
            throw new IllegalArgumentException("Must have non-null value.");
        } else if (this.headers.containsKey(str)) {
            throw new IllegalArgumentException("Must have unique headers keys.");
        } else {
            try {
                byte[] bytes = str.getBytes(Key.STRING_CHARSET_NAME);
                byte[] bytes2 = str2.getBytes(Key.STRING_CHARSET_NAME);
                this.headerSize = bytes.length + this.headerSize;
                this.headerSize += bytes2.length;
            } catch (UnsupportedEncodingException e) {
                this.headerSize += str.length();
                this.headerSize += str2.length();
            }
            if (this.headerSize > 1024) {
                throw new IllegalStateException("Header size exceeds max size (1KB)");
            }
            this.headers.put(str, str2);
        }
    }

    public void addRecipient(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Must have non-empty userId.");
        }
        this.recipientIds.add(str);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public List<String> getRecipientIds() {
        return this.recipientIds;
    }

    public String getTextBody() {
        return this.textBody;
    }

    public void setTextBody(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Must have non-null textBody.");
        }
        this.textBody = str;
    }
}
