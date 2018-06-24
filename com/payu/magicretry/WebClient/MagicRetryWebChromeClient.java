package com.payu.magicretry.WebClient;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MagicRetryWebChromeClient extends WebChromeClient {
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
