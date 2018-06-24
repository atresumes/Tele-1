package com.payu.magicretry;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.bumptech.glide.load.Key;
import com.payu.magicretry.Helpers.C0536L;
import com.payu.magicretry.Helpers.MRConstant;
import com.payu.magicretry.Helpers.SharedPreferenceUtil;
import com.payu.magicretry.Helpers.Util;
import com.payu.magicretry.WaitingDots.DotsTextView;
import com.payu.magicretry.analytics.MRAnalytics;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class MagicRetryFragment extends Fragment implements OnClickListener {
    public static final String KEY_TXNID = "transaction_id";
    public static final String SP_IS_MR_ENABLED = "MR_ENABLED";
    public static final String SP_MR_FILE_NAME = "MR_SETTINGS";
    public static final String SP_WHITELISTED_URLS = "MR_WHITELISTED_URLS";
    private static String analyticsKey = null;
    private static String bankName = null;
    private static String cbVersion = null;
    private static boolean disableMagicRetry = false;
    private static final String projectToken = "68dbbac2c25bc048154999d13cb77a55";
    private static List<String> whiteListedUrls = new ArrayList();
    private boolean DEBUG = true;
    private ActivityCallback activityCallbackHandler;
    private Context context;
    private boolean errorWasReceived = true;
    private int firstCall;
    private boolean fromOnReceivedError = false;
    private boolean isWhiteListingEnabled = true;
    private MRAnalytics mAnalytics;
    private WebView mWebView;
    private ProgressBar magicProgress;
    private LinearLayout magicRetryLayoutParent;
    private String reloadUrl;
    private ImageView retryButton;
    private String txnID = "";
    private Map<String, String> urlListWithPostData = new HashMap();
    private DotsTextView waitingDots;
    private LinearLayout waitingDotsLayoutParent;

    public interface ActivityCallback {
        void hideMagicRetry();

        void showMagicRetry();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity().getBaseContext();
        this.txnID = getArguments().getString("transaction_id");
        View view = inflater.inflate(C0537R.layout.magicretry_fragment, container, false);
        initViewElements(view);
        List<String> whiteListedUrls = new ArrayList();
        whiteListedUrls.add("https://secure.payu.in/_payment");
        whiteListedUrls.add("https://secure.payu.in/_secure_payment");
        whiteListedUrls.add("https://www.payumoney.com/txn/#/user/");
        whiteListedUrls.add("https://mpi.onlinesbi.com/electraSECURE/vbv/MPIEntry.jsp");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/com.enstage.entransact.servers.AccessControlServerSSL");
        whiteListedUrls.add("https://www.citibank.co.in/acspage/cap_nsapi.so");
        whiteListedUrls.add("https://acs.icicibank.com/acspage/cap");
        whiteListedUrls.add("https://secure.payu.in/_payment");
        whiteListedUrls.add("https://www.citibank.co.in/servlets/TransReq");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/com.enstage.entransact.servers.AccessControlServerSSL");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/jsp/MerchantPost.jsp");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/jsp/SCode.jsp");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/com.enstage.entransact.servers.AccessControlServerSSL");
        whiteListedUrls.add("https://netsafe.hdfcbank.com/ACSWeb/jsp/payerAuthOptions.jsp");
        whiteListedUrls.add("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/AccessControlServer");
        whiteListedUrls.add("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer");
        whiteListedUrls.add("https://www.citibank.co.in/acspage/cap_nsapi.so");
        whiteListedUrls.add("https://acs.icicibank.com/acspage/cap");
        whiteListedUrls.add("https://secureonline.idbibank.com/ACSWeb/EnrollWeb/IDBIBank/server/AccessControlServer");
        whiteListedUrls.add("https://vpos.amxvpos.com/vpcpay");
        if (getActivity() != null) {
            initAnalytics(getActivity());
        }
        return view;
    }

    public void initAnalytics(Activity activity) {
        this.mAnalytics = MRAnalytics.getInstance(activity, "local_cache_analytics_mr");
    }

    private void initViewElements(View view) {
        this.magicProgress = (ProgressBar) view.findViewById(C0537R.id.magic_reload_progress);
        this.retryButton = (ImageView) view.findViewById(C0537R.id.retry_btn);
        if (VERSION.SDK_INT >= 11) {
            this.waitingDots = (DotsTextView) view.findViewById(C0537R.id.waiting_dots);
        }
        this.waitingDotsLayoutParent = (LinearLayout) view.findViewById(C0537R.id.waiting_dots_parent);
        this.magicRetryLayoutParent = (LinearLayout) view.findViewById(C0537R.id.magic_retry_parent);
        this.magicRetryLayoutParent.setVisibility(0);
        this.waitingDotsLayoutParent.setVisibility(8);
        this.retryButton.setOnClickListener(this);
    }

    public void setWebView(WebView wv) {
        this.mWebView = wv;
    }

    public void onClick(View v) {
        if (v.getId() == C0537R.id.retry_btn) {
            performReload();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.activityCallbackHandler = (ActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    private void performReload() {
        Log.v("#### PAYU", "PayUWebViewClient.java Reloading URL: " + this.mWebView.getUrl());
        this.reloadUrl = this.mWebView.getUrl();
        if (this.urlListWithPostData.size() <= 0 || !this.urlListWithPostData.containsKey(this.mWebView.getUrl())) {
            if (Util.isNetworkAvailable(this.context)) {
                this.fromOnReceivedError = false;
                this.mWebView.reload();
                addEventAnalytics(MRConstant.MR_USER_INPUT, MRConstant.CLICK_M_RETRY);
                showMagicReloadProgressDialog();
                return;
            }
            Util.showNetworkNotAvailableError(this.context);
        } else if (Util.isNetworkAvailable(this.context)) {
            this.fromOnReceivedError = false;
            this.mWebView.postUrl(this.mWebView.getUrl(), ((String) this.urlListWithPostData.get(this.mWebView.getUrl())).getBytes());
            addEventAnalytics(MRConstant.MR_USER_INPUT, MRConstant.CLICK_M_RETRY);
            showMagicReloadProgressDialog();
        } else {
            Util.showNetworkNotAvailableError(this.context);
        }
    }

    public void setUrlListWithPostData(Map<String, String> urlListWithPostData) {
        this.urlListWithPostData = urlListWithPostData;
    }

    private void showMagicReloadProgressDialog() {
        this.magicRetryLayoutParent.setVisibility(8);
        this.waitingDotsLayoutParent.setVisibility(0);
        if (VERSION.SDK_INT >= 11) {
            this.waitingDots.showAndPlay();
        } else {
            this.magicProgress.setVisibility(0);
        }
    }

    private void hideMagicReloadProgressDialog() {
        if (isAdded()) {
            if (this.waitingDotsLayoutParent != null) {
                this.waitingDotsLayoutParent.setVisibility(8);
            }
            if (VERSION.SDK_INT >= 11) {
                if (this.waitingDots != null) {
                    this.waitingDots.hideAndStop();
                }
            } else if (this.magicProgress != null) {
                this.magicProgress.setVisibility(4);
            }
            if (this.magicRetryLayoutParent != null) {
                this.magicRetryLayoutParent.setVisibility(0);
            }
        }
    }

    public void onPageStarted(WebView view, String url) {
    }

    public void onPageFinished(WebView webView, String url) {
        if (getActivity() != null && !getActivity().isFinishing() && !this.fromOnReceivedError && this.errorWasReceived && this.reloadUrl != null) {
            this.activityCallbackHandler.hideMagicRetry();
            this.errorWasReceived = true;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onReceivedError(WebView mWebView, String failingUrl) {
        try {
            addEventAnalytics(MRConstant.MR_EURL, URLEncoder.encode(failingUrl, Key.STRING_CHARSET_NAME));
            if (this.firstCall == 0) {
                addEventAnalytics(MRConstant.MR_VERION, BuildConfig.VERSION_NAME);
                this.firstCall++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!disableMagicRetry) {
            C0536L.m769v("#### PAYU", "WebView URL: " + mWebView.getUrl() + " FAILING URL: " + failingUrl);
            hideItems();
            if (failingUrl == null || !isWhiteListedURL(failingUrl)) {
                this.reloadUrl = null;
                return;
            }
            this.fromOnReceivedError = true;
            if (this.activityCallbackHandler != null) {
                this.activityCallbackHandler.showMagicRetry();
            }
            addEventAnalytics(MRConstant.MR_USER_INPUT, MRConstant.SHOW_M_RETRY);
            this.reloadUrl = mWebView.getUrl();
        }
    }

    String getLogMessage(String key, String value) {
        try {
            JSONObject eventAnalytics = new JSONObject();
            eventAnalytics.put("txnid", this.txnID == null ? "" : this.txnID);
            eventAnalytics.put(MRConstant.CB_VERSION_NAME, cbVersion == null ? "" : cbVersion);
            eventAnalytics.put("package_name", getActivity().getPackageName());
            eventAnalytics.put("bank", bankName == null ? "" : bankName);
            eventAnalytics.put("key", key);
            eventAnalytics.put("value", value);
            eventAnalytics.put("merchant_key", analyticsKey);
            return eventAnalytics.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addEventAnalytics(String key, String value) {
        try {
            if (getActivity() != null && isAdded() && !isRemoving() && !isDetached() && this.mAnalytics != null) {
                this.mAnalytics.log(getLogMessage(key, value.toLowerCase()));
            }
        } catch (Exception e) {
        }
    }

    private boolean isWhiteListedURL(String url) {
        if (!this.isWhiteListingEnabled) {
            return true;
        }
        for (String whiteListedUrl : whiteListedUrls) {
            if (url != null && url.contains(whiteListedUrl)) {
                C0536L.m769v("#### PAYU", "WHITELISTED URL FOUND.. SHOWING MAGIC RETRY: " + url);
                return true;
            }
        }
        return false;
    }

    private void hideItems() {
        hideMagicReloadProgressDialog();
    }

    public static void disableMagicRetry(boolean disable) {
        disableMagicRetry = disable;
    }

    public static void setWhitelistedURL(List<String> urls) {
        whiteListedUrls.clear();
        C0536L.m769v("#### PAYU", "MR Cleared whitelisted urls, length: " + whiteListedUrls.size());
        whiteListedUrls.addAll(urls);
        C0536L.m769v("#### PAYU", "MR Updated whitelisted urls, length: " + whiteListedUrls.size());
    }

    public void isWhiteListingEnabled(boolean enable) {
        this.isWhiteListingEnabled = enable;
    }

    public static void processAndAddWhiteListedUrls(String data) {
        if (data != null && !data.equalsIgnoreCase("")) {
            String[] urls = data.split("\\|");
            for (String url : urls) {
                C0536L.m769v("#### PAYU", "Split Url: " + url);
            }
            if (urls != null && urls.length > 0) {
                setWhitelistedURL(Arrays.asList(urls));
            }
            C0536L.m769v("#### PAYU", "Whitelisted URLs from JS: " + data);
        }
    }

    public static void setMRData(String data, Context context) {
        if (data == null) {
            SharedPreferenceUtil.addBooleanToSharedPreference(context, SP_MR_FILE_NAME, SP_IS_MR_ENABLED, false);
            disableMagicRetry(true);
            C0536L.m769v("#### PAYU", "MR SP Setting 1) Disable MR: " + disableMagicRetry);
            SharedPreferenceUtil.addStringToSharedPreference(context, SP_MR_FILE_NAME, SP_WHITELISTED_URLS, "");
            setWhitelistedURL(new ArrayList());
            C0536L.m769v("#### PAYU", "MR SP Setting 2) Clear white listed urls, length: " + whiteListedUrls.size());
        } else {
            SharedPreferenceUtil.addBooleanToSharedPreference(context, SP_MR_FILE_NAME, SP_IS_MR_ENABLED, true);
            disableMagicRetry(false);
            C0536L.m769v("#### PAYU", "MR SP Setting 1) Disable MR: " + disableMagicRetry);
            SharedPreferenceUtil.addStringToSharedPreference(context, SP_MR_FILE_NAME, SP_WHITELISTED_URLS, data);
            processAndAddWhiteListedUrls(data);
            C0536L.m769v("#### PAYU", "MR SP Setting 2) Update white listed urls, length: " + whiteListedUrls.size());
        }
        C0536L.m769v("#### PAYU", "MR DATA UPDATED IN SHARED PREFERENCES");
    }

    public void initMRSettingsFromSharedPreference(Context context) {
        boolean z;
        boolean z2 = true;
        String str = SP_MR_FILE_NAME;
        String str2 = SP_IS_MR_ENABLED;
        if (disableMagicRetry) {
            z = false;
        } else {
            z = true;
        }
        if (SharedPreferenceUtil.getBooleanFromSharedPreference(context, str, str2, z)) {
            z2 = false;
        }
        disableMagicRetry(z2);
        processAndAddWhiteListedUrls(SharedPreferenceUtil.getStringFromSharedPreference(context, SP_MR_FILE_NAME, SP_WHITELISTED_URLS, ""));
    }
}
