package com.payu.magicretry.WebClient;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Message;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.payu.magicretry.Helpers.C0536L;
import com.payu.magicretry.MagicRetryFragment;

public class MagicRetryWebViewClient extends WebViewClient {
    private MagicRetryFragment magicRetryFragment;

    public MagicRetryWebViewClient(MagicRetryFragment fragment) {
        this.magicRetryFragment = fragment;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        C0536L.m769v("#### PAYU", "MagicRetryWebViewClient.java: onPageStarted: URL " + url);
        super.onPageStarted(view, url, favicon);
        if (this.magicRetryFragment != null) {
            this.magicRetryFragment.onPageStarted(view, url);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        C0536L.m769v("#### PAYU", "MagicRetryWebViewClient.java: shouldOverrideUrlLoading: URL " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        C0536L.m769v("#### PAYU", "MagicRetryWebViewClient.java: onPageFinished: URL " + url);
        super.onPageFinished(view, url);
        if (this.magicRetryFragment != null) {
            this.magicRetryFragment.onPageFinished(view, url);
        }
    }

    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        resend.sendToTarget();
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        C0536L.m769v("#### PAYU", "MagicRetryWebViewClient.java: onReceivedError: URL " + view.getUrl());
        if (this.magicRetryFragment != null && request.isForMainFrame()) {
            this.magicRetryFragment.onReceivedError(view, request.getUrl().toString());
        }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        C0536L.m769v("#### PAYU", "MagicRetryWebViewClient.java: onReceivedError: URL " + view.getUrl());
        if (VERSION.SDK_INT < 23 && this.magicRetryFragment != null) {
            this.magicRetryFragment.onReceivedError(view, failingUrl);
        }
    }
}
