package com.talktoangel.gts.userauth;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
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
import com.talktoangel.gts.MainActivity;
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

public class CreditCardInfoActivity extends AppCompatActivity {
    EditText etCardName;
    EditText et_cardNo;
    EditText et_csv;
    Button et_month;
    Button et_year;
    View mLoginFormView;
    View mProgressView;
    private SessionManager mSessionManager;
    int monthOrYear;
    OnDateSetListener onDate = new C06427();

    class C06391 implements OnClickListener {
        C06391() {
        }

        public void onClick(View view) {
            CreditCardInfoActivity.this.checkFormData();
        }
    }

    class C06402 implements OnClickListener {
        C06402() {
        }

        public void onClick(View view) {
            CreditCardInfoActivity.this.monthOrYear = 0;
            CreditCardInfoActivity.this.showDatePicker();
        }
    }

    class C06413 implements OnClickListener {
        C06413() {
        }

        public void onClick(View view) {
            CreditCardInfoActivity.this.monthOrYear = 1;
            CreditCardInfoActivity.this.showDatePicker();
        }
    }

    class C06427 implements OnDateSetListener {
        C06427() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (CreditCardInfoActivity.this.monthOrYear == 0) {
                CreditCardInfoActivity.this.et_month.setText((monthOfYear + 1) + "");
                return;
            }
            CreditCardInfoActivity.this.et_year.setText(year + "");
        }
    }

    class C10594 implements Listener<String> {
        C10594() {
        }

        public void onResponse(String response) {
            CreditCardInfoActivity.this.showProgress(false);
            Log.e("Credit Card Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(CreditCardInfoActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    CreditCardInfoActivity.this.startActivity(new Intent(CreditCardInfoActivity.this.getApplicationContext(), MainActivity.class));
                    CreditCardInfoActivity.this.finish();
                    return;
                }
                CreditCardInfoActivity.this.showProgress(false);
                Toast.makeText(CreditCardInfoActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                CreditCardInfoActivity.this.showProgress(false);
                e.printStackTrace();
                Toast.makeText(CreditCardInfoActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10605 implements ErrorListener {
        C10605() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
            CreditCardInfoActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_credit_card_info);
        this.etCardName = (EditText) findViewById(C0585R.id.etCardHolderName);
        this.et_cardNo = (EditText) findViewById(C0585R.id.et_credit_card_no);
        this.et_month = (Button) findViewById(C0585R.id.btnMonth);
        this.et_year = (Button) findViewById(C0585R.id.btnYear);
        this.et_csv = (EditText) findViewById(C0585R.id.et_csv);
        this.mLoginFormView = findViewById(C0585R.id.form2);
        this.mProgressView = findViewById(C0585R.id.reg_progress2);
        ((Button) findViewById(C0585R.id.btn_next)).setOnClickListener(new C06391());
        this.et_month.setOnClickListener(new C06402());
        this.et_year.setOnClickListener(new C06413());
        this.mSessionManager = new SessionManager(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0585R.menu.menu_skip, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != C0585R.id.action_skip) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return true;
    }

    private void checkFormData() {
        this.etCardName.setError(null);
        this.et_cardNo.setError(null);
        this.et_month.setError(null);
        this.et_year.setError(null);
        this.et_csv.setError(null);
        String name = this.etCardName.getText().toString();
        String cardNo = this.et_cardNo.getText().toString();
        String month = this.et_month.getText().toString();
        String year = this.et_year.getText().toString();
        String csv = this.et_csv.getText().toString();
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
        } else if (TextUtils.isEmpty(month)) {
            this.et_month.setError(getString(C0585R.string.error_field_required));
            focusView = this.et_month;
            cancel = true;
        } else if (TextUtils.isEmpty(year)) {
            this.et_year.setError(getString(C0585R.string.error_field_required));
            focusView = this.et_year;
            cancel = true;
        } else if (TextUtils.isEmpty(csv)) {
            this.et_csv.setError(getString(C0585R.string.error_field_required));
            focusView = this.et_csv;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        UserLoginTask((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), cardNo, month + "/" + year, csv);
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

    private void UserLoginTask(String userId, String cardNo, String validity, String csv) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = userId;
        final String str2 = cardNo;
        final String str3 = validity;
        final String str4 = csv;
        Request strReq = new StringRequest(1, EndPoints.ADD_CARD_DETAIL, new C10594(), new C10605()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", str);
                params.put(SdkConstants.LABEL, str);
                params.put("card_number", str2);
                params.put("card_validity", str3);
                params.put("cvv", str4);
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
}
