package com.sinch.android.rtc.internal.client;

public enum InternalCapability {
    VOIP("voip"),
    IM("im"),
    PUSH("push"),
    P2P("p2p"),
    ONLINE("online"),
    SRTP("srtp");
    
    private final String capability;

    private InternalCapability(String str) {
        this.capability = str;
    }

    public static InternalCapability getFromString(String str) {
        for (InternalCapability internalCapability : values()) {
            if (internalCapability.toString().equals(str)) {
                return internalCapability;
            }
        }
        return null;
    }

    public String toString() {
        return this.capability;
    }
}
