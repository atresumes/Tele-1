package com.talktoangel.gts.settings;

import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.DatePicker;
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
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.utils.DatePickerFragment;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AddCreditCardActivity extends AppCompatActivity {
    private Button btnMonth;
    private Button btnYear;
    private EditText etCardName;
    private EditText et_cardNo;
    private EditText et_csv;
    private View mLoginFormView;
    private View mProgressView;
    SessionManager mSessionManager;
    int monthOrYear;
    OnDateSetListener onDate = new C06115();

    class C06071 implements OnClickListener {
        C06071() {
        }

        public void onClick(View view) {
            AddCreditCardActivity.this.checkFormData();
        }
    }

    class C06082 implements OnClickListener {
        C06082() {
        }

        public void onClick(View view) {
            AddCreditCardActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")));
        }
    }

    class C06093 implements OnClickListener {
        C06093() {
        }

        public void onClick(View view) {
            AddCreditCardActivity.this.monthOrYear = 0;
            AddCreditCardActivity.this.showDatePicker();
        }
    }

    class C06104 implements OnClickListener {
        C06104() {
        }

        public void onClick(View view) {
            AddCreditCardActivity.this.monthOrYear = 1;
            AddCreditCardActivity.this.showDatePicker();
        }
    }

    class C06115 implements OnDateSetListener {
        C06115() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (AddCreditCardActivity.this.monthOrYear == 0) {
                AddCreditCardActivity.this.btnMonth.setText((monthOfYear + 1) + "");
                return;
            }
            AddCreditCardActivity.this.btnYear.setText(year + "");
        }
    }

    class C10176 implements Listener<String> {
        C10176() {
        }

        public void onResponse(String response) {
            AddCreditCardActivity.this.showProgress(false);
            Log.e("Credit Card Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    Toast.makeText(AddCreditCardActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    AddCreditCardActivity.this.finish();
                    return;
                }
                AddCreditCardActivity.this.showProgress(false);
                Toast.makeText(AddCreditCardActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                AddCreditCardActivity.this.showProgress(false);
                e.printStackTrace();
                Toast.makeText(AddCreditCardActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10187 implements ErrorListener {
        C10187() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
            AddCreditCardActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_credit_card_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button paymentPolicy = (Button) findViewById(C0585R.id.btn_payment_policy);
        this.etCardName = (EditText) findViewById(C0585R.id.etCardHolderName);
        this.et_cardNo = (EditText) findViewById(C0585R.id.et_credit_card_no);
        this.btnMonth = (Button) findViewById(C0585R.id.btnMonth);
        this.btnYear = (Button) findViewById(C0585R.id.btnYear);
        this.et_csv = (EditText) findViewById(C0585R.id.et_csv);
        this.mLoginFormView = findViewById(C0585R.id.form2);
        this.mProgressView = findViewById(C0585R.id.reg_progress2);
        ((Button) findViewById(C0585R.id.btn_next)).setOnClickListener(new C06071());
        paymentPolicy.setOnClickListener(new C06082());
        this.btnMonth.setOnClickListener(new C06093());
        this.btnYear.setOnClickListener(new C06104());
        this.mSessionManager = new SessionManager(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void checkFormData() {
        this.etCardName.setError(null);
        this.et_cardNo.setError(null);
        this.btnMonth.setError(null);
        this.btnYear.setError(null);
        this.et_csv.setError(null);
        String name = this.etCardName.getText().toString().trim();
        String cardNo = this.et_cardNo.getText().toString().trim();
        String month = this.btnMonth.getText().toString().trim();
        String year = this.btnYear.getText().toString().trim();
        String csv = this.et_csv.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            this.etCardName.setError(getString(C0585R.string.error_field_required));
            focusView = this.etCardName;
            cancel = true;
        } else if (TextUtils.isEmpty(cardNo)) {
            this.et_cardNo.setError(getString(C0585R.string.error_field_required));
            focusView = this.et_cardNo;
            cancel = true;
        } else if (cardNo.length() < 14) {
            this.et_cardNo.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.et_cardNo;
            cancel = true;
        } else if (TextUtils.isEmpty(month)) {
            this.btnMonth.setError(getString(C0585R.string.error_field_required));
            focusView = this.btnMonth;
            cancel = true;
        } else if (TextUtils.isEmpty(year)) {
            this.btnYear.setError(getString(C0585R.string.error_field_required));
            focusView = this.btnYear;
            cancel = true;
        } else if (TextUtils.isEmpty(csv)) {
            this.et_csv.setError(getString(C0585R.string.error_field_required));
            focusView = this.et_csv;
            cancel = true;
        } else if (csv.length() < 3) {
            this.et_csv.setError(getString(C0585R.string.error_invalid_entry));
            focusView = this.et_csv;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        addNewCreditCard((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), name, cardNo, month, year, csv);
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(1));
        args.putInt("month", calender.get(2));
        args.putInt("day", calender.get(5));
        args.putInt("minMax", 0);
        date.setArguments(args);
        date.setCallBack(this.onDate);
        date.show(getSupportFragmentManager(), "Date Picker");
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

    void addNewCreditCard(String userId, String name, String cardNo, String month, String year, String cvv) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = userId;
        final String str2 = name;
        final String str3 = cardNo;
        final String str4 = month;
        final String str5 = year;
        final String str6 = cvv;
        Request strReq = new StringRequest(1, EndPoints.ADD_CARD_DETAIL, new C10176(), new C10187()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", str);
                params.put(SdkConstants.LABEL, str2);
                params.put("card_number", str3);
                params.put("card_validity", str4);
                params.put("card_validity", str5);
                params.put("cvv", str6);
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
