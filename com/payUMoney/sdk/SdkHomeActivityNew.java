package com.payUMoney.sdk;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.payUMoney.sdk.adapter.SdkCouponListAdapter;
import com.payUMoney.sdk.adapter.SdkExpandableListAdapter;
import com.payUMoney.sdk.adapter.SdkStoredCardAdapter;
import com.payUMoney.sdk.dialog.SdkOtpProgressDialog;
import com.payUMoney.sdk.dialog.SdkQustomDialogBuilder;
import com.payUMoney.sdk.fragment.SdkDebit.MakePaymentListener;
import com.payUMoney.sdk.fragment.SdkNetBankingFragment;
import com.payUMoney.sdk.fragment.SdkPayUMoneyPointsFragment;
import com.payUMoney.sdk.fragment.SdkStoredCardFragment;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils;
import com.payu.custombrowser.util.CBConstant;
import de.greenrobot.event.EventBus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkHomeActivityNew extends FragmentActivity implements MakePaymentListener, SdkNetBankingFragment.MakePaymentListener, SdkStoredCardAdapter.MakePaymentListener, SdkStoredCardFragment.MakePaymentListener {
    public static String choosedCoupan = null;
    public static double coupan_amt = 0.0d;
    public final int LOAD_AND_PAY_USING_WALLET = 9;
    final int LOGIN = 1;
    private EditText OTPEditText;
    private AlertDialog OTPVerificationdialog;
    private final int PAYMNET_CANCELLED = 21;
    private final int PAYMNET_LOGOUT = 22;
    public final int SIGN_UP = 7;
    public final int WEB_VIEW = 2;
    private Builder alertDialogBuilder;
    private String allowGuestCheckout = "";
    public double amount = 0.0d;
    public double amt_convenience = 0.0d;
    public double amt_convenience_wallet = 0.0d;
    public double amt_discount = 0.0d;
    public double amt_net = 0.0d;
    private Button anotherAccountButton;
    private JSONObject appResponse;
    private TextView applyCoupon = null;
    private ArrayList<String> availableCreditCards = null;
    private ArrayList<String> availableDebitCards;
    private ArrayList<String> availableModes;
    private String cardHashForOneClickTxn = null;
    public double cashback = 0.0d;
    private boolean chooseOtherMode = false;
    private JSONObject convenienceChargesObject;
    int count = 0;
    private SdkCouponListAdapter coupanAdapter = null;
    private LinearLayout couponLayout = null;
    private ListView couponList = null;
    private JSONObject couponListItem;
    private String currentVersion = null;
    private HashMap<String, Object> data = new HashMap();
    private JSONObject details;
    private String device_id = null;
    public double discount = 0.0d;
    private boolean firstTimeFetchingOneClickFlag = false;
    private boolean fromPayUBizzApp = false;
    private boolean fromPayUMoneyApp;
    private boolean guestCheckOut = false;
    private RelativeLayout humble;
    public TextView info;
    private boolean isAnotherGroupExpanding = false;
    private String key = null;
    private SdkExpandableListAdapter listAdapter = null;
    private boolean loadWalletCall = false;
    public double loadWalletMaxLimit = 10000.0d;
    public double loadWalletMinLimit = 0.0d;
    public TextView mAmount = null;
    public JSONArray mCouponsArray = null;
    public TextView mCvvTnCLink;
    private boolean mFrontPaymentModesEnabled = true;
    private boolean mInternalLoadWalletCall = false;
    private boolean mIsLoginInitiated = false;
    private boolean mIsUserWalletBlocked = false;
    private boolean mNeedToShowOneTapCheckBox = false;
    private JSONObject mNetBankingStatusObject;
    private boolean mOTPAutoRead = false;
    private CheckBox mOneTap;
    private boolean mOnlyWalletPaymentModeActive = false;
    public TextView mOrderSummary = null;
    private boolean mProgress = false;
    private ProgressDialog mProgressDialog = null;
    private boolean mWalletRecentlyVerified = false;
    private EditText mannualCouponEditText = null;
    private String manualCouopnNameString;
    private boolean manualCouponEntered = false;
    private HashMap<String, String> map = null;
    private EditText mobileEditText;
    String mode = SdkConstants.WALLET_STRING;
    private Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
    private Button payByWalletButton = null;
    private String paymentId = "";
    private ExpandableListView paymentModesList = null;
    private JSONObject paymentOption;
    private boolean pointsActive = false;
    private Button proceed;
    private ProgressBar progressBarWaitOTP;
    private String quickLogin = "";
    private BroadcastReceiver receiver = null;
    public TextView resend;
    public TextView savings = null;
    private SdkSession sdkSession = null;
    private AlertDialog splashDialog;
    public JSONArray storedCardList = null;
    private JSONObject user;
    private String userId;
    private boolean userParamsFetchedExplicitely = false;
    public double userPoints = 0.0d;
    private Button verifyCouponBtn = null;
    private RelativeLayout verifyCouponProgress = null;
    private boolean walletActive = false;
    public double walletAmount = 0.0d;
    public double walletBal = 0.0d;
    private TextView walletBalance = null;
    private LinearLayout walletBoxLayout = null;
    private CheckBox walletCheck = null;
    private boolean walletFlag = false;
    private JSONObject walletJason;
    public double walletUsage = 0.0d;
    private LinearLayout whenHideChooseCouponLayoutRequired;

    class C03611 implements OnGroupCollapseListener {
        C03611() {
        }

        public void onGroupCollapse(int groupPosition) {
            if (!SdkHomeActivityNew.this.isAnotherGroupExpanding) {
                SdkHomeActivityNew.this.mode = "";
                SdkHomeActivityNew.this.updateDetails(SdkHomeActivityNew.this.mode);
            }
        }
    }

    class C03632 implements OnGroupExpandListener {
        int previousGroup = -1;

        C03632() {
        }

        public void onGroupExpand(int groupPosition) {
            if (SdkHomeActivityNew.this.mOneTap.getVisibility() == 8) {
                SdkHomeActivityNew.this.mOneTap.setVisibility(0);
                SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(0);
                if (SdkHomeActivityNew.this.payByWalletButton.getVisibility() == 0) {
                    SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                    SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
                }
            }
            if (!(this.previousGroup == -1 || groupPosition == this.previousGroup)) {
                SdkHomeActivityNew.this.isAnotherGroupExpanding = true;
                SdkHomeActivityNew.this.paymentModesList.collapseGroup(this.previousGroup);
                SdkHomeActivityNew.this.isAnotherGroupExpanding = false;
            }
            this.previousGroup = groupPosition;
            Object object = SdkHomeActivityNew.this.listAdapter.getGroup(groupPosition);
            if (object != null) {
                String currentGroup = object.toString();
                if (currentGroup != null) {
                    if (currentGroup.equals(SdkConstants.PAYMENT_MODE_STORE_CARDS)) {
                        SdkHomeActivityNew.this.mode = "";
                    }
                    if (currentGroup.equals(SdkConstants.PAYMENT_MODE_DC)) {
                        SdkHomeActivityNew.this.mode = SdkConstants.PAYMENT_MODE_DC;
                    }
                    if (currentGroup.equals(SdkConstants.PAYMENT_MODE_CC)) {
                        SdkHomeActivityNew.this.mode = SdkConstants.PAYMENT_MODE_CC;
                    }
                    if (currentGroup.equals(SdkConstants.PAYMENT_MODE_NB)) {
                        SdkHomeActivityNew.this.mode = SdkConstants.PAYMENT_MODE_NB;
                        SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                        SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
                    }
                }
            }
            if (SdkHomeActivityNew.this.guestCheckOut) {
                SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
            }
            SdkHomeActivityNew.this.updateDetails(SdkHomeActivityNew.this.mode);
        }
    }

    class C03643 implements OnClickListener {
        C03643() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    class C03654 implements View.OnClickListener {
        C03654() {
        }

        public void onClick(View v) {
            onCheckedChanged(SdkHomeActivityNew.this.mOneTap.isChecked());
        }

        public void onCheckedChanged(boolean isChecked) {
            if (isChecked) {
                SdkHomeActivityNew.this.sdkSession.enableOneClickTransaction("1");
            } else {
                SdkHomeActivityNew.this.sdkSession.enableOneClickTransaction("0");
            }
        }
    }

    class C03665 implements View.OnClickListener {
        C03665() {
        }

        public void onClick(View view) {
            if (!SdkHelper.isValidClick() || SdkHomeActivityNew.this.payByWalletButton == null || SdkHomeActivityNew.this.payByWalletButton.getText() == null || SdkHomeActivityNew.this.payByWalletButton.getText().toString() == null || !SdkHomeActivityNew.this.getString(C0360R.string.pay_using_wallet).equals(SdkHomeActivityNew.this.payByWalletButton.getText().toString())) {
                SdkHomeActivityNew.this.loadWalletDialog();
            } else {
                SdkHomeActivityNew.this.walletDialog();
            }
        }
    }

    class C03676 implements OnCheckedChangeListener {
        C03676() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if (((SdkHomeActivityNew.this.amount + SdkHomeActivityNew.this.amt_convenience_wallet) - SdkHomeActivityNew.this.userPoints) - SdkHomeActivityNew.this.amt_discount <= SdkHomeActivityNew.this.walletAmount) {
                    SdkHomeActivityNew.this.amt_convenience = SdkHomeActivityNew.this.amt_convenience_wallet;
                    SdkHomeActivityNew.this.walletBal = SdkHomeActivityNew.this.walletAmount - (((SdkHomeActivityNew.this.amt_convenience + SdkHomeActivityNew.this.amount) - SdkHomeActivityNew.this.amt_discount) - SdkHomeActivityNew.this.userPoints);
                    SdkHomeActivityNew.this.walletBalance.setText(SdkHomeActivityNew.this.getString(C0360R.string.remaining_wallet_bal) + " " + SdkHomeActivityNew.round(SdkHomeActivityNew.this.walletBal));
                    SdkHomeActivityNew.this.walletUsage = SdkHomeActivityNew.this.walletAmount - SdkHomeActivityNew.this.walletBal;
                    SdkHomeActivityNew.this.updateDetails(SdkConstants.WALLET_STRING);
                    SdkHomeActivityNew.this.walletFlag = true;
                    SdkHomeActivityNew.this.paymentModesList.setVisibility(8);
                    SdkHomeActivityNew.this.payByWalletButton.setVisibility(0);
                    SdkHomeActivityNew.this.payByWalletButton.setText(C0360R.string.pay_using_wallet);
                    if (SdkHomeActivityNew.this.mOneTap != null && SdkHomeActivityNew.this.mOneTap.getVisibility() == 0) {
                        SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                        SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
                    }
                } else {
                    SdkHomeActivityNew.this.walletFlag = false;
                    SdkHomeActivityNew.this.walletUsage = SdkHomeActivityNew.this.walletAmount;
                    SdkHomeActivityNew.this.walletBal = 0.0d;
                    SdkHomeActivityNew.this.updateDetails(SdkHomeActivityNew.this.mode);
                    SdkHomeActivityNew.this.walletBalance.setText(SdkHomeActivityNew.this.getString(C0360R.string.remaining_wallet_bal) + " " + 0.0d);
                    if (!SdkHomeActivityNew.this.mFrontPaymentModesEnabled) {
                        SdkHomeActivityNew.this.walletFlag = true;
                        if (SdkHomeActivityNew.this.mOneTap != null && SdkHomeActivityNew.this.mOneTap.getVisibility() == 0) {
                            SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                            SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
                        }
                        SdkHomeActivityNew.this.paymentModesList.setVisibility(8);
                        SdkHomeActivityNew.this.payByWalletButton.setVisibility(0);
                        SdkHomeActivityNew.this.payByWalletButton.setText(C0360R.string.load_and_pay);
                        SdkHomeActivityNew.this.updateDetailsForLoadWallet();
                    }
                }
                SdkHomeActivityNew.this.walletCheck.setEnabled(false);
                return;
            }
            if (!(SdkHomeActivityNew.this.mOneTap == null || SdkHomeActivityNew.this.mOneTap.getVisibility() == 0 || (!SdkHomeActivityNew.this.mode.equals(SdkConstants.PAYMENT_MODE_CC) && !SdkHomeActivityNew.this.mode.equals(SdkConstants.PAYMENT_MODE_DC) && !SdkHomeActivityNew.this.mode.isEmpty()))) {
                SdkHomeActivityNew.this.mOneTap.setVisibility(0);
                SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(0);
            }
            SdkHomeActivityNew.this.unchecked();
        }
    }

    class C03687 implements View.OnClickListener {
        C03687() {
        }

        public void onClick(View v) {
            SdkHomeActivityNew.this.handleViewDetails();
        }
    }

    class C03738 implements View.OnClickListener {

        class C03691 implements View.OnClickListener {
            C03691() {
            }

            public void onClick(View v) {
                SdkHomeActivityNew.this.hideKeyboardIfShown();
                String manualCoupon = "";
                if (SdkHomeActivityNew.this.mannualCouponEditText.getText() != null) {
                    manualCoupon = SdkHomeActivityNew.this.mannualCouponEditText.getText().toString();
                }
                if (manualCoupon.isEmpty()) {
                    Toast.makeText(SdkHomeActivityNew.this, "Coupon Field is empty!", 0).show();
                    return;
                }
                SdkSession.getInstance(SdkHomeActivityNew.this.getApplicationContext()).verifyManualCoupon(manualCoupon, SdkHomeActivityNew.this.paymentId, SdkHomeActivityNew.this.device_id, "0");
                for (int j = 0; j < SdkHomeActivityNew.this.couponList.getCount(); j++) {
                    if (((RadioButton) SdkHomeActivityNew.this.couponList.getChildAt(j).findViewById(C0360R.id.coupanSelect)).isChecked()) {
                        ((RadioButton) SdkHomeActivityNew.this.couponList.getChildAt(j).findViewById(C0360R.id.coupanSelect)).setChecked(false);
                    }
                }
                SdkHomeActivityNew.this.verifyCouponProgress.setVisibility(0);
                SdkHomeActivityNew.this.mannualCouponEditText.clearFocus();
                SdkHomeActivityNew.this.mannualCouponEditText.setText("");
                SdkHomeActivityNew.this.mannualCouponEditText.setHint("");
                SdkOtpProgressDialog.showDialog(SdkHomeActivityNew.this.getApplicationContext(), SdkHomeActivityNew.this.verifyCouponProgress);
            }
        }

        class C03702 implements OnClickListener {
            C03702() {
            }

            public void onClick(DialogInterface dialog, int which) {
                SdkHomeActivityNew.this.manualCouponEntered = false;
                SdkHomeActivityNew.choosedCoupan = null;
                SdkHomeActivityNew.coupan_amt = 0.0d;
                SdkHomeActivityNew.this.manualCouopnNameString = null;
            }
        }

        class C03713 implements OnClickListener {
            C03713() {
            }

            public void onClick(DialogInterface dialog, int which) {
                if (!SdkHomeActivityNew.this.manualCouponEntered) {
                    Boolean couponSelectedFromList = Boolean.valueOf(false);
                    for (int j = 0; j < SdkHomeActivityNew.this.couponList.getCount(); j++) {
                        if (((RadioButton) SdkHomeActivityNew.this.couponList.getChildAt(j).findViewById(C0360R.id.coupanSelect)).isChecked()) {
                            try {
                                SdkHomeActivityNew.this.couponListItem = (JSONObject) SdkHomeActivityNew.this.coupanAdapter.getItem(j);
                                SdkHomeActivityNew.choosedCoupan = SdkHomeActivityNew.this.couponListItem.getString("couponString");
                                SdkHomeActivityNew.coupan_amt = SdkHomeActivityNew.this.couponListItem.getDouble("couponAmount");
                                couponSelectedFromList = Boolean.valueOf(true);
                                SdkLogger.m20i("Choosed coupan", SdkHomeActivityNew.choosedCoupan);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (SdkHomeActivityNew.this.mannualCouponEditText != null && !SdkHomeActivityNew.this.mannualCouponEditText.getText().toString().isEmpty()) {
                        Toast.makeText(SdkHomeActivityNew.this, "Invalid Coupon entered", 0).show();
                    } else if (!couponSelectedFromList.booleanValue()) {
                        Toast.makeText(SdkHomeActivityNew.this, "No Coupon Selected", 0).show();
                    }
                }
                SdkHomeActivityNew.this.amt_discount = SdkHomeActivityNew.coupan_amt;
                if (SdkHomeActivityNew.this.walletCheck.isChecked()) {
                    SdkHomeActivityNew.this.updateWalletDetails();
                } else {
                    SdkHomeActivityNew.this.updateDetails(SdkHomeActivityNew.this.mode);
                }
                if ((SdkHomeActivityNew.this.amount + SdkHomeActivityNew.this.amt_convenience_wallet) - SdkHomeActivityNew.this.amt_discount <= 0.0d && !SdkHomeActivityNew.this.chooseOtherMode) {
                    SdkHomeActivityNew.this.inflateSufficientDiscountLayout();
                } else if (((SdkHomeActivityNew.this.amount + SdkHomeActivityNew.this.amt_convenience_wallet) - SdkHomeActivityNew.this.amt_discount) - SdkHomeActivityNew.this.userPoints <= 0.0d && !SdkHomeActivityNew.this.chooseOtherMode) {
                    SdkHomeActivityNew.this.pointDialog();
                }
                if (SdkHomeActivityNew.this.amount - SdkHomeActivityNew.this.amt_discount == 0.0d) {
                    if (SdkHomeActivityNew.this.payByWalletButton.isShown()) {
                        SdkHomeActivityNew.this.mAmount.setText(" " + SdkHomeActivityNew.round(SdkHomeActivityNew.this.amt_convenience));
                        SdkHomeActivityNew.this.walletUsage = SdkHomeActivityNew.this.walletAmount - SdkHomeActivityNew.this.amt_net;
                        SdkHomeActivityNew.this.walletBal = SdkHomeActivityNew.this.walletAmount - SdkHomeActivityNew.this.walletUsage;
                        SdkHomeActivityNew.this.walletBalance.setText(SdkHomeActivityNew.this.getString(C0360R.string.remaining_wallet_bal) + " " + SdkHomeActivityNew.round(((float) (SdkHomeActivityNew.this.walletBal / 100.0d)) * 100.0f));
                    } else {
                        SdkHomeActivityNew.this.mAmount.setText(" 0.0");
                    }
                }
                if (SdkHomeActivityNew.coupan_amt > 0.0d) {
                    String coupon_string;
                    if (SdkHomeActivityNew.this.manualCouponEntered) {
                        coupon_string = SdkHomeActivityNew.this.manualCouopnNameString + " Applied";
                    } else {
                        coupon_string = SdkHomeActivityNew.choosedCoupan + " Applied";
                    }
                    ((TextView) SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon1)).setText(coupon_string);
                    SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon1).setVisibility(0);
                    ((TextView) SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon)).setText(C0360R.string.remove);
                    SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon).setVisibility(0);
                    if (SdkHomeActivityNew.this.manualCouponEntered) {
                        SdkHomeActivityNew.this.manualCouponEntered = false;
                    }
                }
                if (SdkHomeActivityNew.this.amt_discount > 0.0d || SdkHomeActivityNew.this.userPoints > 0.0d) {
                    SdkHomeActivityNew.this.savings.setText("Savings : Rs." + SdkHomeActivityNew.round(((float) ((SdkHomeActivityNew.this.amt_discount + SdkHomeActivityNew.this.userPoints) / 100.0d)) * 100.0f));
                    SdkHomeActivityNew.this.savings.setVisibility(0);
                    return;
                }
                SdkHomeActivityNew.this.savings.setVisibility(4);
            }
        }

        class C03724 implements OnItemClickListener {
            C03724() {
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SdkHomeActivityNew.this.mannualCouponEditText.setHint(C0360R.string.enter_coupon);
                if (((RadioButton) view.findViewById(C0360R.id.coupanSelect)).isChecked()) {
                    ((RadioButton) view.findViewById(C0360R.id.coupanSelect)).setChecked(false);
                } else {
                    ((RadioButton) view.findViewById(C0360R.id.coupanSelect)).setChecked(true);
                }
                for (int j = 0; j < SdkHomeActivityNew.this.couponList.getCount(); j++) {
                    if (j != i) {
                        ((RadioButton) SdkHomeActivityNew.this.couponList.getChildAt(j).findViewById(C0360R.id.coupanSelect)).setChecked(false);
                    }
                }
            }
        }

        C03738() {
        }

        public void onClick(View view) {
            if (((TextView) SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon)).getText().toString().equals("Remove")) {
                SdkHomeActivityNew.choosedCoupan = null;
                SdkHomeActivityNew.coupan_amt = 0.0d;
                SdkHomeActivityNew.this.amt_discount = SdkHomeActivityNew.this.discount;
                if (SdkHomeActivityNew.this.walletCheck.isChecked()) {
                    SdkHomeActivityNew.this.updateWalletDetails();
                } else {
                    SdkHomeActivityNew.this.updateDetails(SdkHomeActivityNew.this.mode);
                }
                ((TextView) SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon)).setText(C0360R.string.view_coupon);
                SdkHomeActivityNew.this.findViewById(C0360R.id.selectCoupon1).setVisibility(4);
                if (SdkHomeActivityNew.this.amt_discount > 0.0d || SdkHomeActivityNew.this.userPoints > 0.0d) {
                    SdkHomeActivityNew.this.savings.setText("Savings : Rs." + SdkHomeActivityNew.round(((float) ((SdkHomeActivityNew.this.amt_discount + SdkHomeActivityNew.this.userPoints) / 100.0d)) * 100.0f));
                    SdkHomeActivityNew.this.savings.setVisibility(0);
                    return;
                }
                SdkHomeActivityNew.this.savings.setVisibility(4);
                return;
            }
            SdkQustomDialogBuilder alertDialog = new SdkQustomDialogBuilder(SdkHomeActivityNew.this, C0360R.style.PauseDialog);
            View convertView = SdkHomeActivityNew.this.getLayoutInflater().inflate(C0360R.layout.sdk_coupon_list, null);
            SdkHomeActivityNew.this.couponList = (ListView) convertView.findViewById(C0360R.id.lv);
            SdkHomeActivityNew.this.couponList.setChoiceMode(1);
            SdkHomeActivityNew.this.whenHideChooseCouponLayoutRequired = (LinearLayout) convertView.findViewById(C0360R.id.whenHideChooseCouponLayoutRequired);
            SdkHomeActivityNew.this.verifyCouponBtn = (Button) convertView.findViewById(C0360R.id.verifyCoupon);
            SdkHomeActivityNew.this.mannualCouponEditText = (EditText) convertView.findViewById(C0360R.id.mannualCouponEditText);
            SdkHomeActivityNew.this.verifyCouponProgress = (RelativeLayout) convertView.findViewById(C0360R.id.verifyCouponProgress);
            SdkHomeActivityNew.this.verifyCouponProgress.setVisibility(4);
            SdkHomeActivityNew.this.verifyCouponBtn.setOnClickListener(new C03691());
            try {
                if (!SdkHomeActivityNew.this.user.has(SdkConstants.COUPONS_STRING) || SdkHomeActivityNew.this.user.isNull(SdkConstants.COUPONS_STRING) || SdkHomeActivityNew.this.mCouponsArray == null) {
                    SdkHomeActivityNew.this.whenHideChooseCouponLayoutRequired.setVisibility(8);
                    alertDialog.setNegativeButton("Cancel", new C03702());
                    alertDialog.setPositiveButton("Apply", new C03713()).setView(convertView).show();
                    SdkHomeActivityNew.this.couponList.setOnItemClickListener(new C03724());
                }
                SdkHomeActivityNew.this.coupanAdapter = new SdkCouponListAdapter(SdkHomeActivityNew.this, SdkHomeActivityNew.this.mCouponsArray);
                SdkHomeActivityNew.this.couponList.setAdapter(SdkHomeActivityNew.this.coupanAdapter);
                alertDialog.setNegativeButton("Cancel", new C03702());
                alertDialog.setPositiveButton("Apply", new C03713()).setView(convertView).show();
                SdkHomeActivityNew.this.couponList.setOnItemClickListener(new C03724());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C03749 implements View.OnClickListener {
        C03749() {
        }

        public void onClick(View v) {
            if (SdkHelper.checkNetwork(SdkHomeActivityNew.this)) {
                SdkHomeActivityNew.this.sdkSession.sendMobileVerificationCodeForWalletCreation(SdkHomeActivityNew.this.mobileEditText.getText().toString());
            } else {
                SdkHelper.showToastMessage(SdkHomeActivityNew.this, SdkHomeActivityNew.this.getString(C0360R.string.connect_to_internet), true);
            }
        }
    }

    private class LoadWalletAmountEditTextTextWatcher implements TextWatcher {
        EditText loadAmountEditText;
        Button loadWalletButton;
        double loadWalletMinLimit;

        LoadWalletAmountEditTextTextWatcher(EditText editText, Button button, double minLimit) {
            this.loadAmountEditText = editText;
            this.loadWalletButton = button;
            this.loadWalletMinLimit = minLimit;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (this.loadWalletButton == null || this.loadAmountEditText == null || this.loadAmountEditText.getText() == null || this.loadAmountEditText.getText().toString() == null || !SdkHomeActivityNew.this.isDouble(this.loadAmountEditText.getText().toString()) || Double.parseDouble(this.loadAmountEditText.getText().toString()) < this.loadWalletMinLimit || Double.parseDouble(this.loadAmountEditText.getText().toString()) > SdkHomeActivityNew.this.loadWalletMaxLimit) {
                this.loadWalletButton.setEnabled(false);
            } else {
                this.loadWalletButton.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    public String getUserId() {
        return this.userId;
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.sdkSession = SdkSession.getInstance(getApplicationContext());
        initiateProgressDialog();
        this.device_id = SdkHelper.getAndroidID(this);
        this.currentVersion = SdkHelper.getAppVersion(this);
        this.availableModes = new ArrayList();
        this.availableCreditCards = new ArrayList();
        this.availableDebitCards = new ArrayList();
        try {
            if (SdkConstants.WALLET_SDK.booleanValue()) {
                this.details = new JSONObject(getIntent().getStringExtra(SdkConstants.PAYMENT_DETAILS_OBJECT));
                initLayout();
                return;
            }
            this.map = (HashMap) getIntent().getSerializableExtra(SdkConstants.PARAMS);
            this.map.put(SdkConstants.DEVICE_ID, this.device_id);
            this.map.put(SdkConstants.APP_VERSION, this.currentVersion);
            if (this.map.containsKey(SdkConstants.INTERNAL_LOAD_WALLET_CALL)) {
                this.mInternalLoadWalletCall = true;
                this.appResponse = new JSONObject((String) this.map.get(SdkConstants.INTERNAL_LOAD_WALLET_CALL));
                this.loadWalletCall = true;
                if (this.map.containsKey(SdkConstants.NEED_TO_SHOW_ONE_TAP_CHECK_BOX)) {
                    this.mNeedToShowOneTapCheckBox = false;
                }
            } else if (this.map.containsKey(SdkConstants.PAYUMONEY_APP)) {
                this.fromPayUMoneyApp = true;
                this.mNeedToShowOneTapCheckBox = true;
                this.appResponse = new JSONObject((String) this.map.get(SdkConstants.PAYUMONEY_APP));
                if (this.map.containsKey(SdkConstants.PAYMENT_TYPE) && ((String) this.map.get(SdkConstants.PAYMENT_TYPE)).equals(SdkConstants.LOAD_WALLET)) {
                    this.loadWalletCall = true;
                }
            } else if (this.map.containsKey(SdkConstants.PAYUBIZZ_APP)) {
                this.fromPayUBizzApp = true;
                this.appResponse = new JSONObject((String) this.map.get(SdkConstants.PAYUBIZZ_APP)).getJSONObject(SdkConstants.RESULT);
                if (!this.sdkSession.isLoggedIn()) {
                    if (this.appResponse.has(SdkConstants.CONFIG_DATA) && !this.appResponse.isNull(SdkConstants.CONFIG_DATA)) {
                        JSONObject merchantLoginParams = this.appResponse.getJSONObject(SdkConstants.CONFIG_DATA);
                        this.allowGuestCheckout = merchantLoginParams.optString(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_VALUE, "");
                        String temp = merchantLoginParams.optString("quickLoginEnabled", "");
                        if (temp.isEmpty() || !temp.equals("true")) {
                            this.quickLogin = "0";
                        } else {
                            this.quickLogin = "1";
                        }
                    }
                    check_login();
                    return;
                } else if (this.appResponse != null) {
                    initLayout();
                    return;
                }
            }
            if (this.fromPayUMoneyApp) {
                if (this.appResponse != null) {
                    initLayout();
                }
            } else if (this.mInternalLoadWalletCall) {
                if (this.appResponse != null) {
                    initLayout();
                }
            } else if (this.sdkSession.isLoggedIn()) {
                check_login();
            } else {
                this.sdkSession.fetchMechantParams((String) this.map.get("merchantId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    private void createPaymentModesList() {
        this.listAdapter = new SdkExpandableListAdapter(this, this.availableModes);
        this.paymentModesList.setAdapter(this.listAdapter);
        this.paymentModesList.setOnGroupCollapseListener(new C03611());
        this.paymentModesList.setOnGroupExpandListener(new C03632());
        this.paymentModesList.expandGroup(0);
    }

    public void handleViewDetails() {
        SdkQustomDialogBuilder qb = new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog);
        qb.setTitleColor(SdkConstants.active_black).setDividerColor(SdkConstants.active_yellow).setTitle((CharSequence) "Payment Breakdown").setPositiveButton("Ok", new C03643()).show();
        CharSequence message = new StringBuffer("Order Amount : Rs." + round(((float) (this.amount / 100.0d)) * 100.0f));
        if (this.amt_convenience > 0.0d) {
            message.append("\nConvenience Fee : Rs.").append(round(((float) (this.amt_convenience / 100.0d)) * 100.0f)).append("\nTotal : Rs.").append(round(((float) ((this.amt_convenience + this.amount) / 100.0d)) * 100.0f));
        } else {
            message.append("\nTotal : Rs.").append(round(((float) (this.amount / 100.0d)) * 100.0f));
        }
        if (this.amt_discount > 0.0d) {
            if (coupan_amt > 0.0d) {
                message.append("\nCoupon Discount : Rs.").append(round(((float) (this.amt_discount / 100.0d)) * 100.0f));
            } else {
                message.append("\nDiscount : Rs.").append(round(((float) (this.amt_discount / 100.0d)) * 100.0f));
            }
        } else if (this.cashback > 0.0d && coupan_amt <= 0.0d) {
            message.append("\nCashback : Rs.").append(round(((float) (this.cashback / 100.0d)) * 100.0f));
        }
        if (this.userPoints > 0.0d) {
            message.append("\nAvailable PayUMoney points : Rs.").append(round(((float) (this.userPoints / 100.0d)) * 100.0f));
        }
        message.append("\nNet Amount : Rs.").append(round(((float) (this.amt_net * 100.0d)) / 100.0f));
        if (this.walletUsage > 0.0d) {
            message.append("\nWallet Usage: Rs.").append(round(this.walletUsage));
        }
        qb.setMessage(message);
    }

    protected void onResume() {
        super.onResume();
        if (!(EventBus.getDefault() == null || EventBus.getDefault().isRegistered(this))) {
            EventBus.getDefault().register(this);
        }
        SharedPrefsUtils.setStringPreference(this, SdkConstants.USER_SESSION_COOKIE_PAGE_URL, getClass().getSimpleName());
    }

    public void onStop() {
        super.onStop();
        try {
            if (this.receiver != null) {
                unregisterReceiver(this.receiver);
                this.receiver = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void check_login() {
        SdkLogger.m14d(SdkConstants.TAG, "entered in check login()");
        if (this.sdkSession.isLoggedIn() || this.guestCheckOut) {
            if (!this.fromPayUBizzApp) {
                initLayout();
            } else if (this.appResponse != null && this.appResponse.has(SdkConstants.PAYMENT_ID) && !this.appResponse.isNull(SdkConstants.PAYMENT_ID)) {
                try {
                    this.paymentId = this.appResponse.getString(SdkConstants.PAYMENT_ID);
                    this.sdkSession.fetchUserParams(this.paymentId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (!this.sdkSession.isLoggedIn() && !this.mIsLoginInitiated) {
            dismissProgress();
            this.mIsLoginInitiated = true;
            Intent intent = new Intent(this, SdkLoginSignUpActivity.class);
            intent.putExtra(SdkConstants.AMOUNT, getIntent().getStringExtra(SdkConstants.AMOUNT));
            intent.putExtra("merchantId", getIntent().getStringExtra("merchantId"));
            intent.putExtra(SdkConstants.PARAMS, getIntent().getSerializableExtra(SdkConstants.PARAMS));
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("phone", getIntent().getStringExtra("phone"));
            intent.putExtra(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_VALUE, this.allowGuestCheckout);
            intent.putExtra(SdkConstants.OTP_LOGIN, this.quickLogin);
            startActivityForResult(intent, 1);
        }
    }

    public void initLayout() {
        initHomeLayout();
        invalidateOptionsMenu();
        initiateProgressDialog();
        SdkSession.getInstance(this).getNetBankingStatus();
        if (this.fromPayUMoneyApp || this.fromPayUBizzApp || SdkConstants.WALLET_SDK.booleanValue() || this.mInternalLoadWalletCall) {
            startPayment(this.appResponse);
        } else {
            this.sdkSession.createPayment(this.map);
        }
    }

    private void initHomeLayout() {
        setContentView(C0360R.layout.sdk_activity_home_new);
        this.mAmount = (TextView) findViewById(C0360R.id.sdkAmountText);
        this.savings = (TextView) findViewById(C0360R.id.savings);
        this.mOrderSummary = (TextView) findViewById(C0360R.id.orderSummary);
        this.walletBoxLayout = (LinearLayout) findViewById(C0360R.id.walletLayout);
        this.walletCheck = (CheckBox) this.walletBoxLayout.findViewById(C0360R.id.walletcheck);
        this.walletBalance = (TextView) this.walletBoxLayout.findViewById(C0360R.id.walletbalance);
        this.couponLayout = (LinearLayout) findViewById(C0360R.id.couponSection);
        this.applyCoupon = (TextView) this.couponLayout.findViewById(C0360R.id.selectCoupon);
        if (this.mNeedToShowOneTapCheckBox) {
            findViewById(C0360R.id.user_profile_is_cvv_less_layout).setVisibility(8);
        }
        this.mOneTap = (CheckBox) findViewById(C0360R.id.user_profile_is_cvv_less_checkbox);
        this.mCvvTnCLink = (TextView) findViewById(C0360R.id.cvv_tnc_link);
        this.mCvvTnCLink.setMovementMethod(LinkMovementMethod.getInstance());
        this.mOneTap.setOnClickListener(new C03654());
        this.paymentModesList = (ExpandableListView) findViewById(C0360R.id.lvExp);
        this.payByWalletButton = (Button) findViewById(C0360R.id.PayByWallet);
        this.payByWalletButton.setOnClickListener(new C03665());
        this.walletCheck.setOnCheckedChangeListener(new C03676());
        this.mOrderSummary.setOnClickListener(new C03687());
        if (this.sdkSession.isLoggedIn()) {
            this.sdkSession.enableOneClickTransaction("-1");
            this.firstTimeFetchingOneClickFlag = true;
        }
    }

    public double getAmount() {
        return this.amount;
    }

    private void inflateSufficientDiscountLayout() {
        dismissProgress();
        this.paymentModesList.setVisibility(4);
        this.mAmount.setText("0.0");
        this.savings.setText("Sufficient Discount");
        this.walletBoxLayout.setVisibility(8);
        this.mOrderSummary.setVisibility(8);
        this.payByWalletButton.setVisibility(8);
        this.couponLayout.setVisibility(8);
        SdkPayUMoneyPointsFragment fragment = new SdkPayUMoneyPointsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("details", this.details.toString());
        bundle.putDouble("userPoints", this.userPoints);
        bundle.putDouble("discount", this.discount);
        bundle.putDouble("cashback", this.cashback);
        bundle.putDouble("couponAmount", coupan_amt);
        bundle.putBoolean("enoughDiscount", true);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setCustomAnimations(C0360R.animator.card_flip_right_in, C0360R.animator.card_flip_right_out, C0360R.animator.card_flip_left_in, C0360R.animator.card_flip_left_out);
        transaction.replace(C0360R.id.pagerContainer, fragment, "payumoneypoints");
        transaction.addToBackStack("a");
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    public void startPayment(JSONObject params) {
        SdkLogger.m14d(SdkConstants.TAG, "Entered in Start Payment");
        try {
            if (SdkConstants.WALLET_SDK.booleanValue()) {
                this.applyCoupon.setVisibility(8);
                this.couponLayout.setVisibility(8);
                this.walletCheck.setVisibility(8);
                this.walletBoxLayout.setVisibility(8);
                this.savings.setVisibility(8);
                if (this.details.has(SdkConstants.USER) && !this.details.isNull(SdkConstants.USER)) {
                    if (!this.userParamsFetchedExplicitely) {
                        this.user = this.details.getJSONObject(SdkConstants.USER);
                    }
                    if (!(this.details == null || !this.details.has(SdkConstants.PAYMENT_ID) || this.details.isNull(SdkConstants.PAYMENT_ID))) {
                        this.paymentId = this.details.getString(SdkConstants.PAYMENT_ID);
                    }
                    if (this.user.has("userId") && !this.user.isNull("userId")) {
                        this.userId = this.user.getString("userId");
                    }
                }
            }
            if ((this.fromPayUMoneyApp || this.fromPayUBizzApp || this.mInternalLoadWalletCall) && !this.chooseOtherMode) {
                this.details = params;
            }
            if (this.fromPayUMoneyApp) {
                this.mOTPAutoRead = true;
            }
            if (!this.mWalletRecentlyVerified) {
                checkForAvailablePaymentOptions();
                checkForWalletOnlyPaymentMode();
                checkForUserWalletActive();
            }
            if (!(this.details == null || !this.details.has(SdkConstants.CONVENIENCE_CHARGES) || this.details.isNull(SdkConstants.CONVENIENCE_CHARGES))) {
                this.convenienceChargesObject = new JSONObject(this.details.getString(SdkConstants.CONVENIENCE_CHARGES));
            }
            JSONObject tempPaymentOption = null;
            if (this.paymentOption == null && this.details.has(SdkConstants.PAYMENT_OPTION) && !this.details.isNull(SdkConstants.PAYMENT_OPTION)) {
                tempPaymentOption = this.details.getJSONObject(SdkConstants.PAYMENT_OPTION);
                if (!(tempPaymentOption == null || !tempPaymentOption.has(SdkConstants.OPTIONS) || tempPaymentOption.isNull(SdkConstants.OPTIONS))) {
                    this.paymentOption = tempPaymentOption.getJSONObject(SdkConstants.OPTIONS);
                }
            }
            if (this.availableModes != null && this.availableModes.size() == 0) {
                checkForAvailablePaymentOptions();
            }
            if (!(this.availableModes == null || this.availableModes.size() <= 0 || SdkConstants.PAYMENT_MODE_STORE_CARDS.equals(this.availableModes.get(0)) || this.user == null || !this.user.has("savedCards") || this.user.isNull("savedCards"))) {
                this.availableModes.add(0, SdkConstants.PAYMENT_MODE_STORE_CARDS);
                setStoredCardList(this.user.getJSONArray("savedCards"));
            }
            if (!(tempPaymentOption == null || !tempPaymentOption.has(SdkConstants.CONFIG) || tempPaymentOption.isNull(SdkConstants.CONFIG))) {
                JSONObject tempConfig = tempPaymentOption.getJSONObject(SdkConstants.CONFIG);
                if (!(tempConfig == null || !tempConfig.has("publicKey") || tempConfig.isNull("publicKey"))) {
                    this.key = tempConfig.getString("publicKey").replaceAll("\\r", "");
                }
            }
            if (this.mOnlyWalletPaymentModeActive) {
                this.availableModes.clear();
                coupan_amt = 0.0d;
                this.applyCoupon.setVisibility(8);
                this.couponLayout.setVisibility(8);
            }
            if (this.availableModes == null || this.availableModes.size() <= 0) {
                this.mFrontPaymentModesEnabled = false;
            } else if (((String) this.availableModes.get(0)).equals(SdkConstants.PAYMENT_MODE_STORE_CARDS)) {
                this.mode = "";
            } else if (((String) this.availableModes.get(0)).equals(SdkConstants.PAYMENT_MODE_DC)) {
                this.mode = SdkConstants.PAYMENT_MODE_DC;
            } else if (((String) this.availableModes.get(0)).equals(SdkConstants.PAYMENT_MODE_CC)) {
                this.mode = SdkConstants.PAYMENT_MODE_CC;
            } else if (((String) this.availableModes.get(0)).equals(SdkConstants.PAYMENT_MODE_NB)) {
                this.mode = SdkConstants.PAYMENT_MODE_NB;
            }
            setAmountConvenience(this.mode);
            this.amount = this.details.getJSONObject(SdkConstants.PAYMENT).getDouble(SdkConstants.ORDER_AMOUNT);
            if (!(this.chooseOtherMode || this.guestCheckOut)) {
                this.userPoints = getPoints().doubleValue();
            }
            if (coupan_amt > 0.0d) {
                this.amt_discount = coupan_amt;
            } else {
                this.amt_discount = this.discount;
            }
            if ((this.amount + this.amt_convenience_wallet) - this.amt_discount <= 0.0d && !this.chooseOtherMode) {
                inflateSufficientDiscountLayout();
                if (this.mFrontPaymentModesEnabled) {
                    showWalletCheckBox();
                    this.walletCheck.setChecked(true);
                } else if (this.walletCheck != null) {
                }
            } else if (((this.amount + this.amt_convenience_wallet) - this.amt_discount) - this.userPoints > 0.0d || this.chooseOtherMode) {
                if (this.walletCheck.isChecked()) {
                    this.amt_net = (((this.amount + this.amt_convenience) - this.amt_discount) - this.walletUsage) - this.userPoints;
                } else {
                    this.amt_net = ((this.amount + this.amt_convenience) - this.amt_discount) - this.userPoints;
                }
                if (this.amt_net < 0.0d) {
                    this.amt_net = 0.0d;
                }
                if (this.fromPayUBizzApp) {
                    this.amount = getIntent().getDoubleExtra(SdkConstants.AMOUNT, 0.0d);
                }
                this.mAmount.setText(" " + round(this.amt_net));
                if (this.amt_discount > 0.0d || this.userPoints > 0.0d) {
                    this.savings.setText("Savings : Rs." + round(((float) ((this.amt_discount + this.userPoints) / 100.0d)) * 100.0f));
                    this.savings.setVisibility(0);
                } else {
                    this.savings.setVisibility(4);
                }
                createPaymentModesList();
                if (!SdkConstants.WALLET_SDK.booleanValue()) {
                    updateCouponsVisibility();
                    if (coupan_amt <= 0.0d) {
                        ((TextView) findViewById(C0360R.id.selectCoupon)).setText(C0360R.string.view_coupon);
                        handleCoupon();
                    }
                }
                if (this.guestCheckOut) {
                    if (this.mOneTap != null) {
                        this.mCvvTnCLink.setVisibility(8);
                    }
                    this.mOneTap.setVisibility(8);
                }
                dismissProgress();
                if (this.mFrontPaymentModesEnabled) {
                    showWalletCheckBox();
                    this.walletCheck.setChecked(true);
                } else if (this.walletCheck != null && this.walletCheck.getVisibility() == 0 && this.walletBal > 0.0d) {
                    this.walletCheck.setChecked(true);
                }
            } else {
                pointDialog();
                if (this.mFrontPaymentModesEnabled) {
                    showWalletCheckBox();
                    this.walletCheck.setChecked(true);
                } else if (this.walletCheck != null) {
                }
            }
        } catch (JSONException e) {
        }
    }

    private void checkForAvailablePaymentOptions() {
        JSONObject tempPaymentOption = null;
        try {
            if (this.paymentOption == null && this.details.has(SdkConstants.PAYMENT_OPTION) && !this.details.isNull(SdkConstants.PAYMENT_OPTION)) {
                tempPaymentOption = this.details.getJSONObject(SdkConstants.PAYMENT_OPTION);
                if (!(tempPaymentOption == null || !tempPaymentOption.has(SdkConstants.OPTIONS) || tempPaymentOption.isNull(SdkConstants.OPTIONS))) {
                    this.paymentOption = tempPaymentOption.getJSONObject(SdkConstants.OPTIONS);
                }
            }
            if (!(tempPaymentOption == null || !tempPaymentOption.has(SdkConstants.CONFIG) || tempPaymentOption.isNull(SdkConstants.CONFIG))) {
                JSONObject tempConfig = tempPaymentOption.getJSONObject(SdkConstants.CONFIG);
                if (!(tempConfig == null || !tempConfig.has("publicKey") || tempConfig.isNull("publicKey"))) {
                    this.key = tempConfig.getString("publicKey").replaceAll("\\r", "");
                }
            }
            if (this.paymentOption != null) {
                Iterator keys;
                if (checkForPaymentModeActive("dc") && !this.availableModes.contains(SdkConstants.PAYMENT_MODE_DC)) {
                    this.availableModes.add(SdkConstants.PAYMENT_MODE_DC);
                    if (!this.paymentOption.isNull("dc")) {
                        keys = new JSONObject(this.paymentOption.getString("dc")).keys();
                        while (keys.hasNext()) {
                            this.availableDebitCards.add((String) keys.next());
                        }
                    }
                }
                if (checkForPaymentModeActive("cc") && !this.availableModes.contains(SdkConstants.PAYMENT_MODE_CC)) {
                    this.availableModes.add(SdkConstants.PAYMENT_MODE_CC);
                    if (!this.paymentOption.isNull("cc")) {
                        keys = new JSONObject(this.paymentOption.getString("cc")).keys();
                        while (keys.hasNext()) {
                            this.availableCreditCards.add((String) keys.next());
                        }
                    }
                }
                if (checkForPaymentModeActive(CBConstant.NB) && !this.availableModes.contains(SdkConstants.PAYMENT_MODE_NB)) {
                    JSONObject nbJSONObject = new JSONObject(this.paymentOption.getString(CBConstant.NB));
                    if (nbJSONObject != null && nbJSONObject.keys().hasNext()) {
                        this.availableModes.add(SdkConstants.PAYMENT_MODE_NB);
                    }
                }
                if (checkForPaymentModeActive(SdkConstants.WALLET) && !this.paymentOption.getString(SdkConstants.WALLET).equals("0.0")) {
                    this.walletActive = true;
                }
                if (!(this.mOnlyWalletPaymentModeActive || !checkForPaymentModeActive(SdkConstants.POINTS) || this.paymentOption.getString(SdkConstants.POINTS).equals("0.0"))) {
                    this.pointsActive = true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.availableModes != null && this.availableModes.size() == 0) {
            this.mFrontPaymentModesEnabled = false;
        }
    }

    private boolean checkForPaymentModeActive(String paymentModeString) {
        try {
            if (!this.paymentOption.has(paymentModeString) || this.paymentOption.isNull(paymentModeString) || "-1".equals(this.paymentOption.getString(paymentModeString)) || SdkConstants.FALSE_STRING.equals(this.paymentOption.getString(paymentModeString)) || SdkConstants.NULL_STRING.equals(this.paymentOption.getString(paymentModeString))) {
                return false;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONArray getStoredCardList() {
        return this.storedCardList;
    }

    public void setStoredCardList(JSONArray list) {
        this.storedCardList = list;
    }

    public void handleCoupon() {
        findViewById(C0360R.id.selectCoupon).setOnClickListener(new C03738());
    }

    private void updateCouponsVisibility() {
        if (this.user.has(SdkConstants.COUPONS_STRING) && !this.user.isNull(SdkConstants.COUPONS_STRING)) {
            this.mCouponsArray = new JSONArray();
            try {
                if (this.user.getJSONArray(SdkConstants.COUPONS_STRING) != null) {
                    JSONArray tempArrayCoupons = this.user.getJSONArray(SdkConstants.COUPONS_STRING);
                    for (int i = 0; i < tempArrayCoupons.length(); i++) {
                        if (tempArrayCoupons.getJSONObject(i).getBoolean(SdkConstants.ENABLED_STRING)) {
                            this.mCouponsArray.put(tempArrayCoupons.getJSONObject(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (coupan_amt <= 0.0d) {
            if (this.loadWalletCall || this.guestCheckOut || this.mOnlyWalletPaymentModeActive) {
                this.applyCoupon.setVisibility(8);
                this.couponLayout.setVisibility(8);
                return;
            }
            this.applyCoupon.setText(C0360R.string.view_coupon);
            this.applyCoupon.setVisibility(0);
            this.couponLayout.setVisibility(0);
        }
    }

    public void updateDetails(String currentPaymentMode) {
        try {
            if (this.walletCheck.isChecked() || this.userPoints > 0.0d) {
                if (currentPaymentMode.isEmpty()) {
                    this.amt_convenience = this.amt_convenience_wallet;
                } else if (this.convenienceChargesObject == null || !this.convenienceChargesObject.has(currentPaymentMode) || this.convenienceChargesObject.isNull(currentPaymentMode)) {
                    this.amt_convenience = 0.0d;
                } else {
                    this.amt_convenience = Math.max(this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(SdkConstants.DEFAULT), this.amt_convenience_wallet);
                }
            } else if (currentPaymentMode.isEmpty()) {
                this.amt_convenience = 0.0d;
            } else if (this.convenienceChargesObject == null || !this.convenienceChargesObject.has(currentPaymentMode) || this.convenienceChargesObject.isNull(currentPaymentMode)) {
                this.amt_convenience = 0.0d;
            } else {
                this.amt_convenience = this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(SdkConstants.DEFAULT);
            }
            if (coupan_amt > 0.0d) {
                this.amt_discount = coupan_amt;
            } else {
                this.amt_discount = this.discount;
            }
            this.amt_net = (((this.amount + this.amt_convenience) - this.amt_discount) - this.walletUsage) - this.userPoints;
            if (this.amt_net < 0.0d) {
                this.amt_net = 0.0d;
            }
            this.mAmount.setText(" " + round(this.amt_net));
            this.walletBalance.setText(getString(C0360R.string.remaining_wallet_bal) + " " + round(this.walletBal));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDetailsForLoadWallet() {
        try {
            this.amt_convenience = new JSONObject(this.details.getString(SdkConstants.CONVENIENCE_CHARGES)).getJSONObject(SdkConstants.WALLET_STRING).getDouble(SdkConstants.DEFAULT);
            if (coupan_amt > 0.0d) {
                this.amt_discount = coupan_amt;
            } else {
                this.amt_discount = this.discount;
            }
            this.amt_net = (((this.amount + this.amt_convenience) - this.amt_discount) - this.walletUsage) - this.userPoints;
            if (this.amt_net < 0.0d) {
                this.amt_net = 0.0d;
            }
            this.mAmount.setText(" " + round(this.amt_net));
            this.walletBalance.setText(getString(C0360R.string.remaining_wallet_bal) + " " + round(this.walletBal));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPublicKey() {
        return this.key;
    }

    public JSONObject getBankObject() {
        return this.details;
    }

    public Double getPoints() {
        return Double.valueOf(this.userPoints);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9) {
            SdkSession.getInstance(this).verifyPaymentDetails(this.paymentId);
        } else if (requestCode == 1) {
            if (resultCode == -1) {
                if (this.sdkSession.getLoginMode().equals("guestLogin")) {
                    this.guestCheckOut = true;
                }
                this.mIsLoginInitiated = false;
                check_login();
                return;
            }
            close();
        } else if (requestCode == 2) {
            if (this.guestCheckOut) {
                SdkSession.getInstance(this).fetchPaymentStatus(this.paymentId);
            } else if (this.mInternalLoadWalletCall) {
                finish();
            } else {
                SdkSession.getInstance(this).verifyPaymentDetails(this.paymentId);
            }
        } else if (requestCode != 7) {
        } else {
            if (resultCode == -1) {
                SdkLogger.m20i("login_status", SdkConstants.SUCCESS_STRING);
                check_login();
            } else if (resultCode == 0) {
                SdkLogger.m20i("payment_status", "failure");
                check_login();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0360R.id.logout) {
            if (SdkConstants.WALLET_SDK.booleanValue()) {
                this.sdkSession.logout();
            } else {
                logout();
            }
        }
        return true;
    }

    public void logout() {
        this.sdkSession.logout("");
        Editor edit = getSharedPreferences(SdkConstants.SP_SP_NAME, 0).edit();
        edit.clear();
        edit.commit();
        this.sdkSession.setLoginMode("");
        resetValuesOnLogout();
        this.sdkSession.fetchMechantParams((String) this.map.get("merchantId"));
    }

    public void resetValuesOnLogout() {
        this.walletUsage = 0.0d;
        this.walletAmount = 0.0d;
        this.userPoints = 0.0d;
        this.amt_convenience = 0.0d;
        this.amt_net = 0.0d;
        this.amount = 0.0d;
        this.cashback = 0.0d;
        this.discount = 0.0d;
        this.amt_discount = 0.0d;
        this.amt_convenience_wallet = 0.0d;
        this.walletBal = 0.0d;
        coupan_amt = 0.0d;
        choosedCoupan = null;
        this.guestCheckOut = false;
        this.chooseOtherMode = false;
        this.paymentOption = null;
        this.pointsActive = false;
        this.walletActive = false;
        this.availableModes.clear();
        this.availableDebitCards.clear();
        this.availableCreditCards.clear();
    }

    public void setCardHashForOneClickTxn(String s) {
        this.cardHashForOneClickTxn = s;
    }

    public void goToPayment(String mode, HashMap<String, Object> data) throws JSONException {
        hideKeyboardIfShown();
        if (mode.equals(SdkConstants.PAYMENT_MODE_NB)) {
            String mBankCode = (String) data.get(SdkConstants.BANK_CODE);
            if (!(this.mNetBankingStatusObject == null || !this.mNetBankingStatusObject.has(mBankCode) || this.mNetBankingStatusObject.isNull(mBankCode))) {
                JSONObject mBankStatusObject = this.mNetBankingStatusObject.getJSONObject(mBankCode);
                if (mBankStatusObject != null && mBankStatusObject.has(SdkConstants.UP_STATUS) && !mBankStatusObject.isNull(SdkConstants.UP_STATUS) && Integer.parseInt(mBankStatusObject.getString(SdkConstants.UP_STATUS)) == 0) {
                    Toast.makeText(this, mBankStatusObject.getString(SdkConstants.BANK_TITLE_STRING) + " seems to be down temporarily.\nPlease select another bank or pay using other payment options.", 1).show();
                    return;
                }
            }
        }
        if (mode.equals(SdkConstants.PAYMENT_MODE_NB) && data.get(SdkConstants.BANK_CODE).equals("CITNB")) {
            Toast.makeText(this, "City Bank doesn't provide Net Banking!", 1).show();
            int i = 0;
            if (this.availableModes != null && this.availableModes.contains(SdkConstants.PAYMENT_MODE_DC)) {
                while (!((String) this.availableModes.get(i)).equals(SdkConstants.PAYMENT_MODE_DC)) {
                    i++;
                }
                Toast.makeText(this, "City Bank doesn't provide Net Banking!", 1).show();
                this.paymentModesList.expandGroup(i);
            }
        } else if (mode.equals(SdkConstants.PAYMENT_MODE_DC) && this.availableDebitCards != null && !this.availableDebitCards.contains(data.get(SdkConstants.BANK_CODE))) {
            Toast.makeText(this, "The merchant doesn't support: " + data.get(SdkConstants.BANK_CODE).toString() + " Debit Cards", 0).show();
        } else if (!mode.equals(SdkConstants.PAYMENT_MODE_CC) || (!(data.get(SdkConstants.BANK_CODE).toString().equals(SdkConstants.AMEX) || data.get(SdkConstants.BANK_CODE).toString().equals("DINR")) || this.availableCreditCards.contains(data.get(SdkConstants.BANK_CODE)))) {
            initiateProgressDialog();
            this.sdkSession.sendToPayUWithWallet(this.details, mode, data, Double.valueOf(this.userPoints), Double.valueOf(this.walletUsage), Double.valueOf(this.discount), Double.valueOf(this.amt_convenience));
        } else {
            Toast.makeText(this, "The merchant doesn't support: " + data.get(SdkConstants.BANK_CODE).toString() + " Credit Cards", 0).show();
        }
    }

    public void modifyConvenienceCharges(String cardBankType) {
        if (cardBankType == null) {
            updateDetails(this.mode);
            return;
        }
        String currentPaymentMode = this.mode;
        try {
            JSONObject modeConvenienceChargesObject;
            if (this.walletCheck.isChecked() || this.userPoints > 0.0d) {
                if (currentPaymentMode.isEmpty()) {
                    this.amt_convenience = this.amt_convenience_wallet;
                } else if (this.convenienceChargesObject == null || !this.convenienceChargesObject.has(currentPaymentMode) || this.convenienceChargesObject.isNull(currentPaymentMode)) {
                    this.amt_convenience = 0.0d;
                } else {
                    modeConvenienceChargesObject = this.convenienceChargesObject.getJSONObject(currentPaymentMode);
                    if (modeConvenienceChargesObject == null || !modeConvenienceChargesObject.has(cardBankType) || modeConvenienceChargesObject.isNull(cardBankType)) {
                        this.amt_convenience = this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(SdkConstants.DEFAULT);
                    } else {
                        this.amt_convenience = this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(cardBankType);
                    }
                    this.amt_convenience = Math.max(this.amt_convenience, this.amt_convenience_wallet);
                }
            } else if (currentPaymentMode.isEmpty()) {
                this.amt_convenience = 0.0d;
            } else if (this.convenienceChargesObject == null || !this.convenienceChargesObject.has(currentPaymentMode) || this.convenienceChargesObject.isNull(currentPaymentMode)) {
                this.amt_convenience = 0.0d;
            } else {
                modeConvenienceChargesObject = this.convenienceChargesObject.getJSONObject(currentPaymentMode);
                if (modeConvenienceChargesObject == null || !modeConvenienceChargesObject.has(cardBankType) || modeConvenienceChargesObject.isNull(cardBankType)) {
                    this.amt_convenience = this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(SdkConstants.DEFAULT);
                } else {
                    this.amt_convenience = this.convenienceChargesObject.getJSONObject(currentPaymentMode).getDouble(cardBankType);
                }
            }
            this.amt_net = (((this.amount + this.amt_convenience) - this.amt_discount) - this.walletUsage) - this.userPoints;
            if (this.amt_net < 0.0d) {
                this.amt_net = 0.0d;
            }
            this.mAmount.setText(" " + round(this.amt_net));
            this.walletBalance.setText(getString(C0360R.string.remaining_wallet_bal) + " " + round(this.walletBal));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboardIfShown() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideKeyboardIfShown(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        if (view == null) {
            view = getCurrentFocus();
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences mPref = getSharedPreferences(SdkConstants.SP_SP_NAME, 0);
        if (!this.mInternalLoadWalletCall) {
            getMenuInflater().inflate(C0360R.menu.sdk_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event != null) {
            String status;
            Intent intent;
            if (event.getType() == 51) {
                if (event.getStatus()) {
                    status = "-1";
                    JSONObject eventObject = (JSONObject) event.getValue();
                    try {
                        status = eventObject.getString("status");
                    } catch (Exception e) {
                        e.printStackTrace();
                        status = "-1";
                    }
                    if (!status.equals("0")) {
                        this.progressBarWaitOTP.setVisibility(8);
                        this.OTPEditText.setEnabled(true);
                        this.proceed.setEnabled(true);
                        if (event.getValue() != null) {
                            try {
                                Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                            } catch (Exception e2) {
                            }
                        } else {
                            Toast.makeText(this, getString(C0360R.string.something_went_wrong), 1).show();
                        }
                    } else if (!(eventObject == null || !eventObject.has(SdkConstants.RESULT) || eventObject.isNull(SdkConstants.RESULT))) {
                        try {
                            JSONObject resultObject = eventObject.getJSONObject(SdkConstants.RESULT);
                            if (resultObject.has(SdkConstants.AVAILABLE_AMOUNT) && !resultObject.isNull(SdkConstants.AVAILABLE_AMOUNT)) {
                                this.mWalletRecentlyVerified = true;
                                this.loadWalletMinLimit = resultObject.optDouble(SdkConstants.MIN_LOAD_LIMIT, 10.0d);
                                this.loadWalletMaxLimit = resultObject.optDouble(SdkConstants.MAX_LOAD_LIMIT, 10000.0d);
                                calculateOffersAndCashback();
                                if (this.OTPVerificationdialog != null && this.OTPVerificationdialog.isShowing()) {
                                    this.OTPVerificationdialog.dismiss();
                                }
                            }
                        } catch (JSONException e3) {
                            e3.printStackTrace();
                        }
                    }
                } else {
                    this.OTPEditText.setEnabled(true);
                    this.proceed.setEnabled(true);
                    if (event.getValue() != null) {
                        try {
                            Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, getString(C0360R.string.something_went_wrong), 1).show();
                    }
                    this.progressBarWaitOTP.setVisibility(8);
                }
            } else if (event.getType() == 42) {
                dismissProgress();
                if (event.getStatus()) {
                    JSONObject paymentDetailsObject = (JSONObject) event.getValue();
                    intent = new Intent(this, SdkHomeActivityNew.class);
                    this.map.put(SdkConstants.PAYMENT_TYPE, SdkConstants.LOAD_WALLET);
                    this.map.put(SdkConstants.INTERNAL_LOAD_WALLET_CALL, paymentDetailsObject.toString());
                    if (!this.fromPayUMoneyApp) {
                        this.map.put(SdkConstants.NEED_TO_SHOW_ONE_TAP_CHECK_BOX, "true");
                    }
                    intent.putExtra(SdkConstants.PARAMS, this.map);
                    if (EventBus.getDefault() != null && EventBus.getDefault().isRegistered(this)) {
                        EventBus.getDefault().unregister(this);
                    }
                    startActivityForResult(intent, 9);
                } else {
                    SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                }
            } else if (event.getType() == 27) {
                if (event.getStatus()) {
                    handleOneClickAndOneTapFeature((JSONObject) event.getValue());
                } else {
                    SharedPreferences mPref = getSharedPreferences(SdkConstants.SP_SP_NAME, 0);
                    Boolean oneClickPayment = Boolean.valueOf(false);
                    Boolean oneTapFeature = Boolean.valueOf(false);
                    if (mPref.contains(SdkConstants.CONFIG_DTO)) {
                        try {
                            JSONObject userConfigDto = new JSONObject(mPref.getString(SdkConstants.CONFIG_DTO, SdkConstants.XYZ_STRING));
                            if (!(userConfigDto == null || !userConfigDto.has(SdkConstants.ONE_CLICK_PAYMENT) || userConfigDto.isNull(SdkConstants.ONE_CLICK_PAYMENT))) {
                                oneClickPayment = Boolean.valueOf(userConfigDto.optBoolean(SdkConstants.ONE_CLICK_PAYMENT));
                            }
                        } catch (JSONException e32) {
                            e32.printStackTrace();
                        }
                    }
                    if (oneClickPayment.booleanValue()) {
                        this.mOneTap.setChecked(true);
                    } else {
                        this.mOneTap.setChecked(false);
                    }
                    if (!this.firstTimeFetchingOneClickFlag) {
                        SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                    }
                    this.firstTimeFetchingOneClickFlag = false;
                }
            } else if (event.getType() == 41) {
                this.verifyCouponProgress.setVisibility(4);
                if (event.getStatus()) {
                    try {
                        JSONObject manualCoupon = (JSONObject) event.getValue();
                        if (!manualCoupon.has("couponStringForUser") || manualCoupon.isNull("couponStringForUser")) {
                            this.mannualCouponEditText.setText("Coupon Added");
                        } else {
                            this.manualCouopnNameString = manualCoupon.getString("couponStringForUser");
                            this.mannualCouponEditText.setText(this.manualCouopnNameString + " Added");
                        }
                        if (manualCoupon.has("couponString") && !manualCoupon.isNull("couponString")) {
                            choosedCoupan = manualCoupon.getString("couponString");
                        }
                        if (!manualCoupon.has(SdkConstants.AMOUNT) || manualCoupon.isNull(SdkConstants.AMOUNT)) {
                            coupan_amt = 0.0d;
                        } else {
                            coupan_amt = manualCoupon.getDouble(SdkConstants.AMOUNT);
                        }
                        this.manualCouponEntered = true;
                    } catch (JSONException e322) {
                        e322.printStackTrace();
                        this.manualCouponEntered = false;
                        this.mannualCouponEditText.setText("Invalid Coupon");
                        SdkLogger.m14d(SdkConstants.TAG, "Invalid Coupon Entered");
                    }
                } else {
                    this.manualCouponEntered = false;
                    this.mannualCouponEditText.setText("Invalid Coupon");
                    SdkLogger.m14d(SdkConstants.TAG, "Invalid Coupon Entered");
                }
            }
            if (event.getType() == 40) {
                if (event.getStatus()) {
                    this.userParamsFetchedExplicitely = true;
                    this.user = (JSONObject) event.getValue();
                    initLayout();
                } else {
                    SdkLogger.m14d(SdkConstants.TAG, "Error fetching User Params");
                }
            }
            JSONObject jsonObject;
            if (event.getType() == 39) {
                if (event.getStatus()) {
                    try {
                        jsonObject = (JSONObject) event.getValue();
                        if (!jsonObject.has(SdkConstants.RESULT) || jsonObject.isNull(SdkConstants.RESULT)) {
                            SdkLogger.m14d(SdkConstants.TAG, "Error fetching Merchant Login Params");
                        } else {
                            JSONArray result = jsonObject.getJSONArray(SdkConstants.RESULT);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject object = result.getJSONObject(i);
                                String paramKey = object.optString(SdkConstants.PARAM_KEY, "");
                                String paramValue = object.optString(SdkConstants.PARAM_VALUE, "");
                                if (paramKey.equals(SdkConstants.OTP_LOGIN)) {
                                    this.quickLogin = paramValue;
                                } else if (paramKey.equals(SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_VALUE)) {
                                    if (SdkConstants.MERCHANT_PARAM_ALLOW_QUICK_GUEST_CHECKOUT.equals(paramValue)) {
                                        paramValue = SdkConstants.MERCHANT_PARAM_ALLOW_GUEST_CHECKOUT_ONLY;
                                    }
                                    this.allowGuestCheckout = paramValue;
                                }
                            }
                        }
                    } catch (JSONException e3222) {
                        e3222.printStackTrace();
                    }
                }
                dismissProgress();
                check_login();
            } else if (event.getType() == 2) {
                if (event.getValue() == null) {
                    return;
                }
                if (event.getValue().equals(SdkConstants.LOGOUT_FORCE)) {
                    Toast.makeText(this, C0360R.string.inactivity, 1).show();
                    Editor edit = getSharedPreferences(SdkConstants.SP_SP_NAME, 0).edit();
                    edit.clear();
                    edit.commit();
                    this.sdkSession.reset();
                    if (!this.mIsLoginInitiated) {
                        this.sdkSession.fetchMechantParams((String) this.map.get("merchantId"));
                    }
                } else if (SdkConstants.WALLET_SDK.booleanValue()) {
                    SdkHelper.dismissProgressDialog();
                    if (event.getStatus()) {
                        SdkHelper.showToastMessage(this, getString(C0360R.string.logout_success), false);
                        close(22);
                    } else if (SdkHelper.checkNetwork(this)) {
                        SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                    } else {
                        SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
                    }
                }
            } else if (event.getType() == 35) {
                SdkLogger.m14d(SdkConstants.TAG, "Entered in User Points");
            } else if (event.getType() == 5) {
                SdkLogger.m14d(SdkConstants.TAG, "Entered in Create Payment");
                if (event.getStatus()) {
                    try {
                        this.details = (JSONObject) event.getValue();
                        if (!(this.details == null || !this.details.has(SdkConstants.PAYMENT_ID) || this.details.isNull(SdkConstants.PAYMENT_ID))) {
                            this.paymentId = this.details.getString(SdkConstants.PAYMENT_ID);
                        }
                        if (!(!this.guestCheckOut || this.fromPayUMoneyApp || this.fromPayUBizzApp || this.mInternalLoadWalletCall)) {
                            String guestEmail = this.sdkSession.getGuestEmail();
                            this.sdkSession.updateTransactionDetails(this.paymentId, guestEmail);
                        }
                        startPayment(null);
                    } catch (Exception e42) {
                        dismissProgress();
                        e42.printStackTrace();
                    }
                    SdkLogger.m14d(SdkConstants.TAG, "exited from Create Payment");
                    return;
                }
                dismissProgress();
                try {
                    String responseString = (String) event.getValue();
                    if (responseString == null || responseString.isEmpty() || responseString.equals(SdkConstants.NULL_STRING)) {
                        Toast.makeText(this, "Some error occurred! Try again", 1).show();
                        return;
                    }
                    JSONObject responseObject = new JSONObject(responseString);
                    if (responseObject == null || !responseObject.has("message") || responseObject.isNull("message")) {
                        Toast.makeText(this, "Some error occurred! Try again", 1).show();
                        return;
                    }
                    String messageString = responseObject.getString("message");
                    if (messageString.contains(SdkConstants.PAYMENT_NOT_VALID)) {
                        Toast.makeText(this, messageString, 1).show();
                    } else {
                        Toast.makeText(this, "Some error occurred! Try again", 1).show();
                    }
                } catch (JSONException e32222) {
                    e32222.printStackTrace();
                }
            } else if (event.getType() == 44) {
                if (event.getStatus()) {
                    intent = new Intent();
                    status = null;
                    String paymentId = null;
                    try {
                        jsonObject = new JSONObject(event.getValue().toString()).getJSONObject(SdkConstants.RESULT);
                        if (!(jsonObject == null || !jsonObject.has(SdkConstants.PAYMENT_ID) || jsonObject.isNull(SdkConstants.PAYMENT_ID))) {
                            paymentId = jsonObject.getString(SdkConstants.PAYMENT_ID);
                        }
                        if (!(jsonObject == null || !jsonObject.has("status") || jsonObject.isNull("status"))) {
                            status = jsonObject.getString("status");
                        }
                    } catch (JSONException e322222) {
                        e322222.printStackTrace();
                    }
                    if (intent != null) {
                        intent.putExtra(SdkConstants.PAYMENT_ID, paymentId);
                        intent.putExtra(SdkConstants.RESULT, status);
                    }
                    if (status == null || !status.equalsIgnoreCase(SdkConstants.SUCCESS_STRING)) {
                        setResult(90, intent);
                    } else {
                        setResult(-1, intent);
                    }
                    finish();
                } else if (event.getValue() == null || !event.getValue().toString().equals(SdkConstants.INVALID_APP_VERSION)) {
                    intent = new Intent();
                    intent.putExtra(SdkConstants.RESULT, event.getValue().toString());
                    setResult(90, intent);
                    finish();
                }
            } else if (event.getType() == 8) {
                dismissProgress();
                if (event.getStatus()) {
                    SdkLogger.m20i("reached", "credit");
                    intent = new Intent(this, SdkWebViewActivityNew.class);
                    intent.putExtra(SdkConstants.RESULT, event.getValue().toString());
                    intent.putExtra("mode", this.mode);
                    if (this.mode.isEmpty()) {
                        if (this.cardHashForOneClickTxn != null) {
                            intent.putExtra(SdkConstants.CARD_HASH_FOR_ONE_CLICK_TXN, this.cardHashForOneClickTxn);
                        } else {
                            intent.putExtra(SdkConstants.CARD_HASH_FOR_ONE_CLICK_TXN, "0");
                        }
                    }
                    if (this.mOTPAutoRead) {
                        intent.putExtra(SdkConstants.OTP_AUTO_READ, true);
                    }
                    intent.putExtra(SdkConstants.MERCHANT_KEY, getIntent().getExtras().getString("key"));
                    intent.putExtra(SdkConstants.PAYMENT_ID, this.paymentId);
                    getClass();
                    startActivityForResult(intent, 2);
                } else if (!event.getValue().toString().equals(SdkConstants.INVALID_APP_VERSION)) {
                    SdkLogger.m20i("reached", "failed");
                    Toast.makeText(this, "Payment Failed", 1).show();
                }
            } else if (event.getType() == 50) {
                if (event.getStatus()) {
                    try {
                        if (((JSONObject) event.getValue()).getString("status").equals("0")) {
                            autoFillOTPForWalletCreation();
                            this.proceed.setEnabled(false);
                            this.proceed.setText("Activate");
                            this.resend.setVisibility(0);
                            this.OTPEditText.setVisibility(0);
                            this.humble.setVisibility(0);
                            this.progressBarWaitOTP.setVisibility(0);
                            this.info.setText("Waiting for OTP..");
                            Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                        } else if (event.getValue() != null) {
                            try {
                                Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                            } catch (Exception e5) {
                            }
                        } else {
                            Toast.makeText(this, "Something went wrong", 1).show();
                        }
                    } catch (Exception e6) {
                        if (event.getValue() != null) {
                            try {
                                Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                                return;
                            } catch (Exception e7) {
                                return;
                            }
                        }
                        Toast.makeText(this, "Something went wrong", 1).show();
                    }
                } else if (event.getValue() != null) {
                    try {
                        Toast.makeText(this, ((JSONObject) event.getValue()).getString("message"), 1).show();
                    } catch (Exception e8) {
                    }
                } else {
                    Toast.makeText(this, "Something went wrong", 1).show();
                }
            } else if (event.getType() == 52 && event.getStatus()) {
                this.mNetBankingStatusObject = (JSONObject) event.getValue();
            }
        }
    }

    private void showOtpVerifyDialog(String phoneNumber) {
        SdkQustomDialogBuilder alertDialog = new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog);
        View convertView = getLayoutInflater().inflate(C0360R.layout.sdk_otp_verify_layout, null);
        this.resend = (TextView) convertView.findViewById(C0360R.id.resend);
        this.info = (TextView) convertView.findViewById(C0360R.id.info);
        this.mobileEditText = (EditText) convertView.findViewById(C0360R.id.mobile);
        this.OTPEditText = (EditText) convertView.findViewById(C0360R.id.otp);
        this.proceed = (Button) convertView.findViewById(C0360R.id.activate);
        this.anotherAccountButton = (Button) convertView.findViewById(C0360R.id.logout);
        this.humble = (RelativeLayout) convertView.findViewById(C0360R.id.humble);
        this.progressBarWaitOTP = (ProgressBar) convertView.findViewById(C0360R.id.pbwaitotp);
        if (phoneNumber != null) {
            this.mobileEditText.setText(phoneNumber);
        }
        this.humble.setVisibility(8);
        this.proceed.setEnabled(true);
        this.proceed.setText(getString(C0360R.string.proceed));
        this.OTPEditText.setVisibility(8);
        this.humble.setVisibility(8);
        this.progressBarWaitOTP.setVisibility(8);
        this.resend.setVisibility(8);
        this.info.setText(getString(C0360R.string.please_verify_number));
        this.resend.setOnClickListener(new C03749());
        this.anotherAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SdkHomeActivityNew.this.OTPVerificationdialog != null && SdkHomeActivityNew.this.OTPVerificationdialog.isShowing()) {
                    SdkHomeActivityNew.this.OTPVerificationdialog.dismiss();
                }
                SdkHomeActivityNew.this.logout();
            }
        });
        this.proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!SdkHelper.checkNetwork(SdkHomeActivityNew.this)) {
                    SdkHelper.showToastMessage(SdkHomeActivityNew.this, SdkHomeActivityNew.this.getString(C0360R.string.connect_to_internet), true);
                } else if (SdkHomeActivityNew.this.mobileEditText.getText().toString().equalsIgnoreCase("") || SdkHomeActivityNew.this.mobileEditText.getText().toString().charAt(0) < '6') {
                    Toast.makeText(SdkHomeActivityNew.this, "Please enter valid number", 1).show();
                } else if (SdkHomeActivityNew.this.proceed.getText().toString().equalsIgnoreCase(SdkHomeActivityNew.this.getString(C0360R.string.proceed))) {
                    SdkHomeActivityNew.this.sdkSession.sendMobileVerificationCodeForWalletCreation(SdkHomeActivityNew.this.mobileEditText.getText().toString());
                } else if (SdkHomeActivityNew.this.proceed.getText().toString().equalsIgnoreCase(SdkHomeActivityNew.this.getString(C0360R.string.activate)) && !SdkHomeActivityNew.this.OTPEditText.getText().toString().equalsIgnoreCase("") && !SdkHomeActivityNew.this.mobileEditText.getText().toString().equalsIgnoreCase("")) {
                    SdkHomeActivityNew.this.info.setText(SdkHomeActivityNew.this.getString(C0360R.string.activating));
                    SdkHomeActivityNew.this.progressBarWaitOTP.setVisibility(0);
                    SdkHomeActivityNew.this.mobileEditText.setEnabled(false);
                    SdkHomeActivityNew.this.OTPEditText.setEnabled(false);
                    ((Button) v).setEnabled(false);
                    SdkHomeActivityNew.this.sdkSession.createWallet(SharedPrefsUtils.getStringPreference(SdkHomeActivityNew.this, "email"), SdkHomeActivityNew.this.mobileEditText.getText().toString(), SdkHomeActivityNew.this.OTPEditText.getText().toString());
                } else if (SdkHomeActivityNew.this.OTPEditText.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SdkHomeActivityNew.this, SdkHomeActivityNew.this.getString(C0360R.string.waiting_for_otp), 1).show();
                }
            }
        });
        this.OTPEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 6) {
                    SdkHomeActivityNew.this.progressBarWaitOTP.setVisibility(8);
                    SdkHomeActivityNew.this.info.setText("Verify OTP");
                    SdkHomeActivityNew.this.proceed.setEnabled(true);
                    SdkHomeActivityNew.this.hideKeyboardIfShown(SdkHomeActivityNew.this.OTPEditText);
                    return;
                }
                SdkHomeActivityNew.this.progressBarWaitOTP.setVisibility(0);
                SdkHomeActivityNew.this.info.setText("Waiting for OTP..");
                SdkHomeActivityNew.this.proceed.setEnabled(false);
            }
        });
        alertDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == 1 && keyCode == 4) {
                    SdkHomeActivityNew sdkHomeActivityNew = SdkHomeActivityNew.this;
                    sdkHomeActivityNew.count++;
                    if (SdkHomeActivityNew.this.count % 2 == 0) {
                        SdkHomeActivityNew.this.count = 0;
                        SdkHomeActivityNew.this.close(21);
                        SdkHomeActivityNew.this.sdkSession.notifyUserCancelledTransaction(SdkHomeActivityNew.this.paymentId, "1");
                    } else {
                        Toast.makeText(SdkHomeActivityNew.this.getApplicationContext(), "This merchant supports only wallet as payment mode and your wallet is not active. Press Back again to cancel transaction.", 1).show();
                        return true;
                    }
                }
                return false;
            }
        });
        this.OTPVerificationdialog = alertDialog.setView(convertView).show();
        this.OTPVerificationdialog.setCanceledOnTouchOutside(false);
    }

    public void autoFillOTPForWalletCreation() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String msgBody = null;
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras();
                    if (bundle == null || SdkHomeActivityNew.this.OTPEditText == null) {
                        Toast.makeText(SdkHomeActivityNew.this, "Couldn't read sms, please enter OTP manually", 1).show();
                        SdkHomeActivityNew.this.OTPEditText.requestFocus();
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
                            Matcher m = SdkHomeActivityNew.this.otpPattern.matcher(msgBody);
                            if (m.find()) {
                                SdkHomeActivityNew.this.OTPEditText.setText(m.group(0));
                                return;
                            }
                            Toast.makeText(SdkHomeActivityNew.this, "Couldn't read sms, please enter OTP manually", 1).show();
                            SdkHomeActivityNew.this.OTPEditText.requestFocus();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SdkHomeActivityNew.this, "Couldn't read sms, please enter OTP manually", 1).show();
                        SdkHomeActivityNew.this.OTPEditText.requestFocus();
                    }
                }
            }
        };
        registerReceiver(this.receiver, filter);
    }

    private void checkForUserWalletActive() {
        try {
            if (!this.mOnlyWalletPaymentModeActive && this.mFrontPaymentModesEnabled) {
                calculateOffersAndCashback();
            } else if (this.details.has(SdkConstants.USER) && !this.details.isNull(SdkConstants.USER)) {
                this.user = this.details.getJSONObject(SdkConstants.USER);
                if (!this.user.has(SdkConstants.WALLET) || !this.user.isNull(SdkConstants.WALLET)) {
                    calculateOffersAndCashback();
                } else if (!this.user.has("phone") || this.user.isNull("phone")) {
                    userWalletNotRegisteredDialog();
                } else {
                    showOtpVerifyDialog(this.user.getString("phone"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkForWalletOnlyPaymentMode() {
        try {
            if (this.details != null && this.details.has(SdkConstants.CONFIG_DATA) && !this.details.isNull(SdkConstants.CONFIG_DATA)) {
                JSONObject configDataJsonObject = this.details.getJSONObject(SdkConstants.CONFIG_DATA);
                if (configDataJsonObject.has(SdkConstants.MERCHANT_CATEGORY_TYPE) && !configDataJsonObject.isNull(SdkConstants.MERCHANT_CATEGORY_TYPE) && SdkConstants.ONLY_WALLET_PAYMENT.equals(configDataJsonObject.getString(SdkConstants.MERCHANT_CATEGORY_TYPE))) {
                    this.mOnlyWalletPaymentModeActive = true;
                }
                if (configDataJsonObject.has(SdkConstants.OTP_AUTO_READ) && !configDataJsonObject.isNull(SdkConstants.OTP_AUTO_READ)) {
                    this.mOTPAutoRead = configDataJsonObject.optBoolean(SdkConstants.OTP_AUTO_READ, false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.mOnlyWalletPaymentModeActive = false;
        }
    }

    private void calculateOffersAndCashback() {
        if (this.details != null) {
            try {
                if (this.details.has(SdkConstants.MERCHANT) && !this.details.isNull(SdkConstants.MERCHANT)) {
                    JSONObject merchant = this.details.getJSONObject(SdkConstants.MERCHANT);
                    if (merchant.has(SdkConstants.OFFER) && !merchant.isNull(SdkConstants.OFFER)) {
                        JSONObject tempDiscount = merchant.getJSONObject(SdkConstants.OFFER);
                        if (tempDiscount.has(SdkConstants.OFFER_TYPE) && !tempDiscount.isNull(SdkConstants.OFFER_TYPE)) {
                            String tempOfferType = tempDiscount.optString(SdkConstants.OFFER_TYPE, "Blank");
                            String temp = tempDiscount.optString(SdkConstants.OFFER_AMOUNT, "0.0");
                            if (tempOfferType.equals(SdkConstants.DISCOUNT)) {
                                this.discount = Double.parseDouble(temp);
                                this.cashback = 0.0d;
                            } else if (tempOfferType.equals(SdkConstants.CASHBACK)) {
                                this.cashback = Double.parseDouble(temp);
                                this.discount = 0.0d;
                            }
                        }
                    }
                }
                if (this.details.has(SdkConstants.USER) && !this.details.isNull(SdkConstants.USER)) {
                    if (!this.userParamsFetchedExplicitely) {
                        this.user = this.details.getJSONObject(SdkConstants.USER);
                    }
                    if (!(this.details == null || !this.details.has(SdkConstants.PAYMENT_ID) || this.details.isNull(SdkConstants.PAYMENT_ID))) {
                        this.paymentId = this.details.getString(SdkConstants.PAYMENT_ID);
                    }
                    if (this.user.has("userId") && !this.user.isNull("userId")) {
                        this.userId = this.user.getString("userId");
                    }
                    if (this.paymentOption == null && this.details.has(SdkConstants.PAYMENT_OPTION) && !this.details.isNull(SdkConstants.PAYMENT_OPTION)) {
                        JSONObject tempPaymentOption = this.details.getJSONObject(SdkConstants.PAYMENT_OPTION);
                        if (!(tempPaymentOption == null || !tempPaymentOption.has(SdkConstants.OPTIONS) || tempPaymentOption.isNull(SdkConstants.OPTIONS))) {
                            this.paymentOption = tempPaymentOption.getJSONObject(SdkConstants.OPTIONS);
                        }
                    }
                    if (!(this.mOnlyWalletPaymentModeActive || this.paymentOption == null || !checkForPaymentModeActive(SdkConstants.POINTS))) {
                        this.pointsActive = true;
                    }
                    if (this.paymentOption != null && checkForPaymentModeActive(SdkConstants.WALLET)) {
                        this.walletActive = true;
                    }
                    if (!this.pointsActive) {
                        this.userPoints = 0.0d;
                    } else if (this.user.has(SdkConstants.POINTS) && !this.user.isNull(SdkConstants.POINTS)) {
                        this.userPoints = this.user.getJSONObject(SdkConstants.POINTS).optDouble(SdkConstants.AMOUNT, 0.0d);
                    }
                    if (this.walletActive && this.user.has(SdkConstants.WALLET) && !this.user.isNull(SdkConstants.WALLET)) {
                        this.walletJason = this.user.getJSONObject(SdkConstants.WALLET);
                        if (this.walletJason.has(SdkConstants.AMOUNT) && !this.walletJason.isNull(SdkConstants.AMOUNT)) {
                            this.walletAmount = this.walletJason.optDouble(SdkConstants.AMOUNT, 0.0d);
                            this.mIsUserWalletBlocked = this.walletJason.optInt("status", 2) == 0;
                        }
                        this.walletBal = this.walletAmount;
                        if (this.walletAmount > 0.0d) {
                            showWalletCheckBox();
                        }
                    }
                } else if (this.fromPayUBizzApp && !this.sdkSession.isLoggedIn()) {
                }
                if (this.mWalletRecentlyVerified) {
                    startPayment(null);
                    return;
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Something Went Wrong", 0).show();
    }

    private void showWalletCheckBox() {
        this.walletCheck.setVisibility(0);
        this.walletBoxLayout.setVisibility(0);
        this.walletBalance.setText(getString(C0360R.string.remaining_wallet_bal) + " " + round(this.walletAmount));
    }

    public void onBackPressed() {
        if (this.mProgress) {
            this.mProgress = false;
            return;
        }
        this.count++;
        if (this.count % 2 == 0) {
            this.count = 0;
            close(21);
            this.sdkSession.notifyUserCancelledTransaction(this.paymentId, "1");
            return;
        }
        Toast.makeText(getApplicationContext(), "Press Back again to cancel transaction.", 0).show();
    }

    public void close() {
        Intent intent = new Intent();
        intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
        setResult(8, intent);
        finish();
    }

    public void close(int resultCode) {
        Intent intent = new Intent();
        if (resultCode == 22) {
            intent.putExtra(SdkConstants.IS_LOGOUT_CALL, true);
            setResult(0, intent);
        } else if (resultCode == 21) {
            intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
            setResult(0, intent);
        }
        finish();
    }

    public void onDestroy() {
        coupan_amt = 0.0d;
        super.onDestroy();
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
        if (EventBus.getDefault() != null && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void unchecked() {
        this.walletUsage = 0.0d;
        this.walletBal = this.walletAmount;
        updateDetails(this.mode);
        if (this.walletFlag && this.payByWalletButton.isShown()) {
            this.payByWalletButton.setVisibility(8);
            this.paymentModesList.setVisibility(0);
        }
    }

    public void walletDialog() {
        this.amt_net = this.walletUsage;
        if (coupan_amt > 0.0d) {
            this.amt_discount = coupan_amt;
        }
        if (this.userPoints > 0.0d) {
            showWalletwithPayu(this.userPoints, this.amt_discount, this.amt_net);
        } else if (this.userPoints == 0.0d) {
            showWallet(this.amt_discount, this.amt_net);
        } else {
            Toast.makeText(this, "Something went Wrong", 1).show();
        }
    }

    public void loadWalletDialog() {
        if (!(this.mWalletRecentlyVerified || this.walletJason == null || !this.walletJason.has("minLimit") || this.walletJason.isNull("minLimit"))) {
            this.loadWalletMinLimit = this.walletJason.optDouble("minLimit", 0.0d);
        }
        this.loadWalletMinLimit = Math.max(this.loadWalletMinLimit, this.amt_net);
        if (!(this.mWalletRecentlyVerified || this.walletJason == null || !this.walletJason.has("maxLimit") || this.walletJason.isNull("maxLimit"))) {
            this.loadWalletMaxLimit = this.walletJason.optDouble("maxLimit", 10000.0d);
        }
        String walletBalanceInfoForPaymentString = "You can load a minimum of ₹ " + round(this.loadWalletMinLimit) + " and maximum of ₹ " + round(this.loadWalletMaxLimit) + " to your wallet. ₹ " + round(this.amt_net) + " would be auto-debited from your wallet balance";
        SdkQustomDialogBuilder alertDialog = new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog);
        View convertView = getLayoutInflater().inflate(C0360R.layout.sdk_load_wallet_and_pay_layout, null);
        ((TextView) convertView.findViewById(C0360R.id.wallet_balance_info_for_payment)).setText(walletBalanceInfoForPaymentString);
        final EditText loadWalletAmountEditText = (EditText) convertView.findViewById(C0360R.id.load_wallet_amount_editText);
        Button loadWalletButton = (Button) convertView.findViewById(C0360R.id.load_wallet_button);
        Button backToHomeButton = (Button) convertView.findViewById(C0360R.id.back_to_home_button);
        if (this.amt_net > this.loadWalletMaxLimit) {
            ((TextView) convertView.findViewById(C0360R.id.insufficient_wallet_balance_message_textView)).setText("We are Sorry");
            walletBalanceInfoForPaymentString = "The transaction amount is greater than the maximum load amount (₹ " + this.loadWalletMaxLimit + ") for this mode of payment.";
            if (this.mIsUserWalletBlocked) {
                walletBalanceInfoForPaymentString = "Your PayUmoney Wallet is blocked.";
            }
            convertView.findViewById(C0360R.id.load_wallet_container_layout).setVisibility(8);
            ((TextView) convertView.findViewById(C0360R.id.wallet_balance_info_for_payment)).setVisibility(8);
            ((TextView) convertView.findViewById(C0360R.id.transaction_amount_greater_message)).setVisibility(0);
            ((TextView) convertView.findViewById(C0360R.id.transaction_amount_greater_message)).setText(walletBalanceInfoForPaymentString);
            backToHomeButton.setVisibility(0);
        }
        loadWalletAmountEditText.setText(round(this.loadWalletMinLimit) + "");
        loadWalletAmountEditText.setSelection(loadWalletAmountEditText.getText().toString().length());
        loadWalletAmountEditText.addTextChangedListener(new LoadWalletAmountEditTextTextWatcher(loadWalletAmountEditText, loadWalletButton, this.loadWalletMinLimit));
        final AlertDialog dialog = alertDialog.setView(convertView).show();
        loadWalletButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SdkHomeActivityNew.this.initiateProgressDialog();
                if (SdkHomeActivityNew.this.fromPayUMoneyApp) {
                    SdkHomeActivityNew.this.sdkSession.loadWallet(null, loadWalletAmountEditText.getText().toString(), SdkHomeActivityNew.this.paymentId, "PayUmoney App");
                } else if (SdkHomeActivityNew.this.fromPayUBizzApp) {
                    SdkHomeActivityNew.this.sdkSession.loadWallet(null, loadWalletAmountEditText.getText().toString(), SdkHomeActivityNew.this.paymentId, "PayUBizz App");
                } else {
                    SdkHomeActivityNew.this.sdkSession.loadWallet(null, loadWalletAmountEditText.getText().toString(), SdkHomeActivityNew.this.paymentId, (String) SdkHomeActivityNew.this.map.get(SdkConstants.PRODUCT_INFO));
                }
                dialog.dismiss();
            }
        });
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                SdkHomeActivityNew.this.close(21);
                SdkHomeActivityNew.this.sdkSession.notifyUserCancelledTransaction(SdkHomeActivityNew.this.paymentId, "1");
            }
        });
    }

    public void userWalletNotRegisteredDialog() {
        String walletBalanceInfoForPaymentString = "User wallet is not active and registered phone number not found. Please contact PayUmoney Customer care.";
        SdkQustomDialogBuilder alertDialog = new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog);
        View convertView = getLayoutInflater().inflate(C0360R.layout.sdk_load_wallet_and_pay_layout, null);
        ((TextView) convertView.findViewById(C0360R.id.wallet_balance_info_for_payment)).setText(walletBalanceInfoForPaymentString);
        Button backToHomeButton = (Button) convertView.findViewById(C0360R.id.back_to_home_button);
        ((TextView) convertView.findViewById(C0360R.id.insufficient_wallet_balance_message_textView)).setText("We are Sorry");
        convertView.findViewById(C0360R.id.load_wallet_container_layout).setVisibility(8);
        ((TextView) convertView.findViewById(C0360R.id.wallet_balance_info_for_payment)).setVisibility(8);
        ((TextView) convertView.findViewById(C0360R.id.transaction_amount_greater_message)).setVisibility(0);
        ((TextView) convertView.findViewById(C0360R.id.transaction_amount_greater_message)).setText(walletBalanceInfoForPaymentString);
        backToHomeButton.setVisibility(0);
        final AlertDialog dialog = alertDialog.setView(convertView).show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                SdkHomeActivityNew.this.close(21);
                SdkHomeActivityNew.this.sdkSession.notifyUserCancelledTransaction(SdkHomeActivityNew.this.paymentId, "1");
            }
        });
    }

    public void setAmountConvenience(String currentPaymentMode) throws JSONException {
        if (currentPaymentMode.equals(SdkConstants.WALLET_STRING) || currentPaymentMode.isEmpty()) {
            this.amt_convenience = 0.0d;
        } else if (this.convenienceChargesObject == null || !this.convenienceChargesObject.has(this.mode) || this.convenienceChargesObject.isNull(this.mode)) {
            this.amt_convenience = 0.0d;
        } else {
            this.amt_convenience = this.convenienceChargesObject.getJSONObject(this.mode).getDouble(SdkConstants.DEFAULT);
        }
        if (this.convenienceChargesObject != null && this.convenienceChargesObject.has(SdkConstants.WALLET_STRING) && !this.convenienceChargesObject.isNull(SdkConstants.WALLET_STRING)) {
            this.amt_convenience_wallet = this.convenienceChargesObject.getJSONObject(SdkConstants.WALLET_STRING).getDouble(SdkConstants.DEFAULT);
        }
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void initiateProgressDialog() {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = showProgress(this);
        } else if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
    }

    public void showWallet(double dsc, final double net) {
        new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog).setTitleColor(SdkConstants.WHITE).setDividerColor(SdkConstants.greenPayU).setTitle((CharSequence) "Payment using Wallet").setMessage("Yoo-hoo!\n\nYou have enough money in PayUMoney Wallet for this transaction. All you need to do is confirm the payment by clicking on the OK button below and that's it.\n\nWallet Money Used : Rs." + round(net) + "\nRemaining Money in Wallet : Rs." + round(this.walletBal)).setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (SdkHelper.checkNetwork(SdkHomeActivityNew.this)) {
                    try {
                        SdkHomeActivityNew.this.mProgressDialog.show();
                        SdkSession.getInstance(SdkHomeActivityNew.this).sendToPayU(SdkHomeActivityNew.this.details, SdkConstants.WALLET, SdkHomeActivityNew.this.data, Double.valueOf(net), Double.valueOf(SdkHomeActivityNew.this.discount), Double.valueOf(SdkHomeActivityNew.this.amt_convenience));
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Toast.makeText(SdkHomeActivityNew.this, C0360R.string.disconnected_from_internet, 0).show();
            }
        }).setCancelable(false).show();
    }

    public void showWalletwithPayu(double pnts, double dsc, double net) {
        final double d = net;
        final double d2 = pnts;
        new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog).setTitleColor(SdkConstants.WHITE).setDividerColor(SdkConstants.greenPayU).setTitle((CharSequence) "Payment using Wallet").setMessage("Yoo-hoo!\n\nYou have enough money in PayUMoney Wallet for this transaction. All you need to do is confirm the payment by clicking on the OK button below and that's it.\n\nWallet Money Used : Rs." + round(net) + "\nRemaining Money in Wallet : Rs." + round(this.walletBal)).setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (SdkHelper.checkNetwork(SdkHomeActivityNew.this)) {
                    try {
                        SdkHomeActivityNew.this.mProgressDialog.show();
                        SdkSession.getInstance(SdkHomeActivityNew.this).sendToPayUWithWallet(SdkHomeActivityNew.this.details, SdkConstants.WALLET, SdkHomeActivityNew.this.data, Double.valueOf(d), Double.valueOf(d2), Double.valueOf(SdkHomeActivityNew.this.discount), Double.valueOf(SdkHomeActivityNew.this.amt_convenience));
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Toast.makeText(SdkHomeActivityNew.this, C0360R.string.disconnected_from_internet, 0).show();
            }
        }).setCancelable(false).show();
    }

    public void pointDialog() {
        dismissProgress();
        this.paymentModesList.setVisibility(4);
        new SdkQustomDialogBuilder(this, C0360R.style.PauseDialog).setTitle((CharSequence) "Payment using PayUMoney points").setTitleColor(SdkConstants.WHITE).setDividerColor(SdkConstants.greenPayU).setMessage((CharSequence) "Yoo-hoo!\n\nYou have enough PayUMoney points for this transaction. All you need to do is confirm the payment by clicking on the OK button below and that's it").setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SdkHomeActivityNew.this.mAmount.setText("0.0");
                SdkHomeActivityNew.this.savings.setText("Sufficient PayUPoints");
                SdkHomeActivityNew.this.walletBoxLayout.setVisibility(8);
                SdkHomeActivityNew.this.mOrderSummary.setVisibility(8);
                SdkHomeActivityNew.this.payByWalletButton.setVisibility(8);
                SdkHomeActivityNew.this.couponLayout.setVisibility(8);
                SdkHomeActivityNew.this.mOneTap.setVisibility(8);
                SdkHomeActivityNew.this.mCvvTnCLink.setVisibility(8);
                SdkPayUMoneyPointsFragment fragment = new SdkPayUMoneyPointsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("details", SdkHomeActivityNew.this.details.toString());
                bundle.putDouble("userPoints", SdkHomeActivityNew.this.userPoints);
                bundle.putDouble("discount", SdkHomeActivityNew.this.discount);
                bundle.putDouble("cashback", SdkHomeActivityNew.this.cashback);
                bundle.putDouble("couponAmount", SdkHomeActivityNew.coupan_amt);
                bundle.putDouble("convenienceChargesAmount", SdkHomeActivityNew.this.amt_convenience_wallet);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = SdkHomeActivityNew.this.getFragmentManager().beginTransaction().setCustomAnimations(C0360R.animator.card_flip_right_in, C0360R.animator.card_flip_right_out, C0360R.animator.card_flip_left_in, C0360R.animator.card_flip_left_out);
                transaction.replace(C0360R.id.pagerContainer, fragment, "payumoneypoints");
                transaction.addToBackStack("a");
                transaction.commit();
                SdkHomeActivityNew.this.getFragmentManager().executePendingTransactions();
            }
        }).setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == 4 && SdkHomeActivityNew.this.getFragmentManager().findFragmentByTag(SdkConstants.PAYMENT_OPTION) != null) {
                    Intent intent = new Intent();
                    intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
                    SdkHomeActivityNew.this.setResult(0, intent);
                    SdkHomeActivityNew.this.finish();
                }
                return true;
            }
        }).setCancelable(false).show();
    }

    public static BigDecimal round(float d) {
        return new BigDecimal(Float.toString(d)).setScale(2, 4);
    }

    public static BigDecimal round(double d) {
        return new BigDecimal(Double.toString(d)).setScale(2, 4);
    }

    public ProgressDialog showProgress(Context context) {
        this.mProgress = true;
        final Drawable[] drawables = new Drawable[]{getResources().getDrawable(C0360R.drawable.nopoint_green), getResources().getDrawable(C0360R.drawable.onepoint_green), getResources().getDrawable(C0360R.drawable.twopoint_green), getResources().getDrawable(C0360R.drawable.threepoint_green)};
        View layout = LayoutInflater.from(context).inflate(C0360R.layout.sdk_prog_dialog, null);
        final ImageView imageView = (ImageView) layout.findViewById(C0360R.id.imageView);
        ProgressDialog progDialog = new ProgressDialog(context, C0360R.style.ProgressDialog);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int f6i = -1;

            class C03621 implements Runnable {
                C03621() {
                }

                public void run() {
                    AnonymousClass22 anonymousClass22 = AnonymousClass22.this;
                    anonymousClass22.f6i++;
                    if (AnonymousClass22.this.f6i >= drawables.length) {
                        AnonymousClass22.this.f6i = 0;
                    }
                    imageView.setImageBitmap(null);
                    imageView.destroyDrawingCache();
                    imageView.refreshDrawableState();
                    imageView.setImageDrawable(drawables[AnonymousClass22.this.f6i]);
                }
            }

            public synchronized void run() {
                SdkHomeActivityNew.this.runOnUiThread(new C03621());
            }
        }, 0, 500);
        progDialog.show();
        progDialog.setContentView(layout);
        progDialog.setCancelable(false);
        progDialog.setCanceledOnTouchOutside(false);
        return progDialog;
    }

    private void dismissProgress() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgress = false;
        }
    }

    private void updateWalletDetails() {
        if (this.walletCheck.isChecked()) {
            if (((this.amount + this.amt_convenience_wallet) - this.userPoints) - this.amt_discount <= this.walletAmount && ((this.amount + this.amt_convenience_wallet) - this.userPoints) - this.amt_discount >= 0.0d) {
                this.amt_convenience = this.amt_convenience_wallet;
                this.walletUsage = ((this.amount + this.amt_convenience) - this.userPoints) - this.amt_discount;
                this.walletBal = this.walletAmount - this.walletUsage;
                updateDetails(SdkConstants.WALLET_STRING);
                this.payByWalletButton.setVisibility(0);
                this.mOneTap.setVisibility(8);
                this.mCvvTnCLink.setVisibility(8);
                this.paymentModesList.setVisibility(8);
                this.walletFlag = true;
            } else if (((this.amount + this.amt_convenience) - this.userPoints) - this.amt_discount > this.walletAmount && ((this.amount + this.amt_convenience_wallet) - this.userPoints) - this.amt_discount > 0.0d) {
                this.walletUsage = this.walletAmount;
                this.walletBal = 0.0d;
                updateDetails(this.mode);
                this.walletFlag = false;
                this.payByWalletButton.setVisibility(8);
                if (!(this.mOneTap == null || this.mOneTap.getVisibility() == 0 || (!this.mode.equals(SdkConstants.PAYMENT_MODE_CC) && !this.mode.equals(SdkConstants.PAYMENT_MODE_DC) && !this.mode.isEmpty()))) {
                    this.mOneTap.setVisibility(0);
                    this.mCvvTnCLink.setVisibility(0);
                }
                this.paymentModesList.setVisibility(0);
            } else if (((this.amount + this.amt_convenience_wallet) - this.userPoints) - this.amt_discount > 0.0d) {
                this.amt_convenience = this.amt_convenience_wallet;
                this.walletUsage = ((this.amount + this.amt_convenience) - this.userPoints) - this.amt_discount;
                this.walletBal = this.walletAmount - this.walletUsage;
                updateDetails(SdkConstants.WALLET_STRING);
                this.payByWalletButton.setVisibility(0);
                this.mOneTap.setVisibility(8);
                this.mCvvTnCLink.setVisibility(8);
                this.paymentModesList.setVisibility(8);
                this.walletFlag = true;
            } else if (((this.amount + this.amt_convenience_wallet) - this.userPoints) - this.amt_discount <= 0.0d) {
                this.walletUsage = 0.0d;
                this.walletBal = this.walletAmount - this.walletUsage;
                updateDetails(SdkConstants.WALLET_STRING);
                this.walletFlag = false;
            }
            this.walletBalance.setText(getString(C0360R.string.remaining_wallet_bal) + " " + round(this.walletBal));
        }
    }

    private void handleOneClickAndOneTapFeature(JSONObject userDto) {
        Editor editor = getSharedPreferences(SdkConstants.SP_SP_NAME, 0).edit();
        try {
            if (userDto.has(SdkConstants.CONFIG_DTO) && !userDto.isNull(SdkConstants.CONFIG_DTO)) {
                JSONObject userConfigDtoTmp = userDto.getJSONObject(SdkConstants.CONFIG_DTO);
                String salt = PayUmoneySdkInitilizer.IsDebugMode().booleanValue() ? SdkConstants.AUTHORIZATION_SALT_TEST : SdkConstants.AUTHORIZATION_SALT_PROD;
                if (userConfigDtoTmp.has(SdkConstants.AUTHORIZATION_SALT) && !userConfigDtoTmp.isNull(SdkConstants.AUTHORIZATION_SALT)) {
                    if (salt.equals(userConfigDtoTmp.optString(SdkConstants.AUTHORIZATION_SALT, SdkConstants.XYZ_STRING))) {
                        editor.putBoolean(SdkConstants.ONE_CLICK_PAYMENT, false);
                        editor.putBoolean(SdkConstants.ONE_TAP_FEATURE, false);
                    } else {
                        editor.putBoolean(SdkConstants.ONE_CLICK_PAYMENT, userConfigDtoTmp.optBoolean(SdkConstants.ONE_CLICK_PAYMENT, false));
                        editor.putString(SdkConstants.CONFIG_DTO, userConfigDtoTmp.toString());
                        if (userConfigDtoTmp.has(SdkConstants.ONE_TAP_FEATURE) && !userConfigDtoTmp.isNull(SdkConstants.ONE_TAP_FEATURE)) {
                            boolean temp = userConfigDtoTmp.optBoolean(SdkConstants.ONE_TAP_FEATURE, false);
                            editor.putBoolean(SdkConstants.ONE_TAP_FEATURE, temp);
                            changeDebitCardCheckBoxLable(Boolean.valueOf(temp));
                            if (temp) {
                                this.mOneTap.setChecked(true);
                            } else {
                                this.mOneTap.setChecked(false);
                            }
                            if (this.firstTimeFetchingOneClickFlag) {
                                this.firstTimeFetchingOneClickFlag = false;
                            }
                        }
                    }
                }
            }
            editor.commit();
            editor.apply();
        } catch (Exception e) {
            editor.putBoolean(SdkConstants.ONE_TAP_FEATURE, false);
            editor.putBoolean(SdkConstants.ONE_CLICK_PAYMENT, false);
            this.mOneTap.setChecked(false);
            editor.commit();
            editor.apply();
            e.printStackTrace();
        }
    }

    private void changeDebitCardCheckBoxLable(Boolean oneTap) {
        View v = findViewById(C0360R.id.pagerContainer);
        if (v != null) {
            CheckBox c = (CheckBox) v.findViewById(C0360R.id.store_card);
            TextView t = (TextView) v.findViewById(C0360R.id.sdk_tnc);
            if (c != null && t != null) {
                if (oneTap.booleanValue()) {
                    c.setText("");
                    t.setVisibility(0);
                    return;
                }
                c.setText("Save this card");
                t.setVisibility(8);
            }
        }
    }
}
