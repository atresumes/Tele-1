package com.talktoangel.gts.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.model.Message;
import com.talktoangel.gts.userauth.LoginActivity;
import com.talktoangel.gts.utils.Constant;
import com.talktoangel.gts.utils.SessionManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    Intent intent;
    SessionManager mSessionManager;
    String message;
    Intent pushNotification;
    String pushType = "";
    String task_id;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "" + remoteMessage.getData());
        Log.e(TAG, "" + ((String) remoteMessage.getData().get("pushmessage")));
        this.mSessionManager = new SessionManager(this);
        this.pushType = (String) remoteMessage.getData().get("push_type");
        this.message = ((String) remoteMessage.getData().get("pushmessage")).replace("\"", "");
        if (this.pushType.equalsIgnoreCase("typing_status")) {
            this.task_id = (String) remoteMessage.getData().get("sender_id");
            String typing_status = (String) remoteMessage.getData().get("typing_status");
            Log.e(TAG, this.mSessionManager.getIsBackground() + " - " + this.task_id);
            if (!this.mSessionManager.getIsBackground().equals(this.task_id)) {
                return;
            }
            if (typing_status.equalsIgnoreCase(Constant.ZERO)) {
                typingStatus(Constant.ZERO);
            } else if (typing_status.equalsIgnoreCase(Constant.ONE)) {
                typingStatus(Constant.ONE);
            }
        } else if (this.pushType.equalsIgnoreCase("new_message")) {
            this.task_id = (String) remoteMessage.getData().get("sender_id");
            String messageID = (String) remoteMessage.getData().get("message_id");
            String message = (String) remoteMessage.getData().get("pushmessage");
            String userName = (String) remoteMessage.getData().get("sender_name");
            String chatTime = (String) remoteMessage.getData().get("chat_time");
            this.mSessionManager.setNotificationCount(this.mSessionManager.getNotificationCount() + 1);
            if (this.mSessionManager.isLoggedIn()) {
                processUserMessage(messageID, userName, message, chatTime);
            }
        } else if (this.pushType.equalsIgnoreCase("appointment_status")) {
            sendNotification(getString(C0585R.string.app_name), this.message);
        } else {
            sendNotification(getString(C0585R.string.app_name), this.message);
        }
    }

    private void typingStatus(String status) {
        this.pushNotification = new Intent("typing_status");
        this.pushNotification.putExtra("typing", this.message);
        this.pushNotification.putExtra("status", status);
        Log.e("typingStatus", this.message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(this.pushNotification);
    }

    private void processUserMessage(String messageID, String title, String message, String time) {
        if (this.mSessionManager.getIsBackground().equals(this.task_id)) {
            Message msg = new Message();
            msg.setMessage(message);
            msg.setMsgId(messageID);
            msg.setCreatedAt(time);
            msg.setUserId(this.task_id);
            this.pushNotification = new Intent("pushmessage");
            this.pushNotification.putExtra("message", msg);
            this.pushNotification.putExtra("chat_room_id", "1");
            LocalBroadcastManager.getInstance(this).sendBroadcast(this.pushNotification);
        } else if (this.task_id.length() > 0) {
            sendNotification(title, message);
        }
        this.mSessionManager.setTask(this.task_id);
    }

    private void sendNotification(String title, String messageBody) {
        Context context = getBaseContext();
        if (this.mSessionManager.isLoggedIn()) {
            this.intent = new Intent(context, MainActivity.class);
            this.intent.putExtra("chat_room_id", this.task_id);
            this.intent.putExtra("name", title);
            Log.e("Extras", this.intent.getStringExtra("name") + "");
        } else {
            this.intent = new Intent(context, LoginActivity.class);
        }
        ((NotificationManager) getSystemService("notification")).notify(0, new Builder(this).setSmallIcon(C0585R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), C0585R.mipmap.ic_launcher)).setContentTitle(getString(C0585R.string.app_name)).setSubText(title).setContentText(messageBody).setAutoCancel(true).setDefaults(-1).setContentIntent(PendingIntent.getActivity(this, 0, this.intent, 134217728)).build());
    }
}
