package com.talktoangel.gts.userauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    class C06531 implements OnClickListener {
        C06531() {
        }

        public void onClick(View view) {
            ResetPasswordActivity.this.checkFormData();
        }
    }

    class C10732 implements Listener<String> {
        C10732() {
        }

        public void onResponse(String response) {
            Log.e("Login Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONObject object = jObj.getJSONObject(SdkConstants.RESULT);
                    Toast.makeText(ResetPasswordActivity.this.getApplicationContext(), "logged in successfully", 1).show();
                    return;
                }
                Toast.makeText(ResetPasswordActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ResetPasswordActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10743 implements ErrorListener {
        C10743() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Login Error: ", "" + error.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_reset_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Button) findViewById(C0585R.id.btn_confirm_rp)).setOnClickListener(new C06531());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void checkFormData() {
    }

    private void UserLoginTask(String email, String password) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = email;
        final String str2 = password;
        Request strReq = new StringRequest(1, EndPoints.LOGIN, new C10732(), new C10743()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("device_type", "a");
                params.put("email", str);
                params.put(SdkConstants.PASSWORD, str2);
                params.put("device_id", "67");
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
