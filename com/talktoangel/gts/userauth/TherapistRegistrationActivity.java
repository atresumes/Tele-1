package com.talktoangel.gts.userauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.talktoangel.gts.therapist.LanguageListActivity;
import com.talktoangel.gts.therapist.SpecialityListActivity;
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

public class TherapistRegistrationActivity extends AppCompatActivity implements OnClickListener {
    private Button btnLanguage;
    private Button btnSpeciality;
    private EditText etExperience;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPassword;
    private EditText mEmailView;
    private View mLoginFormView;
    private View mProgressView;
    private EditText mReEmailView;
    SessionManager mSessionManager;
    private RadioButton radioFemale;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    String specialities = "";

    class C10771 implements Listener<String> {
        C10771() {
        }

        public void onResponse(String response) {
            Log.e("Registration Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    TherapistRegistrationActivity.this.showProgress(false);
                    TherapistRegistrationActivity.this.mSessionManager.setUserID(jObj.getJSONObject(SdkConstants.RESULT).getString("dr_id"));
                    TherapistRegistrationActivity.this.mSessionManager.setUserType("t");
                    TherapistRegistrationActivity.this.mSessionManager.setMobileVerified(false);
                    Toast.makeText(TherapistRegistrationActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    TherapistRegistrationActivity.this.startActivity(new Intent(TherapistRegistrationActivity.this.getApplicationContext(), TherapistRegistration2Activity.class));
                    TherapistRegistrationActivity.this.finish();
                    return;
                }
                TherapistRegistrationActivity.this.showProgress(false);
                Toast.makeText(TherapistRegistrationActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                TherapistRegistrationActivity.this.showProgress(false);
            }
        }
    }

