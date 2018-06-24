package com.payu.custombrowser;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.payu.custombrowser.analytics.CBAnalytics;
import com.payu.custombrowser.analytics.PayuDeviceAnalytics;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.custombrowser.bean.CustomBrowserData;
import com.payu.custombrowser.util.C0533L;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import com.payu.magicretry.MagicRetryFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import org.json.JSONObject;

public class CustomBrowserMain extends Fragment implements CBConstant {
    public static final boolean DEBUG = false;
    final String CB_URL = CBConstant.PRODUCTION_URL;
    Activity activity;
    protected boolean autoApprove;
    protected boolean autoSelectOtp;
    protected boolean backwardJourneyStarted = false;
    Set<String> backwardJourneyUrls;
    String bankName;
    Bundle bundle;
    FrameLayout cbBaseView;
    boolean cbOldFlow;
    View cbSlideBarView;
    View cbTransparentView;
    CBUtil cbUtil;
    private boolean cbVisibleOnce;
    ProgressBar cbWebPageProgressBar;
    WebView cbWebView;
    int checkForInput;
    CountDownTimer countDownTimer;
    protected CustomBrowserConfig customBrowserConfig;
    Drawable drawable;
    View enterOTPView;
    ArrayList<String> eventArray = new ArrayList();
    String eventRecorded;
    protected boolean firstTouch = false;
    protected boolean forwardJourneyForChromeLoaderIsComplete = false;
    int frameState;
    boolean isSnoozeWindowVisible = false;
    Boolean isSuccessTransaction;
    protected boolean isTxnNBType;
    protected boolean isWebviewReloading;
    int lastProgress;
    View loadingLayout;
    int loading_height;
    CBAnalytics mAnalytics;
    JSONObject mBankJS;
    BroadcastReceiver mBroadcastReceiver;
    JSONObject mJS;
    BroadcastReceiver mReceiver = null;
    MagicRetryFragment magicRetryFragment;
    int maxWebview;
    String merchantResponse;
    boolean merchantSMSPermission;
    int minWebview;
    boolean nbhelpVisible;
    protected String pageType = "";
    protected boolean payuChromeLoaderDisabled = false;
    PayuDeviceAnalytics payuDeviceAnalytics;
    boolean payuPG;
    String payuReponse;
    ProgressDialog progressDialog;
    Set<String> retryUrls;
    Executor serialExecutor;
    AlertDialog snoozeDialog;
    public int snoozeMode = 1;
    int storeOneClickHash;
    protected String timeOfArrival;
    protected String timeOfDeparture;
    protected Timer timerProgress;
    boolean verificationMsgReceived;
    String webviewUrl;

    class C04974 implements OnDismissListener {
        C04974() {
        }

        public void onDismiss(DialogInterface dialog) {
            CustomBrowserMain.this.cbUtil.cancelTimer(CustomBrowserMain.this.timerProgress);
        }
    }

    class C05006 implements Runnable {
        C05006() {
        }

