package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.InternalErrorCodes;
import com.sinch.android.rtc.internal.client.CurrentThreadSleeper;
import com.sinch.android.rtc.internal.natives.PubPublisher;
import com.sinch.android.rtc.internal.natives.PubSubHistoryConsumer;
import com.sinch.android.rtc.internal.natives.PubSubscriber;
import com.sinch.android.rtc.internal.service.pubnub.http.HttpRequesterFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class PublishSubscribeClient implements PubSubClient {
    private static final String TAG = PublishSubscribeClient.class.getSimpleName();
    private volatile boolean active = false;
    private Executor callbackExecutor;
    private Map<String, PubNubGetHistory> historyGetters;
    private final HttpRequesterFactory httpRequesterFactory;
    private String pubNubBaseUrl;
    private final ExecutorService publishExecutor;
    private Map<String, List<PubNubPublisher>> publishers;
    private final PubNubSubscriberFactory subscriberFactory;
    private Map<Subscription, PubNubSubscriber> subscribers;
    private boolean subscribtionsEnabled;

    class PubNubPublishCallback implements PubPublisher {
        PubPublisher callback;
        String channel;
        PubNubPublisher publisher;

        PubNubPublishCallback(String str, PubNubPublisher pubNubPublisher, PubPublisher pubPublisher) {
            this.channel = str;
            this.publisher = pubNubPublisher;
            this.callback = pubPublisher;
        }

        public void onPublishFailed(int i, String str, String str2) {
            if (this.callback != null) {
                this.callback.onPublishFailed(i, str, str2);
            }
            PublishSubscribeClient.this.removePublisher(this.channel, this.publisher);
        }

        public void onPublishSuccess(String str) {
            if (this.callback != null) {
                this.callback.onPublishSuccess(str);
            }
            PublishSubscribeClient.this.removePublisher(this.channel, this.publisher);
        }
    }

    public PublishSubscribeClient(HttpRequesterFactory httpRequesterFactory, PubNubSubscriberFactory pubNubSubscriberFactory, Executor executor, ExecutorService executorService) {
        this.httpRequesterFactory = httpRequesterFactory;
        this.subscriberFactory = pubNubSubscriberFactory;
        this.callbackExecutor = executor;
        this.publishExecutor = executorService;
        this.subscribers = new HashMap();
        this.historyGetters = new HashMap();
        this.publishers = new HashMap();
        this.subscribtionsEnabled = true;
    }

    private void addPublisher(String str, PubNubPublisher pubNubPublisher) {
        synchronized (this.publishers) {
            List list = (List) this.publishers.get(str);
            if (list == null) {
                list = new ArrayList();
            }
            list.add(pubNubPublisher);
            this.publishers.put(str, list);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void removePublisher(java.lang.String r4, com.sinch.android.rtc.internal.service.pubnub.PubNubPublisher r5) {
        /*
        r3 = this;
        r1 = r3.publishers;
        monitor-enter(r1);
        r0 = r3.publishers;	 Catch:{ all -> 0x001f }
        r0 = r0.get(r4);	 Catch:{ all -> 0x001f }
        r0 = (java.util.List) r0;	 Catch:{ all -> 0x001f }
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
    L_0x000e:
        return;
    L_0x000f:
        r0.remove(r5);	 Catch:{ all -> 0x001f }
        r2 = r0.size();	 Catch:{ all -> 0x001f }
        if (r2 == 0) goto L_0x0022;
    L_0x0018:
        r2 = r3.publishers;	 Catch:{ all -> 0x001f }
        r2.put(r4, r0);	 Catch:{ all -> 0x001f }
    L_0x001d:
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        goto L_0x000e;
    L_0x001f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        throw r0;
    L_0x0022:
        r0 = r3.publishers;	 Catch:{ all -> 0x001f }
        r0.remove(r4);	 Catch:{ all -> 0x001f }
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sinch.android.rtc.internal.service.pubnub.PublishSubscribeClient.removePublisher(java.lang.String, com.sinch.android.rtc.internal.service.pubnub.PubNubPublisher):void");
    }

    private void startSubscribe(Subscription subscription, PubSubscriber pubSubscriber) {
        if (this.subscribers.containsKey(subscription)) {
            ((PubNubSubscriber) this.subscribers.get(subscription)).addCallback(pubSubscriber);
            return;
        }
        PubNubSubscriber createSubscriber = this.subscriberFactory.createSubscriber(this.pubNubBaseUrl, subscription.getSubscriptionKey(), subscription.getChannels(), subscription.getTimeToken(), subscription.getTimeOffsetOnSubscribeInMs(), pubSubscriber, this.callbackExecutor);
        this.subscribers.put(subscription, createSubscriber);
        if (this.subscribtionsEnabled) {
            createSubscriber.subscribe(this.httpRequesterFactory.createHttpRequester());
        }
    }

    private void stopAllHistoryGetters() {
        synchronized (this.historyGetters) {
            for (String str : this.historyGetters.keySet()) {
                PubNubGetHistory pubNubGetHistory = (PubNubGetHistory) this.historyGetters.get(str);
                if (pubNubGetHistory != null) {
                    pubNubGetHistory.stopTrying();
                }
            }
            this.historyGetters.clear();
        }
    }

    private void stopAllPublishers() {
        synchronized (this.publishers) {
            for (String str : this.publishers.keySet()) {
                for (PubNubPublisher pubNubPublisher : (List) this.publishers.get(str)) {
                    if (pubNubPublisher != null) {
                        pubNubPublisher.abort();
                    }
                }
            }
            this.publishers.clear();
        }
    }

    private void stopAllSubscribers() {
        synchronized (this.subscribers) {
            for (Subscription subscription : this.subscribers.keySet()) {
                PubNubSubscriber pubNubSubscriber = (PubNubSubscriber) this.subscribers.get(subscription);
                if (pubNubSubscriber != null) {
                    pubNubSubscriber.unsubscribe();
                }
            }
        }
        this.subscribers.clear();
    }

    private void stopSubscribe(Subscription subscription, PubSubscriber pubSubscriber) {
        PubNubSubscriber pubNubSubscriber = (PubNubSubscriber) this.subscribers.get(subscription);
        if (pubNubSubscriber != null) {
            pubNubSubscriber.removeCallback(pubSubscriber);
            if (pubNubSubscriber.hasNoCallbackers()) {
                pubNubSubscriber.unsubscribe();
                this.subscribers.remove(subscription);
            }
        }
    }

    public void beginBroadcastHistoryGet(PubSubHistoryConsumer pubSubHistoryConsumer, double d) {
        PubSubHistoryConsumer pubSubHistoryConsumer2 = pubSubHistoryConsumer;
        new PubNubHistoryReader(this.httpRequesterFactory.createHttpRequester(), this.pubNubBaseUrl, -Double.valueOf(d).intValue(), pubSubHistoryConsumer2, this.callbackExecutor).startHistoryReader();
    }

    public void disableSubscriptions() {
        this.subscribtionsEnabled = false;
        for (PubNubSubscriber unsubscribe : this.subscribers.values()) {
            unsubscribe.unsubscribe();
        }
    }

    public void enableSubscriptions() {
        this.subscribtionsEnabled = true;
        for (PubNubSubscriber subscribe : this.subscribers.values()) {
            subscribe.subscribe(this.httpRequesterFactory.createHttpRequester());
        }
    }

    public void getHistory(PubSubHistoryConsumer pubSubHistoryConsumer, int i, String str, String str2, boolean z) {
        if (i == 0) {
            i = 100;
        }
        int min = Math.min(100, i);
        PubNubGetHistory pubNubGetHistory = new PubNubGetHistory(min, new PubNubHistoryListener(this.httpRequesterFactory.createHttpRequester(), this.pubNubBaseUrl, pubSubHistoryConsumer, this.callbackExecutor, min, str, str2, z, CurrentThreadSleeper.getInstance()));
        this.historyGetters.put(pubSubHistoryConsumer.getChannel(), pubNubGetHistory);
        pubNubGetHistory.get();
    }

    public boolean getHistoryExistsForChannel(String str) {
        return this.historyGetters.containsKey(str);
    }

    public String getHost() {
        return this.pubNubBaseUrl;
    }

    public boolean isSubscriptionEnabled() {
        return this.subscribtionsEnabled;
    }

    public void publish(String str, String str2, String str3, String str4, PubPublisher pubPublisher) {
        if (this.active) {
            PubNubPublisher pubNubPublisher = new PubNubPublisher(this.publishExecutor, this.httpRequesterFactory, this.pubNubBaseUrl, CurrentThreadSleeper.getInstance());
            addPublisher(str, pubNubPublisher);
            pubNubPublisher.publish(str, str2, str3, str4, new PubNubPublishCallback(str, pubNubPublisher, pubPublisher));
        } else if (pubPublisher != null) {
            pubPublisher.onPublishFailed(InternalErrorCodes.OtherOther, "PubSubClient is stopped.", "0");
        }
    }

    public void setHost(String str) {
        this.pubNubBaseUrl = "http://" + str;
    }

    public void setUseSsl(boolean z) {
    }

    public void startPubSubClient() {
        this.active = true;
    }

    public void startSubscribe(PubSubscriber pubSubscriber, double d) {
        startSubscribe(new Subscription(pubSubscriber.getChannels(), pubSubscriber.getSubscribeKey(), -((int) d)), pubSubscriber);
    }

    public void startSubscribe(PubSubscriber pubSubscriber, String str) {
        startSubscribe(new Subscription(pubSubscriber.getChannels(), pubSubscriber.getSubscribeKey(), str), pubSubscriber);
    }

    public void stopAllHistoryGet() {
        stopAllHistoryGetters();
    }

    public void stopPubSubClient() {
        this.active = false;
        stopSubscribersAndHistory();
        stopAllPublishers();
        this.callbackExecutor = null;
        this.publishExecutor.shutdown();
    }

    public void stopSubscribe(PubSubscriber pubSubscriber) {
        stopSubscribe(new Subscription(pubSubscriber.getChannels(), pubSubscriber.getSubscribeKey()), pubSubscriber);
    }

    public void stopSubscribersAndHistory() {
        stopAllHistoryGetters();
        stopAllSubscribers();
    }
}
