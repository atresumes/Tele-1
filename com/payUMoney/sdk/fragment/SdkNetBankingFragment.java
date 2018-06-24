package com.payUMoney.sdk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.adapter.SdkNetBankingAdapter;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payu.custombrowser.util.CBConstant;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkNetBankingFragment extends View {
    String bankCode = null;
    JSONObject bankObject = null;
    private String key = null;
    String lastUsedBank = null;
    MakePaymentListener mCallback = null;
    Context mContext;
    private View netBankingFragment = null;

    class C04121 implements OnClickListener {
        C04121() {
        }

        public void onClick(View view) {
            if (SdkHelper.checkNetwork(SdkNetBankingFragment.this.mContext)) {
                HashMap<String, Object> data = new HashMap();
                try {
                    data.put(SdkConstants.BANK_CODE, SdkNetBankingFragment.this.bankCode);
                    data.put("key", SdkNetBankingFragment.this.key);
                    SdkNetBankingFragment.this.mCallback.goToPayment(SdkConstants.PAYMENT_MODE_NB, data);
                    return;
                } catch (JSONException e) {
                    Toast.makeText(SdkNetBankingFragment.this.mContext, "Something went wrong", 1).show();
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(SdkNetBankingFragment.this.mContext, C0360R.string.disconnected_from_internet, 0).show();
        }
    }

    class C04132 implements OnItemSelectedListener {
        C04132() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String code;
            Object item = parent.getItemAtPosition(pos);
            Iterator bankCodes = SdkNetBankingFragment.this.bankObject.keys();
            do {
                try {
                    if (bankCodes.hasNext()) {
                        code = (String) bankCodes.next();
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            } while (!SdkNetBankingFragment.this.bankObject.getJSONObject(code).getString(SdkConstants.BANK_TITLE_STRING).equals(item.toString()));
            SdkNetBankingFragment.this.bankCode = code;
            ((SdkHomeActivityNew) SdkNetBankingFragment.this.mContext).findViewById(C0360R.id.nbPayButton).setEnabled(true);
            SdkNetBankingFragment.this.mCallback.modifyConvenienceCharges(SdkNetBankingFragment.this.bankCode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public interface MakePaymentListener {
        void goToPayment(String str, HashMap<String, Object> hashMap) throws JSONException;

        void modifyConvenienceCharges(String str);
    }

    public SdkNetBankingFragment(Context context) {
        super(context);
        this.mContext = context;
        onAttach((SdkHomeActivityNew) context);
    }

    public void onAttach(Activity activity) {
        try {
            this.mCallback = (MakePaymentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        this.netBankingFragment = inflater.inflate(C0360R.layout.sdk_fragment_net_banking, container, false);
        onActivityCreated();
        this.netBankingFragment.findViewById(C0360R.id.nbPayButton).setOnClickListener(new C04121());
        return this.netBankingFragment;
    }

    public void onActivityCreated() {
        try {
            JSONObject tempDetails = ((SdkHomeActivityNew) this.mContext).getBankObject();
            if (!(tempDetails == null || !tempDetails.has(SdkConstants.PAYMENT_OPTION) || tempDetails.isNull(SdkConstants.PAYMENT_OPTION))) {
                JSONObject paymentOption = tempDetails.getJSONObject(SdkConstants.PAYMENT_OPTION);
                if (!(paymentOption == null || !paymentOption.has(SdkConstants.OPTIONS) || paymentOption.isNull(SdkConstants.OPTIONS))) {
                    JSONObject tempOptions = paymentOption.getJSONObject(SdkConstants.OPTIONS);
                    if (!(tempOptions == null || !tempOptions.has(CBConstant.NB) || tempOptions.isNull(CBConstant.NB))) {
                        this.bankObject = new JSONObject(tempOptions.getString(CBConstant.NB));
                    }
                }
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SdkConstants.SP_SP_NAME, 0);
                this.lastUsedBank = sharedPreferences.getString(SdkConstants.LASTUSEDBANK, SdkConstants.XYZ_STRING);
                if (!(paymentOption == null || !paymentOption.has(SdkConstants.CONFIG) || paymentOption.isNull(SdkConstants.CONFIG))) {
                    JSONObject tempConfig = paymentOption.getJSONObject(SdkConstants.CONFIG);
                    if (!(tempConfig == null || !tempConfig.has(SdkConstants.PREFERRED_PAYMENT_OPTION) || tempConfig.isNull(SdkConstants.PREFERRED_PAYMENT_OPTION))) {
                        JSONObject tempPreferredPaymentOption = tempConfig.getJSONObject(SdkConstants.PREFERRED_PAYMENT_OPTION);
                        if (tempPreferredPaymentOption != null && tempPreferredPaymentOption.has("optionType") && tempPreferredPaymentOption.optString("optionType", "").equals(SdkConstants.PAYMENT_MODE_NB)) {
                            this.lastUsedBank = tempPreferredPaymentOption.getString(SdkConstants.BANK_CODE_STRING);
                            Editor editor = sharedPreferences.edit();
                            editor.putString(SdkConstants.LASTUSEDBANK, this.lastUsedBank);
                            editor.commit();
                        }
                    }
                    if (!(tempConfig == null || !tempConfig.has("publicKey") || tempConfig.isNull("publicKey"))) {
                        this.key = tempConfig.getString("publicKey").replaceAll("\\r", "");
                    }
                }
            }
            if (this.bankObject != null) {
                int j;
                JSONArray keyNames = this.bankObject.names();
                String[][] banks1 = (String[][]) Array.newInstance(String.class, new int[]{102, 2});
                for (j = 0; j < keyNames.length(); j++) {
                    String code = keyNames.getString(j);
                    JSONObject object = this.bankObject.getJSONObject(code);
                    if (this.lastUsedBank.equals(code)) {
                        banks1[j][0] = Integer.toString(-1);
                        banks1[j][1] = object.getString(SdkConstants.BANK_TITLE_STRING);
                    } else {
                        banks1[j][0] = object.getString("pt_priority");
                        banks1[j][1] = object.getString(SdkConstants.BANK_TITLE_STRING);
                    }
                }
                for (j = 0; j < keyNames.length(); j++) {
                    for (int k = j + 1; k < keyNames.length(); k++) {
                        if (Integer.valueOf(banks1[k][0]).intValue() < Integer.valueOf(banks1[j][0]).intValue()) {
                            String[] tmpRow = banks1[k];
                            banks1[k] = banks1[j];
                            banks1[j] = tmpRow;
                        }
                    }
                }
                String[] banks = new String[keyNames.length()];
                for (j = 0; j < keyNames.length(); j++) {
                    banks[j] = banks1[j][1];
                }
                setupAdapter(banks);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupAdapter(String[] banks) {
        Spinner netBankingSpinner = (Spinner) this.netBankingFragment.findViewById(C0360R.id.netBankingSpinner);
        netBankingSpinner.setAdapter(new SdkNetBankingAdapter(this.mContext, banks));
        netBankingSpinner.setOnItemSelectedListener(new C04132());
    }
}
