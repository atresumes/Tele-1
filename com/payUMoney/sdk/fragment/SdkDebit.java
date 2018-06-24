package com.payUMoney.sdk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.SdkLuhn;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.SdkSetupCardDetails;
import com.payUMoney.sdk.dialog.SdkCustomDatePicker;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONException;

public class SdkDebit extends View {
    Drawable calenderDrawable = null;
    private String cardNumber = "";
    Drawable cardNumberDrawable = null;
    private EditText cardNumberEditText = null;
    Boolean card_store_check = Boolean.TRUE;
    private String cvv = "";
    Drawable cvvDrawable = null;
    private EditText cvvEditText = null;
    View debitCardDetails = null;
    private EditText expiryDatePickerEditText = null;
    private int expiryMonth = 7;
    private int expiryYear = 2025;
    Boolean isCardNumberValid = Boolean.FALSE;
    Boolean isCvvValid = Boolean.FALSE;
    Boolean isExpired = Boolean.TRUE;
    private boolean isMaestro = false;
    MakePaymentListener mCallback = null;
    private EditText mCardLabel = null;
    public CheckBox mCardStore = null;
    Context mContext;
    SdkCustomDatePicker mDatePicker = null;
    int mDay = 0;
    int mMonth = 0;
    int mYear = 0;
    public TextView sdkTnc;

    class C03911 implements TextWatcher {
        private static final char space = ' ';

