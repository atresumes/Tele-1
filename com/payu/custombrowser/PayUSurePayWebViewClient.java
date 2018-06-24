package com.payu.custombrowser;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PayUSurePayWebViewClient extends WebViewClient {
    private Bank bank;
    private boolean loadingFinished = true;
    private String mainUrl = "";
    private boolean redirect = false;

    public PayUSurePayWebViewClient(@NonNull Bank bank, @NonNull String merchantKey) {
        this.bank = bank;
        if (Bank.keyAnalytics == null) {
            Bank.keyAnalytics = merchantKey;
        }
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.loadingFinished = false;
        if (this.bank != null) {
            this.bank.onPageStartedWebclient(url);
        }
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!this.redirect) {
            this.loadingFinished = true;
        }
        if (url.equals(this.mainUrl)) {
            this.loadingFinished = true;
            this.redirect = false;
        } else {
            this.redirect = false;
        }
        if (this.bank != null) {
            this.bank.onPageFinishWebclient(url);
        }
    }

    public void onLoadResource(WebView view, String url) {
        if (this.bank != null) {
            this.bank.onLoadResourse(view, url);
        }
        super.onLoadResource(view, url);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (failingUrl != null && this.bank != null && VERSION.SDK_INT < 23) {
            this.bank.onReceivedErrorWebClient(errorCode, description);
        }
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (this.bank != null) {
            this.bank.onReceivedErrorWebClient(error.getErrorCode(), error.getDescription().toString());
        }
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (VERSION.SDK_INT <= 10) {
            handler.proceed();
        } else if (VERSION.SDK_INT < 14) {
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        this.mainUrl = url;
        if (!this.loadingFinished) {
            this.redirect = true;
        }
        this.loadingFinished = false;
        if (this.bank != null) {
            this.bank.onOverrideURL(url);
        }
        return false;
    }
}
