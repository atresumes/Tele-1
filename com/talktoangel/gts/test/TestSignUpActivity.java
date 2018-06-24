package com.talktoangel.gts.test;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class TestSignUpActivity extends AppCompatActivity implements OnClickListener {
    private EditText etAge;
    private EditText etContactNo;
    private EditText etEmail;
    private EditText etName;
    private EditText etParentName;
    private EditText etPassword;
    private EditText etSchool;
    private EditText etSubject;
    private View mLoginFormView;
    private View mProgressView;
    private SessionManager session;
    private Spinner spClassName;

    class C10321 implements Listener<String> {
        C10321() {
        }

        public void onResponse(String response) {
            Log.e("Registration", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    TestSignUpActivity.this.showProgress(false);
                    TestSignUpActivity.this.session.setBuddyId(jObj.getJSONObject(SdkConstants.RESULT).getString(SessionManager.BUDDY_ID));
                    Toast.makeText(TestSignUpActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    TestSignUpActivity.this.finish();
                    return;
                }
                TestSignUpActivity.this.showProgress(false);
                Toast.makeText(TestSignUpActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                TestSignUpActivity.this.showProgress(false);
                e.printStackTrace();
                Toast.makeText(TestSignUpActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10332 implements ErrorListener {
        C10332() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
            TestSignUpActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_test_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.etName = (EditText) findViewById(C0585R.id.etName);
        this.etAge = (EditText) findViewById(C0585R.id.etAge);
        this.etParentName = (EditText) findViewById(C0585R.id.etParentName);
        this.etEmail = (EditText) findViewById(C0585R.id.etEmail);
        this.etContactNo = (EditText) findViewById(C0585R.id.etContactNo);
        this.etSchool = (EditText) findViewById(C0585R.id.etSchoolName);
        this.etSubject = (EditText) findViewById(C0585R.id.etSubject);
        this.etSubject = (EditText) findViewById(C0585R.id.etSubject);
        this.etPassword = (EditText) findViewById(C0585R.id.etPassword);
        this.spClassName = (Spinner) findViewById(C0585R.id.spClassName);
        this.mLoginFormView = findViewById(C0585R.id.test_form);
        this.mProgressView = findViewById(C0585R.id.progress_test);
        this.session = new SessionManager(this);
        ((Button) findViewById(C0585R.id.btn_register)).setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void attemptLogin(View view) {
        this.etName.setError(null);
        this.etAge.setError(null);
        this.etParentName.setError(null);
        this.etEmail.setError(null);
        this.etContactNo.setError(null);
        this.etSchool.setError(null);
        this.etSubject.setError(null);
        this.etPassword.setError(null);
        String name = this.etName.getText().toString().trim();
        String age = this.etAge.getText().toString().trim();
        String parentName = this.etParentName.getText().toString().trim();
        String email = this.etEmail.getText().toString().trim();
        String contactNo = this.etContactNo.getText().toString().trim();
        String school = this.etSchool.getText().toString().trim();
        String subject = this.etSubject.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            this.etName.setError(getString(C0585R.string.error_field_required));
            focusView = this.etName;
            cancel = true;
        } else if (!Pattern.matches("[a-zA-Z]+", name)) {
            this.etName.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.etName;
            cancel = true;
        } else if (name.length() < 2) {
            this.etName.setError(getString(C0585R.string.error_too_short));
            focusView = this.etName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            this.etEmail.setError(getString(C0585R.string.error_field_required));
            focusView = this.etEmail;
            cancel = true;
        } else if (!email.matches(Constant.EMAIL_PATTERN)) {
            this.etEmail.setError(getString(C0585R.string.error_invalid_email));
            focusView = this.etEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            this.etPassword.setError(getString(C0585R.string.error_field_required));
            focusView = this.etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            this.etPassword.setError(getString(C0585R.string.error_invalid_password));
            focusView = this.etPassword;
            cancel = true;
        } else if (this.spClassName.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select a security question", -1).show();
            focusView = this.spClassName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        TestRegisterTask(name, age, this.spClassName.getSelectedItem().toString(), parentName, email, contactNo, school, subject, password);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void showProgress(boolean show) {
        int i;
        int i2 = 8;
        View view = this.mProgressView;
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.mLoginFormView;
        if (!show) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btn_register:
                attemptLogin(v);
                return;
            default:
                return;
        }
    }

    private void TestRegisterTask(String name, String age, String className, String parent, String email, String contact, String school, String subject, String password) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = name;
        final String str2 = age;
        final String str3 = className;
        final String str4 = parent;
        final String str5 = email;
        final String str6 = contact;
        final String str7 = school;
        final String str8 = subject;
        final String str9 = password;
        Request strReq = new StringRequest(1, EndPoints.TEST_REGISTER, new C10321(), new C10332()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("device_type", "a");
                params.put("name", str);
                params.put("age", str2);
                params.put("class_name", str3);
                params.put("parent_name", str4);
                params.put("email", str5);
                params.put("contact_no", str6);
                params.put("school_name", str7);
                params.put("subject", str8);
                params.put(SdkConstants.PASSWORD, str9);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            HttpStack hurlStack = new HurlStack(null, new TLSSocketFactory());
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
