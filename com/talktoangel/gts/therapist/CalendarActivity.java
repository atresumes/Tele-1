package com.talktoangel.gts.therapist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.talktoangel.gts.model.AppointmentItem;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
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

public class CalendarActivity extends AppCompatActivity {
    CalendarApoListAdapter adapter;
    String date;
    String date1;
    private boolean isBetweenTime;
    private ArrayList<AppointmentItem> list;
    private ArrayList<AppointmentItem> list1;
    ProgressBar mProgressBar;

    class C10412 implements Listener<String> {
        C10412() {
        }

        public void onResponse(String response) {
            CalendarActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    CalendarActivity.this.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        AppointmentItem item = new AppointmentItem();
                        item.setApoID(object.getString("apo_id"));
                        item.setApoType(object.getString("apo_type"));
                        item.setApoDate(object.getString("apo_date"));
                        item.setApoTime(object.getString("apo_time"));
                        item.setApoStatus(object.getString("apo_status"));
                        item.setName(object.getString("name"));
                        item.setImage(object.getString("pic"));
                        item.setMobile(object.getString("mobile"));
                        CalendarActivity.this.list.add(item);
                        if (item.getApoDate().equals(CalendarActivity.this.date)) {
                            CalendarActivity.this.checkTime(((AppointmentItem) CalendarActivity.this.list.get(i)).getApoTime());
                            if (CalendarActivity.this.isBetweenTime) {
                                CalendarActivity.this.list1.add(item);
                            }
                        }
                    }
                } else {
                    Toast.makeText(CalendarActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                }
                CalendarActivity.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                CalendarActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            }
        }
    }

    class C10423 implements ErrorListener {
        C10423() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            CalendarActivity.this.mProgressBar.setVisibility(8);
        }
    }

    class CalendarApoListAdapter extends Adapter<ViewHolder> {
        private ArrayList<AppointmentItem> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
            TextView txtName;
            TextView txtTime;

            ViewHolder(View itemView) {
                super(itemView);
                this.txtName = (TextView) itemView.findViewById(C0585R.id.txt_name_icl);
                this.txtTime = (TextView) itemView.findViewById(C0585R.id.txt_time_icl);
                itemView.setOnClickListener(this);
            }

            public void onClick(View v) {
                AppointmentItem item = (AppointmentItem) CalendarApoListAdapter.this.list.get(getAdapterPosition());
                Bundle bundle = new Bundle();
                bundle.putString("apoId", item.getApoID());
                bundle.putString("name", item.getName());
                bundle.putString("image", item.getImage());
                bundle.putString("date", item.getApoDate());
                bundle.putString("time", item.getApoTime());
                bundle.putString("type", item.getApoType());
                CalendarActivity.this.startActivity(new Intent(CalendarActivity.this.getApplicationContext(), AppointmentDetailActivity.class).putExtras(bundle));
            }
        }

        CalendarApoListAdapter(ArrayList<AppointmentItem> list) {
            this.list = list;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_calendar_list, parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            AppointmentItem item = (AppointmentItem) this.list.get(position);
            if (item.getApoDate().equals(CalendarActivity.this.date)) {
                holder.txtName.setText(item.getName());
                holder.txtTime.setText(item.getApoTime());
            }
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
        setContentView((int) C0585R.layout.activity_calendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(C0585R.id.imgUser);
        TextView tv_name = (TextView) findViewById(C0585R.id.tv_name);
        final CalendarCustomView mView = (CalendarCustomView) findViewById(C0585R.id.custom_calendar);
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_calendar);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SessionManager mSessionManager = new SessionManager(getApplicationContext());
        tv_name.setText(((String) mSessionManager.getUser().get(SessionManager.KEY_FIRST_NAME)).concat(" ").concat((String) mSessionManager.getUser().get(SessionManager.KEY_FIRST_NAME)));
        Glide.with((FragmentActivity) this).load((String) mSessionManager.getUser().get(SessionManager.KEY_IMAGE)).into(imageView);
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.date = format.format(Calendar.getInstance().getTime());
        this.date1 = format.format(Calendar.getInstance().getTime());
        mView.setOnClickListener(new com.talktoangel.gts.listener.OnClickListener() {
            public void onClick() {
                Log.e("mView Pressed", mView.date.toString());
                CalendarActivity.this.date = format.format(mView.date);
                CalendarActivity.this.list1.clear();
                for (int i = 0; i < CalendarActivity.this.list.size(); i++) {
                    if (CalendarActivity.this.date.equals(((AppointmentItem) CalendarActivity.this.list.get(i)).getApoDate())) {
                        CalendarActivity.this.checkTime(((AppointmentItem) CalendarActivity.this.list.get(i)).getApoTime());
                        if (!CalendarActivity.this.date1.equals(((AppointmentItem) CalendarActivity.this.list.get(i)).getApoDate())) {
                            CalendarActivity.this.list1.add(CalendarActivity.this.list.get(i));
                        } else if (CalendarActivity.this.isBetweenTime) {
                            CalendarActivity.this.list1.add(CalendarActivity.this.list.get(i));
                        }
                    }
                }
                CalendarActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.list = new ArrayList();
        this.list1 = new ArrayList();
        this.adapter = new CalendarApoListAdapter(this.list1);
        recyclerView.setAdapter(this.adapter);
        getMyAppointmentsList((String) mSessionManager.getUser().get(SessionManager.KEY_ID), (String) mSessionManager.getUser().get(SessionManager.KEY_TYPE));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void getMyAppointmentsList(String userID, String userType) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = userType;
        Request strReq = new StringRequest(1, EndPoints.MY_APPOINTMENT, new C10412(), new C10423()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_ID, str);
                params.put(SessionManager.KEY_TYPE, str2);
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

    private void checkTime(String string1) {
        try {
            Date time1 = new SimpleDateFormat("HH:mm a", Locale.getDefault()).parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            System.out.println(calendar1.getTime());
            Date time2 = new SimpleDateFormat("HH:mm a", Locale.getDefault()).parse(string1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            System.out.println(calendar2.getTime());
            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            Date resultDate = new Date(yourmilliseconds);
            System.out.println(sdf.format(resultDate));
            Date d = new SimpleDateFormat("HH:mm a", Locale.getDefault()).parse(sdf.format(resultDate));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            if (calendar3.getTime().before(calendar2.getTime())) {
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
}
