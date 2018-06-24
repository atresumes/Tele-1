package com.talktoangel.gts.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestDetailActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tv_amount;
    private TextView tv_date;
    private TextView tv_name;
    private TextView tv_transaction_id;
    private TextView tv_type;

    class C10281 implements Listener<String> {
        C10281() {
        }

        public void onResponse(String response) {
            TestDetailActivity.this.progressBar.setVisibility(4);
            Log.e("Response", response);
            try {
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    String name = object1.getString("name");
                    String test_type = object1.getString("test_type");
                    String transaction_id = object1.getString("transaction_id");
                    String amount = object1.getString(SdkConstants.AMOUNT);
                    String date = object1.getString("date");
                    TestDetailActivity.this.tv_name.setText(name);
                    TestDetailActivity.this.tv_type.setText(test_type);
                    TestDetailActivity.this.tv_transaction_id.setText(transaction_id);
                    TestDetailActivity.this.tv_amount.setText(amount);
                    TestDetailActivity.this.tv_date.setText(date);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                TestDetailActivity.this.progressBar.setVisibility(4);
            }
        }
    }

    class C10292 implements ErrorListener {
        C10292() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            TestDetailActivity.this.progressBar.setVisibility(4);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_test_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_td);
        this.tv_name = (TextView) findViewById(C0585R.id.tv_name);
        this.tv_type = (TextView) findViewById(C0585R.id.tv_type);
        this.tv_transaction_id = (TextView) findViewById(C0585R.id.tv_transaction_id);
        this.tv_amount = (TextView) findViewById(C0585R.id.tv_amount);
        this.tv_date = (TextView) findViewById(C0585R.id.tv_date);
        getTestUserData(new SessionManager(this).getBuddyId());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    void getTestUserData(String id) {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        final String str = id;
        Request strReq = new StringRequest(1, EndPoints.TEST_USER_DETAIL, new C10281(), new C10292()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.BUDDY_ID, str);
                Log.e("Login params", params.toString());
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
