package com.talktoangel.gts.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.talktoangel.gts.PayUMoneyActivity;
import com.talktoangel.gts.controller.Controller;
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

public class TestLoginActivity extends AppCompatActivity implements OnClickListener {
    private AutoCompleteTextView mEmailView;
    private View mLoginFormView;
    private EditText mPasswordView;
    private View mProgressView;
    private SessionManager session;

    class C10301 implements Listener<String> {
        C10301() {
        }

        public void onResponse(String response) {
            Log.e("Login Response", response);
            TestLoginActivity.this.showProgress(false);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    String test_type = jObj.getJSONArray(SdkConstants.RESULT).getJSONObject(0).getString("test_type");
                    TestLoginActivity.this.session.setBuddyId(jObj.getString(SessionManager.BUDDY_ID));
                    Toast.makeText(TestLoginActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    String title = (String) TestLoginActivity.this.session.getTestData().get(SessionManager.TEST_TITLE);
                    String amount = (String) TestLoginActivity.this.session.getTestData().get(SessionManager.TEST_PRICE);
                    if (test_type.equals("")) {
                        TestLoginActivity.this.startActivity(new Intent(TestLoginActivity.this.getApplicationContext(), PayUMoneyActivity.class).putExtra("transactionId", amount).putExtra(SdkConstants.AMOUNT, amount));
                        return;
                    }
                    if (title.equalsIgnoreCase(test_type)) {
                        TestLoginActivity.this.startActivity(new Intent(TestLoginActivity.this.getApplicationContext(), TestDetailActivity.class).setFlags(268468224));
                    }
                    TestLoginActivity.this.finish();
                    return;
                }
                TestLoginActivity.this.showProgress(false);
                Toast.makeText(TestLoginActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                TestLoginActivity.this.showProgress(false);
                e.printStackTrace();
            }
        }
    }

    class C10312 implements ErrorListener {
        C10312() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
            TestLoginActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_test_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mEmailView = (AutoCompleteTextView) findViewById(C0585R.id.email);
        this.mPasswordView = (EditText) findViewById(C0585R.id.password);
        this.mLoginFormView = findViewById(C0585R.id.login_form);
        this.mProgressView = findViewById(C0585R.id.login_progress);
        Button signUpButton = (Button) findViewById(C0585R.id.btn_sign_up);
        ((Button) findViewById(C0585R.id.sign_in_button)).setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        this.session = new SessionManager(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.sign_in_button:
                attemptLogin();
                return;
            case C0585R.id.btn_sign_up:
                startActivity(new Intent(this, TestSignUpActivity.class));
                return;
            default:
                return;
        }
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
        UserLoginTask(email, password, "");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
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
        Request strReq = new StringRequest(1, EndPoints.TEST_LOGIN, new C10301(), new C10312()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
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
}
