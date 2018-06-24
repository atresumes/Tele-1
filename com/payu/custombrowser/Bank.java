package com.payu.custombrowser;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.bumptech.glide.load.Key;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.bean.CustomBrowserData;
import com.payu.custombrowser.custombar.CustomProgressBar;
import com.payu.custombrowser.services.SnoozeService;
import com.payu.custombrowser.util.C0533L;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import com.payu.custombrowser.util.MissingParamException;
import com.payu.custombrowser.widgets.SnoozeLoaderView;
import com.payu.custombrowser.widgets.SurePayCancelAsyncTaskHelper;
import com.payu.magicretry.MagicRetryFragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class Bank extends PayUCBLifecycle {
    public static String Version;
    public static String keyAnalytics;
    static String paymentMode;
    static String sdkVersion;
    static String transactionID;
    private static List<String> whiteListedUrls = new ArrayList();
    private ButtonOnclickListener buttonClickListener;
    private int currentLoadingProgress;
    Runnable enterOtpRunnable;
    private boolean firstFinish;
    private boolean isFirstURLLoaded;
    private boolean isMRDataSet;
    private boolean isPageStoppedForcefully;
    private boolean isSnoozeTimerRunning;
    private boolean isTransactionStatusReceived;
    private CountDownTimer mCountDownTimer;
    private boolean mLoadingJS;
    private boolean pageStarted;
    private CountDownTimer payUChromeLoaderDisableTimer;
    private CountDownTimer payUChromeLoaderEnableTimer;
    private boolean saveUserIDCheck;
    private boolean showSnoozeWindow;
    private boolean showToggleCheck;
    public long snoozeClickedTime;
    private View snoozeLayout;
    private SnoozeLoaderView snoozeLoaderView;
    private boolean stopOnlyOnce;
    private boolean visibilitychecked;
    private boolean webpageNotFoundError;

    class C04722 implements Runnable {
        C04722() {
        }

        public void run() {
            if (Bank.this.activity != null && !Bank.this.activity.isFinishing() && !Bank.this.isRemoving() && Bank.this.isAdded()) {
                Bank.this.cbWebPageProgressBar.setVisibility(8);
                Bank.this.lastProgress = 0;
            }
        }
    }

    class C04755 implements Runnable {
        C04755() {
        }

        public void run() {
            if (!Bank.this.isPageStoppedForcefully && !Bank.this.pageStarted && Bank.this.isSnoozeWindowVisible && !Bank.this.backwardJourneyStarted) {
                if (Bank.this.isSnoozeWindowVisible) {
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_AUTOMATICALLY_DISAPPEAR_TIME, CustomBrowserMain.getSystemCurrentTime());
                }
                Bank.this.dismissSnoozeWindow();
            }
        }
    }

    class C04788 implements Runnable {
        C04788() {
        }

        public void run() {
            try {
                Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_populate_user_id)) + "(\"" + Bank.this.cbUtil.getStringSharedPreference(Bank.this.activity.getApplicationContext(), Bank.this.bankName) + "\")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ButtonOnclickListener implements OnClickListener {
        private View view_edit;

        public void setView(View view_ed) {
            this.view_edit = view_ed;
        }

        public void onClick(View v) {
            String str = "";
            if (v instanceof Button) {
                str = ((Button) v).getText().toString();
            } else if (v instanceof TextView) {
                str = ((TextView) v).getText().toString();
            }
            switch (Bank.this.getCode(str.toLowerCase())) {
                case 1:
                case 3:
                    Bank.this.pin_selected_flag = true;
                    Bank.this.approve_flag = Boolean.valueOf(true);
                    Bank.this.maximiseWebviewHeight();
                    Bank.this.frameState = 1;
                    Bank.this.onHelpUnavailable();
                    if (Bank.this.cbSlideBarView != null) {
                        Bank.this.cbSlideBarView.setVisibility(8);
                    }
                    if (Bank.this.cbTransparentView != null) {
                        Bank.this.cbTransparentView.setVisibility(8);
                    }
                    try {
                        Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_pin)));
                        Bank.this.eventRecorded = CBAnalyticsConstant.PASSWORD;
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                case 2:
                    try {
                        Bank.this.eventRecorded = CBAnalyticsConstant.REGENERATE;
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                        Bank.this.mPassword = null;
                        Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_regen_otp)));
                        Bank.this.prepareSmsListener();
                        return;
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                        return;
                    }
                case 4:
                    final View view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.wait_for_otp, null);
                    Bank.this.eventRecorded = CBAnalyticsConstant.ENTER_MANUALLY;
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                    if (Bank.this.chooseActionHeight == 0) {
                        view.measure(-2, -2);
                        Bank.this.chooseActionHeight = view.getMeasuredHeight();
                    }
                    Bank.this.cbBaseView.removeAllViews();
                    Bank.this.cbBaseView.addView(view);
                    if (Bank.this.cbBaseView.isShown()) {
                        Bank.this.frameState = 2;
                    } else {
                        Bank.this.maximiseWebviewHeight();
                    }
                    ImageView im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                    if (Bank.this.drawable != null) {
                        im.setImageDrawable(Bank.this.drawable);
                    }
                    view.findViewById(C0517R.id.waiting).setVisibility(8);
                    final Button approveButton = (Button) view.findViewById(C0517R.id.approve);
                    approveButton.setClickable(false);
                    EditText otpSMS = (EditText) view.findViewById(C0517R.id.otp_sms);
                    Bank.this.showSoftKeyboard(otpSMS);
                    CBUtil.setAlpha(0.3f, approveButton);
                    approveButton.setVisibility(0);
                    otpSMS.setVisibility(0);
                    view.findViewById(C0517R.id.regenerate_layout).setVisibility(8);
                    view.findViewById(C0517R.id.progress).setVisibility(4);
                    Bank.this.showSoftKeyboard(otpSMS);
                    otpSMS.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        }

                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            if (((EditText) view.findViewById(C0517R.id.otp_sms)).getText().toString().length() > 5) {
                                Bank.this.buttonClickListener.setView(view);
                                approveButton.setOnClickListener(Bank.this.buttonClickListener);
                                approveButton.setClickable(true);
                                CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                                return;
                            }
                            approveButton.setClickable(false);
                            CBUtil.setAlpha(0.3f, approveButton);
                            approveButton.setOnClickListener(null);
                        }

                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    Bank.this.updateHeight(view);
                    return;
                case 5:
                    try {
                        Bank.this.hideKeyboardForcefully();
                        Bank.this.mPassword = null;
                        Bank.this.checkLoading = false;
                        Bank.this.approve_flag = Boolean.valueOf(true);
                        Bank.this.onHelpUnavailable();
                        Bank.this.maximiseWebviewHeight();
                        Bank.this.frameState = 1;
                        Bank.this.prepareSmsListener();
                        if (((EditText) this.view_edit.findViewById(C0517R.id.otp_sms)).getText().toString().length() > 5) {
                            Bank.this.eventRecorded = CBAnalyticsConstant.APPROVED_OTP;
                            Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                            Bank.this.addEventAnalytics(CBAnalyticsConstant.APPROVE_BTN_CLICK_TIME, CustomBrowserMain.getSystemCurrentTime());
                            Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_process_otp)) + "(\"" + ((TextView) Bank.this.activity.findViewById(C0517R.id.otp_sms)).getText().toString() + "\")");
                            ((EditText) this.view_edit.findViewById(C0517R.id.otp_sms)).setText("");
                            return;
                        }
                        return;
                    } catch (JSONException e22) {
                        e22.printStackTrace();
                        return;
                    }
                case 6:
                case 7:
                    Bank.this.SMSOTPClicked = true;
                    Bank.this.checkPermission();
                    Bank.this.eventRecorded = CBAnalyticsConstant.OTP;
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                    if (VERSION.SDK_INT < 23) {
                        Bank.this.mPassword = null;
                        Bank.this.prepareSmsListener();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    static {
        Version = BuildConfig.VERSION_NAME;
        Version = BuildConfig.VERSION_NAME;
    }

    public Bank() {
        this.mCountDownTimer = null;
        this.isSnoozeTimerRunning = false;
        this.isPageStoppedForcefully = false;
        this.firstFinish = true;
        this.mLoadingJS = false;
        this.saveUserIDCheck = true;
        this.isMRDataSet = false;
        this.showSnoozeWindow = true;
        this.pageStarted = false;
        this.webpageNotFoundError = false;
        this.buttonClickListener = new ButtonOnclickListener();
        this.showToggleCheck = false;
        this.customProgressBar = new CustomProgressBar();
        this.backwardJourneyUrls = new HashSet();
        this.cbUtil = new CBUtil();
        this.serialExecutor = Executors.newCachedThreadPool();
        this.retryUrls = new HashSet();
    }

    public static boolean isUrlWhiteListed(String currentUrl) {
        if ((currentUrl.contains("https://secure.payu.in") || currentUrl.contains(CBConstant.PAYU_DOMAIN_TEST)) && currentUrl.contains(CBConstant.RESPONSE_BACKWARD)) {
            return true;
        }
        for (String whiteListedUrl : whiteListedUrls) {
            if (currentUrl != null && currentUrl.contains(whiteListedUrl)) {
                return true;
            }
        }
        return false;
    }

    public void setTransactionStatusReceived(boolean transactionStatusReceived) {
        this.isTransactionStatusReceived = transactionStatusReceived;
    }

    public String getCurrentURL() {
        return this.cbWebView.getUrl() != null ? this.cbWebView.getUrl() : "";
    }

    public SnoozeLoaderView getSnoozeLoaderView() {
        return this.snoozeLoaderView;
    }

    public String getPageType() {
        return this.pageType;
    }

    public void reloadWebView(String url, String postParams) {
        this.forwardJourneyForChromeLoaderIsComplete = false;
        this.backwardJourneyStarted = false;
        this.isWebviewReloading = true;
        registerSMSBroadcast();
        this.backwardJourneyStarted = false;
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        if (this.isSnoozeWindowVisible) {
            dismissSnoozeWindow();
        }
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        this.progressDialog = null;
        if (VERSION.SDK_INT == 16 || VERSION.SDK_INT == 17 || VERSION.SDK_INT == 18) {
            this.cbWebView.loadUrl("about:blank");
        }
        addEventAnalytics(CBAnalyticsConstant.SNOOZE_RESUME_URL, url);
        resetAutoSelectOTP();
        this.cbUtil.resetPayuID();
        this.cbWebView.postUrl(url, postParams.getBytes());
    }

    public void killSnoozeService() {
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
    }

    public void reloadWebView() {
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        if (this.isSnoozeWindowVisible) {
            dismissSnoozeWindow();
        }
        registerSMSBroadcast();
        this.isWebviewReloading = true;
        if (this.isSnoozeEnabled) {
            initializeSnoozeTimer();
        }
        if (this.cbWebView.getUrl() != null) {
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_RESUME_URL, this.cbWebView.getUrl());
            if (19 == VERSION.SDK_INT) {
                this.cbWebView.reload();
            } else {
                reloadWVUsingJS();
            }
        }
    }

    public void reloadWebView(String resumeUrl) {
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        if (this.isSnoozeWindowVisible) {
            dismissSnoozeWindow();
        }
        registerSMSBroadcast();
        this.isWebviewReloading = true;
        if (this.isSnoozeEnabled) {
            initializeSnoozeTimer();
        }
        if (this.cbWebView.getUrl() != null) {
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_RESUME_URL, resumeUrl);
            if (19 == VERSION.SDK_INT) {
                this.cbWebView.reload();
                return;
            } else {
                reloadWVUsingJS();
                return;
            }
        }
        reloadWebView(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData());
    }

    public String getBankName() {
        if (this.bankName == null) {
            return "";
        }
        return this.bankName;
    }

    private void checkPermission() {
        boolean z = true;
        if (this.checkedPermission || VERSION.SDK_INT < 23 || !this.merchantSMSPermission) {
            onHelpAvailable();
            if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECEIVE_SMS") != 0) {
                z = false;
            }
            this.permissionGranted = z;
            if (this.SMSOTPClicked) {
                try {
                    this.cbWebView.loadUrl("javascript:" + this.mJS.getString(getString(C0517R.string.cb_otp)));
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            return;
        }
        this.checkedPermission = true;
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECEIVE_SMS") != 0) {
            requestPermissions(new String[]{"android.permission.RECEIVE_SMS"}, 1);
            this.checkPermissionVisibility = true;
            return;
        }
        this.permissionGranted = true;
        if (this.SMSOTPClicked) {
            try {
                this.cbWebView.loadUrl("javascript:" + this.mJS.getString(getString(C0517R.string.cb_otp)));
            } catch (JSONException e3) {
                e3.printStackTrace();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }
    }

    @JavascriptInterface
    public void showCustomBrowser(final boolean showCustomBrowser) {
        this.showCB = showCustomBrowser;
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (!showCustomBrowser) {
                        Bank.this.maximiseWebviewHeight();
                        Bank.this.frameState = 1;
                        try {
                            if (Bank.this.cbSlideBarView != null) {
                                Bank.this.cbSlideBarView.setVisibility(8);
                            }
                            Bank.this.onHelpUnavailable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @JavascriptInterface
    public void setMRData(String data) {
        if (!this.isMRDataSet) {
            MagicRetryFragment.setMRData(data, getActivity().getApplicationContext());
            updateWhitelistedRetryUrls(CBUtil.updateRetryData(data, getActivity().getApplicationContext()));
            this.isMRDataSet = true;
        }
    }

    public void onOverrideURL(String url) {
        if (this.cbWebPageProgressBar != null) {
            this.cbWebPageProgressBar.setProgress(10);
        }
    }

    private void snoozeOnReceivedError() {
        setIsPageStoppedForcefully(true);
        launchSnoozeWindow(2);
    }

    public void onReceivedErrorWebClient(int errorCode, String description) {
        this.webpageNotFoundError = true;
        try {
            if (getActivity() != null && !getActivity().isFinishing() && CustomBrowserData.SINGLETON != null && CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                if (!this.backwardJourneyStarted) {
                    snoozeOnReceivedError();
                } else if (this.backwardJourneyStarted && this.isTxnNBType && this.snoozeCountBackwardJourney < this.customBrowserConfig.getEnableSurePay()) {
                    dismissSnoozeWindow();
                    snoozeOnReceivedError();
                }
                onHelpUnavailable();
                this.cbBaseView.removeAllViews();
                if (this.cbWebPageProgressBar != null) {
                    this.cbWebPageProgressBar.setVisibility(8);
                }
                if (this.maxWebview != 0) {
                    maximiseWebviewHeight();
                    this.frameState = 1;
                }
                hideCB();
                if (!this.cbOldFlow) {
                    CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onCBErrorReceived(errorCode, description);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMagicRetryCB() {
        try {
            this.cbWebView.loadUrl("javascript:" + this.mBankJS.getString("getMagicRetryUrls") + "('" + keyAnalytics + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onProgressChanged(int progress) {
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded() && this.cbWebPageProgressBar != null) {
            this.cbWebPageProgressBar.setVisibility(0);
            if (progress != 100) {
                startAnimation(progress);
            } else if (this.cbWebPageProgressBar != null) {
                this.cbWebPageProgressBar.setProgress(100);
                new Handler().postDelayed(new C04722(), 100);
            }
        }
    }

    @JavascriptInterface
    public void onMerchantHashReceived(final String result) {
        if (getActivity() != null && !getActivity().isFinishing() && !isRemoving() && isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    switch (Bank.this.storeOneClickHash) {
                        case 2:
                            try {
                                JSONObject hashObject = new JSONObject(result);
                                Bank.this.cbUtil.storeInSharedPreferences(Bank.this.getActivity().getApplicationContext(), hashObject.getString(CBAnalyticsConstant.CARD_TOKEN), hashObject.getString(CBAnalyticsConstant.MERCHANT_HASH));
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        default:
                            return;
                    }
                }
            });
        }
    }

    private void initializeSnoozeTimer() {
        if (CustomBrowserData.SINGLETON != null && CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null && this.customBrowserConfig != null && this.cbUtil.getBooleanSharedPreferenceDefaultTrue(CBConstant.SNOOZE_ENABLED, this.activity.getApplicationContext()) && this.customBrowserConfig.getEnableSurePay() > this.snoozeCount) {
            if (this.isSnoozeTimerRunning) {
                stopSnoozeCountDownTimer();
            }
            startSnoozeCountDownTimer();
        }
    }

    public void onPageStartedWebclient(String url) {
        this.pageStarted = true;
        this.isPageStoppedForcefully = false;
        if ((VERSION.SDK_INT == 16 || VERSION.SDK_INT == 17 || VERSION.SDK_INT == 18) && this.webpageNotFoundError) {
            jellyBeanOnReceivedError();
        }
        this.webpageNotFoundError = false;
        dismissSlowUserWarning();
        if (!(this.payuPG || url == null || (!url.equalsIgnoreCase("https://secure.payu.in/_payment") && !url.equalsIgnoreCase(CBConstant.PRODUCTION_PAYMENT_URL_SEAMLESS)))) {
            this.payuPG = true;
        }
        if (!this.isFirstURLLoaded) {
            if (this.customBrowserConfig != null && this.customBrowserConfig.getPayuPostData() == null && this.customBrowserConfig.getPostURL() == null) {
                if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().getPostData() == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().getPostURL() == null) {
                    throw new MissingParamException("POST Data or POST URL Missing or wrong POST URL");
                }
                this.customBrowserConfig.setPayuPostData(CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().getPostData());
                this.customBrowserConfig.setPostURL(CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().getPostURL());
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().setPostURL(null);
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().setPostData(null);
            }
            if (this.customBrowserConfig != null) {
                this.isTxnNBType = checkIfTransactionNBType(this.customBrowserConfig.getPayuPostData());
            }
            this.isFirstURLLoaded = true;
        }
        this.showSnoozeWindow = true;
        if (!(this.pageType == null || this.pageType.equalsIgnoreCase(""))) {
            this.timeOfDeparture = CustomBrowserMain.getSystemCurrentTime();
            addEventAnalytics(CBAnalyticsConstant.DEPARTURE, this.timeOfDeparture);
            this.pageType = "";
        }
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded()) {
            String str;
            this.cbUtil.setStringSharedPreferenceLastURL(this.activity.getApplicationContext(), CBAnalyticsConstant.LAST_URL, "s:" + url);
            if (this.cbWebPageProgressBar != null) {
                this.cbWebPageProgressBar.setVisibility(0);
            }
            if (this.payUChromeLoaderDisableTimer != null) {
                this.payUChromeLoaderDisableTimer.cancel();
            }
            if (this.cbWebPageProgressBar != null) {
                this.cbWebPageProgressBar.setVisibility(0);
                this.cbWebPageProgressBar.setProgress(10);
            }
            this.backwardJourneyStarted = isInBackWardJourney(url);
            if (!this.forwardJourneyForChromeLoaderIsComplete || this.backwardJourneyStarted) {
                progressBarVisibilityPayuChrome(0, url);
            }
            if (this.cbWebView.getUrl() == null || this.cbWebView.getUrl().equalsIgnoreCase("")) {
                str = url;
            } else {
                str = this.cbWebView.getUrl();
            }
            this.webviewUrl = str;
            if (CustomBrowserData.SINGLETON != null && CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                if (this.backwardJourneyStarted) {
                    if (this.isTxnNBType) {
                        this.isSnoozeWindowVisible = false;
                    } else {
                        dismissSnoozeWindow();
                    }
                }
                if (url.contains(CBConstant.PAYMENT_OPTION_URL_PROD)) {
                    this.mJS = null;
                }
                try {
                    if ((!this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData(), CBConstant.SURL).equals("") && url.contains(URLDecoder.decode(this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData(), CBConstant.SURL), Key.STRING_CHARSET_NAME))) || ((!this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData(), CBConstant.FURL).equals("") && url.contains(URLDecoder.decode(this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData(), CBConstant.FURL), Key.STRING_CHARSET_NAME))) || isRetryURL(url))) {
                        this.showSnoozeWindow = false;
                        dismissSnoozeWindow();
                        hideCB();
                        if (isRetryURL(url)) {
                            resetAutoSelectOTP();
                            this.backwardJourneyStarted = false;
                        }
                        stopSnoozeCountDownTimer();
                        if (this.snoozeService != null) {
                            this.snoozeService.killSnoozeService();
                        }
                    } else if (this.isSnoozeEnabled && this.customBrowserConfig.getSurePayMode() == 1 && !this.backwardJourneyStarted) {
                        this.snoozeLoadPercentageAndTimeOut = this.snoozeConfigMap.getPercentageAndTimeout(url);
                        this.snoozeUrlLoadingPercentage = this.snoozeLoadPercentageAndTimeOut[0];
                        this.snoozeUrlLoadingTimeout = this.snoozeLoadPercentageAndTimeOut[1];
                        initializeSnoozeTimer();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopWIFI() {
    }

    private void jellyBeanOnReceivedError() {
        setIsPageStoppedForcefully(true);
    }

    public boolean isInBackWardJourney(String url) {
        try {
            if (!this.backwardJourneyStarted) {
                if ((url.contains("https://secure.payu.in") || url.contains(CBConstant.PAYU_DOMAIN_TEST)) && url.contains(CBConstant.RESPONSE_BACKWARD)) {
                    return true;
                }
                if (this.backwardJourneyUrls != null) {
                    for (String backwardJourneyUrl : this.backwardJourneyUrls) {
                        if (url.contains(backwardJourneyUrl)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return this.backwardJourneyStarted;
        } catch (Exception e) {
            e.printStackTrace();
            return this.backwardJourneyStarted;
        }
    }

    public void onLoadResourse(WebView view, String url) {
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded() && !url.equalsIgnoreCase(CBConstant.rupeeURL) && !url.contains(CBConstant.rupeeURL1) && !url.contains(CBConstant.rupeeURL2)) {
        }
    }

    public void onPageFinishWebclient(String url) {
        this.pageStarted = false;
        if (!(this.activity == null || this.activity.isFinishing() || isRemoving() || !isAdded())) {
            this.cbUtil.setStringSharedPreferenceLastURL(this.activity.getApplicationContext(), CBAnalyticsConstant.LAST_URL, "f:" + url);
            startPayUChromeLoaderDisbaleTimer();
            if (!(!this.firstFinish || getArguments() == null || getArguments().getInt(CBConstant.MAIN_LAYOUT, -1) == -1)) {
                try {
                    final View activityRootView = this.activity.findViewById(getArguments().getInt(CBConstant.MAIN_LAYOUT));
                    activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        private final int DefaultKeyboardDP = 100;
                        private final int EstimatedKeyboardDP;
                        private final Rect f924r;

                        public void onGlobalLayout() {
                            if (Bank.this.activity != null && !Bank.this.activity.isFinishing() && !Bank.this.isRemoving() && Bank.this.isAdded()) {
                                boolean isShown;
                                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(1, (float) this.EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());
                                activityRootView.getWindowVisibleDisplayFrame(this.f924r);
                                if (activityRootView.getRootView().getHeight() - (this.f924r.bottom - this.f924r.top) >= estimatedKeyboardHeight) {
                                    isShown = true;
                                } else {
                                    isShown = false;
                                }
                                if (isShown && Bank.this.checkForInput == 0) {
                                    ((InputMethodManager) Bank.this.activity.getSystemService("input_method")).toggleSoftInput(3, 0);
                                    Bank.this.checkForInput = 1;
                                }
                            }
                        }
                    });
                    this.firstFinish = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!this.isPageStoppedForcefully) {
            stopSnoozeCountDownTimer();
        }
        new Handler().postDelayed(new C04755(), 1000);
    }

    @JavascriptInterface
    public void setSnoozeEnabled(boolean snoozeEnabled) {
        if (!snoozeEnabled) {
            this.customBrowserConfig.setEnableSurePay(0);
        }
        this.cbUtil.setBooleanSharedPreference(CBConstant.SNOOZE_ENABLED, snoozeEnabled, this.activity.getApplicationContext());
    }

    private void startPayUChromeLoaderDisbaleTimer() {
        if (this.payUChromeLoaderDisableTimer != null) {
            this.payUChromeLoaderDisableTimer.cancel();
        }
        if (this.payUChromeLoaderEnableTimer != null) {
            this.payUChromeLoaderEnableTimer.cancel();
        }
        this.payUChromeLoaderDisableTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Bank.this.dismissPayULoader();
            }
        }.start();
    }

    private void startPayUChromeLoaderEnableTimer() {
        if (this.payUChromeLoaderEnableTimer != null) {
            this.payUChromeLoaderEnableTimer.cancel();
        }
        this.payUChromeLoaderEnableTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Bank.this.progressBarVisibilityPayuChrome(0, "");
                Bank.this.forwardJourneyForChromeLoaderIsComplete = true;
            }
        }.start();
    }

    @JavascriptInterface
    public void getUserId() {
        if (this.activity != null && !this.activity.isFinishing()) {
            this.activity.runOnUiThread(new C04788());
        }
    }

    @JavascriptInterface
    public void setUserId(String params) {
        if (!this.saveUserIDCheck) {
            String savedParam = this.cbUtil.getStringSharedPreference(this.activity.getApplicationContext(), this.bankName);
            if (!savedParam.equals("") && savedParam.equals(params)) {
                this.cbUtil.removeFromSharedPreferences(this.activity.getApplicationContext(), this.bankName);
            }
        } else if (this.activity != null && !this.activity.isFinishing()) {
            this.cbUtil.storeInSharedPreferences(this.activity.getApplicationContext(), this.bankName, params);
        }
    }

    @JavascriptInterface
    public void nativeHelperForNB(final String fields, final String params) {
        if (!(this.activity == null || this.activity.isFinishing())) {
            this.activity.runOnUiThread(new Runnable() {

                class C04824 implements OnClickListener {
                    C04824() {
                    }

                    public void onClick(View v) {
                        try {
                            Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_btn_action)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                public void run() {
                    try {
                        if (Bank.this.isSnoozeWindowVisible) {
                            Bank.this.dismissSnoozeWindow();
                            Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_WINDOW_DISMISSED_BY_CB);
                            Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_AUTOMATICALLY_DISAPPEAR_TIME, CustomBrowserMain.getSystemCurrentTime());
                        }
                        Bank.this.pageType = CBAnalyticsConstant.NBLOGIN_PAGE;
                        Bank.this.timeOfArrival = CustomBrowserMain.getSystemCurrentTime();
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.ARRIVAL, Bank.this.timeOfArrival);
                        Bank.this.onHelpAvailable();
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.CB_STATUS, CBAnalyticsConstant.NB_CUSTOM_BROWSER);
                        if (fields != null && Bank.this.activity != null) {
                            Bank.this.dismissSnoozeWindow();
                            View nbView = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.nb_layout, null);
                            final Button bContinue = (Button) nbView.findViewById(C0517R.id.b_continue);
                            final CheckBox checkBox = (CheckBox) nbView.findViewById(C0517R.id.checkbox);
                            JSONObject jsonObject = new JSONObject(params);
                            String bText = Bank.this.getString(C0517R.string.cb_btn_text);
                            if (!jsonObject.has(bText) || jsonObject.getString(bText) == null || jsonObject.getString(bText).equalsIgnoreCase("")) {
                                Bank.this.onHelpUnavailable();
                                Bank.this.cbBaseView.removeAllViews();
                            } else if (fields.equals(Bank.this.getString(C0517R.string.cb_button))) {
                                if (!jsonObject.has(Bank.this.getString(C0517R.string.cb_checkbox))) {
                                    checkBox.setVisibility(8);
                                } else if (jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_checkbox))) {
                                    if (Bank.this.saveUserIDCheck) {
                                        Bank.this.addEventAnalytics(CBAnalyticsConstant.INITIAL_USER_NAME_CHECKBOX_STATUS, "y");
                                        checkBox.setChecked(true);
                                    } else {
                                        Bank.this.addEventAnalytics(CBAnalyticsConstant.INITIAL_USER_NAME_CHECKBOX_STATUS, "n");
                                        checkBox.setChecked(false);
                                    }
                                    checkBox.setOnClickListener(new OnClickListener() {
                                        public void onClick(View v) {
                                            Bank.this.saveUserIDCheck = checkBox.isChecked();
                                            if (Bank.this.saveUserIDCheck) {
                                                Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.USER_NAME_CHECKBOX_STATUS + "y");
                                            } else {
                                                Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.USER_NAME_CHECKBOX_STATUS + "n");
                                            }
                                        }
                                    });
                                    checkBox.setVisibility(0);
                                } else {
                                    checkBox.setVisibility(8);
                                }
                                bContinue.setText(jsonObject.getString(bText));
                                bContinue.setTransformationMethod(null);
                                bContinue.setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        try {
                                            Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.NB_BUTTON_CLICK + bContinue.getText());
                                            Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_btn_action)));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Bank.this.cbBaseView.removeAllViews();
                                Bank.this.cbBaseView.addView(nbView);
                                Bank.this.nbhelpVisible = true;
                            } else if (fields.equals(Bank.this.getString(C0517R.string.cb_pwd_btn))) {
                                bContinue.setText(jsonObject.getString(bText));
                                if (Bank.this.showToggleCheck) {
                                    checkBox.setChecked(true);
                                } else {
                                    checkBox.setChecked(false);
                                }
                                if (checkBox.isChecked()) {
                                    try {
                                        Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_toggle_field)) + "(\"" + true + "\")");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                checkBox.setText(Bank.this.getString(C0517R.string.cb_show_password));
                                checkBox.setVisibility(0);
                                checkBox.setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        Bank.this.showToggleCheck = checkBox.isChecked();
                                        if (checkBox.isChecked()) {
                                            try {
                                                Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_toggle_field)) + "(\"" + true + "\")");
                                                return;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                return;
                                            }
                                        }
                                        try {
                                            Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_toggle_field)) + "(\"" + false + "\")");
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                });
                                bContinue.setOnClickListener(new C04824());
                                Bank.this.nbhelpVisible = true;
                                Bank.this.cbBaseView.removeAllViews();
                                Bank.this.cbBaseView.addView(nbView);
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                Bank.this.dismissPayULoader();
            }
        });
    }

    @JavascriptInterface
    public void reInit() {
        if (this.activity != null && !this.activity.isFinishing()) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    Bank.this.onPageFinished();
                }
            });
        }
    }

    @JavascriptInterface
    public void bankFound(final String bank) {
        if (!this.visibilitychecked) {
            checkVisibilityCB(bank);
            this.visibilitychecked = true;
        }
        cbSetBankDrawable(bank);
        CBUtil.setVariableReflection(CBConstant.MAGIC_RETRY_PAKAGE, bank, CBConstant.BANKNAME);
        if (!(this.activity == null || this.activity.isFinishing())) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    Bank.this.calculateMaximumWebViewHeight();
                }
            });
        }
        this.bankName = bank;
        if (!this.mPageReady) {
            try {
                if (this.loadingLayout != null) {
                    this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Bank.this.customProgressBar.removeProgressDialog(Bank.this.loadingLayout.findViewById(C0517R.id.progress));
                        }
                    });
                }
                if (!this.isPageStoppedForcefully) {
                    if (this.loadingLayout == null) {
                        convertToNative(CBConstant.LOADING, "{}");
                    } else if (!(this.activity == null || this.loadingLayout == ((ViewGroup) this.activity.findViewById(C0517R.id.help_view)).getChildAt(0))) {
                        convertToNative(CBConstant.LOADING, "{}");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!this.mLoadingJS && this.mJS == null) {
            this.serialExecutor.execute(new Runnable() {

                class C04681 implements Runnable {
                    C04681() {
                    }

                    public void run() {
                        Bank.this.onPageFinished();
                    }
                }

                public void run() {
                    Exception e;
                    Bank.this.mLoadingJS = true;
                    String fileName;
                    try {
                        if (Bank.this.activity != null) {
                            fileName = Bank.this.mBankJS.getString(bank);
                            if (!new File(Bank.this.activity.getFilesDir(), fileName).exists()) {
                                HttpURLConnection conn = (HttpURLConnection) new URL(CBConstant.PRODUCTION_URL + fileName + ".js").openConnection();
                                conn.setRequestMethod("GET");
                                conn.setRequestProperty("Accept-Charset", Key.STRING_CHARSET_NAME);
                                if (conn.getResponseCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                                    Bank.this.cbUtil.writeFileOutputStream(conn.getInputStream(), Bank.this.activity, fileName, 0);
                                }
                            }
                        }
                        try {
                            if (Bank.this.activity != null) {
                                fileName = Bank.this.mBankJS.getString(bank);
                                Bank.this.mJS = new JSONObject(CBUtil.decodeContents(Bank.this.activity.openFileInput(fileName)));
                                if (Bank.this.mPageReady && Bank.this.activity != null) {
                                    Bank.this.activity.runOnUiThread(new C04681());
                                }
                                Bank.this.mLoadingJS = false;
                            }
                        } catch (JSONException e2) {
                            e = e2;
                            e.printStackTrace();
                        } catch (FileNotFoundException e3) {
                            e = e3;
                            e.printStackTrace();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    } catch (Exception e42) {
                        e42.printStackTrace();
                        try {
                            if (Bank.this.activity != null) {
                                fileName = Bank.this.mBankJS.getString(bank);
                                Bank.this.mJS = new JSONObject(CBUtil.decodeContents(Bank.this.activity.openFileInput(fileName)));
                                if (Bank.this.mPageReady && Bank.this.activity != null) {
                                    Bank.this.activity.runOnUiThread(new C04681());
                                }
                                Bank.this.mLoadingJS = false;
                            }
                        } catch (JSONException e5) {
                            e42 = e5;
                            e42.printStackTrace();
                        } catch (FileNotFoundException e6) {
                            e42 = e6;
                            e42.printStackTrace();
                        } catch (Exception e422) {
                            e422.printStackTrace();
                        }
                    } catch (Throwable th) {
                        try {
                            if (Bank.this.activity != null) {
                                fileName = Bank.this.mBankJS.getString(bank);
                                Bank.this.mJS = new JSONObject(CBUtil.decodeContents(Bank.this.activity.openFileInput(fileName)));
                                if (Bank.this.mPageReady && Bank.this.activity != null) {
                                    Bank.this.activity.runOnUiThread(new C04681());
                                }
                                Bank.this.mLoadingJS = false;
                            }
                        } catch (JSONException e7) {
                            e422 = e7;
                            e422.printStackTrace();
                        } catch (FileNotFoundException e8) {
                            e422 = e8;
                            e422.printStackTrace();
                        } catch (Exception e4222) {
                            e4222.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @JavascriptInterface
    public void convertToNative(final String fields, final String params) {
        if (this.isSnoozeWindowVisible) {
            dismissSnoozeWindow();
            killSnoozeService();
            cancelTransactionNotification();
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_WINDOW_DISMISSED_BY_CB);
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_AUTOMATICALLY_DISAPPEAR_TIME, CustomBrowserMain.getSystemCurrentTime());
        }
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                Bank.this.dismissPayULoader();
            }
        });
        if (!(this.pageType == null || this.pageType.equalsIgnoreCase(""))) {
            addEventAnalytics(CBAnalyticsConstant.DEPARTURE, CustomBrowserMain.getSystemCurrentTime());
            this.pageType = "";
        }
        if (this.activity != null && this.showCB) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (Bank.this.loadingLayout != null) {
                        Bank.this.customProgressBar.removeProgressDialog(Bank.this.loadingLayout.findViewById(C0517R.id.progress));
                    }
                    if (Bank.this.enterOTPView != null) {
                        Bank.this.customProgressBar.removeProgressDialog(Bank.this.enterOTPView.findViewById(C0517R.id.progress));
                    }
                    try {
                        if (!(Bank.this.waitingOTPTimer == null || Bank.this.enterOtpRunnable == null)) {
                            Bank.this.cbUtil.cancelTimer(Bank.this.waitingOTPTimer);
                        }
                        if (fields.equals(Bank.this.getString(C0517R.string.cb_error))) {
                            Bank.this.onBankError();
                        } else if (fields.equals("parse error")) {
                            Bank.this.onBankError();
                        } else if (fields.contentEquals(CBConstant.LOADING) && !Bank.this.pin_selected_flag && Bank.this.checkLoading) {
                            Bank.this.onHelpAvailable();
                            if (Bank.this.cbTransparentView != null) {
                                Bank.this.cbTransparentView.setVisibility(0);
                            }
                            if (Bank.this.loadingLayout == null) {
                                Bank.this.loadingLayout = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.loading, null);
                            }
                            im = (ImageView) Bank.this.loadingLayout.findViewById(C0517R.id.bank_logo);
                            if (Bank.this.drawable != null) {
                                im.setImageDrawable(Bank.this.drawable);
                            }
                            LayoutParams params = new LayoutParams(-1, Bank.this.chooseActionHeight);
                            View layoutView = Bank.this.loadingLayout.findViewById(C0517R.id.loading);
                            layoutView.setLayoutParams(params);
                            layoutView.requestLayout();
                            layoutView.invalidate();
                            Bank.this.customProgressBar.showDialog(Bank.this.loadingLayout.findViewById(C0517R.id.progress));
                            Bank.this.cbBaseView.removeAllViews();
                            Bank.this.cbBaseView.addView(Bank.this.loadingLayout);
                            if (Bank.this.cbBaseView.isShown()) {
                                Bank.this.frameState = 2;
                            } else {
                                Bank.this.maximiseWebviewHeight();
                            }
                            Bank.this.updateHeight(Bank.this.loadingLayout);
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_choose))) {
                            Bank.this.addCustomBrowserEventAnalytics();
                            Bank.this.frameState = 2;
                            Bank.this.checkLoading = true;
                            if (Bank.this.cbTransparentView != null) {
                                Bank.this.cbTransparentView.setVisibility(0);
                            }
                            view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.choose_action, null);
                            if (Bank.this.maxWebview == 0) {
                                Bank.this.calculateMaximumWebViewHeight();
                                Bank.this.maximiseWebviewHeight();
                            }
                            Bank.this.cbBaseView.setVisibility(0);
                            if (Bank.this.cbSlideBarView != null) {
                                Bank.this.cbSlideBarView.setVisibility(8);
                            }
                            Bank.this.calculateCBHeight(view);
                            Bank.this.onHelpAvailable();
                            view.measure(-2, -2);
                            Bank.this.chooseActionHeight = view.getMeasuredHeight();
                            im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                            if (Bank.this.drawable != null) {
                                im.setImageDrawable(Bank.this.drawable);
                            }
                            Bank.this.cbBaseView.removeAllViews();
                            Bank.this.cbBaseView.addView(view);
                            if (Bank.this.cbBaseView.isShown()) {
                                Bank.this.frameState = 2;
                            }
                            SpannableStringBuilder str = new SpannableStringBuilder("Select an option for Faster payment");
                            str.setSpan(new StyleSpan(1), 21, 27, 33);
                            ((TextView) view.findViewById(C0517R.id.choose_text)).setText(str);
                            try {
                                jsonObject = new JSONObject(params);
                                if ((jsonObject.has(Bank.this.getString(C0517R.string.cb_otp)) && jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_otp))) || (jsonObject.has(Bank.this.getString(C0517R.string.cb_pin)) && jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_pin)))) {
                                    Bank.this.pageType = CBAnalyticsConstant.CHOOSE_SCREEN;
                                } else {
                                    Bank.this.pageType = "";
                                }
                                if (!jsonObject.has(Bank.this.getString(C0517R.string.cb_otp)) || jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_otp))) {
                                    view.findViewById(C0517R.id.otp).setOnClickListener(Bank.this.buttonClickListener);
                                    if (Bank.this.autoSelectOtp) {
                                        Bank.this.eventRecorded = CBAnalyticsConstant.CB_AUTO_OTP_SELECT;
                                        Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                                        view.findViewById(C0517R.id.otp).performClick();
                                        Bank.this.autoSelectOtp = false;
                                    }
                                } else {
                                    view.findViewById(C0517R.id.otp).setVisibility(8);
                                    view.findViewById(C0517R.id.view).setVisibility(8);
                                }
                                view.findViewById(C0517R.id.otp).setOnClickListener(Bank.this.buttonClickListener);
                                if (!jsonObject.has(Bank.this.getString(C0517R.string.cb_pin)) || jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_pin))) {
                                    view.findViewById(C0517R.id.pin).setOnClickListener(new OnClickListener() {

                                        class C04691 implements OnClickListener {
                                            C04691() {
                                            }

                                            public void onClick(View view) {
                                                try {
                                                    Bank.this.eventRecorded = CBAnalyticsConstant.PASSWORD;
                                                    Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                                                    Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_pin)));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        public void onClick(View view) {
                                            Bank.this.pin_selected_flag = true;
                                            Bank.this.approve_flag = Boolean.valueOf(true);
                                            Bank.this.maximiseWebviewHeight();
                                            Bank.this.frameState = 1;
                                            if (Bank.this.cbTransparentView != null) {
                                                Bank.this.cbTransparentView.setVisibility(8);
                                            }
                                            try {
                                                if (jsonObject.has(Bank.this.getString(C0517R.string.cb_register)) && jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_register))) {
                                                    view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.register_pin, null);
                                                    Bank.this.cbBaseView.removeAllViews();
                                                    Bank.this.cbBaseView.addView(view);
                                                    if (Bank.this.cbBaseView.isShown()) {
                                                        Bank.this.frameState = 2;
                                                    }
                                                    view.findViewById(C0517R.id.pin).setOnClickListener(new C04691());
                                                    if (jsonObject.has(Bank.this.getString(C0517R.string.cb_otp)) && !jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_otp))) {
                                                        view.findViewById(C0517R.id.otp).setVisibility(8);
                                                    }
                                                    view.findViewById(C0517R.id.otp).setOnClickListener(Bank.this.buttonClickListener);
                                                    Bank.this.updateHeight(view);
                                                }
                                                Bank.this.eventRecorded = CBAnalyticsConstant.PASSWORD;
                                                Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, Bank.this.eventRecorded);
                                                Bank.this.onHelpUnavailable();
                                                Bank.this.cbWebView.loadUrl("javascript:" + Bank.this.mJS.getString(Bank.this.getString(C0517R.string.cb_pin)));
                                                Bank.this.updateHeight(view);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else {
                                    view.findViewById(C0517R.id.pin).setVisibility(8);
                                    view.findViewById(C0517R.id.view).setVisibility(8);
                                }
                                if (jsonObject.has(Bank.this.getString(C0517R.string.cb_error))) {
                                    view.findViewById(C0517R.id.error_message).setVisibility(0);
                                    ((TextView) view.findViewById(C0517R.id.error_message)).setText(jsonObject.getString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_incorrect_OTP_2))) {
                            Bank.this.pageType = CBAnalyticsConstant.OTP_PAGE;
                            Bank.this.addCustomBrowserEventAnalytics();
                            Bank.this.checkLoading = true;
                            Bank.this.onHelpAvailable();
                            view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.retry_otp, null);
                            im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                            if (Bank.this.drawable != null) {
                                im.setImageDrawable(Bank.this.drawable);
                            }
                            Bank.this.cbBaseView.removeAllViews();
                            Bank.this.cbBaseView.addView(view);
                            if (Bank.this.cbBaseView.isShown()) {
                                Bank.this.frameState = 2;
                            } else {
                                if (Bank.this.cbSlideBarView != null) {
                                    Bank.this.cbSlideBarView.setVisibility(0);
                                }
                                Bank.this.maximiseWebviewHeight();
                            }
                            if (Bank.this.mPassword == null) {
                                view.findViewById(C0517R.id.regenerate_layout).setVisibility(0);
                                view.findViewById(C0517R.id.Regenerate_layout_gone).setVisibility(8);
                                try {
                                    p = new JSONObject(params);
                                    pin = p.has(Bank.this.getString(C0517R.string.cb_pin)) && p.getBoolean(Bank.this.getString(C0517R.string.cb_pin));
                                    view.findViewById(C0517R.id.enter_manually).setOnClickListener(Bank.this.buttonClickListener);
                                    if (pin) {
                                        view.findViewById(C0517R.id.pin_layout_gone).setVisibility(0);
                                    } else {
                                        view.findViewById(C0517R.id.pin_layout_gone).setVisibility(8);
                                    }
                                    view.findViewById(C0517R.id.pin).setOnClickListener(Bank.this.buttonClickListener);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                            Bank.this.updateHeight(view);
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_retry_otp))) {
                            Bank.this.pageType = CBAnalyticsConstant.OTP_PAGE;
                            Bank.this.addCustomBrowserEventAnalytics();
                            Bank.this.checkLoading = true;
                            Bank.this.onHelpAvailable();
                            Bank.this.hideSoftKeyboard();
                            if (Bank.this.cbTransparentView != null) {
                                Bank.this.cbTransparentView.setVisibility(0);
                            }
                            view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.retry_otp, null);
                            im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                            if (Bank.this.drawable != null) {
                                im.setImageDrawable(Bank.this.drawable);
                            }
                            Bank.this.cbBaseView.removeAllViews();
                            Bank.this.cbBaseView.addView(view);
                            if (Bank.this.cbBaseView.isShown()) {
                                Bank.this.frameState = 2;
                            } else {
                                if (Bank.this.cbSlideBarView != null) {
                                    Bank.this.cbSlideBarView.setVisibility(0);
                                }
                                Bank.this.maximiseWebviewHeight();
                            }
                            try {
                                if (Bank.this.mPassword == null) {
                                    p = new JSONObject(params);
                                    boolean regenerate = p.has(Bank.this.getString(C0517R.string.cb_regenerate)) && p.getBoolean(Bank.this.getString(C0517R.string.cb_regenerate));
                                    pin = p.has(Bank.this.getString(C0517R.string.cb_pin)) && p.getBoolean(Bank.this.getString(C0517R.string.cb_pin));
                                    view.findViewById(C0517R.id.regenerate_layout).setVisibility(0);
                                    if (regenerate) {
                                        view.findViewById(C0517R.id.Regenerate_layout_gone).setVisibility(0);
                                        if (pin) {
                                            view.findViewById(C0517R.id.Enter_manually_gone).setVisibility(8);
                                            view.findViewById(C0517R.id.pin_layout_gone).setVisibility(0);
                                        } else {
                                            view.findViewById(C0517R.id.Enter_manually_gone).setVisibility(0);
                                            view.findViewById(C0517R.id.pin_layout_gone).setVisibility(8);
                                        }
                                    } else {
                                        if (pin) {
                                            view.findViewById(C0517R.id.pin_layout_gone).setVisibility(0);
                                        } else {
                                            view.findViewById(C0517R.id.pin_layout_gone).setVisibility(8);
                                        }
                                        view.findViewById(C0517R.id.Regenerate_layout_gone).setVisibility(8);
                                        view.findViewById(C0517R.id.Enter_manually_gone).setVisibility(0);
                                    }
                                }
                                view.findViewById(C0517R.id.pin).setOnClickListener(Bank.this.buttonClickListener);
                                view.findViewById(C0517R.id.enter_manually).setOnClickListener(Bank.this.buttonClickListener);
                                view.findViewById(C0517R.id.retry).setOnClickListener(Bank.this.buttonClickListener);
                                Bank.this.buttonClickListener.setView(view);
                                view.findViewById(C0517R.id.approve).setOnClickListener(Bank.this.buttonClickListener);
                            } catch (Exception e22) {
                                e22.printStackTrace();
                            }
                            Bank.this.updateHeight(view);
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_enter_pin))) {
                            Bank.this.pageType = CBAnalyticsConstant.PIN_PAGE;
                            Bank.this.addCustomBrowserEventAnalytics();
                            if (Bank.this.cbSlideBarView != null) {
                                Bank.this.cbSlideBarView.setVisibility(8);
                            }
                            Bank.this.onHelpUnavailable();
                            Bank.this.pin_selected_flag = true;
                            Bank.this.approve_flag = Boolean.valueOf(true);
                            Bank.this.maximiseWebviewHeight();
                            Bank.this.frameState = 1;
                            if (Bank.this.cbTransparentView != null) {
                                Bank.this.cbTransparentView.setVisibility(8);
                            }
                            Bank.this.maximiseWebviewHeight();
                            Bank.this.cbBaseView.removeAllViews();
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_enter_otp))) {
                            Bank.this.pageType = CBAnalyticsConstant.OTP_PAGE;
                            Bank.this.SMSOTPClicked = false;
                            Bank.this.checkPermission();
                            Bank.this.enterOtpParams = params;
                            if (!Bank.this.checkPermissionVisibility) {
                                Bank.this.addCustomBrowserEventAnalytics();
                                Bank.this.enter_otp(params);
                            }
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_incorrect_pin))) {
                            Bank.this.pageType = CBAnalyticsConstant.CHOOSE_SCREEN;
                            Bank.this.addCustomBrowserEventAnalytics();
                            try {
                                jsonObject = new JSONObject(params);
                                if (jsonObject.has(Bank.this.getString(C0517R.string.cb_otp)) && jsonObject.getBoolean(Bank.this.getString(C0517R.string.cb_otp))) {
                                    Bank.this.checkLoading = true;
                                    Bank.this.onHelpAvailable();
                                    view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.choose_action, null);
                                    im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                                    if (Bank.this.drawable != null) {
                                        im.setImageDrawable(Bank.this.drawable);
                                    }
                                    TextView errorText = (TextView) view.findViewById(C0517R.id.error_message);
                                    errorText.setVisibility(0);
                                    errorText.setText(Bank.this.activity.getResources().getString(C0517R.string.cb_incorrect_password));
                                    TextView chooseText = (TextView) view.findViewById(C0517R.id.choose_text);
                                    chooseText.setVisibility(0);
                                    chooseText.setText(Bank.this.activity.getResources().getString(C0517R.string.cb_retry));
                                    Bank.this.cbBaseView.removeAllViews();
                                    Bank.this.cbBaseView.addView(view);
                                    view.findViewById(C0517R.id.otp).setOnClickListener(Bank.this.buttonClickListener);
                                    view.findViewById(C0517R.id.pin).setOnClickListener(Bank.this.buttonClickListener);
                                    Bank.this.updateHeight(view);
                                    if (Bank.this.cbBaseView.isShown()) {
                                        Bank.this.frameState = 2;
                                    } else {
                                        Bank.this.maximiseWebviewHeight();
                                    }
                                }
                            } catch (Exception e222) {
                                e222.printStackTrace();
                            }
                        } else if (fields.equals(Bank.this.getString(C0517R.string.cb_register_option))) {
                            Bank.this.pageType = CBAnalyticsConstant.REGISTER_PAGE;
                            Bank.this.addCustomBrowserEventAnalytics();
                            Bank.this.onHelpAvailable();
                            view = Bank.this.activity.getLayoutInflater().inflate(C0517R.layout.register, null);
                            Bank.this.cbBaseView.removeAllViews();
                            Bank.this.cbBaseView.addView(view);
                            im = (ImageView) view.findViewById(C0517R.id.bank_logo);
                            if (Bank.this.drawable != null) {
                                im.setImageDrawable(Bank.this.drawable);
                            }
                            Bank.this.updateHeight(view);
                            if (Bank.this.cbBaseView.isShown()) {
                                Bank.this.frameState = 2;
                            } else {
                                Bank.this.maximiseWebviewHeight();
                            }
                        } else {
                            Bank.this.maximiseWebviewHeight();
                            Bank.this.frameState = 1;
                            if (Bank.this.cbSlideBarView != null) {
                                Bank.this.cbSlideBarView.setVisibility(8);
                            }
                            Bank.this.onHelpUnavailable();
                        }
                    } catch (Exception e2222) {
                        e2222.printStackTrace();
                    }
                    if (Bank.this.pageType != null && !Bank.this.pageType.equalsIgnoreCase("")) {
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.ARRIVAL, CustomBrowserMain.getSystemCurrentTime());
                    }
                }
            });
        }
    }

    void addCustomBrowserEventAnalytics() {
        if (!this.eventArray.contains(CBAnalyticsConstant.CUSTOM_BROWSER)) {
            this.eventRecorded = CBAnalyticsConstant.CUSTOM_BROWSER;
            this.eventArray.add(CBAnalyticsConstant.CUSTOM_BROWSER);
            addEventAnalytics(CBAnalyticsConstant.CB_STATUS, this.eventRecorded);
        }
    }

    public void onPageFinished() {
        if (isAdded() && !isRemoving() && this.activity != null) {
            this.mPageReady = true;
            if (this.approve_flag.booleanValue()) {
                onHelpUnavailable();
                this.approve_flag = Boolean.valueOf(false);
            }
            if (this.loadingLayout != null && this.loadingLayout.isShown()) {
                this.frameState = 1;
                maximiseWebviewHeight();
                onHelpUnavailable();
            }
            this.activity.getWindow().setSoftInputMode(3);
            if (!(this.mJS == null || !this.showCB || this.isPageStoppedForcefully)) {
                try {
                    this.cbWebView.loadUrl("javascript:" + this.mJS.getString(getString(C0517R.string.cb_init)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (this.mBankJS != null && this.mJS == null && this.cbTransparentView != null) {
                this.cbTransparentView.setVisibility(8);
            }
        }
    }

    public void onPageStarted() {
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded()) {
            if (this.nbhelpVisible) {
                onHelpUnavailable();
                this.nbhelpVisible = false;
            }
            if (isAdded() && !isRemoving()) {
                this.mPageReady = false;
                if (this.mBankJS != null) {
                    try {
                        if (this.showCB) {
                            this.cbWebView.loadUrl("javascript:" + this.mBankJS.getString(getString(C0517R.string.cb_detect_bank)));
                            showMagicRetryCB();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (this.cbTransparentView != null) {
                    this.cbTransparentView.setVisibility(8);
                }
            }
        }
    }

    @JavascriptInterface
    public void onFailure(String result) {
        this.merchantResponse = result;
    }

    @JavascriptInterface
    public void onPayuFailure(String result) {
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        if (this.activity != null) {
            this.eventRecorded = CBAnalyticsConstant.FAILURE_TRANSACTION;
            addEventAnalytics(CBAnalyticsConstant.TRNX_STATUS, this.eventRecorded);
            this.isSuccessTransaction = Boolean.valueOf(false);
            this.payuReponse = result;
        }
        cancelTransactionNotification();
        callTimer();
    }

    @JavascriptInterface
    public void onSuccess() {
        onSuccess("");
    }

    @JavascriptInterface
    public void onPayuSuccess(String result) {
        if (this.snoozeService != null) {
            this.snoozeService.killSnoozeService();
        }
        this.isSuccessTransaction = Boolean.valueOf(true);
        this.eventRecorded = CBAnalyticsConstant.SUCCESS_TRANSACTION;
        addEventAnalytics(CBAnalyticsConstant.TRNX_STATUS, this.eventRecorded);
        this.payuReponse = result;
        if (this.storeOneClickHash == 2) {
            try {
                JSONObject hashObject = new JSONObject(this.payuReponse);
                this.cbUtil.storeInSharedPreferences(this.activity.getApplicationContext(), hashObject.getString(CBAnalyticsConstant.CARD_TOKEN), hashObject.getString(CBAnalyticsConstant.MERCHANT_HASH));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cancelTransactionNotification();
        callTimer();
    }

    @JavascriptInterface
    public void onSuccess(String result) {
        this.merchantResponse = result;
    }

    @JavascriptInterface
    public void onCancel() {
        onCancel("");
    }

    @JavascriptInterface
    public void onCancel(final String result) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                if (Bank.this.activity != null && !Bank.this.activity.isFinishing() && Bank.this.isAdded()) {
                    Intent intent = new Intent();
                    intent.putExtra(Bank.this.getString(C0517R.string.cb_result), result);
                    Bank.this.activity.setResult(0, intent);
                    Bank.this.activity.finish();
                }
            }
        });
    }

    void enter_otp(String params) {
        int time_wait_for_sms;
        final TextView textView;
        final String str;
        final View view;
        prepareSmsListener();
        if (this.eventRecorded.equals(CBAnalyticsConstant.PAYMENT_INITIATED)) {
            this.eventRecorded = CBAnalyticsConstant.CUSTOM_BROWSER;
            this.eventArray.add(CBAnalyticsConstant.CUSTOM_BROWSER);
            addEventAnalytics(CBAnalyticsConstant.CB_STATUS, this.eventRecorded);
        }
        if (this.enterOTPView != null) {
            this.customProgressBar.removeProgressDialog(this.enterOTPView.findViewById(C0517R.id.progress));
        }
        this.checkLoading = true;
        onHelpAvailable();
        if (this.cbTransparentView != null) {
            this.cbTransparentView.setVisibility(0);
        }
        if (this.enterOTPView == null) {
            this.enterOTPView = this.activity.getLayoutInflater().inflate(C0517R.layout.wait_for_otp, null);
        }
        final Button approveButton = (Button) this.enterOTPView.findViewById(C0517R.id.approve);
        View regenerateLayoutGone = this.enterOTPView.findViewById(C0517R.id.Regenerate_layout_gone);
        final View enterManuallyGone = this.enterOTPView.findViewById(C0517R.id.Enter_manually_gone);
        final View pinLayoutGone = this.enterOTPView.findViewById(C0517R.id.pin_layout_gone);
        final View regenerateLayout = this.enterOTPView.findViewById(C0517R.id.regenerate_layout);
        ImageView im = (ImageView) this.enterOTPView.findViewById(C0517R.id.bank_logo);
        TextView timerView = (TextView) this.enterOTPView.findViewById(C0517R.id.timer);
        final EditText otpSms = (EditText) this.enterOTPView.findViewById(C0517R.id.otp_sms);
        final View waiting = this.enterOTPView.findViewById(C0517R.id.waiting);
        final View pinButton = this.enterOTPView.findViewById(C0517R.id.pin);
        final View retryButton = this.enterOTPView.findViewById(C0517R.id.retry);
        final View enterManuallyButton = this.enterOTPView.findViewById(C0517R.id.enter_manually);
        final View retryText = this.enterOTPView.findViewById(C0517R.id.retry_text);
        View mProgress = this.enterOTPView.findViewById(C0517R.id.progress);
        View otpReceived = this.enterOTPView.findViewById(C0517R.id.otp_recieved);
        approveButton.setVisibility(0);
        CBUtil.setAlpha(0.3f, approveButton);
        regenerateLayoutGone.setVisibility(8);
        enterManuallyGone.setVisibility(0);
        pinLayoutGone.setVisibility(8);
        regenerateLayout.setVisibility(8);
        timerView.setVisibility(0);
        otpSms.setVisibility(8);
        waiting.setVisibility(0);
        pinButton.setVisibility(0);
        retryButton.setVisibility(0);
        enterManuallyButton.setVisibility(0);
        retryText.setVisibility(8);
        mProgress.setVisibility(0);
        otpReceived.setVisibility(8);
        if (this.drawable != null) {
            im.setImageDrawable(this.drawable);
        }
        otpSms.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (otpSms.getText().toString().length() > 5) {
                    Bank.this.buttonClickListener.setView(Bank.this.enterOTPView);
                    approveButton.setOnClickListener(Bank.this.buttonClickListener);
                    approveButton.setClickable(true);
                    CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                    return;
                }
                approveButton.setClickable(false);
                CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                approveButton.setOnClickListener(null);
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        this.customProgressBar.showDialog(mProgress);
        this.cbBaseView.removeAllViews();
        this.cbBaseView.addView(this.enterOTPView);
        if (this.cbBaseView.isShown()) {
            this.frameState = 2;
        } else {
            maximiseWebviewHeight();
        }
        if (this.frameState == 1 && this.cbSlideBarView != null) {
            this.cbSlideBarView.setVisibility(0);
        }
        try {
            boolean regenerate;
            boolean skipScreen;
            boolean pin;
            LinearLayout otpLayout;
            JSONObject jSONObject = new JSONObject(params);
            if (jSONObject.has(getString(C0517R.string.cb_regenerate))) {
                if (jSONObject.getBoolean(getString(C0517R.string.cb_regenerate))) {
                    regenerate = true;
                    if (jSONObject.has(getString(C0517R.string.cb_skip_screen))) {
                        if (jSONObject.getBoolean(getString(C0517R.string.cb_skip_screen))) {
                            skipScreen = true;
                            if (jSONObject.has(getString(C0517R.string.cb_pin))) {
                                if (jSONObject.getBoolean(getString(C0517R.string.cb_pin))) {
                                    pin = true;
                                    if (skipScreen) {
                                        this.waitingOTPTimer = new Timer();
                                        this.waitingOTPTimer.scheduleAtFixedRate(new TimerTask() {
                                            public synchronized void run() {
                                                if (Bank.this.activity != null) {
                                                    Bank.this.activity.runOnUiThread(Bank.this.enterOtpRunnable);
                                                }
                                            }
                                        }, 0, 1000);
                                    } else if (!(this.activity == null || otpSms == null || otpSms.getVisibility() == 0)) {
                                        retryText.setVisibility(0);
                                        if (regenerate) {
                                            if (pin) {
                                                pinLayoutGone.setVisibility(8);
                                            } else {
                                                pinLayoutGone.setVisibility(0);
                                            }
                                            regenerateLayoutGone.setVisibility(8);
                                            enterManuallyGone.setVisibility(0);
                                        } else {
                                            regenerateLayoutGone.setVisibility(0);
                                            pinLayoutGone.setVisibility(8);
                                            enterManuallyGone.setVisibility(0);
                                        }
                                        regenerateLayout.setVisibility(0);
                                        approveButton.setVisibility(8);
                                        waiting.setVisibility(8);
                                        pinButton.setOnClickListener(this.buttonClickListener);
                                        retryButton.setOnClickListener(this.buttonClickListener);
                                        enterManuallyButton.setOnClickListener(this.buttonClickListener);
                                        updateHeight(this.enterOTPView);
                                    }
                                    if (VERSION.SDK_INT >= 23 && !this.permissionGranted) {
                                        approveButton.setClickable(false);
                                        otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                                        showSoftKeyboard(otpSms);
                                        otpSms.setVisibility(0);
                                        approveButton.setVisibility(0);
                                        waiting.setVisibility(8);
                                        mProgress.setVisibility(8);
                                        regenerateLayout.setVisibility(0);
                                        enterManuallyGone.setVisibility(8);
                                        otpSms.addTextChangedListener(new TextWatcher() {
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                            }

                                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                                if (otpSms.getText().toString().length() > 5) {
                                                    Bank.this.buttonClickListener.setView(Bank.this.enterOTPView);
                                                    approveButton.setOnClickListener(Bank.this.buttonClickListener);
                                                    approveButton.setClickable(true);
                                                    CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                                                    return;
                                                }
                                                approveButton.setClickable(false);
                                                CBUtil.setAlpha(0.3f, approveButton);
                                                approveButton.setOnClickListener(null);
                                            }

                                            public void afterTextChanged(Editable editable) {
                                            }
                                        });
                                    }
                                    if (VERSION.SDK_INT >= 23 || this.permissionGranted) {
                                        time_wait_for_sms = 30;
                                    } else {
                                        time_wait_for_sms = 45;
                                    }
                                    textView = timerView;
                                    str = params;
                                    view = regenerateLayoutGone;
                                    this.enterOtpRunnable = new Runnable() {
                                        int f923i = time_wait_for_sms;

                                        public void run() {
                                            boolean pin = true;
                                            if (this.f923i == 0) {
                                                try {
                                                    if (Bank.this.activity != null && Bank.this.enterOTPView.isShown() && Bank.this.activity.findViewById(C0517R.id.otp_sms) != null) {
                                                        boolean regenerate;
                                                        textView.setVisibility(8);
                                                        JSONObject p = new JSONObject(str);
                                                        if (p.has(Bank.this.getString(C0517R.string.cb_regenerate)) && p.getBoolean(Bank.this.getString(C0517R.string.cb_regenerate))) {
                                                            regenerate = true;
                                                        } else {
                                                            regenerate = false;
                                                        }
                                                        if (!(p.has(Bank.this.getString(C0517R.string.cb_pin)) && p.getBoolean(Bank.this.getString(C0517R.string.cb_pin)))) {
                                                            pin = false;
                                                        }
                                                        if (regenerate) {
                                                            view.setVisibility(0);
                                                            pinLayoutGone.setVisibility(8);
                                                            enterManuallyGone.setVisibility(0);
                                                        } else {
                                                            if (pin) {
                                                                pinLayoutGone.setVisibility(0);
                                                            } else {
                                                                pinLayoutGone.setVisibility(8);
                                                            }
                                                            view.setVisibility(8);
                                                            enterManuallyGone.setVisibility(0);
                                                        }
                                                        retryText.setVisibility(0);
                                                        regenerateLayout.setVisibility(0);
                                                        approveButton.setVisibility(8);
                                                        waiting.setVisibility(8);
                                                        otpSms.setVisibility(8);
                                                        pinButton.setOnClickListener(Bank.this.buttonClickListener);
                                                        retryButton.setOnClickListener(Bank.this.buttonClickListener);
                                                        enterManuallyButton.setOnClickListener(Bank.this.buttonClickListener);
                                                        Bank.this.updateHeight(Bank.this.enterOTPView);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (!otpSms.hasFocus() && otpSms.getText().toString().matches("")) {
                                                String tMessage;
                                                if (this.f923i == time_wait_for_sms) {
                                                    textView.setVisibility(0);
                                                }
                                                if (VERSION.SDK_INT < 23 || Bank.this.permissionGranted) {
                                                    tMessage = this.f923i + "";
                                                } else if (this.f923i != 1) {
                                                    tMessage = this.f923i + "  secs remaining to regenerate OTP\n";
                                                } else {
                                                    tMessage = this.f923i + " sec remaining to regenerate OTP\n";
                                                }
                                                textView.setText(tMessage);
                                                this.f923i--;
                                            }
                                        }
                                    };
                                    if (!(this.mPassword == null || otpSms == null || otpSms.getVisibility() == 0)) {
                                        this.cbUtil.cancelTimer(this.waitingOTPTimer);
                                        this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                                        if (this.eventRecorded.equals(CBAnalyticsConstant.PAYMENT_INITIATED) || this.eventRecorded.equals(CBAnalyticsConstant.CUSTOM_BROWSER)) {
                                            this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                                            addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                                        }
                                        otpSms.setText(this.mPassword);
                                        approveButton.setText(getString(C0517R.string.cb_approve_otp));
                                        approveButton.setClickable(true);
                                        if (this.autoApprove) {
                                            approveButton.performClick();
                                            this.eventRecorded = "auto_approve";
                                            addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                                        }
                                        CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                                        otpReceived.setVisibility(0);
                                        this.customProgressBar.removeDialog(mProgress);
                                        retryText.setVisibility(8);
                                        regenerateLayout.setVisibility(8);
                                        approveButton.setVisibility(0);
                                        waiting.setVisibility(8);
                                        otpSms.setVisibility(0);
                                        this.buttonClickListener.setView(this.enterOTPView);
                                        approveButton.setOnClickListener(this.buttonClickListener);
                                    }
                                    updateHeight(this.enterOTPView);
                                    if (this.cbBaseView.isShown()) {
                                        maximiseWebviewHeight();
                                    } else {
                                        this.frameState = 2;
                                    }
                                }
                            }
                            pin = false;
                            if (skipScreen) {
                                this.waitingOTPTimer = new Timer();
                                this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                            } else {
                                retryText.setVisibility(0);
                                if (regenerate) {
                                    if (pin) {
                                        pinLayoutGone.setVisibility(8);
                                    } else {
                                        pinLayoutGone.setVisibility(0);
                                    }
                                    regenerateLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                } else {
                                    regenerateLayoutGone.setVisibility(0);
                                    pinLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                }
                                regenerateLayout.setVisibility(0);
                                approveButton.setVisibility(8);
                                waiting.setVisibility(8);
                                pinButton.setOnClickListener(this.buttonClickListener);
                                retryButton.setOnClickListener(this.buttonClickListener);
                                enterManuallyButton.setOnClickListener(this.buttonClickListener);
                                updateHeight(this.enterOTPView);
                            }
                            approveButton.setClickable(false);
                            otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                            showSoftKeyboard(otpSms);
                            otpSms.setVisibility(0);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            mProgress.setVisibility(8);
                            regenerateLayout.setVisibility(0);
                            enterManuallyGone.setVisibility(8);
                            otpSms.addTextChangedListener(/* anonymous class already generated */);
                            if (VERSION.SDK_INT >= 23) {
                            }
                            time_wait_for_sms = 30;
                            textView = timerView;
                            str = params;
                            view = regenerateLayoutGone;
                            this.enterOtpRunnable = /* anonymous class already generated */;
                            this.cbUtil.cancelTimer(this.waitingOTPTimer);
                            this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                            this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                            addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                            otpSms.setText(this.mPassword);
                            approveButton.setText(getString(C0517R.string.cb_approve_otp));
                            approveButton.setClickable(true);
                            if (this.autoApprove) {
                                approveButton.performClick();
                                this.eventRecorded = "auto_approve";
                                addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                            }
                            CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                            otpReceived.setVisibility(0);
                            this.customProgressBar.removeDialog(mProgress);
                            retryText.setVisibility(8);
                            regenerateLayout.setVisibility(8);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            otpSms.setVisibility(0);
                            this.buttonClickListener.setView(this.enterOTPView);
                            approveButton.setOnClickListener(this.buttonClickListener);
                            updateHeight(this.enterOTPView);
                            if (this.cbBaseView.isShown()) {
                                maximiseWebviewHeight();
                            } else {
                                this.frameState = 2;
                            }
                        }
                    }
                    skipScreen = false;
                    if (jSONObject.has(getString(C0517R.string.cb_pin))) {
                        if (jSONObject.getBoolean(getString(C0517R.string.cb_pin))) {
                            pin = true;
                            if (skipScreen) {
                                retryText.setVisibility(0);
                                if (regenerate) {
                                    regenerateLayoutGone.setVisibility(0);
                                    pinLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                } else {
                                    if (pin) {
                                        pinLayoutGone.setVisibility(0);
                                    } else {
                                        pinLayoutGone.setVisibility(8);
                                    }
                                    regenerateLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                }
                                regenerateLayout.setVisibility(0);
                                approveButton.setVisibility(8);
                                waiting.setVisibility(8);
                                pinButton.setOnClickListener(this.buttonClickListener);
                                retryButton.setOnClickListener(this.buttonClickListener);
                                enterManuallyButton.setOnClickListener(this.buttonClickListener);
                                updateHeight(this.enterOTPView);
                            } else {
                                this.waitingOTPTimer = new Timer();
                                this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                            }
                            approveButton.setClickable(false);
                            otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                            showSoftKeyboard(otpSms);
                            otpSms.setVisibility(0);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            mProgress.setVisibility(8);
                            regenerateLayout.setVisibility(0);
                            enterManuallyGone.setVisibility(8);
                            otpSms.addTextChangedListener(/* anonymous class already generated */);
                            if (VERSION.SDK_INT >= 23) {
                            }
                            time_wait_for_sms = 30;
                            textView = timerView;
                            str = params;
                            view = regenerateLayoutGone;
                            this.enterOtpRunnable = /* anonymous class already generated */;
                            this.cbUtil.cancelTimer(this.waitingOTPTimer);
                            this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                            this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                            addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                            otpSms.setText(this.mPassword);
                            approveButton.setText(getString(C0517R.string.cb_approve_otp));
                            approveButton.setClickable(true);
                            if (this.autoApprove) {
                                approveButton.performClick();
                                this.eventRecorded = "auto_approve";
                                addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                            }
                            CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                            otpReceived.setVisibility(0);
                            this.customProgressBar.removeDialog(mProgress);
                            retryText.setVisibility(8);
                            regenerateLayout.setVisibility(8);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            otpSms.setVisibility(0);
                            this.buttonClickListener.setView(this.enterOTPView);
                            approveButton.setOnClickListener(this.buttonClickListener);
                            updateHeight(this.enterOTPView);
                            if (this.cbBaseView.isShown()) {
                                this.frameState = 2;
                            } else {
                                maximiseWebviewHeight();
                            }
                        }
                    }
                    pin = false;
                    if (skipScreen) {
                        this.waitingOTPTimer = new Timer();
                        this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                    } else {
                        retryText.setVisibility(0);
                        if (regenerate) {
                            if (pin) {
                                pinLayoutGone.setVisibility(8);
                            } else {
                                pinLayoutGone.setVisibility(0);
                            }
                            regenerateLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        } else {
                            regenerateLayoutGone.setVisibility(0);
                            pinLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        }
                        regenerateLayout.setVisibility(0);
                        approveButton.setVisibility(8);
                        waiting.setVisibility(8);
                        pinButton.setOnClickListener(this.buttonClickListener);
                        retryButton.setOnClickListener(this.buttonClickListener);
                        enterManuallyButton.setOnClickListener(this.buttonClickListener);
                        updateHeight(this.enterOTPView);
                    }
                    approveButton.setClickable(false);
                    otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                    showSoftKeyboard(otpSms);
                    otpSms.setVisibility(0);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    mProgress.setVisibility(8);
                    regenerateLayout.setVisibility(0);
                    enterManuallyGone.setVisibility(8);
                    otpSms.addTextChangedListener(/* anonymous class already generated */);
                    if (VERSION.SDK_INT >= 23) {
                    }
                    time_wait_for_sms = 30;
                    textView = timerView;
                    str = params;
                    view = regenerateLayoutGone;
                    this.enterOtpRunnable = /* anonymous class already generated */;
                    this.cbUtil.cancelTimer(this.waitingOTPTimer);
                    this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                    this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                    addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                    otpSms.setText(this.mPassword);
                    approveButton.setText(getString(C0517R.string.cb_approve_otp));
                    approveButton.setClickable(true);
                    if (this.autoApprove) {
                        approveButton.performClick();
                        this.eventRecorded = "auto_approve";
                        addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                    }
                    CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                    otpReceived.setVisibility(0);
                    this.customProgressBar.removeDialog(mProgress);
                    retryText.setVisibility(8);
                    regenerateLayout.setVisibility(8);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    otpSms.setVisibility(0);
                    this.buttonClickListener.setView(this.enterOTPView);
                    approveButton.setOnClickListener(this.buttonClickListener);
                    updateHeight(this.enterOTPView);
                    if (this.cbBaseView.isShown()) {
                        maximiseWebviewHeight();
                    } else {
                        this.frameState = 2;
                    }
                }
            }
            regenerate = false;
            if (jSONObject.has(getString(C0517R.string.cb_skip_screen))) {
                if (jSONObject.getBoolean(getString(C0517R.string.cb_skip_screen))) {
                    skipScreen = true;
                    if (jSONObject.has(getString(C0517R.string.cb_pin))) {
                        if (jSONObject.getBoolean(getString(C0517R.string.cb_pin))) {
                            pin = true;
                            if (skipScreen) {
                                retryText.setVisibility(0);
                                if (regenerate) {
                                    regenerateLayoutGone.setVisibility(0);
                                    pinLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                } else {
                                    if (pin) {
                                        pinLayoutGone.setVisibility(0);
                                    } else {
                                        pinLayoutGone.setVisibility(8);
                                    }
                                    regenerateLayoutGone.setVisibility(8);
                                    enterManuallyGone.setVisibility(0);
                                }
                                regenerateLayout.setVisibility(0);
                                approveButton.setVisibility(8);
                                waiting.setVisibility(8);
                                pinButton.setOnClickListener(this.buttonClickListener);
                                retryButton.setOnClickListener(this.buttonClickListener);
                                enterManuallyButton.setOnClickListener(this.buttonClickListener);
                                updateHeight(this.enterOTPView);
                            } else {
                                this.waitingOTPTimer = new Timer();
                                this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                            }
                            approveButton.setClickable(false);
                            otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                            showSoftKeyboard(otpSms);
                            otpSms.setVisibility(0);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            mProgress.setVisibility(8);
                            regenerateLayout.setVisibility(0);
                            enterManuallyGone.setVisibility(8);
                            otpSms.addTextChangedListener(/* anonymous class already generated */);
                            if (VERSION.SDK_INT >= 23) {
                            }
                            time_wait_for_sms = 30;
                            textView = timerView;
                            str = params;
                            view = regenerateLayoutGone;
                            this.enterOtpRunnable = /* anonymous class already generated */;
                            this.cbUtil.cancelTimer(this.waitingOTPTimer);
                            this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                            this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                            addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                            otpSms.setText(this.mPassword);
                            approveButton.setText(getString(C0517R.string.cb_approve_otp));
                            approveButton.setClickable(true);
                            if (this.autoApprove) {
                                approveButton.performClick();
                                this.eventRecorded = "auto_approve";
                                addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                            }
                            CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                            otpReceived.setVisibility(0);
                            this.customProgressBar.removeDialog(mProgress);
                            retryText.setVisibility(8);
                            regenerateLayout.setVisibility(8);
                            approveButton.setVisibility(0);
                            waiting.setVisibility(8);
                            otpSms.setVisibility(0);
                            this.buttonClickListener.setView(this.enterOTPView);
                            approveButton.setOnClickListener(this.buttonClickListener);
                            updateHeight(this.enterOTPView);
                            if (this.cbBaseView.isShown()) {
                                this.frameState = 2;
                            } else {
                                maximiseWebviewHeight();
                            }
                        }
                    }
                    pin = false;
                    if (skipScreen) {
                        this.waitingOTPTimer = new Timer();
                        this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                    } else {
                        retryText.setVisibility(0);
                        if (regenerate) {
                            if (pin) {
                                pinLayoutGone.setVisibility(8);
                            } else {
                                pinLayoutGone.setVisibility(0);
                            }
                            regenerateLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        } else {
                            regenerateLayoutGone.setVisibility(0);
                            pinLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        }
                        regenerateLayout.setVisibility(0);
                        approveButton.setVisibility(8);
                        waiting.setVisibility(8);
                        pinButton.setOnClickListener(this.buttonClickListener);
                        retryButton.setOnClickListener(this.buttonClickListener);
                        enterManuallyButton.setOnClickListener(this.buttonClickListener);
                        updateHeight(this.enterOTPView);
                    }
                    approveButton.setClickable(false);
                    otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                    showSoftKeyboard(otpSms);
                    otpSms.setVisibility(0);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    mProgress.setVisibility(8);
                    regenerateLayout.setVisibility(0);
                    enterManuallyGone.setVisibility(8);
                    otpSms.addTextChangedListener(/* anonymous class already generated */);
                    if (VERSION.SDK_INT >= 23) {
                    }
                    time_wait_for_sms = 30;
                    textView = timerView;
                    str = params;
                    view = regenerateLayoutGone;
                    this.enterOtpRunnable = /* anonymous class already generated */;
                    this.cbUtil.cancelTimer(this.waitingOTPTimer);
                    this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                    this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                    addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                    otpSms.setText(this.mPassword);
                    approveButton.setText(getString(C0517R.string.cb_approve_otp));
                    approveButton.setClickable(true);
                    if (this.autoApprove) {
                        approveButton.performClick();
                        this.eventRecorded = "auto_approve";
                        addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                    }
                    CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                    otpReceived.setVisibility(0);
                    this.customProgressBar.removeDialog(mProgress);
                    retryText.setVisibility(8);
                    regenerateLayout.setVisibility(8);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    otpSms.setVisibility(0);
                    this.buttonClickListener.setView(this.enterOTPView);
                    approveButton.setOnClickListener(this.buttonClickListener);
                    updateHeight(this.enterOTPView);
                    if (this.cbBaseView.isShown()) {
                        maximiseWebviewHeight();
                    } else {
                        this.frameState = 2;
                    }
                }
            }
            skipScreen = false;
            if (jSONObject.has(getString(C0517R.string.cb_pin))) {
                if (jSONObject.getBoolean(getString(C0517R.string.cb_pin))) {
                    pin = true;
                    if (skipScreen) {
                        retryText.setVisibility(0);
                        if (regenerate) {
                            regenerateLayoutGone.setVisibility(0);
                            pinLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        } else {
                            if (pin) {
                                pinLayoutGone.setVisibility(0);
                            } else {
                                pinLayoutGone.setVisibility(8);
                            }
                            regenerateLayoutGone.setVisibility(8);
                            enterManuallyGone.setVisibility(0);
                        }
                        regenerateLayout.setVisibility(0);
                        approveButton.setVisibility(8);
                        waiting.setVisibility(8);
                        pinButton.setOnClickListener(this.buttonClickListener);
                        retryButton.setOnClickListener(this.buttonClickListener);
                        enterManuallyButton.setOnClickListener(this.buttonClickListener);
                        updateHeight(this.enterOTPView);
                    } else {
                        this.waitingOTPTimer = new Timer();
                        this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
                    }
                    approveButton.setClickable(false);
                    otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
                    showSoftKeyboard(otpSms);
                    otpSms.setVisibility(0);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    mProgress.setVisibility(8);
                    regenerateLayout.setVisibility(0);
                    enterManuallyGone.setVisibility(8);
                    otpSms.addTextChangedListener(/* anonymous class already generated */);
                    if (VERSION.SDK_INT >= 23) {
                    }
                    time_wait_for_sms = 30;
                    textView = timerView;
                    str = params;
                    view = regenerateLayoutGone;
                    this.enterOtpRunnable = /* anonymous class already generated */;
                    this.cbUtil.cancelTimer(this.waitingOTPTimer);
                    this.activity.findViewById(C0517R.id.timer).setVisibility(8);
                    this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
                    addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
                    otpSms.setText(this.mPassword);
                    approveButton.setText(getString(C0517R.string.cb_approve_otp));
                    approveButton.setClickable(true);
                    if (this.autoApprove) {
                        approveButton.performClick();
                        this.eventRecorded = "auto_approve";
                        addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
                    }
                    CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
                    otpReceived.setVisibility(0);
                    this.customProgressBar.removeDialog(mProgress);
                    retryText.setVisibility(8);
                    regenerateLayout.setVisibility(8);
                    approveButton.setVisibility(0);
                    waiting.setVisibility(8);
                    otpSms.setVisibility(0);
                    this.buttonClickListener.setView(this.enterOTPView);
                    approveButton.setOnClickListener(this.buttonClickListener);
                    updateHeight(this.enterOTPView);
                    if (this.cbBaseView.isShown()) {
                        this.frameState = 2;
                    } else {
                        maximiseWebviewHeight();
                    }
                }
            }
            pin = false;
            if (skipScreen) {
                this.waitingOTPTimer = new Timer();
                this.waitingOTPTimer.scheduleAtFixedRate(/* anonymous class already generated */, 0, 1000);
            } else {
                retryText.setVisibility(0);
                if (regenerate) {
                    if (pin) {
                        pinLayoutGone.setVisibility(8);
                    } else {
                        pinLayoutGone.setVisibility(0);
                    }
                    regenerateLayoutGone.setVisibility(8);
                    enterManuallyGone.setVisibility(0);
                } else {
                    regenerateLayoutGone.setVisibility(0);
                    pinLayoutGone.setVisibility(8);
                    enterManuallyGone.setVisibility(0);
                }
                regenerateLayout.setVisibility(0);
                approveButton.setVisibility(8);
                waiting.setVisibility(8);
                pinButton.setOnClickListener(this.buttonClickListener);
                retryButton.setOnClickListener(this.buttonClickListener);
                enterManuallyButton.setOnClickListener(this.buttonClickListener);
                updateHeight(this.enterOTPView);
            }
            approveButton.setClickable(false);
            otpLayout = (LinearLayout) this.enterOTPView.findViewById(C0517R.id.linear_layout_waiting_for_otp);
            showSoftKeyboard(otpSms);
            otpSms.setVisibility(0);
            approveButton.setVisibility(0);
            waiting.setVisibility(8);
            mProgress.setVisibility(8);
            regenerateLayout.setVisibility(0);
            enterManuallyGone.setVisibility(8);
            otpSms.addTextChangedListener(/* anonymous class already generated */);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (VERSION.SDK_INT >= 23) {
        }
        time_wait_for_sms = 30;
        textView = timerView;
        str = params;
        view = regenerateLayoutGone;
        this.enterOtpRunnable = /* anonymous class already generated */;
        this.cbUtil.cancelTimer(this.waitingOTPTimer);
        this.activity.findViewById(C0517R.id.timer).setVisibility(8);
        this.eventRecorded = CBAnalyticsConstant.RECEIVED_OTP_DIRECT;
        addEventAnalytics(CBAnalyticsConstant.OTP_RECIEVED, this.eventRecorded);
        otpSms.setText(this.mPassword);
        approveButton.setText(getString(C0517R.string.cb_approve_otp));
        approveButton.setClickable(true);
        if (this.autoApprove) {
            approveButton.performClick();
            this.eventRecorded = "auto_approve";
            addEventAnalytics(CBAnalyticsConstant.USER_INPUT, this.eventRecorded);
        }
        CBUtil.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, approveButton);
        otpReceived.setVisibility(0);
        this.customProgressBar.removeDialog(mProgress);
        retryText.setVisibility(8);
        regenerateLayout.setVisibility(8);
        approveButton.setVisibility(0);
        waiting.setVisibility(8);
        otpSms.setVisibility(0);
        this.buttonClickListener.setView(this.enterOTPView);
        approveButton.setOnClickListener(this.buttonClickListener);
        updateHeight(this.enterOTPView);
        if (this.cbBaseView.isShown()) {
            maximiseWebviewHeight();
        } else {
            this.frameState = 2;
        }
    }

    int getCode(String str) {
        if (str.equalsIgnoreCase("pin")) {
            return 3;
        }
        if (str.equalsIgnoreCase(SdkConstants.PASSWORD)) {
            return 1;
        }
        if (str.equalsIgnoreCase("enter manually")) {
            return 4;
        }
        if (str.equalsIgnoreCase("approve")) {
            return 5;
        }
        if (str.equalsIgnoreCase(SdkConstants.OTP_STRING) || str.equalsIgnoreCase("use sms otp")) {
            return 6;
        }
        if (str.equalsIgnoreCase("sms otp")) {
            return 7;
        }
        if (str.equalsIgnoreCase("regenerate otp")) {
            return 2;
        }
        return 0;
    }

    private void startSnoozeCountDownTimer() {
        this.mCountDownTimer = new CountDownTimer((long) this.snoozeUrlLoadingTimeout, 500) {
            public void onTick(long l) {
                Bank.this.isSnoozeTimerRunning = true;
            }

            public void onFinish() {
                Bank.this.isSnoozeTimerRunning = false;
                if (Bank.this.cbWebView.getProgress() < Bank.this.snoozeUrlLoadingPercentage && !Bank.this.isSnoozeWindowVisible && Bank.this.showSnoozeWindow && !Bank.this.isTransactionStatusReceived) {
                    Bank.this.launchSnoozeWindow();
                }
                Bank.this.stopSnoozeCountDownTimer();
            }
        };
        this.mCountDownTimer.start();
    }

    private void stopSnoozeCountDownTimer() {
        if (this.mCountDownTimer != null) {
            this.isSnoozeTimerRunning = false;
            this.mCountDownTimer.cancel();
            this.mCountDownTimer = null;
        }
    }

    public void launchSnoozeWindow() {
        launchSnoozeWindow(1);
    }

    public void launchSnoozeWindow(int mode) {
        showCbBlankOverlay(8);
        if (this.backwardJourneyStarted) {
            if (this.snoozeCountBackwardJourney >= this.customBrowserConfig.getEnableSurePay()) {
                return;
            }
        } else if (this.snoozeCount >= this.customBrowserConfig.getEnableSurePay()) {
            return;
        }
        this.snoozeMode = mode;
        if (this.activity != null && !this.activity.isFinishing()) {
            dismissSlowUserWarning();
            progressBarVisibilityPayuChrome(8, "");
            this.isSnoozeWindowVisible = true;
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_STATUS, CBAnalyticsConstant.SNOOZE_VISIBLE);
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_APPEAR_URL, this.webviewUrl);
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_LAUNCH_MODE, mode == 1 ? CBConstant.STR_SNOOZE_MODE_WARN : CBConstant.STR_SNOOZE_MODE_FAIL);
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_APPEAR_TIME, CustomBrowserMain.getSystemCurrentTime());
            LayoutInflater layoutInflater = this.activity.getLayoutInflater();
            if (this.snoozeLayout == null) {
                this.snoozeLayout = layoutInflater.inflate(C0517R.layout.cb_layout_snooze, null);
            }
            final TextView snoozeMessageTextView = (TextView) this.snoozeLayout.findViewById(C0517R.id.text_view_snooze_message);
            final TextView transactionSnoozedMessageTextView1 = (TextView) this.snoozeLayout.findViewById(C0517R.id.text_view_transaction_snoozed_message1);
            final TextView dismissSnoozeWindow = (TextView) this.snoozeLayout.findViewById(C0517R.id.button_cancel_transaction);
            final Button snoozeTransactionButton = (Button) this.snoozeLayout.findViewById(C0517R.id.button_snooze_transaction);
            final Button retryTransactionButton = (Button) this.snoozeLayout.findViewById(C0517R.id.button_retry_transaction);
            final TextView cancelTxnSnooze = (TextView) this.snoozeLayout.findViewById(C0517R.id.text_view_cancel_snooze_window);
            final TextView tConfirm = (TextView) this.snoozeLayout.findViewById(C0517R.id.t_confirm);
            final TextView tNConfirm = (TextView) this.snoozeLayout.findViewById(C0517R.id.t_nconfirm);
            final TextView snoozeDialogHeaderText = (TextView) this.snoozeLayout.findViewById(C0517R.id.snooze_header_txt);
            final TextView snoozeRetryDetailText = (TextView) this.snoozeLayout.findViewById(C0517R.id.text_view_retry_message_detail);
            final Button retryAnywayButton = (Button) this.snoozeLayout.findViewById(C0517R.id.button_retry_anyway);
            this.snoozeLoaderView = (SnoozeLoaderView) this.snoozeLayout.findViewById(C0517R.id.snooze_loader_view);
            this.snoozeLoaderView.setVisibility(8);
            cancelTxnSnooze.setVisibility(0);
            dismissSnoozeWindow.setVisibility(0);
            snoozeTransactionButton.setVisibility(0);
            retryTransactionButton.setVisibility(0);
            snoozeMessageTextView.setVisibility(0);
            transactionSnoozedMessageTextView1.setVisibility(8);
            snoozeRetryDetailText.setVisibility(0);
            tConfirm.setVisibility(8);
            tNConfirm.setVisibility(8);
            retryAnywayButton.setVisibility(8);
            snoozeMessageTextView.setText(this.activity.getString(C0517R.string.cb_slownetwork_status));
            snoozeDialogHeaderText.setText(this.activity.getString(C0517R.string.cb_try_later));
            snoozeRetryDetailText.setText(this.activity.getString(C0517R.string.cb_retry_restart));
            if (this.backwardJourneyStarted && this.payuPG) {
                snoozeMessageTextView.setText(this.activity.getResources().getString(C0517R.string.cb_slow_internet_confirmation));
                transactionSnoozedMessageTextView1.setText(this.activity.getResources().getString(C0517R.string.cb_receive_sms));
                snoozeDialogHeaderText.setText(this.activity.getResources().getString(C0517R.string.cb_confirm_transaction));
                snoozeTransactionButton.setVisibility(8);
                snoozeRetryDetailText.setVisibility(8);
                retryTransactionButton.setVisibility(8);
                dismissSnoozeWindow.setVisibility(8);
                snoozeMessageTextView.setVisibility(0);
                transactionSnoozedMessageTextView1.setVisibility(0);
                tConfirm.setVisibility(0);
                tNConfirm.setVisibility(0);
                retryAnywayButton.setVisibility(8);
                this.snoozeVisibleCountBackwdJourney++;
                addEventAnalytics(CBAnalyticsConstant.SNOOZE_BACKWARD_VISIBLE, "Y");
            } else {
                this.snoozeVisibleCountFwdJourney++;
            }
            tConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_BACKWARD_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CONFIRM_DEDUCTION_Y);
                    if (Bank.this.waitingOTPTimer != null) {
                        Bank.this.waitingOTPTimer.cancel();
                        Bank.this.waitingOTPTimer.purge();
                    }
                    Bank bank = Bank.this;
                    bank.snoozeCountBackwardJourney++;
                    Bank.this.snoozeDialog.setCanceledOnTouchOutside(false);
                    snoozeDialogHeaderText.setText(Bank.this.activity.getResources().getString(C0517R.string.cb_confirm_transaction));
                    snoozeMessageTextView.setText(Bank.this.activity.getString(C0517R.string.cb_transaction_status));
                    Bank.this.snoozeLoaderView.setVisibility(0);
                    Bank.this.snoozeLoaderView.startAnimation();
                    snoozeTransactionButton.setVisibility(8);
                    transactionSnoozedMessageTextView1.setVisibility(8);
                    tConfirm.setVisibility(8);
                    tNConfirm.setVisibility(8);
                    if (Bank.this.verificationMsgReceived) {
                        Bank.this.startSnoozeServiceVerifyPayment(Bank.this.activity.getResources().getString(C0517R.string.cb_verify_message_received));
                    } else {
                        Bank.this.startSnoozeServiceVerifyPayment(Bank.this.activity.getResources().getString(C0517R.string.cb_user_input_confirm_transaction));
                    }
                }
            });
            tNConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Bank bank = Bank.this;
                    bank.snoozeCountBackwardJourney++;
                    Bank.this.dismissSnoozeWindow();
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_BACKWARD_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CONFIRM_DEDUCTION_N);
                }
            });
            cancelTxnSnooze.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Bank bank;
                    if (Bank.this.backwardJourneyStarted) {
                        bank = Bank.this;
                        bank.snoozeCountBackwardJourney++;
                    } else {
                        bank = Bank.this;
                        bank.snoozeCount++;
                    }
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
                    if (!Bank.this.backwardJourneyStarted) {
                        Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CANCEL_WINDOW_CLICK);
                    }
                    Bank.this.dismissSnoozeWindow();
                }
            });
            retryTransactionButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Bank.this.hideCB();
                    Bank.this.retryPayment(view);
                }
            });
            final Button button = snoozeTransactionButton;
            final TextView textView = snoozeMessageTextView;
            final TextView textView2 = snoozeDialogHeaderText;
            final TextView textView3 = transactionSnoozedMessageTextView1;
            snoozeTransactionButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Bank bank = Bank.this;
                    bank.snoozeCount++;
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
                    Bank.this.maximiseWebviewHeight();
                    Bank.this.frameState = 1;
                    if (Bank.this.cbSlideBarView != null) {
                        Bank.this.cbSlideBarView.setVisibility(8);
                    }
                    Bank.this.onHelpUnavailable();
                    Bank.this.snoozeClickedTime = System.currentTimeMillis();
                    Bank.this.isSnoozeBroadCastReceiverRegistered = true;
                    Bank.this.isPageStoppedForcefully = true;
                    Bank.this.cbWebView.stopLoading();
                    if (!(CustomBrowserData.SINGLETON == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() == null)) {
                        Bank.this.bindService();
                    }
                    Bank.this.mPassword = null;
                    Bank.this.unregisterBroadcast(Bank.this.mBroadcastReceiver);
                    cancelTxnSnooze.setVisibility(8);
                    dismissSnoozeWindow.setVisibility(8);
                    button.setVisibility(8);
                    retryTransactionButton.setVisibility(8);
                    textView.setText("We have paused your transaction because the network was unable to process it now. We will notify you when the network improves.");
                    snoozeRetryDetailText.setVisibility(8);
                    textView2.setText(Bank.this.activity.getResources().getString(C0517R.string.cb_transaction_paused));
                    textView3.setVisibility(0);
                    retryAnywayButton.setVisibility(0);
                    Bank.this.progressBarVisibilityPayuChrome(8, "");
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CLICK);
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_LOAD_URL, Bank.this.cbWebView.getUrl() == null ? Bank.this.webviewUrl : Bank.this.cbWebView.getUrl());
                    Bank.this.slowUserCountDownTimer = null;
                    Bank.this.showCbBlankOverlay(0);
                }
            });
            dismissSnoozeWindow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Bank bank;
                    if (Bank.this.backwardJourneyStarted) {
                        bank = Bank.this;
                        bank.snoozeCountBackwardJourney++;
                    } else {
                        bank = Bank.this;
                        bank.snoozeCount++;
                    }
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
                    Bank.this.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CANCEL_TRANSACTION_CLICK);
                    Bank.this.showBackButtonDialog();
                }
            });
            retryAnywayButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Bank.this.hideCB();
                    Bank.this.retryPayment(view);
                }
            });
            if (this.snoozeDialog == null) {
                this.snoozeDialog = new Builder(this.activity).create();
                this.snoozeDialog.setView(this.snoozeLayout);
                this.snoozeDialog.setCanceledOnTouchOutside(false);
                this.snoozeDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        Bank.this.showCbBlankOverlay(8);
                    }
                });
                this.snoozeDialog.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                        if (keyCode == 4 && event.getAction() == 0) {
                            Bank.this.showBackButtonDialog();
                        }
                        return true;
                    }
                });
            }
            this.snoozeDialog.show();
        }
    }

    public void bindService() {
        LocalBroadcastManager.getInstance(this.activity).unregisterReceiver(this.snoozeBroadCastReceiver);
        LocalBroadcastManager.getInstance(this.activity.getApplicationContext()).registerReceiver(this.snoozeBroadCastReceiver, new IntentFilter(this.SNOOZE_GET_WEBVIEW_STATUS_INTENT_ACTION));
        Intent intent = new Intent(this.activity, SnoozeService.class);
        intent.putExtra(CBConstant.CB_CONFIG, this.customBrowserConfig);
        intent.putExtra(CBConstant.CURRENT_URL, this.webviewUrl);
        intent.putExtra(CBConstant.MERCHANT_CHECKOUT_ACTIVITY, this.customBrowserConfig.getMerchantCheckoutActivityPath());
        this.isSnoozeServiceBounded = true;
        this.activity.bindService(intent, this.snoozeServiceConnection, 1);
        this.activity.startService(intent);
    }

    public void startSnoozeServiceVerifyPayment(String verifyParam) {
        LocalBroadcastManager.getInstance(this.activity).unregisterReceiver(this.snoozeBroadCastReceiver);
        LocalBroadcastManager.getInstance(this.activity.getApplicationContext()).registerReceiver(this.snoozeBroadCastReceiver, new IntentFilter(this.SNOOZE_GET_WEBVIEW_STATUS_INTENT_ACTION));
        Intent intent = new Intent(this.activity, SnoozeService.class);
        intent.putExtra(CBConstant.CB_CONFIG, this.customBrowserConfig);
        intent.putExtra(CBConstant.VERIFICATION_MSG_RECEIVED, true);
        intent.putExtra(CBConstant.MERCHANT_CHECKOUT_ACTIVITY, this.customBrowserConfig.getMerchantCheckoutActivityPath());
        intent.putExtra(CBConstant.VERIFY_ADDON_PARAMS, verifyParam);
        this.isSnoozeServiceBounded = true;
        this.activity.bindService(intent, this.snoozeServiceConnection, 1);
        this.isSnoozeBroadCastReceiverRegistered = true;
        this.activity.startService(intent);
    }

    public void dismissSnoozeWindow() {
        this.isSnoozeWindowVisible = false;
        if (this.snoozeDialog != null) {
            this.snoozeDialog.dismiss();
            this.snoozeDialog.cancel();
            showCbBlankOverlay(8);
        }
    }

    public void setMagicRetry(Map<String, String> urlList) {
        if (this.magicRetryFragment != null) {
            this.magicRetryFragment.setUrlListWithPostData(urlList);
        }
    }

    public void initMagicRetry() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        this.magicRetryFragment = new MagicRetryFragment();
        Bundle newInformationBundle = new Bundle();
        if (!(CustomBrowserData.SINGLETON == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() == null)) {
            newInformationBundle.putString("transaction_id", this.customBrowserConfig.getTransactionID());
        }
        this.magicRetryFragment.setArguments(newInformationBundle);
        fragmentManager.beginTransaction().add(C0517R.id.magic_retry_container, this.magicRetryFragment, "magicRetry").commit();
        toggleFragmentVisibility(0);
        this.magicRetryFragment.isWhiteListingEnabled(true);
        this.magicRetryFragment.setWebView(this.cbWebView);
        this.magicRetryFragment.initMRSettingsFromSharedPreference(this.activity);
        if (this.customBrowserConfig.getEnableSurePay() > 0) {
            this.cbWebView.setWebViewClient(new PayUSurePayWebViewClient(this, keyAnalytics));
        } else {
            this.cbWebView.setWebViewClient(new PayUWebViewClient(this, this.magicRetryFragment, keyAnalytics));
        }
    }

    public void toggleFragmentVisibility(int flag) {
        if (getActivity() != null && !getActivity().isFinishing() && isAdded() && !isRemoving()) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            if (this.magicRetryFragment != null && flag == 1) {
                ft.show(this.magicRetryFragment).commitAllowingStateLoss();
            } else if (this.magicRetryFragment != null && flag == 0) {
                ft.hide(this.magicRetryFragment).commitAllowingStateLoss();
            }
        }
    }

    public void showMagicRetry() {
        dismissSnoozeWindow();
        toggleFragmentVisibility(1);
    }

    public void hideMagicRetry() {
        toggleFragmentVisibility(0);
    }

    public void showBackButtonDialog() {
        AlertDialog.Builder backButtonClickAlertDialog = new AlertDialog.Builder(this.activity);
        backButtonClickAlertDialog.setCancelable(false);
        backButtonClickAlertDialog.setMessage("Do you really want to cancel the transaction ?");
        backButtonClickAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Bank.this.snoozeDialog != null && Bank.this.snoozeDialog.isShowing()) {
                    Bank.this.snoozeDialog.cancel();
                }
                Bank.this.killSnoozeService();
                Bank.this.cancelTransactionNotification();
                Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_OK_CLICK);
                Bank.this.dismissSnoozeWindow();
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onPaymentTerminate();
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onBackApprove();
                Bank.this.activity.finish();
            }
        });
        backButtonClickAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bank.this.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_CANCEL_CLICK);
                dialog.dismiss();
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onBackDismiss();
            }
        });
        backButtonClickAlertDialog.create().getWindow().getAttributes().type = 2003;
        backButtonClickAlertDialog.show();
    }

    public void setIsPageStoppedForcefully(boolean isPageStoppedForcefully) {
        this.isPageStoppedForcefully = isPageStoppedForcefully;
    }

    private void updateWhitelistedRetryUrls(List<String> urls) {
        whiteListedUrls.clear();
        C0533L.m760v("#### PAYU", "MR Cleared whitelisted urls, length: " + whiteListedUrls.size());
        whiteListedUrls.addAll(urls);
        C0533L.m760v("#### PAYU", "MR Updated whitelisted urls, length: " + whiteListedUrls.size());
    }

    @JavascriptInterface
    public void setSnoozeConfig(String snoozeConfig) {
        this.snoozeConfigMap = this.cbUtil.storeSnoozeConfigInSharedPref(this.activity.getApplicationContext(), snoozeConfig);
    }

    @JavascriptInterface
    public void dismissPayULoader() {
        if (this.activity != null && !this.activity.isFinishing() && this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog.cancel();
            if (!this.webpageNotFoundError) {
                this.forwardJourneyForChromeLoaderIsComplete = true;
                C0533L.m760v("stag", "Setting forwardJourneyForChromeLoaderIsComplete = " + this.forwardJourneyForChromeLoaderIsComplete);
                startSlowUserWarningTimer();
            }
        }
    }

    protected void startSlowUserWarningTimer() {
        C0533L.m760v("sTag", "Attempting to start slowUserCountDownTimer");
        if (this.slowUserCountDownTimer == null) {
            C0533L.m760v("sTag", "Starting slowUserCountDownTimer");
        }
    }

    protected void dismissSlowUserWarningTimer() {
        if (this.slowUserCountDownTimer != null) {
            C0533L.m760v("sTag", "Shutting down slowUserCountDownTimer");
            this.slowUserCountDownTimer.cancel();
        }
    }

    public void reloadWVUsingJS() {
        this.cbWebView.loadUrl("javascript:window.location.reload(true)");
    }

    public void reloadWVNative() {
        this.cbWebView.reload();
    }

    public void reloadWVUsingJSFromCache() {
        this.cbWebView.loadUrl("javascript:window.location.reload()");
    }

    public void markPreviousTxnAsUserCanceled(String logMessage) {
        new SurePayCancelAsyncTaskHelper(logMessage).execute();
    }

    private void retryPayment(View view) {
        if (view.getId() == C0517R.id.button_retry_transaction) {
            this.snoozeCount++;
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_RETRY_CLICK);
        } else if (view.getId() == C0517R.id.button_retry_anyway) {
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_TXN_PAUSED_USER_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
            addEventAnalytics(CBAnalyticsConstant.SNOOZE_TXN_PAUSED_WINDOW_ACTION, CBAnalyticsConstant.RETRY_ANYWAY_CLICK);
        }
        this.isTransactionStatusReceived = false;
        if (CBUtil.isNetworkAvailable(this.activity.getApplicationContext())) {
            this.cbUtil.clearCookie();
            if (this.cbWebView.getUrl() == null || this.cbWebView.getUrl().contentEquals("https://secure.payu.in/_payment") || this.cbWebView.getUrl().contentEquals(CBConstant.PRODUCTION_PAYMENT_URL_SEAMLESS) || !isUrlWhiteListed(this.cbWebView.getUrl())) {
                if (this.customBrowserConfig.getPostURL().contentEquals("https://secure.payu.in/_payment") || this.customBrowserConfig.getPostURL().contentEquals("https://mobiletest.payu.in/_payment")) {
                    markPreviousTxnAsUserCanceled(this.cbUtil.getLogMessage(this.activity.getApplicationContext(), CBAnalyticsConstant.SURE_PAY_CANCELLED, this.customBrowserConfig.getTransactionID(), "", keyAnalytics, this.customBrowserConfig.getTransactionID(), ""));
                }
                reloadWebView(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData());
            } else {
                reloadWebView();
            }
            dismissSnoozeWindow();
            this.slowUserCountDownTimer = null;
            if (view.getId() == C0517R.id.button_retry_anyway) {
                killSnoozeService();
                ((NotificationManager) this.activity.getSystemService("notification")).cancel(CBConstant.SNOOZE_NOTIFICATION_ID);
                return;
            }
            return;
        }
        addEventAnalytics(CBAnalyticsConstant.SNOOZE_RESUME_URL, CBConstant.MSG_NO_INTERNET);
        Toast.makeText(this.activity.getApplicationContext(), CBConstant.MSG_NO_INTERNET, 0).show();
    }
}
