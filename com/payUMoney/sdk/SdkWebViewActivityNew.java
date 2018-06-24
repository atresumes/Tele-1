package com.payUMoney.sdk;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.CustomBrowser;
import com.payu.custombrowser.PayUCustomBrowserCallback;
import com.payu.custombrowser.PayUWebChromeClient;
import com.payu.custombrowser.PayUWebViewClient;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.magicretry.MagicRetryFragment;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkWebViewActivityNew extends AppCompatActivity {
    private static final String TAG = SdkWebViewActivityNew.class.getName();
    public final int RESULT_FAILED = 90;
    private Map<String, String> pMap;
    String userToken = null;
    private boolean viewPortWide;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String merchantKey;
        super.onCreate(savedInstanceState);
        setContentView(C0360R.layout.sdk_activity_web_view_dummy);
        if (getIntent().getExtras().getString(SdkConstants.MERCHANT_KEY) == null) {
            merchantKey = SdkConstants.COULD_NOT_FOUND;
        } else {
            merchantKey = getIntent().getExtras().getString(SdkConstants.MERCHANT_KEY);
        }
        PayUCustomBrowserCallback payUCustomBrowserCallback = new PayUCustomBrowserCallback() {
            private boolean isPostBackParamFallBackRequired;

            public void onPaymentFailure(String payuResponse, String merchantResponse) {
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    Log.i(SdkConstants.TAG, "Failure -- payuResponse" + payuResponse);
                    Log.i(SdkConstants.TAG, "Failure -- merchantResponse" + merchantResponse);
                }
            }

            public void onPaymentTerminate() {
                if (!this.isPostBackParamFallBackRequired) {
                    SdkWebViewActivityNew.this.finish();
                }
            }

            public void onPaymentSuccess(String payuResponse, String merchantResponse) {
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    Log.i(SdkConstants.TAG, "Success -- payuResponse" + payuResponse);
                    Log.i(SdkConstants.TAG, "Success -- merchantResponse" + merchantResponse);
                }
            }

            public void onCBErrorReceived(int code, String errormsg) {
            }

            public void setCBProperties(WebView webview, Bank payUCustomBrowser) {
                webview.setWebChromeClient(new PayUWebChromeClient(payUCustomBrowser));
                webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, merchantKey));
            }

            public void onBackApprove() {
                SdkWebViewActivityNew.this.notifyUserCancelledTransaction();
                Intent intent = new Intent();
                intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
                SdkWebViewActivityNew.this.setResult(0, intent);
                SdkWebViewActivityNew.this.finish();
            }

            public void onBackDismiss() {
                super.onBackDismiss();
            }

            public void onBackButton(Builder alertDialogBuilder) {
                super.onBackButton(alertDialogBuilder);
            }

            public void initializeMagicRetry(Bank payUCustomBrowser, WebView webview, MagicRetryFragment magicRetryFragment) {
                webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, magicRetryFragment, merchantKey));
                payUCustomBrowser.setMagicRetry(new HashMap());
            }
        };
        try {
            JSONObject temp = new JSONObject(getIntent().getStringExtra(SdkConstants.RESULT));
            this.pMap = new HashMap();
            Iterator keys = temp.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                this.pMap.put(key, temp.getString(key));
            }
            this.pMap.put("device_type", "1");
            String txnId = temp.getString("txnid");
            if (txnId == null) {
                txnId = String.valueOf(System.currentTimeMillis());
            }
            CustomBrowserConfig customBrowserConfig = new CustomBrowserConfig(merchantKey, txnId);
            if (getIntent().getStringExtra("mode").equals(SdkConstants.PAYMENT_MODE_NB)) {
                this.viewPortWide = true;
            }
            customBrowserConfig.setViewPortWideEnable(this.viewPortWide);
            SharedPreferences mPref = getSharedPreferences(SdkConstants.SP_SP_NAME, 0);
            String paymentMode = getIntent().getExtras().getString("mode");
            JSONObject userConfigDto = null;
            Boolean oneClickPayment = Boolean.valueOf(false);
            Boolean oneTapFeature = Boolean.valueOf(false);
            if (mPref.contains(SdkConstants.CONFIG_DTO) && mPref.contains(SdkConstants.ONE_TAP_FEATURE) && mPref.getBoolean(SdkConstants.ONE_TAP_FEATURE, false)) {
                userConfigDto = new JSONObject(mPref.getString(SdkConstants.CONFIG_DTO, SdkConstants.XYZ_STRING));
            }
            if (!(userConfigDto == null || !userConfigDto.has(SdkConstants.ONE_CLICK_PAYMENT) || userConfigDto.isNull(SdkConstants.ONE_CLICK_PAYMENT))) {
                oneClickPayment = Boolean.valueOf(userConfigDto.optBoolean(SdkConstants.ONE_CLICK_PAYMENT));
                if (oneClickPayment.booleanValue() && userConfigDto.has(SdkConstants.ONE_TAP_FEATURE) && !userConfigDto.isNull(SdkConstants.ONE_TAP_FEATURE)) {
                    oneTapFeature = Boolean.valueOf(userConfigDto.optBoolean(SdkConstants.ONE_TAP_FEATURE));
                }
            }
            if (!(userConfigDto == null || !userConfigDto.has(SdkConstants.USER_TOKEN) || userConfigDto.isNull(SdkConstants.USER_TOKEN))) {
                this.userToken = userConfigDto.getString(SdkConstants.USER_TOKEN);
            }
            if (paymentMode != null && oneClickPayment.booleanValue()) {
                if (paymentMode.equals("")) {
                    if (getIntent().getExtras().getString(SdkConstants.CARD_HASH_FOR_ONE_CLICK_TXN).equals("0")) {
                        this.pMap.put(SdkConstants.ONE_CLICK_CHECKOUT, "1");
                        if (!(this.userToken == null || this.userToken.isEmpty())) {
                            this.pMap.put(SdkConstants.USER_TOKEN, this.userToken);
                        }
                    } else {
                        this.pMap.put(SdkConstants.CARD_MERCHANT_PARAM, getIntent().getExtras().getString(SdkConstants.CARD_HASH_FOR_ONE_CLICK_TXN));
                    }
                } else if (paymentMode.equals(SdkConstants.PAYMENT_MODE_DC) || paymentMode.equals(SdkConstants.PAYMENT_MODE_CC)) {
                    this.pMap.put(SdkConstants.ONE_CLICK_CHECKOUT, "1");
                    if (!(this.userToken == null || this.userToken.isEmpty())) {
                        this.pMap.put(SdkConstants.USER_TOKEN, this.userToken);
                    }
                }
            }
            if (oneTapFeature.booleanValue()) {
                customBrowserConfig.setAutoApprove(true);
                customBrowserConfig.setAutoSelectOTP(true);
            } else {
                customBrowserConfig.setAutoApprove(false);
                customBrowserConfig.setAutoSelectOTP(false);
            }
            customBrowserConfig.setDisableBackButtonDialog(false);
            customBrowserConfig.setStoreOneClickHash(1);
            customBrowserConfig.setMerchantSMSPermission(true);
            customBrowserConfig.setmagicRetry(true);
            customBrowserConfig.setPostURL(PayUmoneySdkInitilizer.getWebviewRedirectionUrl());
            customBrowserConfig.setPayuPostData(getParameters1(this.pMap));
            new CustomBrowser().addCustomBrowser(this, customBrowserConfig, payUCustomBrowserCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyUserCancelledTransaction() {
        String paymentId = getIntent().getStringExtra(SdkConstants.PAYMENT_ID);
        if (paymentId != null) {
            SdkSession.getInstance(this).notifyUserCancelledTransaction(paymentId, "1");
        }
    }

    private synchronized String getParameters1(Map<String, String> params) {
        String parameters;
        parameters = "";
        Iterator it = params.entrySet().iterator();
        boolean isFirst = true;
        while (it.hasNext()) {
            Entry pair = (Entry) it.next();
            if (isFirst) {
                parameters = parameters.concat(pair.getKey() + "=" + pair.getValue());
            } else {
                parameters = parameters.concat("&" + pair.getKey() + "=" + pair.getValue());
            }
            isFirst = false;
            it.remove();
        }
        return parameters;
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() == 53) {
            if (event.getStatus()) {
                Log.d(TAG, "POST_BACK_PARAM getStatus is true");
            } else {
                Log.d(TAG, "POST_BACK_PARAM getStatus is false");
            }
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