        C03911() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            SdkDebit.this.cardNumber = ((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText)).getText().toString();
            SdkDebit.this.cardNumber = SdkDebit.this.cardNumber.replace(" ", "");
            if (SdkDebit.this.cardNumber.startsWith("34") || SdkDebit.this.cardNumber.startsWith("37")) {
                ((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).setFilters(new InputFilter[]{new LengthFilter(4)});
            } else {
                ((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).setFilters(new InputFilter[]{new LengthFilter(3)});
            }
            String tempIssuer = SdkSetupCardDetails.findIssuer(SdkDebit.this.cardNumber, SdkConstants.PAYMENT_MODE_DC);
            if (tempIssuer == null || !tempIssuer.equals("MAES")) {
                SdkDebit.this.isMaestro = false;
                SdkDebit.this.debitCardDetails.findViewById(C0360R.id.expiryCvvLinearLayout).setVisibility(0);
                if (SdkDebit.this.cardNumber.length() <= 11 || !SdkLuhn.validate(SdkDebit.this.cardNumber)) {
                    SdkDebit.this.isCardNumberValid = Boolean.FALSE;
                    SdkDebit.this.invalid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText), SdkDebit.this.cardNumberDrawable);
                    SdkDebit.this.cardNumberDrawable.setAlpha(100);
                    SdkDebit.this.mCallback.modifyConvenienceCharges(null);
                    return;
                }
                SdkDebit.this.isCardNumberValid = Boolean.TRUE;
                SdkDebit.this.valid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText), SdkSetupCardDetails.getCardDrawable(SdkDebit.this.getResources(), SdkDebit.this.cardNumber));
                SdkDebit.this.mCallback.modifyConvenienceCharges(tempIssuer);
            } else if (SdkDebit.this.cardNumber.length() <= 11 || !SdkLuhn.validate(SdkDebit.this.cardNumber)) {
                SdkDebit.this.isCardNumberValid = Boolean.FALSE;
                SdkDebit.this.invalid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText), SdkDebit.this.cardNumberDrawable);
                SdkDebit.this.cardNumberDrawable.setAlpha(100);
                SdkDebit.this.mCallback.modifyConvenienceCharges(null);
            } else {
                SdkDebit.this.isCardNumberValid = Boolean.TRUE;
                SdkDebit.this.isMaestro = true;
                SdkDebit.this.valid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText), SdkSetupCardDetails.getCardDrawable(SdkDebit.this.getResources(), SdkDebit.this.cardNumber));
                SdkDebit.this.mCallback.modifyConvenienceCharges(tempIssuer);
            }
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && s.length() % 5 == 0 && space == s.charAt(s.length() - 1)) {
                s.delete(s.length() - 1, s.length());
            }
            if (s.length() > 0 && s.length() % 5 == 0 && Character.isDigit(s.charAt(s.length() - 1)) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 4) {
                s.insert(s.length() - 1, String.valueOf(space));
            }
            SdkDebit.this.isCvvValid = Boolean.FALSE;
            ((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).getText().clear();
            SdkDebit.this.invalid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText), SdkDebit.this.cvvDrawable);
        }
    }

    class C03922 implements TextWatcher {
        C03922() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            SdkDebit.this.cvv = ((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).getText().toString();
            if (SdkDebit.this.cardNumber.startsWith("34") || SdkDebit.this.cardNumber.startsWith("37")) {
                if (SdkDebit.this.cvv.length() == 4) {
                    SdkDebit.this.isCvvValid = Boolean.TRUE;
                    SdkDebit.this.valid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText), SdkDebit.this.cvvDrawable);
                    return;
                }
                SdkDebit.this.isCvvValid = Boolean.FALSE;
                SdkDebit.this.invalid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText), SdkDebit.this.cvvDrawable);
            } else if (SdkDebit.this.cvv.length() == 3) {
                SdkDebit.this.isCvvValid = Boolean.TRUE;
                SdkDebit.this.valid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText), SdkDebit.this.cvvDrawable);
            } else {
                SdkDebit.this.isCvvValid = Boolean.FALSE;
                SdkDebit.this.invalid((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.cvvEditText), SdkDebit.this.cvvDrawable);
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    class C03933 implements OnFocusChangeListener {
        C03933() {
        }

        public void onFocusChange(View view, boolean b) {
            if (b) {
                SdkDebit.this.makeInvalid();
            }
        }
    }

    class C03944 implements OnFocusChangeListener {
        C03944() {
        }

        public void onFocusChange(View view, boolean b) {
            if (b) {
                SdkDebit.this.makeInvalid();
            }
        }
    }

    class C03955 implements OnFocusChangeListener {
        C03955() {
        }

        public void onFocusChange(View view, boolean b) {
            if (b) {
                SdkDebit.this.makeInvalid();
            }
        }
    }

    class C03966 implements OnCheckedChangeListener {
        C03966() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                SdkDebit.this.card_store_check = Boolean.TRUE;
                SdkDebit.this.mCardLabel.setVisibility(0);
                return;
            }
            SdkDebit.this.card_store_check = Boolean.FALSE;
            SdkDebit.this.mCardLabel.setVisibility(8);
        }
    }

    class C03997 implements OnTouchListener {

        class C03971 implements OnClickListener {
            C03971() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                SdkDebit.this.checkExpiry((EditText) SdkDebit.this.debitCardDetails.findViewById(C0360R.id.expiryDatePickerEditText), SdkDebit.this.mDatePicker.getSelectedYear(), SdkDebit.this.mDatePicker.getSelectedMonth());
            }
        }

        class C03982 implements OnClickListener {
            C03982() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                SdkDebit.this.mDatePicker.dismissDialog();
            }
        }

        C03997() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                SdkDebit.this.mDatePicker = new SdkCustomDatePicker((SdkHomeActivityNew) SdkDebit.this.mContext);
                SdkDebit.this.mDatePicker.build(SdkDebit.this.mMonth, SdkDebit.this.mYear, new C03971(), new C03982());
                SdkDebit.this.mDatePicker.show();
            }
            return false;
        }
    }

    public interface MakePaymentListener {
        void goToPayment(String str, HashMap<String, Object> hashMap) throws JSONException;

        void modifyConvenienceCharges(String str);
    }

    public SdkDebit(Context context) {
        super(context);
        this.mContext = context;
        onAttach((SdkHomeActivityNew) context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final String mode) {
        SdkLogger.m14d(SdkConstants.TAG, "DebitCardFragmentonCreateView");
        this.debitCardDetails = inflater.inflate(C0360R.layout.sdk_fragment_card_details, container, false);
        this.mYear = Calendar.getInstance().get(1);
        this.mMonth = Calendar.getInstance().get(2);
        this.mDay = Calendar.getInstance().get(5);
        this.mCardLabel = (EditText) this.debitCardDetails.findViewById(C0360R.id.label);
        this.mCardStore = (CheckBox) this.debitCardDetails.findViewById(C0360R.id.store_card);
        if (SdkSession.getInstance(this.mContext).getLoginMode().equals("guestLogin")) {
            this.mCardLabel.setVisibility(8);
            this.mCardStore.setVisibility(8);
        }
        this.cardNumberDrawable = this.mContext.getResources().getDrawable(C0360R.drawable.card);
        this.calenderDrawable = this.mContext.getResources().getDrawable(C0360R.drawable.calendar);
        this.cvvDrawable = this.mContext.getResources().getDrawable(C0360R.drawable.lock);
        this.cardNumberDrawable.setAlpha(100);
        this.calenderDrawable.setAlpha(100);
        this.cvvDrawable.setAlpha(100);
        this.cardNumberEditText = (EditText) this.debitCardDetails.findViewById(C0360R.id.cardNumberEditText);
        this.expiryDatePickerEditText = (EditText) this.debitCardDetails.findViewById(C0360R.id.expiryDatePickerEditText);
        this.cvvEditText = (EditText) this.debitCardDetails.findViewById(C0360R.id.cvvEditText);
        this.sdkTnc = (TextView) this.debitCardDetails.findViewById(C0360R.id.sdk_tnc);
        this.sdkTnc.setMovementMethod(LinkMovementMethod.getInstance());
        this.cardNumberEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, this.cardNumberDrawable, null);
        this.expiryDatePickerEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, this.calenderDrawable, null);
        this.cvvEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, this.cvvDrawable, null);
        this.cardNumberEditText.addTextChangedListener(new C03911());
        ((EditText) this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).addTextChangedListener(new C03922());
        this.cardNumberEditText.setOnFocusChangeListener(new C03933());
        this.debitCardDetails.findViewById(C0360R.id.cvvEditText).setOnFocusChangeListener(new C03944());
        this.expiryDatePickerEditText.setOnFocusChangeListener(new C03955());
        this.mCardStore.setOnCheckedChangeListener(new C03966());
        if (this.mContext.getSharedPreferences(SdkConstants.SP_SP_NAME, 0).getBoolean(SdkConstants.ONE_CLICK_PAYMENT, false)) {
            this.mCardStore.setText("");
        } else {
            this.sdkTnc.setVisibility(8);
            this.mCardStore.setText("Save this card");
        }
        this.expiryDatePickerEditText.setOnTouchListener(new C03997());
        this.debitCardDetails.findViewById(C0360R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SdkHelper.checkNetwork(SdkDebit.this.mContext)) {
                    String cardNumber = SdkDebit.this.cardNumberEditText.getText().toString();
                    HashMap<String, Object> data = new HashMap();
                    try {
                        if (SdkDebit.this.cvv.equals("") || SdkDebit.this.cvv == null) {
                            data.put(SdkConstants.CVV, "123");
                        } else {
                            data.put(SdkConstants.CVV, SdkDebit.this.cvv);
                        }
                        data.put(SdkConstants.EXPIRY_MONTH, Integer.valueOf(SdkDebit.this.expiryMonth));
                        data.put(SdkConstants.EXPIRY_YEAR, Integer.valueOf(SdkDebit.this.expiryYear));
                        data.put(SdkConstants.NUMBER, cardNumber);
                        data.put("key", ((SdkHomeActivityNew) SdkDebit.this.mContext).getPublicKey());
                        String tempIssuer = SdkSetupCardDetails.findIssuer(cardNumber, mode);
                        if (!(!mode.contentEquals(SdkConstants.PAYMENT_MODE_CC) || tempIssuer.contentEquals(SdkConstants.AMEX) || tempIssuer.contentEquals("DINR") || tempIssuer.contentEquals(SdkConstants.PAYMENT_MODE_CC))) {
                            tempIssuer = SdkConstants.PAYMENT_MODE_CC;
                        }
                        data.put(SdkConstants.BANK_CODE, tempIssuer);
                        if (SdkDebit.this.card_store_check.booleanValue()) {
                            data.put(SdkConstants.LABEL, SdkDebit.this.mCardLabel.getText().toString().toUpperCase());
                            data.put(SdkConstants.STORE, "1");
                        }
                        SdkDebit.this.mCallback.goToPayment(mode, data);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Toast.makeText(SdkDebit.this.mContext, C0360R.string.disconnected_from_internet, 0).show();
            }
        });
        return this.debitCardDetails;
    }

    private void checkExpiry(EditText expiryDatePickerEditText, int i, int i2) {
        expiryDatePickerEditText.setText("" + (i2 + 1) + " / " + i);
        this.expiryMonth = i2 + 1;
        this.expiryYear = i;
        if (this.expiryYear > Calendar.getInstance().get(1)) {
            this.isExpired = Boolean.FALSE;
            valid(expiryDatePickerEditText, this.calenderDrawable);
        } else if (this.expiryYear != Calendar.getInstance().get(1) || this.expiryMonth - 1 < Calendar.getInstance().get(2)) {
            this.isExpired = Boolean.TRUE;
            invalid(expiryDatePickerEditText, this.calenderDrawable);
        } else {
            this.isExpired = Boolean.FALSE;
            valid(expiryDatePickerEditText, this.calenderDrawable);
        }
    }

    private void valid(EditText editText, Drawable drawable) {
        drawable.setAlpha(255);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (this.isMaestro && this.isCardNumberValid.booleanValue()) {
            this.expiryDatePickerEditText.setHint("Optional");
            this.cvvEditText.setHint("Optional");
            hideKeyboardIfShown();
            this.debitCardDetails.findViewById(C0360R.id.makePayment).setEnabled(true);
        } else if (this.isCardNumberValid.booleanValue() && !this.isExpired.booleanValue() && this.isCvvValid.booleanValue()) {
            hideKeyboardIfShown();
            this.debitCardDetails.findViewById(C0360R.id.makePayment).setEnabled(true);
        } else {
            this.debitCardDetails.findViewById(C0360R.id.makePayment).setEnabled(false);
        }
    }

    private void invalid(EditText editText, Drawable drawable) {
        if (!(this.expiryDatePickerEditText == null || this.isMaestro)) {
            this.expiryDatePickerEditText.setHint(C0360R.string.expires);
        }
        if (!(this.cvvEditText == null || this.isMaestro)) {
            this.cvvEditText.setHint(C0360R.string.cvv);
        }
        if (this.isMaestro) {
            this.expiryDatePickerEditText.setHint("Optional");
            this.cvvEditText.setHint("Optional");
        }
        drawable.setAlpha(100);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (this.isMaestro) {
            this.debitCardDetails.findViewById(C0360R.id.makePayment).setEnabled(true);
        } else {
            this.debitCardDetails.findViewById(C0360R.id.makePayment).setEnabled(false);
        }
    }

    private void makeInvalid() {
        if (!(this.isCardNumberValid.booleanValue() || this.cardNumber.length() <= 0 || this.cardNumberEditText.isFocused())) {
            this.cardNumberEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, this.mContext.getResources().getDrawable(C0360R.drawable.error_icon), null);
        }
        if (!this.isCvvValid.booleanValue() && this.cvv.length() > 0 && !this.debitCardDetails.findViewById(C0360R.id.cvvEditText).isFocused()) {
            ((EditText) this.debitCardDetails.findViewById(C0360R.id.cvvEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, this.mContext.getResources().getDrawable(C0360R.drawable.error_icon), null);
        }
    }

    public void onAttach(Activity activity) {
        try {
            this.mCallback = (MakePaymentListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        SdkLogger.m17e("ACT", "onDestroy called");
    }

    private void hideKeyboardIfShown() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        View view = ((SdkHomeActivityNew) this.mContext).getCurrentFocus();
        if (view == null) {
            view = new View(this.mContext);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
