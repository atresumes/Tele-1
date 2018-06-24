package com.payUMoney.sdk.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkPayUMoneyPointsFragment extends Fragment {
    public double amount = 0.0d;
    public double amt_convenience = 0.0d;
    public double amt_discount = 0.0d;
    public double amt_net = 0.0d;
    public double amt_total = 0.0d;
    public double amtafterDicount = 0.0d;
    private double cashback = 0.0d;
    Button checkout = null;
    private double couponAmt = 0.0d;
    final HashMap<String, Object> data = new HashMap();
    JSONObject details = null;
    private double discount = 0.0d;
    private boolean enoughDiscount;
    ProgressBar pb = null;
    String f7s = null;
    private TextView tv = null;

    class C04141 implements OnClickListener {
        C04141() {
        }

        public void onClick(View view) {
            if (SdkHelper.checkNetwork(SdkPayUMoneyPointsFragment.this.getActivity())) {
                try {
                    SdkPayUMoneyPointsFragment.this.pb.setVisibility(0);
                    SdkPayUMoneyPointsFragment.this.amt_net = (double) SdkPayUMoneyPointsFragment.round(SdkPayUMoneyPointsFragment.this.amt_net, 2);
                    if (SdkPayUMoneyPointsFragment.this.couponAmt > 0.0d) {
                        SdkSession.getInstance(SdkPayUMoneyPointsFragment.this.getActivity()).sendToPayU(SdkPayUMoneyPointsFragment.this.details, SdkConstants.POINTS, SdkPayUMoneyPointsFragment.this.data, Double.valueOf(SdkPayUMoneyPointsFragment.this.amt_net), Double.valueOf(SdkPayUMoneyPointsFragment.this.couponAmt), Double.valueOf(SdkPayUMoneyPointsFragment.this.amt_convenience));
                        return;
                    } else {
                        SdkSession.getInstance(SdkPayUMoneyPointsFragment.this.getActivity()).sendToPayU(SdkPayUMoneyPointsFragment.this.details, SdkConstants.POINTS, SdkPayUMoneyPointsFragment.this.data, Double.valueOf(SdkPayUMoneyPointsFragment.this.amt_net), Double.valueOf(SdkPayUMoneyPointsFragment.this.discount), Double.valueOf(SdkPayUMoneyPointsFragment.this.amt_convenience));
                        return;
                    }
                } catch (Exception e) {
                    SdkPayUMoneyPointsFragment.this.tv.setText("Something went wrong " + e.toString());
                    return;
                }
            }
            Toast.makeText(SdkPayUMoneyPointsFragment.this.getActivity(), C0360R.string.disconnected_from_internet, 0).show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0360R.layout.sdk_payumoney_points, container, false);
        this.pb = (ProgressBar) view.findViewById(C0360R.id.progressBar);
        this.checkout = (Button) view.findViewById(C0360R.id.checkout);
        this.tv = (TextView) view.findViewById(C0360R.id.tv1);
        this.f7s = getArguments().getString("details");
        try {
            this.details = new JSONObject(this.f7s);
            this.discount = getArguments().getDouble("discount");
            this.cashback = getArguments().getDouble("cashback");
            this.couponAmt = getArguments().getDouble("couponAmount");
            this.enoughDiscount = getArguments().getBoolean("enoughDiscount");
            this.amt_convenience = getArguments().getDouble("convenienceChargesAmount");
            initiate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.checkout.setOnClickListener(new C04141());
        return view;
    }

    public void initiate() throws JSONException {
        StringBuffer s;
        if (this.couponAmt > 0.0d) {
            this.amt_discount = this.couponAmt;
        } else if (this.discount > 0.0d) {
            this.amt_discount = this.discount;
        }
        this.amount = this.details.getJSONObject(SdkConstants.PAYMENT).getDouble(SdkConstants.ORDER_AMOUNT);
        this.amt_total = this.amount + this.amt_convenience;
        this.amtafterDicount = this.amt_total - this.amt_discount;
        this.amt_net = this.amtafterDicount;
        if (this.enoughDiscount) {
            s = new StringBuffer("You don't need to pay anything else for this transaction, please click on \"Pay Now\" to complete transaction\n\n\nSummary: \n\n\n***************************************\n\nNet Amount: " + round(this.amt_net, 2) + "\n");
        } else {
            s = new StringBuffer("You have enough PayUPoints, please click on \"Pay Now\" to complete transaction\n\n\nSummary: \n\n\n***************************************\n\nNet Amount: " + round(this.amt_net, 2) + "\n");
        }
        if (this.amt_convenience > 0.0d) {
            s.append("Convenience Charge : ").append(round(this.amt_convenience, 2)).append("\n");
        }
        if (this.amt_discount > 0.0d) {
            if (this.couponAmt > 0.0d) {
                s.append("Coupon Discount: ").append(round(this.couponAmt, 2)).append("\n");
            } else if (this.discount > 0.0d) {
                s.append("Discount: ").append(round(this.discount, 2)).append("\n");
            }
        } else if (this.cashback > 0.0d) {
            s.append("Cashback: ").append(round(this.cashback, 2)).append("\n");
        }
        s.append("Order Amount: ").append(round(this.amount, 2));
        if (((SdkHomeActivityNew) getActivity()).getPoints().doubleValue() > 0.0d) {
            s.append("\n\n***************************************\n\nAvailable PayUMoney Points ₹ :").append(round(((SdkHomeActivityNew) getActivity()).getPoints().doubleValue(), 2));
        }
        this.tv.setText(s);
    }

    public void onResume() {
        super.onResume();
    }

    public void onDetach() {
        super.onDetach();
        Intent intent = new Intent();
        intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
        Activity activity = getActivity();
        getActivity();
        activity.setResult(0, intent);
        getActivity().finish();
    }

    public void onPause() {
        super.onPause();
    }

    public static float round(double d, int decimalPlace) {
        return new BigDecimal(Double.toString(d)).setScale(decimalPlace, 4).floatValue();
    }
}
