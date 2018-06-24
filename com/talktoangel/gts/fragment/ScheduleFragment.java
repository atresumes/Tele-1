package com.talktoangel.gts.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.AvailableTimes;
import com.talktoangel.gts.model.Item;
import com.talktoangel.gts.model.MyProviderItem;
import com.talktoangel.gts.schedule.ConfirmScheduleActivity;
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

public class ScheduleFragment extends Fragment implements OnClickListener {
    private AllTimeSlotAdapter adapter;
    String app_type;
    private ArrayList<Item> appointmentList;
    private ArrayAdapter<Item> arrayAdapter;
    String charge;
    String date;
    String date1;
    String image;
    private CircleImageView imageView;
    int index = 0;
    private boolean isBetweenTime;
    private View layout;
    private ProgressBar mProgressBar;
    String providerId;
    private RecyclerView recyclerView;
    private ArrayList<AvailableTimes> slotArrayList;
    String slotId;
    private Spinner sp_app_type;
    String time = "";
    String timingId;
    private TextView txtName;

    class C05952 implements OnItemSelectedListener {
        C05952() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ScheduleFragment.this.app_type = ((Item) ScheduleFragment.this.appointmentList.get(position)).getId();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C09944 implements Listener<String> {
        C09944() {
        }

        public void onResponse(String response) {
            ScheduleFragment.this.mProgressBar.setVisibility(8);
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    int i;
                    JSONObject object;
                    ScheduleFragment.this.slotArrayList.clear();
                    ScheduleFragment.this.appointmentList.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        AvailableTimes availableTimes = new AvailableTimes();
                        availableTimes.setSlotId(object.getString("slot_id"));
                        availableTimes.setTimeId(object.getString("timing_id"));
                        availableTimes.setTime(object.getString("slot"));
                        availableTimes.setStatus(object.getString("status"));
                        ScheduleFragment.this.slotArrayList.add(availableTimes);
                    }
                    JSONArray appointment = jObj.getJSONArray("appointment_type");
                    ScheduleFragment.this.appointmentList.add(new Item("id", "Select Appointment Type"));
                    for (i = 0; i < appointment.length(); i++) {
                        object = appointment.getJSONObject(i);
                        ScheduleFragment.this.appointmentList.add(new Item(object.getString("apo_id"), object.getString("appointment_type")));
                    }
                    ScheduleFragment.this.arrayAdapter.notifyDataSetChanged();
                    ScheduleFragment.this.adapter.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(ScheduleFragment.this.getContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                ScheduleFragment.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            }
        }
    }

