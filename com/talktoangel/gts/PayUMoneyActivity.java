package com.talktoangel.gts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBConstant;
import com.talktoangel.gts.utils.SessionManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayUMoneyActivity extends Activity {
    String amount = "1";
    String base_url = "https://secure.payu.in/_payment";
    String emailId = "accounts@talktoangel.com";
    String firstName = "Talk to Angel";
    Handler mHandler = new Handler();
    String merchant_key = "wY51pcD9";
    String productInfo = "";
    String salt = "rJsMQBr87j";
    String txnid = ("TTA" + System.currentTimeMillis());
    WebView webView;

    class C05831 extends WebViewClient {
        C05831() {
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(PayUMoneyActivity.this.getApplicationContext(), "Oh no! " + description, 0).show();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Toast.makeText(PayUMoneyActivity.this.getApplicationContext(), "SslError! " + error, 0).show();
            handler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("TAG", url);
            if (url.contains(SdkConstants.SUCCESS_STRING)) {
                PayUMoneyActivity.this.setResult(-1, new Intent());
                PayUMoneyActivity.this.finish();
            } else {
                Toast.makeText(PayUMoneyActivity.this.getApplicationContext(), url, 0).show();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class PayUJavaScriptInterface {
        Context mContext;

        class C05841 implements Runnable {
            C05841() {
            }

            public void run() {
                PayUMoneyActivity.this.mHandler = null;
                Toast.makeText(PayUMoneyActivity.this.getApplicationContext(), "Success", 0).show();
            }
        }

        PayUJavaScriptInterface(Context c) {
            this.mContext = c;
        }

        public void success(long id, String paymentId) {
            PayUMoneyActivity.this.mHandler.post(new C05841());
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.txnid = getIntent().getStringExtra("transactionId");
        this.amount = getIntent().getStringExtra(SdkConstants.AMOUNT);
        getWindow().requestFeature(2);
        this.webView = new WebView(this);
        setContentView(this.webView);
        JSONObject productInfoObj = new JSONObject();
        JSONArray productPartsArr = new JSONArray();
        JSONObject productPartsObj1 = new JSONObject();
        JSONObject paymentIdenfierParent = new JSONObject();
        JSONArray paymentIdentifiersArr = new JSONArray();
        JSONObject paymentPartsObj1 = new JSONObject();
        JSONObject paymentPartsObj2 = new JSONObject();
        try {
            productPartsObj1.put("name", "devendra");
            productPartsObj1.put("description", "test amount");
            productPartsObj1.put("value", "1000");
            productPartsObj1.put("isRequired", "true");
            productPartsObj1.put("settlementEvent", "EmailConfirmation");
            productPartsArr.put(productPartsObj1);
            productInfoObj.put(SdkConstants.PAYMENT_PARTS_STRING, productPartsArr);
            paymentPartsObj1.put("field", "CompletionDate");
            paymentPartsObj1.put("value", "03/07/2017");
            paymentIdentifiersArr.put(paymentPartsObj1);
            paymentPartsObj2.put("field", "TxnId");
            paymentPartsObj2.put("value", this.txnid);
            paymentIdentifiersArr.put(paymentPartsObj2);
            paymentIdenfierParent.put(SdkConstants.PAYMENT_IDENTIFIERS_STRING, paymentIdentifiersArr);
            productInfoObj.put("", paymentIdenfierParent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.productInfo = productInfoObj.toString();
        Log.e("TAG", productInfoObj.toString());
        this.txnid = hashCal("SHA-256", Integer.toString(new Random().nextInt()) + (System.currentTimeMillis() / 1000)).substring(0, 20);
        String hash = hashCal("SHA-512", this.merchant_key + "|" + this.txnid + "|" + this.amount + "|" + this.productInfo + "|" + this.firstName + "|" + this.emailId + "|||||||||||" + this.salt);
        this.webView.setWebViewClient(new C05831());
        this.webView.setVisibility(0);
        this.webView.getSettings().setCacheMode(-1);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.clearHistory();
        this.webView.clearCache(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), SdkConstants.SP_SP_NAME);
        Map<String, String> mapParams = new HashMap();
        mapParams.put("key", this.merchant_key);
        mapParams.put(SdkConstants.HASH, hash);
        mapParams.put("txnid", this.txnid);
        mapParams.put("service_provider", "payu_paisa");
        mapParams.put(SdkConstants.AMOUNT, this.amount);
        mapParams.put(SdkConstants.FIRST_NAME_STRING, this.firstName);
        mapParams.put("email", this.emailId);
        mapParams.put("phone", "9810636188");
        mapParams.put(SdkConstants.PRODUCT_INFO_STRING, this.productInfo);
        mapParams.put(CBConstant.SURL, "https://www.payumoney.com/mobileapp/PayUmoney/success.php");
        mapParams.put(CBConstant.FURL, "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        mapParams.put("lastname", "Raghuwanshi");
        mapParams.put(SessionManager.KEY_ADDRESS1, "");
        mapParams.put("address2", "");
        mapParams.put("city", "");
        mapParams.put("state", "");
        mapParams.put(SdkConstants.COUNTRY, "");
        mapParams.put(SdkConstants.ZIPCODE, "");
        mapParams.put(SdkConstants.UDF1, "");
        mapParams.put(SdkConstants.UDF2, "");
        mapParams.put(SdkConstants.UDF3, "");
        mapParams.put(SdkConstants.UDF4, "");
        mapParams.put(SdkConstants.UDF5, "");
        webView_ClientPost(this.webView, this.base_url, mapParams.entrySet());
    }

    public void webView_ClientPost(WebView webView, String url, Collection<Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", new Object[]{url, "post"}));
        for (Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", new Object[]{item.getKey(), item.getValue()}));
        }
        sb.append("</form></body></html>");
        Log.e("tag", "webView_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            for (byte aMessageDigest : algorithm.digest()) {
                String hex = Integer.toHexString(aMessageDigest & 255);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
