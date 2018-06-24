package com.payUMoney.sdk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONObject;

public class SdkForgotPasswordFragment extends Fragment implements ValidationListener {
    private Button done = null;
    private Crouton mCrouton = null;
    @Email(message = "email_is_invalid", order = 2)
    @Required(message = "Email_is_required", order = 1)
    private AutoCompleteTextView mEmail = null;
    private Validator mValidator = null;

    class C04011 implements OnTouchListener {
        C04011() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkForgotPasswordFragment.this.mEmail.showDropDown();
            }
            return false;
        }
    }

    class C04022 implements OnTouchListener {
        C04022() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkForgotPasswordFragment.this.mEmail.showDropDown();
            }
            return false;
        }
    }

    class C04033 implements OnClickListener {
        C04033() {
        }

        public void onClick(View arg0) {
            if (SdkHelper.checkNetwork(SdkForgotPasswordFragment.this.getActivity())) {
                SdkForgotPasswordFragment.this.mValidator.validate();
            } else {
                Toast.makeText(SdkForgotPasswordFragment.this.getActivity(), C0360R.string.disconnected_from_internet, 0).show();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0360R.layout.sdk_activity_forgot_password, container, false);
        getActivity().getWindow().setSoftInputMode(2);
        this.mEmail = (AutoCompleteTextView) view.findViewById(C0360R.id.email);
        this.done = (Button) view.findViewById(C0360R.id.done);
        this.mEmail.setAdapter(new ArrayAdapter(getActivity(), 17367050, new ArrayList(new HashSet())));
        this.mEmail.setOnTouchListener(new C04011());
        this.mValidator = new Validator(this);
        this.mValidator.setValidationListener(this);
        this.mEmail.setOnTouchListener(new C04022());
        this.done.setOnClickListener(new C04033());
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != C0360R.id.sign_in) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().setResult(0);
        getActivity().finish();
        return true;
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        switch (event.getType()) {
            case 19:
                if (event.getStatus()) {
                    try {
                        if (((JSONObject) event.getValue()).getString(SdkConstants.RESULT).equals("User is null")) {
                            Toast.makeText(getActivity(), "Email is not registered with PayUMoney", 1).show();
                            this.done.setText(C0360R.string.recover_password);
                            this.done.setEnabled(true);
                            return;
                        }
                        Toast.makeText(getActivity(), C0360R.string.email_sent_apologies_from_us, 1).show();
                        getActivity().setResult(-1);
                        getActivity().finish();
                        return;
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), 1).show();
                        return;
                    }
                }
                if (this.mCrouton != null) {
                    this.mCrouton.hide();
                    this.mCrouton = null;
                }
                this.mCrouton = Crouton.makeText(getActivity(), C0360R.string.something_went_wrong, Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
                this.mCrouton.show();
                this.done.setText(C0360R.string.recover_password);
                this.done.setEnabled(true);
                return;
            default:
                return;
        }
    }

    public void onValidationSucceeded() {
        if (this.mCrouton != null) {
            this.mCrouton.cancel();
        }
        this.done.setText(C0360R.string.please_wait);
        this.done.setEnabled(false);
        SdkSession.getInstance(getActivity().getApplicationContext()).forgotPassword(this.mEmail.getText().toString());
    }

    public void onValidationFailed(View view, Rule<?> rule) {
        this.mCrouton = Crouton.makeText(getActivity(), rule.getFailureMessage(), Style.ALERT).setConfiguration(SdkConstants.CONFIGURATION_SHORT);
        this.mCrouton.show();
        view.requestFocus();
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        this.mEmail.setAdapter(new ArrayAdapter(getActivity(), 17367050, emailAddressCollection));
    }
}
