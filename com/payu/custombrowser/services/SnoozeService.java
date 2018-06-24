package com.payu.custombrowser.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.C0517R;
import com.payu.custombrowser.CBActivity;
import com.payu.custombrowser.CustomBrowserMain;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;
import javax.net.ssl.SSLException;
import org.json.JSONObject;

public class SnoozeService extends Service {
    private static int IMAGE_DOWNLOAD_TIME_OUT;
    private final String CURRENT_URL = CBConstant.CURRENT_URL;
    private int EXPONENTIAL_BACKOFF_TIME_THRESHOLD = 60000;
    String MERCHANT_CHECKOUT_ACTIVITY = CBConstant.MERCHANT_CHECKOUT_ACTIVITY;
    private final String SNOOZE_BROAD_CAST_MESSAGE = "snooze_broad_cast_message";
    private final String SNOOZE_GET_WEBVIEW_STATUS_INTENT_ACTION = "webview_status_action";
    private int SNOOZE_SERVICE_TTL = CBConstant.DEFAULT_SURE_PAY_TTL;
    private final int TRACK_WEB_VIEW_TIME_INTERVAL = 500;
    private String broadCastingMessage = "";
    private CBUtil cbUtil;
    private String currentUrl = "";
    private CustomBrowserConfig customBrowserConfig;
    private long endTime;
    private int exponentialBackOffTime = 1000;
    private Handler handler;
    private long imageDownloadTime;
    private boolean isImageDownloadTimedOut;
    private boolean isNotificationIntentPrepared;
    private boolean isServiceAlive = true;
    private boolean isWebViewAlive;
    private boolean isWebViewIntentPrepared;
    private CountDownTimer killSnoozeServiceCounter;
    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    private String merchantCheckoutActivity;
    private String payuResponse;
    private String postData = "";
    private HashMap<String, String> postParamsMap;
    private String postURL = "";
    private String receivedMessage = "";
    private Runnable runnable;
    private final IBinder snoozeBinder = new SnoozeBinder();
    private HandlerThread snoozeHandlerThread;
    private long startTime;
    private long timeToNotify;
    private Handler trackWebViewStatusHandler;
    private String verifyParam;
    private boolean verifyPaymentCheck;
    private Runnable verifyPaymentRunnable = new C05271();
    private String verifyURL = "https://info.payu.in/merchant/postservice?form=2";

