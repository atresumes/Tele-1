package com.payUMoney.sdk.walledSdk;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils.Keys;
import de.greenrobot.event.EventBus;
import java.math.BigDecimal;
import java.util.HashMap;
import org.json.JSONObject;

public class SdkLoadWalletFragment extends SdkBaseFragment {
    private final int LESS_AMOUNT = 1;
    private final int LESS_THEN_ALLOWED_AMOUNT = 2;
    private final int MORE_AMOUNT = 3;
    private final int VALID_AMOUNT = 0;
    private TextView WalletBalanceTextView;
    private TextView amountToPayTextView;
    private Button cancelButton;
    private TextView finalWalletBalanceTextView;
    private double initialNeededAmount;
    private WalletSdkLoginSignUpActivity mSdkLoginSignUpActivity;
    private EditText neededAmountTextView;
    private Button payButton;
    private HashMap<String, String> userParams;

    class C04211 implements OnClickListener {
        C04211() {
        }

        public void onClick(View arg0) {
            if (SdkHelper.checkNetwork(SdkLoadWalletFragment.this.getActivity())) {
                String inputAmount = SdkLoadWalletFragment.this.neededAmountTextView.getText().toString();
                int mIsValidAmount = SdkLoadWalletFragment.this.checkForValidInputAmount(inputAmount);
                if (inputAmount != null && inputAmount.length() > 2 && mIsValidAmount == 0) {
                    SdkLoadWalletFragment.this.setResetButtons(false);
                    SdkSession.getInstance(SdkLoadWalletFragment.this.mSdkLoginSignUpActivity).loadWallet(SdkLoadWalletFragment.this.userParams, inputAmount.substring(2, inputAmount.length()));
                    return;
                } else if (mIsValidAmount == 1) {
                    SdkHelper.showToastMessage(SdkLoadWalletFragment.this.getActivity(), SdkLoadWalletFragment.this.getString(C0360R.string.less_input_balance), true);
                    return;
                } else if (mIsValidAmount == 2) {
                    SdkHelper.showToastMessage(SdkLoadWalletFragment.this.getActivity(), "Minimum allowed wallet load Amount is " + SharedPrefsUtils.getStringPreference(SdkLoadWalletFragment.this.mSdkLoginSignUpActivity, "minLimit") + ".", true);
                    return;
                } else {
                    SdkHelper.showToastMessage(SdkLoadWalletFragment.this.getActivity(), "Maximum allowed wallet load Amount is " + SharedPrefsUtils.getStringPreference(SdkLoadWalletFragment.this.mSdkLoginSignUpActivity, "maxLimit") + ".", true);
                    return;
                }
            }
            SdkHelper.showToastMessage(SdkLoadWalletFragment.this.getActivity(), SdkLoadWalletFragment.this.getString(C0360R.string.disconnected_from_internet), true);
        }
    }

    class C04222 implements OnClickListener {
        C04222() {
        }

        public void onClick(View arg0) {
            WalletSdkLoginSignUpActivity access$200 = SdkLoadWalletFragment.this.mSdkLoginSignUpActivity;
            SdkLoadWalletFragment.this.mSdkLoginSignUpActivity.getClass();
            access$200.close(4, null);
        }
    }

    private class SimpleTextWatcher implements TextWatcher {
        private String currentInputString;
        private EditText neededAmountTextView;

        SimpleTextWatcher(EditText editText) {
            this.neededAmountTextView = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s != null) {
                this.currentInputString = s.toString();
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (this.neededAmountTextView != null && this.neededAmountTextView.getSelectionStart() == 1) {
                this.neededAmountTextView.setText(this.currentInputString);
                this.neededAmountTextView.setSelection(2);
            }
            if (s == null || s.toString().length() <= 2 || !SdkLoadWalletFragment.this.isDouble(s.toString().substring(2, s.toString().length()))) {
                SdkLoadWalletFragment.this.payButton.setEnabled(false);
                return;
            }
            SdkLoadWalletFragment.this.payButton.setEnabled(true);
            SdkLoadWalletFragment.this.finalWalletBalanceTextView.setText("₹ " + SdkLoadWalletFragment.this.getFinalWalletBalance());
        }

