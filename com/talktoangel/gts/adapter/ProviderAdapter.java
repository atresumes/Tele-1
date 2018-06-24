package com.talktoangel.gts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.ProviderDetailActivity;
import com.talktoangel.gts.model.ProviderItem;
import java.util.ArrayList;
import java.util.List;

public class ProviderAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<ProviderItem> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        ImageView imageView;
        TextView txtName;
        TextView txtRate;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(C0585R.id.imgTherapist);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txtName);
            this.txtRate = (TextView) itemView.findViewById(C0585R.id.txtRate);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            ProviderItem item = (ProviderItem) ProviderAdapter.this.list.get(getAdapterPosition());
            String id = item.getId();
            String name = item.getName();
            view.getContext().startActivity(new Intent(ProviderAdapter.this.context, ProviderDetailActivity.class).putExtra("id", id).putExtra("name", name).putExtra("image", item.getImage()));
        }
    }

    public ProviderAdapter(Context context, ArrayList<ProviderItem> imageList) {
        this.context = context;
        this.list = imageList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_provider, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderItem item = (ProviderItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtRate.setText("Self-Pay " + item.getCharge());
        Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_user_black_24dp)).into(holder.imageView);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void setFilter(List<ProviderItem> providerItems) {
        this.list = new ArrayList();
        this.list.addAll(providerItems);
        notifyDataSetChanged();
    }
}
