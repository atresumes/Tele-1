package com.payUMoney.sdk.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.SdkSetupCardDetails;
import com.payUMoney.sdk.adapter.SdkStoredCardAdapter;
import com.payUMoney.sdk.entity.SdkIssuer;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import com.payu.custombrowser.util.CBConstant;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkStoredCardFragment extends View {
    private SdkStoredCardAdapter adapter;
    private String authorizationSalt;
    private String cardCvvHash;
    private String device_id;
    private String encryptedUserId;
    private final Handler handler;
    ListView listView = null;
    MakePaymentListener mCallback;
    Context mContext;
    private ProgressDialog mProgressDialog = null;
    private RequestQueue mRequestQueue;
    String mode = null;
    private JSONObject selectedCard;
    private int selectedCardPosition = -1;
    private JSONObject userConfigDto = null;
    private String userToken;

    class C04181 implements OnTouchListener {
        C04181() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    }

    class C04192 implements OnItemClickListener {
        C04192() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SdkStoredCardFragment.this.mProgressDialog = ((SdkHomeActivityNew) SdkStoredCardFragment.this.mContext).showProgress(SdkStoredCardFragment.this.mContext);
            SdkStoredCardFragment.this.selectedCard = (JSONObject) adapterView.getAdapter().getItem(i);
            SdkStoredCardFragment.this.selectedCardPosition = i;
            try {
                SharedPreferences mPref = SdkStoredCardFragment.this.mContext.getSharedPreferences(SdkConstants.SP_SP_NAME, 0);
                if (mPref.contains(SdkConstants.ONE_TAP_FEATURE) && mPref.getBoolean(SdkConstants.ONE_TAP_FEATURE, false) && mPref.contains(SdkConstants.CONFIG_DTO)) {
                    SdkStoredCardFragment.this.userConfigDto = new JSONObject(mPref.getString(SdkConstants.CONFIG_DTO, SdkConstants.XYZ_STRING));
                }
                SdkStoredCardFragment.this.mode = SdkStoredCardFragment.this.selectedCard.getString("pg");
                ((SdkHomeActivityNew) SdkStoredCardFragment.this.mContext).updateDetails(SdkStoredCardFragment.this.mode);
                if (SdkStoredCardFragment.this.userConfigDto != null && SdkStoredCardFragment.this.userConfigDto.has(SdkConstants.ONE_CLICK_PAYMENT) && SdkStoredCardFragment.this.userConfigDto.optBoolean(SdkConstants.ONE_CLICK_PAYMENT, false) && SdkStoredCardFragment.this.selectedCard.has(SdkConstants.ONE_CLICK_CHECK_OUT) && !SdkStoredCardFragment.this.selectedCard.isNull(SdkConstants.ONE_CLICK_CHECK_OUT) && SdkStoredCardFragment.this.selectedCard.optBoolean(SdkConstants.ONE_CLICK_CHECK_OUT, false) && SdkStoredCardFragment.this.selectedCard.has(SdkConstants.CARD_TOKEN) && !SdkStoredCardFragment.this.selectedCard.isNull(SdkConstants.CARD_TOKEN)) {
                    SdkStoredCardFragment.this.calculateCardHash(SdkStoredCardFragment.this.selectedCard.getString(SdkConstants.CARD_TOKEN), SdkStoredCardFragment.this.userConfigDto);
                } else {
                    SdkStoredCardFragment.this.askForCvvDialog(SdkStoredCardFragment.this.adapter, SdkStoredCardFragment.this.selectedCardPosition);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface MakePaymentListener {
        void goToPayment(String str, HashMap<String, Object> hashMap) throws JSONException;

        void setCardHashForOneClickTxn(String str);
    }

    class C08953 implements Listener<String> {
        C08953() {
        }

        public void onResponse(String response) {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has("error")) {
                    onFailure(object.getString("error"), new Throwable(object.getString("error")));
                } else if (object == null || object.getString("status").equals("-1") || !object.has(SdkConstants.RESULT) || object.isNull(SdkConstants.RESULT)) {
                    SdkStoredCardFragment.this.askForCvvDialog(SdkStoredCardFragment.this.adapter, SdkStoredCardFragment.this.selectedCardPosition);
                } else {
                    SdkStoredCardFragment.this.cardCvvHash = object.getString(SdkConstants.RESULT);
                    SdkStoredCardFragment.this.proceedToPay(SdkStoredCardFragment.this.selectedCard);
                }
            } catch (JSONException e) {
                onFailure(e.getMessage(), e);
            }
        }

        public void onFailure(String msg, Throwable e) {
            if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                Log.e(SdkConstants.TAG, "Session...new JsonHttpResponseHandler() {...}.onFailure: " + e.getMessage() + " " + msg);
            }
            SdkStoredCardFragment.this.askForCvvDialog(SdkStoredCardFragment.this.adapter, SdkStoredCardFragment.this.selectedCardPosition);
        }
    }

    class C08964 implements ErrorListener {
        C08964() {
        }

        public void onErrorResponse(VolleyError error) {
            if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
                Log.e(SdkConstants.TAG, "Session...new JsonHttpResponseHandler() {...}.onFailure: " + error.getMessage());
            }
            if (error == null || error.networkResponse == null || error.networkResponse.statusCode == 401) {
                SdkStoredCardFragment.this.askForCvvDialog(SdkStoredCardFragment.this.adapter, SdkStoredCardFragment.this.selectedCardPosition);
            } else {
                SdkStoredCardFragment.this.askForCvvDialog(SdkStoredCardFragment.this.adapter, SdkStoredCardFragment.this.selectedCardPosition);
            }
        }
    }

    public SdkStoredCardFragment(Context context) {
        super(context);
        this.mContext = context;
        this.mCallback = (MakePaymentListener) context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public RequestQueue getRequestQueue(Context context) {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(context);
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue(this.mContext).add(req);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        SdkLogger.m14d(SdkConstants.TAG, "StoredCardFragmentonCreateView");
        View storedCardFragment = inflater.inflate(C0360R.layout.sdk_fragment_stored_card, container, false);
        this.listView = (ListView) storedCardFragment.findViewById(C0360R.id.storedCardListView);
        this.mContext = container.getContext();
        this.adapter = new SdkStoredCardAdapter(this.mContext, ((SdkHomeActivityNew) this.mContext).getStoredCardList());
        this.listView.setAdapter(this.adapter);
        this.listView.setDescendantFocusability(262144);
        this.listView.setOnTouchListener(new C04181());
        if (this.adapter.getCount() > 1) {
            int totalHeight = this.listView.getPaddingTop() + this.listView.getPaddingBottom();
            for (int i = 0; i < 2; i++) {
                View listItem = this.adapter.getView(i, null, this.listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new LayoutParams(-2, -2));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            LayoutParams params = this.listView.getLayoutParams();
            params.height = this.listView.getDividerHeight() + totalHeight;
            this.listView.setLayoutParams(params);
        }
        this.listView.setOnItemClickListener(new C04192());
        return storedCardFragment;
    }

    private void askForCvvDialog(SdkStoredCardAdapter adapter, int i) {
        dismissProgress();
        adapter.setSelectedCard(i);
        adapter.notifyDataSetInvalidated();
    }

    private void calculateCardHash(String cardToken, JSONObject userConfigDto) {
        if (userConfigDto != null) {
            try {
                if (userConfigDto.has(SdkConstants.AUTHORIZATION_SALT) && !userConfigDto.isNull(SdkConstants.AUTHORIZATION_SALT)) {
                    this.authorizationSalt = userConfigDto.getString(SdkConstants.AUTHORIZATION_SALT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!(userConfigDto == null || !userConfigDto.has(SdkConstants.USER_TOKEN) || userConfigDto.isNull(SdkConstants.USER_TOKEN))) {
            this.userToken = userConfigDto.getString(SdkConstants.USER_TOKEN);
        }
        if (!(userConfigDto == null || !userConfigDto.has("userId") || userConfigDto.isNull("userId"))) {
            this.encryptedUserId = userConfigDto.getString("userId");
        }
        String hash = hashCal(this.userToken + "|" + ((SdkHomeActivityNew) this.mContext).getUserId() + "|" + cardToken + "|" + this.authorizationSalt);
        String paymentId = ((SdkHomeActivityNew) this.mContext).getPaymentId();
        String deviceId = getAndroidID(this.mContext);
        if (!hash.isEmpty() && !paymentId.isEmpty() && !deviceId.isEmpty()) {
            Map<String, String> p = new HashMap();
            p.put(SdkConstants.HASH, hash);
            p.put("uid", this.encryptedUserId);
            p.put("pid", paymentId);
            p.put("did", deviceId);
            postFetch(PayUmoneySdkInitilizer.IsDebugMode().booleanValue() ? SdkConstants.KVAULT_TEST_URL : SdkConstants.KVAULT_PROD_URL, p, 1);
        }
    }

    public String getAndroidID(Context context) {
        if (context == null) {
            return "";
        }
        this.device_id = Secure.getString(context.getContentResolver(), "android_id");
        return this.device_id;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
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

    private void proceedToPay(JSONObject jsonObject) {
        try {
            HashMap<String, Object> data = new HashMap();
            data.put("storeCardId", jsonObject.getString("cardId"));
            data.put("store_card_token", jsonObject.getString(SdkConstants.CARD_TOKEN));
            data.put(SdkConstants.LABEL, jsonObject.getString("cardName"));
            data.put(SdkConstants.NUMBER, "");
            data.put("key", ((SdkHomeActivityNew) this.mContext).getPublicKey());
            data.put(SdkConstants.BANK_CODE, SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), this.mode));
            if (SdkHelper.checkNetwork(this.mContext)) {
                data.put(SdkConstants.EXPIRY_MONTH, "");
                data.put(SdkConstants.EXPIRY_YEAR, "");
                dismissProgress();
                this.mCallback.setCardHashForOneClickTxn(this.cardCvvHash);
                this.mCallback.goToPayment(this.mode, data);
                return;
            }
            Toast.makeText(this.mContext, C0360R.string.disconnected_from_internet, 0).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static SdkIssuer getIssuer(String mNumber, String cardMode) {
        String tempIssuer = SdkSetupCardDetails.findIssuer(mNumber, cardMode);
        Object obj = -1;
        switch (tempIssuer.hashCode()) {
            case 73257:
                if (tempIssuer.equals("JCB")) {
                    obj = 2;
                    break;
                }
                break;
            case 2012639:
                if (tempIssuer.equals(SdkConstants.AMEX)) {
                    obj = null;
                    break;
                }
                break;
            case 2098441:
                if (tempIssuer.equals("DINR")) {
                    obj = 1;
                    break;
                }
                break;
            case 2358594:
                if (tempIssuer.equals("MAES")) {
                    obj = 7;
                    break;
                }
                break;
            case 2359029:
                if (tempIssuer.equals("MAST")) {
                    obj = 5;
                    break;
                }
                break;
            case 2634817:
                if (tempIssuer.equals("VISA")) {
                    obj = 4;
                    break;
                }
                break;
            case 72205995:
                if (tempIssuer.equals("LASER")) {
                    obj = 3;
                    break;
                }
                break;
            case 78339941:
                if (tempIssuer.equals("RUPAY")) {
                    obj = 6;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return SdkIssuer.AMEX;
            case 1:
                return SdkIssuer.DINER;
            case 2:
                return SdkIssuer.JCB;
            case 3:
                return SdkIssuer.LASER;
            case 4:
                return SdkIssuer.VISA;
            case 5:
                return SdkIssuer.MASTERCARD;
            case 6:
                return SdkIssuer.RUPAY;
            case 7:
                return SdkIssuer.MAESTRO;
            default:
                if (cardMode.contentEquals(SdkConstants.PAYMENT_MODE_CC)) {
                    return SdkIssuer.UNKNOWN;
                }
                if (cardMode.contentEquals(SdkConstants.PAYMENT_MODE_DC)) {
                    return SdkIssuer.MASTERCARD;
                }
                return SdkIssuer.UNKNOWN;
        }
    }

    public void postFetch(String url, Map<String, String> params, int method) {
        if (PayUmoneySdkInitilizer.IsDebugMode().booleanValue()) {
            SdkLogger.m14d(SdkConstants.TAG, "SdkSession.postFetch: " + url + " " + params + " " + method);
        }
        final Map<String, String> map = params;
        StringRequest myRequest = new StringRequest(method, url, new C08953(), new C08964()) {
            protected Map<String, String> getParams() {
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put(SdkConstants.USER_AGENT, "PayUMoneyAPP");
                if (SdkSession.getInstance(SdkStoredCardFragment.this.mContext).getToken() != null) {
                    params.put("Authorization", "Bearer " + SdkSession.getInstance(SdkStoredCardFragment.this.mContext).getToken());
                } else {
                    params.put("Accept", "*/*;");
                }
                params.put("Cookie", SdkHelper.getUserCookieSessionId(SdkStoredCardFragment.this.mContext));
                return params;
            }

            public String getBodyContentType() {
                if (SdkSession.getInstance(SdkStoredCardFragment.this.mContext).getToken() == null) {
                    return CBConstant.HTTP_URLENCODED;
                }
                return super.getBodyContentType();
            }
        };
        myRequest.setShouldCache(false);
        myRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(myRequest);
    }

    private void dismissProgress() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }
}
