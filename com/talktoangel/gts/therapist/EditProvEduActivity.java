package com.talktoangel.gts.therapist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.DegreeCollege;
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

public class EditProvEduActivity extends AppCompatActivity implements OnClickListener {
    SpinnerAdapter adapter1;
    SpinnerAdapter adapter2;
    String clg = "";
    ArrayList<DegreeCollege> collegeList;
    String degree = "";
    ArrayList<DegreeCollege> degreeList;
    String drId = "";
    String eduId = "";
    EditText etYear;
    Spinner spCollege;
    Spinner spDegree;

    private class SpinnerAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<DegreeCollege> list;

        SpinnerAdapter(Context context, ArrayList<DegreeCollege> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return ((DegreeCollege) this.list.get(position)).getId();
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DegreeCollege degreeCollege = (DegreeCollege) this.list.get(position);
            TextView name = new TextView(this.context);
            name.setText(degreeCollege.getName());
            return name;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            DegreeCollege degreeCollege = (DegreeCollege) this.list.get(position);
            View row = EditProvEduActivity.this.getLayoutInflater().inflate(C0585R.layout.spinner_item, parent, false);
            ((TextView) row.findViewById(C0585R.id.txt_name)).setText(degreeCollege.getName());
            return row;
        }
    }

    class C10431 implements Listener<String> {
        C10431() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("true")) {
                    Toast.makeText(EditProvEduActivity.this, object.getString("message"), 0).show();
                    EditProvEduActivity.this.onBackPressed();
                    return;
                }
                Toast.makeText(EditProvEduActivity.this, object.getString("message"), 0).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class C10442 implements ErrorListener {
        C10442() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }

    class C10454 implements Listener<String> {
        C10454() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                Toast.makeText(EditProvEduActivity.this, new JSONObject(response).getString("message"), 0).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class C10465 implements ErrorListener {
        C10465() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
        }
    }

    class C10477 implements Listener<String> {
        C10477() {
        }

        public void onResponse(String response) {
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONObject object;
                    JSONArray degree_list = jObj.getJSONArray("degree_list");
                    JSONArray college_list = jObj.getJSONArray("college_list");
                    for (int i = 0; i < degree_list.length(); i++) {
                        object = degree_list.getJSONObject(i);
                        EditProvEduActivity.this.degreeList.add(new DegreeCollege(object.getString("degree_id"), object.getString("degree_name")));
                    }
                    for (int j = 0; j < college_list.length(); j++) {
                        object = college_list.getJSONObject(j);
                        EditProvEduActivity.this.collegeList.add(new DegreeCollege(object.getString("clg_id"), object.getString("clg_name")));
                    }
                    EditProvEduActivity.this.adapter1.notifyDataSetChanged();
                    EditProvEduActivity.this.adapter2.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(EditProvEduActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class C10488 implements ErrorListener {
        C10488() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_edit_prov_edu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.spDegree = (Spinner) findViewById(C0585R.id.sp_degree);
        this.spCollege = (Spinner) findViewById(C0585R.id.sp_college);
        this.etYear = (EditText) findViewById(C0585R.id.et_year);
        Button btnSave = (Button) findViewById(C0585R.id.btn_save_epa);
        Button btnDelete = (Button) findViewById(C0585R.id.btn_remove_epa);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.eduId = bundle.getString("eduId");
            this.degree = bundle.getString("degree");
            this.clg = bundle.getString("clg");
            this.etYear.setText(bundle.getString("year"));
        } else {
            btnDelete.setVisibility(8);
        }
        this.drId = (String) new SessionManager(getApplicationContext()).getUser().get(SessionManager.KEY_ID);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        this.degreeList = new ArrayList();
        this.collegeList = new ArrayList();
        this.adapter1 = new SpinnerAdapter(getApplicationContext(), this.degreeList);
        this.adapter2 = new SpinnerAdapter(getApplicationContext(), this.collegeList);
        this.spDegree.setAdapter(this.adapter1);
        this.spCollege.setAdapter(this.adapter2);
        getAllDegreeColleges();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_save_epa:
                checkFormData();
                return;
            case C0585R.id.btn_remove_epa:
                deleteProviderEducation(this.eduId);
                return;
            default:
                return;
        }
    }

    private void checkFormData() {
        this.etYear.setError(null);
        boolean cancel = false;
        View focusView = null;
        String year = this.etYear.getText().toString();
        if (TextUtils.isEmpty(year)) {
            this.etYear.setError(getString(C0585R.string.error_field_required));
            focusView = this.etYear;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        editProviderEducation(this.drId, this.eduId, this.spDegree.getSelectedItem().toString(), this.spCollege.getSelectedItem().toString(), year);
    }

    private void editProviderEducation(String drId, String eduId, String degreeId, String clgId, String year) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = eduId;
        final String str2 = drId;
        final String str3 = degreeId;
        final String str4 = clgId;
        final String str5 = year;
        Request strReq = new StringRequest(1, EndPoints.EDIT_EDUCATION_DR, new C10431(), new C10442()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("education_id", str);
                params.put("dr_id", str2);
                params.put("degree_id", str3);
                params.put("college_id", str4);
                params.put("pass_year", str5);
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

    private void deleteProviderEducation(String eduId) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = eduId;
        Request strReq = new StringRequest(1, EndPoints.DELETE_EDUCATION_DR, new C10454(), new C10465()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("education_id", str);
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

    private void getAllDegreeColleges() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(1, EndPoints.DEGREE_COLLEGE, new C10477(), new C10488());
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
