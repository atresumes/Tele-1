package com.talktoangel.gts;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.sinch.android.rtc.SinchError;
import com.talktoangel.gts.fragment.AboutusFragment;
import com.talktoangel.gts.fragment.AccountAndSettingsFragment;
import com.talktoangel.gts.fragment.AppointmentsFragment;
import com.talktoangel.gts.fragment.DashboardUserFragment;
import com.talktoangel.gts.fragment.MessagesFragment;
import com.talktoangel.gts.fragment.ScheduleFragment;
import com.talktoangel.gts.fragment.TherapistListFragment;
import com.talktoangel.gts.sinch.BaseActivity;
import com.talktoangel.gts.sinch.SinchService.StartFailedListener;
import com.talktoangel.gts.test.TestActivity;
import com.talktoangel.gts.therapist.TherapistDashboardFragment;
import com.talktoangel.gts.utils.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements OnNavigationItemSelectedListener, StartFailedListener {
    private static final int REQUEST_PERMISSIONS = 0;
    boolean doubleBackToExitPressedOnce = false;
    private View headerLayout;
    private SessionManager session;

    class C05801 implements Runnable {
        C05801() {
        }

        public void run() {
            MainActivity.this.doubleBackToExitPressedOnce = false;
        }
    }

    class C05812 implements OnDismissListener {
        C05812() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialog) {
            MainActivity.this.requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE"}, 0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Fragment fragment;
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(C0585R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(C0585R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, C0585R.string.navigation_drawer_open, C0585R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(C0585R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.headerLayout = navigationView.getHeaderView(0);
        this.session = new SessionManager(getApplicationContext());
        if (((String) this.session.getUser().get(SessionManager.KEY_TYPE)).equals("p")) {
            fragment = new DashboardUserFragment();
        } else {
            navigationView.getMenu().findItem(C0585R.id.nav_schedule).setVisible(false);
            navigationView.getMenu().findItem(C0585R.id.nav_providers).setVisible(false);
            navigationView.getMenu().findItem(C0585R.id.nav_test).setVisible(false);
            fragment = new TherapistDashboardFragment();
        }
        getSupportFragmentManager().beginTransaction().add((int) C0585R.id.container, fragment).commit();
        mayRequestPermissions();
    }

    protected void onResume() {
        super.onResume();
        CircleImageView imageView = (CircleImageView) this.headerLayout.findViewById(C0585R.id.imageView);
        ((TextView) this.headerLayout.findViewById(C0585R.id.textView)).setText(((String) this.session.getUser().get(SessionManager.KEY_FIRST_NAME)) + " " + ((String) this.session.getUser().get(SessionManager.KEY_LAST_NAME)));
        Glide.with(getApplicationContext()).load((String) this.session.getUser().get(SessionManager.KEY_IMAGE)).error((int) C0585R.drawable.ic_user_white_24dp).into(imageView);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(C0585R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", 0).show();
            new Handler().postDelayed(new C05801(), 2000);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        if (id == C0585R.id.nav_dashboard) {
            getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_dashboard));
            if (((String) this.session.getUser().get(SessionManager.KEY_TYPE)).equals("p")) {
                fragment = new DashboardUserFragment();
            } else {
                fragment = new TherapistDashboardFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, fragment).commit();
        } else if (id == C0585R.id.nav_appointment) {
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new AppointmentsFragment()).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_schedule) {
            fragment = new ScheduleFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", DashboardUserFragment.list);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, fragment).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_messages) {
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new MessagesFragment()).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_providers) {
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new TherapistListFragment()).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_test) {
            startActivity(new Intent(getApplicationContext(), TestActivity.class));
        } else if (id == C0585R.id.nav_support) {
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new AboutusFragment()).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_account_settings) {
            getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new AccountAndSettingsFragment()).addToBackStack(null).commit();
        } else if (id == C0585R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction("android.intent.action.SEND");
            sendIntent.putExtra("android.intent.extra.TEXT", "Get Therapist at your place Download \n Talk To Angel \n https://play.google.com/apps/com.talktoangel.gts");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == C0585R.id.nav_logout) {
            getSinchServiceInterface().stopClient();
            this.session.logoutUser();
            finish();
            return true;
        }
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient((String) this.session.getUser().get("mobile"));
        }
        item.setChecked(true);
        ((DrawerLayout) findViewById(C0585R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    public void onStartFailed(SinchError error) {
        Log.e("MainActivity", error.getErrorType() + "");
    }

    public void onStarted() {
        Log.e("MainActivity", "started");
        getSinchServiceInterface().startClient((String) this.session.getUser().get("mobile"));
    }

    private boolean mayRequestPermissions() {
        if (VERSION.SDK_INT < 23 || (((((((checkSelfPermission("android.permission.READ_CONTACTS") + checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE")) + checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")) + checkSelfPermission("android.permission.RECORD_AUDIO")) + checkSelfPermission("android.permission.CAMERA")) + checkSelfPermission("android.permission.RECEIVE_SMS")) + checkSelfPermission("android.permission.SEND_SMS")) + checkSelfPermission("android.permission.READ_PHONE_STATE")) + checkSelfPermission("android.permission.CALL_PHONE") == 0) {
            return true;
        }
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
            Builder builder = new Builder(this).setTitle((int) C0585R.string.permission_rationale);
            builder.setPositiveButton(17039370, null);
            builder.setMessage((CharSequence) "Please confirm access to files & folders");
            builder.setOnDismissListener(new C05812());
            builder.show();
        } else {
            requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE"}, 0);
        }
        return false;
    }
}
