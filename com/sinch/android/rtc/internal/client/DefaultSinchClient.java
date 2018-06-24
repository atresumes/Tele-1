package com.sinch.android.rtc.internal.client;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Base64;
import com.bumptech.glide.load.Key;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.ErrorType;
import com.sinch.android.rtc.MissingGCMException;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.internal.CallbackHandler;
import com.sinch.android.rtc.internal.Capability;
import com.sinch.android.rtc.internal.DefaultSinchError;
import com.sinch.android.rtc.internal.InternalErrorCodes;
import com.sinch.android.rtc.internal.SinchClientPrivate;
import com.sinch.android.rtc.internal.UserCapabilitiesCallback;
import com.sinch.android.rtc.internal.client.calling.DefaultCallClient;
import com.sinch.android.rtc.internal.client.gcm.GcmRegistrationCallback;
import com.sinch.android.rtc.internal.client.gcm.GetDeviceTokenTask;
import com.sinch.android.rtc.internal.client.gcm.PersistedToken;
import com.sinch.android.rtc.internal.client.gcm.TokenRefreshCallback;
import com.sinch.android.rtc.internal.client.gcm.TokenRefreshTask;
import com.sinch.android.rtc.internal.client.libloader.NativeLibLoader;
import com.sinch.android.rtc.internal.client.messaging.DefaultMessageClient;
import com.sinch.android.rtc.internal.client.model.DefaultNotificationResult;
import com.sinch.android.rtc.internal.client.video.DefaultVideoController;
import com.sinch.android.rtc.internal.client.video.VideoControllerInternal;
import com.sinch.android.rtc.internal.client.video.VideoFrameSink;
import com.sinch.android.rtc.internal.client.video.VideoTrackListenerInternal;
import com.sinch.android.rtc.internal.natives.LogLevel;
import com.sinch.android.rtc.internal.natives.ObjectRef;
import com.sinch.android.rtc.internal.natives.UserAgentEventListener;
import com.sinch.android.rtc.internal.natives.UserAgentState;
import com.sinch.android.rtc.internal.natives.jni.DefaultUserAgent;
import com.sinch.android.rtc.internal.natives.jni.PushPayloadQuery;
import com.sinch.android.rtc.internal.natives.jni.PushPayloadQuery.PushPayloadQueryResult;
import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import com.sinch.android.rtc.internal.service.crypto.DefaultCryptoService;
import com.sinch.android.rtc.internal.service.dispatcher.DefaultDispatcher;
import com.sinch.android.rtc.internal.service.http.HttpService;
import com.sinch.android.rtc.internal.service.http.SinchHttpServiceObserver;
import com.sinch.android.rtc.internal.service.persistence.PersistenceService;
import com.sinch.android.rtc.internal.service.pubnub.PublishSubscribeClient;
import com.sinch.android.rtc.internal.service.serviceprovider.DefaultServiceProvider;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.video.VideoController;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import org.webrtc.sinch.AudioDeviceUtil;
import org.webrtc.sinch.WebRTCDriver;

public class DefaultSinchClient implements SinchClient, SinchClientPrivate, ConnectivityListenerCallback, SinchClientStatusProvider, SinchLogger, GcmRegistrationCallback, TokenRefreshCallback, UserAgentEventListener, SinchHttpServiceObserver {
    private static final int DEFAULT_DO_HOUSE_KEEPING_DELAY = 2000;
    private static final String DEFAULT_PREFERENCE_NAME = "RebtelPersistence";
    private static final int DEFAULT_REFRESH_CONFIG_DELAY = 43200000;
    private static final int DEVICE_TYPE_ANDROID = 2;
    public static final String GCM_PAYLOAD_TAG_DISPLAYNAME = "name";
    public static final String GCM_PAYLOAD_TAG_MXP = "mxp";
    public static final String GCM_PAYLOAD_TAG_SINCH = "sinch";
    public static final int GRACEFUL_TIMEOUT = 5000;
    private static final String TAG = SinchClient.class.getSimpleName();
    private AudioController mAudioController;
    private DefaultCallClient mCallClient;
    private CallbackHandler mCallbackHandler;
    private ConfigRefresher mConfigRefresher;
    private ConnectivityListener mConnectivityListener;
    private Context mContext;
    private final String mDeviceId;
    private DefaultDispatcher mDispatcher;
    private boolean mDisposed = false;
    private HouseKeeper mHouseKeeper;
    private HttpService mHttpService;
    private SinchHttpServiceObserver mHttpServiceObserver;
    private boolean mManagedPushEnabled = false;
    private DefaultMessageClient mMessageClient;
    private List<Runnable> mPendingActions = new ArrayList();
    private final PublishSubscribeClient mPubsubClient;
    private boolean mPushDataRegistered = false;
    private DefaultServiceProvider mServiceProvider;
    private boolean mShouldStartListeningOnActiveConnection = false;
    private CopyOnWriteArraySet<SinchClientListener> mSinchClientListeners = new CopyOnWriteArraySet();
    private boolean mStarting = false;
    private TokenRefreshTask mTokenRefreshTask;
    private UserAgent mUserAgent;
    private Map<String, UserCapabilitiesCallback> mUserCapabilitiesCallbacks = new HashMap();
    private final String mUserId;
    private VideoControllerInternal mVideoController;
    private WorkerThread mWorkerThread;

