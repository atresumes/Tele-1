package com.payu.magicretry.WebClient;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import com.payu.magicretry.MagicRetryFragment;

public class MerchantsWebViewClient extends MagicRetryWebViewClient {
    public MerchantsWebViewClient(MagicRetryFragment fragment) {
        super(fragment);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }
}
