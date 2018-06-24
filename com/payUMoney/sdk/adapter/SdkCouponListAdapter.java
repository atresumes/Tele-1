package com.payUMoney.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.utils.SdkLogger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkCouponListAdapter extends BaseAdapter {
    static JSONArray mCoupons = null;
    Context mContext;
    RadioButton mCoupanselected = null;

    public SdkCouponListAdapter(Context context, JSONArray coupans) {
        this.mContext = context;
        mCoupons = coupans;
    }

    public int getCount() {
        return mCoupons.length();
    }

    public Object getItem(int i) {
        try {
            return mCoupons.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0360R.layout.sdk_coupons, null);
        }
        this.mCoupanselected = (RadioButton) view.findViewById(C0360R.id.coupanSelect);
        this.mCoupanselected.setFocusable(false);
        JSONObject jsonObject = (JSONObject) getItem(i);
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(jsonObject.getString("expiryDate")));
            String dateString = formater.format(calendar.getTime());
            SdkLogger.m20i("Dateformat", dateString);
            ((TextView) view.findViewById(C0360R.id.coupanNameForUser)).setText(jsonObject.getString("couponString"));
            ((TextView) view.findViewById(C0360R.id.coupanValidDate)).setText("valid upto \t \n" + dateString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
