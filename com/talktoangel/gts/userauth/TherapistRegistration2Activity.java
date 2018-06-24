package com.talktoangel.gts.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.therapist.AppointmentTypeActivity;
import com.talktoangel.gts.therapist.TherapistTypeActivity;
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

public class TherapistRegistration2Activity extends AppCompatActivity implements OnClickListener {
    String apoType;
    private Button btnApoType;
    private Button btnTherapistType;
    private EditText etLicenseNo;
    private EditText etMobile;
    private EditText etRate;
    private View mProgressView;
    SessionManager mSessionManager;
    String therapistType;

    class C10751 implements Listener<String> {
        C10751() {
        }

        public void onResponse(String response) {
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    TherapistRegistration2Activity.this.showProgress(false);
                    TherapistRegistration2Activity.this.mSessionManager.setUserType("t");
                    TherapistRegistration2Activity.this.mSessionManager.setMobileVerified(false);
                    Toast.makeText(TherapistRegistration2Activity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    TherapistRegistration2Activity.this.startActivity(new Intent(TherapistRegistration2Activity.this.getApplicationContext(), OTPActivity.class));
                    TherapistRegistration2Activity.this.finish();
                    return;
                }
                TherapistRegistration2Activity.this.showProgress(false);
                Toast.makeText(TherapistRegistration2Activity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                TherapistRegistration2Activity.this.showProgress(false);
            }
        }
    }

    class C10762 implements ErrorListener {
        C10762() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
            TherapistRegistration2Activity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_provider_registration2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.etLicenseNo = (EditText) findViewById(C0585R.id.et_license_apr);
        this.etRate = (EditText) findViewById(C0585R.id.et_charge_apr);
        this.etMobile = (EditText) findViewById(C0585R.id.et_mobile_apr);
        this.btnTherapistType = (Button) findViewById(C0585R.id.btn_therapist_type_apr);
        this.btnApoType = (Button) findViewById(C0585R.id.btn_apo_type_apr);
        Button btnNext = (Button) findViewById(C0585R.id.btn_next_apr);
        this.mProgressView = findViewById(C0585R.id.reg_progress1);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.btnTherapistType.setOnClickListener(this);
        this.btnApoType.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TherapistRegistrationActivity.class));
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_therapist_type_apr:
                startActivityForResult(new Intent(this, TherapistTypeActivity.class), 0);
                return;
            case C0585R.id.btn_apo_type_apr:
                startActivityForResult(new Intent(this, AppointmentTypeActivity.class), 1);
                return;
            case C0585R.id.btn_next_apr:
                attemptLogin();
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0) {
            this.therapistType = data.getStringExtra("id");
            this.btnTherapistType.setText(data.getStringExtra(SdkConstants.DATA));
        } else if (requestCode == 1) {
            this.apoType = data.getStringExtra("id");
            this.btnApoType.setText(data.getStringExtra(SdkConstants.DATA));
        }
    }

    private void attemptLogin() {
        this.btnTherapistType.setError(null);
        this.etLicenseNo.setError(null);
        this.etRate.setError(null);
        this.etMobile.setError(null);
        String licenseNo = this.etLicenseNo.getText().toString().trim();
        String rate = this.etRate.getText().toString().trim();
        String mobile = this.etMobile.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (this.btnTherapistType.getText().toString().trim().equals("")) {
            this.btnTherapistType.setError(getString(C0585R.string.error_field_required));
            focusView = this.btnTherapistType;
            cancel = true;
        } else if (TextUtils.isEmpty(licenseNo)) {
            this.etLicenseNo.setError(getString(C0585R.string.error_field_required));
            focusView = this.etLicenseNo;
            cancel = true;
        } else if (TextUtils.isEmpty(rate)) {
            this.etRate.setError(getString(C0585R.string.error_field_required));
            focusView = this.etRate;
            cancel = true;
        } else if (TextUtils.isEmpty(mobile)) {
            this.etMobile.setError(getString(C0585R.string.error_field_required));
            focusView = this.etMobile;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        UserLoginTask((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), this.therapistType, licenseNo, rate, mobile, "", getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null));
    }

    private void showProgress(boolean show) {
        this.mProgressView.setVisibility(show ? 0 : 8);
    }

    private void UserLoginTask(String drId, String therapistType, String licenseNo, String rate, String mobile, String paymentType, String deviceId) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = drId;
        final String str2 = licenseNo;
        final String str3 = rate;
        final String str4 = mobile;
        final String str5 = therapistType;
        final String str6 = paymentType;
        final String str7 = deviceId;
        Request strReq = new StringRequest(1, EndPoints.ADD_DETAIL_PROVIDER, new C10751(), new C10762()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("licences_no", str2);
                params.put(SessionManager.KEY_RATES, str3);
                params.put("dr_mobile", str4);
                params.put(SessionManager.KEY_THERAPIST_TYPE, str5);
                params.put("payment_type", str6);
                params.put("apo_type", TherapistRegistration2Activity.this.apoType);
                params.put("device_id", str7);
                Log.e("TAG", params.toString());
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
