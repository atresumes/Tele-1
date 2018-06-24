package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.PubSubscriber;
import java.util.List;

public class NativePubSubscriber extends NativeProxy implements PubSubscriber {
    private NativePubSubscriber(long j) {
        super(j);
    }

    public static synchronized NativePubSubscriber createInstance(long j) {
        NativePubSubscriber nativePubSubscriber;
        synchronized (NativePubSubscriber.class) {
            nativePubSubscriber = (NativePubSubscriber) NativeProxy.get(j, NativePubSubscriber.class);
            if (nativePubSubscriber == null) {
                nativePubSubscriber = new NativePubSubscriber(j);
                NativeProxy.put(j, nativePubSubscriber);
            }
        }
        return nativePubSubscriber;
    }

    public native void dispose();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NativePubSubscriber nativePubSubscriber = (NativePubSubscriber) obj;
        if (getChannels() == null) {
            if (nativePubSubscriber.getChannels() != null) {
                return false;
            }
        } else if (!getChannels().equals(nativePubSubscriber.getChannels())) {
            return false;
        }
        if (getPublishKey() == null) {
            if (nativePubSubscriber.getPublishKey() != null) {
                return false;
            }
        } else if (!getPublishKey().equals(nativePubSubscriber.getPublishKey())) {
            return false;
        }
        return getSubscribeKey() == null ? nativePubSubscriber.getSubscribeKey() == null : getSubscribeKey().equals(nativePubSubscriber.getSubscribeKey());
    }

    public native List<String> getChannels();

    public native String getPublishKey();

    public native String getSubscribeKey();

    public native void handleData(String str, String str2, String str3);

    public native void handleFailure(String str);

    public int hashCode() {
        int i = 0;
        int hashCode = ((getPublishKey() == null ? 0 : getPublishKey().hashCode()) + (((getChannels() == null ? 0 : getChannels().hashCode()) + 31) * 31)) * 31;
        if (getSubscribeKey() != null) {
            i = getSubscribeKey().hashCode();
        }
        return hashCode + i;
    }
}