    class C05472 implements Runnable {
        C05472() {
        }

        public void run() {
            DefaultSinchClient.this.finalCleanUp();
        }
    }

    class Environment {
        String host;
        boolean useSSL;

        Environment(String str, boolean z) {
            this.host = str;
            this.useSSL = z;
        }
    }

    public DefaultSinchClient(Context context, ServiceFactory serviceFactory, DeviceIdReader deviceIdReader, String str, String str2, String str3, String str4, String str5, ConnectivityListener connectivityListener, String str6, String str7, VideoControllerInternal videoControllerInternal, CallbackHandler callbackHandler) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("userId is empty");
        } else if (str4 == null || str4.length() == 0) {
            throw new IllegalArgumentException("applicationKey is empty");
        } else if (str3 == null || str3.length() == 0) {
            throw new IllegalArgumentException("environmentHost is empty");
        } else if (str6 == null) {
            throw new IllegalArgumentException("instantMessagingDatabase is null");
        } else if (str7 == null) {
            throw new IllegalArgumentException("persistenceServiceDatabase is null");
        } else {
            NativeLibLoader.loadAllRequiredLibraries(context.getApplicationContext());
            this.mCallbackHandler = callbackHandler;
            this.mContext = context.getApplicationContext();
            this.mUserId = str;
            this.mDeviceId = deviceIdReader.getDeviceId(this.mContext);
            this.mConnectivityListener = connectivityListener;
            this.mConnectivityListener.setCallback(this);
            this.mDispatcher = serviceFactory.createDefaultDispatcher();
            this.mWorkerThread = serviceFactory.createWorkerThread();
            this.mHttpService = serviceFactory.createDefaultHttpService();
            PersistenceService createDefaultPersistenceService = serviceFactory.createDefaultPersistenceService(this.mContext, DEFAULT_PREFERENCE_NAME);
            this.mPubsubClient = serviceFactory.createPublishSubscribeClient(this.mWorkerThread);
            this.mServiceProvider = new DefaultServiceProvider(this.mDispatcher, this.mHttpService, this.mPubsubClient, new DefaultCryptoService(), createDefaultPersistenceService);
            Environment environment = getEnvironment(str3);
            String str8 = Build.MANUFACTURER;
            String str9 = Build.DEVICE != "" ? Build.DEVICE : Build.MODEL;
            String str10 = VERSION.RELEASE;
            this.mUserAgent = serviceFactory.createUserAgent(this.mServiceProvider.getNativeServiceProvider(), "3.11.0", "android", str4, this.mUserId, str2, 2, this.mDeviceId, str5, environment.host, environment.useSSL, str6, str7, str8, str9, str10);
            this.mUserAgent.setEventListener(this);
            this.mHouseKeeper = new HouseKeeper(this.mDispatcher, this.mUserAgent);
            this.mConfigRefresher = new ConfigRefresher(this.mDispatcher, this.mUserAgent);
            setSupportPeerToPeer(true);
            this.mCallClient = new DefaultCallClient(this, this, this.mUserAgent.getCallClient(), this.mContext);
            this.mMessageClient = new DefaultMessageClient(this, this, this.mUserAgent.getMessaging());
            this.mContext.registerReceiver(this.mConnectivityListener, this.mConnectivityListener.createIntentFilter());
            this.mVideoController = videoControllerInternal;
            this.mCallClient.setVideoTrackListener((VideoTrackListenerInternal) this.mVideoController);
        }
    }

    private void finalCleanUp() {
        this.mCallbackHandler = null;
        this.mPubsubClient.stopPubSubClient();
        this.mWorkerThread.stopThread();
        this.mWorkerThread = null;
        this.mDispatcher.stop();
        this.mDispatcher = null;
        this.mUserAgent.dispose();
        if (this.mUserAgent instanceof DefaultUserAgent) {
            ((DefaultUserAgent) this.mUserAgent).invalidate();
        }
        this.mUserAgent = null;
        this.mContext = null;
        this.mUserCapabilitiesCallbacks = null;
        this.mConnectivityListener = null;
        this.mServiceProvider.dispose();
        this.mServiceProvider = null;
        this.mHttpService = null;
    }

    static Environment getEnvironment(String str) {
        if (!str.startsWith("https://") && !str.startsWith("http://")) {
            return new Environment(str, true);
        }
        try {
            URL url = new URL(str);
            return "http".equals(url.getProtocol()) ? new Environment(url.getHost(), false) : new Environment(url.getHost(), true);
        } catch (MalformedURLException e) {
            return new Environment(str, true);
        }
    }

    private boolean isCapabilityEnabled(InternalCapability internalCapability) {
        return this.mUserAgent.isCapabilityEnabled(internalCapability.toString());
    }

    private NotificationResult relayRemotePushNotificationPayload(String str, String str2) {
        throwIfDisposed();
        if (str == null) {
            return DefaultNotificationResult.getNonValidResult();
        }
        try {
            final PushPayloadQueryResult queryPayload = PushPayloadQuery.queryPayload(Base64.decode(str, 2), System.currentTimeMillis() / 1000);
            if (!queryPayload.isValid()) {
                mo2264e(TAG, "Error in payload query: " + queryPayload.getError());
                return DefaultNotificationResult.getNonValidResult();
            } else if ((queryPayload.getType() == 1 && !isSupportCalling()) || (queryPayload.getType() == 2 && !isSupportMessaging())) {
                return DefaultNotificationResult.getNonValidResult();
            } else {
                NotificationResult defaultNotificationResult = new DefaultNotificationResult(queryPayload);
                if (str2 != null) {
                    defaultNotificationResult.setDisplayName(str2);
                }
                if (queryPayload.getType() == 1) {
                    if (isStarted()) {
                        this.mCallClient.handleCallPushPayload(queryPayload);
                        return defaultNotificationResult;
                    }
                    this.mPendingActions.add(new Runnable() {
                        public void run() {
                            DefaultSinchClient.this.mCallClient.handleCallPushPayload(queryPayload);
                        }
                    });
                    startIfNeeded();
                    return defaultNotificationResult;
                } else if (queryPayload.getType() != 2) {
                    return defaultNotificationResult;
                } else {
                    startIfNeeded();
                    return defaultNotificationResult;
                }
            }
        } catch (IllegalArgumentException e) {
            return DefaultNotificationResult.getNonValidResult();
        }
    }

    private void startIfNeeded() {
        if (!isStarted() && this.mUserAgent.getState() != UserAgentState.STARTING && !this.mStarting) {
            start();
        }
    }

    private void throwIfDisposed() {
        if (isDisposed()) {
            throw new IllegalStateException("SinchClient stopped, further calls will throw Exceptions.");
        }
    }

    private void throwIfStarted() {
        throwIfDisposed();
        if (isStarted()) {
            throw new IllegalStateException("SinchClient already started, you may not perform this action.");
        }
    }

    private void throwUnlessStarted() {
        throwIfDisposed();
        if (!isStarted()) {
            throw new IllegalStateException("SinchClient not started");
        }
    }

    private void toggleCapability(InternalCapability internalCapability, boolean z) {
        if (z) {
            this.mUserAgent.enableCapability(internalCapability.toString());
        } else {
            this.mUserAgent.disableCapability(internalCapability.toString());
        }
    }

    public void addSinchClientListener(SinchClientListener sinchClientListener) {
        this.mSinchClientListeners.add(sinchClientListener);
    }

    protected boolean canEnableManagedPush() {
        if (checkPlayServices()) {
            return true;
        }
        throw new MissingGCMException("Can't enable managed push as this depends on GCM, which is not available on this device.");
    }

    public void checkManifest() {
        throwIfDisposed();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            throw new MissingPermissionException("android.permission.INTERNET");
        } else if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            throw new MissingPermissionException("android.permission.ACCESS_NETWORK_STATE");
        } else {
            if (isSupportCalling()) {
                if (this.mContext.checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") != 0) {
                    throw new MissingPermissionException("android.permission.RECORD_AUDIO");
                } else if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_SETTINGS") != 0) {
                    throw new MissingPermissionException("android.permission.MODIFY_AUDIO_SETTINGS");
                } else if (this.mCallClient.isRespectNativeCalls() && this.mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
                    throw new MissingPermissionException("android.permission.READ_PHONE_STATE");
                }
            }
            if (this.mManagedPushEnabled && this.mContext.checkCallingOrSelfPermission("com.google.android.c2dm.permission.RECEIVE") != 0) {
                throw new MissingPermissionException("com.google.android.c2dm.permission.RECEIVE");
            }
        }
    }

    protected boolean checkPlayServices() {
        try {
            return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext) == 0;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    public void mo2263d(String str, String str2) {
        notifyLog(3, str, str2);
    }

    public void mo2264e(String str, String str2) {
        notifyLog(6, str, str2);
    }

    public void fetchUserCapabilities(String str, UserCapabilitiesCallback userCapabilitiesCallback) {
        throwUnlessStarted();
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Must have non-empty userId");
        } else if (userCapabilitiesCallback == null) {
            throw new IllegalArgumentException("Must have non-null callback");
        } else {
            String uuid = UUID.randomUUID().toString();
            this.mUserCapabilitiesCallbacks.put(uuid, userCapabilitiesCallback);
            this.mUserAgent.getDestinationCapabilities(uuid, str);
        }
    }

    public AudioController getAudioController() {
        throwUnlessStarted();
        if (this.mAudioController == null) {
            this.mAudioController = new DefaultAudioController(this.mContext, this, this.mUserAgent.getAudioController());
        }
        return this.mAudioController;
    }

    public CallClient getCallClient() {
        throwIfDisposed();
        return this.mCallClient;
    }

    public String getLocalUserId() {
        return this.mUserId;
    }

    public MessageClient getMessageClient() {
        throwIfDisposed();
        return this.mMessageClient;
    }

    public Object getNativeVideoCapturer() {
        if (this.mUserAgent == null || !this.mUserAgent.isStarted()) {
            return null;
        }
        ObjectRef objectRef = new ObjectRef();
        this.mUserAgent.getNativeVideoCapturer(objectRef);
        return objectRef.get();
    }

    public VideoController getVideoController() {
        if (this.mVideoController == null) {
            this.mVideoController = new DefaultVideoController(this.mContext, this, new VideoFrameSink());
            this.mCallClient.setVideoTrackListener((VideoTrackListenerInternal) this.mVideoController);
        }
        return this.mVideoController;
    }

    public void mo2270i(String str, String str2) {
        notifyLog(4, str, str2);
    }

    public boolean isDisposed() {
        return this.mDisposed;
    }

    public boolean isStarted() {
        return this.mUserAgent != null && this.mUserAgent.isStarted();
    }

    public boolean isSupportCalling() {
        throwIfDisposed();
        return isCapabilityEnabled(InternalCapability.VOIP);
    }

    public boolean isSupportMessaging() {
        throwIfDisposed();
        return isCapabilityEnabled(InternalCapability.IM);
    }

    public void log(int i, String str, String str2) {
        notifyLog(LogLevel.nativeToAndroid(i), str, str2);
    }

    public void notifyLog(int i, String str, String str2) {
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            ((SinchClientListener) it.next()).onLogMessage(i, str, str2);
        }
    }

    public void onAudioFeaturesChanged(int i, int i2, boolean z) {
        AudioDeviceUtil.setAudioSource(i);
        AudioDeviceUtil.setAudioMode(i2);
        AudioDeviceUtil.setOverrideAudioMode(z);
    }

    public void onConfigChanged(String str) {
        if (this.mManagedPushEnabled && !str.isEmpty()) {
            registerManagedPushProfile(str, false);
        }
    }

    public void onDestinationCapabilities(String str, String str2, List<String> list) {
        UserCapabilitiesCallback userCapabilitiesCallback = (UserCapabilitiesCallback) this.mUserCapabilitiesCallbacks.get(str);
        if (userCapabilitiesCallback != null) {
            List arrayList = new ArrayList();
            for (String fromString : list) {
                Capability fromString2 = CapabilityUtils.fromString(fromString);
                if (fromString2 != null) {
                    arrayList.add(fromString2);
                }
            }
            userCapabilitiesCallback.onFetchSuccess(str2, arrayList);
            this.mUserCapabilitiesCallbacks.remove(str);
        }
    }

    public void onDestinationCapabilitiesFailed(String str, String str2, SinchError sinchError) {
        UserCapabilitiesCallback userCapabilitiesCallback = (UserCapabilitiesCallback) this.mUserCapabilitiesCallbacks.get(str);
        if (userCapabilitiesCallback != null) {
            userCapabilitiesCallback.onFetchFailed(str2, sinchError);
            this.mUserCapabilitiesCallbacks.remove(str);
        }
    }

    public void onGcmRegistrationSucceeded(PersistedToken persistedToken, String str, String str2) {
        if (!isDisposed()) {
            persistedToken.savePersistedToken(str2);
            this.mUserAgent.setManagedPushProfile(str, str2);
        }
    }

    public void onHttpRequestSent(final String str, final String str2, final byte[] bArr) {
        if (this.mHttpServiceObserver != null) {
            this.mCallbackHandler.post(new Runnable() {
                public void run() {
                    DefaultSinchClient.this.mHttpServiceObserver.onHttpRequestSent(str, str2, bArr);
                }
            });
        }
    }

    public void onNewPushProfileRequired(String str) {
        if (this.mManagedPushEnabled && !str.isEmpty()) {
            registerManagedPushProfile(str, true);
        }
    }

    public void onNotification(String str, String str2) {
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            SinchClientListener sinchClientListener = (SinchClientListener) it.next();
            if (sinchClientListener instanceof InternalSinchClientListener) {
                ((InternalSinchClientListener) sinchClientListener).onNotification(this, str, str2);
            }
        }
    }

    public void onRegisterInstance(UserAgent userAgent, ClientRegistration clientRegistration) {
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            ((SinchClientListener) it.next()).onRegistrationCredentialsRequired(this, clientRegistration);
        }
    }

    public void onStartFailed(UserAgent userAgent, SinchError sinchError) {
        mo2309w(TAG, "onStartFailed()");
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            ((SinchClientListener) it.next()).onClientFailed(this, sinchError);
        }
    }

    public void onStarted(UserAgent userAgent) {
        mo2263d(TAG, "onStarted()");
        for (Runnable run : this.mPendingActions) {
            run.run();
        }
        this.mPendingActions.clear();
        if (this.mShouldStartListeningOnActiveConnection) {
            startListeningOnActiveConnection();
            this.mShouldStartListeningOnActiveConnection = false;
        }
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            ((SinchClientListener) it.next()).onClientStarted(this);
        }
    }

    public void onTokenRefreshNeeded() {
        this.mUserAgent.triggerNewPushProfileRequest();
    }

    protected void registerManagedPushProfile(String str, boolean z) {
        PersistedToken persistedToken = new PersistedToken(this.mContext, str);
        String persistedToken2 = persistedToken.getPersistedToken();
        if (persistedToken2 == null || persistedToken2.length() == 0 || z) {
            new GetDeviceTokenTask(this.mContext, persistedToken, str, this).execute();
        } else {
            this.mUserAgent.setManagedPushProfile(str, persistedToken2);
        }
    }

    public void registerPushNotificationData(byte[] bArr) {
        throwIfDisposed();
        if (!isCapabilityEnabled(InternalCapability.PUSH)) {
            throw new IllegalStateException("Push capability is not enabled, enable it with setSupportPushNotifications(true)");
        } else if (this.mManagedPushEnabled) {
            throw new IllegalStateException("SupportManagedPush is enabled, you may not registerPushNotificationData.");
        } else if (bArr.length > 1024) {
            throw new IllegalArgumentException("pushNotificationData must be <= 1024 bytes");
        } else {
            this.mPushDataRegistered = true;
            this.mUserAgent.setPushData(bArr);
        }
    }

    public NotificationResult relayRemotePushNotificationPayload(Intent intent) {
        if (SinchHelpers.isSinchPushIntent(intent)) {
            if (intent.getStringExtra(GCM_PAYLOAD_TAG_SINCH) != null) {
                return relayRemotePushNotificationPayload(intent.getStringExtra(GCM_PAYLOAD_TAG_SINCH), intent.getStringExtra("name"));
            }
            if (intent.getStringExtra(GCM_PAYLOAD_TAG_MXP) != null) {
                return relayRemotePushNotificationPayload(intent.getStringExtra(GCM_PAYLOAD_TAG_MXP));
            }
        }
        return DefaultNotificationResult.getNonValidResult();
    }

    public NotificationResult relayRemotePushNotificationPayload(String str) {
        return relayRemotePushNotificationPayload(str, null);
    }

    public void removeSinchClientListener(SinchClientListener sinchClientListener) {
        this.mSinchClientListeners.remove(sinchClientListener);
    }

    public void resendFailedRequests() {
        if (this.mUserAgent != null && this.mUserAgent.isStarted()) {
            mo2263d(TAG, "Resending failed requests");
            this.mUserAgent.resendFailedRequests();
            this.mUserAgent.getMessaging().tryScheduleRetryFailedMessages();
        }
    }

    public void setHttpServiceObserver(SinchHttpServiceObserver sinchHttpServiceObserver) {
        if (sinchHttpServiceObserver != null) {
            this.mHttpService.setObserver(this);
            this.mHttpServiceObserver = sinchHttpServiceObserver;
            return;
        }
        this.mHttpService.setObserver(null);
    }

    public void setMinimumLogLevel(int i) {
        throwIfDisposed();
        this.mUserAgent.setMinimumLogLevel(i);
    }

    public void setPushNotificationDisplayName(String str) {
        throwIfDisposed();
        if (!this.mManagedPushEnabled) {
            throw new IllegalStateException("SupportManagedPush must be set to use push notification display name");
        } else if (str == null) {
            throw new IllegalArgumentException("displayName must not be null");
        } else {
            try {
                if (str.getBytes(Key.STRING_CHARSET_NAME).length > 255) {
                    throw new IllegalArgumentException("displayName must not exceed 255 bytes");
                }
                this.mUserAgent.setPushNotificationDisplayName(str);
            } catch (Throwable e) {
                throw new IllegalArgumentException("displayName must be UTF-8 encodable", e);
            }
        }
    }

    public void setSupportActiveConnectionInBackground(boolean z) {
        throwIfStarted();
        toggleCapability(InternalCapability.ONLINE, z);
    }

    public void setSupportCalling(boolean z) {
        throwIfStarted();
        toggleCapability(InternalCapability.VOIP, z);
    }

    public void setSupportManagedPush(boolean z) {
        throwIfStarted();
        this.mManagedPushEnabled = z;
        if (!z) {
            return;
        }
        if (this.mPushDataRegistered) {
            throw new IllegalStateException("SupportManagedPush cannot be enabled when registerPushNotificationData is used.");
        } else if (canEnableManagedPush()) {
            toggleCapability(InternalCapability.PUSH, z);
            this.mUserAgent.setUseManagedPush(true);
            this.mTokenRefreshTask = new TokenRefreshTask(this.mContext, this);
            this.mTokenRefreshTask.start();
        }
    }

    public void setSupportMessaging(boolean z) {
        throwIfStarted();
        toggleCapability(InternalCapability.IM, z);
    }

    public void setSupportPeerToPeer(boolean z) {
        throwIfStarted();
        toggleCapability(InternalCapability.P2P, z);
        toggleCapability(InternalCapability.SRTP, z);
    }

    public void setSupportPushNotifications(boolean z) {
        throwIfStarted();
        if (!this.mManagedPushEnabled) {
            toggleCapability(InternalCapability.PUSH, z);
        }
    }

    public void shutdown(boolean z) {
        throwIfDisposed();
        mo2263d(TAG, "terminate()");
        this.mDisposed = true;
        this.mContext.unregisterReceiver(this.mConnectivityListener);
        if (this.mVideoController != null) {
            this.mVideoController.dispose();
            this.mVideoController = null;
        }
        if (this.mTokenRefreshTask != null) {
            this.mTokenRefreshTask.stop();
            this.mTokenRefreshTask = null;
        }
        this.mHouseKeeper.stop();
        this.mConfigRefresher.stop();
        if (this.mUserAgent != null) {
            this.mUserAgent.stop();
            this.mUserAgent.setEventListener(null);
        }
        this.mHouseKeeper.dispose();
        this.mHouseKeeper = null;
        this.mConfigRefresher.dispose();
        this.mConfigRefresher = null;
        this.mAudioController = null;
        this.mMessageClient = null;
        this.mCallClient.terminate();
        this.mCallClient = null;
        this.mPubsubClient.stopSubscribersAndHistory();
        Iterator it = this.mSinchClientListeners.iterator();
        while (it.hasNext()) {
            ((SinchClientListener) it.next()).onClientStopped(this);
        }
        if (z) {
            this.mCallbackHandler.postDelayed(new C05472(), 5000);
        } else {
            finalCleanUp();
        }
    }

    public void start() {
        throwIfStarted();
        this.mStarting = true;
        if (this.mUserAgent.getState() == UserAgentState.STARTING) {
            throw new IllegalStateException("SinchClient already starting");
        } else if (isSupportCalling() || isSupportMessaging()) {
            if (isSupportCalling()) {
                WebRTCDriver.init(this.mContext);
                if (this.mVideoController == null) {
                    this.mVideoController = new DefaultVideoController(this.mContext, this, new VideoFrameSink());
                    this.mCallClient.setVideoTrackListener((VideoTrackListenerInternal) this.mVideoController);
                }
                long videoFrameSink = this.mVideoController.getVideoFrameSink();
                if (videoFrameSink != 0) {
                    this.mUserAgent.setApplicationContext(videoFrameSink);
                } else {
                    mo2309w(TAG, "Native renderer is null");
                }
            }
            this.mUserAgent.start();
            this.mHouseKeeper.start(2000);
            this.mConfigRefresher.start(DEFAULT_REFRESH_CONFIG_DELAY);
        } else {
            onStartFailed(null, new DefaultSinchError(InternalErrorCodes.CapabilityCapabilityMissing, "Can't enable SinchClient without either IM or calling enabled.", ErrorType.CAPABILITY.ordinal()));
        }
    }

    public void startListeningOnActiveConnection() {
        throwIfDisposed();
        if (this.mUserAgent == null || !this.mUserAgent.isStarted()) {
            this.mShouldStartListeningOnActiveConnection = true;
            return;
        }
        mo2263d(TAG, "mUserAgent.startBroadcastListener()");
        this.mUserAgent.startBroadcastListener();
    }

    public void stop() {
        terminate();
    }

    public void stopListeningOnActiveConnection() {
        throwIfDisposed();
        if (this.mUserAgent == null || !this.mUserAgent.isStarted()) {
            this.mShouldStartListeningOnActiveConnection = false;
            return;
        }
        mo2263d(TAG, "mUserAgent.stopBroadcastListener()");
        this.mUserAgent.stopBroadcastListener();
    }

    public boolean supportsCapability(String str) {
        return this.mUserAgent.isCapabilityEnabled(str);
    }

    public void terminate() {
        shutdown(false);
    }

    public void terminateGracefully() {
        shutdown(true);
    }

    public void unregisterManagedPush() {
        throwIfDisposed();
        if (this.mManagedPushEnabled) {
            this.mUserAgent.deletePushData();
            return;
        }
        throw new IllegalStateException("SupportManagedPush is not enabled.");
    }

    public void unregisterPushNotificationData() {
        throwIfDisposed();
        if (this.mManagedPushEnabled) {
            throw new IllegalStateException("SupportManagedPush is enabled, you may not unregisterPushNotificationData.");
        }
        this.mUserAgent.deletePushData();
    }

    public void mo2308v(String str, String str2) {
        notifyLog(2, str, str2);
    }

    public void mo2309w(String str, String str2) {
        notifyLog(5, str, str2);
    }
}
