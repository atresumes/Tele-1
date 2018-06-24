package com.talktoangel.gts.userauth;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import com.talktoangel.gts.MainActivity;
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

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView mEmailView;
    private View mLoginFormView;
    private EditText mPasswordView;
    private View mProgressView;
    SessionManager mSessionManager;
    private RadioButton radioPatient;
    private RadioButton radioProvider;

    class C06431 implements OnEditorActionListener {
        C06431() {
        }

        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id != C0585R.id.login && id != 0) {
                return false;
            }
            LoginActivity.this.attemptLogin();
            return true;
        }
    }

    class C06442 implements OnClickListener {
        C06442() {
        }

        @TargetApi(23)
        public void onClick(View v) {
            LoginActivity.this.requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "audio", "android.permission.CAMERA", "android.permission.READ_SMS", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE"}, 0);
        }
    }

    class C10613 implements Listener<String> {
        C10613() {
        }

        public void onResponse(String response) {
            Log.e("Login Response", response);
            try {
                JSONObject jSONObject = new JSONObject(response);
                if (jSONObject.getString("status").equals("true")) {
                    LoginActivity.this.showProgress(false);
                    JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                    String uType = object.getString(SessionManager.KEY_TYPE);
                    if (uType.equalsIgnoreCase("t")) {
                        String id = object.getString("dr_id");
                        String fName = object.getString("dr_fname");
                        String lName = object.getString("dr_lname");
                        String email = object.getString("email");
                        String gender = object.getString(SessionManager.KEY_GENDER);
                        String about = object.getString(SessionManager.ABOUT);
                        String speciality = object.getString("dr_speciality");
                        String speciality_id = object.getString("id_dr_speciality");
                        String rates = object.getString(SessionManager.KEY_RATES);
                        String licencesNo = object.getString("licences_no");
                        String drEducation = object.getString(SessionManager.KEY_EDUCATION);
                        String mobile = object.getString("mobile");
                        String experience = object.getString(SessionManager.KEY_EXPERIENCE);
                        String address = object.getString(SessionManager.KEY_ADDRESS);
                        String city = object.getString("city");
                        String country = object.getString(SdkConstants.COUNTRY);
                        String pinCode = object.getString(SdkConstants.ZIPCODE);
                        String pic = object.getString("dr_pic");
                        String therapistType = object.getString(SessionManager.KEY_THERAPIST_TYPE);
                        String accName = "";
                        String accNo = "";
                        String accType = "";
                        String bankName = "";
                        String ifscCode = "";
                        String branch = "";
                        String panNO = "";
                        String aadharNo = "";
                        if (object.has("acc_holder_nm")) {
                            accName = object.getString("acc_holder_nm");
                            accNo = object.getString(SessionManager.ACC_NO);
                            accType = object.getString("acc_type");
                            bankName = object.getString(SessionManager.BANK_NAME);
                            ifscCode = object.getString(SessionManager.IFSC_CODE);
                            branch = object.getString(SessionManager.BRANCH_CITY);
                            panNO = object.getString(SessionManager.PAN_NO);
                            aadharNo = object.getString(SessionManager.AADHAR_NO);
                        }
                        LoginActivity.this.mSessionManager.setAvailability(object.getString("dr_availability"));
                        LoginActivity.this.mSessionManager.setCounselor(id, uType, fName, lName, email, gender, experience, speciality_id, speciality, rates, licencesNo, drEducation, address, "", city, country, pinCode, mobile, therapistType, pic, about);
                        LoginActivity.this.mSessionManager.setBankDetails(accName, accNo, accType, bankName, ifscCode, branch, panNO, aadharNo);
                        LoginActivity.this.mSessionManager.setLoginSession();
                    } else {
                        LoginActivity.this.mSessionManager.setUser(object.getString(SessionManager.FREE_SESSION), object.getString("id"), uType, object.getString(SessionManager.KEY_FIRST_NAME), object.getString(SessionManager.KEY_LAST_NAME), object.getString("email"), object.getString("security_ques"), object.getString("answer"), object.getString(SessionManager.KEY_GENDER), object.getString("add_line"), object.getString("apt_bldg"), object.getString("city"), object.getString("state"), object.getString(SdkConstants.ZIPCODE), object.getString(SessionManager.COUNTRY_CODE), object.getString(SessionManager.KEY_CURRENCY_PREF), object.getString("mobile"), object.getString("pic"));
                        LoginActivity.this.mSessionManager.setLoginSession();
                    }
                    Toast.makeText(LoginActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class).addFlags(67108864));
                    LoginActivity.this.finish();
                    return;
                }
                LoginActivity.this.showProgress(false);
                Toast.makeText(LoginActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
            } catch (JSONException e) {
                LoginActivity.this.showProgress(false);
                e.printStackTrace();
            }
        }
    }

    class C10624 implements ErrorListener {
        C10624() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
            LoginActivity.this.showProgress(false);
        }
    }

    class C10638 implements Listener<String> {
        C10638() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    LoginActivity.this.showProgress(false);
                    Toast.makeText(LoginActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                    return;
                }
                LoginActivity.this.showProgress(false);
                Toast.makeText(LoginActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                LoginActivity.this.showProgress(false);
                e.printStackTrace();
            }
        }
    }

    class C10649 implements ErrorListener {
        C10649() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error", "" + error.getMessage());
            LoginActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_login);
        getSupportActionBar().hide();
        this.radioPatient = (RadioButton) findViewById(C0585R.id.radio_patient);
        this.radioProvider = (RadioButton) findViewById(C0585R.id.radio_provider);
        this.mEmailView = (AutoCompleteTextView) findViewById(C0585R.id.email);
        this.mPasswordView = (EditText) findViewById(C0585R.id.password);
        Button mSignInButton = (Button) findViewById(C0585R.id.sign_in_button);
        Button forgotPasword = (Button) findViewById(C0585R.id.btn_forgot_password);
        Button inCrisisButon = (Button) findViewById(C0585R.id.btn_in_crisis);
        Button signUpButton = (Button) findViewById(C0585R.id.btn_sign_up);
        this.radioPatient.setChecked(true);
        mSignInButton.setOnClickListener(this);
        forgotPasword.setOnClickListener(this);
        inCrisisButon.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        this.mLoginFormView = findViewById(C0585R.id.login_form);
        this.mProgressView = findViewById(C0585R.id.login_progress);
        this.mPasswordView.setOnEditorActionListener(new C06431());
        this.mSessionManager = new SessionManager(getApplicationContext());
        mayRequestPermissions();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.sign_in_button:
                attemptLogin();
                return;
            case C0585R.id.btn_forgot_password:
                showForgotPasswordDialog();
                return;
            case C0585R.id.btn_in_crisis:
                showCrisisDialog();
                return;
            case C0585R.id.btn_sign_up:
                if (this.radioPatient.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
                    finish();
                    return;
                } else if (this.radioProvider.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), TherapistRegistrationActivity.class));
                    finish();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private boolean mayRequestPermissions() {
        if (VERSION.SDK_INT < 23 || ((((((checkSelfPermission("android.permission.READ_CONTACTS") + checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE")) + checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")) + checkSelfPermission("audio")) + checkSelfPermission("android.permission.CAMERA")) + checkSelfPermission("android.permission.READ_SMS")) + checkSelfPermission("android.permission.READ_PHONE_STATE")) + checkSelfPermission("android.permission.CALL_PHONE") == 0) {
            return true;
        }
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
            Snackbar.make(this.mEmailView, (int) C0585R.string.permission_rationale, -2).setAction(17039370, new C06442());
        } else {
            requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "audio", "android.permission.CAMERA", "android.permission.READ_SMS", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE"}, 0);
        }
        return false;
    }

    private void attemptLogin() {
        this.mEmailView.setError(null);
        this.mPasswordView.setError(null);
        String email = this.mEmailView.getText().toString();
        String password = this.mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            this.mEmailView.setError(getString(C0585R.string.error_field_required));
            focusView = this.mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            this.mPasswordView.setError(getString(C0585R.string.error_field_required));
            focusView = this.mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            this.mPasswordView.setError(getString(C0585R.string.error_invalid_password));
            focusView = this.mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        UserLoginTask(email, password, getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null));
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(boolean show) {
        int i;
        int i2 = 4;
        View view = this.mProgressView;
        if (show) {
            i = 0;
        } else {
            i = 4;
        }
        view.setVisibility(i);
        View view2 = this.mLoginFormView;
        if (!show) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    private void UserLoginTask(String email, String password, String deviceId) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = email;
        final String str2 = password;
        final String str3 = deviceId;
        Request strReq = new StringRequest(1, EndPoints.LOGIN, new C10613(), new C10624()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("device_type", "a");
                params.put("email", str);
                params.put(SdkConstants.PASSWORD, str2);
                params.put("device_id", str3);
                Log.e("Login params", params.toString());
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

    private void showCrisisDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0585R.layout.dialog_incrisis);
        ((Button) dialog.findViewById(C0585R.id.btn_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }

    private void showForgotPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0585R.layout.dialog_forgot_password);
        final EditText editText = (EditText) dialog.findViewById(C0585R.id.et_email_fp);
        ((Button) dialog.findViewById(C0585R.id.btn_submit_fp)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String email = editText.getText().toString();
                if (email.isEmpty()) {
                    editText.setError(LoginActivity.this.getResources().getString(C0585R.string.error_field_required));
                    return;
                }
                dialog.dismiss();
                LoginActivity.this.showProgress(true);
                LoginActivity.this.forgotPassword(email);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }

    private void forgotPassword(String email) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = email;
        Request strReq = new StringRequest(1, EndPoints.FORGOT_PASS, new C10638(), new C10649()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("device_type", "a");
                params.put("email", str);
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
