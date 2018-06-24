package com.sinch.android.rtc.calling;

public enum CallEndCause {
    NONE(0),
    TIMEOUT(1),
    DENIED(2),
    NO_ANSWER(3),
    FAILURE(4),
    HUNG_UP(5),
    CANCELED(6),
    OTHER_DEVICE_ANSWERED(7),
    TRANSFERRED(8);
    
    private int value;

    private CallEndCause(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }
}
