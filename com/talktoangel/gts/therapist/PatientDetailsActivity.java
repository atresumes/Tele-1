package com.talktoangel.gts.therapist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.utils.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDetailsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0585R.layout.activity_patient_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        bundle.getString("id");
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String dob = bundle.getString("dob");
        String gender = bundle.getString(SessionManager.KEY_GENDER);
        String status = bundle.getString("status");
        String address = bundle.getString(SessionManager.KEY_ADDRESS);
        String mobile = bundle.getString("mobile");
        String pic = bundle.getString("image");
        ImageView view = (CircleImageView) findViewById(C0585R.id.img_apd);
        TextView txtEmail = (TextView) findViewById(C0585R.id.txt_email_apd);
        TextView txtDob = (TextView) findViewById(C0585R.id.txt_dob_apd);
        TextView txtGender = (TextView) findViewById(C0585R.id.txt_gender_apd);
        TextView txtStatus = (TextView) findViewById(C0585R.id.txt_status_apd);
        TextView txtMobile = (TextView) findViewById(C0585R.id.txt_mobile_apd);
        TextView txtAddress = (TextView) findViewById(C0585R.id.txt_address_apd);
        ((TextView) findViewById(C0585R.id.txt_name_apd)).setText(name);
        txtEmail.setText(email);
        txtDob.setText(dob);
        txtGender.setText(gender);
        txtStatus.setText(status);
        txtMobile.setText(mobile);
        txtAddress.setText(address);
        Glide.with(getApplicationContext()).load(pic).error(getResources().getDrawable(C0585R.drawable.ic_user_black_24dp)).into(view);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}
