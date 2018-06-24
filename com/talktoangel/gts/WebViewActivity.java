package com.talktoangel.gts;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.payu.custombrowser.util.CBConstant;

public class WebViewActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView webView;

    class C05861 extends WebViewClient {
        C05861() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewActivity.this.progressBar.setVisibility(0);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Snackbar.make((View) view, "Oh no! " + description, -1).show();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Snackbar.make((View) view, "SslError! " + error, -1).show();
            handler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewActivity.this.progressBar.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getStringExtra(CBConstant.URL);
        this.webView = (WebView) findViewById(C0585R.id.webView);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progressWeb);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new C05861());
        this.webView.loadUrl(url);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
