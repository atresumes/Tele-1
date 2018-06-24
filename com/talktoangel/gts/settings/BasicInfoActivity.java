package com.talktoangel.gts.settings;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.talktoangel.gts.userauth.OTPActivity;
import com.talktoangel.gts.utils.Base64;
import com.talktoangel.gts.utils.DatePickerFragment;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BasicInfoActivity extends AppCompatActivity {
    private static final int REQUEST_READ_STORAGE = 0;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private SpinnerAdapter adapter;
    private String base64image = "";
    private Button btnAnniversary;
    private EditText etAddress;
    private EditText etAddress1;
    private EditText etCity;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNo;
    private EditText etZipCode;
    private CircleImageView imageView;
    private ArrayList<CountryCode> list;
    private SessionManager mSessionManager;
    OnDateSetListener onDate = new OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            BasicInfoActivity.this.btnAnniversary.setHint(monthOfYear + "/" + dayOfMonth + "/" + year);
            BasicInfoActivity.this.btnAnniversary.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
        }
    };
    ProgressBar progressBar;
    RadioButton radioFemale;
    private RadioGroup radioGroup;
    RadioButton radioMale;
    RadioButton radioOther;
    private Spinner spCountry;
    private Spinner statusSpinner;

    class C06121 implements OnClickListener {
        C06121() {
        }

        public void onClick(View view) {
            BasicInfoActivity.this.mayRequestExternalStorage();
        }
    }

    class C06132 implements OnClickListener {
        C06132() {
        }

        public void onClick(View view) {
            BasicInfoActivity.this.checkFormData(view);
        }
    }

    class C06143 implements OnClickListener {
        C06143() {
        }

        public void onClick(View view) {
            BasicInfoActivity.this.showDatePicker();
        }
    }

    class C06154 implements OnItemSelectedListener {
        C06154() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Married")) {
                BasicInfoActivity.this.btnAnniversary.setEnabled(true);
            } else {
                BasicInfoActivity.this.btnAnniversary.setEnabled(false);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06165 implements OnItemSelectedListener {
        C06165() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            CountryCode code = (CountryCode) BasicInfoActivity.this.list.get(position);
            if (position != 0) {
                BasicInfoActivity.this.mSessionManager.setCountry(code.getName());
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06176 implements OnDismissListener {
        C06176() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialog) {
            BasicInfoActivity.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        }
    }

    class C10198 implements Listener<String> {
        C10198() {
        }

        public void onResponse(String response) {
            BasicInfoActivity.this.progressBar.setVisibility(8);
            Log.e("Response", response);
            try {
                JSONObject jSONObject = new JSONObject(response);
                if (jSONObject.getString("status").equals("true")) {
                    Toast.makeText(BasicInfoActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
                    JSONObject object = jSONObject.getJSONObject(SdkConstants.RESULT);
                    String freeSession = (String) BasicInfoActivity.this.mSessionManager.getUser().get(SessionManager.FREE_SESSION);
                    String id = object.getString("id");
                    String pic = object.getString("pic");
                    String uType = object.getString(SessionManager.KEY_TYPE);
                    String fName = object.getString(SessionManager.KEY_FIRST_NAME);
                    String lName = object.getString(SessionManager.KEY_LAST_NAME);
                    String email = object.getString("email");
                    String secQues = object.getString("security_ques");
                    String answer = object.getString("answer");
                    String gender = object.getString(SessionManager.KEY_GENDER);
                    String address = object.getString("add_line");
                    String address1 = object.getString("apt_bldg");
                    String city = object.getString("city");
                    String state = object.getString("state");
                    String zipCode = object.getString(SdkConstants.ZIPCODE);
                    String countryCode = object.getString(SessionManager.COUNTRY_CODE);
                    String currencyP = object.getString(SessionManager.KEY_CURRENCY_PREF);
                    String mobile = object.getString("mobile");
                    BasicInfoActivity.this.mSessionManager.setMaritalStatus(object.getString(SessionManager.KEY_MARITAL_STATUS));
                    BasicInfoActivity.this.mSessionManager.setAnniversary(object.getString(SessionManager.KEY_ANNIVERSARY));
                    BasicInfoActivity.this.mSessionManager.setUser(freeSession, id, uType, fName, lName, email, secQues, answer, gender, address, address1, city, state, zipCode, countryCode, currencyP, mobile, pic);
                    if (!BasicInfoActivity.this.etPhoneNo.getText().toString().equals(BasicInfoActivity.this.mSessionManager.getUser().get("mobile"))) {
                        BasicInfoActivity.this.startActivity(new Intent(BasicInfoActivity.this.getApplicationContext(), OTPActivity.class).putExtra("update", true));
                    }
                    BasicInfoActivity.this.finish();
                    return;
                }
                BasicInfoActivity.this.progressBar.setVisibility(8);
                Toast.makeText(BasicInfoActivity.this.getApplicationContext(), jSONObject.getString("message"), 1).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(BasicInfoActivity.this.getApplicationContext(), "Json error" + e.getMessage(), 1).show();
            }
        }
    }

    class C10209 implements ErrorListener {
        C10209() {
        }

        public void onErrorResponse(VolleyError error) {
            BasicInfoActivity.this.progressBar.setVisibility(8);
            Log.e("Error", "" + error.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_basic_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_bi);
        this.imageView = (CircleImageView) findViewById(C0585R.id.imgUser);
        this.radioGroup = (RadioGroup) findViewById(C0585R.id.gender_radio_bi);
        this.radioMale = (RadioButton) findViewById(C0585R.id.radio_male_bi);
        this.radioFemale = (RadioButton) findViewById(C0585R.id.radio_female_bi);
        this.radioOther = (RadioButton) findViewById(C0585R.id.radio_other_bi);
        this.etFirstName = (EditText) findViewById(C0585R.id.first_name_bi);
        this.etLastName = (EditText) findViewById(C0585R.id.last_name_bi);
        this.etPhoneNo = (EditText) findViewById(C0585R.id.et_phone_bi);
        this.etAddress = (EditText) findViewById(C0585R.id.address_bi);
        this.etAddress1 = (EditText) findViewById(C0585R.id.address1_bi);
        this.etCity = (EditText) findViewById(C0585R.id.city_bi);
        this.etZipCode = (EditText) findViewById(C0585R.id.zip_code_bi);
        this.statusSpinner = (Spinner) findViewById(C0585R.id.spRelationStatus);
        this.spCountry = (Spinner) findViewById(C0585R.id.sp_country_bi);
        this.btnAnniversary = (Button) findViewById(C0585R.id.btnAnniversary);
        Button btn_next = (Button) findViewById(C0585R.id.btn_save_bi);
        this.mSessionManager = new SessionManager(getApplicationContext());
        Glide.with(getApplicationContext()).load((String) this.mSessionManager.getUser().get(SessionManager.KEY_IMAGE)).error((int) C0585R.drawable.ic_user_black_24dp).into(this.imageView);
        this.etFirstName.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_FIRST_NAME));
        this.etLastName.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_LAST_NAME));
        this.etPhoneNo.setText((CharSequence) this.mSessionManager.getUser().get("mobile"));
        this.etAddress.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ADDRESS));
        this.etAddress1.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ADDRESS1));
        this.etCity.setText((CharSequence) this.mSessionManager.getUser().get("city"));
        this.etZipCode.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_PIN_CODE));
        if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_GENDER)).equals("male")) {
            this.radioMale.setChecked(true);
        } else if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_GENDER)).equals("female")) {
            this.radioFemale.setChecked(true);
        } else {
            this.radioOther.setChecked(true);
        }
        List<String> stringList = Arrays.asList(getResources().getStringArray(C0585R.array.relation_status_array));
        for (int i = 0; i < stringList.size(); i++) {
            if (((String) stringList.get(i)).equalsIgnoreCase((String) this.mSessionManager.getUser().get(SessionManager.KEY_MARITAL_STATUS))) {
                this.statusSpinner.setSelection(i);
            }
        }
        if (this.mSessionManager.getUser().get(SessionManager.KEY_ANNIVERSARY) != null) {
            this.btnAnniversary.setHint((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ANNIVERSARY));
            this.btnAnniversary.setText((CharSequence) this.mSessionManager.getUser().get(SessionManager.KEY_ANNIVERSARY));
            this.btnAnniversary.setEnabled(true);
        }
        this.imageView.setOnClickListener(new C06121());
        btn_next.setOnClickListener(new C06132());
        this.btnAnniversary.setOnClickListener(new C06143());
        this.statusSpinner.setOnItemSelectedListener(new C06154());
        this.spCountry.setOnItemSelectedListener(new C06165());
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
                builder.setOnDismissListener(new C06176());
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
            }
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
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
                    BasicInfoActivity.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), BasicInfoActivity.this.REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    BasicInfoActivity.this.startActivityForResult(Intent.createChooser(intent, "Select File"), BasicInfoActivity.this.SELECT_FILE);
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
        this.etAddress.setError(null);
        this.etAddress1.setError(null);
        this.etCity.setError(null);
        this.etZipCode.setError(null);
        this.etPhoneNo.setError(null);
        String address = this.etAddress.getText().toString();
        String address1 = this.etAddress1.getText().toString();
        String city = this.etCity.getText().toString();
        String zipCode = this.etZipCode.getText().toString();
        String phoneNo = this.etPhoneNo.getText().toString();
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
        if (TextUtils.isEmpty(address)) {
            this.etAddress.setError(getString(C0585R.string.error_field_required));
            focusView = this.etAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(city)) {
            this.etCity.setError(getString(C0585R.string.error_field_required));
            focusView = this.etCity;
            cancel = true;
        } else if (this.spCountry.getSelectedItemPosition() == 0) {
            Snackbar.make(view, (CharSequence) "Please select a Country", -1).show();
            focusView = this.etZipCode;
            cancel = true;
        } else if (TextUtils.isEmpty(zipCode)) {
            this.etZipCode.setError(getString(C0585R.string.error_field_required));
            focusView = this.etZipCode;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNo)) {
            this.etPhoneNo.setError(getString(C0585R.string.error_field_required));
            cancel = true;
        }
        if (this.statusSpinner.getSelectedItem().toString().equalsIgnoreCase("Married") && this.btnAnniversary.getHint().toString().equals(getResources().getString(C0585R.string.date_format))) {
            Snackbar.make(view, (CharSequence) "Select your anniversary date", -1).show();
            focusView = this.btnAnniversary;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            updateUserDetails((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), gender, this.statusSpinner.getSelectedItem().toString(), this.btnAnniversary.getText().toString(), address, address1, city, (String) this.mSessionManager.getUser().get("state"), zipCode, phoneNo, this.base64image);
        }
    }

    private void updateUserDetails(String userID, String gender, String maritalStatus, String anniversary, String addressLine, String addressLine2, String city, String state, String zipCode, String mobile, String pic) {
        GeneralSecurityException e;
        HttpStack stack;
        this.progressBar.setVisibility(0);
        final String str = userID;
        final String str2 = gender;
        final String str3 = maritalStatus;
        final String str4 = anniversary;
        final String str5 = addressLine;
        final String str6 = addressLine2;
        final String str7 = city;
        final String str8 = state;
        final String str9 = zipCode;
        final String str10 = mobile;
        final String str11 = pic;
        Request strReq = new StringRequest(1, EndPoints.UPDATE_PROFILE, new C10198(), new C10209()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", str);
                params.put(SessionManager.KEY_GENDER, str2);
                params.put(SessionManager.KEY_MARITAL_STATUS, str3);
                params.put(SessionManager.KEY_ANNIVERSARY, str4);
                params.put("add_line", str5);
                params.put("apt_bldg", str6);
                params.put("city", str7);
                params.put("state", str8);
                params.put(SdkConstants.ZIPCODE, str9);
                params.put("mobile", str10);
                params.put("pic", str11);
                Log.e("profile_params", params.toString());
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

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(1));
        args.putInt("month", calender.get(2));
        args.putInt("day", calender.get(5));
        args.putInt("minMax", 2);
        date.setArguments(args);
        date.setCallBack(this.onDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    private void getCountryCodes() {
        HttpStack stack;
        GeneralSecurityException e;
        Request strReq = new StringRequest(0, EndPoints.COUNTRY_LIST, new Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    int i;
                    JSONArray array = new JSONObject(response).getJSONArray(SdkConstants.RESULT);
                    BasicInfoActivity.this.list.add(new CountryCode("0", "Select Country", "--", SdkConstants.NULL_STRING));
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        String id = object1.getString("cntry_id");
                        String name = object1.getString(SdkConstants.COUNTRY);
                        String flag = object1.getString("flag");
                        BasicInfoActivity.this.list.add(new CountryCode(id, name, object1.getString("phonecode"), flag));
                    }
                    BasicInfoActivity.this.adapter.notifyDataSetChanged();
                    for (i = 0; i < BasicInfoActivity.this.list.size(); i++) {
                        if (((CountryCode) BasicInfoActivity.this.list.get(i)).getName().equalsIgnoreCase((String) BasicInfoActivity.this.mSessionManager.getUser().get("state"))) {
                            BasicInfoActivity.this.spCountry.setSelection(i);
                        }
                    }
                    Log.e(SdkConstants.DATA, (String) BasicInfoActivity.this.mSessionManager.getUser().get("state"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BasicInfoActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "" + error.getMessage());
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
}
