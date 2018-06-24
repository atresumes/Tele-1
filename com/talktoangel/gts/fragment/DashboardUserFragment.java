package com.talktoangel.gts.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
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
import com.talktoangel.gts.adapter.MyProviderAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.MyProviderItem;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardUserFragment extends Fragment implements OnClickListener {
    public static ArrayList<MyProviderItem> list;
    private MyProviderAdapter adapter;
    LinearLayout apoLayout;
    Button btnDetails;
    Button btnMessage;
    Button btnSchedule;
    Button findProvider;
    CircleImageView img_ud;
    private ProgressBar mProgressBar;
    LinearLayout noApoLayout;
    SessionManager session;
    TextView tv_badge;
    TextView txt_date;
    TextView txt_name;
    TextView txt_time;
    private View view;

    class C09892 implements Listener<String> {
        C09892() {
        }

        public void onResponse(String response) {
            DashboardUserFragment.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONObject object;
                    DashboardUserFragment.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        MyProviderItem item = new MyProviderItem();
                        item.setId(object.getString("dr_id"));
                        item.setName(object.getString("dr_name"));
                        item.setAvailability(object.getString("availability"));
                        item.setCharge(object.getString(SessionManager.KEY_LICENSE_NO));
                        item.setImage(object.getString("dr_pic"));
                        DashboardUserFragment.list.add(item);
                    }
                    object = jObj.getJSONObject("appointment");
                    if (!object.has("apo_result")) {
                        DashboardUserFragment.this.apoLayout.setVisibility(0);
                        DashboardUserFragment.this.noApoLayout.setVisibility(8);
                        JSONObject object1 = jObj.getJSONObject("appointment");
                        String date = object1.getString("apo_date");
                        DashboardUserFragment.this.txt_time.setText(object1.getString("apo_time"));
                        DashboardUserFragment.this.txt_name.setText(object1.getString("dr_name"));
                        Glide.with(DashboardUserFragment.this.getContext()).load(object.getString("pic")).error((int) C0585R.drawable.ic_user_white_24dp).into(DashboardUserFragment.this.img_ud);
                        Date date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date);
                        DashboardUserFragment.this.txt_date.setText(new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(date1));
                    }
                    DashboardUserFragment.this.btnSchedule.setEnabled(true);
                } else {
                    Log.e(SdkConstants.FALSE_STRING, jObj.getString("message"));
                    DashboardUserFragment.list.clear();
                    DashboardUserFragment.list.add(new MyProviderItem("", "", "", ""));
                }
                DashboardUserFragment.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                DashboardUserFragment.list.clear();
                DashboardUserFragment.list.add(new MyProviderItem());
                DashboardUserFragment.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
    }

    class C09903 implements ErrorListener {
        C09903() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            DashboardUserFragment.this.mProgressBar.setVisibility(8);
            DashboardUserFragment.list.clear();
            DashboardUserFragment.list.add(new MyProviderItem("", "", "", ""));
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(C0585R.layout.fragment_user_dashboard, container, false);
            this.apoLayout = (LinearLayout) this.view.findViewById(C0585R.id.layout_apo);
            this.noApoLayout = (LinearLayout) this.view.findViewById(C0585R.id.layout_no_apo);
            this.mProgressBar = (ProgressBar) this.view.findViewById(C0585R.id.progress_dash);
            CircleImageView imageView = (CircleImageView) this.view.findViewById(C0585R.id.img_dash);
            TextView name = (TextView) this.view.findViewById(C0585R.id.txt_name_dash);
            this.img_ud = (CircleImageView) this.view.findViewById(C0585R.id.img_ud);
            this.txt_name = (TextView) this.view.findViewById(C0585R.id.txt_name_ud);
            this.txt_date = (TextView) this.view.findViewById(C0585R.id.txt_date_ud);
            this.txt_time = (TextView) this.view.findViewById(C0585R.id.txt_time_ud);
            this.tv_badge = (TextView) this.view.findViewById(C0585R.id.tv_badge);
            this.findProvider = (Button) this.view.findViewById(C0585R.id.btn_find_provider);
            this.btnSchedule = (Button) this.view.findViewById(C0585R.id.btn_schedule);
            this.btnMessage = (Button) this.view.findViewById(C0585R.id.btn_message);
            this.btnDetails = (Button) this.view.findViewById(C0585R.id.btn_details);
            RecyclerView recyclerView = (RecyclerView) this.view.findViewById(C0585R.id.recycler_dash);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            this.session = new SessionManager(getContext());
            name.setText(((String) this.session.getUser().get(SessionManager.KEY_FIRST_NAME)).concat(" " + ((String) this.session.getUser().get(SessionManager.KEY_LAST_NAME))));
            Glide.with(getContext()).load((String) this.session.getUser().get(SessionManager.KEY_IMAGE)).into(imageView);
            list = new ArrayList();
            this.adapter = new MyProviderAdapter(getContext(), list);
            recyclerView.setAdapter(this.adapter);
            this.findProvider.setOnClickListener(this);
            this.btnSchedule.setOnClickListener(this);
            this.btnMessage.setOnClickListener(this);
            this.btnDetails.setOnClickListener(this);
            this.btnSchedule.setEnabled(false);
        }
        return this.view;
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_dashboard));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(0).setChecked(true);
        this.session = new SessionManager(getContext());
        if (this.session.getNotificationCount() == 0) {
            this.tv_badge.setText("");
        } else {
            this.tv_badge.setText("" + this.session.getNotificationCount());
        }
        getMyProviders((String) this.session.getUser().get(SessionManager.KEY_ID));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0585R.menu.menu_main, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != C0585R.id.action_in_crisis) {
            return super.onOptionsItemSelected(item);
        }
        showCrisisDialog();
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_schedule:
                Fragment fragment = new ScheduleFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, fragment).addToBackStack(null).commit();
                return;
            case C0585R.id.btn_message:
                this.session.setNotificationCount(0);
                getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new MessagesFragment()).addToBackStack(null).commit();
                return;
            case C0585R.id.btn_find_provider:
                getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new TherapistListFragment()).addToBackStack(null).commit();
                return;
            case C0585R.id.btn_details:
                getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new AppointmentsFragment()).addToBackStack(null).commit();
                return;
            default:
                return;
        }
    }

    private void showCrisisDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0585R.layout.dialog_incrisis);
        dialog.setTitle(getString(C0585R.string.action_in_crisis));
        ((Button) dialog.findViewById(C0585R.id.btn_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }

    private void getMyProviders(String userId) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userId;
        Request strReq = new StringRequest(1, EndPoints.MY_PROVIDERS, new C09892(), new C09903()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_ID, str);
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
