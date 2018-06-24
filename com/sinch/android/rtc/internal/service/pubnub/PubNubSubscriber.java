package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.client.CurrentThreadSleeper;
import com.sinch.android.rtc.internal.natives.PubSubscriber;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequester;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class PubNubSubscriber {
    private final String baseUrl;
    private final Executor callbackExecutor;
    private Set<PubSubscriber> callbacks = Collections.synchronizedSet(new HashSet());
    private final List<String> channels;
    private PubNubListener listener;
    private final String subscribeKey;
    private SubscribeThread thread;
    private final int timeOffsetOnSubscribeInMs;
    private final String timeToken;

    public class SubscribeThread extends Thread {
        private volatile boolean run = true;

        public boolean isRunning() {
            return this.run;
        }

        public void run() {
            while (this.run) {
                PubNubSubscriber.this.listener.readMessage(this);
            }
        }

        public void stopSelf() {
            this.run = false;
        }

        public void stopThread() {
            stopSelf();
        }
    }

    public PubNubSubscriber(String str, String str2, List<String> list, String str3, int i, PubSubscriber pubSubscriber, Executor executor) {
        this.baseUrl = str;
        this.subscribeKey = str2;
        this.channels = list;
        this.timeToken = str3;
        this.timeOffsetOnSubscribeInMs = i;
        this.callbackExecutor = executor;
        this.callbacks.add(pubSubscriber);
    }

    public void addCallback(PubSubscriber pubSubscriber) {
        this.callbacks.add(pubSubscriber);
    }

    public boolean hasNoCallbackers() {
        return this.callbacks.isEmpty();
    }

    public boolean isSubscribing() {
        return this.thread != null && this.thread.isRunning();
    }

    public void removeCallback(PubSubscriber pubSubscriber) {
        this.callbacks.remove(pubSubscriber);
    }

    public void subscribe(HttpRequester httpRequester) {
        if (isSubscribing()) {
            throw new IllegalStateException("Already subscribing");
        }
        this.listener = new PubNubListener(httpRequester, this.baseUrl, this.subscribeKey, this.channels, this.timeToken, this.timeOffsetOnSubscribeInMs, Collections.unmodifiableSet(this.callbacks), this.callbackExecutor, CurrentThreadSleeper.getInstance());
        this.thread = new SubscribeThread();
        this.thread.setName("PubNub subscriber");
        this.thread.start();
    }

    public void unsubscribe() {
        this.listener.abort();
        if (this.thread != null) {
            this.thread.stopThread();
            this.thread.interrupt();
        }
    }
}
