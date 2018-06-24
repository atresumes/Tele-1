package com.talktoangel.gts.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.talktoangel.gts.listener.YourFragmentInterface;
import com.talktoangel.gts.model.CareerTest;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Class6Fragment extends Fragment implements OnClickListener, YourFragmentInterface {
    public static ArrayList<CareerTest> list;
    private ProgressBar progressBar;
    private RadioButton radioCouns;
    private RadioButton radioNoCouns;
    private SessionManager session;
    private TextView tv_description;
    private TextView tv_title;

    class C10261 implements Listener<String> {
        C10261() {
        }

        public void onResponse(String response) {
            Class6Fragment.this.progressBar.setVisibility(4);
            Log.e("Response", response);
            try {
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    Class6Fragment.list.add(new CareerTest(object1.getString("id"), object1.getString(SdkConstants.BANK_TITLE_STRING), object1.getString("description"), object1.getString("with_counseling"), object1.getString("without_counseling"), object1.getString("pdf_url"), object1.getString("status")));
                }
                Class6Fragment.this.tv_title.setText(((CareerTest) Class6Fragment.list.get(0)).getTitle());
                Class6Fragment.this.tv_description.setText(((CareerTest) Class6Fragment.list.get(0)).getDescription());
                Class10Fragment class10Fragment = new Class10Fragment();
            } catch (JSONException e) {
                e.printStackTrace();
                Class6Fragment.this.progressBar.setVisibility(4);
            }
        }
    }

    class C10272 implements ErrorListener {
        C10272() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Class6Fragment.this.progressBar.setVisibility(4);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_class6, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.progressBar = (ProgressBar) view.findViewById(C0585R.id.progressClass);
        this.tv_title = (TextView) view.findViewById(C0585R.id.tv_title);
        this.tv_description = (TextView) view.findViewById(C0585R.id.tv_description);
        this.radioCouns = (RadioButton) view.findViewById(C0585R.id.radioCouns);
        this.radioNoCouns = (RadioButton) view.findViewById(C0585R.id.radioNoCouns);
        this.session = new SessionManager(getContext());
        Button btnLogin = (Button) view.findViewById(C0585R.id.btnLogin);
        ((Button) view.findViewById(C0585R.id.btnReport)).setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        list = new ArrayList();
        getTestData();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btnLogin:
                if (this.radioCouns.isChecked()) {
                    this.session.setTestData(((CareerTest) list.get(0)).getTitle(), ((CareerTest) list.get(0)).getWithout_counseling());
                    startActivity(new Intent(getActivity(), TestLoginActivity.class));
                    return;
                } else if (this.radioNoCouns.isChecked()) {
                    this.session.setTestData(((CareerTest) list.get(0)).getTitle(), ((CareerTest) list.get(0)).getWith_counseling());
                    startActivity(new Intent(getActivity(), TestLoginActivity.class));
                    return;
                } else {
                    Snackbar.make(v, (CharSequence) "Please select any Test", 0).show();
                    return;
                }
            case C0585R.id.btnReport:
                if (list != null) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((CareerTest) list.get(0)).getPdf_url())));
                    return;
                }
                return;
            default:
                return;
        }
    }

    void getTestData() {
        HttpStack stack;
        GeneralSecurityException e;
        this.progressBar.setVisibility(0);
        Request strReq = new StringRequest(1, EndPoints.CAREER_TEST_DETAIL, new C10261(), new C10272());
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

    public void fragmentBecameVisible() {
    }
}
