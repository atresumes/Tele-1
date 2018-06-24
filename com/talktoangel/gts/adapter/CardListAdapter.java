package com.talktoangel.gts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.CardItem;
import java.util.ArrayList;

public class CardListAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<CardItem> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView txtExpiry;
        TextView txtName;
        TextView txtRate;

        ViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txtName);
            this.txtRate = (TextView) itemView.findViewById(C0585R.id.txtNo);
            this.txtExpiry = (TextView) itemView.findViewById(C0585R.id.txtExpiry);
        }
    }

    public CardListAdapter(Context context, ArrayList<CardItem> imageList) {
        this.context = context;
        this.list = imageList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_card_list, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem item = (CardItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtRate.setText(item.getNumber());
        holder.txtExpiry.setText(item.getValidity());
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
