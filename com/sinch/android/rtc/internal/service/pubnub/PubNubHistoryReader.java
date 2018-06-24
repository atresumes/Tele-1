package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.natives.PubSubHistoryConsumer;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequester;
import java.io.IOException;
import java.util.concurrent.Executor;

public class PubNubHistoryReader {
    private static final int HTTP_TIMEOUT = 10000;
    private static final long MILLISECONDS_TO_TEN_THOUSAND_MILLISECONDS = 10000;
    private final String baseRequest;
    private final PubSubHistoryConsumer callback;
    private final Executor callbackExecutor;
    private final HttpRequester requester;
    private final int timeOffsetOnSubscribeInMs;

    public class PubNubHistoryReaderRunner extends Thread {
        public void run() {
            try {
                PubNubResponse parse = PubNubResponse.parse(PubNubHistoryReader.this.requester.get(PubNubHistoryReader.this.baseRequest + "0", PubNubHistoryReader.HTTP_TIMEOUT));
                if (parse.isValid()) {
                    parse = PubNubResponse.parse(PubNubHistoryReader.this.requester.get(PubNubHistoryReader.this.baseRequest + (parse.getTimeToken().longValue() + (((long) PubNubHistoryReader.this.timeOffsetOnSubscribeInMs) * PubNubHistoryReader.MILLISECONDS_TO_TEN_THOUSAND_MILLISECONDS)), PubNubHistoryReader.HTTP_TIMEOUT));
                    if (parse.isValid()) {
                        PubNubHistoryReader.this.callbackExecutor.execute(new Runnable() {
                            public void run() {
                                PubNubHistoryReader.this.callback.endHistoryGetWithoutTimestamps(parse.getMessagesAsArray());
                            }
                        });
                        return;
                    } else {
                        PubNubHistoryReader.this.callback.failedHistoryGet();
                        return;
                    }
                }
                PubNubHistoryReader.this.callback.failedHistoryGet();
            } catch (InterruptedException e) {
                PubNubHistoryReader.this.callback.failedHistoryGet();
            } catch (IOException e2) {
                PubNubHistoryReader.this.callback.failedHistoryGet();
            }
        }
    }

    public PubNubHistoryReader(HttpRequester httpRequester, String str, int i, PubSubHistoryConsumer pubSubHistoryConsumer, Executor executor) {
        this.requester = httpRequester;
        this.baseRequest = str + "/" + "subscribe" + "/" + pubSubHistoryConsumer.getSubscribeKey() + "/" + pubSubHistoryConsumer.getChannel() + "/" + "0" + "/";
        this.timeOffsetOnSubscribeInMs = i;
        this.callback = pubSubHistoryConsumer;
        this.callbackExecutor = executor;
    }

    public void startHistoryReader() {
        PubNubHistoryReaderRunner pubNubHistoryReaderRunner = new PubNubHistoryReaderRunner();
        pubNubHistoryReaderRunner.setName("PubNub History Consumer");
        pubNubHistoryReaderRunner.start();
    }
}
