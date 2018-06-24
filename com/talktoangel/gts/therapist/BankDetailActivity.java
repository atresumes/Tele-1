package com.talktoangel.gts.therapist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.talktoangel.gts.C0585R;
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

public class BankDetailActivity extends AppCompatActivity {
    EditText etAadharNo;
    EditText etAccHolderName;
    EditText etAccNo;
    EditText etBranch;
    EditText etIfscCode;
    EditText etPanNo;
    ProgressBar mProgressView;
    SessionManager manager;
    Spinner spAccType;
    Spinner spBankName;

    class C06351 implements OnClickListener {
        C06351() {
        }

        public void onClick(View v) {
            BankDetailActivity.this.verifyFormData();
        }
    }

    class C10382 implements Listener<String> {
        C10382() {
        }

        public void onResponse(String response) {
            BankDetailActivity.this.showProgress(false);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    Toast.makeText(BankDetailActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    BankDetailActivity.this.finish();
                    return;
                }
                BankDetailActivity.this.showProgress(false);
                Toast.makeText(BankDetailActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                BankDetailActivity.this.showProgress(false);
            }
        }
    }

    class C10393 implements ErrorListener {
        C10393() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            BankDetailActivity.this.showProgress(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_bank_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mProgressView = (ProgressBar) findViewById(C0585R.id.progress_abd);
        this.etAccHolderName = (EditText) findViewById(C0585R.id.etAccHolderName);
        this.etAccNo = (EditText) findViewById(C0585R.id.et_account_no);
        this.etIfscCode = (EditText) findViewById(C0585R.id.et_ifsc_code);
        this.etBranch = (EditText) findViewById(C0585R.id.etBranch);
        this.etPanNo = (EditText) findViewById(C0585R.id.etPanNo);
        this.etAadharNo = (EditText) findViewById(C0585R.id.etAadharNo);
        this.spAccType = (Spinner) findViewById(C0585R.id.sp_acc_type);
        this.spBankName = (Spinner) findViewById(C0585R.id.spBankName);
        Button saveButton = (Button) findViewById(C0585R.id.btn_save_abd);
        this.manager = new SessionManager(getApplicationContext());
        this.etAccHolderName.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.ACC_HOLD_NM));
        this.etAccNo.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.ACC_NO));
        this.etIfscCode.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.IFSC_CODE));
        this.etBranch.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.BRANCH_CITY));
        this.etPanNo.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.PAN_NO));
        this.etAadharNo.setText((CharSequence) this.manager.getBankDetails().get(SessionManager.AADHAR_NO));
        saveButton.setOnClickListener(new C06351());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    void verifyFormData() {
        this.etAccHolderName.setError(null);
        this.etAccNo.setError(null);
        this.etIfscCode.setError(null);
        this.etBranch.setError(null);
        this.etPanNo.setError(null);
        String name = this.etAccHolderName.getText().toString().trim();
        String accNo = this.etAccNo.getText().toString().trim();
        String ifscCode = this.etIfscCode.getText().toString().trim();
        String branch = this.etBranch.getText().toString().trim();
        String panNo = this.etPanNo.getText().toString().trim();
        String aadharNo = this.etAadharNo.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            this.etAccHolderName.setError(getString(C0585R.string.error_field_required));
        } else if (TextUtils.isEmpty(accNo)) {
            this.etAccNo.setError(getString(C0585R.string.error_field_required));
        } else if (accNo.length() != 16) {
            this.etAccNo.setError(getString(C0585R.string.error_field_required));
        } else if (TextUtils.isEmpty(ifscCode)) {
            this.etIfscCode.setError(getString(C0585R.string.error_field_required));
        } else if (TextUtils.isEmpty(branch)) {
            this.etBranch.setError(getString(C0585R.string.error_field_required));
        } else if (TextUtils.isEmpty(panNo)) {
            this.etPanNo.setError(getString(C0585R.string.error_field_required));
        } else if (TextUtils.isEmpty(aadharNo)) {
            this.etAadharNo.setError(getString(C0585R.string.error_field_required));
        } else {
            showProgress(true);
            addTherapistBankDetails((String) this.manager.getUser().get(SessionManager.KEY_ID), name, accNo, this.spAccType.getSelectedItem().toString(), this.spBankName.getSelectedItem().toString(), ifscCode, branch, panNo, aadharNo);
        }
    }

    void addTherapistBankDetails(String userId, String userName, String accountNo, String accountType, String bankName, String ifscCode, String branchName, String panNo, String aadharNo) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = userId;
        final String str2 = userName;
        final String str3 = accountNo;
        final String str4 = accountType;
        final String str5 = bankName;
        final String str6 = ifscCode;
        final String str7 = branchName;
        final String str8 = panNo;
        final String str9 = aadharNo;
        Request strReq = new StringRequest(1, EndPoints.ADD_BANK_DETAIL, new C10382(), new C10393()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("acc_holder_nm", str2);
                params.put(SessionManager.ACC_NO, str3);
                params.put("acc_type", str4);
                params.put(SessionManager.BANK_NAME, str5);
                params.put(SessionManager.IFSC_CODE, str6);
                params.put(SessionManager.BRANCH_CITY, str7);
                params.put(SessionManager.PAN_NO, str8);
                params.put(SessionManager.AADHAR_NO, str9);
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

    private void showProgress(boolean show) {
        this.mProgressView.setVisibility(show ? 0 : 8);
    }
}
