package com.talktoangel.gts.therapist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
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
import com.talktoangel.gts.adaptertherapist.PatientAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.PatientItem;
import com.talktoangel.gts.utils.DividerItemDecoration;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientListActivity extends AppCompatActivity {
    private PatientAdapter adapter;
    private ArrayList<PatientItem> list;
    private ProgressBar mProgressBar;
    SessionManager mSessionManager;

    class C10491 implements Listener<String> {
        C10491() {
        }

        public void onResponse(String response) {
            PatientListActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    PatientListActivity.this.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        PatientItem item = new PatientItem();
                        item.setId(object.getString(SessionManager.KEY_ID));
                        item.setName(object.getString("name"));
                        item.setEmail(object.getString("email"));
                        item.setDob(object.getString("dob"));
                        item.setGender(object.getString(SessionManager.KEY_GENDER));
                        item.setMarital_status(object.getString(SessionManager.KEY_MARITAL_STATUS));
                        item.setAddress(object.getString("city") + " " + object.getString("state"));
                        item.setMobile(object.getString("mobile"));
                        item.setImage(object.getString("pic"));
                        PatientListActivity.this.list.add(item);
                    }
                } else {
                    Toast.makeText(PatientListActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                }
                PatientListActivity.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                PatientListActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(PatientListActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
            }
        }
    }

    class C10502 implements ErrorListener {
        C10502() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", "" + error.getMessage());
            PatientListActivity.this.mProgressBar.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_patient_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_apl);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_apl);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        this.list = new ArrayList();
        this.adapter = new PatientAdapter(getApplicationContext(), this.list);
        recyclerView.setAdapter(this.adapter);
        this.mSessionManager = new SessionManager(getApplicationContext());
        getProvidersList((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void getProvidersList(String id) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = id;
        Request strReq = new StringRequest(1, EndPoints.PATIENT_LIST, new C10491(), new C10502()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
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
