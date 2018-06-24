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
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpecialityListActivity extends AppCompatActivity {
    Adapter adapter;
    ArrayList<Speciality> list;
    ListView listView;
    ProgressBar progressBar;

    class Adapter extends ArrayAdapter {
        Context context;

        public Adapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.context = context;
        }

        public int getCount() {
            return SpecialityListActivity.this.list.size();
        }

        public Object getItem(int position) {
            return ((Speciality) SpecialityListActivity.this.list.get(position)).getName();
        }

        public long getItemId(int position) {
            return (long) position;
        }

        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(SpecialityListActivity.this).inflate(17367056, null);
            CheckedTextView tv = (CheckedTextView) v.findViewById(16908308);
            tv.setTextColor(SpecialityListActivity.this.getColor(C0585R.color.primary_text));
            tv.setText(((Speciality) SpecialityListActivity.this.list.get(position)).getName());
            return v;
        }
    }

    class C10511 implements Listener<String> {
        C10511() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    SpecialityListActivity.this.list.add(new Speciality(object1.getString("id_dr_speciality"), object1.getString("dr_speciality"), object1.getString("dr_speciality_is_deleted")));
                }
                SpecialityListActivity.this.progressBar.setVisibility(8);
                SpecialityListActivity.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                SpecialityListActivity.this.progressBar.setVisibility(8);
            }
        }
    }

    class C10522 implements ErrorListener {
        C10522() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            SpecialityListActivity.this.progressBar.setVisibility(8);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_speciality);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.listView = (ListView) findViewById(C0585R.id.listView);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_speciality);
        this.list = new ArrayList();
        this.adapter = new Adapter(getApplicationContext(), 17367056);
        this.listView.setAdapter(this.adapter);
        getSpecialities();
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
                Log.e("SpecialityActivity", i + ", ");
                Log.e("SpecialityActivity", s + ", ");
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

    private void getSpecialities() {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        Request strReq = new StringRequest(1, EndPoints.SPECIALITIES, new C10511(), new C10522());
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
