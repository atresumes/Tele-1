package com.sinch.android.rtc.internal.service.pubnub.http;

import com.sinch.gson.JsonObject;

public interface HttpRequester {
    String get(String str, int i);

    String postJson(String str, JsonObject jsonObject, int i);
}
