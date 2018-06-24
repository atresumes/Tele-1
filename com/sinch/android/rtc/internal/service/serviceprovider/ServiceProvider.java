package com.sinch.android.rtc.internal.service.serviceprovider;

import com.sinch.android.rtc.internal.service.crypto.CryptoService;
import com.sinch.android.rtc.internal.service.dispatcher.Dispatcher;
import com.sinch.android.rtc.internal.service.http.HttpService;
import com.sinch.android.rtc.internal.service.persistence.PersistenceService;
import com.sinch.android.rtc.internal.service.pubnub.PubSubClient;

public interface ServiceProvider {
    CryptoService getCryptoService();

    Dispatcher getDispatcher();

    HttpService getHttpService();

    PersistenceService getPersistenceService();

    PubSubClient getPubSubClient();
}
