package com.payu.custombrowser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.CustomBrowserMain.CBMainViewOnTouchListener;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.custombrowser.bean.CustomBrowserData;
import com.payu.custombrowser.cbinterface.MagicRetryCallbacks;
import com.payu.custombrowser.custombar.CustomProgressBar;
import com.payu.custombrowser.services.SnoozeService;
import com.payu.custombrowser.services.SnoozeService.SnoozeBinder;
import com.payu.custombrowser.util.C0533L;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import com.payu.custombrowser.util.SharedPreferenceUtil;
import com.payu.custombrowser.util.SnoozeConfigMap;
import com.talktoangel.gts.utils.SessionManager;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class PayUCBLifecycle extends CustomBrowserMain implements MagicRetryCallbacks {
    private static boolean isFromSnoozeService;
    public static int snoozeImageDownloadTimeout;
    protected static List<String> whiteListedUrls = new ArrayList();
    boolean SMSOTPClicked = false;
    private String SNOOZE_BROAD_CAST_MESSAGE = "snooze_broad_cast_message";
    protected String SNOOZE_GET_WEBVIEW_STATUS_INTENT_ACTION = "webview_status_action";
    Boolean approve_flag = Boolean.valueOf(false);
    View cbBlankOverlay;
    boolean checkLoading;
    boolean checkPermissionVisibility;
    boolean checkedPermission = false;
    int chooseActionHeight;
    CustomProgressBar customProgressBar;
    String enterOtpParams;
    protected boolean isSnoozeBroadCastReceiverRegistered = false;
    protected boolean isSnoozeEnabled = true;
    protected boolean isSnoozeServiceBounded = false;
    boolean mPageReady = false;
    String mPassword;
    boolean permissionGranted = true;
    boolean pin_selected_flag;
    boolean showCB = true;
    protected CountDownTimer slowUserCountDownTimer;
    protected AlertDialog slowUserWarningDialog;
    protected BroadcastReceiver snoozeBroadCastReceiver;
    SnoozeConfigMap snoozeConfigMap;
    protected int snoozeCount = 0;
    protected int snoozeCountBackwardJourney = 0;
    int[] snoozeLoadPercentageAndTimeOut;
    protected SnoozeService snoozeService;
    protected ServiceConnection snoozeServiceConnection = new C05041();
    protected int snoozeUrlLoadingPercentage;
    protected int snoozeUrlLoadingTimeout;
    protected int snoozeVisibleCountBackwdJourney;
    protected int snoozeVisibleCountFwdJourney;
    Timer waitingOTPTimer;

    class C05041 implements ServiceConnection {
        C05041() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SnoozeBinder snoozeBinder = (SnoozeBinder) iBinder;
            PayUCBLifecycle.this.snoozeService = snoozeBinder.getSnoozeService();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            PayUCBLifecycle.this.snoozeService = null;
        }
    }

    class C05052 extends BroadcastReceiver {
        C05052() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("broadcaststatus")) {
                Intent cbIntent = new Intent(PayUCBLifecycle.this.activity, CBActivity.class);
                cbIntent.putExtra(CBConstant.SENDER, CBConstant.SNOOZE_SERVICE);
                cbIntent.putExtra(CBConstant.VERIFICATION_MSG_RECEIVED, true);
                cbIntent.putExtra(CBConstant.PAYU_RESPONSE, intent.getExtras().getString(CBConstant.PAYU_RESPONSE));
                cbIntent.setFlags(805306368);
                PayUCBLifecycle.this.startActivity(cbIntent);
            }
            if (intent.hasExtra(PayUCBLifecycle.this.SNOOZE_BROAD_CAST_MESSAGE) && PayUCBLifecycle.this.snoozeService != null) {
                PayUCBLifecycle.this.snoozeService.updateWebviewStatus(intent.getStringExtra(PayUCBLifecycle.this.SNOOZE_BROAD_CAST_MESSAGE));
            }
            if (intent.getBooleanExtra(CBAnalyticsConstant.BROAD_CAST_FROM_SNOOZE_SERVICE, false)) {
                PayUCBLifecycle.this.addEventAnalytics(intent.getStringExtra(CBAnalyticsConstant.KEY), intent.getStringExtra(CBAnalyticsConstant.VALUE));
            }
            if (intent.hasExtra(CBConstant.SNOOZE_SERVICE_STATUS)) {
                PayUCBLifecycle.this.updateSnoozeDialogWithSnoozeServiceStatus();
            }
        }
    }

    class C05063 implements OnClickListener {
        C05063() {
        }

        public void onClick(View view) {
            PayUCBLifecycle.this.activity.finish();
        }
    }

    class C05074 implements OnCancelListener {
        C05074() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            PayUCBLifecycle.this.activity.finish();
        }
    }

    class C05085 implements OnTouchListener {
        C05085() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            PayUCBLifecycle.this.dismissSlowUserWarningTimerOnTouch();
            if (PayUCBLifecycle.this.cbTransparentView != null) {
                PayUCBLifecycle.this.cbTransparentView.setVisibility(8);
            }
            if (PayUCBLifecycle.this.frameState == 2) {
                PayUCBLifecycle.this.minimizeWebviewHeight();
            }
            return false;
        }
    }

    class C05116 implements OnKeyListener {

        class C05091 implements DialogInterface.OnClickListener {
            C05091() {
            }

            public void onClick(DialogInterface dialog, int which) {
                PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_OK_CLICK);
                dialog.dismiss();
                PayUCBLifecycle.this.onBackApproved();
                PayUCBLifecycle.this.activity.finish();
            }
        }

        class C05102 implements DialogInterface.OnClickListener {
            C05102() {
            }

            public void onClick(DialogInterface dialog, int which) {
                PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_CANCEL_CLICK);
                PayUCBLifecycle.this.onBackCancelled();
                dialog.dismiss();
            }
        }

        C05116() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == 1 && keyCode == 4) {
                if (PayUCBLifecycle.this.getArguments().getBoolean(CBConstant.BACK_BUTTON, true)) {
                    Builder alertDialog = new Builder(PayUCBLifecycle.this.activity);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Do you really want to cancel the transaction ?");
                    alertDialog.setPositiveButton("Ok", new C05091());
                    alertDialog.setNegativeButton("Cancel", new C05102());
                    PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.PAYU_BACK_BUTTON_CLICK);
                    PayUCBLifecycle.this.onBackPressed(alertDialog);
                    alertDialog.show();
                    return true;
                }
                PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.MERCHANT_BACK_BUTTON_CLICK);
                PayUCBLifecycle.this.activity.onBackPressed();
            }
            return false;
        }
    }

    class C05127 extends BroadcastReceiver {
        C05127() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (PayUCBLifecycle.this.mBankJS != null) {
                    Bundle myBundle = intent.getExtras();
                    if (PayUCBLifecycle.this.getActivity() != null) {
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            String msgBody = null;
                            String msgAddress = null;
                            Object[] pdus = (Object[]) extras.get("pdus");
                            if (pdus != null) {
                                SmsMessage[] msgs = new SmsMessage[pdus.length];
                                for (int i = 0; i < msgs.length; i++) {
                                    if (VERSION.SDK_INT >= 23) {
                                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], myBundle.getString("format"));
                                    } else {
                                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                    }
                                    msgBody = msgBody + msgs[i].getMessageBody();
                                    msgAddress = msgs[i].getDisplayOriginatingAddress();
                                }
                            }
                            PayUCBLifecycle.this.mPassword = CBUtil.filterSMS(PayUCBLifecycle.this.mBankJS, msgBody, PayUCBLifecycle.this.activity.getApplicationContext());
                            if (PayUCBLifecycle.this.mPassword != null) {
                                PayUCBLifecycle.this.fillOTP(this);
                                return;
                            }
                            if (PayUCBLifecycle.this.payuPG) {
                                PayUCBLifecycle.this.verificationMsgReceived = PayUCBLifecycle.this.checkConfirmMessage(msgAddress, msgBody);
                            }
                            if (PayUCBLifecycle.this.verificationMsgReceived) {
                                PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_BACKWARD_WINDOW_SMS_RECEIVED, CBAnalyticsConstant.RECEIVED);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C05169 implements Runnable {

        class C05152 implements Runnable {
            C05152() {
            }

            public void run() {
                PayUCBLifecycle.this.onPageStarted();
            }
        }

        C05169() {
        }

        public void run() {
            Exception e;
            final String snoozeConfig;
            String fileName = "initialize";
            HttpURLConnection conn = CBUtil.getHttpsConn(CBConstant.PRODUCTION_URL + fileName + ".js");
            if (conn != null) {
                try {
                    if (conn.getResponseCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                        PayUCBLifecycle.this.cbUtil.writeFileOutputStream(conn.getInputStream(), PayUCBLifecycle.this.activity, fileName, 0);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    try {
                        if (PayUCBLifecycle.this.activity != null) {
                            PayUCBLifecycle.this.mBankJS = new JSONObject(CBUtil.decodeContents(PayUCBLifecycle.this.activity.openFileInput(fileName)));
                            PayUCBLifecycle.this.setUrlString();
                            PayUCBLifecycle.this.checkVisibilityCB("", true);
                            snoozeConfig = PayUCBLifecycle.this.mBankJS.has("snooze_config") ? PayUCBLifecycle.this.mBankJS.get("snooze_config") + "('" + Bank.keyAnalytics + "')" : "";
                            PayUCBLifecycle.snoozeImageDownloadTimeout = Integer.parseInt(PayUCBLifecycle.this.mBankJS.has("snooze_image_download_time") ? PayUCBLifecycle.this.mBankJS.get("snooze_image_download_time").toString() : "0");
                            SharedPreferenceUtil.addIntToSharedPreference(PayUCBLifecycle.this.activity.getApplicationContext(), CBUtil.CB_PREFERENCE, CBConstant.SNOOZE_IMAGE_DOWNLOAD_TIME_OUT, PayUCBLifecycle.snoozeImageDownloadTimeout);
                            PayUCBLifecycle.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    PayUCBLifecycle.this.cbWebView.loadUrl("javascript:" + snoozeConfig);
                                }
                            });
                            if (PayUCBLifecycle.this.mPageReady && PayUCBLifecycle.this.activity != null) {
                                PayUCBLifecycle.this.activity.runOnUiThread(new C05152());
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (FileNotFoundException e3) {
                        e2 = e3;
                        PayUCBLifecycle.this.communicationError();
                        e2.printStackTrace();
                        return;
                    } catch (JSONException e4) {
                        e2 = e4;
                        PayUCBLifecycle.this.communicationError();
                        e2.printStackTrace();
                        return;
                    } catch (Exception e22) {
                        PayUCBLifecycle.this.communicationError();
                        e22.printStackTrace();
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        if (PayUCBLifecycle.this.activity != null) {
                            PayUCBLifecycle.this.mBankJS = new JSONObject(CBUtil.decodeContents(PayUCBLifecycle.this.activity.openFileInput(fileName)));
                            PayUCBLifecycle.this.setUrlString();
                            PayUCBLifecycle.this.checkVisibilityCB("", true);
                            snoozeConfig = PayUCBLifecycle.this.mBankJS.has("snooze_config") ? PayUCBLifecycle.this.mBankJS.get("snooze_config") + "('" + Bank.keyAnalytics + "')" : "";
                            PayUCBLifecycle.snoozeImageDownloadTimeout = Integer.parseInt(PayUCBLifecycle.this.mBankJS.has("snooze_image_download_time") ? PayUCBLifecycle.this.mBankJS.get("snooze_image_download_time").toString() : "0");
                            SharedPreferenceUtil.addIntToSharedPreference(PayUCBLifecycle.this.activity.getApplicationContext(), CBUtil.CB_PREFERENCE, CBConstant.SNOOZE_IMAGE_DOWNLOAD_TIME_OUT, PayUCBLifecycle.snoozeImageDownloadTimeout);
                            PayUCBLifecycle.this.activity.runOnUiThread(/* anonymous class already generated */);
                            if (PayUCBLifecycle.this.mPageReady && PayUCBLifecycle.this.activity != null) {
                                PayUCBLifecycle.this.activity.runOnUiThread(new C05152());
                            }
                        }
                    } catch (FileNotFoundException e5) {
                        e22 = e5;
                        PayUCBLifecycle.this.communicationError();
                        e22.printStackTrace();
                    } catch (JSONException e6) {
                        e22 = e6;
                        PayUCBLifecycle.this.communicationError();
                        e22.printStackTrace();
                    } catch (Exception e222) {
                        PayUCBLifecycle.this.communicationError();
                        e222.printStackTrace();
                    }
                }
            }
            try {
                if (PayUCBLifecycle.this.activity != null) {
                    PayUCBLifecycle.this.mBankJS = new JSONObject(CBUtil.decodeContents(PayUCBLifecycle.this.activity.openFileInput(fileName)));
                    PayUCBLifecycle.this.setUrlString();
                    PayUCBLifecycle.this.checkVisibilityCB("", true);
                    snoozeConfig = PayUCBLifecycle.this.mBankJS.has("snooze_config") ? PayUCBLifecycle.this.mBankJS.get("snooze_config") + "('" + Bank.keyAnalytics + "')" : "";
                    PayUCBLifecycle.snoozeImageDownloadTimeout = Integer.parseInt(PayUCBLifecycle.this.mBankJS.has("snooze_image_download_time") ? PayUCBLifecycle.this.mBankJS.get("snooze_image_download_time").toString() : "0");
                    SharedPreferenceUtil.addIntToSharedPreference(PayUCBLifecycle.this.activity.getApplicationContext(), CBUtil.CB_PREFERENCE, CBConstant.SNOOZE_IMAGE_DOWNLOAD_TIME_OUT, PayUCBLifecycle.snoozeImageDownloadTimeout);
                    PayUCBLifecycle.this.activity.runOnUiThread(/* anonymous class already generated */);
                    if (PayUCBLifecycle.this.mPageReady && PayUCBLifecycle.this.activity != null) {
                        PayUCBLifecycle.this.activity.runOnUiThread(new C05152());
                    }
                }
            } catch (FileNotFoundException e7) {
                e222 = e7;
                PayUCBLifecycle.this.communicationError();
                e222.printStackTrace();
            } catch (JSONException e8) {
                e222 = e8;
                PayUCBLifecycle.this.communicationError();
                e222.printStackTrace();
            } catch (Exception e2222) {
                PayUCBLifecycle.this.communicationError();
                e2222.printStackTrace();
            }
        }
    }

    public class PayUCBLifeCycleTouchListener extends CBMainViewOnTouchListener {
        public PayUCBLifeCycleTouchListener() {
            super();
        }

        public boolean onTouch(View v, MotionEvent event) {
            C0533L.m760v("sTag", "onTouch of PayUCBLifeCycleCalled");
            PayUCBLifecycle.this.dismissSlowUserWarningTimerOnTouch();
            return super.onTouch(v, event);
        }
    }

    abstract void dismissSlowUserWarningTimer();

    abstract void enter_otp(String str);

    abstract void onPageStarted();

    abstract void startSlowUserWarningTimer();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.cbUtil.resetPayuID();
        this.isSnoozeEnabled = this.cbUtil.getBooleanSharedPreferenceDefaultTrue(CBConstant.SNOOZE_ENABLED, getActivity().getApplicationContext());
        isFromSnoozeService = false;
        this.snoozeConfigMap = this.cbUtil.convertToSnoozeConfigMap(SharedPreferenceUtil.getSharedPrefMap(this.activity, CBConstant.SNOOZE_SHARED_PREF));
        this.snoozeLoadPercentageAndTimeOut = this.snoozeConfigMap.getPercentageAndTimeout(CBConstant.DEFAULT_PAYMENT_URLS);
        this.snoozeUrlLoadingPercentage = this.snoozeLoadPercentageAndTimeOut[0];
        this.snoozeUrlLoadingTimeout = this.snoozeLoadPercentageAndTimeOut[1];
        whiteListedUrls = CBUtil.processAndAddWhiteListedUrls(SharedPreferenceUtil.getStringFromSharedPreference(this.activity, CBConstant.SP_RETRY_FILE_NAME, CBConstant.SP_RETRY_WHITELISTED_URLS, ""));
        snoozeImageDownloadTimeout = SharedPreferenceUtil.getIntFromSharedPreference(this.activity.getApplicationContext(), CBUtil.CB_PREFERENCE, CBConstant.SNOOZE_IMAGE_DOWNLOAD_TIME_OUT, 0);
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        if (this.activity.getIntent().getStringExtra(CBConstant.SENDER) != null && this.activity.getIntent().getStringExtra(CBConstant.SENDER).contentEquals(CBConstant.SNOOZE_SERVICE)) {
            isFromSnoozeService = true;
        }
        this.snoozeBroadCastReceiver = new C05052();
        if (this.activity.getClass().getSimpleName().equalsIgnoreCase("CBActivity")) {
            cbOnCreate();
        } else {
            this.cbOldFlow = true;
            cbOldOnCreate();
        }
        initAnalytics(Bank.keyAnalytics);
        this.pin_selected_flag = false;
        if (this.activity != null) {
            this.cbUtil.clearCookie();
        }
        if (this.customBrowserConfig != null) {
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_ENABLE_COUNT, "" + this.customBrowserConfig.getEnableSurePay());
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_MODE_SET_MERCHANT, this.customBrowserConfig.getSurePayMode() == 1 ? "WARN" : "FAIL");
        }
    }

    private void updateSnoozeDialogWithSnoozeServiceStatus() {
        if (this.snoozeDialog != null && this.snoozeDialog.isShowing()) {
            this.snoozeDialog.cancel();
            this.snoozeDialog.dismiss();
        }
        View snoozeLayout = this.activity.getLayoutInflater().inflate(C0517R.layout.cb_layout_snooze, null);
        ((TextView) snoozeLayout.findViewById(C0517R.id.snooze_header_txt)).setText(getString(C0517R.string.cb_snooze_network_error));
        snoozeLayout.findViewById(C0517R.id.text_view_cancel_snooze_window).setVisibility(8);
        ((TextView) snoozeLayout.findViewById(C0517R.id.text_view_snooze_message)).setText(getString(C0517R.string.cb_snooze_network_down_message));
        snoozeLayout.findViewById(C0517R.id.snooze_loader_view).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.button_snooze_transaction).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.text_view_retry_message_detail).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.button_retry_transaction).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.button_cancel_transaction).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.t_confirm).setVisibility(8);
        snoozeLayout.findViewById(C0517R.id.t_nconfirm).setVisibility(8);
        Button goBackButton = (Button) snoozeLayout.findViewById(C0517R.id.button_go_back_snooze);
        goBackButton.setVisibility(0);
        goBackButton.setOnClickListener(new C05063());
        this.snoozeDialog = new android.support.v7.app.AlertDialog.Builder(this.activity).create();
        this.snoozeDialog.setView(snoozeLayout);
        this.snoozeDialog.setCanceledOnTouchOutside(false);
        this.snoozeDialog.setOnCancelListener(new C05074());
        this.snoozeDialog.show();
    }

    public void cbOldOnCreate() {
        this.bundle = getArguments();
        this.autoApprove = this.bundle.getBoolean("auto_approve", false);
        this.autoSelectOtp = this.bundle.getBoolean(CBConstant.AUTO_SELECT_OTP, false);
        this.storeOneClickHash = this.bundle.getInt(CBConstant.STORE_ONE_CLICK_HASH, 0);
        this.merchantSMSPermission = this.bundle.getBoolean(CBConstant.MERCHANT_SMS_PERMISSION, false);
        if (Bank.sdkVersion == null || Bank.sdkVersion.equalsIgnoreCase("")) {
            Bank.sdkVersion = getArguments().getString(CBConstant.SDK_DETAILS);
        }
        if (Bank.transactionID == null || Bank.transactionID.equalsIgnoreCase("")) {
            Bank.transactionID = getArguments().getString("txnid");
        }
        if (Bank.keyAnalytics == null || Bank.keyAnalytics.equalsIgnoreCase("")) {
            Bank.keyAnalytics = getArguments().getString("merchantid");
        }
    }

    public void cbOnCreate() {
        int i = 0;
        boolean z = true;
        if (getArguments() != null && getArguments().containsKey(CBConstant.CB_CONFIG)) {
            this.customBrowserConfig = (CustomBrowserConfig) getArguments().getParcelable(CBConstant.CB_CONFIG);
            boolean z2 = this.customBrowserConfig != null && this.customBrowserConfig.getMerchantSMSPermission() == 1;
            this.merchantSMSPermission = z2;
            if (this.customBrowserConfig == null || this.customBrowserConfig.getAutoApprove() != 1) {
                z2 = false;
            } else {
                z2 = true;
            }
            this.autoApprove = z2;
            if (this.customBrowserConfig == null || this.customBrowserConfig.getAutoSelectOTP() != 1) {
                z = false;
            }
            this.autoSelectOtp = z;
            if (this.customBrowserConfig != null) {
                i = this.customBrowserConfig.getStoreOneClickHash();
            }
            this.storeOneClickHash = i;
            String url;
            if (this.customBrowserConfig == null) {
                url = "";
            } else {
                url = this.customBrowserConfig.getPostURL();
            }
            if (this.customBrowserConfig != null) {
                if (Bank.keyAnalytics == null || Bank.keyAnalytics.trim().equals("")) {
                    if (this.customBrowserConfig.getMerchantKey() == null && this.customBrowserConfig.getMerchantKey().trim().equals("")) {
                        Bank.keyAnalytics = "";
                    } else {
                        Bank.keyAnalytics = this.customBrowserConfig.getMerchantKey();
                    }
                }
                if (Bank.transactionID == null || Bank.transactionID.trim().equals("")) {
                    if (this.customBrowserConfig.getTransactionID() == null || this.customBrowserConfig.getTransactionID().trim().equals("")) {
                        Bank.transactionID = "123";
                    } else {
                        Bank.transactionID = this.customBrowserConfig.getTransactionID();
                    }
                }
                if (Bank.sdkVersion != null && !Bank.sdkVersion.trim().equals("")) {
                    return;
                }
                if (this.customBrowserConfig.getSdkVersionName() == null || this.customBrowserConfig.getSdkVersionName().trim().equals("")) {
                    Bank.sdkVersion = "";
                } else {
                    Bank.sdkVersion = this.customBrowserConfig.getSdkVersionName();
                }
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View cbInflatedView;
        View cbMainView;
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.cbOldFlow) {
            cbInflatedView = inflater.inflate(C0517R.layout.bankold, container, false);
            cbMainView = cbInflatedView;
            cbInflatedView.bringToFront();
            cbOldFlowOnCreateView();
        } else {
            cbInflatedView = inflater.inflate(C0517R.layout.bank, container, false);
            this.cbTransparentView = cbInflatedView.findViewById(C0517R.id.trans_overlay);
            this.cbWebView = (WebView) cbInflatedView.findViewById(C0517R.id.webview);
            this.cbBlankOverlay = cbInflatedView.findViewById(C0517R.id.cb_blank_overlay);
            cbMainView = cbInflatedView.findViewById(C0517R.id.parent);
            cbOnCreateView();
        }
        CBUtil.setVariableReflection(CBConstant.MAGIC_RETRY_PAKAGE, BuildConfig.VERSION_NAME, CBConstant.CB_VERSION);
        this.cbBaseView = (FrameLayout) cbInflatedView.findViewById(C0517R.id.help_view);
        this.cbSlideBarView = cbInflatedView.findViewById(C0517R.id.view);
        this.cbWebPageProgressBar = (ProgressBar) cbInflatedView.findViewById(C0517R.id.cb_progressbar);
        initCBSettings();
        getInitializeJS();
        cbMainView.setOnTouchListener(new PayUCBLifeCycleTouchListener());
        return cbInflatedView;
    }

    private void initCBSettings() {
        this.cbWebView.getSettings().setJavaScriptEnabled(true);
        this.cbWebView.addJavascriptInterface(this, "PayU");
        this.cbWebView.getSettings().setSupportMultipleWindows(true);
        this.cbWebView.setOnTouchListener(new C05085());
        this.cbWebView.getSettings().setDomStorageEnabled(true);
        this.cbWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        this.cbWebView.getSettings().setCacheMode(2);
        this.cbWebView.getSettings().setAppCacheEnabled(false);
    }

    public void cbOldFlowOnCreateView() {
        this.cbWebView = (WebView) this.activity.findViewById(getArguments().getInt(CBConstant.WEBVIEW));
        if (Bank.paymentMode != null && Bank.paymentMode.equalsIgnoreCase(CBConstant.NB)) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        } else if (this.customBrowserConfig != null && this.customBrowserConfig.getViewPortWideEnable() == 1) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        }
        this.cbWebView.setFocusable(true);
        if (getArguments().getBoolean(CBConstant.BACK_BUTTON, true)) {
            this.cbWebView.setOnKeyListener(new C05116());
        }
        if (Bank.paymentMode != null && Bank.paymentMode.equalsIgnoreCase(CBConstant.NB)) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        } else if (this.bundle.getBoolean(CBConstant.VIEWPORTWIDE, false)) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        }
    }

    public void cbOnCreateView() {
        if (Bank.paymentMode != null && Bank.paymentMode.equalsIgnoreCase(CBConstant.NB)) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        } else if (this.customBrowserConfig != null && this.customBrowserConfig.getViewPortWideEnable() == 1) {
            this.cbWebView.getSettings().setUseWideViewPort(true);
        }
        this.cbWebView.setWebChromeClient(new PayUWebChromeClient((Bank) this));
        if (this.customBrowserConfig.getEnableSurePay() > 0) {
            this.cbWebView.setWebViewClient(new PayUSurePayWebViewClient((Bank) this, Bank.keyAnalytics));
        } else {
            this.cbWebView.setWebViewClient(new PayUWebViewClient((Bank) this, Bank.keyAnalytics));
        }
        if (!(this.customBrowserConfig == null || this.customBrowserConfig.getPostURL() == null || this.customBrowserConfig.getPayuPostData() == null)) {
            this.cbWebView.postUrl(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData().getBytes());
        }
        if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
            CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().setCBProperties(this.cbWebView, (Bank) this);
        }
        if (this.customBrowserConfig != null && this.customBrowserConfig.getMagicretry() == 1) {
            if (this.customBrowserConfig.getEnableSurePay() == 0) {
                initMagicRetry();
            }
            if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().initializeMagicRetry((Bank) this, this.cbWebView, this.magicRetryFragment);
            }
        }
    }

    public void logOnTerminate() {
        try {
            addEventAnalytics(CBAnalyticsConstant.LAST_URL, CBUtil.updateLastUrl(this.cbUtil.getStringSharedPreference(this.activity.getApplicationContext(), CBAnalyticsConstant.LAST_URL)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cbUtil.deleteSharedPrefKey(this.activity.getApplicationContext(), CBAnalyticsConstant.LAST_URL);
        }
        if (!this.eventArray.contains(CBAnalyticsConstant.CUSTOM_BROWSER)) {
            this.eventRecorded = CBAnalyticsConstant.NON_CUSTOM_BROWSER;
            addEventAnalytics(CBAnalyticsConstant.CB_STATUS, this.eventRecorded);
        }
        this.eventRecorded = CBAnalyticsConstant.TERMINATE_TRANSACTION;
        addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
        if (!(this.progressDialog == null || this.progressDialog.isShowing())) {
            this.progressDialog.dismiss();
        }
        if (this.mBroadcastReceiver != null) {
            unregisterBroadcast(this.mBroadcastReceiver);
            this.mBroadcastReceiver = null;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.cbUtil.cancelTimer(this.timerProgress);
        if (this.snoozeDialog != null && this.snoozeDialog.isShowing()) {
            this.snoozeDialog.dismiss();
        }
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.cbUtil.cancelTimer(this.timerProgress);
        this.cbUtil.cancelTimer(this.waitingOTPTimer);
        if (this.slowUserCountDownTimer != null) {
            this.slowUserCountDownTimer.cancel();
        }
        addEventAnalytics(CBAnalyticsConstant.SNOOZE_COUNT, "" + (this.snoozeVisibleCountBackwdJourney + this.snoozeVisibleCountFwdJourney));
        CustomBrowserData.SINGLETON.setPayuCustomBrowserCallback(null);
        if (this.snoozeDialog != null && this.snoozeDialog.isShowing()) {
            this.snoozeDialog.dismiss();
        }
        if (!(this.snoozeBroadCastReceiver == null || !this.isSnoozeBroadCastReceiverRegistered || isFromSnoozeService)) {
            LocalBroadcastManager.getInstance(this.activity.getApplicationContext()).unregisterReceiver(this.snoozeBroadCastReceiver);
        }
        if (this.snoozeServiceConnection != null && this.isSnoozeServiceBounded) {
            this.activity.unbindService(this.snoozeServiceConnection);
        }
        if (this.snoozeService != null && isFromSnoozeService) {
            this.snoozeService.killSnoozeService();
        }
        if (this.loadingLayout != null) {
            this.customProgressBar.removeProgressDialog(this.loadingLayout.findViewById(C0517R.id.progress));
        }
        if (this.enterOTPView != null) {
            this.customProgressBar.removeProgressDialog(this.enterOTPView.findViewById(C0517R.id.progress));
        }
        if (this.payuDeviceAnalytics != null) {
            this.cbUtil.cancelTimer(this.payuDeviceAnalytics.getmTimer());
        }
        if (this.mAnalytics != null) {
            this.cbUtil.cancelTimer(this.mAnalytics.getmTimer());
        }
        this.cbUtil.cancelTimer(this.waitingOTPTimer);
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
        logOnTerminate();
        Bank.sdkVersion = null;
        Bank.keyAnalytics = null;
        Bank.transactionID = null;
        Bank.paymentMode = null;
        this.cbUtil.resetPayuID();
    }

    public void onResume() {
        super.onResume();
    }

    void prepareSmsListener() {
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new C05127();
            IntentFilter filter = new IntentFilter();
            filter.setPriority(9999999);
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerBroadcast(this.mBroadcastReceiver, filter);
        }
    }

    public void registerSMSBroadcast() {
        if (this.mBroadcastReceiver == null) {
            prepareSmsListener();
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.setPriority(9999999);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerBroadcast(this.mBroadcastReceiver, filter);
    }

    String getValueFromPostData(String key) {
        for (String item : this.customBrowserConfig.getPayuPostData().split("&")) {
            String[] items = item.split("=");
            if (items.length >= 2 && items[0].equalsIgnoreCase(key)) {
                return items[1];
            }
        }
        return null;
    }

    private boolean checkConfirmMessage(String msgAddress, String msgBody) {
        int count = 0;
        msgBody = msgBody.toLowerCase();
        boolean returnValue = false;
        if (msgAddress.contains(this.bankName)) {
            count = 0 + 1;
        }
        if (msgBody.toLowerCase().contains(getValueFromPostData(SdkConstants.AMOUNT).replace(",", ""))) {
            count++;
        }
        if (count == 2) {
            returnValue = true;
        }
        if (count == 0) {
            returnValue = false;
        }
        if (count == 0) {
            return false;
        }
        if (msgBody.contains("made") && msgBody.contains("purchase")) {
            return true;
        }
        if (msgBody.contains("account") && msgBody.contains("debited")) {
            return true;
        }
        if (msgBody.contains("ac") && msgBody.contains("debited")) {
            return true;
        }
        if (msgBody.contains("tranx") && msgBody.contains("made")) {
            return true;
        }
        if (msgBody.contains("transaction") && msgBody.contains("made")) {
            return true;
        }
        if (msgBody.contains("spent")) {
            return true;
        }
        if (msgBody.contains("Thank you using card for")) {
            return true;
        }
        if (!msgBody.contains(SessionManager.KEY_LICENSE_NO) || !msgBody.contains("initiated")) {
            return returnValue;
        }
        returnValue = msgBody.contains(SessionManager.KEY_LICENSE_NO) && msgBody.contains("initiated");
        return returnValue;
    }

    public void fillOTP(BroadcastReceiver mReceiver) {
        if (getActivity().findViewById(C0517R.id.otp_sms) != null) {
            final TextView otpSMS = (TextView) getActivity().findViewById(C0517R.id.otp_sms);
            if (this.showCB && this.mPassword != null && otpSMS.getVisibility() != 0) {
                this.cbUtil.cancelTimer(this.waitingOTPTimer);
                String str = this.eventRecorded;
                int i = -1;
                switch (str.hashCode()) {
                    case -557081102:
                        if (str.equals(CBAnalyticsConstant.PAYMENT_INITIATED)) {
                            i = 0;
                            break;
                        }
                        break;
                    case 674270068:
                        if (str.equals(CBAnalyticsConstant.OTP)) {
                            boolean z = true;
                            break;
                        }
                        break;
                    case 2084916017:
                        if (str.equals(CBAnalyticsConstant.REGENERATE)) {
                            i = 2;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                        break;
                    case 1:
                        this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_SELECTED;
                        break;
                    case 2:
                        this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_REGENERATE;
                        break;
                    default:
                        this.eventRecorded = CBAnalyticsConstant.OTP_WEB;
                        break;
                }
                addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                otpSMS.setText(this.mPassword);
                this.mPassword = null;
                this.customProgressBar.removeDialog(getActivity().findViewById(C0517R.id.progress));
                Button approveButton = (Button) getActivity().findViewById(C0517R.id.approve);
                approveButton.setClickable(true);
                CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                approveButton.setVisibility(0);
                this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                this.activity.findViewById(C0517R.id.retry_text).setVisibility(8);
                this.activity.findViewById(C0517R.id.regenerate_layout).setVisibility(8);
                this.activity.findViewById(C0517R.id.waiting).setVisibility(8);
                this.activity.findViewById(C0517R.id.otp_recieved).setVisibility(0);
                otpSMS.setVisibility(0);
                if (this.autoApprove) {
                    approveButton.performClick();
                    this.eventRecorded = "auto_approve";
                    addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                }
                approveButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        try {
                            PayUCBLifecycle.this.mPassword = null;
                            PayUCBLifecycle.this.eventRecorded = CBAnalyticsConstant.APPROVED_OTP;
                            PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, PayUCBLifecycle.this.eventRecorded);
                            PayUCBLifecycle.this.addEventAnalytics(CBAnalyticsConstant.APPROVE_BTN_CLICK_TIME, CustomBrowserMain.getSystemCurrentTime());
                            PayUCBLifecycle.this.prepareSmsListener();
                            PayUCBLifecycle.this.checkLoading = false;
                            PayUCBLifecycle.this.approve_flag = Boolean.valueOf(true);
                            PayUCBLifecycle.this.onHelpUnavailable();
                            PayUCBLifecycle.this.maximiseWebviewHeight();
                            PayUCBLifecycle.this.frameState = 1;
                            PayUCBLifecycle.this.cbWebView.loadUrl("javascript:" + PayUCBLifecycle.this.mJS.getString(PayUCBLifecycle.this.getString(C0517R.string.cb_process_otp)) + "(\"" + otpSMS.getText().toString() + "\")");
                            otpSMS.setText("");
                            PayUCBLifecycle.this.hideSoftKeyboard();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
                if (this.mBroadcastReceiver != null) {
                    mReceiver.abortBroadcast();
                    unregisterBroadcast(this.mBroadcastReceiver);
                    this.mBroadcastReceiver = null;
                }
            }
        }
    }

    private void getInitializeJS() {
        prepareSmsListener();
        this.eventRecorded = CBAnalyticsConstant.PAYMENT_INITIATED;
        addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
        this.serialExecutor.execute(new C05169());
    }

    public void updateHeight(View view) {
        if (this.maxWebview == 0) {
            calculateMaximumWebViewHeight();
            maximiseWebviewHeight();
        }
        calculateCBHeight(view);
    }

    public void updateLoaderHeight() {
        if (this.chooseActionHeight == 0) {
            this.cbWebView.measure(-1, -1);
            this.chooseActionHeight = (int) (((double) this.cbWebView.getMeasuredHeight()) * 0.35d);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                this.checkPermissionVisibility = false;
                if (this.SMSOTPClicked) {
                    try {
                        this.cbWebView.loadUrl("javascript:" + this.mJS.getString(getString(C0517R.string.cb_otp)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECEIVE_SMS") == 0) {
                    this.permissionGranted = true;
                    this.mPassword = null;
                    prepareSmsListener();
                    enter_otp(this.enterOtpParams);
                    return;
                }
                this.permissionGranted = false;
                enter_otp(this.enterOtpParams);
                return;
            default:
                return;
        }
    }

    protected void showSlowUserWarning() {
        if (this.activity != null && !this.activity.isFinishing() && !this.isSnoozeWindowVisible) {
            View slowUserWarningLayout = this.activity.getLayoutInflater().inflate(C0517R.layout.cb_layout_snooze_slow_user, null);
            ((TextView) slowUserWarningLayout.findViewById(C0517R.id.snooze_header_txt)).setText(C0517R.string.cb_snooze_slow_user_warning_header);
            TextView slowUserWarningDialogCloseBtn = (TextView) slowUserWarningLayout.findViewById(C0517R.id.text_view_cancel_snooze_window);
            ImageView slowUserWarningDialogGraphics = (ImageView) slowUserWarningLayout.findViewById(C0517R.id.snooze_status_icon);
            slowUserWarningDialogGraphics.setVisibility(0);
            slowUserWarningDialogGraphics.setImageDrawable(cbGetDrawable(this.activity.getApplicationContext(), C0517R.drawable.hourglass));
            if (this.slowUserWarningDialog == null) {
                this.slowUserWarningDialog = new Builder(this.activity).create();
                this.slowUserWarningDialog.setView(slowUserWarningLayout);
                this.slowUserWarningDialog.setCanceledOnTouchOutside(true);
                this.slowUserWarningDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                this.slowUserWarningDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                        if (keyCode == 4 && event.getAction() == 0) {
                            PayUCBLifecycle.this.slowUserWarningDialog.dismiss();
                        }
                        return true;
                    }
                });
                slowUserWarningDialogCloseBtn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PayUCBLifecycle.this.slowUserWarningDialog.dismiss();
                    }
                });
            }
            this.slowUserWarningDialog.show();
            CBActivity cBActivity = (CBActivity) this.activity;
            if (CBActivity.STATE == 1) {
                showSlowUserWarningNotification();
            }
        }
    }

    protected void dismissSlowUserWarning() {
        if (this.slowUserWarningDialog != null) {
            this.slowUserWarningDialog.dismiss();
        }
    }

    private void dismissSlowUserWarningTimerOnTouch() {
        if (this.forwardJourneyForChromeLoaderIsComplete) {
            this.firstTouch = true;
            dismissSlowUserWarningTimer();
        }
    }

    protected void showSlowUserWarningNotification() {
        if (this.activity != null && !this.activity.isFinishing()) {
            Intent intent = new Intent();
        }
    }

    protected void showCbBlankOverlay(int visibility) {
        if (this.cbBlankOverlay != null) {
            this.cbBlankOverlay.setVisibility(visibility);
        }
    }
}
