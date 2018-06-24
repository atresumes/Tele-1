package com.talktoangel.gts.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.CalendarCustomView;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.AvailableTimes;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import de.hdodenhof.circleimageview.CircleImageView;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditAppointmentActivity extends AppCompatActivity implements OnClickListener {
    AllTimeSlotAdapter adapter;
    String apoID;
    String date;
    String image;
    ArrayList<AvailableTimes> list;
    private ProgressBar mProgressBar;
    SessionManager mSessionManager;
    String mobile;
    String name;
    String providerId;
    String slotId;
    private Spinner sp_app_type;
    String time;

    class C10082 implements Listener<String> {
        C10082() {
        }

        public void onResponse(String response) {
            EditAppointmentActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(EditAppointmentActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(SdkConstants.RESULT, 0);
                    EditAppointmentActivity.this.setResult(-1, returnIntent);
                    EditAppointmentActivity.this.finish();
                    return;
                }
                Toast.makeText(EditAppointmentActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                EditAppointmentActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(EditAppointmentActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10093 implements ErrorListener {
        C10093() {
        }

        public void onErrorResponse(VolleyError error) {
            EditAppointmentActivity.this.mProgressBar.setVisibility(8);
            error.printStackTrace();
        }
    }

    class C10105 implements Listener<String> {
        C10105() {
        }

        public void onResponse(String response) {
            EditAppointmentActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    EditAppointmentActivity.this.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        AvailableTimes availableTimes = new AvailableTimes();
                        availableTimes.setSlotId(object.getString("slot_id"));
                        availableTimes.setTimeId(object.getString("timingId"));
                        availableTimes.setTime(object.getString("slot"));
                        availableTimes.setStatus(object.getString("status"));
                        EditAppointmentActivity.this.list.add(availableTimes);
                    }
                    EditAppointmentActivity.this.adapter.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(EditAppointmentActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                EditAppointmentActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            }
        }
    }

    class C10116 implements ErrorListener {
        C10116() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            EditAppointmentActivity.this.mProgressBar.setVisibility(8);
        }
    }

    class AllTimeSlotAdapter extends Adapter<ViewHolder> {
        Context context;
        int index;
        ArrayList<AvailableTimes> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            TextView btnTime;

            ViewHolder(View itemView) {
                super(itemView);
                this.btnTime = (TextView) itemView.findViewById(C0585R.id.txtTime);
            }
        }

        AllTimeSlotAdapter(Context context, ArrayList<AvailableTimes> imageList) {
            this.context = context;
            this.list = imageList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_time_slot, parent, false));
        }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.btnTime.setText(((AvailableTimes) this.list.get(position)).getTime());
            if (((AvailableTimes) this.list.get(position)).getStatus().equalsIgnoreCase("booked")) {
                holder.btnTime.setEnabled(false);
                holder.btnTime.setTextColor(EditAppointmentActivity.this.getResources().getColor(C0585R.color.colorPrimary));
            } else {
                holder.btnTime.setEnabled(true);
                holder.btnTime.setTextColor(EditAppointmentActivity.this.getResources().getColor(C0585R.color.primary_text));
            }
            holder.btnTime.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AllTimeSlotAdapter.this.index = holder.getAdapterPosition();
                    AllTimeSlotAdapter.this.notifyDataSetChanged();
                }
            });
            if (this.index == position) {
                holder.btnTime.setBackgroundColor(EditAppointmentActivity.this.getResources().getColor(C0585R.color.colorPrimary));
                holder.btnTime.setTextColor(EditAppointmentActivity.this.getResources().getColor(C0585R.color.white));
                EditAppointmentActivity.this.time = ((AvailableTimes) this.list.get(position)).getTime();
                EditAppointmentActivity.this.slotId = ((AvailableTimes) this.list.get(holder.getAdapterPosition())).getSlotId();
                return;
            }
            holder.btnTime.setBackgroundColor(EditAppointmentActivity.this.getResources().getColor(C0585R.color.transparent));
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getItemCount() {
            return this.list.size();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_edit_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        this.apoID = bundle.getString("apo_id");
        this.providerId = bundle.getString(SessionManager.KEY_ID);
        this.name = bundle.getString("name");
        this.image = bundle.getString("time");
        this.date = bundle.getString("date");
        this.time = bundle.getString("time");
        this.mobile = bundle.getString("mobile");
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_eap);
        CircleImageView view = (CircleImageView) findViewById(C0585R.id.img_schedule);
        TextView txtName = (TextView) findViewById(C0585R.id.txtName_s);
        final CalendarCustomView mView = (CalendarCustomView) findViewById(C0585R.id.custom_calendar);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_schedule);
        Button btnSend = (Button) findViewById(C0585R.id.btnUpdate);
        this.sp_app_type = (Spinner) findViewById(C0585R.id.sp_app_type);
        txtName.setText(this.name);
        Glide.with(getApplicationContext()).load(this.image).error((int) C0585R.drawable.ic_user_black_24dp).into(view);
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        btnSend.setOnClickListener(this);
        mView.setOnClickListener(new com.talktoangel.gts.listener.OnClickListener() {
            public void onClick() {
                Log.e("mView Pressed", mView.date.toString());
                EditAppointmentActivity.this.date = format.format(mView.date);
                EditAppointmentActivity.this.list.clear();
                EditAppointmentActivity.this.adapter.notifyDataSetChanged();
                EditAppointmentActivity.this.getProviderAvailability(EditAppointmentActivity.this.providerId, EditAppointmentActivity.this.date);
            }
        });
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.list = new ArrayList();
        this.adapter = new AllTimeSlotAdapter(getApplicationContext(), this.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setAdapter(this.adapter);
        getProviderAvailability(this.providerId, this.date);
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
            case C0585R.id.btnUpdate:
                checkFormData(view);
                return;
            default:
                return;
        }
    }

    void checkFormData(View view) {
        if (this.time.equalsIgnoreCase("")) {
            Snackbar.make(view, (CharSequence) "Select your preferred time", -1).show();
        } else if (this.sp_app_type.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select Appointment Type", -1).show();
        } else {
            updateAppointment(this.apoID, this.date, this.time, this.sp_app_type.getSelectedItem().toString(), (String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), (String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
        }
    }

    private void updateAppointment(String apoID, String date, String time, String type, String userId, String userType) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = apoID;
        final String str2 = type;
        final String str3 = date;
        final String str4 = time;
        final String str5 = userId;
        final String str6 = userType;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_APPOINTMENT, new C10082(), new C10093()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("apo_id", str);
                params.put("apo_type", str2);
                params.put("apo_date", str3);
                params.put("apo_time", str4);
                params.put(SessionManager.KEY_ID, str5);
                params.put(SessionManager.KEY_TYPE, str6);
                params.put("slot_id", EditAppointmentActivity.this.slotId);
                Log.e("Error", "" + params.toString());
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

    void getProviderAvailability(String dr_id, String date) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = dr_id;
        final String str2 = date;
        Request strReq = new StringRequest(1, EndPoints.FETCH_AVAILABILITY, new C10105(), new C10116()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("date", str2);
                Log.e(SdkConstants.PARAMS, params.toString());
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
