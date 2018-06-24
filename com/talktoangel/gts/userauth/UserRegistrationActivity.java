package com.talktoangel.gts.userauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.talktoangel.gts.adapter.SpinnerAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.CountryCode;
import com.talktoangel.gts.utils.Constant;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistrationActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = UserRegistrationActivity.class.getSimpleName();
    private SpinnerAdapter adapter;
    String country = "";
    private EditText etAddress1R;
    private EditText etAddressR;
    private EditText etAnswer;
    private EditText etCityR;
    private EditText etFirstName;
    private EditText etFirstNameR;
    private EditText etLastName;
    private EditText etLastNameR;
    private EditText etPassword;
    private EditText etPhoneR;
    private EditText etZipCodeR;
    private ArrayList<CountryCode> list;
    private AutoCompleteTextView mEmailView;
    private View mLoginFormView;
    private View mProgressView;
    private AutoCompleteTextView mReEmailView;
    private SessionManager mSessionManager;
    private boolean minor = false;
    private Spinner spCountry;
    private Spinner sqSpinner;
    String subscribe = "n";

    class C06541 implements OnEditorActionListener {
        C06541() {
        }

        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id != C0585R.id.login && id != 0) {
                return false;
            }
            UserRegistrationActivity.this.attemptLogin(textView);
            return true;
        }
    }

    class C06563 implements OnCheckedChangeListener {
        C06563() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                UserRegistrationActivity.this.subscribe = "y";
                return;
            }
            UserRegistrationActivity.this.subscribe = "n";
        }
    }

    class C06574 implements OnItemSelectedListener {
        C06574() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position != 0) {
                UserRegistrationActivity.this.mSessionManager.setSecQues(String.valueOf(adapterView.getSelectedItem()));
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C10796 implements Listener<String> {
        C10796() {
        }

        public void onResponse(String response) {
            Log.e(UserRegistrationActivity.TAG, response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    UserRegistrationActivity.this.showProgress(false);
                    UserRegistrationActivity.this.mSessionManager.setUserID(jObj.getJSONObject(SdkConstants.RESULT).getString("id"));
                    UserRegistrationActivity.this.mSessionManager.setMobileVerified(false);
                    UserRegistrationActivity.this.mSessionManager.setUserType("p");
                    Toast.makeText(UserRegistrationActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    UserRegistrationActivity.this.startActivity(new Intent(UserRegistrationActivity.this.getApplicationContext(), Registration2Activity.class).putExtra("minor", UserRegistrationActivity.this.minor));
                    UserRegistrationActivity.this.finish();
                    return;
                }
                UserRegistrationActivity.this.showProgress(false);
                Toast.makeText(UserRegistrationActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                UserRegistrationActivity.this.showProgress(false);
                e.printStackTrace();
                Toast.makeText(UserRegistrationActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10807 implements ErrorListener {
        C10807() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e(UserRegistrationActivity.TAG, "Login Error: " + error.getMessage());
            UserRegistrationActivity.this.showProgress(false);
        }
    }

    class C10819 implements Listener<String> {
        C10819() {
        }

        public void onResponse(String response) {
            Log.e(UserRegistrationActivity.TAG, response);
            try {
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                UserRegistrationActivity.this.list.add(new CountryCode("0", "Select Country", "--", SdkConstants.NULL_STRING));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    String id = object1.getString("cntry_id");
                    String name = object1.getString(SdkConstants.COUNTRY);
                    String flag = object1.getString("flag");
                    UserRegistrationActivity.this.list.add(new CountryCode(id, name, object1.getString("phonecode"), flag));
                }
                UserRegistrationActivity.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UserRegistrationActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_user_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mEmailView = (AutoCompleteTextView) findViewById(C0585R.id.email);
        this.mReEmailView = (AutoCompleteTextView) findViewById(C0585R.id.retype_email);
        CheckBox minorBox = (CheckBox) findViewById(C0585R.id.checkbox_minor);
        final LinearLayout minorLayout = (LinearLayout) findViewById(C0585R.id.minor_layout);
        final LinearLayout layout = (LinearLayout) findViewById(C0585R.id.card_representative);
        this.etFirstName = (EditText) findViewById(C0585R.id.firstName);
        this.etLastName = (EditText) findViewById(C0585R.id.lastName);
        this.etPassword = (EditText) findViewById(C0585R.id.password);
        this.etAnswer = (EditText) findViewById(C0585R.id.answer);
        this.etFirstNameR = (EditText) findViewById(C0585R.id.et_represnt_fn);
        this.etLastNameR = (EditText) findViewById(C0585R.id.et_represnt_ln);
        this.etPhoneR = (EditText) findViewById(C0585R.id.etPhoneR);
        this.etAddressR = (EditText) findViewById(C0585R.id.et_represnt_address);
        this.etAddress1R = (EditText) findViewById(C0585R.id.et_represnt_address1);
        this.etCityR = (EditText) findViewById(C0585R.id.et_represnt_city);
        this.etZipCodeR = (EditText) findViewById(C0585R.id.et_represnt_zip_code);
        this.sqSpinner = (Spinner) findViewById(C0585R.id.sp_sec_ques);
        this.spCountry = (Spinner) findViewById(C0585R.id.sp_represnt_state);
        CheckBox checkBox = (CheckBox) findViewById(C0585R.id.checkbox_terms);
        final TextView txtCode = (TextView) findViewById(C0585R.id.txtCode);
        Button btnTermsCondition = (Button) findViewById(C0585R.id.btn_terms_cond);
        Button btnPrivacyPolicy = (Button) findViewById(C0585R.id.btn_privacy_policy);
        Button mEmailSignInButton = (Button) findViewById(C0585R.id.sign_up_button);
        this.etPassword.setOnEditorActionListener(new C06541());
        minorBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    UserRegistrationActivity.this.minor = true;
                    minorLayout.setVisibility(0);
                    layout.setVisibility(0);
                    return;
                }
                minorLayout.setVisibility(8);
                layout.setVisibility(8);
                UserRegistrationActivity.this.minor = false;
            }
        });
        checkBox.setOnCheckedChangeListener(new C06563());
        this.mLoginFormView = findViewById(C0585R.id.reg_form);
        this.mProgressView = findViewById(C0585R.id.reg_progress);
        this.mSessionManager = new SessionManager(getApplicationContext());
        btnTermsCondition.setOnClickListener(this);
        btnPrivacyPolicy.setOnClickListener(this);
        mEmailSignInButton.setOnClickListener(this);
        this.sqSpinner.setOnItemSelectedListener(new C06574());
        this.spCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CountryCode code = (CountryCode) UserRegistrationActivity.this.list.get(position);
                if (position != 0) {
                    txtCode.setText(code.getCode());
                    UserRegistrationActivity.this.country = code.getName();
                    return;
                }
                txtCode.setText("");
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.list = new ArrayList();
        this.adapter = new SpinnerAdapter(this, this.list);
        this.spCountry.setAdapter(this.adapter);
        getCountryCodes();
    }

    private void attemptLogin(View view) {
        this.etFirstName.setError(null);
        this.etLastName.setError(null);
        this.mEmailView.setError(null);
        this.mReEmailView.setError(null);
        this.etPassword.setError(null);
        this.etAnswer.setError(null);
        this.etFirstNameR.setError(null);
        this.etLastNameR.setError(null);
        this.etPhoneR.setError(null);
        this.etAddressR.setError(null);
        this.etCityR.setError(null);
        this.etZipCodeR.setError(null);
        String firstName = this.etFirstName.getText().toString().trim();
        String lastName = this.etLastName.getText().toString().trim();
        String email = this.mEmailView.getText().toString().trim();
        String reEmail = this.mReEmailView.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        String answer = this.etAnswer.getText().toString().trim();
        String firstNameR = this.etFirstNameR.getText().toString().trim();
        String lastNameR = this.etLastNameR.getText().toString().trim();
        String phoneR = this.etPhoneR.getText().toString().trim();
        String addressR = this.etAddressR.getText().toString().trim();
        String address1R = this.etAddress1R.getText().toString().trim();
        String cityR = this.etCityR.getText().toString().trim();
        String zipCodeR = this.etZipCodeR.getText().toString().trim();
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
        } else {
            if (!reEmail.matches(Constant.EMAIL_PATTERN)) {
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
            } else if (this.sqSpinner.getSelectedItemPosition() == 0) {
                Snackbar.make(view, (CharSequence) "Please select a security question", -1).show();
                focusView = this.sqSpinner;
                cancel = true;
            } else if (TextUtils.isEmpty(answer)) {
                this.etAnswer.setError(getString(C0585R.string.error_field_required));
                focusView = this.etAnswer;
                cancel = true;
            } else if (!Pattern.matches("[a-zA-Z]+", answer)) {
                this.etAnswer.setError(getString(C0585R.string.error_invalid_entry));
                focusView = this.etAnswer;
                cancel = true;
            } else if (answer.length() < 2) {
                this.etLastName.setError(getString(C0585R.string.error_too_short));
                focusView = this.etLastName;
                cancel = true;
            } else if (this.minor) {
                if (TextUtils.isEmpty(firstNameR)) {
                    this.etFirstNameR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etFirstNameR;
                    cancel = true;
                } else if (!Pattern.matches("[a-zA-Z]+", firstNameR)) {
                    this.etFirstNameR.setError(getString(C0585R.string.error_invalid_entry));
                    focusView = this.etFirstNameR;
                    cancel = true;
                } else if (firstNameR.length() < 2) {
                    this.etFirstNameR.setError(getString(C0585R.string.error_too_short));
                    focusView = this.etFirstNameR;
                    cancel = true;
                } else if (TextUtils.isEmpty(lastNameR)) {
                    this.etLastNameR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etLastNameR;
                    cancel = true;
                } else if (!Pattern.matches("[a-zA-Z]+", lastNameR)) {
                    this.etLastNameR.setError(getString(C0585R.string.error_invalid_entry));
                    focusView = this.etLastNameR;
                    cancel = true;
                } else if (lastNameR.length() < 2) {
                    this.etLastNameR.setError(getString(C0585R.string.error_too_short));
                    focusView = this.etLastNameR;
                    cancel = true;
                } else if (TextUtils.isEmpty(phoneR)) {
                    this.etPhoneR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etPhoneR;
                    cancel = true;
                } else if (TextUtils.isEmpty(addressR)) {
                    this.etAddressR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etAddressR;
                    cancel = true;
                } else if (addressR.length() < 2) {
                    this.etAddressR.setError(getString(C0585R.string.error_too_short));
                    focusView = this.etAddressR;
                    cancel = true;
                } else if (TextUtils.isEmpty(cityR)) {
                    this.etCityR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etCityR;
                    cancel = true;
                } else if (!Pattern.matches("[a-zA-Z]+", cityR)) {
                    this.etCityR.setError(getString(C0585R.string.error_invalid_entry));
                    focusView = this.etCityR;
                    cancel = true;
                } else if (cityR.length() < 2) {
                    this.etCityR.setError(getString(C0585R.string.error_too_short));
                    focusView = this.etCityR;
                    cancel = true;
                } else if (this.spCountry.getSelectedItemPosition() == 0) {
                    Snackbar.make(view, (CharSequence) "Please select representative's country", -1).show();
                    focusView = this.spCountry;
                    cancel = true;
                } else if (TextUtils.isEmpty(zipCodeR)) {
                    this.etZipCodeR.setError(getString(C0585R.string.error_field_required));
                    focusView = this.etZipCodeR;
                    cancel = true;
                } else if (zipCodeR.length() != 6) {
                    this.etZipCodeR.setError(getString(C0585R.string.error_invalid_entry));
                    focusView = this.etZipCodeR;
                    cancel = true;
                }
            }
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        UserRegisterTask(firstName, lastName, email, password, this.sqSpinner.getSelectedItem().toString(), answer, this.subscribe, firstNameR, lastNameR, phoneR, addressR, address1R, cityR, this.country, zipCodeR, getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null));
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

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_terms_cond:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")));
                return;
            case C0585R.id.btn_privacy_policy:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.co.in")));
                return;
            case C0585R.id.sign_up_button:
                attemptLogin(view);
                return;
            default:
                return;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return true;
    }

    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void UserRegisterTask(String firstName, String lastName, String email, String password, String securityQuestion, String answer, String subscribe, String firstNameR, String lastNameR, String phoneR, String address1R, String address2R, String cityR, String stateR, String zipCodeR, String deviceId) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = firstName;
        final String str2 = lastName;
        final String str3 = email;
        final String str4 = password;
        final String str5 = securityQuestion;
        final String str6 = answer;
        final String str7 = subscribe;
        final String str8 = firstNameR;
        final String str9 = lastNameR;
        final String str10 = phoneR;
        final String str11 = address1R;
        final String str12 = address2R;
        final String str13 = cityR;
        final String str14 = stateR;
        final String str15 = zipCodeR;
        final String str16 = deviceId;
        Request strReq = new StringRequest(1, EndPoints.REGISTER, new C10796(), new C10807()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_TYPE, "p");
                params.put(SessionManager.KEY_FIRST_NAME, str);
                params.put(SessionManager.KEY_LAST_NAME, str2);
                params.put("email", str3);
                params.put(SdkConstants.PASSWORD, str4);
                params.put("security_ques", str5);
                params.put("answer", str6);
                params.put("subscribe", str7);
                params.put("guardian_fname", str8);
                params.put("guardian_lname", str9);
                params.put("guardian_mobile", str10);
                params.put("guardian_addline", str11);
                params.put("guardian_aptbldg", str12);
                params.put("guardian_city", str13);
                params.put("guardian_state", str14);
                params.put("guardian_zipcode", str15);
                params.put("device_id", str16);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            HttpStack hurlStack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e(TAG, "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e(TAG, "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }

    private void getCountryCodes() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(0, EndPoints.COUNTRY_LIST, new C10819(), new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(UserRegistrationActivity.TAG, "" + error.getMessage());
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e(TAG, "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e(TAG, "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }
}
