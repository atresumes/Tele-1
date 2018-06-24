package com.talktoangel.gts.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.CountryCode;
import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CountryCode> list;

    public SpinnerAdapter(Context context, ArrayList<CountryCode> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return ((CountryCode) this.list.get(position)).getName();
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CountryCode code = (CountryCode) this.list.get(position);
        TextView name = new TextView(this.context);
        name.setText(code.getName());
        return name;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CountryCode code = (CountryCode) this.list.get(position);
        View row = ((Activity) this.context).getLayoutInflater().inflate(C0585R.layout.spinner_item, parent, false);
        ((TextView) row.findViewById(C0585R.id.txt_name)).setText(code.getName());
        ((TextView) row.findViewById(C0585R.id.txt_code)).setText(code.getCode());
        Glide.with(this.context).load(code.getFlag()).into((ImageView) row.findViewById(C0585R.id.img_flag));
        return row;
    }
}
