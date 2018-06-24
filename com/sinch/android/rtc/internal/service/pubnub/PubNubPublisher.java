package com.sinch.android.rtc.internal.service.pubnub;

import android.util.Log;
import com.sinch.android.rtc.internal.InternalErrorCodes;
import com.sinch.android.rtc.internal.client.Sleepable;
import com.sinch.android.rtc.internal.natives.PubPublisher;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequesterFactory;
import com.sinch.android.rtc.internal.service.pubnub.http.UriEncoder;
import com.sinch.gson.JsonArray;
import com.sinch.gson.JsonElement;
import com.sinch.gson.JsonParser;
import com.sinch.gson.JsonSyntaxException;
import java.util.Date;
import java.util.concurrent.Executor;

public class PubNubPublisher {
    private static final int DEFAULT_PUBLISH_RETRY_SCOPE = 15000;
    private static final int DEFAULT_PUBLISH_RETRY_WAIT = 3000;
    public static final String MALFORMED_JSON_FROM_PUB_NUB = "Malformed JSON from PubNub";
    private static final int PUBLISH_TIME_OUT = 5000;
    private static final String TAG = PubNubPublisher.class.getSimpleName();
    private volatile boolean active;
    private int attempt;
    private final HttpRequesterFactory httpRequesterFactory;
    private Exception lastException;
    private final String pubNubBaseUrl;
    private final Executor publishExecutor;
    private boolean publishFailed;
    private final int retryScope;
    private final int retryWait;
    private final Sleepable sleepable;

    public PubNubPublisher(Executor executor, HttpRequesterFactory httpRequesterFactory, String str, Sleepable sleepable) {
        this(executor, httpRequesterFactory, str, sleepable, DEFAULT_PUBLISH_RETRY_SCOPE, 3000);
    }

    public PubNubPublisher(Executor executor, HttpRequesterFactory httpRequesterFactory, String str, Sleepable sleepable, int i, int i2) {
        this.publishFailed = false;
        this.attempt = 0;
        this.lastException = null;
        this.publishExecutor = executor;
        this.httpRequesterFactory = httpRequesterFactory;
        this.pubNubBaseUrl = str;
        this.sleepable = sleepable;
        this.retryScope = i;
        this.retryWait = i2;
        this.active = true;
    }

    private void failCallback(PubPublisher pubPublisher, int i, String str) {
        failCallback(pubPublisher, i, str, "0");
    }

    private void failCallback(PubPublisher pubPublisher, int i, String str, String str2) {
        if (pubPublisher != null && this.active) {
            pubPublisher.onPublishFailed(i, str, str2);
        }
    }

    private void successCallback(PubPublisher pubPublisher, String str) {
        if (pubPublisher != null && this.active) {
            pubPublisher.onPublishSuccess(str);
        }
    }

    public void abort() {
        this.active = false;
    }

    public void publish(String str, String str2, String str3, String str4, PubPublisher pubPublisher) {
        final String str5 = str2;
        final String str6 = str4;
        final String str7 = str3;
        final String str8 = str;
        final PubPublisher pubPublisher2 = pubPublisher;
        this.publishExecutor.execute(new Runnable() {
            public void run() {
                long time = new Date().getTime();
                do {
                    try {
                        if (PubNubPublisher.this.publishFailed) {
                            PubNubPublisher.this.sleepable.sleep((long) PubNubPublisher.this.retryWait);
                        }
                        PubNubPublisher.this.publishFailed = false;
                        JsonElement parse = new JsonParser().parse(PubNubPublisher.this.httpRequesterFactory.createHttpRequester().get(PubNubPublisher.this.pubNubBaseUrl + "/" + "publish" + "/" + UriEncoder.encode(str6) + "/" + UriEncoder.encode(str7) + "/0/" + UriEncoder.encode(str8) + "/0/" + ("\"" + UriEncoder.encode(str5) + "\""), 5000));
                        if (parse.isJsonArray()) {
                            JsonArray asJsonArray = parse.getAsJsonArray();
                            if (asJsonArray.size() < 3) {
                                PubNubPublisher.this.failCallback(pubPublisher2, InternalErrorCodes.ApiApiCallFailed, PubNubPublisher.MALFORMED_JSON_FROM_PUB_NUB);
                                return;
                            }
                            int asInt = asJsonArray.get(0).getAsInt();
                            String asString = asJsonArray.get(1).getAsString();
                            long asLong = asJsonArray.get(2).getAsLong();
                            if (asInt == 1) {
                                PubNubPublisher.this.successCallback(pubPublisher2, String.valueOf(asLong));
                                PubNubPublisher.this.attempt = PubNubPublisher.this.attempt + 1;
                                if (!PubNubPublisher.this.publishFailed) {
                                    break;
                                }
                            } else {
                                PubNubPublisher.this.failCallback(pubPublisher2, InternalErrorCodes.ApiApiCallFailed, asString, String.valueOf(asLong));
                                return;
                            }
                        }
                        PubNubPublisher.this.failCallback(pubPublisher2, InternalErrorCodes.ApiApiCallFailed, PubNubPublisher.MALFORMED_JSON_FROM_PUB_NUB);
                        return;
                    } catch (JsonSyntaxException e) {
                        PubNubPublisher.this.failCallback(pubPublisher2, InternalErrorCodes.ApiApiCallFailed, PubNubPublisher.MALFORMED_JSON_FROM_PUB_NUB);
                        return;
                    } catch (Exception e2) {
                        PubNubPublisher.this.lastException = e2;
                        Log.w(PubNubPublisher.TAG, "Attempt " + PubNubPublisher.this.attempt + " -> IO exception while publishing. " + e2.getMessage());
                        PubNubPublisher.this.publishFailed = true;
                    } catch (Exception e22) {
                        PubNubPublisher.this.lastException = e22;
                        PubNubPublisher.this.publishFailed = true;
                    }
                } while (new Date().getTime() - time < ((long) PubNubPublisher.this.retryScope));
                if (PubNubPublisher.this.publishFailed) {
                    Log.e(PubNubPublisher.TAG, "Permanent failure for publishing (after " + PubNubPublisher.this.attempt + " attempts) message: " + str5);
                    if (PubNubPublisher.this.lastException == null) {
                        PubNubPublisher.this.failCallback(pubPublisher2, 1002, "Unknown error, " + PubNubPublisher.this.attempt + " attempts failed without any exception.");
                    } else {
                        PubNubPublisher.this.failCallback(pubPublisher2, 1002, "Network error when publishing to PubNub (" + PubNubPublisher.this.attempt + " attempts). " + PubNubPublisher.this.lastException);
                    }
                }
            }
        });
    }
}
