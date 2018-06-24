package com.talktoangel.gts.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.utils.Constant;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {
    static final String TAG = OTPActivity.class.getSimpleName();
    public EditText etOtp;
    private ProgressBar mProgressBar;
    private SessionManager mSessionManager;
    boolean updateMobile;

    class C06471 implements TextWatcher {
        C06471() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String deviceID = OTPActivity.this.getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null);
            if (editable.length() != 4) {
                return;
            }
            if (OTPActivity.this.updateMobile) {
                if (((String) OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                    OTPActivity.this.verifyUpdatedMobileOtpProvider(OTPActivity.this.getApplicationContext().getSharedPreferences(Constant.TEMP_MOBILE_PREF, 0).getString("mobile", null), editable.toString());
                    return;
                }
                OTPActivity.this.verifyUpdatedMobileOtp(editable.toString());
            } else if (((String) OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                OTPActivity.this.verifyOtpProvider(editable.toString(), deviceID);
            } else {
                OTPActivity.this.verifyOtpRequest(editable.toString(), deviceID);
            }
        }
    }

    class C06482 implements OnClickListener {
        C06482() {
        }

        public void onClick(View view) {
            OTPActivity.this.requestNewOtp();
        }
    }

    class C06493 implements OnClickListener {
        C06493() {
        }

        public void onClick(View v) {
            String otp = OTPActivity.this.etOtp.getText().toString().trim();
            String deviceID = OTPActivity.this.getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null);
            if (otp.length() <= 1) {
                Snackbar.make(v, (CharSequence) "Enter OTP", 0).show();
            } else if (OTPActivity.this.updateMobile) {
                if (((String) OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                    OTPActivity.this.verifyUpdatedMobileOtpProvider(OTPActivity.this.getApplicationContext().getSharedPreferences(Constant.TEMP_MOBILE_PREF, 0).getString("mobile", null), otp);
                    return;
                }
                OTPActivity.this.verifyUpdatedMobileOtp(otp);
            } else if (((String) OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                OTPActivity.this.verifyOtpProvider(otp, deviceID);
            } else {
                OTPActivity.this.verifyOtpRequest(otp, deviceID);
            }
        }
    }

    class C10654 implements Listener<String> {
        C10654() {
        }

        public void onResponse(String response) {
            Log.e(OTPActivity.TAG, response);
            try {
                OTPActivity.this.mProgressBar.setVisibility(4);
                Toast.makeText(OTPActivity.this.getApplicationContext(), new JSONObject(response).getString("message"), 0).show();
            } catch (JSONException e) {
                e.printStackTrace();
                OTPActivity.this.mProgressBar.setVisibility(4);
            }
        }
    }

    class C10665 implements ErrorListener {
        C10665() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            OTPActivity.this.mProgressBar.setVisibility(4);
        }
    }

    class C10677 implements Listener<String> {
        C10677() {
        }

        public void onResponse(String response) {
            try {
                OTPActivity.this.mProgressBar.setVisibility(4);
                Log.e(OTPActivity.TAG, response);
                JSONObject jSONObject = new JSONObject(response);
                if (jSONObject.has("status")) {
                    if (jSONObject.getString("status").equalsIgnoreCase("true")) {
                        JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                        OTPActivity.this.mSessionManager.setUser(object.getString(SessionManager.FREE_SESSION), object.getString("id"), object.getString(SessionManager.KEY_TYPE), object.getString(SessionManager.KEY_FIRST_NAME), object.getString(SessionManager.KEY_LAST_NAME), object.getString("email"), object.getString("security_ques"), object.getString("answer"), object.getString(SessionManager.KEY_GENDER), object.getString("add_line"), object.getString("apt_bldg"), object.getString("city"), object.getString("state"), object.getString(SdkConstants.ZIPCODE), object.getString(SessionManager.COUNTRY_CODE), object.getString(SessionManager.KEY_CURRENCY_PREF), object.getString("mobile"), object.getString("pic"));
                        OTPActivity.this.mSessionManager.setMobileVerified(true);
                        OTPActivity.this.mSessionManager.setLoginSession();
                        Toast.makeText(OTPActivity.this.getApplicationContext(), jSONObject.getString("message"), 0).show();
                        OTPActivity.this.startActivity(new Intent(OTPActivity.this, CreditCardInfoActivity.class));
                        OTPActivity.this.finish();
                        return;
                    }
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Toast.makeText(OTPActivity.this.getApplicationContext(), jSONObject.getString("message"), 0).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                OTPActivity.this.mProgressBar.setVisibility(4);
            }
        }
    }

    class C10688 implements ErrorListener {
        C10688() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            OTPActivity.this.mProgressBar.setVisibility(4);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_otp);
        this.mSessionManager = new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.updateMobile = getIntent().getBooleanExtra("update", false);
        init();
    }

    private void init() {
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.mProgressBar);
        this.etOtp = (EditText) findViewById(C0585R.id.etOtp);
        Button btnReSend = (Button) findViewById(C0585R.id.btnReSend);
        Button btnSubmit = (Button) findViewById(C0585R.id.btnSubmit);
        this.etOtp.addTextChangedListener(new C06471());
        btnReSend.setOnClickListener(new C06482());
        btnSubmit.setOnClickListener(new C06493());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void receivedSms(String message) {
        this.etOtp.setText(message);
    }

    private void requestNewOtp() {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        Request postRequest = new StringRequest(1, EndPoints.REQUEST_OTP, new C10654(), new C10665()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(postRequest, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(postRequest, stack);
        }
        Controller.getInstance().addToRequestQueue(postRequest, stack);
    }

    private void verifyOtpRequest(String otp, String deviceID) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = otp;
        final String str2 = deviceID;
        Request strReq = new StringRequest(1, EndPoints.VERIFY_OTP, new C10677(), new C10688()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put(SdkConstants.OTP_STRING, str);
                params.put("device_id", str2);
                params.put("device_type", "a");
                Log.e("verify otp param", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }

    private void verifyOtpProvider(String otp, String deviceID) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = otp;
        final String str2 = deviceID;
        Request strReq = new StringRequest(1, EndPoints.VERIFY_OTP_DR, new Listener<String>() {
            public void onResponse(String response) {
                try {
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Log.e(OTPActivity.TAG, response);
                    JSONObject jsonObj = new JSONObject(response);
                    if (!jsonObj.has("status")) {
                        return;
                    }
                    if (jsonObj.getString("status").equalsIgnoreCase("true")) {
                        JSONObject object = jsonObj.getJSONObject(SdkConstants.RESULT);
                        Toast.makeText(OTPActivity.this.getApplicationContext(), jsonObj.getString("message"), 0).show();
                        OTPActivity.this.startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                        OTPActivity.this.finish();
                        return;
                    }
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Toast.makeText(OTPActivity.this.getApplicationContext(), jsonObj.getString("message"), 0).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    OTPActivity.this.mProgressBar.setVisibility(4);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                OTPActivity.this.mProgressBar.setVisibility(4);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put(SdkConstants.OTP_STRING, str);
                params.put("dr_device_id", str2);
                params.put("dr_device_type", "a");
                Log.e("verify dr otp", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }

    private void verifyUpdatedMobileOtp(String otp) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = otp;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_MOBILE, new Listener<String>() {
            public void onResponse(String response) {
                try {
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Log.e(OTPActivity.TAG, response);
                    JSONObject jSONObject = new JSONObject(response);
                    if (jSONObject.has("status")) {
                        if (jSONObject.getString("status").equalsIgnoreCase("true")) {
                            JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                            OTPActivity.this.mSessionManager.setUser((String) OTPActivity.this.mSessionManager.getUser().get(SessionManager.FREE_SESSION), object.getString("id"), object.getString(SessionManager.KEY_TYPE), object.getString(SessionManager.KEY_FIRST_NAME), object.getString(SessionManager.KEY_LAST_NAME), object.getString("email"), object.getString("security_ques"), object.getString("answer"), object.getString(SessionManager.KEY_GENDER), object.getString("add_line"), object.getString("apt_bldg"), object.getString("city"), object.getString("state"), object.getString(SdkConstants.ZIPCODE), object.getString(SessionManager.COUNTRY_CODE), object.getString(SessionManager.KEY_CURRENCY_PREF), object.getString("mobile"), object.getString("pic"));
                            OTPActivity.this.mSessionManager.setLoginSession();
                            Toast.makeText(OTPActivity.this.getApplicationContext(), jSONObject.getString("message"), 0).show();
                            OTPActivity.this.startActivity(new Intent(OTPActivity.this, CreditCardInfoActivity.class));
                            OTPActivity.this.finish();
                            return;
                        }
                        OTPActivity.this.mProgressBar.setVisibility(4);
                        Toast.makeText(OTPActivity.this.getApplicationContext(), jSONObject.getString("message"), 0).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    OTPActivity.this.mProgressBar.setVisibility(4);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                OTPActivity.this.mProgressBar.setVisibility(4);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put(SdkConstants.OTP_STRING, str);
                Log.e("verify new otp", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }

    private void verifyUpdatedMobileOtpProvider(String mobile, String otp) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = mobile;
        final String str2 = otp;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_MOBILE_PROVIDER, new Listener<String>() {
            public void onResponse(String response) {
                try {
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Log.e(OTPActivity.TAG, response);
                    JSONObject jsonObj = new JSONObject(response);
                    if (!jsonObj.has("status")) {
                        return;
                    }
                    if (jsonObj.getString("status").equalsIgnoreCase("true")) {
                        String mobile = jsonObj.getJSONObject(SdkConstants.RESULT).getString("mobile");
                        OTPActivity.this.mSessionManager.setLoginSession();
                        Toast.makeText(OTPActivity.this.getApplicationContext(), jsonObj.getString("message"), 0).show();
                        OTPActivity.this.startActivity(new Intent(OTPActivity.this, CreditCardInfoActivity.class));
                        OTPActivity.this.finish();
                        return;
                    }
                    OTPActivity.this.mProgressBar.setVisibility(4);
                    Toast.makeText(OTPActivity.this.getApplicationContext(), jsonObj.getString("message"), 0).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    OTPActivity.this.mProgressBar.setVisibility(4);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                OTPActivity.this.mProgressBar.setVisibility(4);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", OTPActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put("mobile", str);
                params.put(SdkConstants.OTP_STRING, str2);
                Log.e("verify new dr otp", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }
}
