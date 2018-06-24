package com.talktoangel.gts.userauth;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.talktoangel.gts.utils.DatePickerFragment;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration2Activity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = Registration2Activity.class.getSimpleName().toString();
    private SpinnerAdapter adapter;
    private SpinnerAdapter adapter1;
    private Button btnNoCo;
    private Button btnYesCo;
    private Button btn_dob;
    String countryH = "";
    private EditText etAddress;
    private EditText etAddress1;
    private EditText etAddress1H;
    private EditText etAddressH;
    private EditText etCity;
    private EditText etCityH;
    private EditText etPhoneNo;
    private EditText etZipCode;
    private EditText etZipCodeH;
    private LinearLayout layout1;
    private ArrayList<CountryCode> list;
    private ArrayList<CountryCode> list1;
    private View mProgressView;
    private SessionManager mSessionManager;
    private boolean minor = false;
    OnDateSetListener onDate = new C06526();
    boolean payInsurance = false;
    String pay_insurance = "n";
    private RadioButton radioFemale;
    private RadioGroup radioGroup;
    private RadioButton radioInr;
    private RadioButton radioMale;
    Spinner spCountry;
    Spinner spCountryH;
    Spinner spHearAbout;
    private TextView txtCode;

    class C06501 implements OnItemSelectedListener {
        C06501() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            CountryCode code = (CountryCode) Registration2Activity.this.list.get(position);
            if (position != 0) {
                Registration2Activity.this.txtCode.setText(code.getCode());
                Registration2Activity.this.mSessionManager.setCountry(code.getName());
                return;
            }
            Registration2Activity.this.txtCode.setText("");
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06512 implements OnItemSelectedListener {
        C06512() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position != 0) {
                CountryCode code = (CountryCode) Registration2Activity.this.list.get(position);
                Registration2Activity.this.countryH = code.getName();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06526 implements OnDateSetListener {
        C06526() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            int month;
            if (Registration2Activity.this.minor) {
                if (cal.get(1) - year >= 18) {
                    Toast.makeText(Registration2Activity.this, "Please select correct date", 0).show();
                    return;
                }
                month = monthOfYear + 1;
                Registration2Activity.this.btn_dob.setHint(month + "/" + dayOfMonth + "/" + year);
                Registration2Activity.this.btn_dob.setText(month + "/" + dayOfMonth + "/" + year);
            } else if (cal.get(1) - year <= 18) {
                Toast.makeText(Registration2Activity.this, "Please select correct date", 0).show();
            } else {
                month = monthOfYear + 1;
                Registration2Activity.this.btn_dob.setHint(month + "/" + dayOfMonth + "/" + year);
                Registration2Activity.this.btn_dob.setText(month + "/" + dayOfMonth + "/" + year);
            }
        }
    }

    class C10693 implements Listener<String> {
        C10693() {
        }

        public void onResponse(String response) {
            Log.e(Registration2Activity.TAG, "add_detail" + response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    Registration2Activity.this.showProgress(false);
                    Registration2Activity.this.mSessionManager.setUserType("p");
                    Toast.makeText(Registration2Activity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    Registration2Activity.this.startActivity(new Intent(Registration2Activity.this.getApplicationContext(), OTPActivity.class));
                    Registration2Activity.this.finish();
                    return;
                }
                Registration2Activity.this.showProgress(false);
                Toast.makeText(Registration2Activity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                Registration2Activity.this.showProgress(false);
                e.printStackTrace();
                Toast.makeText(Registration2Activity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10704 implements ErrorListener {
        C10704() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
            Registration2Activity.this.showProgress(false);
        }
    }

    class C10717 implements Listener<String> {
        C10717() {
        }

        public void onResponse(String response) {
            Log.e(Registration2Activity.TAG, "getCountryCodes " + response);
            try {
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                Registration2Activity.this.list.add(new CountryCode("0", "Select Country", "-", SdkConstants.NULL_STRING));
                Registration2Activity.this.list1.add(new CountryCode("0", "Select Country", "-", SdkConstants.NULL_STRING));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    String id = object1.getString("cntry_id");
                    String name = object1.getString(SdkConstants.COUNTRY);
                    String flag = object1.getString("flag");
                    String code = object1.getString("phonecode");
                    Registration2Activity.this.list.add(new CountryCode(id, name, code, flag));
                    Registration2Activity.this.list1.add(new CountryCode(id, name, code, flag));
                }
                Registration2Activity.this.adapter.notifyDataSetChanged();
                Registration2Activity.this.adapter1.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Registration2Activity.this.getApplicationContext(), "error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10728 implements ErrorListener {
        C10728() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_registration2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.minor = getIntent().getBooleanExtra("minor", false);
        this.radioGroup = (RadioGroup) findViewById(C0585R.id.gender_radio);
        this.radioMale = (RadioButton) findViewById(C0585R.id.radio_male);
        this.radioFemale = (RadioButton) findViewById(C0585R.id.radio_female);
        this.radioInr = (RadioButton) findViewById(C0585R.id.radio_inr);
        this.radioMale.setChecked(true);
        this.radioInr.setChecked(true);
        this.btn_dob = (Button) findViewById(C0585R.id.btn_dob);
        this.btnYesCo = (Button) findViewById(C0585R.id.btn_yes_r);
        this.btnNoCo = (Button) findViewById(C0585R.id.btn_no_r);
        Button btn_next = (Button) findViewById(C0585R.id.btn_next);
        this.etAddress = (EditText) findViewById(C0585R.id.address);
        this.etAddress1 = (EditText) findViewById(C0585R.id.address1);
        this.etCity = (EditText) findViewById(C0585R.id.city);
        this.etZipCode = (EditText) findViewById(C0585R.id.zip_code);
        this.etPhoneNo = (EditText) findViewById(C0585R.id.mobile_number);
        this.etAddressH = (EditText) findViewById(C0585R.id.etAddress);
        this.etAddress1H = (EditText) findViewById(C0585R.id.etAddress1);
        this.etCityH = (EditText) findViewById(C0585R.id.etCity);
        this.etZipCodeH = (EditText) findViewById(C0585R.id.etZipCode);
        this.spCountry = (Spinner) findViewById(C0585R.id.spCountryUr);
        this.txtCode = (TextView) findViewById(C0585R.id.txtCode);
        this.spHearAbout = (Spinner) findViewById(C0585R.id.sp_hear_about);
        this.spCountryH = (Spinner) findViewById(C0585R.id.spCountryH);
        this.layout1 = (LinearLayout) findViewById(C0585R.id.linear1);
        this.mProgressView = findViewById(C0585R.id.reg_progress1);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.btn_dob.setOnClickListener(this);
        this.btnYesCo.setOnClickListener(this);
        this.btnNoCo.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        this.spCountry.setOnItemSelectedListener(new C06501());
        this.spCountryH.setOnItemSelectedListener(new C06512());
        this.list = new ArrayList();
        this.list1 = new ArrayList();
        this.adapter = new SpinnerAdapter(this, this.list);
        this.adapter1 = new SpinnerAdapter(this, this.list1);
        this.spCountry.setAdapter(this.adapter);
        this.spCountryH.setAdapter(this.adapter1);
        getCountryCodes();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_next:
                validateFormData(view);
                return;
            case C0585R.id.btn_dob:
                showDatePicker();
                return;
            case C0585R.id.btn_no_r:
                this.payInsurance = false;
                this.pay_insurance = "n";
                this.btnNoCo.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_green_selected));
                this.btnYesCo.setBackgroundDrawable(null);
                this.btnYesCo.setTextColor(getResources().getColor(C0585R.color.colorPrimary));
                this.btnNoCo.setTextColor(getResources().getColor(C0585R.color.white));
                this.layout1.setVisibility(8);
                return;
            case C0585R.id.btn_yes_r:
                this.payInsurance = true;
                this.pay_insurance = "y";
                this.btnYesCo.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_green_selected));
                this.btnNoCo.setBackgroundDrawable(null);
                this.btnYesCo.setTextColor(getResources().getColor(C0585R.color.white));
                this.btnNoCo.setTextColor(getResources().getColor(C0585R.color.colorPrimary));
                this.layout1.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void validateFormData(View view) {
        String gender;
        String currencyPref;
        this.etAddress.setError(null);
        this.etAddress1.setError(null);
        this.etCity.setError(null);
        this.etZipCode.setError(null);
        this.etPhoneNo.setError(null);
        this.etAddressH.setError(null);
        this.etAddress1H.setError(null);
        this.etCityH.setError(null);
        this.etZipCodeH.setError(null);
        String hearAbout = "";
        String address = this.etAddress.getText().toString().trim();
        String address1 = this.etAddress1.getText().toString().trim();
        String city = this.etCity.getText().toString().trim();
        String zipCode = this.etZipCode.getText().toString().trim();
        String phoneNo = this.etPhoneNo.getText().toString().trim();
        String addressH = this.etAddressH.getText().toString().trim();
        String address1H = this.etAddress1H.getText().toString().trim();
        String cityH = this.etCityH.getText().toString().trim();
        String zipCodeH = this.etZipCodeH.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        int selectedId = this.radioGroup.getCheckedRadioButtonId();
        if (selectedId == this.radioMale.getId()) {
            gender = "male";
        } else if (selectedId == this.radioFemale.getId()) {
            gender = "female";
        } else {
            gender = "other";
        }
        if (this.radioInr.isChecked()) {
            currencyPref = "INR";
        } else {
            currencyPref = "USD";
        }
        if (this.btn_dob.getHint().toString().equals(getResources().getString(C0585R.string.date_format))) {
            Snackbar.make(view, (CharSequence) "Select your Date of birth", -1).show();
            focusView = this.btn_dob;
            cancel = true;
        } else if (TextUtils.isEmpty(address)) {
            this.etAddress.setError(getString(C0585R.string.error_field_required));
            focusView = this.etAddress;
            cancel = true;
        } else if (address.length() < 2) {
            this.etAddress.setError(getString(C0585R.string.error_too_short));
            focusView = this.etAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(city)) {
            this.etCity.setError(getString(C0585R.string.error_field_required));
            focusView = this.etCity;
            cancel = true;
        } else if (!Pattern.matches("[a-zA-Z]+", city)) {
            this.etCity.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.etCity;
            cancel = true;
        } else if (city.length() < 2) {
            this.etCity.setError(getString(C0585R.string.error_too_short));
            focusView = this.etCity;
            cancel = true;
        } else if (this.spCountry.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select your Country", -1).show();
            focusView = this.spCountry;
            cancel = true;
        } else if (TextUtils.isEmpty(zipCode)) {
            this.etZipCode.setError(getString(C0585R.string.error_field_required));
            focusView = this.etZipCode;
            cancel = true;
        } else if (zipCode.length() != 6) {
            this.etZipCode.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.etZipCode;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNo)) {
            this.etPhoneNo.setError(getString(C0585R.string.error_field_required));
            focusView = this.etPhoneNo;
            cancel = true;
        } else if (phoneNo.length() <= 4) {
            this.etPhoneNo.setError(getString(C0585R.string.error_invalid_mobile));
            focusView = this.etPhoneNo;
            cancel = true;
        }
        if (this.spHearAbout.getSelectedItemPosition() != 0) {
            hearAbout = this.spHearAbout.getSelectedItem().toString();
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        updateUserDetails(this.btn_dob.getText().toString(), gender, address, address1, city, (String) this.mSessionManager.getUser().get("state"), zipCode, phoneNo, this.txtCode.getText().toString(), this.pay_insurance, currencyPref, hearAbout, "", "", "", "", "", "", "", "", addressH, address1H, cityH, this.countryH, zipCodeH);
    }

    private void showProgress(boolean show) {
        this.mProgressView.setVisibility(show ? 0 : 8);
    }

    private void updateUserDetails(String dob, String gender, String addressLine, String addressLine1, String city, String country, String zipCode, String mobile, String countryCode, String payInsurance, String currencyPref, String aboutTTA, String insuranceId, String insuranceType, String group, String planName, String relation, String firstName, String lastName, String dobH, String addressH, String address1H, String cityH, String countryH, String zipCodeH) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = dob;
        final String str2 = gender;
        final String str3 = addressLine;
        final String str4 = addressLine1;
        final String str5 = city;
        final String str6 = country;
        final String str7 = zipCode;
        final String str8 = mobile;
        final String str9 = countryCode;
        final String str10 = currencyPref;
        final String str11 = aboutTTA;
        final String str12 = payInsurance;
        final String str13 = insuranceType;
        final String str14 = insuranceId;
        final String str15 = group;
        final String str16 = planName;
        final String str17 = relation;
        final String str18 = firstName;
        final String str19 = lastName;
        final String str20 = dobH;
        final String str21 = addressH;
        final String str22 = address1H;
        final String str23 = cityH;
        final String str24 = countryH;
        final String str25 = zipCodeH;
        Request strReq = new StringRequest(1, EndPoints.ADD_DETAIL, new C10693(), new C10704()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", Registration2Activity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put("dob", str);
                params.put(SessionManager.KEY_GENDER, str2);
                params.put("add_line", str3);
                params.put("apt_bldg", str4);
                params.put("city", str5);
                params.put("state", str6);
                params.put(SdkConstants.ZIPCODE, str7);
                params.put("mobile", str8);
                params.put(SessionManager.COUNTRY_CODE, str9);
                params.put(SessionManager.KEY_CURRENCY_PREF, str10);
                params.put("about_tta", str11);
                params.put("pay_insurance", str12);
                params.put("Health_insurance", str13);
                params.put("insurance_id", str14);
                params.put("group", str15);
                params.put("plan_nm", str16);
                params.put("relationship_policy", str17);
                params.put("policyholder_fname", str18);
                params.put("policyholder_lname", str19);
                params.put("policyholder_dob", str20);
                params.put("ins_add_line", str21);
                params.put("ins_apt_bldg", str22);
                params.put("ins_city", str23);
                params.put("ins_state", str24);
                params.put("ins_zipcode", str25);
                Log.e(Registration2Activity.TAG, "params " + params.toString());
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

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(1));
        args.putInt("month", calender.get(2));
        args.putInt("day", calender.get(5));
        args.putInt("minMax", 1);
        date.setArguments(args);
        date.setCallBack(this.onDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    private void getCountryCodes() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(0, EndPoints.COUNTRY_LIST, new C10717(), new C10728());
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
