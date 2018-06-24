package com.sinch.android.rtc.internal.service.pubnub;

import java.util.List;

public class Subscription {
    private final List<String> channels;
    private final String subscriptionKey;
    private final int timeOffsetOnSubscribeInMs;
    private final String timeToken;

    public Subscription(List<String> list, String str) {
        this.channels = list;
        this.subscriptionKey = str;
        this.timeToken = "0";
        this.timeOffsetOnSubscribeInMs = 0;
    }

    public Subscription(List<String> list, String str, int i) {
        this.channels = list;
        this.subscriptionKey = str;
        this.timeToken = "0";
        this.timeOffsetOnSubscribeInMs = i;
    }

    public Subscription(List<String> list, String str, String str2) {
        this.channels = list;
        this.subscriptionKey = str;
        this.timeToken = str2;
        this.timeOffsetOnSubscribeInMs = 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Subscription subscription = (Subscription) obj;
        if (this.channels == null) {
            if (subscription.channels != null) {
                return false;
            }
        } else if (!this.channels.equals(subscription.channels)) {
            return false;
        }
        return this.subscriptionKey == null ? subscription.subscriptionKey == null : this.subscriptionKey.equals(subscription.subscriptionKey);
    }

    public List<String> getChannels() {
        return this.channels;
    }

    public String getSubscriptionKey() {
        return this.subscriptionKey;
    }

    public int getTimeOffsetOnSubscribeInMs() {
        return this.timeOffsetOnSubscribeInMs;
    }

    public String getTimeToken() {
        return this.timeToken;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.channels == null ? 0 : this.channels.hashCode()) + 31) * 31;
        if (this.subscriptionKey != null) {
            i = this.subscriptionKey.hashCode();
        }
        return hashCode + i;
    }
}