        public void afterTextChanged(Editable s) {
            if (this.neededAmountTextView != null && this.neededAmountTextView.getText().toString().length() <= 1) {
                this.neededAmountTextView.setText("₹ ");
                this.neededAmountTextView.setSelection(2);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSdkLoginSignUpActivity = (WalletSdkLoginSignUpActivity) getActivity();
        this.userParams = this.mSdkLoginSignUpActivity.getMapObject();
        return inflater.inflate(C0360R.layout.walletsdk_load_wallet_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSdkLoginSignUpActivity.setTabNewTitle(this.mSdkLoginSignUpActivity.getResources().getString(C0360R.string.wallet_in_sufficient));
        this.cancelButton = (Button) view.findViewById(C0360R.id.cancel_button);
        this.payButton = (Button) view.findViewById(C0360R.id.pay_button);
        this.initialNeededAmount = Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT)) - Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE));
        this.amountToPayTextView = (TextView) view.findViewById(C0360R.id.amount_to_pay);
        this.WalletBalanceTextView = (TextView) view.findViewById(C0360R.id.wal_bal);
        this.neededAmountTextView = (EditText) view.findViewById(C0360R.id.wal_use);
        this.finalWalletBalanceTextView = (TextView) view.findViewById(C0360R.id.rem_wal);
        this.amountToPayTextView.setText("₹ " + round(Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT))));
        this.WalletBalanceTextView.setText("₹ " + round(Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE))));
        this.neededAmountTextView.setText("₹ " + round(getNeededBalance()));
        this.finalWalletBalanceTextView.setText("₹ " + getFinalWalletBalance());
        this.neededAmountTextView.addTextChangedListener(new SimpleTextWatcher(this.neededAmountTextView));
        this.payButton.setOnClickListener(new C04211());
        this.cancelButton.setOnClickListener(new C04222());
        this.mSdkLoginSignUpActivity.showHideLogoutButton(true);
        this.mSdkLoginSignUpActivity.setTabVisibility();
        this.mSdkLoginSignUpActivity.invalidateActivityOptionsMenu();
    }

    private int checkForValidInputAmount(String inputAmount) {
        if (Double.parseDouble(inputAmount.substring(2, inputAmount.length())) < this.initialNeededAmount) {
            return 1;
        }
        if (SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit") != null && !SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit").equals(SdkConstants.NULL_STRING) && Double.parseDouble(inputAmount.substring(2, inputAmount.length())) < Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit"))) {
            return 2;
        }
        if (SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "maxLimit") == null || SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "maxLimit").equals(SdkConstants.NULL_STRING) || Double.parseDouble(inputAmount.substring(2, inputAmount.length())) <= Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "maxLimit"))) {
            return 0;
        }
        return 3;
    }

    public void onDestroy() {
        super.onDestroy();
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

    public void onEventMainThread(SdkCobbocEvent event) {
        switch (event.getType()) {
            case 42:
                setResetButtons(true);
                if (event.getStatus()) {
                    this.mSdkLoginSignUpActivity.callSdkToLoadWallet((JSONObject) event.getValue());
                    return;
                } else if (event.getValue() != null) {
                    try {
                        SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, ((JSONObject) event.getValue()).getString("message"), true);
                        return;
                    } catch (Exception e) {
                        SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
                        return;
                    }
                } else {
                    SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
                    return;
                }
            default:
                return;
        }
    }

    private void setResetButtons(boolean visibility) {
        this.payButton.setText(visibility ? this.mSdkLoginSignUpActivity.getString(C0360R.string.load_wallet) : this.mSdkLoginSignUpActivity.getString(C0360R.string.please_wait));
        this.payButton.setEnabled(visibility);
        this.cancelButton.setEnabled(visibility);
    }

    public static BigDecimal round(double d) {
        return new BigDecimal(Double.toString(d)).setScale(2, 4);
    }

    private double getNeededBalance() {
        double neededAmount = Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT)) - Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE));
        if (SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit") == null || !SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit").equals(SdkConstants.NULL_STRING) || neededAmount >= Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit"))) {
            return neededAmount;
        }
        return Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, "minLimit"));
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private BigDecimal getFinalWalletBalance() {
        String inputAmount = null;
        if (!(this.neededAmountTextView == null || this.neededAmountTextView.getText() == null)) {
            inputAmount = this.neededAmountTextView.getText().toString();
        }
        if (inputAmount == null || inputAmount.length() <= 2) {
            return round(0.0d);
        }
        double currentInputAmount = Double.parseDouble(inputAmount.substring(2, inputAmount.length()));
        if (currentInputAmount < this.initialNeededAmount) {
            return round(0.0d);
        }
        return round(currentInputAmount - this.initialNeededAmount);
    }
}
