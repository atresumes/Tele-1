package com.payu.magicretry;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.payu.magicretry.WebClient.MerchantsWebViewClient;

public class MainActivity extends AppCompatActivity {
    MagicRetryFragment fragment;
    WebView wv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0537R.layout.magicretry_main);
        this.wv = (WebView) findViewById(C0537R.id.wv1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        this.fragment = new MagicRetryFragment();
        fragmentManager.beginTransaction().add(C0537R.id.magic_retry_container, this.fragment, "magicRetry").commit();
        this.wv.setWebChromeClient(new WebChromeClient());
        this.wv.setWebViewClient(new MerchantsWebViewClient(this.fragment));
        this.fragment.setWebView(this.wv);
        this.wv.loadUrl("http://google.com");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0537R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0537R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleFragmentVisibility() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (this.fragment.isVisible()) {
            ft.hide(this.fragment);
        } else {
            ft.show(this.fragment);
        }
    }
}
