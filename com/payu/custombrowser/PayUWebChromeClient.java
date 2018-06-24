package com.payu.custombrowser;

import android.os.Message;
import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class PayUWebChromeClient extends WebChromeClient {
    private Bank mBank;
    private boolean mPageDone = false;

    public PayUWebChromeClient(@NonNull Bank bank) {
        this.mBank = bank;
    }

    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return false;
    }

    public void onProgressChanged(WebView view, int newProgress) {
        if (this.mBank != null) {
            if (!this.mPageDone && newProgress < 100) {
                this.mPageDone = true;
                this.mBank.onPageStarted();
            } else if (newProgress == 100) {
                this.mBank.onPageStarted();
                this.mPageDone = false;
                this.mBank.onPageFinished();
            }
            this.mBank.onProgressChanged(newProgress);
        }
    }
}
