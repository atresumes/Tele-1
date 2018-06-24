package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.service.crypto.CryptoService;
import com.sinch.android.rtc.internal.service.dispatcher.Dispatcher;
import com.sinch.android.rtc.internal.service.http.HttpService;
import com.sinch.android.rtc.internal.service.persistence.PersistenceService;
import com.sinch.android.rtc.internal.service.pubnub.PubSubClient;

public class ServiceProviderFactory {
    public static native NativeServiceProviderImpl createServiceProvider(Dispatcher dispatcher, HttpService httpService, PubSubClient pubSubClient, CryptoService cryptoService, PersistenceService persistenceService);
}
