package com.payu.custombrowser;

import android.app.AlertDialog.Builder;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import com.payu.magicretry.MagicRetryFragment;

public class PayUCustomBrowserCallback {
    private String postData;
    private String postURL;

    public void onPaymentFailure(String payuResult, String merchantResponse) {
    }

    public void onPaymentTerminate() {
    }

    public void onPaymentSuccess(String payuResult, String merchantResponse) {
    }

    public void onCBErrorReceived(int code, String errormsg) {
    }

    public void setCBProperties(@NonNull String postURL, @NonNull String postData) {
        setPostData(postData);
        setPostURL(postURL);
    }

    public void setCBProperties(WebView webview, Bank payUCustomBrowser) {
    }

    public void onBackButton(Builder alertDialogBuilder) {
    }

    public void onBackApprove() {
    }

    public void onBackDismiss() {
    }

    public void initializeMagicRetry(Bank payUCustomBrowser, WebView webview, MagicRetryFragment magicRetryFragment) {
    }

    public String getPostData() {
        return this.postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getPostURL() {
        return this.postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }
}