    class C10782 implements ErrorListener {
        C10782() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
            TherapistRegistrationActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_provider_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mEmailView = (EditText) findViewById(C0585R.id.email_apr);
        this.mReEmailView = (EditText) findViewById(C0585R.id.retype_email_apr);
        this.etFirstName = (EditText) findViewById(C0585R.id.first_name_apr);
        this.etLastName = (EditText) findViewById(C0585R.id.last_name_apr);
        this.etPassword = (EditText) findViewById(C0585R.id.password_apr);
        this.etExperience = (EditText) findViewById(C0585R.id.et_experience_app);
        this.radioGroup = (RadioGroup) findViewById(C0585R.id.gender_radio);
        this.radioMale = (RadioButton) findViewById(C0585R.id.radio_male);
        this.radioFemale = (RadioButton) findViewById(C0585R.id.radio_female);
        this.radioMale.setChecked(true);
        this.btnSpeciality = (Button) findViewById(C0585R.id.btn_speciality_apr);
        this.btnLanguage = (Button) findViewById(C0585R.id.btn_lang_apr);
        Button btnTermsCondition = (Button) findViewById(C0585R.id.btn_terms_cond);
        Button btnPrivacyPolicy = (Button) findViewById(C0585R.id.btn_privacy_policy);
        Button mEmailSignInButton = (Button) findViewById(C0585R.id.btn_sign_up_apr);
        this.mLoginFormView = findViewById(C0585R.id.reg_form);
        this.mProgressView = findViewById(C0585R.id.reg_progress);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.btnSpeciality.setOnClickListener(this);
        this.btnLanguage.setOnClickListener(this);
        btnTermsCondition.setOnClickListener(this);
        btnPrivacyPolicy.setOnClickListener(this);
        mEmailSignInButton.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_speciality_apr:
                startActivityForResult(new Intent(this, SpecialityListActivity.class), 0);
                return;
            case C0585R.id.btn_lang_apr:
                startActivityForResult(new Intent(this, LanguageListActivity.class), 1);
                return;
            case C0585R.id.btn_terms_cond:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")));
                return;
            case C0585R.id.btn_privacy_policy:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.co.in")));
                return;
            case C0585R.id.btn_sign_up_apr:
                attemptLogin();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 0) {
                this.specialities = data.getStringExtra("id");
                this.btnSpeciality.setText(data.getStringExtra(SdkConstants.DATA));
            } else if (requestCode == 1) {
                this.btnLanguage.setText(data.getStringExtra(SdkConstants.DATA));
            }
            Log.e("TAG", this.specialities);
        }
    }

    private void attemptLogin() {
        this.etFirstName.setError(null);
        this.etLastName.setError(null);
        this.mEmailView.setError(null);
        this.mReEmailView.setError(null);
        this.etPassword.setError(null);
        this.etExperience.setError(null);
        String firstName = this.etFirstName.getText().toString().trim();
        String lastName = this.etLastName.getText().toString().trim();
        String email = this.mEmailView.getText().toString().trim();
        String reEmail = this.mReEmailView.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        String experience = this.etExperience.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(firstName)) {
            this.etFirstName.setError(getString(C0585R.string.error_field_required));
            focusView = this.etFirstName;
            cancel = true;
        } else if (!Pattern.matches("[a-zA-Z]+", firstName)) {
            this.etFirstName.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.etFirstName;
            cancel = true;
        } else if (firstName.length() < 2) {
            this.etFirstName.setError(getString(C0585R.string.error_too_short));
            focusView = this.etFirstName;
            cancel = true;
        } else if (TextUtils.isEmpty(lastName)) {
            this.etLastName.setError(getString(C0585R.string.error_field_required));
            focusView = this.etLastName;
            cancel = true;
        } else if (!Pattern.matches("[a-zA-Z]+", lastName)) {
            this.etLastName.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.etLastName;
            cancel = true;
        } else if (lastName.length() < 2) {
            this.etLastName.setError(getString(C0585R.string.error_too_short));
            focusView = this.etLastName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            this.mEmailView.setError(getString(C0585R.string.error_field_required));
            focusView = this.mEmailView;
            cancel = true;
        } else if (!email.matches(Constant.EMAIL_PATTERN)) {
            this.mEmailView.setError(getString(C0585R.string.error_invalid_email));
            focusView = this.mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(reEmail)) {
            this.mReEmailView.setError(getString(C0585R.string.error_field_required));
            focusView = this.mReEmailView;
            cancel = true;
        } else if (!reEmail.matches(Constant.EMAIL_PATTERN)) {
            this.mReEmailView.setError(getString(C0585R.string.error_invalid_email));
            focusView = this.mReEmailView;
            cancel = true;
        } else if (!email.matches(reEmail)) {
            this.mEmailView.setError(getString(C0585R.string.error_same_email));
            this.mReEmailView.setError(getString(C0585R.string.error_same_email));
            focusView = this.mReEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            this.etPassword.setError(getString(C0585R.string.error_field_required));
            focusView = this.etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            this.etPassword.setError(getString(C0585R.string.error_invalid_password));
            focusView = this.etPassword;
            cancel = true;
        } else if (this.btnSpeciality.getText().equals("")) {
            this.btnSpeciality.setError(getString(C0585R.string.error_invalid_password));
            focusView = this.btnSpeciality;
            cancel = true;
        } else if (TextUtils.isEmpty(experience)) {
            this.etExperience.setError(getString(C0585R.string.error_field_required));
            focusView = this.etExperience;
            cancel = true;
        } else if (this.btnLanguage.getText().equals("")) {
            this.btnLanguage.setError(getString(C0585R.string.error_invalid_password));
            focusView = this.btnLanguage;
            cancel = true;
        }
        String gender = "";
        int selectedId = this.radioGroup.getCheckedRadioButtonId();
        if (selectedId == this.radioMale.getId()) {
            gender = "male";
        } else if (selectedId == this.radioFemale.getId()) {
            gender = "female";
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        String deviceID = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null);
        UserLoginTask(firstName, lastName, email, password, gender, this.specialities, experience, this.btnLanguage.getText().toString(), deviceID);
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

    private void UserLoginTask(String firstName, String lastName, String email, String password, String gender, String speciality, String experience, String language, String deviceId) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = firstName;
        final String str2 = lastName;
        final String str3 = email;
        final String str4 = password;
        final String str5 = gender;
        final String str6 = speciality;
        final String str7 = experience;
        final String str8 = language;
        final String str9 = deviceId;
        Request strReq = new StringRequest(1, EndPoints.REGISTER_PROVIDER, new C10771(), new C10782()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_TYPE, "t");
                params.put("dr_fname", str);
                params.put("dr_lname", str2);
                params.put("dr_email", str3);
                params.put("dr_password", str4);
                params.put(SessionManager.KEY_GENDER, str5);
                params.put("dr_speciality", str6);
                params.put(SessionManager.KEY_EXPERIENCE, str7);
                params.put("dr_language", str8);
                params.put("device_id", str9);
                Log.e("TAG", params.toString());
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
