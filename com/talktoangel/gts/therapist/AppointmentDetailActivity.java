package com.talktoangel.gts.therapist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sinch.android.rtc.SinchError;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.sinch.BaseActivity;
import com.talktoangel.gts.sinch.SinchService.StartFailedListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentDetailActivity extends BaseActivity implements OnClickListener, StartFailedListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_appointment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        final String apoId = bundle.getString("apoId");
        final String name = bundle.getString("name");
        final String image = bundle.getString("image");
        final String date = bundle.getString("date");
        final String time = bundle.getString("time");
        final String type = bundle.getString("type");
        ImageView view = (CircleImageView) findViewById(C0585R.id.img_pic_aad);
        TextView txtDate = (TextView) findViewById(C0585R.id.txt_date_aad);
        TextView txtTime = (TextView) findViewById(C0585R.id.txt_time_aad);
        Button btnCall = (Button) findViewById(C0585R.id.btn_call_aad);
        Button btnAccept = (Button) findViewById(C0585R.id.btn_accept_aad);
        Button btnCancel = (Button) findViewById(C0585R.id.btn_cancel_aad);
        Button btnReschedule = (Button) findViewById(C0585R.id.reschedule_aad);
        ((TextView) findViewById(C0585R.id.txt_name_aad)).setText(name);
        txtDate.setText(date);
        txtTime.setText(time);
        Glide.with(getApplicationContext()).load(image).error((int) C0585R.drawable.ic_user_black_24dp).into(view);
        btnCall.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnReschedule.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("apoId", apoId);
                bundle.putString("name", name);
                bundle.putString("image", image);
                bundle.putString("date", date);
                bundle.putString("time", time);
                bundle.putString("type", type);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void onStartFailed(SinchError error) {
    }

    public void onStarted() {
    }
}
