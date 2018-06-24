package com.payUMoney.sdk.fragment;

import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkLoginSignUpActivity;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.dialog.SdkOtpProgressDialog;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkLoginFragment extends Fragment implements ValidationListener {
    private FragmentActivity f933c = null;
    private String loginMode = "";
    AccountManager mAccountManager = null;
    Crouton mCrouton = null;
    @Email(message = "This email appears to be invalid", order = 2)
    @Required(message = "Your email is required", order = 1)
    private AutoCompleteTextView mEmail = null;
    private Button mLogin = null;
    private EditText mOtpEditText = null;
    @Password(message = "Please enter your password", order = 3)
    private EditText mPassword = null;
    Validator mValidator = null;
    private Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
    private View otpProgress = null;
    private RadioGroup radioGroup = null;
    private BroadcastReceiver receiver = null;

    class C04074 implements OnClickListener {
        C04074() {
        }

        public void onClick(View view) {
            if (SdkLoginFragment.this.mCrouton != null) {
                SdkLoginFragment.this.mCrouton.cancel();
                SdkLoginFragment.this.mCrouton = null;
            }
            SdkSession.getInstance(SdkLoginFragment.this.getActivity()).cancelPendingRequests(SdkSession.TAG);
            ((SdkLoginSignUpActivity) SdkLoginFragment.this.getActivity()).loadForgotPasswordFragment(true);
        }
    }

    class C04096 implements TextWatcher {
        C04096() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                SdkLoginFragment.this.mLogin.setEnabled(true);
            } else {
                SdkLoginFragment.this.mLogin.setEnabled(false);
            }
        }
    }

    class C04107 implements TextWatcher {
        C04107() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() == 6) {
                SdkLoginFragment.this.otpProgress.setVisibility(8);
                SdkLoginFragment.this.hideKeyboardIfShown();
                SdkLoginFragment.this.mLogin.setEnabled(true);
                return;
            }
            SdkLoginFragment.this.mLogin.setEnabled(false);
            SdkLoginFragment.this.otpProgress.setVisibility(0);
        }
    }

    class C04118 extends BroadcastReceiver {
        C04118() {
        }

        public void onReceive(Context context, Intent intent) {
            String msgBody = null;
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle == null || SdkLoginFragment.this.mOtpEditText == null) {
                    Toast.makeText(SdkLoginFragment.this.f933c, "Couldn't read sms, please enter OTP manually", 1).show();
                    SdkLoginFragment.this.mOtpEditText.requestFocus();
                    return;
                }
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msgBody = msgs[i].getMessageBody();
                    }
                    if (msgBody != null && msgBody.toLowerCase().contains("verification")) {
                        Matcher m = SdkLoginFragment.this.otpPattern.matcher(msgBody);
                        if (m.find()) {
                            SdkLoginFragment.this.mOtpEditText.setText(m.group(0));
                            SdkLoginFragment.this.otpProgress.setVisibility(4);
                            SdkSession.getInstance(SdkLoginFragment.this.f933c.getApplicationContext()).create(SdkLoginFragment.this.mEmail.getText().toString(), m.group(0));
                            return;
                        }
                        Toast.makeText(SdkLoginFragment.this.f933c, "Couldn't read sms, please enter OTP manually", 1).show();
                        SdkLoginFragment.this.mOtpEditText.requestFocus();
                    }
                } catch (Exception e) {
                    Toast.makeText(SdkLoginFragment.this.f933c, "Couldn't read sms, please enter OTP manually", 1).show();
                    SdkLoginFragment.this.mOtpEditText.requestFocus();
                }
            }
        }
    }

    public class OnError implements Callback {
        public boolean handleMessage(Message msg) {
            SdkLogger.m17e("onError", "ERROR");
            return false;
        }
    }

    private class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {
        private SetupEmailAutoCompleteTask() {
        }

        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList();
            Cursor emailCur = SdkLoginFragment.this.f933c.getContentResolver().query(CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
            while (emailCur.moveToNext()) {
                emailAddressCollection.add(emailCur.getString(emailCur.getColumnIndex("data1")));
            }
            emailCur.close();
            return emailAddressCollection;
        }

        protected void onPostExecute(List<String> emailAddressCollection) {
            SdkLoginFragment.this.addEmailsToAutoComplete(emailAddressCollection);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(C0360R.layout.sdk_activity_login, container, false);
        getActivity().getWindow().setSoftInputMode(2);
        this.f933c = getActivity();
        this.mAccountManager = AccountManager.get(getActivity().getApplicationContext());
        this.mValidator = new Validator(this);
        this.mValidator.setValidationListener(this);
        this.mEmail = (AutoCompleteTextView) view.findViewById(C0360R.id.email);
        this.mPassword = (EditText) view.findViewById(C0360R.id.password);
        this.mOtpEditText = (EditText) view.findViewById(C0360R.id.otpEditText);
        this.mLogin = (Button) view.findViewById(C0360R.id.login);
        this.mLogin.setEnabled(false);
        this.mEmail.setAdapter(new ArrayAdapter(getActivity(), 17367050, new ArrayList(new HashSet())));
        this.otpProgress = view.findViewById(C0360R.id.otpProgress);
        this.otpProgress.setVisibility(4);
        this.radioGroup = (RadioGroup) view.findViewById(C0360R.id.loginOptions);
        RadioButton passwordLogin = (RadioButton) view.findViewById(C0360R.id.passwordLogin);
        RadioButton guestLogin = (RadioButton) view.findViewById(C0360R.id.guest_login);
        final RadioButton otpLogin = (RadioButton) view.findViewById(C0360R.id.loginotp);
        String allowGuestCheckoutValue = this.f933c.getIntent().getStringExtra(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_VALUE);
        String otpLoginValue = this.f933c.getIntent().getStringExtra(SdkConstants.OTP_LOGIN);
        if ((allowGuestCheckoutValue == null || allowGuestCheckoutValue.equals("") || allowGuestCheckoutValue.equals("0") || allowGuestCheckoutValue.equals(SdkConstants.NULL_STRING)) && (otpLoginValue == null || otpLoginValue.equals("") || otpLoginValue.equals("0") || otpLoginValue.equals(SdkConstants.NULL_STRING))) {
            this.loginMode = CBAnalyticsConstant.DEFAULT;
            view.findViewById(C0360R.id.passwordLayout).setVisibility(0);
            view.findViewById(C0360R.id.forgot_password).setVisibility(0);
        } else if (allowGuestCheckoutValue.equals(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT)) {
            passwordLogin.setVisibility(0);
            guestLogin.setVisibility(0);
            view.findViewById(C0360R.id.forgot_password).setVisibility(0);
        } else if (allowGuestCheckoutValue.equals(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_ONLY)) {
            this.loginMode = "guestLogin";
            this.mLogin.setEnabled(true);
        } else if (otpLoginValue.equals("1")) {
            otpLogin.setVisibility(0);
            passwordLogin.setVisibility(0);
            view.findViewById(C0360R.id.forgot_password).setVisibility(0);
        }
        ((Button) view.findViewById(C0360R.id.sendOtpBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SdkSession.getInstance(SdkLoginFragment.this.f933c.getApplicationContext()).generateAndSendOtp(SdkLoginFragment.this.mEmail.getText().toString());
                SdkLoginFragment.this.hideKeyboardIfShown();
                SdkLoginFragment.this.otpProgress.setVisibility(0);
                SdkOtpProgressDialog.showDialog(SdkLoginFragment.this.f933c.getApplicationContext(), SdkLoginFragment.this.otpProgress);
                ((EditText) view.findViewById(C0360R.id.otpEditText)).setText("");
                ((Button) view.findViewById(C0360R.id.sendOtpBtn)).setText("Resend");
                ((Button) view.findViewById(C0360R.id.sendOtpBtn)).setGravity(17);
            }
        });
        this.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (SdkLoginFragment.this.mEmail.getText().toString().equals("") && checkedId != -1) {
                    SdkLoginFragment.this.mCrouton = Crouton.makeText(SdkLoginFragment.this.f933c, C0360R.string.enter_email_id, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                    SdkLoginFragment.this.mCrouton.show();
                    SdkLoginFragment.this.radioGroup.clearCheck();
                } else if (SdkLoginFragment.isEmailValid(SdkLoginFragment.this.mEmail.getText().toString()) && checkedId != -1) {
                    SdkLoginFragment.this.mCrouton = Crouton.makeText(SdkLoginFragment.this.f933c, C0360R.string.invalid_email_id, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                    SdkLoginFragment.this.mCrouton.show();
                    SdkLoginFragment.this.radioGroup.clearCheck();
                } else if (checkedId == C0360R.id.passwordLogin) {
                    if (SdkLoginFragment.this.mCrouton != null) {
                        SdkLoginFragment.this.mCrouton.cancel();
                        SdkLoginFragment.this.mCrouton = null;
                    }
                    SdkLoginFragment.this.loginMode = "passwordLogin";
                    if (view.findViewById(C0360R.id.password).getVisibility() == 8) {
                        view.findViewById(C0360R.id.password).setVisibility(0);
                        view.findViewById(C0360R.id.password).requestFocus();
                    }
                    if (view.findViewById(C0360R.id.passwordLayout).getVisibility() == 8) {
                        view.findViewById(C0360R.id.passwordLayout).setVisibility(0);
                    }
                    if (view.findViewById(C0360R.id.forgot_password).getVisibility() == 8) {
                        view.findViewById(C0360R.id.forgot_password).setVisibility(0);
                    }
                    if (view.findViewById(C0360R.id.loginOTP).getVisibility() == 0) {
                        view.findViewById(C0360R.id.loginOTP).setVisibility(8);
                    }
                    if (SdkLoginFragment.this.otpProgress != null && SdkLoginFragment.this.otpProgress.getVisibility() == 0) {
                        SdkLoginFragment.this.otpProgress.setVisibility(4);
                    }
                } else if (checkedId == C0360R.id.loginotp) {
                    if (SdkLoginFragment.this.mCrouton != null) {
                        SdkLoginFragment.this.mCrouton.cancel();
                        SdkLoginFragment.this.mCrouton = null;
                    }
                    SdkLoginFragment.this.loginMode = "otpLogin";
                    if (view.findViewById(C0360R.id.password).getVisibility() == 0) {
                        view.findViewById(C0360R.id.password).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.passwordLayout).getVisibility() == 0) {
                        view.findViewById(C0360R.id.passwordLayout).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.forgot_password).getVisibility() == 0) {
                        view.findViewById(C0360R.id.forgot_password).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.loginOTP).getVisibility() == 8) {
                        view.findViewById(C0360R.id.loginOTP).setVisibility(0);
                    }
                    if (otpLogin.isChecked()) {
                        SdkSession.getInstance(SdkLoginFragment.this.f933c.getApplicationContext()).generateAndSendOtp(SdkLoginFragment.this.mEmail.getText().toString());
                    }
                    SdkLoginFragment.this.hideKeyboardIfShown();
                    SdkLoginFragment.this.otpProgress.setVisibility(0);
                    SdkOtpProgressDialog.showDialog(SdkLoginFragment.this.f933c.getApplicationContext(), SdkLoginFragment.this.otpProgress);
                    ((EditText) view.findViewById(C0360R.id.otpEditText)).setHint("");
                    ((Button) view.findViewById(C0360R.id.sendOtpBtn)).setText("Resend");
                    ((Button) view.findViewById(C0360R.id.sendOtpBtn)).setGravity(17);
                } else if (checkedId == C0360R.id.guest_login) {
                    if (SdkLoginFragment.this.mCrouton != null) {
                        SdkLoginFragment.this.mCrouton.cancel();
                        SdkLoginFragment.this.mCrouton = null;
                    }
                    SdkLoginFragment.this.loginMode = "guestLogin";
                    if (view.findViewById(C0360R.id.password).getVisibility() == 0) {
                        view.findViewById(C0360R.id.password).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.passwordLayout).getVisibility() == 0) {
                        view.findViewById(C0360R.id.passwordLayout).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.forgot_password).getVisibility() == 0) {
                        view.findViewById(C0360R.id.forgot_password).setVisibility(8);
                    }
                    if (SdkLoginFragment.this.otpProgress != null && SdkLoginFragment.this.otpProgress.getVisibility() == 0) {
                        SdkLoginFragment.this.otpProgress.setVisibility(4);
                    }
                    SdkLoginFragment.this.mLogin.setEnabled(true);
                }
            }
        });
        this.mEmail.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view1, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    SdkLoginFragment.this.mEmail.showDropDown();
                }
                if (SdkLoginFragment.this.otpProgress != null && SdkLoginFragment.this.otpProgress.getVisibility() == 0) {
                    SdkLoginFragment.this.otpProgress.setVisibility(4);
                }
                SdkLoginFragment.this.radioGroup.clearCheck();
                if (view.findViewById(C0360R.id.loginOTP).getVisibility() == 0) {
                    view.findViewById(C0360R.id.loginOTP).setVisibility(8);
                }
                if (!SdkLoginFragment.this.loginMode.equals(CBAnalyticsConstant.DEFAULT)) {
                    if (view.findViewById(C0360R.id.password).getVisibility() == 0) {
                        view.findViewById(C0360R.id.password).setVisibility(8);
                    }
                    if (view.findViewById(C0360R.id.passwordLayout).getVisibility() == 0) {
                        view.findViewById(C0360R.id.passwordLayout).setVisibility(8);
                    }
                }
                return false;
            }
        });
        view.findViewById(C0360R.id.forgot_password).setOnClickListener(new C04074());
        this.mLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (SdkHelper.checkNetwork(SdkLoginFragment.this.getActivity())) {
                    if (SdkLoginFragment.this.mCrouton != null) {
                        SdkLoginFragment.this.mCrouton.hide();
                        SdkLoginFragment.this.mCrouton = null;
                    }
                    if (SdkLoginFragment.this.loginMode == "guestLogin") {
                        if (SdkLoginFragment.this.mEmail.getText().toString().equals("")) {
                            SdkLoginFragment.this.mCrouton = Crouton.makeText(SdkLoginFragment.this.f933c, C0360R.string.enter_email_id, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                            SdkLoginFragment.this.mCrouton.show();
                            SdkLoginFragment.this.radioGroup.clearCheck();
                            return;
                        } else if (SdkLoginFragment.isEmailValid(SdkLoginFragment.this.mEmail.getText().toString())) {
                            SdkLoginFragment.this.mCrouton = Crouton.makeText(SdkLoginFragment.this.f933c, C0360R.string.invalid_email_id, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                            SdkLoginFragment.this.mCrouton.show();
                            SdkLoginFragment.this.radioGroup.clearCheck();
                            return;
                        } else {
                            SdkLoginFragment.this.onValidationSucceeded();
                            return;
                        }
                    } else if (SdkLoginFragment.this.loginMode != "otpLogin") {
                        SdkLoginFragment.this.mValidator.validate();
                        return;
                    } else if (((EditText) view.findViewById(C0360R.id.otpEditText)).getText().toString().isEmpty()) {
                        Toast.makeText(SdkLoginFragment.this.f933c.getApplicationContext(), "OTP Field is empty", 1);
                        return;
                    } else {
                        SdkSession.getInstance(SdkLoginFragment.this.f933c.getApplicationContext()).create(SdkLoginFragment.this.mEmail.getText().toString(), ((EditText) view.findViewById(C0360R.id.otpEditText)).getText().toString());
                        return;
                    }
                }
                Toast.makeText(SdkLoginFragment.this.getActivity(), C0360R.string.disconnected_from_internet, 0).show();
            }
        });
        this.mPassword.addTextChangedListener(new C04096());
        this.mOtpEditText.addTextChangedListener(new C04107());
        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        populateAutoComplete();
    }

    public void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    private void populateAutoComplete() {
        if (VERSION.SDK_INT < 14 && VERSION.SDK_INT >= 8) {
            new SetupEmailAutoCompleteTask().execute(new Void[]{null, null});
        }
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (this.mCrouton != null) {
            this.mCrouton.cancel();
            this.mCrouton = null;
        }
        if (this.receiver != null) {
            this.f933c.unregisterReceiver(this.receiver);
        }
    }

    private void login(String username, String password) {
        this.mLogin.setEnabled(false);
        this.mLogin.setText(C0360R.string.logging_in);
        if (this.loginMode.equals("guestLogin")) {
            SdkSession.getInstance(this.f933c.getApplicationContext()).setLoginMode(this.loginMode);
            SdkSession.getInstance(this.f933c.getApplicationContext()).setGuestEmail(username);
            Intent intent = new Intent();
            intent.putExtra(SdkConstants.AMOUNT, this.f933c.getIntent().getStringExtra(SdkConstants.AMOUNT));
            intent.putExtra("merchantId", this.f933c.getIntent().getStringExtra("merchantId"));
            intent.putExtra(SdkConstants.PARAMS, this.f933c.getIntent().getSerializableExtra(SdkConstants.PARAMS));
            this.f933c.setResult(-1, intent);
            this.f933c.finish();
            return;
        }
        SdkSession.getInstance(this.f933c.getApplicationContext()).create(username, password);
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        switch (event.getType()) {
            case 1:
                if (!event.getStatus()) {
                    this.mCrouton = Crouton.makeText(this.f933c, this.loginMode == "otpLogin" ? C0360R.string.invalid_otp : C0360R.string.invalid_email_or_password, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                    this.mCrouton.show();
                    this.mLogin.setEnabled(true);
                    this.mLogin.setText(C0360R.string.login);
                    this.mPassword.setText("");
                    this.mPassword.requestFocus();
                    return;
                } else if (event.getValue().toString().equals("Error")) {
                    Toast.makeText(this.f933c, "Error while Login", 1).show();
                    return;
                } else {
                    ((SdkLoginSignUpActivity) getActivity()).close();
                    return;
                }
            case 38:
                JSONObject result = (JSONObject) event.getValue();
                if (!event.getStatus() || result == null) {
                    if (result == null || result.optString("message") == null) {
                        this.mCrouton = Crouton.makeText(this.f933c, (CharSequence) "Unable to send OTP now,try resending or use password login...", Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                        this.mOtpEditText.setHint("Try resending OTP...");
                    } else {
                        this.mCrouton = Crouton.makeText(this.f933c, result.optString("message"), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                    }
                    this.otpProgress.setVisibility(4);
                    this.mCrouton.show();
                    return;
                }
                try {
                    this.mCrouton = Crouton.makeText(this.f933c, result.getString("message"), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                    this.mCrouton.show();
                    registerOTP();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public void registerOTP() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.receiver = new C04118();
        this.f933c.registerReceiver(this.receiver, filter);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        this.mEmail.setAdapter(new ArrayAdapter(this.f933c, 17367050, emailAddressCollection));
    }

    public void onValidationSucceeded() {
        if (this.mCrouton != null) {
            this.mCrouton.cancel();
        }
        ((InputMethodManager) this.f933c.getSystemService("input_method")).hideSoftInputFromWindow(this.mPassword.getApplicationWindowToken(), 0);
        login(this.mEmail.getText().toString(), this.mPassword.getText().toString());
    }

    public void onValidationFailed(View view, Rule<?> rule) {
        this.mCrouton = Crouton.makeText(this.f933c, rule.getFailureMessage(), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
        this.mCrouton.show();
        view.requestFocus();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        if (Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 2).matcher(email).matches()) {
            isValid = true;
        }
        return !isValid;
    }

    public void hideKeyboardIfShown() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.f933c.getSystemService("input_method");
        View view = this.f933c.getCurrentFocus();
        if (view == null) {
            view = new View(this.f933c);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
