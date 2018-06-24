package com.talktoangel.gts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.Item;
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

public class FiltersActivity extends AppCompatActivity implements OnClickListener {
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<Item> adapter2;
    private ArrayAdapter<Item> adapter3;
    private ArrayAdapter<Item> adapter4;
    private String app_type;
    private ArrayList<Item> appointmentList;
    private TextView endRange;
    private ArrayList<String> languageList;
    private SessionManager mSessionManager;
    private RadioButton radioFemale;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private CrystalRangeSeekbar rangeSeekbar;
    private Spinner spAppointType;
    private Spinner spLanguage;
    private Spinner spSpecialty;
    private Spinner spTherapist;
    private String speciality;
    private ArrayList<Item> specialityList;
    private TextView startRange;
    private String therapist;
    private ArrayList<Item> therapistList;

    class C05733 implements OnItemSelectedListener {
        C05733() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            FiltersActivity.this.therapist = ((Item) FiltersActivity.this.therapistList.get(position)).getId();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05744 implements OnItemSelectedListener {
        C05744() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            FiltersActivity.this.app_type = ((Item) FiltersActivity.this.appointmentList.get(position)).getId();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05755 implements OnItemSelectedListener {
        C05755() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            FiltersActivity.this.speciality = ((Item) FiltersActivity.this.specialityList.get(position)).getId();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C09731 implements OnRangeSeekbarChangeListener {
        C09731() {
        }

        public void valueChanged(Number minValue, Number maxValue) {
            FiltersActivity.this.startRange.setText(String.valueOf(minValue).concat((String) FiltersActivity.this.mSessionManager.getUser().get(SessionManager.KEY_CURRENCY_PREF)));
            FiltersActivity.this.endRange.setText(String.valueOf(maxValue).concat((String) FiltersActivity.this.mSessionManager.getUser().get(SessionManager.KEY_CURRENCY_PREF)));
        }
    }

    class C09742 implements OnRangeSeekbarFinalValueListener {
        C09742() {
        }

        public void finalValue(Number minValue, Number maxValue) {
            Log.e("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
        }
    }

    class C09759 implements Listener<String> {
        C09759() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    int i;
                    JSONObject object;
                    JSONArray language = jObj.getJSONArray("language");
                    JSONArray therapist = jObj.getJSONArray(SessionManager.KEY_THERAPIST_TYPE);
                    JSONArray appointment = jObj.getJSONArray("appointment_type");
                    JSONArray speciality = jObj.getJSONArray("dr_speciality_list");
                    for (i = 0; i < language.length(); i++) {
                        FiltersActivity.this.languageList.add(language.getJSONObject(i).getString("lg_name"));
                    }
                    for (i = 0; i < therapist.length(); i++) {
                        object = therapist.getJSONObject(i);
                        FiltersActivity.this.therapistList.add(new Item(object.getString("therapist_type_id"), object.getString(SessionManager.KEY_THERAPIST_TYPE)));
                    }
                    for (i = 0; i < appointment.length(); i++) {
                        object = appointment.getJSONObject(i);
                        FiltersActivity.this.appointmentList.add(new Item(object.getString("id"), object.getString("apo_type")));
                    }
                    for (i = 0; i < speciality.length(); i++) {
                        object = speciality.getJSONObject(i);
                        FiltersActivity.this.specialityList.add(new Item(object.getString("id_dr_speciality"), object.getString("dr_speciality")));
                    }
                    FiltersActivity.this.adapter1.notifyDataSetChanged();
                    FiltersActivity.this.adapter2.notifyDataSetChanged();
                    FiltersActivity.this.adapter3.notifyDataSetChanged();
                    FiltersActivity.this.adapter4.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(FiltersActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(FiltersActivity.this.getApplicationContext(), "error: " + e.getMessage(), 1).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_filters);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.spTherapist = (Spinner) findViewById(C0585R.id.sp_therapist);
        this.spSpecialty = (Spinner) findViewById(C0585R.id.sp_speciality);
        this.spAppointType = (Spinner) findViewById(C0585R.id.spAppointType);
        this.spLanguage = (Spinner) findViewById(C0585R.id.sp_language);
        this.rangeSeekbar = (CrystalRangeSeekbar) findViewById(C0585R.id.rangeSeekbar1);
        this.startRange = (TextView) findViewById(C0585R.id.txt_start_range);
        this.endRange = (TextView) findViewById(C0585R.id.txt_end_range);
        this.radioGroup = (RadioGroup) findViewById(C0585R.id.radio_filter);
        this.radioMale = (RadioButton) findViewById(C0585R.id.radio_male);
        this.radioFemale = (RadioButton) findViewById(C0585R.id.radio_female);
        Button btnApply = (Button) findViewById(C0585R.id.btn_apply);
        this.radioMale.setChecked(true);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.rangeSeekbar.setOnRangeSeekbarChangeListener(new C09731());
        this.rangeSeekbar.setOnRangeSeekbarFinalValueListener(new C09742());
        btnApply.setOnClickListener(this);
        this.spTherapist.setOnItemSelectedListener(new C05733());
        this.spAppointType.setOnItemSelectedListener(new C05744());
        this.spSpecialty.setOnItemSelectedListener(new C05755());
        init();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0585R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            return true;
        } else if (item.getItemId() != C0585R.id.action_clear_all) {
            return super.onOptionsItemSelected(item);
        } else {
            this.rangeSeekbar.setMinValue(0.0f);
            this.rangeSeekbar.setMaxValue(5000.0f);
            this.startRange.setText("0$");
            this.endRange.setText("5000$");
            this.rangeSeekbar.apply();
            this.radioMale.setChecked(true);
            this.radioFemale.setChecked(false);
            this.spLanguage.setSelection(0);
            this.spTherapist.setSelection(0);
            this.spSpecialty.setSelection(0);
            this.mSessionManager.setFilterData(null, null, "", null, null, null, null, null);
            return true;
        }
    }

    void init() {
        this.languageList = new ArrayList();
        this.appointmentList = new ArrayList();
        this.therapistList = new ArrayList();
        this.specialityList = new ArrayList();
        setAppointmentTypeAdapter();
        setTherapistAdapter();
        setSpecialityAdapter();
        this.adapter1 = new ArrayAdapter(this, 17367049, this.languageList);
        this.spLanguage.setAdapter(this.adapter1);
        getFiltersList();
    }

    void setAppointmentTypeAdapter() {
        this.adapter2 = new ArrayAdapter<Item>(this, 17367049, this.appointmentList) {
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(((Item) FiltersActivity.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setGravity(17);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                v.setPadding(15, 0, 15, 0);
                ((TextView) v).setTextSize(16.0f);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(GravityCompat.START);
                ((TextView) v).setText(((Item) FiltersActivity.this.appointmentList.get(position)).getTitle());
                ((TextView) v).setTextSize(16.0f);
                v.setPadding(15, 15, 15, 15);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                return v;
            }
        };
        this.spAppointType.setAdapter(this.adapter2);
    }

    void setTherapistAdapter() {
        this.adapter3 = new ArrayAdapter<Item>(this, 17367049, this.therapistList) {
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(((Item) FiltersActivity.this.therapistList.get(position)).getTitle());
                ((TextView) v).setGravity(17);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                v.setPadding(15, 0, 15, 0);
                ((TextView) v).setTextSize(16.0f);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(GravityCompat.START);
                ((TextView) v).setText(((Item) FiltersActivity.this.therapistList.get(position)).getTitle());
                ((TextView) v).setTextSize(16.0f);
                v.setPadding(15, 15, 15, 15);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                return v;
            }
        };
        this.spTherapist.setAdapter(this.adapter3);
    }

    void setSpecialityAdapter() {
        this.adapter4 = new ArrayAdapter<Item>(this, 17367049, this.specialityList) {
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(((Item) FiltersActivity.this.specialityList.get(position)).getTitle());
                ((TextView) v).setGravity(17);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                v.setPadding(15, 0, 15, 0);
                ((TextView) v).setTextSize(16.0f);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(GravityCompat.START);
                ((TextView) v).setText(((Item) FiltersActivity.this.specialityList.get(position)).getTitle());
                ((TextView) v).setTextSize(16.0f);
                v.setPadding(15, 15, 15, 15);
                ((TextView) v).setTextColor(FiltersActivity.this.getResources().getColor(C0585R.color.secondary_text));
                return v;
            }
        };
        this.spSpecialty.setAdapter(this.adapter4);
    }

    private void getFiltersList() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(1, EndPoints.FILTER_DATA, new C09759(), new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "" + error.getMessage());
            }
        });
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

    public void onClick(View view) {
        String gender;
        String language;
        if (this.radioGroup.getCheckedRadioButtonId() == this.radioMale.getId()) {
            gender = "m";
        } else {
            gender = "f";
        }
        if (this.spLanguage.getSelectedItemPosition() == 0) {
            language = "English";
        } else {
            language = this.spLanguage.getSelectedItem().toString();
        }
        this.mSessionManager.setFilterData(this.therapist, this.speciality, "", this.app_type, language, gender, String.valueOf(this.rangeSeekbar.getSelectedMinValue()), String.valueOf(this.rangeSeekbar.getSelectedMaxValue()));
        Log.e(SdkConstants.DATA, language);
        onBackPressed();
    }
}
