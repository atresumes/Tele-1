package com.talktoangel.gts.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.payUMoney.sdk.SdkConstants;
import com.payu.custombrowser.util.CBConstant;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupportFragment extends Fragment {
    LinearLayout layout;
    String number;
    ProgressBar progressBar;

    class C05981 implements OnClickListener {
        C05981() {
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + SupportFragment.this.number));
            SupportFragment.this.startActivity(intent);
        }
    }

    class C09962 implements Listener<String> {
        C09962() {
        }

        public void onResponse(String response) {
            Log.e(CBConstant.RESPONSE, response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        TextView view = new TextView(SupportFragment.this.getContext());
                        SupportFragment.this.number = object.getString("contact_no");
                        view.setText(object.getString("content"));
                        SupportFragment.this.layout.addView(view);
                    }
                    Toast.makeText(SupportFragment.this.getContext(), jObj.getString("message"), 1).show();
                } else {
                    Toast.makeText(SupportFragment.this.getContext(), jObj.getString("message"), 1).show();
                }
                SupportFragment.this.progressBar.setVisibility(8);
            } catch (JSONException e) {
                e.printStackTrace();
                SupportFragment.this.progressBar.setVisibility(8);
            }
        }
    }

    class C09973 implements ErrorListener {
        C09973() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            SupportFragment.this.progressBar.setVisibility(8);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_support, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.layout = (LinearLayout) view.findViewById(C0585R.id.linear_support);
        this.progressBar = (ProgressBar) view.findViewById(C0585R.id.progress_support);
        ((Button) view.findViewById(C0585R.id.btn_call)).setOnClickListener(new C05981());
        getSupportContents();
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.support));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(7).setChecked(true);
    }

    private void getSupportContents() {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        Request strReq = new StringRequest(1, EndPoints.SUPPORT_PAGE, new C09962(), new C09973());
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
