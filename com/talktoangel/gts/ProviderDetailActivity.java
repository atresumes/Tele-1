package com.talktoangel.gts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
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
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.schedule.ScheduleActivity;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ProviderDetailActivity extends AppCompatActivity implements OnClickListener {
    public static String dr_id;
    public static String image;
    public static String name;
    private ImageView imageView;
    private ProgressBar mProgressBar;
    SessionManager manager;
    private TextView txtAbout;
    private TextView txtCity;
    private TextView txtCountry;
    private TextView txtEducation;
    private TextView txtLicenses;
    private TextView txtRates;
    private TextView txtSpecialities;
    private TextView txtState;

    class C09792 implements Listener<String> {
        C09792() {
        }

        public void onResponse(String response) {
            ProviderDetailActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONObject object = jObj.getJSONObject(SdkConstants.RESULT);
                    object.getString("dr_fname");
                    object.getString("dr_lname");
                    ProviderDetailActivity.this.txtAbout.setText(object.getString("dr_about"));
                    ProviderDetailActivity.this.txtSpecialities.setText(object.getString("dr_speciality"));
                    object.getString("demographics");
                    ProviderDetailActivity.this.txtRates.setText(object.getString(SessionManager.KEY_RATES));
                    object.getString("dr_language");
                    ProviderDetailActivity.this.txtLicenses.setText(object.getString("licences_no"));
                    ProviderDetailActivity.this.txtEducation.setText(object.getString(SessionManager.KEY_EDUCATION));
                    object.getString("dr_mobile");
                    ProviderDetailActivity.this.txtCity.setText(object.getString("dr_city"));
                    ProviderDetailActivity.this.txtState.setText(object.getString("dr_state"));
                    ProviderDetailActivity.this.txtCountry.setText(object.getString("dr_country"));
                    object.getString(SessionManager.KEY_THERAPIST_TYPE);
                    object.getString("payment_type");
                    ProviderDetailActivity.this.manager.setAvailability(object.getString("dr_availability"));
                    object.getString("pic");
                    return;
                }
                Toast.makeText(ProviderDetailActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                ProviderDetailActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
            }
        }
    }

    class C09803 implements ErrorListener {
        C09803() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            ProviderDetailActivity.this.mProgressBar.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_provider_detail);
        setSupportActionBar((Toolbar) findViewById(C0585R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppBarLayout layout = (AppBarLayout) findViewById(C0585R.id.app_bar);
        final CollapsingToolbarLayout layout1 = (CollapsingToolbarLayout) findViewById(C0585R.id.toolbar_layout);
        dr_id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        setTitle(name);
        this.imageView = (ImageView) findViewById(C0585R.id.imageView_pd);
        Glide.with((FragmentActivity) this).load(image).asBitmap().error((int) C0585R.mipmap.ic_launcher).into(this.imageView);
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_af);
        this.txtAbout = (TextView) findViewById(C0585R.id.txt_about_me);
        this.txtSpecialities = (TextView) findViewById(C0585R.id.txt_specialities);
        this.txtRates = (TextView) findViewById(C0585R.id.txt_rates);
        this.txtLicenses = (TextView) findViewById(C0585R.id.txt_licenses);
        this.txtEducation = (TextView) findViewById(C0585R.id.txt_education);
        this.txtCity = (TextView) findViewById(C0585R.id.txt_city_dash);
        this.txtState = (TextView) findViewById(C0585R.id.txt_state_dash);
        this.txtCountry = (TextView) findViewById(C0585R.id.txt_country_dash);
        this.manager = new SessionManager(getApplicationContext());
        getProvidersDetails(dr_id);
        Button btnMessage = (Button) findViewById(C0585R.id.btn_message);
        ((Button) findViewById(C0585R.id.btn_schedule)).setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        layout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (VERSION.SDK_INT >= 21) {
                    Window window;
                    if (verticalOffset <= -260) {
                        Log.e("if 1", verticalOffset + "");
                        try {
                            int color = ProviderDetailActivity.getDominantColor(((BitmapDrawable) ProviderDetailActivity.this.imageView.getDrawable()).getBitmap());
                            int color1 = ProviderDetailActivity.manipulateColor(color, 0.8f);
                            window = ProviderDetailActivity.this.getWindow();
                            window.addFlags(Integer.MIN_VALUE);
                            window.clearFlags(67108864);
                            layout1.setContentScrimColor(color);
                            ProviderDetailActivity.this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                            window.setStatusBarColor(color1);
                            return;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    Log.e("else 1", verticalOffset + "");
                    try {
                        window = ProviderDetailActivity.this.getWindow();
                        window.addFlags(Integer.MIN_VALUE);
                        window.clearFlags(67108864);
                        window.setStatusBarColor(0);
                        ProviderDetailActivity.this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0));
                    } catch (NullPointerException e2) {
                        e2.printStackTrace();
                    }
                } else if (verticalOffset <= -200) {
                    Log.e("if 2", verticalOffset + "");
                    try {
                        ProviderDetailActivity.this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ProviderDetailActivity.getDominantColor(((BitmapDrawable) ProviderDetailActivity.this.imageView.getDrawable()).getBitmap())));
                    } catch (NullPointerException e22) {
                        e22.printStackTrace();
                    }
                } else {
                    Log.e("else 2", verticalOffset + "");
                    ProviderDetailActivity.this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0));
                }
            }
        });
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public static int manipulateColor(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.min(Math.round(((float) Color.red(color)) * factor), 255), Math.min(Math.round(((float) Color.green(color)) * factor), 255), Math.min(Math.round(((float) Color.blue(color)) * factor), 255));
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
            case C0585R.id.btn_schedule:
                startActivity(new Intent(this, ScheduleActivity.class).putExtra("id", dr_id).putExtra("name", name).putExtra("image", image).putExtra(SessionManager.KEY_LICENSE_NO, this.txtRates.getText().toString().replace(" $", "")));
                return;
            case C0585R.id.btn_message:
                startActivity(new Intent(this, NewMessageActivity.class).putExtra("id", dr_id).putExtra("name", name));
                return;
            default:
                return;
        }
    }

    void getProvidersDetails(String dr_id) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = dr_id;
        Request strReq = new StringRequest(1, EndPoints.DOCTOR_DETAIL, new C09792(), new C09803()) {
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
