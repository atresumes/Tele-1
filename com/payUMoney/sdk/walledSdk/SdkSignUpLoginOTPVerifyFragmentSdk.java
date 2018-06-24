package com.payUMoney.sdk.walledSdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils.Keys;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SdkSignUpLoginOTPVerifyFragmentSdk extends SdkBaseFragment {
    private Button cancelButton;
    private String email;
    private RelativeLayout humble;
    private TextView info;
    WalletSdkLoginSignUpActivity mSdkLoginSignUpActivity;
    private EditText mobile;
    private String number = null;
    public EditText otp;
    public Pattern f1096p = Pattern.compile("(|^)\\d{6}");
    private ProgressBar pbwaitotp;
    private Button proceed;
    private BroadcastReceiver receiver;
    public TextView resend;
    private HashMap<String, String> userParams;

    class C04271 implements OnClickListener {
        C04271() {
        }

        public void onClick(View arg0) {
            if (SdkHelper.isValidClick()) {
                WalletSdkLoginSignUpActivity walletSdkLoginSignUpActivity = SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity;
                SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity.getClass();
                walletSdkLoginSignUpActivity.close(4, null);
            }
        }
    }

    class C04282 implements OnClickListener {
        C04282() {
        }

        public void onClick(View v) {
            if (!SdkHelper.checkNetwork(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity)) {
                SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity, SdkSignUpLoginOTPVerifyFragmentSdk.this.getString(C0360R.string.disconnected_from_internet), true);
            } else if (SdkHelper.isValidClick()) {
                SdkSignUpLoginOTPVerifyFragmentSdk.this.setResetOtpButton(false);
                SdkSession.getInstance(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity).sendMobileVerificationCode(SharedPrefsUtils.getStringPreference(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity, "email"), SharedPrefsUtils.getStringPreference(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity, "phone"));
            }
        }
    }

    class C04293 implements OnClickListener {
        C04293() {
        }

        public void onClick(View v) {
            if (!SdkHelper.checkNetwork(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity)) {
                SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity, SdkSignUpLoginOTPVerifyFragmentSdk.this.getString(C0360R.string.disconnected_from_internet), true);
            } else if (SdkSignUpLoginOTPVerifyFragmentSdk.this.proceed.getText().equals(SdkSignUpLoginOTPVerifyFragmentSdk.this.getString(C0360R.string.activate)) && !SdkSignUpLoginOTPVerifyFragmentSdk.this.otp.getText().toString().equals("")) {
                SdkSignUpLoginOTPVerifyFragmentSdk.this.setResetViews(false);
                ((Button) v).setEnabled(false);
                SdkSession.getInstance(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity).verifyUserCredential(SdkSignUpLoginOTPVerifyFragmentSdk.this.email, SdkSignUpLoginOTPVerifyFragmentSdk.this.number, SdkSignUpLoginOTPVerifyFragmentSdk.this.otp.getText().toString());
            } else if (SdkSignUpLoginOTPVerifyFragmentSdk.this.otp.getText().equals("")) {
                SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.mSdkLoginSignUpActivity, SdkSignUpLoginOTPVerifyFragmentSdk.this.getString(C0360R.string.waiting_for_otp), true);
            }
        }
    }

    class C04304 implements TextWatcher {
        C04304() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() == 6) {
                SdkSignUpLoginOTPVerifyFragmentSdk.this.pbwaitotp.setVisibility(8);
                SdkSignUpLoginOTPVerifyFragmentSdk.this.info.setText("Verify OTP");
                SdkSignUpLoginOTPVerifyFragmentSdk.this.proceed.setEnabled(true);
                return;
            }
            SdkSignUpLoginOTPVerifyFragmentSdk.this.proceed.setEnabled(false);
            SdkSignUpLoginOTPVerifyFragmentSdk.this.pbwaitotp.setVisibility(0);
            SdkSignUpLoginOTPVerifyFragmentSdk.this.info.setText(SdkSignUpLoginOTPVerifyFragmentSdk.this.getString(C0360R.string.waiting_for_otp));
        }
    }

    class C04315 implements Runnable {
        C04315() {
        }

        public void run() {
            SdkSignUpLoginOTPVerifyFragmentSdk.this.setResetOtpButton(true);
        }
    }

    class C04326 extends BroadcastReceiver {
        C04326() {
        }

        public void onReceive(Context context, Intent intent) {
            String msgBody = null;
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle == null || SdkSignUpLoginOTPVerifyFragmentSdk.this.otp == null) {
                    SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.getActivity(), "Couldn't read sms, please enter OTP manually", true);
                    return;
                }
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }
                    if (msgBody != null && msgBody.toLowerCase().contains("verification")) {
                        Matcher m = SdkSignUpLoginOTPVerifyFragmentSdk.this.f1096p.matcher(msgBody);
                        if (m.find()) {
                            SdkSignUpLoginOTPVerifyFragmentSdk.this.otp.setText(m.group(0));
                        } else {
                            SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.getActivity(), "Couldn't read sms, please enter OTP manually", true);
                        }
                    }
                } catch (Exception e) {
                    SdkHelper.showToastMessage(SdkSignUpLoginOTPVerifyFragmentSdk.this.getActivity(), "Couldn't read sms, please enter OTP manually", true);
                }
            }
        }
    }

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View walletPayment = inflater.inflate(C0360R.layout.walletsdk_fragment_otpverify_signup_login, container, false);
        this.mSdkLoginSignUpActivity = (WalletSdkLoginSignUpActivity) getActivity();
        this.userParams = this.mSdkLoginSignUpActivity.getMapObject();
        IllegalStateException illegalStateException;
        if (getArguments() != null) {
            this.number = getArguments().getString("phone");
            this.email = getArguments().getString("email");
            if (this.number == null) {
                illegalStateException = new IllegalStateException("No Mobile number found on for OTP through bundle");
            }
        } else {
            illegalStateException = new IllegalStateException("No Mobile number found on for OTP through bundle");
        }
        this.resend = (TextView) walletPayment.findViewById(C0360R.id.resend);
        this.mobile = (EditText) walletPayment.findViewById(C0360R.id.mobile);
        this.otp = (EditText) walletPayment.findViewById(C0360R.id.otp);
        this.proceed = (Button) walletPayment.findViewById(C0360R.id.activate);
        this.info = (TextView) walletPayment.findViewById(C0360R.id.info);
        this.humble = (RelativeLayout) walletPayment.findViewById(C0360R.id.humble);
        this.pbwaitotp = (ProgressBar) walletPayment.findViewById(C0360R.id.pbwaitotp);
        this.proceed.setEnabled(false);
        this.proceed.setText(getString(C0360R.string.activate));
        this.otp.setVisibility(0);
        this.humble.setVisibility(0);
        this.pbwaitotp.setVisibility(0);
        this.info.setText(getString(C0360R.string.waiting_for_otp));
        this.mobile.setVisibility(8);
        this.cancelButton = (Button) walletPayment.findViewById(C0360R.id.cancel_button);
        this.cancelButton.setOnClickListener(new C04271());
        this.resend.setOnClickListener(new C04282());
        this.proceed.setOnClickListener(new C04293());
        this.otp.addTextChangedListener(new C04304());
        this.mSdkLoginSignUpActivity.showHideLogoutButton(false);
        this.mSdkLoginSignUpActivity.invalidateActivityOptionsMenu();
        return walletPayment;
    }

    private void setResetOtpButton(boolean visibility) {
        this.resend.setText(visibility ? this.mSdkLoginSignUpActivity.getResources().getString(C0360R.string.retry_otp) : this.mSdkLoginSignUpActivity.getResources().getString(C0360R.string.processing));
        this.resend.setEnabled(visibility);
    }

    private void setResetViews(boolean visibility) {
        int i;
        int i2 = 0;
        this.info.setText(visibility ? getString(C0360R.string.waiting_for_otp) : getString(C0360R.string.activating));
        ProgressBar progressBar = this.pbwaitotp;
        if (visibility) {
            i = 8;
        } else {
            i = 0;
        }
        progressBar.setVisibility(i);
        this.mobile.setEnabled(false);
        ProgressBar progressBar2 = this.pbwaitotp;
        if (!visibility) {
            i2 = 8;
        }
        progressBar2.setVisibility(i2);
        this.otp.setEnabled(visibility);
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() == 46) {
            if (event.getStatus()) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, this.mSdkLoginSignUpActivity.getResources().getString(C0360R.string.login_successful), false);
                if (this.userParams == null || !this.userParams.containsKey(SdkConstants.IS_HISTORY_CALL)) {
                    SdkSession.getInstance(this.mSdkLoginSignUpActivity).getUserVaults();
                    return;
                } else {
                    this.mSdkLoginSignUpActivity.startShowingHistory();
                    return;
                }
            }
            setResetViews(true);
            if (event.getValue() != null && event.getValue().toString().trim().toLowerCase().contains(SdkConstants.WALLET_ALREADY_EXIST_STRING)) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, event.getValue().toString(), true);
                this.mSdkLoginSignUpActivity.loadSignUpFragment(true);
            } else if (event.getValue() != null) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, event.getValue().toString(), true);
            } else {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
            }
        } else if (event.getType() == 49) {
            if (event.getStatus()) {
                checkForWalletOnlyPayment();
            } else if (SdkHelper.checkNetwork(this.mSdkLoginSignUpActivity)) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
            } else {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.disconnected_from_internet), true);
            }
        } else if (event.getType() != 48) {
        } else {
            if (event.getStatus()) {
                if (!(event == null || event.getValue() == null || !event.getValue().toString().equals(SdkConstants.CUSTOMER_REGISTERED))) {
                    SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, "OTP sent to Phone Number " + SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "phone"), false);
                }
                new Handler().postDelayed(new C04315(), 1000);
                return;
            }
            if (event.getValue() != null) {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, event.getValue().toString(), true);
            } else {
                SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
            }
            setResetOtpButton(true);
        }
    }

    private void checkForWalletOnlyPayment() {
        if (Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE)) > Double.parseDouble((String) this.mSdkLoginSignUpActivity.getMapObject().get(SdkConstants.AMOUNT))) {
            this.mSdkLoginSignUpActivity.inflateWalletPaymentFragment(true);
        } else {
            this.mSdkLoginSignUpActivity.inflateLoadWalletFragment(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mSdkLoginSignUpActivity != null && this.receiver != null) {
            this.mSdkLoginSignUpActivity.unregisterReceiver(this.receiver);
        }
    }

    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.receiver = new C04326();
        this.mSdkLoginSignUpActivity.registerReceiver(this.receiver, filter);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
