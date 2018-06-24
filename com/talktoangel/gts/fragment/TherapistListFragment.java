package com.talktoangel.gts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.talktoangel.gts.FiltersActivity;
import com.talktoangel.gts.adapter.ProviderAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.ProviderItem;
import com.talktoangel.gts.utils.DividerItemDecoration;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TherapistListFragment extends Fragment implements OnQueryTextListener {
    private ProviderAdapter adapter;
    private ArrayList<ProviderItem> list;
    private ProgressBar mProgressBar;
    SessionManager mSessionManager;

    class C05991 implements OnClickListener {
        C05991() {
        }

        public void onClick(View view) {
            TherapistListFragment.this.startActivity(new Intent(TherapistListFragment.this.getActivity(), FiltersActivity.class));
        }
    }

    class C09982 implements OnActionExpandListener {
        C09982() {
        }

        public boolean onMenuItemActionCollapse(MenuItem item) {
            return true;
        }

        public boolean onMenuItemActionExpand(MenuItem item) {
            return true;
        }
    }

    class C09993 implements Listener<String> {
        C09993() {
        }

        public void onResponse(String response) {
            TherapistListFragment.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    TherapistListFragment.this.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        ProviderItem item = new ProviderItem();
                        item.setId(object.getString("dr_id"));
                        item.setName(object.getString("name"));
                        item.setImage(object.getString("dr_pic"));
                        item.setCharge(object.getString(SessionManager.KEY_RATES));
                        TherapistListFragment.this.list.add(item);
                    }
                } else {
                    Toast.makeText(TherapistListFragment.this.getContext(), jObj.getString("message"), 1).show();
                }
                TherapistListFragment.this.adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                TherapistListFragment.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(TherapistListFragment.this.getContext(), "error" + e.getMessage(), 1).show();
            }
        }
    }

    class C10004 implements ErrorListener {
        C10004() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", "" + error.getMessage());
            TherapistListFragment.this.mProgressBar.setVisibility(8);
        }
    }

    class C10015 implements Listener<String> {
        C10015() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    TherapistListFragment.this.list.clear();
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        ProviderItem item = new ProviderItem();
                        item.setId(object.getString("dr_id"));
                        item.setName(object.getString("dr_name"));
                        item.setImage(object.getString("pic"));
                        TherapistListFragment.this.list.add(item);
                    }
                    TherapistListFragment.this.adapter.notifyDataSetChanged();
                    Toast.makeText(TherapistListFragment.this.getContext(), jObj.getString("message"), 1).show();
                    return;
                }
                Toast.makeText(TherapistListFragment.this.getContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(TherapistListFragment.this.getContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10026 implements ErrorListener {
        C10026() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_providers_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mProgressBar = (ProgressBar) view.findViewById(C0585R.id.progress_provider);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C0585R.id.recycler_providers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        ((FloatingActionButton) view.findViewById(C0585R.id.fab_filter)).setOnClickListener(new C05991());
        this.list = new ArrayList();
        this.adapter = new ProviderAdapter(getActivity(), this.list);
        recyclerView.setAdapter(this.adapter);
        this.mSessionManager = new SessionManager(getContext());
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_providers));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(4).setChecked(true);
        if (this.mSessionManager.getFilterData().get(SessionManager.F_SPECIALITY) != null) {
            getFilteredList((String) this.mSessionManager.getFilterData().get(SessionManager.F_THERAPIST), (String) this.mSessionManager.getFilterData().get(SessionManager.F_SPECIALITY), (String) this.mSessionManager.getFilterData().get(SessionManager.F_PAYMENT), (String) this.mSessionManager.getFilterData().get(SessionManager.F_APPOINTMENT), (String) this.mSessionManager.getFilterData().get(SessionManager.F_MIN), (String) this.mSessionManager.getFilterData().get(SessionManager.F_MAX), (String) this.mSessionManager.getFilterData().get(SessionManager.F_LANGUAGE), (String) this.mSessionManager.getFilterData().get(SessionManager.F_GENDER));
        } else {
            getProvidersList();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0585R.menu.menu_provider, menu);
        MenuItem myActionMenuItem = menu.findItem(C0585R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);
        searchView.setQueryHint(getResources().getString(C0585R.string.search_providers));
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(myActionMenuItem, new C09982());
    }

    private void getProvidersList() {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        Request strReq = new StringRequest(1, EndPoints.DOCTOR_LIST, new C09993(), new C10004());
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

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String newText) {
        this.adapter.setFilter(filter(this.list, newText));
        return true;
    }

    private List<ProviderItem> filter(List<ProviderItem> models, String query) {
        query = query.toLowerCase();
        List<ProviderItem> filteredModelList = new ArrayList();
        for (ProviderItem model : models) {
            if (model.getName().toLowerCase().contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void getFilteredList(String therapistType, String speciality, String paymentType, String appointType, String minRate, String maxRate, String language, String gender) {
        GeneralSecurityException e;
        HttpStack stack;
        final String str = therapistType;
        final String str2 = speciality;
        final String str3 = paymentType;
        final String str4 = appointType;
        final String str5 = language;
        final String str6 = gender;
        final String str7 = minRate;
        final String str8 = maxRate;
        Request strReq = new StringRequest(1, EndPoints.FILTERED_LIST, new C10015(), new C10026()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_THERAPIST_TYPE, str);
                params.put("dr_speciality", str2);
                params.put("payment_type", str3);
                params.put("appointment_type", str4);
                params.put("dr_language", str5);
                params.put(SessionManager.KEY_GENDER, str6);
                params.put("min_rate", str7);
                params.put("max_rate", str8);
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
}
