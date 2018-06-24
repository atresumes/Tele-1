package com.payUMoney.sdk.fragment;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkLoginSignUpActivity;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

@TargetApi(11)
public class SdkSignUpFragment extends Fragment implements ValidationListener {
    private Crouton mCrouton = null;
    @Email(message = "This email appears to be invalid", order = 2)
    @Required(message = "Your email is required", order = 1)
    private AutoCompleteTextView mEmail = null;
    private EditText mPassword = null;
    @Required(message = "Please enter your phone number", order = 3)
    private AutoCompleteTextView mPhone = null;
    private Button mSignUp = null;
    Validator mValidator = null;
    Pattern patt = Pattern.compile("^(?=.{7,}$)((.*[A-Za-z]+.*[0-9]+|.*[0-9]+.*[A-Za-z]+).*$)");
    final String regex = "^(?=.{7,}$)((.*[A-Za-z]+.*[0-9]+|.*[0-9]+.*[A-Za-z]+).*$)";

    class C04151 implements OnTouchListener {
        C04151() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkSignUpFragment.this.mEmail.showDropDown();
            }
            return false;
        }
    }

    class C04162 implements OnClickListener {
        C04162() {
        }

        public void onClick(View view) {
            if (SdkHelper.checkNetwork(SdkSignUpFragment.this.getActivity())) {
                if (SdkSignUpFragment.this.mCrouton != null) {
                    SdkSignUpFragment.this.mCrouton.cancel();
                    SdkSignUpFragment.this.mCrouton = null;
                }
                SdkSignUpFragment.this.mValidator.validate();
                return;
            }
            Toast.makeText(SdkSignUpFragment.this.getActivity(), C0360R.string.disconnected_from_internet, 0).show();
        }
    }

    class C04173 implements OnTouchListener {
        C04173() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkSignUpFragment.this.mEmail.showDropDown();
            }
            return false;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0360R.layout.sdk_activity_signup, container, false);
        getActivity().getWindow().setSoftInputMode(2);
        this.mValidator = new Validator(this);
        this.mValidator.setValidationListener(this);
        this.mEmail = (AutoCompleteTextView) view.findViewById(C0360R.id.email);
        this.mPhone = (AutoCompleteTextView) view.findViewById(C0360R.id.phone_number);
        this.mPassword = (EditText) view.findViewById(C0360R.id.password);
        this.mEmail.setAdapter(new ArrayAdapter(getActivity(), 17367050, new ArrayList(new HashSet())));
        this.mEmail.setOnTouchListener(new C04151());
        this.mSignUp = (Button) view.findViewById(C0360R.id.done);
        this.mSignUp.setOnClickListener(new C04162());
        this.mEmail.setOnTouchListener(new C04173());
        ((TextView) view.findViewById(C0360R.id.tos_n_privacy)).setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        if (this.mCrouton != null) {
            this.mCrouton.cancel();
            this.mCrouton = null;
        }
        EventBus.getDefault().unregister(this);
    }

    public void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() == 16) {
            if (event.getStatus()) {
                SdkSession.getInstance(getActivity().getApplicationContext()).create(this.mEmail.getText().toString().trim(), this.mPassword.getText().toString().trim());
                return;
            }
            this.mCrouton = Crouton.makeText(getActivity(), (String) event.getValue(), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_LONG);
            this.mCrouton.show();
            resetButton();
        } else if (event.getType() == 1 && event.getStatus()) {
            ((SdkLoginSignUpActivity) getActivity()).close();
        }
    }

    void resetButton() {
        this.mPhone.setText("");
        this.mSignUp.setText(C0360R.string.sign_up);
        this.mSignUp.setEnabled(true);
    }

    public void onValidationSucceeded() {
        if (this.mCrouton != null) {
            this.mCrouton.cancel();
        }
        if (this.mPhone.getText().toString().length() < 10 || !this.mPhone.getText().toString().matches("[\\d]{10}$")) {
            this.mCrouton = Crouton.makeText(getActivity(), (CharSequence) "The phone number entered is invalid", Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_LONG);
            this.mCrouton.show();
        } else if (this.mPassword.getText().toString().matches("^(?=.{7,}$)((.*[A-Za-z]+.*[0-9]+|.*[0-9]+.*[A-Za-z]+).*$)")) {
            SdkSession.getInstance(getActivity().getApplicationContext()).sign_up(this.mEmail.getText().toString().trim(), this.mPhone.getText().toString().trim(), this.mPassword.getText().toString().trim());
            this.mSignUp.setText(C0360R.string.please_wait);
            this.mSignUp.setEnabled(false);
        } else {
            this.mCrouton = Crouton.makeText(getActivity(), (CharSequence) "Password should be minimum 7 character with atleast 1 letter and 1 number", Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_LONG);
            this.mCrouton.show();
        }
    }

    public void onValidationFailed(View view, Rule<?> rule) {
        this.mCrouton = Crouton.makeText(getActivity(), rule.getFailureMessage(), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_LONG);
        this.mCrouton.show();
        view.requestFocus();
    }
}