        public void run() {
            if (CustomBrowserMain.this.activity != null && !CustomBrowserMain.this.activity.isFinishing() && CustomBrowserMain.this.isAdded()) {
                if (CustomBrowserMain.this.cbOldFlow) {
                    Intent intent = new Intent();
                    intent.putExtra(CustomBrowserMain.this.getString(C0517R.string.cb_result), CustomBrowserMain.this.merchantResponse);
                    intent.putExtra(CustomBrowserMain.this.getString(C0517R.string.cb_payu_response), CustomBrowserMain.this.payuReponse);
                    if (CustomBrowserMain.this.isSuccessTransaction.booleanValue()) {
                        if (CustomBrowserMain.this.storeOneClickHash == 1) {
                            new StoreMerchantHashTask().execute(new String[]{CustomBrowserMain.this.payuReponse});
                        }
                        CustomBrowserMain.this.activity.setResult(-1, intent);
                    } else {
                        CustomBrowserMain.this.activity.setResult(0, intent);
                    }
                } else if (CustomBrowserMain.this.isSuccessTransaction.booleanValue()) {
                    if (CustomBrowserMain.this.customBrowserConfig.getStoreOneClickHash() == 1) {
                        new StoreMerchantHashTask().execute(new String[]{CustomBrowserMain.this.payuReponse});
                    }
                    if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                        CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onPaymentSuccess(CustomBrowserMain.this.payuReponse, CustomBrowserMain.this.merchantResponse);
                    } else {
                        C0533L.m760v("PayuError", "No PayUCustomBrowserCallback found, please assign a callback: com.payu.custombrowser.PayUCustomBrowserCallback.setPayuCustomBrowserCallback(PayUCustomBrowserCallback payuCustomBrowserCallback)");
                    }
                } else if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                    CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onPaymentFailure(CustomBrowserMain.this.payuReponse, CustomBrowserMain.this.merchantResponse);
                } else {
                    C0533L.m760v("PayuError", "No PayUCustomBrowserCallback found, please assign a callback: com.payu.custombrowser.PayUCustomBrowserCallback.setPayuCustomBrowserCallback(PayUCustomBrowserCallback payuCustomBrowserCallback)");
                }
                CustomBrowserMain.this.activity.finish();
            }
        }
    }

    public class CBMainViewOnTouchListener implements OnTouchListener {
        int height = 0;
        float initialY;
        boolean isTouch = true;

        class C05011 implements Runnable {
            C05011() {
            }

            public void run() {
                CustomBrowserMain.this.cbSlideBarView.setVisibility(8);
            }
        }

        class C05022 implements Runnable {
            C05022() {
            }

            public void run() {
                CBMainViewOnTouchListener.this.isTouch = true;
                CustomBrowserMain.this.frameState = 2;
                if (CustomBrowserMain.this.cbTransparentView != null && CustomBrowserMain.this.activity != null && !CustomBrowserMain.this.activity.isFinishing()) {
                    CustomBrowserMain.this.showTransparentView(CustomBrowserMain.this.cbTransparentView, CustomBrowserMain.this.activity);
                }
            }
        }

        class C05033 implements Runnable {
            C05033() {
            }

            public void run() {
                CustomBrowserMain.this.frameState = 1;
                CustomBrowserMain.this.cbBaseView.setVisibility(8);
                CustomBrowserMain.this.cbSlideBarView.setVisibility(0);
            }
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (CustomBrowserMain.this.nbhelpVisible) {
                return false;
            }
            CustomBrowserMain.this.maximiseWebviewHeight();
            if (!this.isTouch) {
                return false;
            }
            int action = event.getActionMasked();
            if (CustomBrowserMain.this.cbSlideBarView.getVisibility() != 0) {
                switch (action) {
                    case 0:
                        this.initialY = event.getY();
                        break;
                    case 1:
                        float finalY = event.getY();
                        if (this.initialY < finalY && CustomBrowserMain.this.cbBaseView.getVisibility() == 0 && finalY - this.initialY > 0.0f) {
                            this.height = v.getHeight();
                            TranslateAnimation animate = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (v.getHeight() - 30));
                            animate.setDuration(500);
                            animate.setFillBefore(false);
                            animate.setFillEnabled(true);
                            animate.setZAdjustment(1);
                            v.startAnimation(animate);
                            if (CustomBrowserMain.this.cbTransparentView != null) {
                                CustomBrowserMain.this.cbTransparentView.setVisibility(8);
                            }
                            this.isTouch = false;
                            this.isTouch = true;
                            new Handler().postDelayed(new C05033(), 400);
                            break;
                        }
                    case 2:
                    case 3:
                        break;
                    default:
                        break;
                }
            }
            CustomBrowserMain.this.cbSlideBarView.setClickable(false);
            CustomBrowserMain.this.cbSlideBarView.setOnTouchListener(null);
            animate = new TranslateAnimation(0.0f, 0.0f, (float) this.height, 0.0f);
            animate.setDuration(500);
            animate.setFillBefore(true);
            v.startAnimation(animate);
            CustomBrowserMain.this.cbBaseView.setVisibility(0);
            this.isTouch = false;
            new Handler().postDelayed(new C05011(), 20);
            new Handler().postDelayed(new C05022(), 500);
            return true;
        }
    }

    protected boolean checkIfTransactionNBType(String postData) {
        try {
            if (this.cbUtil.getDataFromPostData(this.customBrowserConfig.getPayuPostData(), "pg").equalsIgnoreCase(CBConstant.NB)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void resetAutoSelectOTP() {
        boolean z = true;
        if (this.customBrowserConfig == null || this.customBrowserConfig.getAutoSelectOTP() != 1) {
            z = false;
        }
        this.autoSelectOtp = z;
    }

    public Drawable cbGetDrawable(Context context, int resID) {
        if (VERSION.SDK_INT >= 21) {
            return context.getResources().getDrawable(resID, context.getTheme());
        }
        return context.getResources().getDrawable(resID);
    }

    public static String getSystemCurrentTime() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    void showTransparentView(final View view, Context mContext) {
        if (view != null) {
            view.startAnimation(AnimationUtils.loadAnimation(mContext, C0517R.anim.cb_fade_in));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    view.setVisibility(0);
                }
            }, 500);
        }
    }

    public void checkVisibilityCB(String bank) {
        checkVisibilityCB(bank, false);
    }

    public void checkVisibilityCB(final String bank, final boolean checkSnooze) {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("androidosversion", VERSION.RELEASE + "");
                            obj.put("androidmanufacturer", (Build.MANUFACTURER + "").toLowerCase());
                            obj.put("model", (Build.MODEL + "").toLowerCase());
                            obj.put("merchantid", Bank.keyAnalytics);
                            obj.put(CBConstant.SDK_DETAILS, Bank.sdkVersion);
                            obj.put("cbname", BuildConfig.VERSION_NAME);
                            if (checkSnooze) {
                                CustomBrowserMain.this.cbWebView.loadUrl("javascript:" + (CustomBrowserMain.this.mBankJS.has("set_dynamic_snooze") ? CustomBrowserMain.this.mBankJS.getString("set_dynamic_snooze") + "(" + obj + ")" : ""));
                                return;
                            }
                            obj.put("bankname", bank.toLowerCase());
                            CustomBrowserMain.this.cbWebView.loadUrl("javascript:" + CustomBrowserMain.this.mBankJS.getString("checkVisibilityCBCall") + "(" + obj + ")");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showSoftKeyboard(View editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ((InputMethodManager) this.activity.getSystemService("input_method")).showSoftInput(editText, 2);
    }

    void hideSoftKeyboard() {
        this.activity.getWindow().setSoftInputMode(3);
    }

    protected void initAnalytics(String sdkMerchantKey) {
        this.mAnalytics = CBAnalytics.getInstance(this.activity.getApplicationContext(), "local_cache_analytics");
        deviceAnalytics(sdkMerchantKey, this.activity.getApplicationContext());
    }

    private void deviceAnalytics(String sdkMerchantKey, Context context) {
        JSONObject deviceDetails = new JSONObject();
        try {
            deviceDetails.put(CBAnalyticsConstant.PAYU_ID, this.cbUtil.getCookie(CBConstant.PAYUID, context));
            deviceDetails.put("txnid", Bank.transactionID);
            deviceDetails.put("merchant_key", sdkMerchantKey);
            deviceDetails.put(CBAnalyticsConstant.DEVICE_OS_VERSION, VERSION.SDK_INT + "");
            deviceDetails.put(CBAnalyticsConstant.DEVICE_RESOLUTION, this.cbUtil.getDeviceDensity(this.activity));
            deviceDetails.put(CBAnalyticsConstant.DEVICE_MANUFACTURE, Build.MANUFACTURER);
            deviceDetails.put(CBAnalyticsConstant.DEVICE_MODEL, Build.MODEL);
            deviceDetails.put(CBAnalyticsConstant.NETWORK_INFO, this.cbUtil.getNetworkStatus(this.activity.getApplicationContext()));
            deviceDetails.put(CBAnalyticsConstant.NETWORK_STRENGTH, this.cbUtil.getNetworkStrength(this.activity.getApplicationContext()));
            deviceDetails.put(CBAnalyticsConstant.SDK_VERSION_NAME, Bank.sdkVersion);
            deviceDetails.put(CBAnalyticsConstant.CB_VERSION_NAME, BuildConfig.VERSION_NAME);
            deviceDetails.put("package_name", context.getPackageName());
            CBUtil.setVariableReflection(CBConstant.MAGIC_RETRY_PAKAGE, sdkMerchantKey, CBConstant.ANALYTICS_KEY);
            this.payuDeviceAnalytics = new PayuDeviceAnalytics(this.activity.getApplicationContext(), "cb_local_cache_device");
            this.payuDeviceAnalytics.log(deviceDetails.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addEventAnalytics(String key, String value) {
        if (value != null) {
            try {
                if (!value.trim().equalsIgnoreCase("")) {
                    this.mAnalytics.log(this.cbUtil.getLogMessage(this.activity.getApplicationContext(), key, value.toLowerCase(), this.bankName, Bank.keyAnalytics, Bank.transactionID, this.pageType));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void cbSetBankDrawable(String bankNameImage) {
        if (this.drawable == null && bankNameImage != null) {
            try {
                if (bankNameImage.equalsIgnoreCase("sbinet") || bankNameImage.equalsIgnoreCase("sbi") || bankNameImage.startsWith("sbi_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.sbi);
                } else if (bankNameImage.equalsIgnoreCase("icici") || bankNameImage.equalsIgnoreCase("icicinet") || bankNameImage.equalsIgnoreCase("icicicc") || bankNameImage.startsWith("icici_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.icici);
                } else if (bankNameImage.equalsIgnoreCase("kotaknet") || bankNameImage.equalsIgnoreCase("kotak") || bankNameImage.startsWith("kotak_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.kotak);
                } else if (bankNameImage.equalsIgnoreCase("indus") || bankNameImage.startsWith("indus_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.induslogo);
                } else if (bankNameImage.equalsIgnoreCase("hdfc") || bankNameImage.equalsIgnoreCase("hdfcnet") || bankNameImage.startsWith("hdfc_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.hdfc_bank);
                } else if (bankNameImage.equalsIgnoreCase("yesnet") || bankNameImage.startsWith("yes_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.yesbank_logo);
                } else if (bankNameImage.equalsIgnoreCase("sc") || bankNameImage.startsWith("sc_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.scblogo);
                } else if (bankNameImage.equalsIgnoreCase("axisnet") || bankNameImage.equalsIgnoreCase("axis") || bankNameImage.startsWith("axis_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.axis_logo);
                } else if (bankNameImage.equalsIgnoreCase("amex") || bankNameImage.startsWith("amex_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.cb_amex_logo);
                } else if (bankNameImage.equalsIgnoreCase("hdfcnet") || bankNameImage.equalsIgnoreCase("hdfc") || bankNameImage.startsWith("hdfc_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity, C0517R.drawable.hdfc_bank);
                } else if (bankNameImage.equalsIgnoreCase("ing") || bankNameImage.startsWith("ing_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.ing_logo);
                } else if (bankNameImage.equalsIgnoreCase("idbi") || bankNameImage.startsWith("idbi_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.idbi);
                } else if (bankNameImage.equalsIgnoreCase("citi") || bankNameImage.startsWith("citi_")) {
                    this.drawable = this.cbUtil.getDrawableCB(this.activity.getApplicationContext(), C0517R.drawable.citi);
                } else {
                    this.drawable = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void hideKeyboardForcefully() {
        View view = this.activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) this.activity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void calculateCBHeight(View view) {
        view.measure(-2, -2);
        this.loading_height = view.getMeasuredHeight();
        if (this.maxWebview != 0) {
            this.minWebview = this.maxWebview - this.loading_height;
        }
    }

    void calculateMaximumWebViewHeight() {
        try {
            if (this.maxWebview == 0 && this.bankName != null) {
                this.cbWebView.measure(-1, -1);
                this.cbWebView.requestLayout();
                this.maxWebview = this.cbWebView.getMeasuredHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void maximiseWebviewHeight() {
        if (this.maxWebview == 0) {
            calculateMaximumWebViewHeight();
        }
        if (this.maxWebview != 0) {
            this.cbWebView.getLayoutParams().height = this.maxWebview;
            this.cbWebView.requestLayout();
        }
    }

    void minimizeWebviewHeight() {
        if (this.maxWebview != 0) {
            this.cbWebView.getLayoutParams().height = this.minWebview;
            this.cbWebView.requestLayout();
        }
    }

    private ProgressDialog showProgress(Context context) {
        ProgressDialog progDialog = null;
        if (!(this.activity == null || !isAdded() || context == null || this.activity.isFinishing())) {
            final Drawable[] drawables = new Drawable[]{cbGetDrawable(this.activity.getApplicationContext(), C0517R.drawable.l_icon1), cbGetDrawable(this.activity.getApplicationContext(), C0517R.drawable.l_icon2), cbGetDrawable(this.activity.getApplicationContext(), C0517R.drawable.l_icon3), cbGetDrawable(this.activity.getApplicationContext(), C0517R.drawable.l_icon4)};
            View layout = LayoutInflater.from(context).inflate(C0517R.layout.cb_prog_dialog, null);
            final ImageView imageView = (ImageView) layout.findViewById(C0517R.id.imageView);
            TextView dialogDesc = (TextView) layout.findViewById(C0517R.id.dialog_desc);
            if (this.isWebviewReloading) {
                dialogDesc.setText(this.activity.getText(C0517R.string.cb_resuming_transaction));
                this.isWebviewReloading = false;
            } else {
                dialogDesc.setText(this.activity.getText(C0517R.string.cb_please_wait));
            }
            if (this.progressDialog == null) {
                progDialog = new ProgressDialog(context, C0517R.style.cb_progress_dialog);
            } else {
                progDialog = this.progressDialog;
            }
            this.cbUtil.cancelTimer(this.timerProgress);
            this.timerProgress = new Timer();
            this.timerProgress.scheduleAtFixedRate(new TimerTask() {
                int f925i = -1;

                class C04951 implements Runnable {
                    C04951() {
                    }

                    public void run() {
                        if (CustomBrowserMain.this.activity != null) {
                            C04963 c04963 = C04963.this;
                            c04963.f925i++;
                            if (C04963.this.f925i >= drawables.length) {
                                C04963.this.f925i = 0;
                            }
                            imageView.setImageBitmap(null);
                            imageView.destroyDrawingCache();
                            imageView.refreshDrawableState();
                            imageView.setImageDrawable(drawables[C04963.this.f925i]);
                        }
                    }
                }

                public synchronized void run() {
                    if (CustomBrowserMain.this.activity != null) {
                        CustomBrowserMain.this.activity.runOnUiThread(new C04951());
                    }
                }
            }, 0, 500);
            progDialog.show();
            progDialog.setContentView(layout);
            progDialog.setCancelable(true);
            progDialog.setCanceledOnTouchOutside(false);
            progDialog.setOnDismissListener(new C04974());
        }
        return progDialog;
    }

    void progressBarVisibilityPayuChrome(int visibility, String url) {
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded()) {
            if (visibility == 8 || visibility == 4) {
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
            } else if (visibility == 0 && !this.payuChromeLoaderDisabled && !this.isSnoozeWindowVisible) {
                this.progressDialog = showProgress(this.activity);
            }
        }
    }

    void communicationError() {
        progressBarVisibilityPayuChrome(8, "");
    }

    void startAnimation(int newProgress) {
        if (this.activity != null && !this.activity.isFinishing() && !isRemoving() && isAdded()) {
            if (this.lastProgress > newProgress) {
                this.cbWebPageProgressBar.setProgress(newProgress);
            }
            if (VERSION.SDK_INT >= 11) {
                ObjectAnimator animation = ObjectAnimator.ofInt(this.cbWebPageProgressBar, "progress", new int[]{newProgress});
                animation.setDuration(50);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.start();
            } else {
                if (newProgress <= 10) {
                    newProgress = 10;
                }
                this.cbWebPageProgressBar.setProgress(newProgress);
            }
            this.lastProgress = newProgress;
        }
    }

    void hideCB() {
        maximiseWebviewHeight();
        this.frameState = 1;
        onHelpUnavailable();
    }

    public void registerBroadcast(BroadcastReceiver broadcastReceiver, IntentFilter filter) {
        this.mReceiver = broadcastReceiver;
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterBroadcast(BroadcastReceiver broadcastReceiver) {
        if (this.mReceiver != null) {
            this.activity.unregisterReceiver(broadcastReceiver);
            this.mReceiver = null;
        }
    }

    public void onHelpUnavailable() {
        if (this.activity != null && !this.activity.isFinishing()) {
            this.activity.findViewById(C0517R.id.parent).setVisibility(8);
        }
    }

    public void onBankError() {
        this.activity.findViewById(C0517R.id.parent).setVisibility(8);
    }

    public void onHelpAvailable() {
        this.cbVisibleOnce = true;
        this.activity.findViewById(C0517R.id.parent).setVisibility(0);
    }

    public boolean wasCBVisibleOnce() {
        return this.cbVisibleOnce;
    }

    public boolean isRetryURL(String url) {
        if (this.retryUrls.size() == 0) {
            return url.contains(CBConstant.PAYMENT_OPTION_URL_PROD);
        }
        for (String retryUrl : this.retryUrls) {
            if (url.contains(retryUrl)) {
                return true;
            }
        }
        return false;
    }

    void setUrlString() {
        if (this.mBankJS != null) {
            try {
                StringTokenizer st;
                if (this.mBankJS.has("postPaymentPgUrlList")) {
                    st = new StringTokenizer(this.mBankJS.getString("postPaymentPgUrlList").replace(" ", ""), CBConstant.CB_DELIMITER);
                    while (st.hasMoreTokens()) {
                        this.backwardJourneyUrls.add(st.nextToken());
                    }
                }
                if (this.mBankJS.has("retryList")) {
                    st = new StringTokenizer(this.mBankJS.getString("retryUrlList").replace(" ", ""), CBConstant.CB_DELIMITER);
                    while (st.hasMoreTokens()) {
                        this.retryUrls.add(st.nextToken());
                    }
                }
            } catch (Exception e) {
                communicationError();
                e.printStackTrace();
            }
        }
    }

    void callTimer() {
        this.countDownTimer = new CountDownTimer(5000, 1000) {

            class C04981 implements Runnable {
                C04981() {
                }

                public void run() {
                    if (CustomBrowserMain.this.activity != null && !CustomBrowserMain.this.activity.isFinishing() && CustomBrowserMain.this.isAdded()) {
                        CustomBrowserMain.this.onMerchantUrlFinished();
                    }
                }
            }

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (CustomBrowserMain.this.activity != null) {
                    CustomBrowserMain.this.activity.runOnUiThread(new C04981());
                }
            }
        }.start();
    }

    void onMerchantUrlFinished() {
        if (this.activity != null) {
            this.activity.runOnUiThread(new C05006());
        }
    }

    public void loadUrlWebView(JSONObject mJS, String functName) {
    }

    public void onBackPressed(Builder alertDialog) {
    }

    public void onBackApproved() {
    }

    public void onBackCancelled() {
    }

    protected void cancelTransactionNotification() {
        NotificationManager mNotificationManager = (NotificationManager) this.activity.getSystemService("notification");
        mNotificationManager.cancel(CBConstant.TRANSACTION_STATUS_NOTIFICATION_ID);
        mNotificationManager.cancel(CBConstant.SNOOZE_NOTIFICATION_ID);
    }
}
