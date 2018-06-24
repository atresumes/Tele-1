package com.talktoangel.gts.therapist;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.talktoangel.gts.model.Availability;
import com.talktoangel.gts.utils.DatePickerFragment;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AvailabilityActivity extends AppCompatActivity implements OnClickListener {
    RecyclerAdapter adapter;
    String availability = "";
    Button btnEndTimeE;
    Button btnEndTimeM;
    Button btnFromDate;
    Button btnStartTimeE;
    Button btnStartTimeM;
    Button btnToDate;
    int date;
    ArrayList<Availability> list;
    ProgressBar mProgressBar;
    OnDateSetListener onDate = new C06346();
    SessionManager session;

    class C06302 implements OnTimeSetListener {
        C06302() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourString;
            String minuteSting;
            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            } else {
                hourString = "" + hourOfDay;
            }
            if (minute < 10) {
                minuteSting = "0" + minute;
            } else {
                minuteSting = "" + minute;
            }
            AvailabilityActivity.this.btnStartTimeM.setText(hourString + ":" + minuteSting + ":00");
        }
    }

    class C06313 implements OnTimeSetListener {
        C06313() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourString;
            String minuteSting;
            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            } else {
                hourString = "" + hourOfDay;
            }
            if (minute < 10) {
                minuteSting = "0" + minute;
            } else {
                minuteSting = "" + minute;
            }
            AvailabilityActivity.this.btnEndTimeM.setText(hourString + ":" + minuteSting + ":00");
        }
    }

    class C06324 implements OnTimeSetListener {
        C06324() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourString;
            String minuteSting;
            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            } else {
                hourString = "" + hourOfDay;
            }
            if (minute < 10) {
                minuteSting = "0" + minute;
            } else {
                minuteSting = "" + minute;
            }
            AvailabilityActivity.this.btnStartTimeE.setText(hourString + ":" + minuteSting + ":00");
        }
    }

    class C06335 implements OnTimeSetListener {
        C06335() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourString;
            String minuteSting;
            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            } else {
                hourString = "" + hourOfDay;
            }
            if (minute < 10) {
                minuteSting = "0" + minute;
            } else {
                minuteSting = "" + minute;
            }
            AvailabilityActivity.this.btnEndTimeE.setText(hourString + ":" + minuteSting + ":00");
        }
    }

    class C06346 implements OnDateSetListener {
        C06346() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            if (AvailabilityActivity.this.date == 1) {
                if (month < 10) {
                    AvailabilityActivity.this.btnFromDate.setText(year + "-0" + month + "-" + dayOfMonth);
                } else {
                    AvailabilityActivity.this.btnFromDate.setText(year + "-" + month + "-" + dayOfMonth);
                }
            } else if (month < 10) {
                AvailabilityActivity.this.btnToDate.setText(year + "-0" + month + "-" + dayOfMonth);
            } else {
                AvailabilityActivity.this.btnToDate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }
    }

    class C10378 implements ErrorListener {
        C10378() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            AvailabilityActivity.this.mProgressBar.setVisibility(8);
        }
    }

    class RecyclerAdapter extends Adapter<ViewHolder> {
        Context context;
        ArrayList<Availability> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            TextView tv_date;
            TextView tv_time_ee;
            TextView tv_time_es;
            TextView tv_time_me;
            TextView tv_time_ms;

            ViewHolder(View itemView) {
                super(itemView);
                this.tv_date = (TextView) itemView.findViewById(C0585R.id.tv_date);
                this.tv_time_ms = (TextView) itemView.findViewById(C0585R.id.tv_time_ms);
                this.tv_time_me = (TextView) itemView.findViewById(C0585R.id.tv_time_me);
                this.tv_time_es = (TextView) itemView.findViewById(C0585R.id.tv_time_es);
                this.tv_time_ee = (TextView) itemView.findViewById(C0585R.id.tv_time_ee);
            }
        }

        RecyclerAdapter(Context context, ArrayList<Availability> list) {
            this.context = context;
            this.list = list;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_availability, parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Availability slot = (Availability) this.list.get(position);
            holder.tv_date.setText(slot.getAvailable_date());
            holder.tv_time_ms.setText(slot.getM_start_time());
            holder.tv_time_me.setText(slot.getM_end_time());
            holder.tv_time_es.setText(slot.getE_start_time());
            holder.tv_time_ee.setText(slot.getE_end_time());
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
        setContentView((int) C0585R.layout.activity_availability);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeViews();
    }

    void initializeViews() {
        TextView tvDrName = (TextView) findViewById(C0585R.id.tv_dr_name);
        SwitchCompat aSwitch = (SwitchCompat) findViewById(C0585R.id.switch_a);
        final LinearLayout layout = (LinearLayout) findViewById(C0585R.id.linear_time_slot);
        this.btnStartTimeM = (Button) findViewById(C0585R.id.btnStartTimeM);
        this.btnEndTimeM = (Button) findViewById(C0585R.id.btnEndTimeM);
        this.btnStartTimeE = (Button) findViewById(C0585R.id.btnStartTimeE);
        this.btnEndTimeE = (Button) findViewById(C0585R.id.btnEndTimeE);
        this.btnFromDate = (Button) findViewById(C0585R.id.btnFromDate);
        this.btnToDate = (Button) findViewById(C0585R.id.btnToDate);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_aav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_avail);
        ((Button) findViewById(C0585R.id.btn_save_aav)).setOnClickListener(this);
        this.btnStartTimeM.setOnClickListener(this);
        this.btnEndTimeM.setOnClickListener(this);
        this.btnStartTimeE.setOnClickListener(this);
        this.btnEndTimeE.setOnClickListener(this);
        this.btnFromDate.setOnClickListener(this);
        this.btnToDate.setOnClickListener(this);
        this.list = new ArrayList();
        this.adapter = new RecyclerAdapter(getApplicationContext(), this.list);
        recyclerView.setAdapter(this.adapter);
        this.session = new SessionManager(this);
        tvDrName.setText(((String) this.session.getUser().get(SessionManager.KEY_FIRST_NAME)).concat(" ").concat((String) this.session.getUser().get(SessionManager.KEY_LAST_NAME)));
        if (this.session.getAvailability().equalsIgnoreCase("yes")) {
            aSwitch.setChecked(true);
            layout.setVisibility(0);
            this.availability = "yes";
        } else {
            layout.setVisibility(4);
            this.availability = "no";
        }
        aSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    layout.setVisibility(0);
                    AvailabilityActivity.this.availability = "yes";
                    return;
                }
                layout.setVisibility(4);
                AvailabilityActivity.this.availability = "no";
            }
        });
        getAllTimeSlots((String) this.session.getUser().get(SessionManager.KEY_ID));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        int mHour = c.get(11);
        int mMinute = c.get(12);
        switch (view.getId()) {
            case C0585R.id.btnStartTimeM:
                new TimePickerDialog(this, new C06302(), mHour, mMinute, false).show();
                return;
            case C0585R.id.btnEndTimeM:
                new TimePickerDialog(this, new C06313(), mHour, mMinute, false).show();
                return;
            case C0585R.id.btnStartTimeE:
                new TimePickerDialog(this, new C06324(), mHour, mMinute, false).show();
                return;
            case C0585R.id.btnEndTimeE:
                new TimePickerDialog(this, new C06335(), mHour, mMinute, false).show();
                return;
            case C0585R.id.btnFromDate:
                this.date = 1;
                showDatePicker();
                return;
            case C0585R.id.btnToDate:
                this.date = 2;
                showDatePicker();
                return;
            case C0585R.id.btn_save_aav:
                checkValidData(view);
                return;
            default:
                return;
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(1));
        args.putInt("month", calender.get(2));
        args.putInt("day", calender.get(5));
        args.putInt("minMax", 0);
        date.setArguments(args);
        date.setCallBack(this.onDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    void checkValidData(View view) {
        String m_start_time = this.btnStartTimeM.getText().toString().trim();
        String m_end_time = this.btnEndTimeM.getText().toString().trim();
        String e_start_time = this.btnStartTimeE.getText().toString().trim();
        String e_end_time = this.btnEndTimeE.getText().toString().trim();
        String from_date = this.btnFromDate.getText().toString().trim();
        String to_date = this.btnToDate.getText().toString().trim();
        if (m_start_time.equals("") && e_start_time.equals("")) {
            Snackbar.make(view, (CharSequence) "Please select Either Morning Start Time or Evening Start Time", 0).show();
        } else if (m_end_time.equals("") && e_end_time.equals("")) {
            Snackbar.make(view, (CharSequence) "Please select Either Morning End Time or Evening End Time", 0).show();
        } else if (from_date.equals("")) {
            Snackbar.make(view, (CharSequence) "Please select from Date", 0).show();
        } else if (to_date.equals("")) {
            Snackbar.make(view, (CharSequence) "Please select to Date", 0).show();
        } else {
            this.mProgressBar.setVisibility(0);
            updateProviderAvailability((String) this.session.getUser().get(SessionManager.KEY_ID), this.availability, m_start_time, m_end_time, e_start_time, e_end_time, from_date, to_date);
        }
    }

    private void updateProviderAvailability(String drId, String availability, String m_start_time, String m_end_time, String e_start_time, String e_end_time, String from_date, String to_date) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = availability;
        final String str2 = drId;
        final String str3 = availability;
        final String str4 = m_start_time;
        final String str5 = m_end_time;
        final String str6 = e_start_time;
        final String str7 = e_end_time;
        final String str8 = from_date;
        final String str9 = to_date;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_AVAILABILITY, new Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response);
                AvailabilityActivity.this.mProgressBar.setVisibility(8);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status").equals("true")) {
                        AvailabilityActivity.this.session.setAvailability(str);
                        Toast.makeText(AvailabilityActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    } else {
                        Toast.makeText(AvailabilityActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    }
                    AvailabilityActivity.this.onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AvailabilityActivity.this.mProgressBar.setVisibility(8);
                }
            }
        }, new C10378()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str2);
                params.put("dr_availability", str3);
                params.put("m_start_time", str4);
                params.put("m_end_time", str5);
                params.put("e_start_time", str6);
                params.put("e_end_time", str7);
                params.put("from_date", str8);
                params.put("to_date", str9);
                Log.e(SdkConstants.PARAMS, params.toString());
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

    void getAllTimeSlots(String dr_id) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = dr_id;
        Request strReq = new StringRequest(1, EndPoints.APO_DETAIL, new Listener<String>() {
            public void onResponse(String response) {
                AvailabilityActivity.this.mProgressBar.setVisibility(8);
                Log.e("Response", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status").equals("true")) {
                        JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Availability timeSlot = new Availability();
                            timeSlot.setAvailable_date(object.getString("available_date"));
                            timeSlot.setM_start_time(object.getString("m_start_time"));
                            timeSlot.setM_end_time(object.getString("m_end_time"));
                            timeSlot.setE_start_time(object.getString("e_start_time"));
                            timeSlot.setE_end_time(object.getString("e_end_time"));
                            AvailabilityActivity.this.list.add(timeSlot);
                        }
                        AvailabilityActivity.this.adapter.notifyDataSetChanged();
                        return;
                    }
                    Toast.makeText(AvailabilityActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AvailabilityActivity.this.mProgressBar.setVisibility(8);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AvailabilityActivity.this.mProgressBar.setVisibility(8);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
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
}
