package com.sinch.android.rtc.internal.client;

import android.content.Context;
import com.sinch.android.rtc.internal.CallbackHandler;
import com.sinch.android.rtc.internal.natives.jni.NativeServiceProviderImpl;
import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import com.sinch.android.rtc.internal.natives.jni.UserAgentFactory;
import com.sinch.android.rtc.internal.service.dispatcher.DefaultDispatcher;
import com.sinch.android.rtc.internal.service.http.DefaultHttpService;
import com.sinch.android.rtc.internal.service.persistence.DefaultPersistenceService;
import com.sinch.android.rtc.internal.service.pubnub.PubNubSubscriberFactory;
import com.sinch.android.rtc.internal.service.pubnub.PublishSubscribeClient;
import com.sinch.android.rtc.internal.service.pubnub.http.DefaultHttpRequesterFactory;
import java.util.concurrent.Executors;

public class ServiceFactory {
    static final /* synthetic */ boolean $assertionsDisabled = (!ServiceFactory.class.desiredAssertionStatus());
    CallbackHandler mCallbackHandler;

    ServiceFactory(CallbackHandler callbackHandler) {
        if (callbackHandler == null) {
            throw new IllegalArgumentException("CallbackHandler must not be null");
        }
        this.mCallbackHandler = callbackHandler;
    }

    private CallbackHandler getCurrentCallbackHandler() {
        if ($assertionsDisabled || this.mCallbackHandler != null) {
            return this.mCallbackHandler;
        }
        throw new AssertionError();
    }

    public DefaultDispatcher createDefaultDispatcher() {
        return new DefaultDispatcher(getCurrentCallbackHandler());
    }

    public DefaultHttpService createDefaultHttpService() {
        return new DefaultHttpService();
    }

    public DefaultPersistenceService createDefaultPersistenceService(Context context, String str) {
        return new DefaultPersistenceService(context.getSharedPreferences(str, 0));
    }

    public PublishSubscribeClient createPublishSubscribeClient(WorkerThread workerThread) {
        return new PublishSubscribeClient(new DefaultHttpRequesterFactory(), new PubNubSubscriberFactory(), workerThread, Executors.newSingleThreadExecutor());
    }

    public UserAgent createUserAgent(NativeServiceProviderImpl nativeServiceProviderImpl, String str, String str2, String str3, String str4, String str5, int i, String str6, String str7, String str8, boolean z, String str9, String str10, String str11, String str12, String str13) {
        return UserAgentFactory.createUserAgent(nativeServiceProviderImpl, str, str2, str3, str4, str5, i, str6, str7, str8, z, str9, str10, str11, str12, str13);
    }

    public WorkerThread createWorkerThread() {
        return WorkerThread.createWorkerThread();
    }
}
