package com.payUMoney.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.json.JSONArray;

public class SdkNetBankingAdapter extends BaseAdapter {
    JSONArray availableBanks = null;
    String[] banks;
    Context mContext;

    public SdkNetBankingAdapter(Context context, String[] availableBanks) {
        this.mContext = context;
        this.banks = availableBanks;
    }

    public int getCount() {
        if (this.banks.length > 0) {
            return this.banks.length;
        }
        return 0;
    }

    public String getItem(int position) {
        return this.banks[position];
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(17367043, null);
        }
        ((TextView) view).setText(getItem(position));
        return view;
    }
}
