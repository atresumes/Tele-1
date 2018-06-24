package com.sinch.android.rtc.internal.service.pubnub;

import android.net.Uri;
import android.util.Log;
import com.payUMoney.sdk.SdkConstants;
import com.sinch.android.rtc.internal.client.Sleepable;
import com.sinch.android.rtc.internal.natives.PubSubHistoryConsumer;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequester;
import java.io.IOException;
import java.util.concurrent.Executor;

public class PubNubHistoryListener {
    private static final String CHANNEL = "channel";
    private static final String COUNT = "count";
    private static final String END = "end";
    private static final String HISTORY = "history";
    private static final int HTTP_TIMEOUT = 10000;
    private static final String INCLUDE_TIMETOKEN = "include_token";
    private static final String REVERSE = "reverse";
    private static final String START = "start";
    private static final String SUBKEY = "sub-key";
    private static final String TAG = PubNubHistoryListener.class.getSimpleName();
    private static final String VERSION = "v2";
    private volatile boolean active = true;
    private final Uri baseUri;
    private final PubSubHistoryConsumer callback;
    private final Executor callbackExecutor;
    private String end;
    private int numRetries = 0;
    private final HttpRequester requester;
    private RetryHoldback retryHoldback;
    private String start;

    public class ListenerResponse {
        public int lastCount;
        public boolean requestFailed;

        public ListenerResponse(boolean z, int i) {
            this.requestFailed = z;
            this.lastCount = i;
        }
    }

    public PubNubHistoryListener(HttpRequester httpRequester, String str, PubSubHistoryConsumer pubSubHistoryConsumer, Executor executor, int i, String str2, String str3, boolean z, Sleepable sleepable) {
        this.retryHoldback = new RetryHoldback(sleepable);
        this.requester = httpRequester;
        this.callback = pubSubHistoryConsumer;
        this.callbackExecutor = executor;
        this.start = str2;
        this.end = str3;
        this.baseUri = Uri.parse(str).buildUpon().appendPath(VERSION).appendPath(HISTORY).appendPath(SUBKEY).appendPath(pubSubHistoryConsumer.getSubscribeKey()).appendPath(CHANNEL).appendPath(pubSubHistoryConsumer.getChannel()).appendQueryParameter(REVERSE, z ? "true" : SdkConstants.FALSE_STRING).appendQueryParameter(INCLUDE_TIMETOKEN, "true").appendQueryParameter("count", String.valueOf(i)).build();
    }

    private void handleData(final PubNubHistoryResponse pubNubHistoryResponse, final String str, final String str2) {
        this.callbackExecutor.execute(new Runnable() {
            public void run() {
                if (PubNubHistoryListener.this.active) {
                    PubNubHistoryListener.this.callback.endHistoryGet(pubNubHistoryResponse.getMessagesAsArray(), pubNubHistoryResponse.getTimeTokensAsArray(), str, str2);
                }
            }
        });
    }

    public void abort() {
        this.active = false;
    }

    public String getChannel() {
        return this.callback.getChannel();
    }

    public ListenerResponse readMessages() {
        int messageCount;
        IOException e;
        boolean z = false;
        try {
            if (this.retryHoldback.getCurrentHoldBack(this.numRetries) > 0) {
                this.retryHoldback.holdback(this.numRetries);
            }
            PubNubHistoryResponse parse = PubNubHistoryResponse.parse(this.requester.get(this.baseUri.buildUpon().appendQueryParameter(START, this.start).appendQueryParameter(END, this.end).build().toString(), HTTP_TIMEOUT));
            if (!parse.isValid()) {
                this.callback.failedHistoryGet();
            }
            messageCount = parse.getMessageCount();
            if (messageCount > 0) {
                try {
                    String startTimeTokenAsString = parse.getStartTimeTokenAsString();
                    String endTimeTokenAsString = parse.getEndTimeTokenAsString();
                    handleData(parse, startTimeTokenAsString, endTimeTokenAsString);
                    this.start = endTimeTokenAsString;
                    this.end = "";
                } catch (InterruptedException e2) {
                    Log.v(TAG, "Got interrupt request");
                    this.numRetries++;
                    z = true;
                    return new ListenerResponse(z, messageCount);
                } catch (IOException e3) {
                    e = e3;
                    Log.v(TAG, "IO exception while subscribing for data. " + e.getMessage());
                    this.numRetries++;
                    z = true;
                    return new ListenerResponse(z, messageCount);
                }
            }
            this.numRetries = 0;
        } catch (InterruptedException e4) {
            messageCount = 0;
            Log.v(TAG, "Got interrupt request");
            this.numRetries++;
            z = true;
            return new ListenerResponse(z, messageCount);
        } catch (IOException e5) {
            IOException iOException = e5;
            messageCount = 0;
            e = iOException;
            Log.v(TAG, "IO exception while subscribing for data. " + e.getMessage());
            this.numRetries++;
            z = true;
            return new ListenerResponse(z, messageCount);
        }
        return new ListenerResponse(z, messageCount);
    }
}
