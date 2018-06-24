package com.talktoangel.gts.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.talktoangel.gts.C0585R;

public class TestActivity extends AppCompatActivity implements OnClickListener {
    Button btnAnxiety;
    Button btnCareer;
    Button btnDepression;
    Button btnPersonality;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.btnCareer = (Button) findViewById(C0585R.id.btn_career);
        this.btnPersonality = (Button) findViewById(C0585R.id.btn_personality);
        this.btnAnxiety = (Button) findViewById(C0585R.id.btn_anxiety);
        this.btnDepression = (Button) findViewById(C0585R.id.btn_depression);
        this.btnCareer.setOnClickListener(this);
        this.btnPersonality.setOnClickListener(this);
        this.btnAnxiety.setOnClickListener(this);
        this.btnDepression.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btn_career:
                startActivity(new Intent(getApplicationContext(), CareerTestActivity.class));
                return;
            case C0585R.id.btn_personality:
                startActivity(new Intent(getApplicationContext(), PersonalityTestActivity.class));
                return;
            case C0585R.id.btn_anxiety:
                startActivity(new Intent(getApplicationContext(), AnxietyTestActivity.class));
                return;
            case C0585R.id.btn_depression:
                startActivity(new Intent(getApplicationContext(), DepressionTestActivity.class));
                return;
            default:
                return;
        }
    }
}
