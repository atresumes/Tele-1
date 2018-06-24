package com.talktoangel.gts.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.utils.SessionManager;

public class AnxietyTestActivity extends AppCompatActivity implements OnClickListener {
    SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_anxiety_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Button) findViewById(C0585R.id.btnLogin)).setOnClickListener(this);
        this.session = new SessionManager(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btnLogin:
                this.session.setTestData("anxiety_depression_test", "1000");
                startActivity(new Intent(this, TestLoginActivity.class));
                return;
            default:
                return;
        }
    }
}
