package com.sinch.android.rtc.internal.service.http;

import com.sinch.android.rtc.internal.natives.HttpRequest;
import com.sinch.android.rtc.internal.natives.HttpRequestCallback;
import java.util.Map;

public interface HttpService {
    void cancelAllRequests();

    HttpRequest createHttpRequest(String str, int i, String str2, Map<String, String> map);

    void sendRequest(HttpRequest httpRequest, HttpRequestCallback httpRequestCallback);

    void setObserver(SinchHttpServiceObserver sinchHttpServiceObserver);
}
