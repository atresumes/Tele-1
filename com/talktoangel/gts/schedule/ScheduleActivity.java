package com.talktoangel.gts.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
import com.talktoangel.gts.model.Item;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import de.hdodenhof.circleimageview.CircleImageView;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleActivity extends AppCompatActivity implements OnClickListener {
    private AllTimeSlotAdapter adapter;
    private String app_type;
    private ArrayList<Item> appointmentList;
    private ArrayAdapter<Item> arrayAdapter;
    private String charge;
    private String date = "";
    private String date1;
    private String image;
    private int index;
    private boolean isBetweenTime;
    private ArrayList<AvailableTimes> list;
    private ProgressBar mProgressBar;
    private String name;
    private String providerId;
    private String slotId;
    private Spinner sp_app_type;
    private String time = "";
    private String timingId;

    class C06032 implements OnItemSelectedListener {
        C06032() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ScheduleActivity.this.app_type = ((Item) ScheduleActivity.this.appointmentList.get(position)).getId();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C10134 implements Listener<String> {
        C10134() {
        }

        public void onResponse(String response) {
            ScheduleActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    int i;
                    JSONObject object;
                    ScheduleActivity.this.list.clear();
                    ScheduleActivity.this.appointmentList.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        AvailableTimes availableTimes = new AvailableTimes();
                        availableTimes.setSlotId(object.getString("slot_id"));
                        availableTimes.setTimeId(object.getString("timing_id"));
                        availableTimes.setTime(object.getString("slot"));
                        availableTimes.setStatus(object.getString("status"));
                        ScheduleActivity.this.list.add(availableTimes);
                    }
                    JSONArray appointment = jObj.getJSONArray("appointment_type");
                    ScheduleActivity.this.appointmentList.add(new Item("id", "Select Appointment Type"));
                    for (i = 0; i < appointment.length(); i++) {
                        object = appointment.getJSONObject(i);
                        ScheduleActivity.this.appointmentList.add(new Item(object.getString("apo_id"), object.getString("appointment_type")));
                    }
                    ScheduleActivity.this.arrayAdapter.notifyDataSetChanged();
                    ScheduleActivity.this.adapter.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(ScheduleActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                ScheduleActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            }
        }
    }

    class C10145 implements ErrorListener {
        C10145() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            ScheduleActivity.this.mProgressBar.setVisibility(8);
        }
    }

    class AllTimeSlotAdapter extends Adapter<ViewHolder> {
        Context context;
        ArrayList<AvailableTimes> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            Button btnTime;

            ViewHolder(View itemView) {
                super(itemView);
                this.btnTime = (Button) itemView.findViewById(C0585R.id.txtTime);
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
            if (((AvailableTimes) this.list.get(position)).getStatus().equalsIgnoreCase("0")) {
                holder.btnTime.setEnabled(false);
                holder.btnTime.setTextColor(ScheduleActivity.this.getResources().getColor(C0585R.color.colorPrimary));
            } else {
                holder.btnTime.setEnabled(true);
                holder.btnTime.setTextColor(ScheduleActivity.this.getResources().getColor(C0585R.color.primary_text));
            }
            holder.btnTime.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ScheduleActivity.this.index = holder.getAdapterPosition();
                    AllTimeSlotAdapter.this.notifyDataSetChanged();
                }
            });
            if (ScheduleActivity.this.index == position) {
                holder.btnTime.setBackgroundColor(ScheduleActivity.this.getResources().getColor(C0585R.color.colorPrimary));
                holder.btnTime.setTextColor(ScheduleActivity.this.getResources().getColor(C0585R.color.white));
                ScheduleActivity.this.time = ((AvailableTimes) this.list.get(position)).getTime();
                ScheduleActivity.this.slotId = ((AvailableTimes) this.list.get(holder.getAdapterPosition())).getSlotId();
                ScheduleActivity.this.timingId = ((AvailableTimes) this.list.get(holder.getAdapterPosition())).getTimeId();
                return;
            }
            holder.btnTime.setBackgroundColor(ScheduleActivity.this.getResources().getColor(C0585R.color.transparent));
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
        setContentView((int) C0585R.layout.activity_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.providerId = getIntent().getStringExtra("id");
        this.name = getIntent().getStringExtra("name");
        this.image = getIntent().getStringExtra("image");
        this.charge = getIntent().getStringExtra(SessionManager.KEY_LICENSE_NO);
        CircleImageView view = (CircleImageView) findViewById(C0585R.id.img_schedule);
        TextView txtName = (TextView) findViewById(C0585R.id.txtName_s);
        final TextView tvAvailability = (TextView) findViewById(C0585R.id.txtAvailability);
        TextView txtNotes = (TextView) findViewById(C0585R.id.txt_notes);
        final CalendarCustomView mView = (CalendarCustomView) findViewById(C0585R.id.custom_calendar);
        this.sp_app_type = (Spinner) findViewById(C0585R.id.sp_app_type);
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_as);
        Button btnSend = (Button) findViewById(C0585R.id.btnSend);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_aschedule);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        if (new SessionManager(this).getAvailability().equalsIgnoreCase("yes")) {
            tvAvailability.setText("Available");
        } else {
            tvAvailability.setText("Not Available");
        }
        txtName.setText(this.name);
        txtNotes.setText("By requesting this appointment i agree to grant " + this.name + " access to my health record on Talk to Angel as described in the Talk to Angel Policy.");
        Glide.with(getApplicationContext()).load(this.image).error((int) C0585R.drawable.ic_user_black_24dp).into(view);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Log.e("date", "" + format.format(calendar.getTime()));
        this.date = format.format(calendar.getTime());
        this.date1 = format.format(calendar.getTime());
        btnSend.setOnClickListener(this);
        mView.setOnClickListener(new com.talktoangel.gts.listener.OnClickListener() {
            public void onClick() {
                Log.e("TAG", "" + mView.date.toString().substring(0, 3));
                if (tvAvailability.getText().equals("Available")) {
                    ScheduleActivity.this.date = format.format(mView.date);
                    ScheduleActivity.this.list.clear();
                    ScheduleActivity.this.index = 0;
                    ScheduleActivity.this.adapter.notifyDataSetChanged();
                    ScheduleActivity.this.getProviderAvailability(ScheduleActivity.this.providerId, ScheduleActivity.this.date);
                    return;
                }
                ScheduleActivity.this.list.clear();
                ScheduleActivity.this.index = 0;
                ScheduleActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.sp_app_type.setOnItemSelectedListener(new C06032());
        setAppointmentTypeAdapter();
        this.list = new ArrayList();
        this.adapter = new AllTimeSlotAdapter(getApplicationContext(), this.list);
        recyclerView.setAdapter(this.adapter);
        if (tvAvailability.getText().equals("Available")) {
            getProviderAvailability(this.providerId, this.date);
        }
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
            case C0585R.id.btnSend:
                checkFormData(view);
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == -1) {
            finish();
        }
    }

    void checkFormData(View view) {
        checkTime(this.date, ((AvailableTimes) this.list.get(this.index)).getTime());
        if (this.sp_app_type.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select Appointment Type", -1).show();
        } else if (this.time.equalsIgnoreCase("")) {
            Snackbar.make(view, (CharSequence) "Please select time", -1).show();
        } else if (((AvailableTimes) this.list.get(this.index)).getStatus().equals("0")) {
            Snackbar.make(view, (CharSequence) "Slot you have selected is already Booked, Please select Another Time.", -1).show();
        } else if (!this.date.contentEquals(this.date1)) {
            bundle = new Bundle();
            bundle.putString("id", this.providerId);
            bundle.putString("name", this.name);
            bundle.putString("image", this.image);
            bundle.putString("type", this.app_type);
            bundle.putString("date", this.date);
            bundle.putString("time", this.time);
            bundle.putString("slot_id", this.slotId);
            bundle.putString("timingId", this.timingId);
            bundle.putString(SessionManager.KEY_LICENSE_NO, this.charge);
            startActivityForResult(new Intent(this, ConfirmScheduleActivity.class).putExtras(bundle), 0);
        } else if (this.isBetweenTime) {
            bundle = new Bundle();
            bundle.putString("id", this.providerId);
            bundle.putString("name", this.name);
            bundle.putString("image", this.image);
            bundle.putString("type", this.app_type);
            bundle.putString("date", this.date);
            bundle.putString("time", this.time);
            bundle.putString("slot_id", this.slotId);
            bundle.putString("timingId", this.timingId);
            bundle.putString(SessionManager.KEY_LICENSE_NO, this.charge);
            startActivityForResult(new Intent(this, ConfirmScheduleActivity.class).putExtras(bundle), 0);
        } else {
            Snackbar.make(view, (CharSequence) "Please select Correct Time!", -1).show();
        }
    }

    private void checkTime(String da, String string) {
        try {
            System.out.println(da + " " + string);
            Date time1 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(da + " " + string);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            if (string.contains("AM")) {
                calendar1.set(9, 0);
            } else {
                calendar1.set(9, 1);
            }
            Log.e("calender1", calendar1.getTime() + "");
            long yourmilliseconds = System.currentTimeMillis();
            Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date(yourmilliseconds)));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            Log.e("calender3", calendar3.getTime() + "");
            if (calendar3.getTime().before(calendar1.getTime())) {
                this.isBetweenTime = true;
                System.out.println(true);
                return;
            }
            this.isBetweenTime = false;
            System.out.println(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void setAppointmentTypeAdapter() {
        this.appointmentList = new ArrayList();
        this.arrayAdapter = new ArrayAdapter<Item>(this, 17367049, this.appointmentList) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(((Item) ScheduleActivity.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setGravity(17);
                ((TextView) v).setTextColor(ScheduleActivity.this.getResources().getColor(C0585R.color.secondary_text));
                v.setPadding(15, 0, 15, 0);
                ((TextView) v).setTextSize(16.0f);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(GravityCompat.START);
                ((TextView) v).setText(((Item) ScheduleActivity.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setTextSize(16.0f);
                v.setPadding(15, 15, 15, 15);
                ((TextView) v).setTextColor(ScheduleActivity.this.getResources().getColor(C0585R.color.secondary_text));
                return v;
            }
        };
        this.sp_app_type.setAdapter(this.arrayAdapter);
    }

    void getProviderAvailability(String dr_id, String date) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = dr_id;
        final String str2 = date;
        Request strReq = new StringRequest(1, EndPoints.FETCH_AVAILABILITY, new C10134(), new C10145()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("date", str2);
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
