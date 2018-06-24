package com.sinch.android.rtc.internal.client.calling;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.bumptech.glide.load.Key;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.internal.client.DefaultCall;
import com.sinch.android.rtc.internal.client.SinchClientStatusProvider;
import com.sinch.android.rtc.internal.client.SinchLogger;
import com.sinch.android.rtc.internal.client.video.VideoTrackListenerInternal;
import com.sinch.android.rtc.internal.natives.AccessNumber;
import com.sinch.android.rtc.internal.natives.ConnectionInfo;
import com.sinch.android.rtc.internal.natives.MediaOfferFlag;
import com.sinch.android.rtc.internal.natives.SessionEventListener;
import com.sinch.android.rtc.internal.natives.jni.CallClientListener;
import com.sinch.android.rtc.internal.natives.jni.NativeCallClient;
import com.sinch.android.rtc.internal.natives.jni.PushPayloadQuery.PushPayloadQueryResult;
import com.sinch.android.rtc.internal.natives.jni.Session;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultCallClient implements CallClient, SessionEventListener, CallClientListener {
    static final /* synthetic */ boolean $assertionsDisabled = (!DefaultCallClient.class.desiredAssertionStatus());
    public static final String APP_USER_DOMAIN = "";
    public static final String CONFERENCE_DOMAIN = "conference";
    private static final int HEADER_MAX_SIZE = 1024;
    public static final String PSTN_DOMAIN = "pstn";
    public static final String SIP_DOMAIN = "sip";
    private static final String TAG = DefaultCallClient.class.getSimpleName();
    private final Map<Session, DefaultCall> activeCalls = Collections.synchronizedMap(new HashMap());
    private final Map<String, DefaultCall> callsHandled = Collections.synchronizedMap(new HashMap());
    private final com.sinch.android.rtc.internal.natives.jni.CallClient mCallClient;
    private final CopyOnWriteArraySet<com.sinch.android.rtc.calling.CallClientListener> mCallClientListeners = new CopyOnWriteArraySet();
    private Context mContext;
    private SinchLogger mLogger;
    private SinchClientStatusProvider mSinchClientStatusProvider;
    private VideoTrackListenerInternal mVideoTrackListener;
    private PhoneListener phoneListener;
    private boolean respectNativeCalls = true;

    class PhoneListener extends PhoneStateListener {
        private PhoneListener() {
        }

        public void onCallStateChanged(int i, String str) {
            if (i == 2) {
                synchronized (DefaultCallClient.this.activeCalls) {
                    for (Entry value : DefaultCallClient.this.activeCalls.entrySet()) {
                        ((DefaultCall) value.getValue()).hangup();
                    }
                }
                unlisten();
            }
        }

        public void unlisten() {
            ((TelephonyManager) DefaultCallClient.this.mContext.getSystemService("phone")).listen(this, 0);
            DefaultCallClient.this.phoneListener = null;
        }
    }

    public DefaultCallClient(SinchLogger sinchLogger, SinchClientStatusProvider sinchClientStatusProvider, com.sinch.android.rtc.internal.natives.jni.CallClient callClient, Context context) {
        this.mLogger = sinchLogger;
        this.mSinchClientStatusProvider = sinchClientStatusProvider;
        this.mCallClient = callClient;
        this.mCallClient.setListener(this);
        this.mContext = context;
    }

    private Call call(String str, String str2, Map<String, String> map, MediaOfferFlag mediaOfferFlag) {
        throwUnlessStarted();
        throwIfMissingPermission();
        if (str == null || "".equals(str)) {
            throw new IllegalArgumentException("toUserId must not be null or empty");
        } else if (this.mSinchClientStatusProvider.isSupportCalling()) {
            Map hashMap;
            if (map == null) {
                hashMap = new HashMap();
            }
            throwIfHeadersSizeLimitExceeded(hashMap);
            String[] strArr = (String[]) hashMap.keySet().toArray(new String[hashMap.size()]);
            String[] strArr2 = (String[]) hashMap.values().toArray(new String[hashMap.size()]);
            Session createOutgoingCall = this.mCallClient.createOutgoingCall(UUID.randomUUID().toString(), str, str2, strArr, strArr2, mediaOfferFlag.ordinal());
            Call createCall = createCall(createOutgoingCall);
            createOutgoingCall.start();
            return createCall;
        } else {
            throw new IllegalStateException("Calling is not enabled");
        }
    }

    private Call createCall(Session session) {
        session.setEventListener(this);
        Call defaultCall = new DefaultCall(this.mLogger, session, this.mContext);
        this.activeCalls.put(session, defaultCall);
        this.callsHandled.put(session.getSessionId(), defaultCall);
        return defaultCall;
    }

    private void setupPhoneStateListener() {
        if (this.respectNativeCalls && this.phoneListener == null && this.activeCalls.size() > 0 && this.mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            this.phoneListener = new PhoneListener();
            ((TelephonyManager) this.mContext.getSystemService("phone")).listen(this.phoneListener, 32);
        }
    }

    private void throwIfDisposed() {
        if (this.mSinchClientStatusProvider.isDisposed()) {
            throw new IllegalStateException("SinchClient is stopped, you may not perform further actions until it is recreated");
        }
    }

    private void throwIfHeadersSizeLimitExceeded(Map<String, String> map) {
        int i = 0;
        for (Entry entry : map.entrySet()) {
            try {
                int length = ((String) entry.getValue()).getBytes(Key.STRING_CHARSET_NAME).length + (((String) entry.getKey()).getBytes(Key.STRING_CHARSET_NAME).length + i);
                if (length > 1024) {
                    throw new IllegalArgumentException("Header size limit exceeded 1024 bytes");
                }
                i = length;
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Could not encode headers as UTF-8");
            }
        }
    }

    private void throwIfMissingPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            throw new MissingPermissionException("android.permission.RECORD_AUDIO");
        }
    }

    private void throwUnlessStarted() {
        throwIfDisposed();
        if (!this.mSinchClientStatusProvider.isStarted()) {
            throw new IllegalStateException("SinchClient not started");
        }
    }

    public void addCallClientListener(com.sinch.android.rtc.calling.CallClientListener callClientListener) {
        this.mCallClientListeners.add(callClientListener);
    }

    public Call callConference(String str) {
        return callConference(str, null);
    }

    public Call callConference(String str, Map<String, String> map) {
        if (str.length() <= 64) {
            return call(str, CONFERENCE_DOMAIN, map, MediaOfferFlag.AudioOnly);
        }
        throw new IllegalArgumentException("conferenceId must be at most 64 characters");
    }

    public Call callPhoneNumber(String str) {
        return callPhoneNumber(str, null);
    }

    public Call callPhoneNumber(String str, Map<String, String> map) {
        return call(str, PSTN_DOMAIN, map, MediaOfferFlag.AudioOnly);
    }

    public Call callSip(String str) {
        return callSip(str, null);
    }

    public Call callSip(String str, Map<String, String> map) {
        return call(str, SIP_DOMAIN, map, MediaOfferFlag.AudioOnly);
    }

    public Call callUser(String str) {
        return callUser(str, null);
    }

    public Call callUser(String str, Map<String, String> map) {
        return call(str, "", map, MediaOfferFlag.AudioOnly);
    }

    public Call callUserVideo(String str) {
        return callUserVideo(str, null);
    }

    public Call callUserVideo(String str, Map<String, String> map) {
        return call(str, "", map, MediaOfferFlag.AudioAndVideo);
    }

    public Call getCall(String str) {
        if (str == null) {
            return null;
        }
        for (Entry entry : this.callsHandled.entrySet()) {
            if (str.equalsIgnoreCase((String) entry.getKey())) {
                return (Call) entry.getValue();
            }
        }
        return null;
    }

    public void handleCallPushPayload(PushPayloadQueryResult pushPayloadQueryResult) {
        if (pushPayloadQueryResult.isValid() && !pushPayloadQueryResult.isTimedOut()) {
            throwUnlessStarted();
            if (!this.callsHandled.keySet().contains(pushPayloadQueryResult.getSessionId())) {
                Session createIncomingCall = this.mCallClient.createIncomingCall(pushPayloadQueryResult.getSessionId(), pushPayloadQueryResult.getUserId(), (pushPayloadQueryResult.getVideoOffered() ? MediaOfferFlag.AudioAndVideo : MediaOfferFlag.AudioOnly).ordinal());
                createCall(createIncomingCall);
                this.mLogger.mo2263d(TAG, "Incoming session created: " + createIncomingCall.getSessionId());
                if (!this.respectNativeCalls) {
                    createIncomingCall.start();
                } else if (((TelephonyManager) this.mContext.getSystemService("phone")).getCallState() != 0) {
                    createIncomingCall.terminate();
                } else {
                    createIncomingCall.start();
                }
            }
        }
    }

    public boolean isRespectNativeCalls() {
        return this.respectNativeCalls;
    }

    public void notifyIncomingCall(Call call) {
        Iterator it = this.mCallClientListeners.iterator();
        while (it.hasNext()) {
            ((com.sinch.android.rtc.calling.CallClientListener) it.next()).onIncomingCall(this, call);
        }
    }

    public void onConnectionInfo(Session session, ConnectionInfo connectionInfo) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall != null) {
            defaultCall.onConnectionInfo(defaultCall, connectionInfo);
        }
    }

    public void onEstablished(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall != null) {
            setupPhoneStateListener();
            defaultCall.onSessionEstablished();
        }
    }

    public void onIncomingCall(Session session) {
        this.mLogger.mo2263d(TAG, "onIncomingSession " + session.getSessionId());
        if (!this.respectNativeCalls || ((TelephonyManager) this.mContext.getSystemService("phone")).getCallState() == 0) {
            notifyIncomingCall(this.activeCalls.containsKey(session) ? (Call) this.activeCalls.get(session) : createCall(session));
        } else {
            session.terminate();
        }
    }

    public void onProgressing(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall != null) {
            defaultCall.onSessionProgressing();
        }
    }

    public void onPushData(Session session, List<PushPair> list) {
        if ($assertionsDisabled || list.size() > 0) {
            DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
            if (defaultCall != null) {
                defaultCall.onPushData(list);
                return;
            } else {
                this.mLogger.mo2309w(TAG, "Got onPushData callback for unknown call");
                return;
            }
        }
        throw new AssertionError();
    }

    public void onSignalStrength(Session session, int i, int i2, int i3, int i4) {
        this.mLogger.mo2263d(TAG, "onSignalStrength: " + session.getSessionId());
    }

    public void onTerminated(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.remove(session);
        if (defaultCall != null) {
            defaultCall.onSessionTerminated();
            if (this.respectNativeCalls && this.activeCalls.isEmpty() && this.phoneListener != null) {
                this.phoneListener.unlisten();
            }
        }
    }

    public void onTransferCompleted(Session session, boolean z, SinchError sinchError, AccessNumber accessNumber) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall != null) {
            defaultCall.onSessionTransferCompleted(z, sinchError, accessNumber);
        }
    }

    public void onTransferring(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall != null) {
            defaultCall.onSessionTransferring();
        }
    }

    public void onVideoTrackAdded(Session session, Object obj) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall == null) {
            this.mLogger.mo2309w(TAG, "Got VideoTrackAdded for unknown call");
            return;
        }
        if (this.mVideoTrackListener != null) {
            this.mVideoTrackListener.onVideoTrackAddedInternal(session.getSessionId());
        }
        defaultCall.onVideoTrackAdded();
    }

    public void onVideoTrackPaused(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall == null) {
            this.mLogger.mo2309w(TAG, "Got VideoTrackPaused for unknown call");
        } else {
            defaultCall.onVideoTrackPaused();
        }
    }

    public void onVideoTrackResumed(Session session) {
        DefaultCall defaultCall = (DefaultCall) this.activeCalls.get(session);
        if (defaultCall == null) {
            this.mLogger.mo2309w(TAG, "Got VideoTrackPaused for unknown call");
        } else {
            defaultCall.onVideoTrackResumed();
        }
    }

    public void removeCallClientListener(com.sinch.android.rtc.calling.CallClientListener callClientListener) {
        this.mCallClientListeners.remove(callClientListener);
    }

    public void setRespectNativeCalls(boolean z) {
        throwIfDisposed();
        if (this.mSinchClientStatusProvider.isStarted()) {
            throw new IllegalStateException("This setting needs to be set before starting the SinchClient");
        }
        this.respectNativeCalls = z;
    }

    public void setVideoTrackListener(VideoTrackListenerInternal videoTrackListenerInternal) {
        this.mVideoTrackListener = videoTrackListenerInternal;
    }

    public void terminate() {
        this.mCallClient.setListener(null);
        if (this.mCallClient instanceof NativeCallClient) {
            ((NativeCallClient) this.mCallClient).invalidate();
        }
    }
}
