package com.payUMoney.sdk.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.SdkSetupCardDetails;
import com.payUMoney.sdk.fragment.SdkStoredCardFragment;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.utils.SdkLogger;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkStoredCardAdapter extends BaseAdapter {
    EditText cvv = null;
    Dialog cvvDialog = null;
    private boolean cvvDialogIsShowing = false;
    MakePaymentListener mCallback;
    Context mContext;
    private int mSelectedCard = -1;
    JSONArray mStoredCards;
    String mode = null;
    Button negativeResponse = null;
    Button positiveResponse = null;

    class C03791 implements OnClickListener {
        C03791() {
        }

        public void onClick(View v) {
            SdkStoredCardAdapter.this.cvvDialog.dismiss();
        }
    }

    class C03802 implements OnFocusChangeListener {
        C03802() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SdkStoredCardAdapter.this.cvvDialog.getWindow().setSoftInputMode(5);
            }
        }
    }

    class C03813 implements OnDismissListener {
        C03813() {
        }

        public void onDismiss(DialogInterface dialog) {
            ((SdkHomeActivityNew) SdkStoredCardAdapter.this.mContext).updateDetails("");
            SdkStoredCardAdapter.this.cvvDialogIsShowing = false;
        }
    }

    class C03846 implements TextWatcher {
        C03846() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == 0) {
                SdkStoredCardAdapter.this.positiveResponse.setEnabled(true);
                SdkStoredCardAdapter.this.hideKeyboardIfShown(SdkStoredCardAdapter.this.cvv);
            } else if (editable.toString().length() > 0 && editable.toString().length() < 3) {
                SdkStoredCardAdapter.this.positiveResponse.setEnabled(false);
            } else if (editable.toString().length() > 0 && editable.toString().length() >= 3) {
                SdkStoredCardAdapter.this.positiveResponse.setEnabled(true);
                SdkStoredCardAdapter.this.hideKeyboardIfShown(SdkStoredCardAdapter.this.cvv);
            }
        }
    }

    public interface MakePaymentListener {
        void goToPayment(String str, HashMap<String, Object> hashMap) throws JSONException;
    }

    public SdkStoredCardAdapter(Context context, JSONArray storedCards) {
        this.mContext = context;
        this.mStoredCards = storedCards;
        this.mCallback = (MakePaymentListener) context;
    }

    public int getCount() {
        try {
            return this.mStoredCards.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Object getItem(int i) {
        try {
            return this.mStoredCards.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    private void handleCvvDialog() {
        if (!this.cvvDialogIsShowing) {
            this.cvvDialog = new Dialog(this.mContext);
            this.cvvDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.cvvDialog.requestWindowFeature(1);
            this.cvvDialog.setContentView(C0360R.layout.sdk_enter_cvv);
            this.cvvDialog.getWindow().setLayout(-2, -2);
            this.cvv = (EditText) this.cvvDialog.findViewById(C0360R.id.cvv);
            this.positiveResponse = (Button) this.cvvDialog.findViewById(C0360R.id.makePayment);
            this.negativeResponse = (Button) this.cvvDialog.findViewById(C0360R.id.cancel);
            this.negativeResponse.setOnClickListener(new C03791());
            if (this.mContext.getSharedPreferences(SdkConstants.SP_SP_NAME, 0).getBoolean(SdkConstants.ONE_CLICK_PAYMENT, false)) {
                this.cvvDialog.findViewById(C0360R.id.sdk_tnc).setVisibility(0);
                ((TextView) this.cvvDialog.findViewById(C0360R.id.sdk_tnc)).setMovementMethod(LinkMovementMethod.getInstance());
            }
            this.cvv.setOnFocusChangeListener(new C03802());
            this.cvvDialog.setOnDismissListener(new C03813());
            this.cvvDialog.setCanceledOnTouchOutside(true);
            this.cvvDialog.show();
            this.cvvDialogIsShowing = true;
        }
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        SdkLogger.m14d(SdkConstants.TAG, ": Position" + position);
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0360R.layout.sdk_card, null);
        }
        try {
            int img;
            final JSONObject jsonObject = (JSONObject) getItem(position);
            switch (SdkStoredCardFragment.getIssuer(jsonObject.getString(SdkConstants.NUMBER), jsonObject.getString("pg"))) {
                case LASER:
                    img = C0360R.drawable.card_laser;
                    break;
                case VISA:
                    img = C0360R.drawable.card_visa;
                    break;
                case MASTERCARD:
                    img = C0360R.drawable.card_master;
                    break;
                case MAESTRO:
                    img = C0360R.drawable.card_maestro;
                    break;
                case JCB:
                    img = C0360R.drawable.card_jcb;
                    break;
                case DINER:
                    img = C0360R.drawable.card_diner;
                    break;
                case AMEX:
                    img = C0360R.drawable.card_amex;
                    break;
                case RUPAY:
                    img = C0360R.drawable.card_rupay;
                    break;
                default:
                    img = C0360R.drawable.card;
                    break;
            }
            ((ImageView) view.findViewById(C0360R.id.sdk_card_type_imageView)).setImageDrawable(view.getResources().getDrawable(img));
            String cardNumber = jsonObject.getString(SdkConstants.NUMBER);
            cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
            ((TextView) view.findViewById(C0360R.id.sdk_card_label_textView)).setText(jsonObject.getString("cardName"));
            ((TextView) view.findViewById(C0360R.id.sdk_card_number_textView)).setText("••••" + cardNumber);
            if (position == getSelectedCard()) {
                handleCvvDialog();
                final HashMap<String, Object> data = new HashMap();
                data.put("storeCardId", jsonObject.getString("cardId"));
                data.put("store_card_token", jsonObject.getString(SdkConstants.CARD_TOKEN));
                data.put(SdkConstants.LABEL, jsonObject.getString("cardName"));
                data.put(SdkConstants.NUMBER, "");
                if (jsonObject.getString("pg").equals(SdkConstants.PAYMENT_MODE_CC)) {
                    this.mode = SdkConstants.PAYMENT_MODE_CC;
                } else {
                    this.mode = SdkConstants.PAYMENT_MODE_DC;
                }
                data.put("key", ((SdkHomeActivityNew) this.mContext).getPublicKey());
                if (SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), this.mode).equals("MAES")) {
                    this.positiveResponse.setEnabled(true);
                    this.cvv.setHint("CVV(Optional)");
                    data.put(SdkConstants.BANK_CODE, SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), this.mode));
                    this.cvv.setFilters(new InputFilter[]{new LengthFilter(3)});
                    this.cvv.addTextChangedListener(new C03846());
                    this.positiveResponse.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            ((SdkHomeActivityNew) SdkStoredCardAdapter.this.mContext).hideKeyboardIfShown();
                            if (SdkHelper.checkNetwork(SdkStoredCardAdapter.this.mContext)) {
                                if (SdkStoredCardAdapter.this.cvv.getText().toString().length() > 0) {
                                    data.put(SdkConstants.CVV, SdkStoredCardAdapter.this.cvv.getText().toString());
                                } else {
                                    data.put(SdkConstants.CVV, "123");
                                }
                                data.put(SdkConstants.EXPIRY_MONTH, "");
                                data.put(SdkConstants.EXPIRY_YEAR, "");
                                SdkStoredCardAdapter.this.cvvDialog.dismiss();
                                try {
                                    SdkStoredCardAdapter.this.mCallback.goToPayment(SdkStoredCardAdapter.this.mode, data);
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(SdkStoredCardAdapter.this.mContext, C0360R.string.disconnected_from_internet, 0).show();
                        }
                    });
                } else {
                    this.positiveResponse.setEnabled(false);
                    this.cvv.setHint("CVV");
                    if (SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), this.mode).equals(SdkConstants.AMEX)) {
                        data.put(SdkConstants.BANK_CODE, SdkConstants.AMEX);
                        this.cvv.setFilters(new InputFilter[]{new LengthFilter(4)});
                    } else {
                        data.put(SdkConstants.BANK_CODE, SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), this.mode));
                        this.cvv.setFilters(new InputFilter[]{new LengthFilter(3)});
                    }
                    this.cvv.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        }

                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        }

                        public void afterTextChanged(Editable editable) {
                            try {
                                if (SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), SdkStoredCardAdapter.this.mode).equals(SdkConstants.AMEX) && editable.toString().length() >= 4) {
                                    SdkStoredCardAdapter.this.positiveResponse.setEnabled(true);
                                    SdkStoredCardAdapter.this.hideKeyboardIfShown(SdkStoredCardAdapter.this.cvv);
                                } else if (SdkSetupCardDetails.findIssuer(jsonObject.getString(SdkConstants.NUMBER), SdkStoredCardAdapter.this.mode).equals(SdkConstants.AMEX) || editable.toString().length() < 3) {
                                    SdkStoredCardAdapter.this.positiveResponse.setEnabled(false);
                                } else {
                                    SdkStoredCardAdapter.this.positiveResponse.setEnabled(true);
                                    SdkStoredCardAdapter.this.hideKeyboardIfShown(SdkStoredCardAdapter.this.cvv);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    this.positiveResponse.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            if (SdkHelper.checkNetwork(SdkStoredCardAdapter.this.mContext)) {
                                data.put(SdkConstants.CVV, SdkStoredCardAdapter.this.cvv.getText().toString());
                                data.put(SdkConstants.EXPIRY_MONTH, "");
                                data.put(SdkConstants.EXPIRY_YEAR, "");
                                SdkStoredCardAdapter.this.cvvDialog.dismiss();
                                try {
                                    SdkStoredCardAdapter.this.mCallback.goToPayment(SdkStoredCardAdapter.this.mode, data);
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(SdkStoredCardAdapter.this.mContext, C0360R.string.disconnected_from_internet, 0).show();
                        }
                    });
                }
                this.mSelectedCard = -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void hideKeyboardIfShown(EditText cvv) {
        ((InputMethodManager) this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(cvv.getWindowToken(), 0);
    }

    public int getSelectedCard() {
        return this.mSelectedCard;
    }

    public void setSelectedCard(int selectedCard) {
        this.mSelectedCard = selectedCard;
    }
}
