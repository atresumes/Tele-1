package com.talktoangel.gts.fragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.talktoangel.gts.adapter.AppointmentAdapter;
import com.talktoangel.gts.adapter.AppointmentAdapter1;
import com.talktoangel.gts.adaptertherapist.AppointmentDrAdapter;
import com.talktoangel.gts.adaptertherapist.AppointmentDrAdapter1;
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

public class AppointmentsFragment extends Fragment {
    private static final String TAG = AppointmentsFragment.class.getSimpleName();
    private AppointmentAdapter adapter;
    private AppointmentAdapter1 adapter1;
    private AppointmentDrAdapter adapterT;
    private AppointmentDrAdapter1 adapterT1;
    private boolean isBetweenTime;
    private LinearLayout layout;
    private ArrayList<AppointmentItem> list;
    private ArrayList<AppointmentItem> list1;
    private ProgressBar mProgressBar;
    private SessionManager mSessionManager;
    private View view;

    class C05931 implements OnClickListener {
        C05931() {
        }

        public void onClick(View view) {
            AppointmentsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new TherapistListFragment()).addToBackStack(null).commit();
        }
    }

    class C09883 implements ErrorListener {
        C09883() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
            AppointmentsFragment.this.mProgressBar.setVisibility(8);
            AppointmentsFragment.this.layout.setVisibility(0);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(C0585R.layout.fragment_appointments, container, false);
            this.layout = (LinearLayout) this.view.findViewById(C0585R.id.linearLayout1);
            this.mProgressBar = (ProgressBar) this.view.findViewById(C0585R.id.progress_appointment);
            ((Button) this.view.findViewById(C0585R.id.btnSchedule)).setOnClickListener(new C05931());
            RecyclerView recyclerView = (RecyclerView) this.view.findViewById(C0585R.id.recycler_af);
            RecyclerView recyclerView1 = (RecyclerView) this.view.findViewById(C0585R.id.recycler_af1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
            this.list = new ArrayList();
            this.list1 = new ArrayList();
            this.mSessionManager = new SessionManager(getContext());
            if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("p")) {
                this.adapter = new AppointmentAdapter(getContext(), this.list, this.mProgressBar, this);
                this.adapter1 = new AppointmentAdapter1(getContext(), this.list1);
                recyclerView.setAdapter(this.adapter);
                recyclerView1.setAdapter(this.adapter1);
            } else {
                this.adapterT = new AppointmentDrAdapter(getContext(), this.list, this.mProgressBar, this);
                this.adapterT1 = new AppointmentDrAdapter1(getContext(), this.list1);
                recyclerView.setAdapter(this.adapterT);
                recyclerView1.setAdapter(this.adapterT1);
            }
        }
        return this.view;
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_appointment));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(1).setChecked(true);
        getMyAppointmentsList((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), (String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
    }

    private void getMyAppointmentsList(String userID, final String userType) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = userType;
        Request strReq = new StringRequest(1, EndPoints.MY_APPOINTMENT, new Listener<String>() {
            public void onResponse(String response) {
                System.out.println(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                AppointmentsFragment.this.mProgressBar.setVisibility(8);
                Log.e(AppointmentsFragment.TAG, response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("status").equals("true")) {
                        AppointmentsFragment.this.list.clear();
                        JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                        int i;
                        JSONObject object;
                        AppointmentItem item;
                        if (userType.equalsIgnoreCase("t")) {
                            for (i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                item = new AppointmentItem();
                                String apo_date = object.getString("apo_date");
                                if (object.getString("apo_status").equalsIgnoreCase("p") || object.getString("apo_status").equalsIgnoreCase("a")) {
                                    AppointmentsFragment.this.checkTime(object.getString("apo_date"), object.getString("apo_time"));
                                    if (AppointmentsFragment.this.isBetweenTime) {
                                        item.setApoID(object.getString("apo_id"));
                                        item.setUserId(object.getString(SessionManager.KEY_ID));
                                        item.setApoType(object.getString("apo_type"));
                                        item.setApoDate(object.getString("apo_date"));
                                        item.setApoTime(object.getString("apo_time"));
                                        item.setApoStatus(object.getString("apo_status"));
                                        item.setName(object.getString("name"));
                                        item.setImage(object.getString("pic"));
                                        item.setMobile(object.getString("mobile"));
                                        AppointmentsFragment.this.list.add(item);
                                    } else {
                                        item.setApoID(object.getString("apo_id"));
                                        item.setUserId(object.getString(SessionManager.KEY_ID));
                                        item.setApoType(object.getString("apo_type"));
                                        item.setApoDate(object.getString("apo_date"));
                                        item.setApoTime(object.getString("apo_time"));
                                        item.setApoStatus(object.getString("apo_status"));
                                        item.setName(object.getString("name"));
                                        item.setImage(object.getString("pic"));
                                        item.setMobile(object.getString("mobile"));
                                        AppointmentsFragment.this.list1.add(item);
                                    }
                                } else {
                                    item.setApoID(object.getString("apo_id"));
                                    item.setUserId(object.getString(SessionManager.KEY_ID));
                                    item.setApoType(object.getString("apo_type"));
                                    item.setApoDate(object.getString("apo_date"));
                                    item.setApoTime(object.getString("apo_time"));
                                    item.setApoStatus(object.getString("apo_status"));
                                    item.setName(object.getString("name"));
                                    item.setImage(object.getString("pic"));
                                    item.setMobile(object.getString("mobile"));
                                    AppointmentsFragment.this.list1.add(item);
                                }
                            }
                        } else {
                            for (i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                item = new AppointmentItem();
                                Log.e("TAG", object.getString("apo_date"));
                                if (object.getString("apo_status").equalsIgnoreCase("p") || object.getString("apo_status").equalsIgnoreCase("a")) {
                                    AppointmentsFragment.this.checkTime(object.getString("apo_date"), object.getString("apo_time"));
                                    if (AppointmentsFragment.this.isBetweenTime) {
                                        item.setApoID(object.getString("apo_id"));
                                        item.setUserId(object.getString(SessionManager.KEY_ID));
                                        item.setApoType(object.getString("apo_type"));
                                        item.setApoDate(object.getString("apo_date"));
                                        item.setApoTime(object.getString("apo_time"));
                                        item.setApoStatus(object.getString("apo_status"));
                                        item.setName(object.getString("dr_name"));
                                        item.setImage(object.getString("pic"));
                                        item.setMobile(object.getString("mobile"));
                                        AppointmentsFragment.this.list.add(item);
                                    } else {
                                        item.setApoID(object.getString("apo_id"));
                                        item.setUserId(object.getString(SessionManager.KEY_ID));
                                        item.setApoType(object.getString("apo_type"));
                                        item.setApoDate(object.getString("apo_date"));
                                        item.setApoTime(object.getString("apo_time"));
                                        item.setApoStatus(object.getString("apo_status"));
                                        item.setName(object.getString("dr_name"));
                                        item.setImage(object.getString("pic"));
                                        item.setMobile(object.getString("mobile"));
                                        AppointmentsFragment.this.list1.add(item);
                                    }
                                } else {
                                    item.setApoID(object.getString("apo_id"));
                                    item.setUserId(object.getString(SessionManager.KEY_ID));
                                    item.setApoType(object.getString("apo_type"));
                                    item.setApoDate(object.getString("apo_date"));
                                    item.setApoTime(object.getString("apo_time"));
                                    item.setApoStatus(object.getString("apo_status"));
                                    item.setName(object.getString("dr_name"));
                                    item.setImage(object.getString("pic"));
                                    item.setMobile(object.getString("mobile"));
                                    AppointmentsFragment.this.list1.add(item);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(AppointmentsFragment.this.getContext(), jObj.getString("message"), 1).show();
                    }
                    if (userType.equalsIgnoreCase("p")) {
                        AppointmentsFragment.this.adapter.notifyDataSetChanged();
                        AppointmentsFragment.this.adapter1.notifyDataSetChanged();
                        return;
                    }
                    AppointmentsFragment.this.adapterT.notifyDataSetChanged();
                    AppointmentsFragment.this.adapterT1.notifyDataSetChanged();
                } catch (JSONException e) {
                    AppointmentsFragment.this.layout.setVisibility(0);
                    AppointmentsFragment.this.mProgressBar.setVisibility(8);
                    e.printStackTrace();
                    Toast.makeText(AppointmentsFragment.this.getContext(), "error" + e.getMessage(), 1).show();
                }
            }
        }, new C09883()) {
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

    private void checkTime(String da, String string) {
        try {
            System.out.println(da + " " + string);
            Date time2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(da + " " + string);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time2);
            calendar1.add(12, 30);
            if (string.contains("AM")) {
                calendar1.set(9, 0);
            } else {
                calendar1.set(9, 1);
            }
            Log.e("calendar", calendar1.getTime() + "");
            long yourmilliseconds = System.currentTimeMillis();
            Date d = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(yourmilliseconds)));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            Log.e("calendar3", calendar3.getTime() + "");
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
}
