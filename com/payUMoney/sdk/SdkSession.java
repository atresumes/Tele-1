package com.payUMoney.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils.Keys;
import com.payUMoney.sdk.walledSdk.WalletSdkLoginSignUpActivity;
import com.payu.custombrowser.util.CBConstant;
import de.greenrobot.event.EventBus;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkSession {
    private static SdkSession INSTANCE = null;
    static final Map<PaymentMode, String> PAYMENT_MODE_TITLES = new HashMap();
    public static final String TAG = SdkSession.class.getSimpleName();
    private static String clientId;
    private static String merchantKey;
    private static String merchantSalt;
    private static String merchantTxnId;
    Long diff = null;
    Long end = null;
    private final EventBus eventBus = EventBus.getDefault();
    private String guestEmail = "";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private String loginMode = "";
    private final Context mContext;
    private boolean mIsLogOutCall = false;
    private RequestQueue mRequestQueue;
    private final SessionData mSessionData = new SessionData();
    Long start = null;
    public double wallet_points = 0.0d;

    public enum Method {
        POST,
        GET,
        DELETE
    }

    public enum PaymentMode {
        CC,
        DC,
        NB,
        EMI,
        PAYU_MONEY,
        STORED_CARDS,
        CASH
    }

    private class SessionData {
        private String revisedCashbackReceivedStatus = "0";
        private String token = null;

        public SessionData() {
            reset();
        }

        public void setrevisedCashbackReceivedStatus(String s) {
            this.revisedCashbackReceivedStatus = s;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void reset() {
            this.token = null;
        }
    }

    public interface Task {
        void onError(Throwable th);

        void onProgress(int i);

        void onSuccess(String str);

        void onSuccess(JSONObject jSONObject);
    }

    class C08881 implements Task {
        C08881() {
        }

        public void onSuccess(JSONObject jsonObject) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(39, true, (Object) jsonObject));
        }

        public void onSuccess(String response) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(39, false));
        }

        public void onError(Throwable throwable) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(39, false, null));
        }

        public void onProgress(int percent) {
        }
    }

    class C08892 implements Task {
        C08892() {
        }

        public void onSuccess(JSONObject jsonObject) {
            try {
                if (jsonObject.get("status") == null || jsonObject.getString("status").equals(SdkConstants.NULL_STRING)) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(51, false, (Object) jsonObject));
                } else {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(51, true, (Object) jsonObject));
                }
            } catch (JSONException e) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(51, false));
            }
        }

        public void onSuccess(String response) {
        }

        public void onError(Throwable throwable) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(51, false, (Object) "An error occurred while verifying your OTP. Please generate again."));
        }

        public void onProgress(int percent) {
        }
    }

    class C08903 implements Task {
        C08903() {
        }

        public void onSuccess(JSONObject jsonObject) {
            try {
                if (jsonObject.get("status") == null || jsonObject.getString("status").equals(SdkConstants.NULL_STRING)) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(50, false, jsonObject.getString("message")));
                } else {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(50, true, (Object) jsonObject));
                }
            } catch (JSONException e) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(50, false));
            }
        }

        public void onSuccess(String response) {
        }

        public void onError(Throwable throwable) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(50, false, (Object) "An error occurred while trying to sign you up. Please try again later."));
        }

        public void onProgress(int percent) {
        }
    }

    class C08914 implements Task {
        C08914() {
        }

        public void onSuccess(JSONObject jsonObject) {
            try {
                if (jsonObject.has(SdkConstants.RESULT) && !jsonObject.isNull(SdkConstants.RESULT)) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(40, true, jsonObject.getJSONObject(SdkConstants.RESULT)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void onSuccess(String response) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(40, false));
        }

        public void onError(Throwable throwable) {
            SdkSession.this.eventBus.post(new SdkCobbocEvent(40, false, null));
        }

        public void onProgress(int percent) {
        }
    }

    static {
        PAYMENT_MODE_TITLES.put(PaymentMode.CC, "Credit Card");
        PAYMENT_MODE_TITLES.put(PaymentMode.DC, "Debit Card");
        PAYMENT_MODE_TITLES.put(PaymentMode.NB, "Net Banking");
        PAYMENT_MODE_TITLES.put(PaymentMode.EMI, "EMI");
        PAYMENT_MODE_TITLES.put(PaymentMode.PAYU_MONEY, SdkConstants.SP_SP_NAME);
        PAYMENT_MODE_TITLES.put(PaymentMode.STORED_CARDS, "Stored Cards");
        PAYMENT_MODE_TITLES.put(PaymentMode.CASH, "Cash Card");
    }

    public String getLoginMode() {
        return this.loginMode;
    }

    public String getGuestEmail() {
        return this.guestEmail;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    private SdkSession(Context context) {
        this.mContext = context;
        clientId = null;
        this.mIsLogOutCall = false;
        String mToken = SharedPrefsUtils.getStringPreference(this.mContext, "access_token");
        if (mToken != null) {
            this.mSessionData.setToken(mToken);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.equals("/payuPaisa/up.php")) {
            return SdkConstants.BASE_URL_IMAGE + relativeUrl;
        }
        return SdkConstants.BASE_URL + relativeUrl;
    }

    public static synchronized SdkSession getInstance(Context context) {
        SdkSession sdkSession;
        synchronized (SdkSession.class) {
            if (INSTANCE == null) {
                INSTANCE = new SdkSession(context);
            }
            sdkSession = INSTANCE;
        }
        return sdkSession;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(context);
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TextUtils.isEmpty(TAG) ? TAG : TAG);
        getRequestQueue(this.mContext).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(tag);
        }
    }

    public static synchronized SdkSession getInstanceForService() {
        SdkSession sdkSession;
        synchronized (SdkSession.class) {
            sdkSession = INSTANCE;
        }
        return sdkSession;
    }

    public static synchronized SdkSession createNewInstance(Context context) {
        SdkSession sdkSession;
        synchronized (SdkSession.class) {
            INSTANCE = null;
            INSTANCE = new SdkSession(context);
            sdkSession = INSTANCE;
        }
        return sdkSession;
    }

    public static void startPaymentProcess(Activity mActivity, HashMap<String, String> userParams) {
        if (SdkConstants.WALLET_SDK.booleanValue()) {
            merchantKey = (String) userParams.get("key");
            merchantSalt = (String) userParams.get(SdkConstants.SALT);
            merchantTxnId = (String) userParams.get(SdkConstants.MERCHANT_TXNID);
            clientId = (String) userParams.get(SdkConstants.CLIENT_ID);
            Intent intent = new Intent(mActivity, WalletSdkLoginSignUpActivity.class);
            intent.putExtra(SdkConstants.PARAMS, userParams);
            mActivity.startActivityForResult(intent, 1001);
            return;
        }
        intent = new Intent(mActivity, SdkHomeActivityNew.class);
        if (!(userParams.containsKey(SdkConstants.PAYUMONEY_APP) || userParams.containsKey(SdkConstants.PAYUBIZZ_APP))) {
            if (userParams.get(SdkConstants.HASH) == null || ((String) userParams.get(SdkConstants.HASH)).equals("")) {
                throw new RuntimeException("Hash is  missing");
            }
            intent.putExtra(SdkConstants.HASH, (String) userParams.get(SdkConstants.HASH));
            if (SdkConstants.WALLET_SDK.booleanValue()) {
                if (userParams.get(SdkConstants.MERCHANT_TXNID) == null || ((String) userParams.get(SdkConstants.MERCHANT_TXNID)).equals("")) {
                    throw new RuntimeException("TxnId Id missing");
                }
                intent.putExtra(SdkConstants.MERCHANT_TXNID, (String) userParams.get(SdkConstants.MERCHANT_TXNID));
            } else if (userParams.get("txnid") == null || ((String) userParams.get("txnid")).equals("")) {
                throw new RuntimeException("TxnId Id missing");
            } else {
                intent.putExtra("txnid", (String) userParams.get("txnid"));
            }
            if (userParams.get(SdkConstants.AMOUNT) == null || ((String) userParams.get(SdkConstants.AMOUNT)).equals("")) {
                throw new RuntimeException("Amount is missing");
            }
            intent.putExtra(SdkConstants.AMOUNT, (String) userParams.get(SdkConstants.AMOUNT));
            if (userParams.get(SdkConstants.SURL) == null || ((String) userParams.get(SdkConstants.SURL)).equals("")) {
                throw new RuntimeException("Surl is missing");
            }
            intent.putExtra(SdkConstants.SURL, (String) userParams.get(SdkConstants.SURL));
            if (userParams.get(SdkConstants.FURL) == null || ((String) userParams.get(SdkConstants.FURL)).equals("")) {
                throw new RuntimeException("Furl is missing");
            }
            intent.putExtra(SdkConstants.FURL, (String) userParams.get(SdkConstants.FURL));
            if (userParams.get(SdkConstants.PRODUCT_INFO) == null || ((String) userParams.get(SdkConstants.PRODUCT_INFO)).equals("")) {
                throw new RuntimeException("Product info is missing");
            }
            intent.putExtra(SdkConstants.PRODUCT_INFO, (String) userParams.get(SdkConstants.PRODUCT_INFO));
            if (userParams.get(SdkConstants.FIRSTNAME) == null || ((String) userParams.get(SdkConstants.FIRSTNAME)).equals("")) {
                throw new RuntimeException("Firstname is missing");
            }
            intent.putExtra(SdkConstants.FIRSTNAME, (String) userParams.get(SdkConstants.FIRSTNAME));
            if (userParams.get("email") == null || ((String) userParams.get("email")).equals("")) {
                throw new RuntimeException("Email is missing");
            }
            intent.putExtra("email", (String) userParams.get("email"));
            if (((String) userParams.get("phone")).equals("") || userParams.get("phone") == null) {
                throw new RuntimeException("Phone is missing");
            }
            intent.putExtra("phone", (String) userParams.get("phone"));
        }
        intent.putExtra(SdkConstants.PARAMS, userParams);
        mActivity.startActivityForResult(intent, 1001);
    }

    public void fetchMechantParams(String merchantId) {
        Map<String, String> p = new HashMap();
        p.put("merchantId", merchantId);
        postFetch(String.format("/auth/app/op/merchant/LoginParams" + getParameters(p), new Object[]{merchantId}), null, new C08881(), 0);
    }

    private Map<String, String> getUserSessionInfo() {
        Map<String, String> customHeaders = new HashMap();
        customHeaders.put(SdkConstants.USER_SESSION_COOKIE, SdkHelper.getUserSessionId(this.mContext));
        customHeaders.put(SdkConstants.CUSTOM_BROWSER_PROPERTY, SdkHelper.getDeviceCustomPropertyJsonString(this.mContext));
        customHeaders.put(SdkConstants.USER_SESSION_COOKIE_PAGE_URL, SharedPrefsUtils.getStringPreference(this.mContext, SdkConstants.USER_SESSION_COOKIE_PAGE_URL));
        if (SdkHelper.isUpdateSessionRequired(this.mContext)) {
            customHeaders.put(SdkConstants.USER_SESSION_UPDATE, "1");
        }
        return customHeaders;
    }

    public void createWallet(String email, String phone, String OTP) {
        Map<String, String> p = new HashMap();
        p.put("userName", email);
        p.put("name", email);
        p.put("mobile", phone);
        p.put(SdkConstants.OTP_STRING, OTP);
        postFetch("/vault/app/createWallet", p, new C08892(), 1);
    }

    public void sendMobileVerificationCodeForWalletCreation(String phone) {
        Map<String, String> p = new HashMap();
        p.put("mobile", phone);
        p.put(SdkConstants.OTP_TYPE, "R");
        postFetch("/auth/app/generateWalletCode", p, new C08903(), 1);
    }

    public void fetchUserParams(String paymentId) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID, paymentId);
        postFetch("/payment/app/fetchPaymentUserData", p, new C08914(), 1);
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(key.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "").trim(), 0));
        SdkLogger.m14d(SdkConstants.TAG, new String(spec.getEncoded()));
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public void reset() {
        this.mSessionData.reset();
    }

    public void postFetch(String url, Map<String, String> params, Task task, int method) {
        postFetch(url, params, null, task, method);
    }

    public void postFetch(String url, Map<String, String> params, Map<String, String> customHeader, final Task task, int method) {
        if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
            SdkLogger.m14d(SdkConstants.TAG, "SdkSession.postFetch: " + url + " " + params + " " + method);
        }
        String absoluteUrl = getAbsoluteUrl(url);
        final String str = url;
        final Map<String, String> map = params;
        final int i = method;
        final Task task2 = task;
        C08925 c08925 = new Listener<String>() {
            public void onResponse(String response) {
                SdkSession.this.diff = Long.valueOf(System.currentTimeMillis() - SdkSession.this.start.longValue());
                SdkLogger.m20i("Difference ", "URL=" + str + "Time=" + SdkSession.this.diff);
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    SdkLogger.m14d(SdkConstants.TAG, "SdkSession.postFetch.onSuccess: " + str + " " + map + " " + i + ": " + response);
                }
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("error")) {
                        onFailure(object.getString("error"), new Throwable(object.getString("error")));
                    } else {
                        SdkSession.this.runSuccessOnHandlerThread(task2, object);
                    }
                } catch (JSONException e) {
                    onFailure(e.getMessage(), e);
                }
            }

            public void onFailure(String msg, Throwable e) {
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    Log.e(SdkConstants.TAG, "Session...new JsonHttpResponseHandler() {...}.onFailure: " + e.getMessage() + " " + msg);
                }
                if (msg.contains("401")) {
                    if (!SdkConstants.WALLET_SDK.booleanValue()) {
                        SdkSession.this.logout(SdkConstants.LOGOUT_FORCE);
                        SdkSession.this.cancelPendingRequests(SdkSession.TAG);
                    } else if (SdkSession.this.mIsLogOutCall) {
                        SdkSession.this.mIsLogOutCall = false;
                    } else {
                        SdkSession.this.logout();
                    }
                }
                SdkSession.this.runErrorOnHandlerThread(task2, e);
            }
        };
        final Map<String, String> map2 = params;
        final Map<String, String> map3 = customHeader;
        StringRequest c11497 = new StringRequest(method, absoluteUrl, c08925, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    Log.e(SdkConstants.TAG, "Session...new JsonHttpResponseHandler() {...}.onFailure: " + error.getMessage());
                }
                if (!(error == null || error.networkResponse == null || error.networkResponse.statusCode != 401)) {
                    if (!SdkConstants.WALLET_SDK.booleanValue()) {
                        SdkSession.this.logout(SdkConstants.LOGOUT_FORCE);
                    } else if (SdkSession.this.mIsLogOutCall) {
                        SdkSession.this.mIsLogOutCall = false;
                    } else {
                        SdkSession.this.logout();
                    }
                }
                SdkSession.this.runErrorOnHandlerThread(task, error);
            }
        }) {
            protected Map<String, String> getParams() {
                if (SdkConstants.WALLET_SDK.booleanValue()) {
                    map2.put(SdkConstants.CLIENT_ID, SdkSession.clientId);
                    map2.put(SdkConstants.IS_MOBILE, "1");
                }
                return map2;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                if (!(map3 == null || map3.isEmpty())) {
                    params.putAll(map3);
                }
                params.putAll(SdkSession.this.getUserSessionInfo());
                params.put(SdkConstants.USER_AGENT, "PayUMoneyAPP");
                if (SdkSession.this.getToken() != null) {
                    params.put("Authorization", "Bearer " + SdkSession.this.getToken());
                } else {
                    params.put("Accept", "*/*;");
                }
                return params;
            }

            public String getBodyContentType() {
                if (SdkSession.this.getToken() == null) {
                    return CBConstant.HTTP_URLENCODED;
                }
                return super.getBodyContentType();
            }
        };
        c11497.setShouldCache(false);
        c11497.setRetryPolicy(new DefaultRetryPolicy(30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(c11497);
        this.start = Long.valueOf(System.currentTimeMillis());
    }

    private String getParameters(Map<String, String> params) {
        String parameters = "?";
        Iterator it = params.entrySet().iterator();
        boolean isFirst = true;
        while (it.hasNext()) {
            Entry pair = (Entry) it.next();
            if (isFirst) {
                parameters = parameters.concat(pair.getKey() + "=" + pair.getValue());
            } else {
                parameters = parameters.concat("&" + pair.getKey() + "=" + pair.getValue());
            }
            isFirst = false;
            it.remove();
        }
        return parameters;
    }

    private void runErrorOnHandlerThread(final Task task, final Throwable e) {
        if ((e instanceof ConnectTimeoutException) || (e instanceof SocketTimeoutException)) {
            final Throwable x = new Throwable("time out error");
            this.handler.post(new Runnable() {
                public void run() {
                    task.onError(x);
                }
            });
            return;
        }
        this.handler.post(new Runnable() {
            public void run() {
                task.onError(e);
            }
        });
    }

    private void runSuccessOnHandlerThread(final Task task, final JSONObject jsonObject) {
        this.handler.post(new Runnable() {
            public void run() {
                task.onSuccess(jsonObject);
            }
        });
    }

    private void runSuccessOnHandlerThread(final Task task, final String response) {
        this.handler.post(new Runnable() {
            public void run() {
                task.onSuccess(response);
            }
        });
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void setToken(String token) {
        this.mSessionData.setToken(token);
    }

    public void create(final String username, String password) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.GRANT_TYPE, SdkConstants.PASSWORD);
        p.put(SdkConstants.CLIENT_ID, "10182");
        p.put(SdkConstants.USER_NAME, username);
        p.put(SdkConstants.PASSWORD, password);
        postFetch("/auth/oauth/token", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (!jsonObject.has("access_token") || jsonObject.isNull("access_token")) {
                        SdkLogger.m14d(SdkConstants.TAG, "Token Not Found");
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(1, false, (Object) "Error"));
                        return;
                    }
                    String token = jsonObject.getString("access_token");
                    SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "access_token", token);
                    SdkSession.getInstance(SdkSession.this.mContext).setToken(token);
                    SdkHelper.resetSessionUpdateTimeStamp(SdkSession.this.mContext);
                    SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "email", username);
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(1, true, (Object) jsonObject));
                } catch (Throwable e) {
                    if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                        SdkLogger.m14d(SdkConstants.TAG, e.getMessage());
                    }
                    onError(e);
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable e) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(1, false, e.getMessage()));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void generateAndSendOtp(String userEmail) {
        Map<String, String> p = new HashMap();
        p.put("requestType", Event.LOGIN);
        p.put("userName", userEmail);
        postFetch("/auth/app/op/generateAndSendOTP", p, new Task() {
            public void onSuccess(JSONObject object) {
                if (object.has("status")) {
                    String status = object.optString("status");
                    if (status == null || status.equals("-1")) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(38, false, (Object) object));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(38, true, (Object) object));
                    }
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(38, false));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void createPayment(HashMap<String, String> params) {
        Map p = new HashMap();
        p.put("key", params.get("key"));
        p.put(SdkConstants.AMOUNT, params.get(SdkConstants.AMOUNT));
        p.put("txnid", params.get("txnid"));
        p.put(SdkConstants.PRODUCT_INFO_STRING, params.get(SdkConstants.PRODUCT_INFO));
        p.put(SdkConstants.FIRST_NAME_STRING, params.get(SdkConstants.FIRSTNAME));
        p.put("email", params.get("email"));
        p.put(SdkConstants.UDF1, params.get(SdkConstants.UDF1));
        p.put(SdkConstants.UDF2, params.get(SdkConstants.UDF2));
        p.put(SdkConstants.UDF3, params.get(SdkConstants.UDF3));
        p.put(SdkConstants.UDF4, params.get(SdkConstants.UDF4));
        p.put(SdkConstants.UDF5, params.get(SdkConstants.UDF5));
        p.put(SdkConstants.HASH, params.get(SdkConstants.HASH));
        p.put(SdkConstants.PAYMENT_IDENTIFIERS_STRING, "[]");
        p.put("purchaseFrom", "merchant-app");
        p.put(SdkConstants.TRANSACTION_DETAILS, "{\"surl\": \"" + ((String) params.get(SdkConstants.SURL)) + "\", \"furl\": \"" + ((String) params.get(SdkConstants.FURL)) + "\"}");
        p.put(SdkConstants.PAYMENT_PARTS_STRING, "[]");
        p.put(SdkConstants.DEVICE_ID, params.get(SdkConstants.DEVICE_ID));
        p.put(SdkConstants.IS_MOBILE, "1");
        if (this.loginMode.equals("guestLogin")) {
            p.put("guestCheckout", "true");
        }
        postFetch("/payment/app/payment/addSdkPayment", p, new Task() {
            public void onSuccess(JSONObject object) {
                try {
                    if (object.has(SdkConstants.RESULT) && !object.isNull(SdkConstants.RESULT)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(5, true, object.getJSONObject(SdkConstants.RESULT)));
                    } else if (!object.has("message") || object.isNull("message")) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(5, false));
                        SdkLogger.m15e(object.toString());
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(5, false, object.toString()));
                    }
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(5, false));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(5, false));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(5, false));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void updateTransactionDetails(String paymentId, String guestEmail) {
        Map p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID, paymentId);
        JSONObject tmp = new JSONObject();
        try {
            tmp.put("guestemail", guestEmail);
            tmp.put("state", SdkConstants.NULL_STRING);
            tmp.put(SdkConstants.COUNTRY, SdkConstants.NULL_STRING);
            tmp.put("addressId", SdkConstants.NULL_STRING);
            tmp.put(SdkConstants.ADDRESS_LINE, SdkConstants.NULL_STRING);
            tmp.put("entityId", SdkConstants.NULL_STRING);
            tmp.put("city", SdkConstants.NULL_STRING);
            tmp.put(SdkConstants.ZIPCODE, SdkConstants.NULL_STRING);
            tmp.put("entityType", SdkConstants.NULL_STRING);
            tmp.put("city", SdkConstants.NULL_STRING);
            tmp.put("city", SdkConstants.NULL_STRING);
            tmp.put("city", SdkConstants.NULL_STRING);
        } catch (JSONException e) {
        }
        p.put(SdkConstants.TRANSACTION_DETAILS, tmp.toString());
        postFetch("/payment/app/op/payment/updateTxnDetails", p, new Task() {
            public void onSuccess(JSONObject object) {
                SdkLogger.m14d("ss", "entered success json");
            }

            public void onSuccess(String response) {
                SdkLogger.m14d(SdkConstants.TAG, "entered success");
            }

            public void onError(Throwable throwable) {
                SdkLogger.m14d(SdkConstants.TAG, "entered error");
            }

            public void onProgress(int percent) {
                SdkLogger.m14d(SdkConstants.TAG, "entered progress");
            }
        }, 1);
    }

    public void forgotPassword(String email) {
        Map<String, String> p = new HashMap();
        p.put("userName", email);
        postFetch("/auth/app/forgot/password", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(19, true, (Object) jsonObject));
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(19, false, throwable.getMessage()));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void getNetBankingStatus() {
        Map<String, String> p = new HashMap();
        postFetch("/payment/op/getNetBankingStatus", null, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(52, true, new JSONObject(new JSONArray(jsonObject.getString(SdkConstants.RESULT)).getString(0))));
                } catch (JSONException e) {
                    e.printStackTrace();
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(52, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(52, false));
            }

            public void onProgress(int percent) {
            }
        }, 0);
    }

    public void sign_up(String email, String phone, String password) {
        Map<String, String> p = new HashMap();
        p.put(Keys.USER_TYPE, "customer");
        p.put(SdkConstants.USER_NAME, email);
        p.put("phone", phone);
        p.put(Param.SOURCE, "payumoney app");
        p.put(SdkConstants.PASSWORD, password);
        p.put("pageSource", "sign up");
        p.put("name", email);
        postFetch("/auth/app/register", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.get(SdkConstants.RESULT) == null || jsonObject.getString(SdkConstants.RESULT).equals(SdkConstants.NULL_STRING)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(16, false, jsonObject.getString("msg")));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(16, true, jsonObject.getJSONObject(SdkConstants.RESULT)));
                    }
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(16, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(16, false, (Object) "An error occurred while trying to sign you up. Please try again later."));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void logout(String message) {
        this.eventBus.post(new SdkCobbocEvent(2, false, (Object) message));
        if (message != null && !message.equals(SdkConstants.LOGOUT_FORCE)) {
            this.mSessionData.reset();
        }
    }

    public void logout() {
        if (SdkConstants.WALLET_SDK.booleanValue()) {
            logoutWalletSdk();
        } else {
            logout(null);
        }
    }

    private void logoutWalletSdk() {
        this.mIsLogOutCall = true;
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.TOKEN, SharedPrefsUtils.getStringPreference(this.mContext, "access_token"));
        p.put("email", SharedPrefsUtils.getStringPreference(this.mContext, "email"));
        p.put("mobile", SharedPrefsUtils.getStringPreference(this.mContext, "phone"));
        p.put(SdkConstants.HASH, getHashForThisCall(SharedPrefsUtils.getStringPreference(this.mContext, "phone"), SharedPrefsUtils.getStringPreference(this.mContext, "email"), null, null, null));
        postFetch("/auth/ext/wallet/deleteToken", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("status") < 0) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(2, false, jsonObject.getString("message")));
                        return;
                    }
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "access_token");
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "refresh_token");
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "email");
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "phone");
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "userId");
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.USER_TYPE);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.DISPLAY_NAME);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.AVATAR);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.WALLET_BALANCE);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.P2P_PENDING_COUNT);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.P2P_PENDING_AMOUNT);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.MY_BILLS_BADGE_COUNT);
                    SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.ADDED_ON);
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(2, true, jsonObject.getString("message")));
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(2, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(2, false, (Object) "An error occurred while trying to logging you out. Please try again later."));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void updatePostBackParamDetails(Map postBackParamMap) {
        postFetch("/payment/app/processP2Response", postBackParamMap, new Task() {
            public void onSuccess(JSONObject object) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(53, true));
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(53, true));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(53, false));
            }

            public void onProgress(int percent) {
                SdkLogger.m14d(SdkConstants.TAG, "entered progress");
            }
        }, 1);
    }

    public void notifyUserCancelledTransaction(String paymentId, String userCancelled) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID, paymentId);
        if (userCancelled != null) {
            p.put(SdkConstants.USER_CANCELLED_TRANSACTION, userCancelled);
        }
        postFetch("/payment/postBackParam.do" + getParameters(p), null, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                    SdkLogger.m14d(SdkConstants.TAG, "Successfully Cancelled the transaction");
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
            }

            public void onProgress(int percent) {
            }
        }, 0);
    }

    public void sendToPayUWithWallet(JSONObject details, String mode, HashMap<String, Object> data, Double cashback, Double vault, Double discount, Double convenienceChargesAmount) throws JSONException {
        this.wallet_points = vault.doubleValue();
        sendToPayU(details, mode, data, cashback, discount, convenienceChargesAmount);
    }

    public void sendToPayU(JSONObject details, final String mode, final HashMap<String, Object> data, Double cashback1, Double discount, Double convenienceChargesAmount) throws JSONException {
        Double Dis_amt;
        Float cashback = Float.valueOf(round(cashback1.doubleValue()));
        Map<String, String> p = new HashMap();
        final String paymentID = details.optString(SdkConstants.PAYMENT_ID);
        if (SdkHomeActivityNew.coupan_amt != 0.0d) {
            p.put(SdkConstants.COUPON_USED, SdkHomeActivityNew.choosedCoupan);
            Dis_amt = Double.valueOf(SdkHomeActivityNew.coupan_amt);
        } else {
            Dis_amt = discount;
        }
        Double payUAmt;
        if (cashback != null && cashback.floatValue() == 0.0f) {
            payUAmt = Double.valueOf((details.getJSONObject(SdkConstants.PAYMENT).getDouble(SdkConstants.ORDER_AMOUNT) + convenienceChargesAmount.doubleValue()) - Dis_amt.doubleValue());
            if (this.wallet_points > 0.0d) {
                p.put("sourceAmountMap", "{\"PAYU\":" + Double.valueOf(payUAmt.doubleValue() - this.wallet_points) + ",\"DISCOUNT\":" + Dis_amt + ",\"WALLET\":" + this.wallet_points + "}");
            } else {
                p.put("sourceAmountMap", "{\"PAYU\":" + payUAmt + ",\"DISCOUNT\":" + Dis_amt + "}");
            }
        } else if (mode.equals(SdkConstants.WALLET)) {
            if (this.wallet_points == 0.0d) {
                p.put("sourceAmountMap", "{\"WALLET\":" + cashback + ",\"PAYU\":" + 0 + ",\"DISCOUNT\":" + Dis_amt + "}");
            } else {
                p.put("sourceAmountMap", "{\"WALLET\":" + cashback + ",\"CASHBACK\":" + this.wallet_points + ",\"PAYU\":" + 0 + ",\"DISCOUNT\":" + Dis_amt + "}");
            }
        } else if (mode.equals(SdkConstants.POINTS)) {
            p.put("sourceAmountMap", "{\"CASHBACK\":" + cashback + ",\"PAYU\":" + 0 + ",\"DISCOUNT\":" + Dis_amt + "}");
        } else {
            payUAmt = Double.valueOf(((details.getJSONObject(SdkConstants.PAYMENT).getDouble(SdkConstants.ORDER_AMOUNT) + convenienceChargesAmount.doubleValue()) - Dis_amt.doubleValue()) - cashback.doubleValue());
            if (this.wallet_points > 0.0d) {
                payUAmt = Double.valueOf(payUAmt.doubleValue() - this.wallet_points);
            }
            p.put("sourceAmountMap", "{\"CASHBACK\":" + cashback + ",\"PAYU\":" + payUAmt + ",\"DISCOUNT\":" + Dis_amt + ",\"WALLET\":" + this.wallet_points + "}");
        }
        this.wallet_points = 0.0d;
        if (mode.equals(SdkConstants.POINTS) || mode.equals(SdkConstants.WALLET)) {
            p.put("PG", SdkConstants.WALLET_STRING);
        } else {
            p.put("PG", mode);
        }
        if (!(mode.equals(SdkConstants.POINTS) || mode.equals(SdkConstants.WALLET))) {
            if (data.containsKey(SdkConstants.BANK_CODE)) {
                p.put(SdkConstants.BANK_CODE_STRING, data.get(SdkConstants.BANK_CODE).toString());
            } else {
                p.put(SdkConstants.BANK_CODE_STRING, mode);
            }
        }
        if (data.containsKey("storeCardId")) {
            p.put("storeCardId", String.valueOf(data.get("storeCardId")));
        }
        p.put("revisedCashbackReceivedStatus", getrevisedCashbackReceivedStatus());
        p.put(SdkConstants.IS_MOBILE, "1");
        p.put(SdkConstants.CALLING_PLATFORM_NAME, SdkConstants.CALLING_PLATFORM_VALUE);
        if (this.loginMode.equals("guestLogin")) {
            p.put("guestCheckout", "true");
        }
        SdkLogger.m14d("PayUMoneySdk:Params -->", p.toString());
        postFetch("/payment/app/customer/getPaymentMerchant/" + paymentID + getParameters(p), null, new Task() {
            public void onSuccess(JSONObject object) {
                try {
                    if (!object.has("message") || !object.optString("message", SdkConstants.XYZ_STRING).contains(SdkConstants.INVALID_APP_VERSION)) {
                        SdkLogger.m14d("PayUMoneySdk:Success-->", object.toString());
                        object = object.getJSONObject(SdkConstants.RESULT);
                        Object p = new JSONObject();
                        if (mode.equals(SdkConstants.POINTS) || mode.equals(SdkConstants.WALLET)) {
                            SdkSession.this.fetchPaymentStatus(paymentID);
                            return;
                        }
                        String key = (String) data.get("key");
                        data.remove("key");
                        object = object.getJSONObject(SdkConstants.TRANSACTION_DTO).getJSONObject(SdkConstants.HASH);
                        if (!mode.equals(SdkConstants.PAYMENT_MODE_NB)) {
                            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                            cipher.init(1, SdkSession.getPublicKey(key));
                            p.put("encrypted_payment_data", URLEncoder.encode(Base64.encodeToString(cipher.doFinal((data.get(SdkConstants.NUMBER) + "|payu|" + data.get(SdkConstants.CVV) + "|" + data.get(SdkConstants.EXPIRY_MONTH) + "|" + data.get(SdkConstants.EXPIRY_YEAR) + "|").getBytes()), 0)));
                        }
                        Iterator keys = object.keys();
                        while (keys.hasNext()) {
                            key = (String) keys.next();
                            p.put(key, object.getString(key));
                        }
                        p.put("pg", mode);
                        if (data.containsKey(SdkConstants.BANK_CODE)) {
                            p.put(SdkConstants.BANK_CODE, data.get(SdkConstants.BANK_CODE));
                        } else {
                            p.put(SdkConstants.BANK_CODE, mode);
                        }
                        if (!mode.equals(SdkConstants.PAYMENT_MODE_NB)) {
                            if (data.containsKey(SdkConstants.LABEL)) {
                                p.put(SdkConstants.LABEL, data.get(SdkConstants.LABEL));
                            }
                            if (data.containsKey(SdkConstants.STORE)) {
                                p.put(SdkConstants.STORE, data.get(SdkConstants.STORE));
                            }
                            if (data.containsKey("store_card_token")) {
                                p.put("store_card_token", data.get("store_card_token"));
                            }
                        }
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(8, true, p));
                    } else if (mode.equals(SdkConstants.POINTS) || mode.equals(SdkConstants.WALLET)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, SdkConstants.INVALID_APP_VERSION));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(8, false, SdkConstants.INVALID_APP_VERSION));
                    }
                } catch (Exception e) {
                    onError(e);
                }
            }

            public void onSuccess(String response) {
                SdkLogger.m14d("PayUMoneySdk:Success-->", response);
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(8, false, throwable.getMessage()));
                SdkLogger.m14d("PayUMoneySdk:failure-->", throwable.toString());
            }

            public void onProgress(int percent) {
            }
        }, 0);
    }

    public void fetchPaymentStatus(String paymentID) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID, paymentID);
        postFetch("/payment/app/postPayment", p, new Task() {
            public void onSuccess(JSONObject object) {
                String status = object.optString("status");
                if (status == null || status.equals("-1")) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, (Object) object));
                } else {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) object));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) response));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, throwable.getMessage()));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void fetchPaymentResponse(String paymentID) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID_STRING, paymentID);
        postFetch("/payment/app/payment/verifyPaymentStatus", p, new Task() {
            public void onSuccess(JSONObject object) {
                String status = object.optString("status");
                if (status == null || status.equals("-1")) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, (Object) object));
                } else {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) object));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) response));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, throwable.getMessage()));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void verifyPaymentDetails(String paymentID) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.PAYMENT_ID_STRING, paymentID);
        postFetch("/payment/app/payment/verifyPaymentDetails", p, new Task() {
            public void onSuccess(JSONObject object) {
                String status = object.optString("status");
                if (status == null || status.equals("-1")) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, (Object) object));
                } else {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) object));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, true, (Object) response));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(44, false, throwable.getMessage()));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void enableOneClickTransaction(String enable) {
        Map<String, String> p = new HashMap();
        if (this.mContext.getSharedPreferences(SdkConstants.SP_SP_NAME, 0) != null) {
            p.put("oneClickTxn", enable);
            postFetch("/auth/app/setUserPaymentOption", p, new Task() {
                public void onSuccess(JSONObject object) {
                    try {
                        Object result = object.getJSONObject(SdkConstants.RESULT);
                        if (result != null) {
                            SdkSession.this.eventBus.post(new SdkCobbocEvent(27, true, result));
                        } else {
                            SdkSession.this.eventBus.post(new SdkCobbocEvent(27, false));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(27, false));
                    }
                }

                public void onSuccess(String response) {
                }

                public void onError(Throwable throwable) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(27, false));
                }

                public void onProgress(int percent) {
                }
            }, 1);
        }
    }

    public void verifyManualCoupon(String manualCoupon, String paymentId, String device_id, String mobileStatus) {
        Map<String, String> p = new HashMap();
        p.put("userCouponString", manualCoupon);
        p.put("visitId", paymentId);
        p.put("reqId", device_id);
        p.put("mobileStatus", mobileStatus);
        postFetch("/payment/app/validateUserCouponString", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (!jsonObject.has(SdkConstants.RESULT) || jsonObject.isNull(SdkConstants.RESULT)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(41, false));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(41, true, jsonObject.getJSONObject(SdkConstants.RESULT)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(41, false));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(41, false, null));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void getUserVaults() {
        Map p = new HashMap();
        p.put(SdkConstants.HASH, getHashForThisCall(SharedPrefsUtils.getStringPreference(this.mContext, "phone"), SharedPrefsUtils.getStringPreference(this.mContext, "email"), null, null, null));
        postFetch("/auth/ext/wallet/getWalletLimit", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("status") == null || jsonObject.getString("status").equals(SdkConstants.NULL_STRING)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(49, false, jsonObject.getString("message")));
                        return;
                    }
                    if (jsonObject.getJSONObject(SdkConstants.RESULT).has(SdkConstants.AVAILABLE_BALANCE)) {
                        double walletBalance = jsonObject.getJSONObject(SdkConstants.RESULT).optDouble(SdkConstants.AVAILABLE_BALANCE, 0.0d);
                        SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, Keys.WALLET_BALANCE);
                        SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, Keys.WALLET_BALANCE, walletBalance + "");
                    }
                    if (jsonObject.getJSONObject(SdkConstants.RESULT).has("maxLimit")) {
                        double maxWalletBalance = jsonObject.getJSONObject(SdkConstants.RESULT).optDouble("maxLimit", 0.0d);
                        SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "maxLimit");
                        SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "maxLimit", maxWalletBalance + "");
                    }
                    if (jsonObject.getJSONObject(SdkConstants.RESULT).has("minLimit")) {
                        double minWalletBalance = jsonObject.getJSONObject(SdkConstants.RESULT).optDouble("minLimit", 0.0d);
                        SharedPrefsUtils.removePreferenceByKey(SdkSession.this.mContext, "minLimit");
                        SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "minLimit", minWalletBalance + "");
                    }
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(49, true));
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(49, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(49, false, (Object) "An error occurred while verifying your OTP. Please generate again."));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte[] messageDigest = algorithm.digest();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return hexString.toString();
    }

    public String getHashForThisCall(String mobile, String email, String amount, String merchantTxnId, String productInfo) {
        String hashSequence = merchantKey;
        if (mobile != null) {
            hashSequence = hashSequence + "|" + mobile;
        }
        if (email != null) {
            hashSequence = hashSequence + "|" + email;
        }
        if (amount != null) {
            hashSequence = hashSequence + "|" + amount;
        }
        if (productInfo != null && !SdkConstants.PRODUCT_INFO.equals(productInfo)) {
            hashSequence = hashSequence + "|" + productInfo;
        } else if (productInfo != null) {
            hashSequence = hashSequence + "|";
        }
        if (merchantTxnId != null) {
            hashSequence = hashSequence + "|" + merchantTxnId;
        }
        return hashCal(hashSequence + "|" + merchantSalt);
    }

    public void getTransactionHistory(int offset) {
        Map p = new HashMap();
        p.put(SdkConstants.WALLET_HISTORY_PARAM_OFFSET, Integer.valueOf(offset));
        p.put(SdkConstants.WALLET_HISTORY_PARAM_LIMIT, Integer.valueOf(12));
        p.put("key", merchantKey);
        p.put(SdkConstants.HASH, getHashForThisCall(offset + "", 12 + "", null, null, null));
        postFetch("/vault/ext/getVaultTransactionDetails" + getParameters(p), null, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("status") < 0) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(43, false, jsonObject.getString("message")));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(43, true, jsonObject.getJSONObject(SdkConstants.RESULT)));
                    }
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(43, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(43, false, (Object) "An error occurred while trying to sign you up. Please try again later."));
            }

            public void onProgress(int percent) {
            }
        }, 0);
    }

    public void verifyUserCredential(String email, String mobileNo, String otp) {
        Map p = new HashMap();
        p.put(SdkConstants.OTP_STRING, otp);
        p.put("email", email);
        p.put("mobile", mobileNo);
        p.put(SdkConstants.HASH, getHashForThisCall(mobileNo, email, null, null, null));
        postFetch("/auth/ext/wallet/verify", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("status") < 0) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(46, false, jsonObject.getString("message")));
                        return;
                    }
                    SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "access_token", jsonObject.getJSONObject(SdkConstants.RESULT).getJSONObject(SdkConstants.BODY).getString("access_token"));
                    SharedPrefsUtils.setStringPreference(SdkSession.this.mContext, "refresh_token", jsonObject.getJSONObject(SdkConstants.RESULT).getJSONObject(SdkConstants.BODY).getString("refresh_token"));
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(46, true, jsonObject.getString("message")));
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(46, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(46, false, (Object) "An error occurred while trying to sign you up. Please try again later."));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void sendMobileVerificationCode(String email, String mobileNo) {
        Map p = new HashMap();
        p.put("email", email);
        p.put("mobile", mobileNo);
        p.put(SdkConstants.HASH, getHashForThisCall(mobileNo, email, null, null, null));
        SharedPrefsUtils.setStringPreference(this.mContext, "email", email);
        SharedPrefsUtils.setStringPreference(this.mContext, "phone", mobileNo);
        postFetch("/auth/ext/wallet/register", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("status") < 0) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(48, false, jsonObject.getString("message")));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(48, true, jsonObject.getString("message")));
                    }
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(48, false));
                }
            }

            public void onSuccess(String response) {
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(48, false, (Object) "An error occurred while trying to sign you up. Please try again later."));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void loadWallet(String otp, String amount, String paymentId, String productInfo) {
        Map<String, String> p = new HashMap();
        p.put(SdkConstants.TOTAL_AMOUNT, amount);
        if (otp != null) {
            p.put(SdkConstants.OTP_STRING, otp);
        }
        p.put(SdkConstants.PAYMENT_IDENTIFIERS_STRING, "[]");
        p.put(SdkConstants.PAYMENT_PARTS_STRING, "[]");
        p.put(SdkConstants.PRODUCT_INFO, productInfo);
        p.put("paymentDescription", SdkConstants.LOAD_WALLET);
        p.put("sourceReferenceId", paymentId);
        p.put(SdkConstants.IS_MOBILE, "1");
        p.put(SdkConstants.DEVICE_ID, SdkHelper.getAndroidID(this.mContext));
        p.put(SdkConstants.APP_VERSION_CODE, "5000");
        postFetch("/payment/app/wallet/loadWalletPayment", p, new Task() {
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.has(SdkConstants.RESULT) && !jsonObject.isNull(SdkConstants.RESULT)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(42, true, jsonObject.getJSONObject(SdkConstants.RESULT)));
                    } else if (!jsonObject.has("message") || jsonObject.isNull("message")) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false, jsonObject.getString("message")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false, null));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void loadWallet(HashMap<String, String> params, String amt_net) {
        Map p = new HashMap();
        p.put("key", params.get("key"));
        p.put(SdkConstants.TRANSACTION_DETAILS, params.get(SdkConstants.TRANSACTION_DETAILS));
        p.put(SdkConstants.TOTAL_AMOUNT, amt_net);
        p.put(SdkConstants.TRANSACTION_DETAILS, "{\"surl\": \"" + ((String) params.get(SdkConstants.SURL)) + "\", \"furl\": \"" + ((String) params.get(SdkConstants.FURL)) + "\", \"email\": \"" + SharedPrefsUtils.getStringPreference(this.mContext, "email") + "\"}");
        p.put(SdkConstants.HASH, getHashForThisCall(null, null, amt_net, null, (String) params.get(SdkConstants.PRODUCT_INFO)));
        postFetch("/payment/app/wallet/loadWalletPayment", p, new Task() {
            public void onSuccess(JSONObject object) {
                try {
                    if (!object.has(SdkConstants.RESULT) || object.isNull(SdkConstants.RESULT)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
                        return;
                    }
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(42, true, object.getJSONObject(SdkConstants.RESULT)));
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(42, false));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public void debitFromWallet(HashMap<String, String> params) {
        Map p = new HashMap();
        p.put("key", params.get("key"));
        p.put(SdkConstants.TOTAL_AMOUNT, params.get(SdkConstants.AMOUNT));
        p.put(SdkConstants.MERCHANT_TXNID, params.get(SdkConstants.MERCHANT_TXNID));
        p.put(SdkConstants.HASH, getHashForThisCall(null, null, ((String) params.get(SdkConstants.AMOUNT)) + "", merchantTxnId, (String) params.get(SdkConstants.PRODUCT_INFO)));
        p.put(SdkConstants.DEVICE_ID, SdkHelper.getAndroidID(this.mContext));
        postFetch("/payment/ext/wallet/useWallet", p, new Task() {
            public void onSuccess(JSONObject object) {
                try {
                    if (!object.has(SdkConstants.RESULT) || object.isNull(SdkConstants.RESULT)) {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(45, false, object.getString("message")));
                    } else {
                        SdkSession.this.eventBus.post(new SdkCobbocEvent(45, true, object.getString(SdkConstants.RESULT)));
                    }
                } catch (JSONException e) {
                    SdkSession.this.eventBus.post(new SdkCobbocEvent(45, false));
                }
            }

            public void onSuccess(String response) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(45, false));
            }

            public void onError(Throwable throwable) {
                SdkSession.this.eventBus.post(new SdkCobbocEvent(45, false));
            }

            public void onProgress(int percent) {
            }
        }, 1);
    }

    public String getToken() {
        if (SdkConstants.WALLET_SDK.booleanValue()) {
            return SharedPrefsUtils.getStringPreference(this.mContext, "access_token");
        }
        return this.mSessionData.getToken();
    }

    public void setrevisedCashbackReceivedStatus(String s) {
        this.mSessionData.setrevisedCashbackReceivedStatus(s);
    }

    public String getrevisedCashbackReceivedStatus() {
        return this.mSessionData.revisedCashbackReceivedStatus;
    }

    public static float round(double d) {
        return new BigDecimal(Double.toString(d)).setScale(2, 4).floatValue();
    }
}
