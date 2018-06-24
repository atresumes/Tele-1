package com.talktoangel.gts.therapist;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.talktoangel.gts.adapter.SpinnerAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.CountryCode;
import com.talktoangel.gts.utils.Base64;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TherapistProfileActivity extends AppCompatActivity implements OnClickListener {
    private static final int REQUEST_READ_STORAGE = 1000;
    private int REQUEST_CAMERA = 99;
    private int SELECT_FILE = 999;
    private SpinnerAdapter adapter;
    private String base64image = "";
    Button btnSpeciality;
    private EditText etAbout;
    private EditText etAddress;
    private EditText etAddress1;
    private EditText etCharge;
    private EditText etCity;
    private EditText etEducation;
    private EditText etExperience;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNo;
    private EditText etZipCode;
    private CircleImageView imageView;
    private ArrayList<CountryCode> list;
    private SessionManager mSessionManager;
    ProgressBar progressBar;
    RadioButton radioFemale;
    private RadioGroup radioGroup;
    RadioButton radioMale;
    private AppCompatSpinner spCountry;
    private String specialityId = "";

    class C06361 implements OnItemSelectedListener {
        C06361() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            CountryCode code = (CountryCode) TherapistProfileActivity.this.list.get(position);
            if (position != 0) {
                TherapistProfileActivity.this.mSessionManager.setCountry(code.getName());
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06372 implements OnDismissListener {
        C06372() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialog) {
            TherapistProfileActivity.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1000);
        }
    }

    class C10545 implements ErrorListener {
        C10545() {
        }

        public void onErrorResponse(VolleyError error) {
            TherapistProfileActivity.this.progressBar.setVisibility(8);
            error.printStackTrace();
        }
    }

    class C10557 implements Listener<String> {
        C10557() {
        }

        public void onResponse(String response) {
            Log.e("Response", response);
            try {
                int i;
                JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                TherapistProfileActivity.this.list.add(new CountryCode("0", "Select Country", "--", SdkConstants.NULL_STRING));
                for (i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    String id = object1.getString("cntry_id");
                    String name = object1.getString(SdkConstants.COUNTRY);
                    String flag = object1.getString("flag");
                    TherapistProfileActivity.this.list.add(new CountryCode(id, name, object1.getString("phonecode"), flag));
                }
                TherapistProfileActivity.this.adapter.notifyDataSetChanged();
                for (i = 0; i < TherapistProfileActivity.this.list.size(); i++) {
                    if (((CountryCode) TherapistProfileActivity.this.list.get(i)).getName().equalsIgnoreCase((String) TherapistProfileActivity.this.mSessionManager.getUser().get("state"))) {
                        TherapistProfileActivity.this.spCountry.setSelection(i);
                    }
                }
                Log.e(SdkConstants.DATA, (String) TherapistProfileActivity.this.mSessionManager.getUser().get("state"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(TherapistProfileActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
            }
        }
    }

    class C10568 implements ErrorListener {
        C10568() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_provider_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_app);
        this.imageView = (CircleImageView) findViewById(C0585R.id.imgUser);
        this.radioGroup = (RadioGroup) findViewById(C0585R.id.gender_radio_app);
        this.radioMale = (RadioButton) findViewById(C0585R.id.radio_male_app);
        this.radioFemale = (RadioButton) findViewById(C0585R.id.radio_female_app);
        this.etFirstName = (EditText) findViewById(C0585R.id.first_name_app);
        this.etLastName = (EditText) findViewById(C0585R.id.last_name_app);
        this.etPhoneNo = (EditText) findViewById(C0585R.id.et_phone_app);
        this.etExperience = (EditText) findViewById(C0585R.id.et_experience_app);
        this.etEducation = (EditText) findViewById(C0585R.id.btnQualification);
        this.etCharge = (EditText) findViewById(C0585R.id.et_charge_app);
        this.etAddress = (EditText) findViewById(C0585R.id.address_app);
        this.etAddress1 = (EditText) findViewById(C0585R.id.address1_app);
        this.etCity = (EditText) findViewById(C0585R.id.city_app);
        this.etZipCode = (EditText) findViewById(C0585R.id.zip_code_app);
        this.etAbout = (EditText) findViewById(C0585R.id.et_about_app);
        this.btnSpeciality = (Button) findViewById(C0585R.id.btnSpeciality);
        this.spCountry = (AppCompatSpinner) findViewById(C0585R.id.sp_country_app);
        Button btn_next = (Button) findViewById(C0585R.id.btn_save_app);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.specialityId = (String) this.mSessionManager.getUser().get(SessionManager.SPECIALITY_ID);
        Glide.with(getApplicationContext()).load((String) this.mSessionManager.getUser().get(SessionManager.KEY_IMAGE)).error((int) C0585R.mipmap.ic_launcher).into(this.imageView);
        this.btnSpeciality.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.F_SPECIALITY));
        this.etEducation.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_EDUCATION));
        this.etExperience.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_EXPERIENCE));
        this.etEducation.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_EDUCATION));
        this.etCharge.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_LICENSE_NO));
        this.etFirstName.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_FIRST_NAME));
        this.etLastName.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_LAST_NAME));
        this.etPhoneNo.setText((CharSequence) this.mSessionManager.getUser().get("mobile"));
        this.etAddress.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ADDRESS));
        this.etAddress1.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ADDRESS1));
        this.etCity.setText((CharSequence) this.mSessionManager.getUser().get("city"));
        this.etZipCode.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_PIN_CODE));
        this.etAbout.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.ABOUT));
        if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_GENDER)).equals("male")) {
            this.radioMale.setChecked(true);
        } else {
            this.radioFemale.setChecked(true);
        }
        this.imageView.setOnClickListener(this);
        this.btnSpeciality.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        this.spCountry.setOnItemSelectedListener(new C06361());
        this.list = new ArrayList();
        this.adapter = new SpinnerAdapter(this, this.list);
        this.spCountry.setAdapter(this.adapter);
        getCountryCodes();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private boolean mayRequestExternalStorage() {
        if (VERSION.SDK_INT < 23) {
            showUploadImageDialog();
            return true;
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            showUploadImageDialog();
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                Builder builder = new Builder(this).setTitle((CharSequence) "Permission required!");
                builder.setPositiveButton(17039370, null);
                builder.setMessage((CharSequence) "Please confirm access to files");
                builder.setOnDismissListener(new C06372());
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1000);
            }
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            showUploadImageDialog();
        }
    }

    private void showUploadImageDialog() {
        final CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    TherapistProfileActivity.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TherapistProfileActivity.this.REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    TherapistProfileActivity.this.startActivityForResult(Intent.createChooser(intent, "Select File"), TherapistProfileActivity.this.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private String base64Convert(Bitmap picturePath) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        picturePath.compress(CompressFormat.JPEG, 90, bao);
        String ba1 = Base64.encodeBytes(bao.toByteArray());
        Log.i("base64", "-----" + ba1);
        return ba1;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == this.SELECT_FILE) {
            onSelectFromGalleryResult(data);
        } else if (requestCode == this.REQUEST_CAMERA) {
            onCaptureImageResult(data);
        } else if (requestCode == 0) {
            this.specialityId = data.getStringExtra("id");
            this.btnSpeciality.setText(data.getStringExtra(SdkConstants.DATA));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.imgUser:
                mayRequestExternalStorage();
                return;
            case C0585R.id.btnSpeciality:
                startActivityForResult(new Intent(this, SpecialityListActivity.class), 0);
                return;
            case C0585R.id.btn_save_app:
                checkFormData(view);
                return;
            default:
                return;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get(SdkConstants.DATA);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            destination.createNewFile();
            FileOutputStream fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imageView.setImageBitmap(thumbnail);
        this.base64image = base64Convert(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Cursor cursor = managedQuery(data.getData(), new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        int scale = 1;
        while ((options.outWidth / scale) / 2 >= Callback.DEFAULT_DRAG_ANIMATION_DURATION && (options.outHeight / scale) / 2 >= Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
        this.imageView.setImageBitmap(thumbnail);
        this.base64image = base64Convert(thumbnail);
    }

    void checkFormData(View view) {
        String gender;
        this.etFirstName.setError(null);
        this.etLastName.setError(null);
        this.etPhoneNo.setError(null);
        this.etExperience.setError(null);
        this.etEducation.setError(null);
        this.etCharge.setError(null);
        this.etAddress.setError(null);
        this.etAddress1.setError(null);
        this.etCity.setError(null);
        this.etZipCode.setError(null);
        this.etAbout.setError(null);
        String firstName = this.etFirstName.getText().toString();
        String lastName = this.etLastName.getText().toString();
        String experience = this.etExperience.getText().toString();
        String education = this.etEducation.getText().toString();
        String charge = this.etCharge.getText().toString();
        String address = this.etAddress.getText().toString();
        String address1 = this.etAddress1.getText().toString();
        String city = this.etCity.getText().toString();
        String zipCode = this.etZipCode.getText().toString();
        String phoneNo = this.etPhoneNo.getText().toString();
        String about = this.etAbout.getText().toString();
        boolean cancel = false;
        View focusView = null;
        int selectedId = this.radioGroup.getCheckedRadioButtonId();
        if (selectedId == this.radioMale.getId()) {
            gender = "male";
            this.mSessionManager.setGender(gender);
        } else if (selectedId == this.radioFemale.getId()) {
            gender = "female";
            this.mSessionManager.setGender(gender);
        } else {
            gender = "other";
            this.mSessionManager.setGender(gender);
        }
        if (TextUtils.isEmpty(experience)) {
            this.etExperience.setError(getString(C0585R.string.error_field_required));
            focusView = this.etExperience;
            cancel = true;
        }
        if (TextUtils.isEmpty(education)) {
            this.etEducation.setError(getString(C0585R.string.error_field_required));
            focusView = this.etEducation;
            cancel = true;
        }
        if (TextUtils.isEmpty(charge)) {
            this.etCharge.setError(getString(C0585R.string.error_field_required));
            focusView = this.etCharge;
            cancel = true;
        }
        if (TextUtils.isEmpty(address)) {
            this.etAddress.setError(getString(C0585R.string.error_field_required));
            focusView = this.etAddress;
            cancel = true;
        }
        if (TextUtils.isEmpty(city)) {
            this.etCity.setError(getString(C0585R.string.error_field_required));
            focusView = this.etCity;
            cancel = true;
        }
        if (this.spCountry.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select a Country", -1).show();
            focusView = this.etZipCode;
            cancel = true;
        }
        if (TextUtils.isEmpty(zipCode)) {
            this.etZipCode.setError(getString(C0585R.string.error_field_required));
            focusView = this.etZipCode;
            cancel = true;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            this.etPhoneNo.setError(getString(C0585R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(about)) {
            this.etAbout.setError(getString(C0585R.string.error_field_required));
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            updateProfile((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), firstName, lastName, phoneNo, gender, this.specialityId, experience, charge, address, address1, city, (String) this.mSessionManager.getUser().get("state"), zipCode, about, this.base64image);
        }
    }

    private void updateProfile(String userID, String firstName, String lastName, String mobile, String gender, String speciality, String experience, String charge, String addressLine, String addressLine2, String city, String state, String zipCode, String about, String pic) {
        GeneralSecurityException e;
        HttpStack stack;
        this.progressBar.setVisibility(0);
        String str = EndPoints.UPDATE_PROFILE_DR;
        final String str2 = firstName;
        final String str3 = lastName;
        final String str4 = addressLine2;
        final String str5 = about;
        C10534 c10534 = new Listener<String>() {
            public void onResponse(String response) {
                TherapistProfileActivity.this.progressBar.setVisibility(8);
                Log.e("Response", response);
                try {
                    JSONObject jSONObject = new JSONObject(response);
                    if (jSONObject.getString("status").equals("true")) {
                        JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                        String id = object.getString("dr_id");
                        String pic = object.getString("dr_pic");
                        String uType = object.getString(SessionManager.KEY_TYPE);
                        String email = object.getString("email");
                        String gender = object.getString(SessionManager.KEY_GENDER);
                        String speciality = object.getString("dr_speciality");
                        String rates = object.getString(SessionManager.KEY_RATES);
                        String licenseNo = object.getString("licences_no");
                        String education = object.getString(SessionManager.KEY_EDUCATION);
                        String mobile = object.getString("mobile");
                        String experience = object.getString(SessionManager.KEY_EXPERIENCE);
                        String address = object.getString(SessionManager.KEY_ADDRESS);
                        String city = object.getString("city");
                        String country = object.getString(SdkConstants.COUNTRY);
                        String zipCode = object.getString(SdkConstants.ZIPCODE);
                        String therapistType = object.getString(SessionManager.KEY_THERAPIST_TYPE);
                        TherapistProfileActivity.this.mSessionManager.setAvailability(object.getString("dr_availability"));
                        Toast.makeText(TherapistProfileActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
                        TherapistProfileActivity.this.mSessionManager.setCounselor(id, uType, str2, str3, email, gender, experience, TherapistProfileActivity.this.specialityId, speciality, rates, licenseNo, education, address, str4, city, country, zipCode, mobile, therapistType, pic, str5);
                        TherapistProfileActivity.this.finish();
                        return;
                    }
                    TherapistProfileActivity.this.progressBar.setVisibility(8);
                    Toast.makeText(TherapistProfileActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        final String str6 = userID;
        str = firstName;
        final String str7 = lastName;
        final String str8 = gender;
        final String str9 = speciality;
        final String str10 = experience;
        final String str11 = charge;
        final String str12 = addressLine;
        final String str13 = addressLine2;
        final String str14 = city;
        final String str15 = state;
        final String str16 = zipCode;
        final String str17 = mobile;
        final String str18 = about;
        final String str19 = pic;
        Request c11866 = new StringRequest(1, str, c10534, new C10545()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str6);
                params.put("dr_fname", str);
                params.put("dr_lname", str7);
                params.put(SessionManager.KEY_GENDER, str8);
                params.put("dr_speciality", str9);
                params.put(SessionManager.KEY_EXPERIENCE, str10);
                params.put(SessionManager.KEY_RATES, str11);
                params.put("dr_address", str12);
                params.put("apt_bldg", str13);
                params.put("dr_city", str14);
                params.put("dr_country", str15);
                params.put("dr_zipcode", str16);
                params.put("mobile", str17);
                params.put("about_me", str18);
                params.put("dr_pic", str19);
                Log.e("profile_params", params.toString());
                return params;
            }
        };
        c11866.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            HttpStack hurlStack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(c11866, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(c11866, stack);
        }
        Controller.getInstance().addToRequestQueue(c11866, stack);
    }

    private void getCountryCodes() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(0, EndPoints.COUNTRY_LIST, new C10557(), new C10568());
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
