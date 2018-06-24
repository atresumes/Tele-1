package com.payUMoney.sdk.walledSdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SdkWalletPaymentFragment extends SdkBaseFragment {
    private TextView WalletBalanceTextView;
    private TextView amountToPayTextView;
    private Button cancelButton;
    private WalletSdkLoginSignUpActivity mSdkLoginSignUpActivity;
    private Button payButton;
    private TextView remainingWalletBalanceTextView;
    private HashMap<String, String> userParams;
    private TextView walletUsageTextView;

    class C04341 implements OnClickListener {
        C04341() {
        }

        public void onClick(View view1) {
            if (!SdkHelper.isValidClick()) {
                return;
            }
            if (SdkHelper.checkNetwork(SdkWalletPaymentFragment.this.getActivity())) {
                SdkWalletPaymentFragment.this.setResetButtons(false);
                SdkSession.getInstance(SdkWalletPaymentFragment.this.mSdkLoginSignUpActivity).debitFromWallet(SdkWalletPaymentFragment.this.userParams);
                return;
            }
            SdkHelper.showToastMessage(SdkWalletPaymentFragment.this.getActivity(), SdkWalletPaymentFragment.this.getString(C0360R.string.disconnected_from_internet), true);
        }
    }

    class C04352 implements OnClickListener {
        C04352() {
        }

        public void onClick(View arg0) {
            if (SdkHelper.isValidClick()) {
                WalletSdkLoginSignUpActivity access$200 = SdkWalletPaymentFragment.this.mSdkLoginSignUpActivity;
                SdkWalletPaymentFragment.this.mSdkLoginSignUpActivity.getClass();
                access$200.close(4, null);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSdkLoginSignUpActivity = (WalletSdkLoginSignUpActivity) getActivity();
        this.userParams = this.mSdkLoginSignUpActivity.getMapObject();
        return inflater.inflate(C0360R.layout.wallet_payment_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSdkLoginSignUpActivity.setTabNewTitle(this.mSdkLoginSignUpActivity.getResources().getString(C0360R.string.wallet_sufficient));
        this.cancelButton = (Button) view.findViewById(C0360R.id.cancel_button);
        this.payButton = (Button) view.findViewById(C0360R.id.pay_button);
        this.amountToPayTextView = (TextView) view.findViewById(C0360R.id.amount_to_pay);
        this.WalletBalanceTextView = (TextView) view.findViewById(C0360R.id.wal_bal);
        this.walletUsageTextView = (TextView) view.findViewById(C0360R.id.wal_use);
        this.remainingWalletBalanceTextView = (TextView) view.findViewById(C0360R.id.rem_wal);
        this.amountToPayTextView.setText("₹ " + round(Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT)), 2));
        this.WalletBalanceTextView.setText("₹ " + round(Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE)), 2));
        this.walletUsageTextView.setText("₹ " + round(Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT)), 2));
        this.remainingWalletBalanceTextView.setText("₹ " + getRemainingBalance());
        this.payButton.setOnClickListener(new C04341());
        this.cancelButton.setOnClickListener(new C04352());
        this.mSdkLoginSignUpActivity.showHideLogoutButton(true);
        this.mSdkLoginSignUpActivity.setTabVisibility();
        this.mSdkLoginSignUpActivity.invalidateActivityOptionsMenu();
    }

    private void setResetButtons(boolean visibility) {
        this.payButton.setText(visibility ? this.mSdkLoginSignUpActivity.getString(C0360R.string.pay_now) : this.mSdkLoginSignUpActivity.getString(C0360R.string.please_wait));
        this.payButton.setEnabled(visibility);
        this.cancelButton.setEnabled(visibility);
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
        SdkHelper.dismissProgressDialog();
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        switch (event.getType()) {
            case 45:
                setResetButtons(true);
                if (event.getStatus()) {
                    SdkHelper.showToastMessage(getActivity(), "Payment Success", false);
                    Intent intent = new Intent();
                    intent.putExtra(SdkConstants.PAYMENT_ID, (String) event.getValue());
                    WalletSdkLoginSignUpActivity walletSdkLoginSignUpActivity = this.mSdkLoginSignUpActivity;
                    this.mSdkLoginSignUpActivity.getClass();
                    walletSdkLoginSignUpActivity.close(3, intent);
                    return;
                } else if (((String) event.getValue()).contains(SdkConstants.NOT_ENOUGH_AMOUNT_STRING)) {
                    try {
                        SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, ((String) event.getValue()).replaceAll(SdkConstants.USER, "your").replaceAll("account", "Account."), true);
                        SdkSession.getInstance(this.mSdkLoginSignUpActivity).getUserVaults();
                        return;
                    } catch (Exception e) {
                        SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
                        return;
                    }
                } else {
                    SdkHelper.showToastMessage(this.mSdkLoginSignUpActivity, getString(C0360R.string.something_went_wrong), true);
                    return;
                }
            case 49:
                this.mSdkLoginSignUpActivity.onEventMainThread(event);
                return;
            default:
                return;
        }
    }

    public static BigDecimal round(double d, int decimalPlace) {
        return new BigDecimal(Double.toString(d)).setScale(decimalPlace, 4);
    }

    private BigDecimal getRemainingBalance() {
        return round(Double.parseDouble(SharedPrefsUtils.getStringPreference(this.mSdkLoginSignUpActivity, Keys.WALLET_BALANCE)) - Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT)), 2);
    }
}
