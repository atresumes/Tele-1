package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.internal.natives.jni.Session;
import java.util.List;

public interface SessionEventListener {
    void onConnectionInfo(Session session, ConnectionInfo connectionInfo);

    void onEstablished(Session session);

    void onProgressing(Session session);

    void onPushData(Session session, List<PushPair> list);

    void onSignalStrength(Session session, int i, int i2, int i3, int i4);

    void onTerminated(Session session);

    void onTransferCompleted(Session session, boolean z, SinchError sinchError, AccessNumber accessNumber);

    void onTransferring(Session session);

    void onVideoTrackAdded(Session session, Object obj);

    void onVideoTrackPaused(Session session);

    void onVideoTrackResumed(Session session);
}
