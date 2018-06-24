package com.sinch.android.rtc.internal.service.pubnub.http;

public class DefaultHttpRequesterFactory implements HttpRequesterFactory {
    public HttpRequester createHttpRequester() {
        return new DefaultHttpRequester();
    }
}
