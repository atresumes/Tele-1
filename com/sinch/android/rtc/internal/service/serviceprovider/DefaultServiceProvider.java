package com.sinch.android.rtc.internal.service.serviceprovider;

import com.sinch.android.rtc.internal.natives.jni.NativeServiceProviderImpl;
import com.sinch.android.rtc.internal.natives.jni.ServiceProviderFactory;
import com.sinch.android.rtc.internal.service.crypto.CryptoService;
import com.sinch.android.rtc.internal.service.dispatcher.Dispatcher;
import com.sinch.android.rtc.internal.service.http.HttpService;
import com.sinch.android.rtc.internal.service.persistence.PersistenceService;
import com.sinch.android.rtc.internal.service.pubnub.PubSubClient;

public class DefaultServiceProvider implements ServiceProvider {
    private CryptoService cryptoService;
    private Dispatcher dispatcher;
    private HttpService httpService;
    private NativeServiceProviderImpl mNativeServiceProvider;
    private PersistenceService persistenceService;
    private PubSubClient pubSubClient;

    public DefaultServiceProvider(Dispatcher dispatcher, HttpService httpService, PubSubClient pubSubClient, CryptoService cryptoService, PersistenceService persistenceService) {
        this.dispatcher = dispatcher;
        this.httpService = httpService;
        this.pubSubClient = pubSubClient;
        this.cryptoService = cryptoService;
        this.persistenceService = persistenceService;
        this.mNativeServiceProvider = ServiceProviderFactory.createServiceProvider(dispatcher, httpService, pubSubClient, cryptoService, persistenceService);
    }

    public void dispose() {
        if (this.mNativeServiceProvider != null) {
            this.mNativeServiceProvider.dispose();
            this.mNativeServiceProvider = null;
        }
        this.dispatcher = null;
        this.httpService = null;
        this.pubSubClient = null;
        this.cryptoService = null;
        this.persistenceService = null;
    }

    public CryptoService getCryptoService() {
        return this.cryptoService;
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    public HttpService getHttpService() {
        return this.httpService;
    }

    public NativeServiceProviderImpl getNativeServiceProvider() {
        return this.mNativeServiceProvider;
    }

    public PersistenceService getPersistenceService() {
        return this.persistenceService;
    }

    public PubSubClient getPubSubClient() {
        return this.pubSubClient;
    }
}
