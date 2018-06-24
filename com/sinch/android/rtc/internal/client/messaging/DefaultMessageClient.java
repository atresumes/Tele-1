package com.sinch.android.rtc.internal.client.messaging;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.internal.client.SinchClientStatusProvider;
import com.sinch.android.rtc.internal.client.SinchLogger;
import com.sinch.android.rtc.internal.natives.MessageError;
import com.sinch.android.rtc.internal.natives.MessageInfo;
import com.sinch.android.rtc.internal.natives.MessagingEventListener;
import com.sinch.android.rtc.internal.natives.PushPayloadDataPair;
import com.sinch.android.rtc.internal.natives.jni.Messaging;
import com.sinch.android.rtc.internal.natives.jni.NativeMessage;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultMessageClient implements MessagingEventListener, MessageClient {
    static final /* synthetic */ boolean $assertionsDisabled = (!DefaultMessageClient.class.desiredAssertionStatus());
    private static final int MAX_RECIPIENTS = 10;
    private static final String TAG = MessageClient.class.getSimpleName();
    private SinchLogger logger;
    private SinchClientStatusProvider mSinchClientStatusProvider;
    private CopyOnWriteArraySet<MessageClientListener> messageListeners = new CopyOnWriteArraySet();
    private Messaging messaging;

    public DefaultMessageClient(SinchLogger sinchLogger, SinchClientStatusProvider sinchClientStatusProvider, Messaging messaging) {
        this.logger = sinchLogger;
        this.mSinchClientStatusProvider = sinchClientStatusProvider;
        this.messaging = messaging;
    }

    private void notifyOnIncomingMessage(Message message) {
        warnIfNoListeners("notifyOnIncomingMessage");
        Iterator it = this.messageListeners.iterator();
        while (it.hasNext()) {
            ((MessageClientListener) it.next()).onIncomingMessage(this, message);
        }
    }

    private void notifyOnMessageDelivered(MessageInfo messageInfo) {
        warnIfNoListeners("notifyOnMessageDelivered");
        Iterator it = this.messageListeners.iterator();
        while (it.hasNext()) {
            ((MessageClientListener) it.next()).onMessageDelivered(this, messageInfo);
        }
    }

    private void notifyOnMessageFailed(Message message, MessageError messageError) {
        warnIfNoListeners("notifyOnMessageFailed");
        Iterator it = this.messageListeners.iterator();
        while (it.hasNext()) {
            ((MessageClientListener) it.next()).onMessageFailed(this, message, messageError);
        }
    }

    private void notifyOnMessageSent(Message message, String str) {
        warnIfNoListeners("notifyOnMessageSent");
        Iterator it = this.messageListeners.iterator();
        while (it.hasNext()) {
            ((MessageClientListener) it.next()).onMessageSent(this, message, str);
        }
    }

    private void notifyOnShouldSendPushNotification(Message message, List<PushPair> list) {
        if ($assertionsDisabled || list.size() > 0) {
            warnIfNoListeners("notifyOnShouldSendPushNotification");
            Iterator it = this.messageListeners.iterator();
            while (it.hasNext()) {
                ((MessageClientListener) it.next()).onShouldSendPushData(this, message, list);
            }
            return;
        }
        throw new AssertionError();
    }

    private void throwIfDisposed() {
        if (this.mSinchClientStatusProvider.isDisposed()) {
            throw new IllegalStateException("SinchClient is stopped, you may not perform further actions until it is recreated");
        }
    }

    private void throwIfMissingCapability() {
        if (!this.mSinchClientStatusProvider.isSupportMessaging()) {
            throw new IllegalStateException("Messaging capability is not enabled");
        }
    }

    private void warnIfNoListeners(String str) {
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        } else if (this.messageListeners.size() == 0) {
            this.logger.mo2309w(TAG, str + "no MessageClientListener(s)");
        }
    }

    public void addMessageClientListener(MessageClientListener messageClientListener) {
        throwIfDisposed();
        if (messageClientListener == null) {
            throw new IllegalArgumentException("Must have non-null listener");
        }
        this.messageListeners.add(messageClientListener);
        if (this.messageListeners.size() == 1) {
            this.messaging.setEventListener(this);
        }
    }

    public void onIncomingMessage(NativeMessage nativeMessage) {
        this.logger.mo2263d(TAG, "onIncomingMessage: " + nativeMessage.toString());
        notifyOnIncomingMessage(new DefaultMessage(nativeMessage));
    }

    public void onMessageDelivered(MessageInfo messageInfo) {
        this.logger.mo2263d(TAG, "onDelivered: " + messageInfo);
        notifyOnMessageDelivered(messageInfo);
    }

    public void onMessageFailed(NativeMessage nativeMessage, MessageError messageError) {
        this.logger.mo2263d(TAG, "onFailed: " + messageError);
        notifyOnMessageFailed(new DefaultMessage(nativeMessage), messageError);
    }

    public void onMessageSent(NativeMessage nativeMessage, MessageInfo messageInfo) {
        this.logger.mo2263d(TAG, "onSentMessage: " + nativeMessage.toString() + " to recipient with id" + messageInfo.getRecipientId());
        notifyOnMessageSent(new DefaultMessage(nativeMessage, messageInfo.getTimestamp()), messageInfo.getRecipientId());
    }

    public void onShouldSendPushData(NativeMessage nativeMessage, List<PushPayloadDataPair> list) {
        List arrayList = new ArrayList(list.size());
        for (PushPair add : list) {
            arrayList.add(add);
        }
        notifyOnShouldSendPushNotification(new DefaultMessage(nativeMessage), arrayList);
    }

    public void removeMessageClientListener(MessageClientListener messageClientListener) {
        throwIfDisposed();
        if (messageClientListener == null) {
            throw new IllegalArgumentException("Must have non-null listener");
        }
        this.messageListeners.remove(messageClientListener);
        if (this.messageListeners.size() == 0) {
            this.messaging.setEventListener(null);
        }
    }

    public void send(WritableMessage writableMessage) {
        throwIfDisposed();
        throwIfMissingCapability();
        if (writableMessage == null) {
            throw new IllegalArgumentException("Must have non-null message");
        }
        Collection recipientIds = writableMessage.getRecipientIds();
        if (recipientIds == null) {
            throw new IllegalArgumentException("Must have non-null recipient list");
        }
        Set hashSet = new HashSet(recipientIds);
        if (hashSet.size() == 0 || hashSet.size() > 10) {
            throw new IllegalArgumentException("Must have at least 1 and at most 10 recipients");
        }
        String messageId = writableMessage.getMessageId();
        if (messageId == null || messageId.length() == 0) {
            throw new IllegalArgumentException("Must have non-empty messageId");
        }
        String textBody = writableMessage.getTextBody();
        if (textBody == null) {
            throw new IllegalArgumentException("Must have non-null textBody");
        }
        Map headers = writableMessage.getHeaders();
        this.messaging.sendMessage(messageId, (String[]) headers.keySet().toArray(new String[headers.size()]), (String[]) headers.values().toArray(new String[headers.size()]), (String[]) hashSet.toArray(new String[hashSet.size()]), textBody, new Date().getTime() / 1000);
    }
}
