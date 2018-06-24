package com.talktoangel.gts.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.controller.Controller;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NotificationUtils {
    private Context context;
    private SessionManager mSessionManager;

    public NotificationUtils(Context context) {
        this.context = context;
        this.mSessionManager = new SessionManager(context);
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        this.mSessionManager = new SessionManager(this.context);
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    private void showNotificationMessage(String title, String message, String timeStamp, Intent intent, String imageUrl) {
        this.mSessionManager = new SessionManager(this.context);
        if (!TextUtils.isEmpty(message)) {
            intent.setFlags(603979776);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this.context, 0, intent, 268435456);
            Builder mBuilder = new Builder(this.context);
            Uri alarmSound = Uri.parse("android.resource://" + this.context.getPackageName() + "/raw/notification");
            if (TextUtils.isEmpty(imageUrl)) {
                showSmallNotification(mBuilder, C0585R.mipmap.ic_launcher, title, message, timeStamp, resultPendingIntent, alarmSound);
                playNotificationSound();
            } else if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, C0585R.mipmap.ic_launcher, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, C0585R.mipmap.ic_launcher, title, message, timeStamp, resultPendingIntent, alarmSound);
                }
            }
        }
    }

    private void showSmallNotification(Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        this.mSessionManager = new SessionManager(this.context);
        InboxStyle inboxStyle = new InboxStyle();
        if (Constant.appendNotificationMessages) {
            this.mSessionManager.addNotification(message);
            List<String> messages = Arrays.asList(this.mSessionManager.getNotifications().split("\\|"));
            for (int i = messages.size() - 1; i >= 0; i--) {
                inboxStyle.addLine((CharSequence) messages.get(i));
            }
        } else {
            inboxStyle.addLine(message);
        }
        ((NotificationManager) this.context.getSystemService("notification")).notify(100, mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setContentIntent(resultPendingIntent).setSound(alarmSound).setStyle(inboxStyle).setWhen(getTimeMilliSec(timeStamp)).setSmallIcon(C0585R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), icon)).setContentText(message).build());
    }

    private void showBigNotification(Bitmap bitmap, Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        BigPictureStyle bigPictureStyle = new BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        ((NotificationManager) this.context.getSystemService("notification")).notify(101, mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setContentIntent(resultPendingIntent).setSound(alarmSound).setStyle(bigPictureStyle).setWhen(getTimeMilliSec(timeStamp)).setSmallIcon(C0585R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), icon)).setContentText(message).build());
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(strURL).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void playNotificationSound() {
        try {
            RingtoneManager.getRingtone(Controller.getInstance().getApplicationContext(), Uri.parse("android.resource://" + Controller.getInstance().getApplicationContext().getPackageName() + "/raw/notification")).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 20) {
            for (RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
                if (processInfo.importance == 100) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
            return isInBackground;
        } else if (((RunningTaskInfo) am.getRunningTasks(1).get(0)).topActivity.getPackageName().equals(context.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    public static void clearNotifications() {
        ((NotificationManager) Controller.getInstance().getSystemService("notification")).cancelAll();
    }

    private static long getTimeMilliSec(String timeStamp) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timeStamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
