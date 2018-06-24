package com.talktoangel.gts.therapist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
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
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.Speciality;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TherapistTypeActivity extends AppCompatActivity {
    private Adapter adapter;
    private ArrayList<Speciality> list;
    private ListView listView;
    private ProgressBar progressBar;

    class Adapter extends ArrayAdapter {
        Context context;

        public Adapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.context = context;
        }

        public int getCount() {
            return TherapistTypeActivity.this.list.size();
        }

        public Object getItem(int position) {
            return TherapistTypeActivity.this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(TherapistTypeActivity.this).inflate(17367056, null);
            CheckedTextView tv = (CheckedTextView) v.findViewById(16908308);
            tv.setTextColor(TherapistTypeActivity.this.getColor(C0585R.color.primary_text));
            tv.setText(((Speciality) TherapistTypeActivity.this.list.get(position)).getName());
            return v;
        }
    }

    class C10571 implements Listener<String> {
        C10571() {
        }

        public void onResponse(String response) {
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONArray therapist = jObj.getJSONArray(SessionManager.KEY_THERAPIST_TYPE);
                    for (int i = 0; i < therapist.length(); i++) {
                        JSONObject object = therapist.getJSONObject(i);
                        TherapistTypeActivity.this.list.add(new Speciality(object.getString("therapist_type_id"), object.getString(SessionManager.KEY_THERAPIST_TYPE), ""));
                    }
                    TherapistTypeActivity.this.adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TherapistTypeActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                }
                TherapistTypeActivity.this.progressBar.setVisibility(8);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class C10582 implements ErrorListener {
        C10582() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            TherapistTypeActivity.this.progressBar.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_therapist_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.listView = (ListView) findViewById(C0585R.id.list_therapist);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_therapist);
        this.list = new ArrayList();
        this.adapter = new Adapter(getApplicationContext(), 17367056);
        this.listView.setAdapter(this.adapter);
        getTherapistTypeList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0585R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != C0585R.id.action_done) {
            return super.onOptionsItemSelected(item);
        } else {
            String i = "";
            String s = "";
            SparseBooleanArray clickedItemPositions = this.listView.getCheckedItemPositions();
            for (int index = 0; index < clickedItemPositions.size(); index++) {
                if (clickedItemPositions.valueAt(index)) {
                    int key = clickedItemPositions.keyAt(index);
                    if (index == 0) {
                        i = ((Speciality) this.list.get(key)).getId();
                        s = ((Speciality) this.list.get(key)).getName();
                    } else {
                        i = i + "," + ((Speciality) this.list.get(key)).getId();
                        s = s + ", " + ((Speciality) this.list.get(key)).getName();
                    }
                }
                Log.e("TherapistTypeActivity", i + ", ");
                Log.e("TherapistTypeActivity", s + ", ");
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SdkConstants.RESULT, 0);
            returnIntent.putExtra("id", i);
            returnIntent.putExtra(SdkConstants.DATA, s);
            setResult(-1, returnIntent);
            onBackPressed();
            return true;
        }
    }

    private void getTherapistTypeList() {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        Request strReq = new StringRequest(1, EndPoints.FILTER_DATA, new C10571(), new C10582());
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
