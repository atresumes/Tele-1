package com.payUMoney.sdk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.payUMoney.sdk.fragment.SdkForgotPasswordFragment;
import com.payUMoney.sdk.fragment.SdkLoginFragment;
import com.payUMoney.sdk.fragment.SdkSignUpFragment;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils;

public class SdkLoginSignUpActivity extends FragmentActivity {
    public final int RESULT_BACK = 8;

    class C03751 implements OnClickListener {
        C03751() {
        }

        public void onClick(View v) {
            SdkSession.getInstance(SdkLoginSignUpActivity.this).cancelPendingRequests(SdkSession.TAG);
            SdkLoginSignUpActivity.this.loadLoginFragment(true);
        }
    }

    class C03762 implements OnClickListener {
        C03762() {
        }

        public void onClick(View v) {
            SdkSession.getInstance(SdkLoginSignUpActivity.this).cancelPendingRequests(SdkSession.TAG);
            SdkLoginSignUpActivity.this.findViewById(C0360R.id.sign_up_tab).setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            SdkLoginSignUpActivity.this.findViewById(C0360R.id.login_tab).setAlpha(0.2f);
            if (((TextView) SdkLoginSignUpActivity.this.findViewById(C0360R.id.sign_up_tab)).getCurrentTextColor() != SdkLoginSignUpActivity.this.getResources().getColor(17170443)) {
                SdkLoginSignUpActivity.this.getSupportFragmentManager().popBackStack(null, 1);
                FragmentTransaction fragmentTransaction = SdkLoginSignUpActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(C0360R.anim.enter, C0360R.anim.exit, C0360R.anim.pop_enter, C0360R.anim.pop_exit);
                fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkSignUpFragment(), SdkSignUpFragment.class.getName());
                fragmentTransaction.commitAllowingStateLoss();
                ((TextView) SdkLoginSignUpActivity.this.findViewById(C0360R.id.login_tab)).setTextColor(SdkLoginSignUpActivity.this.getResources().getColor(17170444));
                ((TextView) SdkLoginSignUpActivity.this.findViewById(C0360R.id.sign_up_tab)).setTextColor(SdkLoginSignUpActivity.this.getResources().getColor(17170443));
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String allowGuestCheckoutValue = getIntent().getStringExtra(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_VALUE);
        setContentView(C0360R.layout.sdk_activity_login_sign_up);
        setTitle(C0360R.string.app_name);
        ((TextView) findViewById(C0360R.id.login_tab)).setAllCaps(true);
        ((TextView) findViewById(C0360R.id.sign_up_tab)).setAllCaps(true);
        if (allowGuestCheckoutValue != null && allowGuestCheckoutValue.equals(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_ONLY)) {
            findViewById(C0360R.id.sign_up_tab).setVisibility(8);
            findViewById(C0360R.id.titleDivider).setVisibility(8);
        }
        findViewById(C0360R.id.login_tab).setOnClickListener(new C03751());
        findViewById(C0360R.id.sign_up_tab).setOnClickListener(new C03762());
        loadLoginFragment(false);
    }

    private void loadLoginFragment(boolean animate) {
        findViewById(C0360R.id.sign_up_tab).setAlpha(0.2f);
        findViewById(C0360R.id.login_tab).setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        if (((TextView) findViewById(C0360R.id.login_tab)).getCurrentTextColor() != getResources().getColor(17170443)) {
            getSupportFragmentManager().popBackStack(null, 1);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (animate) {
                fragmentTransaction.setCustomAnimations(C0360R.anim.pop_enter, C0360R.anim.pop_exit, C0360R.anim.enter, C0360R.anim.exit);
            }
            fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkLoginFragment(), SdkLoginFragment.class.getName());
            fragmentTransaction.commitAllowingStateLoss();
            ((TextView) findViewById(C0360R.id.login_tab)).setTextColor(getResources().getColor(17170443));
            ((TextView) findViewById(C0360R.id.sign_up_tab)).setTextColor(getResources().getColor(17170444));
        }
    }

    public void loadForgotPasswordFragment(boolean animate) {
        findViewById(C0360R.id.login_tab).setAlpha(0.2f);
        findViewById(C0360R.id.sign_up_tab).setAlpha(0.2f);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(C0360R.anim.pop_enter, C0360R.anim.pop_exit, C0360R.anim.enter, C0360R.anim.exit);
        fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkForgotPasswordFragment(), SdkForgotPasswordFragment.class.getName());
        fragmentTransaction.addToBackStack(SdkForgotPasswordFragment.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
        ((TextView) findViewById(C0360R.id.login_tab)).setTextColor(getResources().getColor(17170444));
        ((TextView) findViewById(C0360R.id.sign_up_tab)).setTextColor(getResources().getColor(17170444));
    }

    public void close() {
        setResult(-1);
        finish();
    }

    public void onResume() {
        super.onResume();
        SharedPrefsUtils.setStringPreference(this, SdkConstants.USER_SESSION_COOKIE_PAGE_URL, getClass().getSimpleName());
    }
}
