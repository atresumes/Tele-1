package com.talktoangel.gts.settings;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountDetailsActivity extends AppCompatActivity {
    private EditText etAnswer;
    private EditText etConfirmPass;
    private EditText etEmail;
    private EditText etNewPassword;
    private EditText etOldPassword;
    private SessionManager mSessionManager;
    ProgressBar progressBar;
    Spinner spinner;

    class C06061 implements OnClickListener {
        C06061() {
        }

        public void onClick(View view) {
            AccountDetailsActivity.this.checkFieldData(view);
        }
    }

    class C10152 implements Listener<String> {
        C10152() {
        }

        public void onResponse(String response) {
            AccountDetailsActivity.this.progressBar.setVisibility(8);
            Log.e("Response", response);
            try {
                JSONObject jSONObject = new JSONObject(response);
                if (jSONObject.getString("status").equals("true")) {
                    Toast.makeText(AccountDetailsActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
                    JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                    String freeSession = (String) AccountDetailsActivity.this.mSessionManager.getUser().get(SessionManager.FREE_SESSION);
                    String id = object.getString("id");
                    String pic = object.getString("pic");
                    AccountDetailsActivity.this.mSessionManager.setUser(freeSession, id, object.getString(SessionManager.KEY_TYPE), object.getString(SessionManager.KEY_FIRST_NAME), object.getString(SessionManager.KEY_LAST_NAME), object.getString("email"), object.getString("security_ques"), object.getString("answer"), object.getString(SessionManager.KEY_GENDER), object.getString("add_line"), object.getString("apt_bldg"), object.getString("city"), object.getString("state"), object.getString(SdkConstants.ZIPCODE), object.getString(SessionManager.COUNTRY_CODE), object.getString(SessionManager.KEY_CURRENCY_PREF), object.getString("mobile"), pic);
                    AccountDetailsActivity.this.finish();
                    return;
                }
                Toast.makeText(AccountDetailsActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
            } catch (JSONException e) {
                AccountDetailsActivity.this.progressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(AccountDetailsActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10163 implements ErrorListener {
        C10163() {
        }

        public void onErrorResponse(VolleyError error) {
            AccountDetailsActivity.this.progressBar.setVisibility(8);
            Log.e("Error", "" + error.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_account_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.etEmail = (EditText) findViewById(C0585R.id.email_ac);
        this.etOldPassword = (EditText) findViewById(C0585R.id.old_password_ac);
        this.etNewPassword = (EditText) findViewById(C0585R.id.new_password_ac);
        this.etConfirmPass = (EditText) findViewById(C0585R.id.confirm_pass_ac);
        this.etAnswer = (EditText) findViewById(C0585R.id.answer_ac);
        this.spinner = (Spinner) findViewById(C0585R.id.sp_sec_ques_ac);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_ac);
        Button btnSave = (Button) findViewById(C0585R.id.btn_save);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.etEmail.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_EMAIL));
        this.etAnswer.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ANSWER));
        List<String> list = Arrays.asList(getResources().getStringArray(C0585R.array.questions_array));
        for (int i = 0; i < list.size(); i++) {
            if (((String) list.get(i)).equalsIgnoreCase((String) this.mSessionManager.getUser().get(SessionManager.KEY_SEC_QUES))) {
                this.spinner.setSelection(i);
            }
        }
        btnSave.setOnClickListener(new C06061());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void checkFieldData(View view) {
        this.etEmail.setError(null);
        this.etOldPassword.setError(null);
        this.etNewPassword.setError(null);
        this.etConfirmPass.setError(null);
        this.etAnswer.setError(null);
        boolean cancel = false;
        View focusView = null;
        String email = this.etEmail.getText().toString().trim();
        String oldPassword = this.etOldPassword.getText().toString().trim();
        String newPassword = this.etNewPassword.getText().toString().trim();
        String confirmPass = this.etConfirmPass.getText().toString().trim();
        String answer = this.etAnswer.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            this.etEmail.setError(getString(C0585R.string.error_field_required));
            focusView = this.etEmail;
            cancel = true;
        } else if (!email.matches(Constant.EMAIL_PATTERN)) {
            this.etEmail.setError(getString(C0585R.string.error_invalid_email));
            focusView = this.etEmail;
            cancel = true;
        } else if (this.spinner.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select a Security Question", -1).show();
            focusView = this.spinner;
            cancel = true;
        } else if (TextUtils.isEmpty(answer)) {
            this.etAnswer.setError(getString(C0585R.string.error_field_required));
            focusView = this.etAnswer;
            cancel = true;
        }
        if (this.etOldPassword.getText().length() > 0 || this.etNewPassword.getText().length() > 0) {
            if (TextUtils.isEmpty(oldPassword)) {
                this.etOldPassword.setError(getString(C0585R.string.error_field_required));
                focusView = this.etOldPassword;
                cancel = true;
            } else if (!isPasswordValid(oldPassword)) {
                this.etOldPassword.setError(getString(C0585R.string.error_invalid_password));
                focusView = this.etOldPassword;
                cancel = true;
            } else if (TextUtils.isEmpty(newPassword)) {
                this.etNewPassword.setError(getString(C0585R.string.error_field_required));
                focusView = this.etNewPassword;
                cancel = true;
            } else if (!isPasswordValid(newPassword)) {
                this.etNewPassword.setError(getString(C0585R.string.error_invalid_password));
                focusView = this.etNewPassword;
                cancel = true;
            } else if (TextUtils.isEmpty(confirmPass)) {
                this.etConfirmPass.setError(getString(C0585R.string.error_field_required));
                focusView = this.etConfirmPass;
                cancel = true;
            } else if (!isPasswordValid(confirmPass)) {
                this.etConfirmPass.setError(getString(C0585R.string.error_invalid_password));
                focusView = this.etConfirmPass;
                cancel = true;
            } else if (!newPassword.equals(confirmPass)) {
                this.etConfirmPass.setError(getString(C0585R.string.error_same_password));
                focusView = this.etConfirmPass;
                cancel = true;
            }
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        updateUserDetails((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), email, oldPassword, newPassword, this.spinner.getSelectedItem().toString(), answer);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void updateUserDetails(String userID, String email, String oldPassword, String newPassword, String secQues, String answer) {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        final String str = userID;
        final String str2 = email;
        final String str3 = oldPassword;
        final String str4 = newPassword;
        final String str5 = secQues;
        final String str6 = answer;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_PROFILE, new C10152(), new C10163()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", str);
                params.put("email", str2);
                params.put("old_password", str3);
                params.put("new_password", str4);
                params.put("security_ques", str5);
                params.put("answer", str6);
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
