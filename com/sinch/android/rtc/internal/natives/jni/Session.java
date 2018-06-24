package com.sinch.android.rtc.internal.natives.jni;

import android.util.Log;
import com.sinch.android.rtc.internal.natives.SessionDetails;
import com.sinch.android.rtc.internal.natives.SessionEventListener;
import java.util.Map;

public class Session extends NativeProxy {
    private Session(long j) {
        super(j);
    }

    private static synchronized Session createInstance(long j) {
        Session session;
        synchronized (Session.class) {
            session = (Session) NativeProxy.get(j, Session.class);
            if (session == null) {
                session = new Session(j);
                NativeProxy.put(j, session);
            }
        }
        return session;
    }

    public native void accept();

    public native void dispose();

    public native void enableVideoTrack(boolean z);

    protected void finalize() {
        try {
            Log.d("Session", "Disposing session object with address " + getNativeAddress());
            dispose();
            invalidate();
        } finally {
            super.finalize();
        }
    }

    public native SessionDetails getDetails();

    public native int getDirection();

    public native SessionEventListener getEventListener();

    public native Map<String, String> getHeaders();

    public native String getRemoteUserCLI();

    public native String getRemoteUserId();

    public native String getSessionId();

    public native int getState();

    public native void sendDtmf(String str);

    public native void setEventListener(SessionEventListener sessionEventListener);

    public native void start();

    public native void terminate();

    public native void transfer();
}