    class C05271 implements Runnable {
        C05271() {
        }

        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(SnoozeService.this.verifyURL).openConnection();
                String post = "command=verifyTxnStatus&var1=" + SnoozeService.this.cbUtil.getDataFromPostData(SnoozeService.this.postData, "txnid") + "&key=" + SnoozeService.this.cbUtil.getDataFromPostData(SnoozeService.this.postData, "key") + "&priorityParam=" + SnoozeService.this.verifyParam;
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(CBConstant.VERIFY_HTTP_TIMEOUT);
                conn.setRequestProperty("Content-Type", CBConstant.HTTP_URLENCODED);
                conn.setRequestProperty("Content-Length", String.valueOf(post.length()));
                conn.setRequestProperty("Cookie", "PHPSESSID=" + SnoozeService.this.cbUtil.getCookie(CBConstant.PHPSESSID, SnoozeService.this.getApplicationContext()) + "; PAYUID=" + SnoozeService.this.cbUtil.getCookie(CBConstant.PAYUID, SnoozeService.this.getApplicationContext()));
                conn.setDoOutput(true);
                conn.getOutputStream().write(post.getBytes());
                byte[] buffer = new byte[1024];
                if (conn.getResponseCode() != Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    SnoozeService.this.launchNotificationTransactionUpdate("{\"api_status\":\"0\",\"message\":\"Some error occurred\"}");
                } else if (conn.getInputStream() != null) {
                    StringBuffer responseStringBuffer = CBUtil.getStringBufferFromInputStream(conn.getInputStream());
                    if (responseStringBuffer != null) {
                        JSONObject jsonObject = new JSONObject(responseStringBuffer.toString());
                        SnoozeService.this.payuResponse = responseStringBuffer.toString();
                        SnoozeService.this.launchNotificationTransactionUpdate(responseStringBuffer.toString());
                    }
                }
            } catch (Exception e) {
                SnoozeService.this.launchNotificationTransactionUpdate("{\"api_status\":\"0\",\"message\":\"Some exception occurred\"}");
                e.printStackTrace();
            }
        }
    }

    class C05282 implements Runnable {
        C05282() {
        }

        public void run() {
            if (SnoozeService.this.isServiceAlive) {
                SnoozeService.this.downloadImage();
            }
        }
    }

    private final class ServiceHandler extends Handler {

        class C05322 implements Runnable {
            C05322() {
            }

            public void run() {
                if (SnoozeService.this.isServiceAlive) {
                    if (SnoozeService.this.broadCastingMessage.contentEquals(SnoozeService.this.receivedMessage)) {
                        SnoozeService.this.isWebViewAlive = true;
                    } else {
                        SnoozeService.this.isWebViewAlive = false;
                        if (SnoozeService.this.isServiceAlive && SnoozeService.this.verifyPaymentCheck && SnoozeService.this.isNotificationIntentPrepared && SnoozeService.this.isWebViewIntentPrepared) {
                            SnoozeService.this.launchNotificationTransactionUpdate(SnoozeService.this.payuResponse);
                        } else if (SnoozeService.this.isServiceAlive && SnoozeService.this.isNotificationIntentPrepared && SnoozeService.this.isWebViewIntentPrepared) {
                            SnoozeService.this.launchNotification(SnoozeService.this.isWebViewAlive);
                        }
                    }
                    SnoozeService.this.trackWebViewStatusHandler.postDelayed(this, 500);
                    Intent broadCastIntent = new Intent("webview_status_action");
                    SnoozeService.this.broadCastingMessage = "" + System.currentTimeMillis();
                    broadCastIntent.putExtra("snooze_broad_cast_message", SnoozeService.this.broadCastingMessage);
                    LocalBroadcastManager.getInstance(SnoozeService.this).sendBroadcast(broadCastIntent);
                }
            }
        }

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            SnoozeService.this.isServiceAlive = true;
            SnoozeService.this.killSnoozeServiceCounter = new CountDownTimer((long) SnoozeService.this.SNOOZE_SERVICE_TTL, 5000) {
                public void onTick(long l) {
                    SnoozeService.this.timeToNotify = (((long) SnoozeService.this.SNOOZE_SERVICE_TTL) - l) / 1000;
                }

                public void onFinish() {
                    if (SnoozeService.this.isWebViewAlive && !SnoozeService.this.isNotificationIntentPrepared) {
                        Intent broadCastIntent = new Intent("webview_status_action");
                        broadCastIntent.putExtra(CBConstant.SNOOZE_SERVICE_STATUS, CBConstant.SNOOZE_SERVICE_DEAD);
                        LocalBroadcastManager.getInstance(SnoozeService.this).sendBroadcast(broadCastIntent);
                    }
                    if (!SnoozeService.this.isNotificationIntentPrepared) {
                        SnoozeService.this.launchNoInternetNotification();
                    }
                    SnoozeService.this.killSnoozeService();
                }
            };
            SnoozeService.this.killSnoozeServiceCounter.start();
            SnoozeService.this.trackWebViewStatusHandler = new Handler();
            SnoozeService.this.trackWebViewStatusHandler.postDelayed(new C05322(), 500);
            SnoozeService.this.calculateInternetSpeed();
        }
    }

    public class SnoozeBinder extends Binder {
        public SnoozeService getSnoozeService() {
            return SnoozeService.this;
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        this.isWebViewAlive = true;
        return this.snoozeBinder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.cbUtil = new CBUtil();
        this.merchantCheckoutActivity = intent.getStringExtra(this.MERCHANT_CHECKOUT_ACTIVITY);
        this.customBrowserConfig = (CustomBrowserConfig) intent.getParcelableExtra(CBConstant.CB_CONFIG);
        this.SNOOZE_SERVICE_TTL = this.customBrowserConfig.getSurePayBackgroundTTL();
        this.postParamsMap = this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData());
        IMAGE_DOWNLOAD_TIME_OUT = Bank.snoozeImageDownloadTimeout > 0 ? Bank.snoozeImageDownloadTimeout : 10000;
        if (intent.getExtras().containsKey(CBConstant.VERIFICATION_MSG_RECEIVED) && intent.getExtras().getBoolean(CBConstant.VERIFICATION_MSG_RECEIVED)) {
            this.verifyPaymentCheck = true;
            if (intent.getExtras().containsKey(CBConstant.VERIFY_ADDON_PARAMS)) {
                this.verifyParam = intent.getExtras().getString(CBConstant.VERIFY_ADDON_PARAMS);
            }
            this.postData = this.customBrowserConfig.getPayuPostData();
            this.postURL = this.customBrowserConfig.getPostURL();
        } else {
            this.verifyPaymentCheck = false;
            this.currentUrl = intent.getStringExtra(CBConstant.CURRENT_URL);
        }
        Message msg = this.mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        this.mServiceHandler.sendMessage(msg);
        return 3;
    }

    public void onCreate() {
        this.snoozeHandlerThread = new HandlerThread("SnoozeServiceHandlerThread", 10);
        this.snoozeHandlerThread.start();
        this.mServiceLooper = this.snoozeHandlerThread.getLooper();
        this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
    }

    public void killSnoozeService() {
        this.isServiceAlive = false;
        if (this.killSnoozeServiceCounter != null) {
            this.killSnoozeServiceCounter.cancel();
        }
        this.snoozeHandlerThread.interrupt();
        stopSelf();
    }

    public void updateWebviewStatus(String webviewStatus) {
        this.receivedMessage = webviewStatus;
    }

    private void calculateInternetSpeed() {
        this.handler = new Handler(this.mServiceLooper);
        this.runnable = new C05282();
        this.handler.postDelayed(this.runnable, (long) Math.min(this.exponentialBackOffTime, this.EXPONENTIAL_BACKOFF_TIME_THRESHOLD));
    }

    private void downloadImage() {
        this.isImageDownloadTimedOut = false;
        final String imageURL = CBConstant.SNOOZE_IMAGE_DOWNLOAD_END_POINT + CBConstant.SNOOZE_IMAGE_COLLECTIONS[new Random().nextInt(2)];
        final CountDownTimer imageDownloadTimer = new CountDownTimer((long) IMAGE_DOWNLOAD_TIME_OUT, 1000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                cancel();
                SnoozeService.this.isImageDownloadTimedOut = true;
            }
        };
        imageDownloadTimer.start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    SnoozeService.this.cbUtil;
                    if (CBUtil.isNetworkAvailable(SnoozeService.this.getApplicationContext())) {
                        SnoozeService.this.startTime = System.currentTimeMillis();
                        URLConnection connection = new URL(imageURL).openConnection();
                        connection.setUseCaches(false);
                        connection.connect();
                        int imageSize = connection.getContentLength();
                        InputStream input = connection.getInputStream();
                        byte[] buffer = new byte[1024];
                        while (!SnoozeService.this.isImageDownloadTimedOut) {
                            if (input.read(buffer) == -1) {
                                break;
                            }
                        }
                        if (SnoozeService.this.isImageDownloadTimedOut) {
                            imageDownloadTimer.cancel();
                            input.close();
                            SnoozeService.this.imageDownloadTime = (long) (SnoozeService.IMAGE_DOWNLOAD_TIME_OUT + 1);
                        } else {
                            imageDownloadTimer.cancel();
                            SnoozeService.this.endTime = System.currentTimeMillis();
                            input.close();
                            SnoozeService.this.imageDownloadTime = SnoozeService.this.endTime - SnoozeService.this.startTime;
                        }
                        if (SnoozeService.this.imageDownloadTime > ((long) SnoozeService.IMAGE_DOWNLOAD_TIME_OUT)) {
                            SnoozeService.this.exponentialBackOffTime = SnoozeService.this.exponentialBackOffTime + SnoozeService.this.exponentialBackOffTime;
                            SnoozeService.this.handler.postDelayed(SnoozeService.this.runnable, (long) Math.min(SnoozeService.this.exponentialBackOffTime, SnoozeService.this.EXPONENTIAL_BACKOFF_TIME_THRESHOLD));
                            return;
                        } else if (SnoozeService.this.isServiceAlive) {
                            SnoozeService.this.broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TYPE, CBAnalyticsConstant.INTERNET_RESTORED);
                            if (SnoozeService.this.verifyPaymentCheck) {
                                SnoozeService.this.broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_VERIFY_API_STATUS, CBAnalyticsConstant.SNOOZE_VERIFY_API_CALLED);
                                new Thread(SnoozeService.this.verifyPaymentRunnable).start();
                                return;
                            }
                            SnoozeService.this.launchNotification(SnoozeService.this.isWebViewAlive);
                            return;
                        } else {
                            return;
                        }
                    }
                    SnoozeService.this.handler.postDelayed(SnoozeService.this.runnable, (long) Math.min(SnoozeService.this.exponentialBackOffTime, SnoozeService.this.EXPONENTIAL_BACKOFF_TIME_THRESHOLD));
                } catch (MalformedURLException e) {
                    SnoozeService.this.imageDownloadTime = -1;
                    imageDownloadTimer.cancel();
                    e.printStackTrace();
                } catch (SSLException e2) {
                    SnoozeService.this.handler.postDelayed(SnoozeService.this.runnable, (long) Math.min(SnoozeService.this.exponentialBackOffTime, SnoozeService.this.EXPONENTIAL_BACKOFF_TIME_THRESHOLD));
                    e2.printStackTrace();
                } catch (IOException e3) {
                    SnoozeService.this.imageDownloadTime = -1;
                    imageDownloadTimer.cancel();
                    e3.printStackTrace();
                } catch (Exception e4) {
                    SnoozeService.this.imageDownloadTime = -1;
                    imageDownloadTimer.cancel();
                }
            }
        }).start();
    }

    private void launchNotification(boolean webViewLiving) {
        Builder mBuilder = new Builder(this);
        mBuilder.setContentTitle(this.customBrowserConfig.getSurePayNotificationGoodNetworkTitle()).setContentText(this.customBrowserConfig.getSurePayNotificationGoodNetWorkHeader()).setSmallIcon(this.customBrowserConfig.getSurePayNotificationIcon()).setAutoCancel(true).setPriority(1).setDefaults(2).setStyle(new BigTextStyle().bigText(this.customBrowserConfig.getSurePayNotificationGoodNetWorkHeader() + "\n\n" + this.customBrowserConfig.getSurePayNotificationGoodNetWorkBody()));
        if (VERSION.SDK_INT >= 23) {
            mBuilder.setColor(getResources().getColor(C0517R.color.cb_blue_button, null));
        } else {
            mBuilder.setColor(getResources().getColor(C0517R.color.cb_blue_button));
        }
        this.isNotificationIntentPrepared = true;
        Intent notifyIntent = new Intent();
        notifyIntent.putExtra(CBConstant.CURRENT_URL, this.currentUrl);
        notifyIntent.putExtra(CBConstant.SENDER, CBConstant.SNOOZE_SERVICE);
        boolean isValidMerchantCheckoutActivity = true;
        if (webViewLiving) {
            this.isWebViewIntentPrepared = true;
            notifyIntent.setFlags(536870912);
            notifyIntent.putExtra(CBConstant.CURRENT_URL, this.currentUrl);
            notifyIntent.putExtra(CBConstant.CB_CONFIG, this.customBrowserConfig);
            notifyIntent.setClass(getApplicationContext(), CBActivity.class);
        } else {
            Intent checkValidActivityIntent = new Intent();
            checkValidActivityIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity == null ? "" : this.merchantCheckoutActivity);
            if (checkValidActivityIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
                notifyIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity);
                notifyIntent.putExtra(CBConstant.POST_TYPE, "sure_pay_payment_data");
                notifyIntent.putExtra(CBConstant.POST_DATA, this.customBrowserConfig.getPayuPostData());
            } else {
                isValidMerchantCheckoutActivity = false;
            }
            broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_EXPECTED_ACTION, CBAnalyticsConstant.MERCHANT_CHECKOUT_PAGE);
            this.isWebViewIntentPrepared = false;
            killSnoozeService();
        }
        if (isValidMerchantCheckoutActivity) {
            mBuilder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent, 134217728));
            broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TIME, CustomBrowserMain.getSystemCurrentTime());
            ((NotificationManager) getSystemService("notification")).notify(CBConstant.SNOOZE_NOTIFICATION_ID, mBuilder.build());
            return;
        }
        try {
            throw new ActivityNotFoundException("The Activity " + this.merchantCheckoutActivity + " is not found, Please set valid activity ");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void broadcastAnalyticsEvent(String key, String value) {
        Intent broadCastIntent = new Intent("webview_status_action");
        broadCastIntent.putExtra(CBAnalyticsConstant.BROAD_CAST_FROM_SNOOZE_SERVICE, true);
        broadCastIntent.putExtra(CBAnalyticsConstant.KEY, key);
        broadCastIntent.putExtra(CBAnalyticsConstant.VALUE, value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
    }

    private void launchNotificationTransactionUpdate(String verifyResponse) {
        try {
            JSONObject jsonObject = new JSONObject(verifyResponse);
            if (jsonObject.has(getString(C0517R.string.cb_snooze_verify_api_status))) {
                broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_VERIFY_API_RESPONSE_RECEIVED, Integer.parseInt(jsonObject.get(getString(C0517R.string.cb_snooze_verify_api_status)).toString()) + "");
            }
            if (jsonObject.has(getString(C0517R.string.cb_snooze_verify_api_status))) {
                CharSequence surePayNotificationTransactionVerifiedHeader;
                int verifyApiStatus = Integer.parseInt(jsonObject.get(getString(C0517R.string.cb_snooze_verify_api_status)).toString());
                if (verifyApiStatus == 1) {
                    broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TIME, CustomBrowserMain.getSystemCurrentTime());
                    broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TYPE, CBAnalyticsConstant.TRANSACTION_VERIFIED);
                } else if (verifyApiStatus == 0) {
                    broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TIME, CustomBrowserMain.getSystemCurrentTime());
                    broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TYPE, CBAnalyticsConstant.TRANSACTION_STATUS_UNKNOWN);
                }
                Builder mBuilder = new Builder(this);
                String snoozeNotificationTxnVerifiedBigText = verifyApiStatus == 1 ? this.customBrowserConfig.getSurePayNotificationTransactionVerifiedHeader() + "\n\n" + this.customBrowserConfig.getSurePayNotificationTransactionVerifiedBody() : this.customBrowserConfig.getSurePayNotificationTransactionNotVerifiedHeader() + "\n\n" + this.customBrowserConfig.getSurePayNotificationTransactionNotVerifiedBody();
                NotificationCompat.Builder contentTitle = mBuilder.setContentTitle(verifyApiStatus == 1 ? this.customBrowserConfig.getSurePayNotificationTransactionVerifiedTitle() : this.customBrowserConfig.getSurePayNotificationTransactionNotVerifiedTitle());
                if (verifyApiStatus == 1) {
                    surePayNotificationTransactionVerifiedHeader = this.customBrowserConfig.getSurePayNotificationTransactionVerifiedHeader();
                } else {
                    surePayNotificationTransactionVerifiedHeader = this.customBrowserConfig.getSurePayNotificationTransactionNotVerifiedHeader();
                }
                contentTitle.setContentText(surePayNotificationTransactionVerifiedHeader).setSmallIcon(this.customBrowserConfig.getSurePayNotificationIcon()).setAutoCancel(true).setPriority(1).setDefaults(2).setStyle(new BigTextStyle().bigText(snoozeNotificationTxnVerifiedBigText));
                Intent notifyIntent = new Intent();
                notifyIntent.putExtra(CBConstant.CB_CONFIG, this.customBrowserConfig);
                this.isNotificationIntentPrepared = true;
                notifyIntent.putExtra(CBConstant.PAYU_RESPONSE, verifyResponse);
                boolean isValidMerchantCheckoutActivity = true;
                if (this.isWebViewAlive) {
                    notifyIntent.setFlags(805306368);
                    this.isWebViewIntentPrepared = true;
                    notifyIntent.putExtra(CBConstant.SENDER, CBConstant.SNOOZE_SERVICE);
                    notifyIntent.putExtra(CBConstant.VERIFICATION_MSG_RECEIVED, true);
                    notifyIntent.setClass(getApplicationContext(), CBActivity.class);
                } else {
                    Intent checkValidActivityIntent = new Intent();
                    checkValidActivityIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity == null ? "" : this.merchantCheckoutActivity);
                    if (checkValidActivityIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
                        notifyIntent.putExtra(CBConstant.POST_DATA, verifyResponse);
                        notifyIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity);
                        notifyIntent.putExtra(CBConstant.POST_TYPE, "verify_response_post_data");
                    } else {
                        isValidMerchantCheckoutActivity = false;
                    }
                    broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_EXPECTED_ACTION, CBAnalyticsConstant.MERCHANT_CHECKOUT_PAGE);
                    this.isWebViewIntentPrepared = false;
                    killSnoozeService();
                }
                if (isValidMerchantCheckoutActivity) {
                    mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, notifyIntent, 134217728));
                    ((NotificationManager) getSystemService("notification")).notify(CBConstant.TRANSACTION_STATUS_NOTIFICATION_ID, mBuilder.build());
                    return;
                }
                try {
                    throw new ActivityNotFoundException("The Activity " + this.merchantCheckoutActivity + " is not found, Please set valid activity ");
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void launchNoInternetNotification() {
        broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TIME, CustomBrowserMain.getSystemCurrentTime());
        broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_TYPE, CBAnalyticsConstant.NO_INTERNET_FOUND);
        broadcastAnalyticsEvent(CBAnalyticsConstant.SNOOZE_NOTIFICATION_EXPECTED_ACTION, CBAnalyticsConstant.MERCHANT_CHECKOUT_PAGE);
        Builder mBuilder = new Builder(this);
        mBuilder.setContentTitle(this.customBrowserConfig.getSurePayNotificationPoorNetWorkTitle()).setContentText(this.customBrowserConfig.getSurePayNotificationPoorNetWorkHeader()).setSmallIcon(this.customBrowserConfig.getSurePayNotificationIcon()).setAutoCancel(true).setPriority(1).setDefaults(2).setStyle(new BigTextStyle().bigText(this.customBrowserConfig.getSurePayNotificationPoorNetWorkHeader() + this.customBrowserConfig.getSurePayNotificationPoorNetWorkBody()));
        if (VERSION.SDK_INT >= 23) {
            mBuilder.setColor(getResources().getColor(C0517R.color.cb_blue_button, null));
        } else {
            mBuilder.setColor(getResources().getColor(C0517R.color.cb_blue_button));
        }
        Intent checkValidActivityIntent = new Intent();
        checkValidActivityIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity == null ? "" : this.merchantCheckoutActivity);
        if (checkValidActivityIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
            Intent notifyIntent = new Intent();
            notifyIntent.setClassName(getApplicationContext(), this.merchantCheckoutActivity);
            notifyIntent.putExtra(CBConstant.POST_TYPE, "sure_pay_payment_data");
            notifyIntent.putExtra(CBConstant.POST_DATA, this.customBrowserConfig.getPayuPostData());
            mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, notifyIntent, 134217728));
            ((NotificationManager) getSystemService("notification")).notify(CBConstant.SNOOZE_NOTIFICATION_ID, mBuilder.build());
            return;
        }
        try {
            throw new ActivityNotFoundException("The Activity " + this.merchantCheckoutActivity + " is not found, Please set valid activity ");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
