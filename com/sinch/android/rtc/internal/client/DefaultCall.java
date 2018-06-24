package com.sinch.android.rtc.internal.client;

import android.content.Context;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallDetails;
import com.sinch.android.rtc.calling.CallDirection;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.internal.natives.AccessNumber;
import com.sinch.android.rtc.internal.natives.ConnectionInfo;
import com.sinch.android.rtc.internal.natives.SessionDetails;
import com.sinch.android.rtc.internal.natives.jni.Session;
import com.sinch.android.rtc.video.VideoCallListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultCall implements Call {
    private static final String TAG = Call.class.getSimpleName();
    private static String validDtmf = "0123456789*#ABCD";
    private boolean isValidForActions = true;
    private CopyOnWriteArraySet<CallListener> listeners = new CopyOnWriteArraySet();
    private final SinchLogger logger;
    private Context mContext;
    private DtmfPlayer mDtmfFeedback;
    private final Session session;

    public DefaultCall(SinchLogger sinchLogger, Session session, Context context) {
        this.logger = sinchLogger;
        this.session = session;
        this.mContext = context;
    }

    private void validateDtmf(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (validDtmf.indexOf(str.charAt(i)) < 0) {
                throw new IllegalArgumentException("keys can only contain the following characters: " + validDtmf);
            }
        }
    }

    public void addCallListener(CallListener callListener) {
        this.listeners.add(callListener);
    }

    public void answer() {
        if (!this.isValidForActions) {
            return;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            throw new MissingPermissionException("android.permission.RECORD_AUDIO");
        }
        this.session.accept();
    }

    public String getCallId() {
        return this.session.getSessionId();
    }

    public CallDetails getDetails() {
        return new DefaultCallDetails(this.session.getDetails());
    }

    public CallDirection getDirection() {
        return this.session.getDirection() == 0 ? CallDirection.INCOMING : CallDirection.OUTGOING;
    }

    public Map<String, String> getHeaders() {
        return this.session.getHeaders();
    }

    public String getRemoteUserDisplayName() {
        return this.session.getRemoteUserCLI();
    }

    public String getRemoteUserId() {
        return this.session.getRemoteUserId();
    }

    public CallState getState() {
        int state = this.session.getState();
        if (state >= 0 && state < CallState.values().length) {
            return CallState.values()[state];
        }
        throw new IllegalStateException("Illegal Call State exception with number:" + state);
    }

    public void hangup() {
        if (this.isValidForActions) {
            this.session.terminate();
        }
    }

    public void onConnectionInfo(Call call, ConnectionInfo connectionInfo) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof InternalCallListener) {
                ((InternalCallListener) callListener).onConnectionInfo(call, connectionInfo);
            }
        }
    }

    public void onPushData(List<PushPair> list) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((CallListener) it.next()).onShouldSendPushNotification(this, list);
        }
    }

    public void onSessionEstablished() {
        SessionDetails details = this.session.getDetails();
        if (details != null) {
            this.logger.mo2263d(TAG, "onSessionEstablished: " + this.session.getSessionId() + ": " + details.toString());
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((CallListener) it.next()).onCallEstablished(this);
        }
    }

    public void onSessionProgressing() {
        SessionDetails details = this.session.getDetails();
        if (details != null) {
            this.logger.mo2263d(TAG, "onSessionProgressing: " + this.session.getSessionId() + ": " + details.toString());
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof InternalCallListener) {
                ((InternalCallListener) callListener).onCallProgressing(this);
            } else {
                callListener.onCallProgressing(this);
            }
        }
    }

    public void onSessionTerminated() {
        this.isValidForActions = false;
        SessionDetails details = this.session.getDetails();
        if (details != null) {
            this.logger.mo2263d(TAG, "onSessionTerminated: " + this.session.getSessionId() + ": " + details.toString());
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((CallListener) it.next()).onCallEnded(this);
        }
    }

    public void onSessionTransferCompleted(boolean z, SinchError sinchError, AccessNumber accessNumber) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof InternalCallListener) {
                if (z) {
                    ((InternalCallListener) callListener).onCallTransferSucceeded(this, accessNumber);
                } else {
                    ((InternalCallListener) callListener).onCallTransferFailed(this, sinchError);
                }
            }
        }
    }

    public void onSessionTransferring() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof InternalCallListener) {
                ((InternalCallListener) callListener).onCallTransferringOnRemoteEnd(this);
            }
        }
    }

    public void onVideoTrackAdded() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof VideoCallListener) {
                ((VideoCallListener) callListener).onVideoTrackAdded(this);
            }
        }
    }

    public void onVideoTrackPaused() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof VideoCallListener) {
                ((VideoCallListener) callListener).onVideoTrackPaused(this);
            }
        }
    }

    public void onVideoTrackResumed() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            CallListener callListener = (CallListener) it.next();
            if (callListener instanceof VideoCallListener) {
                ((VideoCallListener) callListener).onVideoTrackResumed(this);
            }
        }
    }

    public void pauseVideo() {
        if (this.isValidForActions) {
            this.session.enableVideoTrack(false);
        }
    }

    public void removeCallListener(CallListener callListener) {
        this.listeners.remove(callListener);
    }

    public void resumeVideo() {
        if (this.isValidForActions) {
            this.session.enableVideoTrack(true);
        }
    }

    public void sendDTMF(String str) {
        if (this.isValidForActions) {
            validateDtmf(str);
            this.session.sendDtmf(str);
            if (this.mDtmfFeedback == null) {
                this.mDtmfFeedback = new DtmfPlayer();
            }
            this.mDtmfFeedback.play(str);
        }
    }

    public void transfer() {
        if (this.isValidForActions) {
            this.session.transfer();
        }
    }
}
