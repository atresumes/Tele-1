package com.payu.custombrowser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.load.Key;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.custombrowser.bean.CustomBrowserData;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import com.payu.custombrowser.util.CBConstant;
import com.payu.custombrowser.util.CBUtil;
import com.payu.magicretry.MagicRetryFragment.ActivityCallback;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.json.JSONObject;

public class CBActivity extends FragmentActivity implements ActivityCallback {
    public static int STATE;
    private AlertDialog backButtonAlertDialog;
    CBUtil cbUtil;
    CustomBrowserConfig customBrowserConfig;
    private Bank payUCustomBrowser;
    private android.support.v7.app.AlertDialog snoozeDialog;

    class C04851 implements OnClickListener {
        C04851() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (!(CustomBrowserData.SINGLETON == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() == null)) {
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onBackApprove();
            }
            CBActivity.this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_OK_CLICK.toLowerCase());
            CBActivity.this.finish();
        }
    }

    class C04862 implements OnClickListener {
        C04862() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            CBActivity.this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.BACK_BUTTON_CANCEL_CLICK.toLowerCase());
            if (CustomBrowserData.SINGLETON != null && CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
                CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onBackDismiss();
            }
        }
    }

    class C04884 implements Runnable {
        C04884() {
        }

        public void run() {
            if (CBActivity.this.snoozeDialog != null && CBActivity.this.snoozeDialog.isShowing()) {
                CBActivity.this.snoozeDialog.cancel();
                CBActivity.this.snoozeDialog.dismiss();
                CBActivity.this.finish();
            }
        }
    }

    class C04895 implements View.OnClickListener {
        C04895() {
        }

        public void onClick(View v) {
            CBActivity.this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_INTERACTION_TIME, CustomBrowserMain.getSystemCurrentTime());
            CBActivity.this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_WINDOW_ACTION, CBAnalyticsConstant.SNOOZE_CANCEL_TRANSACTION_CLICK);
            CBActivity.this.snoozeDialog.dismiss();
            CBActivity.this.snoozeDialog.cancel();
            CBActivity.this.finish();
        }
    }

    class C04906 implements OnDismissListener {
        C04906() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            CBActivity.this.snoozeDialog.dismiss();
            CBActivity.this.snoozeDialog.cancel();
        }
    }

    class C04917 implements Runnable {
        C04917() {
        }

        public void run() {
            if (CBActivity.this.snoozeDialog != null && CBActivity.this.snoozeDialog.isShowing()) {
                CBActivity.this.snoozeDialog.cancel();
                CBActivity.this.snoozeDialog.dismiss();
                CBActivity.this.finish();
            }
        }
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        STATE = 1;
    }

    protected void onPause() {
        super.onPause();
        STATE = 2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(C0517R.layout.cb_payments);
        this.payUCustomBrowser = new Bank();
        this.cbUtil = new CBUtil();
        this.cbUtil.resetPayuID();
        Bundle bundle = new Bundle();
        this.customBrowserConfig = (CustomBrowserConfig) getIntent().getParcelableExtra(CBConstant.CB_CONFIG);
        bundle.putParcelable(CBConstant.CB_CONFIG, this.customBrowserConfig);
        this.payUCustomBrowser.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(C0517R.id.main_frame, this.payUCustomBrowser).commit();
    }

    public void showBackButtonDialog() {
        Builder alertDialog = new Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you really want to cancel the transaction ?");
        alertDialog.setPositiveButton("Ok", new C04851());
        alertDialog.setNegativeButton("Cancel", new C04862());
        this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.USER_INPUT, CBAnalyticsConstant.PAYU_BACK_BUTTON_CLICK.toLowerCase());
        if (!(CustomBrowserData.SINGLETON == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() == null)) {
            CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onBackButton(alertDialog);
        }
        this.backButtonAlertDialog = alertDialog.create();
        alertDialog.show();
    }

    public void onBackPressed() {
        if (this.customBrowserConfig == null || this.customBrowserConfig.getDisableBackButtonDialog() == 1) {
            finish();
        } else {
            showBackButtonDialog();
        }
    }

    public void showMagicRetry() {
        this.payUCustomBrowser.showMagicRetry();
    }

    public void hideMagicRetry() {
        this.payUCustomBrowser.hideMagicRetry();
    }

    public void onDestroy() {
        if (this.backButtonAlertDialog != null && this.backButtonAlertDialog.isShowing()) {
            this.backButtonAlertDialog.dismiss();
            this.backButtonAlertDialog.cancel();
        }
        if (this.snoozeDialog != null && this.snoozeDialog.isShowing()) {
            this.snoozeDialog.dismiss();
            this.snoozeDialog.cancel();
        }
        STATE = 3;
        if (!(this.payUCustomBrowser == null || this.payUCustomBrowser.getSnoozeLoaderView() == null)) {
            this.payUCustomBrowser.getSnoozeLoaderView().cancelAnimation();
        }
        if (CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() != null) {
            CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onPaymentTerminate();
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        mNotificationManager.cancel(CBConstant.SNOOZE_NOTIFICATION_ID);
        mNotificationManager.cancel(63);
        super.onDestroy();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getStringExtra(CBConstant.SENDER).contentEquals(CBConstant.SNOOZE_SERVICE)) {
            this.payUCustomBrowser.killSnoozeService();
            if (intent.getExtras().getBoolean(CBConstant.VERIFICATION_MSG_RECEIVED)) {
                if (this.payUCustomBrowser != null) {
                    this.payUCustomBrowser.dismissSnoozeWindow();
                }
                showTransactionStatusDialog(intent.getExtras().getString(CBConstant.PAYU_RESPONSE), intent);
                return;
            }
            this.customBrowserConfig = (CustomBrowserConfig) intent.getExtras().getParcelable(CBConstant.CB_CONFIG);
            this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_NOTIFICATION_ACTION, CBAnalyticsConstant.SNOOZE_TRANSACTION_RESUMED);
            if (intent.getStringExtra(CBConstant.CURRENT_URL) == null) {
                this.payUCustomBrowser.reloadWebView(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData());
            } else if (intent.getStringExtra(CBConstant.CURRENT_URL).equalsIgnoreCase(this.customBrowserConfig.getPostURL())) {
                if (this.customBrowserConfig.getPostURL().contentEquals("https://secure.payu.in/_payment") || this.customBrowserConfig.getPostURL().contentEquals("https://mobiletest.payu.in/_payment")) {
                    this.payUCustomBrowser.markPreviousTxnAsUserCanceled(this.cbUtil.getLogMessage(getApplicationContext(), CBAnalyticsConstant.SURE_PAY_CANCELLED, this.customBrowserConfig.getTransactionID(), "", Bank.keyAnalytics, this.customBrowserConfig.getTransactionID(), ""));
                }
                this.payUCustomBrowser.reloadWebView(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData());
            } else if (Bank.isUrlWhiteListed(intent.getStringExtra(CBConstant.CURRENT_URL))) {
                this.payUCustomBrowser.reloadWebView(intent.getStringExtra(CBConstant.CURRENT_URL));
            } else {
                if (this.customBrowserConfig.getPostURL().contentEquals("https://secure.payu.in/_payment") || this.customBrowserConfig.getPostURL().contentEquals("https://mobiletest.payu.in/_payment")) {
                    this.payUCustomBrowser.markPreviousTxnAsUserCanceled(this.cbUtil.getLogMessage(getApplicationContext(), CBAnalyticsConstant.SURE_PAY_CANCELLED, this.customBrowserConfig.getTransactionID(), "", Bank.keyAnalytics, this.customBrowserConfig.getTransactionID(), ""));
                }
                this.payUCustomBrowser.reloadWebView(this.customBrowserConfig.getPostURL(), this.customBrowserConfig.getPayuPostData());
            }
        }
    }

    private void showTransactionStatusDialog(String payuResponse, Intent intent) {
        try {
            this.payUCustomBrowser.setTransactionStatusReceived(true);
            final JSONObject jsonObject = new JSONObject(payuResponse.toString());
            if (jsonObject.has(CBConstant.API_STATUS)) {
                View snoozeLayout = getLayoutInflater().inflate(C0517R.layout.cb_layout_snooze, null);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setView(snoozeLayout);
                this.snoozeDialog = builder.create();
                int verifyApiStatus = 0;
                if (jsonObject.has(getString(C0517R.string.cb_snooze_verify_api_status))) {
                    verifyApiStatus = Integer.parseInt(jsonObject.get(getString(C0517R.string.cb_snooze_verify_api_status)).toString());
                }
                if (verifyApiStatus == 1) {
                    this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_NOTIFICATION_ACTION, CBAnalyticsConstant.SUCCESS_SCREEN);
                    Bank bank = this.payUCustomBrowser;
                    if (!Bank.isUrlWhiteListed(this.payUCustomBrowser.getCurrentURL()) || 19 == VERSION.SDK_INT) {
                        this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_TRANSACTION_STATUS_UPDATE, CBAnalyticsConstant.SNOOZE_POST_SURL);
                        if (jsonObject.has(CBConstant.RESPONSE)) {
                            setCheckFURLSURL(jsonObject.getString(CBConstant.RESPONSE), new CBUtil().getDataFromPostData(this.customBrowserConfig.getPayuPostData(), CBConstant.SURL));
                        }
                        snoozeLayout.findViewById(C0517R.id.snooze_status_icon).setVisibility(0);
                        ((TextView) snoozeLayout.findViewById(C0517R.id.snooze_header_txt)).setText(C0517R.string.cb_transaction_sucess);
                        snoozeLayout.findViewById(C0517R.id.text_view_cancel_snooze_window).setVisibility(8);
                        ((TextView) snoozeLayout.findViewById(C0517R.id.text_view_snooze_message)).setText(getString(C0517R.string.cb_transaction_success_msg));
                        snoozeLayout.findViewById(C0517R.id.snooze_loader_view).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.button_snooze_transaction).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.text_view_retry_message_detail).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.button_retry_transaction).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.button_cancel_transaction).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.t_confirm).setVisibility(8);
                        snoozeLayout.findViewById(C0517R.id.t_nconfirm).setVisibility(8);
                        this.snoozeDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialogInterface) {
                                try {
                                    if (!(CustomBrowserData.SINGLETON == null || CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback() == null)) {
                                        CustomBrowserData.SINGLETON.getPayuCustomBrowserCallback().onPaymentSuccess(jsonObject.getString(CBConstant.RESPONSE), "");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                CBActivity.this.snoozeDialog.dismiss();
                                CBActivity.this.snoozeDialog.cancel();
                                CBActivity.this.finish();
                            }
                        });
                        this.snoozeDialog.setCanceledOnTouchOutside(false);
                        this.snoozeDialog.show();
                        new Handler().postDelayed(new C04884(), 5000);
                        return;
                    }
                    this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_TRANSACTION_STATUS_UPDATE, CBAnalyticsConstant.SNOOZE_RELOAD);
                    this.payUCustomBrowser.dismissSnoozeWindow();
                    this.payUCustomBrowser.progressBarVisibilityPayuChrome(8, "");
                    this.payUCustomBrowser.reloadWebView();
                    return;
                }
                this.payUCustomBrowser.addEventAnalytics(CBAnalyticsConstant.SNOOZE_NOTIFICATION_ACTION, CBAnalyticsConstant.TXN_NOT_CONFIRMED);
                snoozeLayout.findViewById(C0517R.id.button_snooze_transaction).setVisibility(0);
                snoozeLayout.findViewById(C0517R.id.snooze_status_icon).setVisibility(0);
                snoozeLayout.findViewById(C0517R.id.text_view_cancel_snooze_window).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.button_snooze_transaction).setVisibility(8);
                ((TextView) snoozeLayout.findViewById(C0517R.id.snooze_header_txt)).setText(C0517R.string.cb_transaction_failed_title);
                ((TextView) snoozeLayout.findViewById(C0517R.id.text_view_snooze_message)).setText(C0517R.string.cb_transaction_failed);
                snoozeLayout.findViewById(C0517R.id.button_retry_transaction).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.button_cancel_transaction).setVisibility(0);
                snoozeLayout.findViewById(C0517R.id.button_snooze_transaction).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.text_view_retry_message_detail).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.text_view_transaction_snoozed_message1).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.text_view_ac_debited_twice).setVisibility(8);
                snoozeLayout.findViewById(C0517R.id.button_cancel_transaction).setOnClickListener(new C04895());
                this.snoozeDialog.setOnDismissListener(new C04906());
                this.snoozeDialog.setCanceledOnTouchOutside(false);
                this.snoozeDialog.show();
                new Handler().postDelayed(new C04917(), 5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCheckFURLSURL(final String postData, final String surl) {
        new Thread(new Runnable() {
            public void run() {
                String responseStringBuffer = "";
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(URLDecoder.decode(surl, Key.STRING_CHARSET_NAME)).openConnection();
                    String post = postData;
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", CBConstant.HTTP_URLENCODED);
                    conn.setRequestProperty("Content-Length", String.valueOf(post.length()));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(post.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
