package com.sinch.android.rtc.internal.natives;

public class PubSubChannel {
    private String channel;
    private long historyMs;

    public PubSubChannel(String str, long j) {
        this.channel = str;
        this.historyMs = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PubSubChannel pubSubChannel = (PubSubChannel) obj;
        return getChannel() == null ? pubSubChannel.getChannel() == null : getChannel().equals(pubSubChannel.getChannel());
    }

    public String getChannel() {
        return this.channel;
    }

    public long getHistoryMs() {
        return this.historyMs;
    }

    public int hashCode() {
        return (getChannel() == null ? 0 : getChannel().hashCode()) + 31;
    }
}
