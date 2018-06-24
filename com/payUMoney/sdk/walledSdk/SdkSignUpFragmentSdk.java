package com.payUMoney.sdk.walledSdk;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashSet;

public class SdkSignUpFragmentSdk extends SdkBaseFragment {
    public static final String TAG = ("---viswash---" + SdkSignUpFragmentSdk.class.getSimpleName());
    private Button cancelButton;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPhone;
    private WalletSdkLoginSignUpActivity mSdkLoginSignUpActivity;
    private Button mSignUp;

    class C04231 implements OnTouchListener {
        C04231() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkSignUpFragmentSdk.this.mEmail.showDropDown();
            }
            return false;
        }
    }

    class C04242 implements OnClickListener {
        C04242() {
        }

        public void onClick(View view) {
            if (SdkSignUpFragmentSdk.this.mEmail == null || SdkSignUpFragmentSdk.this.mEmail.getText() == null || SdkSignUpFragmentSdk.this.mEmail.getText().toString().trim().isEmpty()) {
                SdkSignUpFragmentSdk.this.onValidationFailed(SdkSignUpFragmentSdk.this.mEmail, "Your Email is Required");
            } else if (SdkSignUpFragmentSdk.this.mEmail != null && !SdkConstants.EMAIL_PATTERN.matcher(SdkSignUpFragmentSdk.this.mEmail.getText().toString()).matches()) {
                SdkSignUpFragmentSdk.this.onValidationFailed(SdkSignUpFragmentSdk.this.mEmail, "This Email appears to be Invalid");
            } else if (SdkSignUpFragmentSdk.this.mPhone == null || SdkSignUpFragmentSdk.this.mPhone.getText() == null || SdkSignUpFragmentSdk.this.mPhone.getText().toString().trim().isEmpty()) {
                SdkSignUpFragmentSdk.this.onValidationFailed(SdkSignUpFragmentSdk.this.mEmail, "Please enter your Phone Number");
            } else if (SdkSignUpFragmentSdk.this.mPhone != null && !SdkConstants.PHONE_PATTERN.matcher(SdkSignUpFragmentSdk.this.mPhone.getText().toString()).matches()) {
                SdkSignUpFragmentSdk.this.onValidationFailed(SdkSignUpFragmentSdk.this.mPhone, "This Phone Number appears to be Invalid");
            } else if (SdkHelper.checkNetwork(SdkSignUpFragmentSdk.this.mSdkLoginSignUpActivity)) {
                SdkSignUpFragmentSdk.this.onValidationSucceeded();
            } else {
                SdkHelper.showToastMessage(SdkSignUpFragmentSdk.this.mSdkLoginSignUpActivity, SdkSignUpFragmentSdk.this.getString(C0360R.string.disconnected_from_internet), true);
            }
        }
    }

    class C04253 implements OnClickListener {
        C04253() {
        }

        public void onClick(View arg0) {
            if (SdkHelper.isValidClick()) {
                WalletSdkLoginSignUpActivity access$200 = SdkSignUpFragmentSdk.this.mSdkLoginSignUpActivity;
                SdkSignUpFragmentSdk.this.mSdkLoginSignUpActivity.getClass();
                access$200.close(4, null);
            }
        }
    }

    class C04264 implements Runnable {
        C04264() {
        }

        public void run() {
            SdkSignUpFragmentSdk.this.setResetButton(true);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0360R.layout.walletasdk_activity_signup, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSdkLoginSignUpActivity = (WalletSdkLoginSignUpActivity) getActivity();
        this.mEmail = (AutoCompleteTextView) view.findViewById(C0360R.id.email);
        this.mEmail.setAdapter(new ArrayAdapter(this.mSdkLoginSignUpActivity, 17367050, new ArrayList(new HashSet())));
        this.mEmail.setOnTouchListener(new C04231());
        this.mPhone = (AutoCompleteTextView) view.findViewById(C0360R.id.phone_number);
        this.mSignUp = (Button) view.findViewById(C0360R.id.done);
        this.mSignUp.setOnClickListener(new C04242());
        this.cancelButton = (Button) view.findViewById(C0360R.id.cancel_button);
        this.cancelButton.setOnClickListener(new C04253());
        ((TextView) view.findViewById(C0360R.id.tos_n_privacy)).setMovementMethod(LinkMovementMethod.getInstance());
        this.mSdkLoginSignUpActivity.showHideLogoutButton(false);
        this.mSdkLoginSignUpActivity.setTabNewTitle(getString(C0360R.string.walletsdk_sign_up));
        this.mSdkLoginSignUpActivity.setTabVisibility();
        this.mSdkLoginSignUpActivity.invalidateActivityOptionsMenu();
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mSdkLoginSignUpActivity != null) {
            this.mSdkLoginSignUpActivity.setTabVisibility();
        }
    }

    public void onPause() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() != 48) {
            return;
        }
        if (event.getStatus()) {
            if (!(event == null || event.getValue() == null || !event.getValue().toString().equals(SdkConstants.CUSTOMER_REGISTERED))) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, "OTP sent to Phone Number " + SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "phone"), false);
            }
            Bundle bundle = new Bundle();
            bundle.putString("email", this.mEmail.getText().toString());
            bundle.putString("phone", this.mPhone.getText().toString());
            FragmentTransaction fragmentTran = this.mSdkLoginSignUpActivity.getSupportFragmentManager().beginTransaction();
            fragmentTran.setCustomAnimations(C0360R.anim.enter, C0360R.anim.exit, C0360R.anim.pop_enter, C0360R.anim.pop_exit);
            SdkSignUpLoginOTPVerifyFragmentSdk homeFragmentMoreOptions = new SdkSignUpLoginOTPVerifyFragmentSdk();
            homeFragmentMoreOptions.setArguments(bundle);
            fragmentTran.replace(C0360R.id.login_signup_fragment_container, homeFragmentMoreOptions, SdkSignUpLoginOTPVerifyFragmentSdk.class.getName());
            fragmentTran.addToBackStack(SdkSignUpLoginOTPVerifyFragmentSdk.class.getName());
            fragmentTran.commitAllowingStateLoss();
            new Handler().postDelayed(new C04264(), 1000);
            return;
        }
        if (event.getValue() != null) {
            SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, event.getValue().toString(), true);
        } else {
            SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
        }
        setResetButton(true);
    }

    void setResetButton(boolean visibility) {
        this.mSignUp.setText(visibility ? C0360R.string.continue_string : C0360R.string.please_wait);
        this.mSignUp.setEnabled(visibility);
        this.cancelButton.setEnabled(visibility);
    }

    public void onValidationSucceeded() {
        if (this.mPhone.getText().toString().charAt(0) < '6') {
            SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, "Please Enter Valid Phone Number.", true);
            this.mPhone.requestFocus();
        } else if (SdkHelper.checkNetwork(this.mSdkLoginSignUpActivity)) {
            SdkSession.getInstance(this.mSdkLoginSignUpActivity).sendMobileVerificationCode(this.mEmail.getText().toString(), this.mPhone.getText().toString());
            setResetButton(false);
        } else {
            SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.disconnected_from_internet), true);
            this.mSignUp.requestFocus();
        }
    }

    public void onValidationFailed(View view, String msg) {
        SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, msg, true);
        view.requestFocus();
    }
}