    class C09955 implements ErrorListener {
        C09955() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            ScheduleFragment.this.mProgressBar.setVisibility(8);
        }
    }

    class AllTimeSlotAdapter extends Adapter<ViewHolder> {
        Context context;
        ArrayList<AvailableTimes> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            TextView btnTime;

            ViewHolder(View itemView) {
                super(itemView);
                this.btnTime = (TextView) itemView.findViewById(C0585R.id.txtTime);
            }
        }

        AllTimeSlotAdapter(Context context, ArrayList<AvailableTimes> list) {
            this.context = context;
            this.list = list;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_time_slot, parent, false));
        }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.btnTime.setText(((AvailableTimes) this.list.get(position)).getTime());
            if (((AvailableTimes) this.list.get(position)).getStatus().equalsIgnoreCase("0")) {
                holder.btnTime.setEnabled(false);
                holder.btnTime.setTextColor(ScheduleFragment.this.getResources().getColor(C0585R.color.colorPrimary));
            } else {
                holder.btnTime.setEnabled(true);
                holder.btnTime.setTextColor(ScheduleFragment.this.getResources().getColor(C0585R.color.primary_text));
            }
            holder.btnTime.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ScheduleFragment.this.index = holder.getAdapterPosition();
                    AllTimeSlotAdapter.this.notifyDataSetChanged();
                }
            });
            if (ScheduleFragment.this.index == position) {
                holder.btnTime.setBackgroundColor(ScheduleFragment.this.getResources().getColor(C0585R.color.colorPrimary));
                holder.btnTime.setTextColor(ScheduleFragment.this.getResources().getColor(C0585R.color.white));
                ScheduleFragment.this.time = ((AvailableTimes) this.list.get(position)).getTime();
                ScheduleFragment.this.slotId = ((AvailableTimes) this.list.get(holder.getAdapterPosition())).getSlotId();
                return;
            }
            holder.btnTime.setBackgroundColor(ScheduleFragment.this.getResources().getColor(C0585R.color.transparent));
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getItemCount() {
            return this.list.size();
        }
    }

    class RecyclerAdapter extends Adapter<ViewHolder> {
        private Context context;
        private ArrayList<MyProviderItem> list;

        class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
            ImageView imageView;
            TextView txtName;
            TextView txtRate;

            ViewHolder(View itemView) {
                super(itemView);
                this.imageView = (ImageView) itemView.findViewById(C0585R.id.imgTherapist);
                this.txtName = (TextView) itemView.findViewById(C0585R.id.txtName);
                this.txtName.setTextSize(14.0f);
                this.txtRate = (TextView) itemView.findViewById(C0585R.id.txtRate);
                itemView.setOnClickListener(this);
            }

            public void onClick(View view) {
                if (RecyclerAdapter.this.list.size() <= 1) {
                    ((MainActivity) RecyclerAdapter.this.context).getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new TherapistListFragment()).addToBackStack(null).commit();
                    return;
                }
                MyProviderItem item = (MyProviderItem) RecyclerAdapter.this.list.get(getAdapterPosition());
                ScheduleFragment.this.providerId = item.getId();
                String name = item.getName();
                Glide.with(RecyclerAdapter.this.context).load(item.getImage()).error(RecyclerAdapter.this.context.getResources().getDrawable(C0585R.drawable.ic_user_black_24dp)).into(ScheduleFragment.this.imageView);
                ScheduleFragment.this.txtName.setText(name);
                ScheduleFragment.this.recyclerView.setVisibility(8);
                ScheduleFragment.this.layout.setVisibility(0);
            }
        }

        RecyclerAdapter(Context context, ArrayList<MyProviderItem> imageList) {
            this.context = context;
            this.list = imageList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_provider, parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            MyProviderItem item = (MyProviderItem) this.list.get(position);
            if (position == 0) {
                holder.txtName.setText(item.getName());
                holder.txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, this.context.getResources().getDrawable(17301507), null);
                Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_add_white_24dp)).into(holder.imageView);
                return;
            }
            holder.txtName.setText(item.getName());
            Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_add_white_24dp)).into(holder.imageView);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getItemCount() {
            return this.list.size();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0585R.layout.fragment_schedule, container, false);
        ArrayList<MyProviderItem> list = (ArrayList) getArguments().getSerializable("list");
        this.layout = view.findViewById(C0585R.id.linear_sf);
        this.imageView = (CircleImageView) view.findViewById(C0585R.id.img_sf);
        this.txtName = (TextView) view.findViewById(C0585R.id.txt_name_sf);
        final TextView tvAvailability = (TextView) view.findViewById(C0585R.id.txt_availability);
        this.sp_app_type = (Spinner) view.findViewById(C0585R.id.sp_app_type_sf);
        Button btnSend = (Button) view.findViewById(C0585R.id.btn_send_sf);
        this.mProgressBar = (ProgressBar) view.findViewById(C0585R.id.progress_sf);
        this.recyclerView = (RecyclerView) view.findViewById(C0585R.id.recycler_fs);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        final CalendarCustomView mView = (CalendarCustomView) view.findViewById(C0585R.id.custom_calendar);
        if (list != null) {
            int size = list.size() - 1;
            this.providerId = ((MyProviderItem) list.get(size)).getId();
            this.txtName.setText(((MyProviderItem) list.get(size)).getName());
            this.image = ((MyProviderItem) list.get(size)).getImage();
            this.charge = ((MyProviderItem) list.get(size)).getCharge();
            if (((MyProviderItem) list.get(size)).getAvailability() != null) {
                if (((MyProviderItem) list.get(size)).getAvailability().equalsIgnoreCase("yes")) {
                    tvAvailability.setText("Available");
                } else {
                    tvAvailability.setText("Not Available");
                }
            }
            Glide.with(getContext()).load(this.image).error((int) C0585R.drawable.ic_user_black_24dp).into(this.imageView);
        }
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        this.date = format.format(calendar.getTime());
        this.date1 = format.format(calendar.getTime());
        this.recyclerView.setAdapter(new RecyclerAdapter(getContext(), list));
        this.layout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        mView.setOnClickListener(new com.talktoangel.gts.listener.OnClickListener() {
            public void onClick() {
                Log.e("TAG", "" + mView.date.toString().substring(0, 3));
                if (tvAvailability.getText().equals("Available")) {
                    ScheduleFragment.this.date = format.format(mView.date);
                    ScheduleFragment.this.slotArrayList.clear();
                    ScheduleFragment.this.index = 0;
                    ScheduleFragment.this.adapter.notifyDataSetChanged();
                    ScheduleFragment.this.getProviderAvailability(ScheduleFragment.this.providerId, ScheduleFragment.this.date);
                    return;
                }
                ScheduleFragment.this.slotArrayList.clear();
                ScheduleFragment.this.index = 0;
                ScheduleFragment.this.adapter.notifyDataSetChanged();
            }
        });
        this.sp_app_type.setOnItemSelectedListener(new C05952());
        setAppointmentTypeAdapter();
        this.slotArrayList = new ArrayList();
        this.adapter = new AllTimeSlotAdapter(getContext(), this.slotArrayList);
        this.recyclerView.setAdapter(this.adapter);
        if (tvAvailability.getText().equals("Available")) {
            getProviderAvailability(this.providerId, this.date);
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_schedule));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(2).setChecked(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_send_sf:
                checkFormData(view);
                return;
            default:
                return;
        }
    }

    void setAppointmentTypeAdapter() {
        this.appointmentList = new ArrayList();
        this.arrayAdapter = new ArrayAdapter<Item>(getActivity(), 17367049, this.appointmentList) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(((Item) ScheduleFragment.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setGravity(17);
                ((TextView) v).setTextColor(ScheduleFragment.this.getResources().getColor(C0585R.color.secondary_text));
                v.setPadding(15, 0, 15, 0);
                ((TextView) v).setTextSize(16.0f);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(GravityCompat.START);
                ((TextView) v).setText(((Item) ScheduleFragment.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setTextSize(16.0f);
                v.setPadding(15, 15, 15, 15);
                ((TextView) v).setTextColor(ScheduleFragment.this.getResources().getColor(C0585R.color.secondary_text));
                return v;
            }
        };
        this.sp_app_type.setAdapter(this.arrayAdapter);
    }

    void checkFormData(View view) {
        checkTime(this.date, ((AvailableTimes) this.slotArrayList.get(this.index)).getTime());
        if (this.time.equalsIgnoreCase("")) {
            Snackbar.make(view, (CharSequence) "Please select Appointment Time", -1).show();
        } else if (this.sp_app_type.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select Appointment Type", -1).show();
        } else if (((AvailableTimes) this.slotArrayList.get(this.index)).getStatus().equals("0")) {
            Snackbar.make(view, (CharSequence) "Slot you have selected is already Booked, Please select Another Time.", -1).show();
        } else if (!this.date.contentEquals(this.date1)) {
            bundle = new Bundle();
            bundle.putString("id", this.providerId);
            bundle.putString("name", this.txtName.getText().toString());
            bundle.putString("image", this.image);
            bundle.putString("type", this.app_type);
            bundle.putString("date", this.date);
            bundle.putString("time", this.time);
            bundle.putString("slot_id", this.slotId);
            bundle.putString("timingId", this.timingId);
            bundle.putString(SessionManager.KEY_LICENSE_NO, this.charge);
            bundle.putString("message", "");
            bundle.putString("referral", "");
            startActivityForResult(new Intent(getContext(), ConfirmScheduleActivity.class).putExtras(bundle), 0);
        } else if (this.isBetweenTime) {
            bundle = new Bundle();
            bundle.putString("id", this.providerId);
            bundle.putString("name", this.txtName.getText().toString());
            bundle.putString("image", this.image);
            bundle.putString("type", this.app_type);
            bundle.putString("date", this.date);
            bundle.putString("time", this.time);
            bundle.putString("slot_id", this.slotId);
            bundle.putString("timingId", this.timingId);
            bundle.putString(SessionManager.KEY_LICENSE_NO, this.charge);
            bundle.putString("message", "");
            bundle.putString("referral", "");
            startActivityForResult(new Intent(getContext(), ConfirmScheduleActivity.class).putExtras(bundle), 0);
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

    void getProviderAvailability(String dr_id, String date) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = dr_id;
        final String str2 = date;
        Request strReq = new StringRequest(1, EndPoints.FETCH_AVAILABILITY, new C09944(), new C09955()) {
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
