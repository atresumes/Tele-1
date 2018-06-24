package com.sinch.android.rtc.internal.natives;

public class ObjectRef {
    private Object ref;

    public Object get() {
        return this.ref;
    }

    public void set(Object obj) {
        this.ref = obj;
    }
}
