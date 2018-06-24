package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.android.rtc.internal.service.pubnub.PubNubHistoryListener.ListenerResponse;

public class PubNubGetHistory {
    public static final int MAX_BATCH_SIZE = 100;
    private static final String TAG = PubNubGetHistory.class.getSimpleName();
    private int count;
    private PubNubHistoryListener listener;
    private final PubNubHistoryFetcher thread = new PubNubHistoryFetcher();

    public class PubNubHistoryFetcher extends Thread {
        private volatile boolean run = true;
        private boolean shouldRetry;

        public void run() {
            int i = 0;
            do {
                this.shouldRetry = true;
                while (this.run && this.shouldRetry) {
                    ListenerResponse readMessages = PubNubGetHistory.this.listener.readMessages();
                    i = readMessages.lastCount;
                    this.shouldRetry = readMessages.requestFailed;
                }
                if (!this.run) {
                    return;
                }
            } while (i == PubNubGetHistory.this.count);
        }

        public void stopThread() {
            this.run = false;
        }
    }

    public PubNubGetHistory(int i, PubNubHistoryListener pubNubHistoryListener) {
        this.count = i;
        this.listener = pubNubHistoryListener;
    }

    public void get() {
        this.thread.setName("PubNub History Getter");
        this.thread.start();
    }

    public String getChannel() {
        return this.listener.getChannel();
    }

    public void stopTrying() {
        this.thread.stopThread();
        this.listener.abort();
        this.thread.interrupt();
    }
}
