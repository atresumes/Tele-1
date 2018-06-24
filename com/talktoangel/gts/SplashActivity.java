package com.talktoangel.gts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.talktoangel.gts.userauth.LoginActivity;
import com.talktoangel.gts.userauth.Registration2Activity;
import com.talktoangel.gts.userauth.TherapistRegistration2Activity;
import com.talktoangel.gts.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_splash);
        this.session = new SessionManager(getApplicationContext());
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (this.session.isMobileVerified()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (((String) this.session.getUser().get(SessionManager.KEY_TYPE)).equals("p")) {
            startActivity(new Intent(this, Registration2Activity.class));
            finish();
        } else {
            startActivity(new Intent(this, TherapistRegistration2Activity.class));
            finish();
        }
    }
}
