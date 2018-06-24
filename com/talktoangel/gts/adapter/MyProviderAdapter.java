package com.talktoangel.gts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.ProviderDetailActivity;
import com.talktoangel.gts.fragment.TherapistListFragment;
import com.talktoangel.gts.model.MyProviderItem;
import java.util.ArrayList;

public class MyProviderAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<MyProviderItem> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(C0585R.id.imgDash);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (((MyProviderItem) MyProviderAdapter.this.list.get(0)).getId().equals("")) {
                ((MainActivity) MyProviderAdapter.this.context).getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new TherapistListFragment()).addToBackStack(null).commit();
                return;
            }
            MyProviderItem item = (MyProviderItem) MyProviderAdapter.this.list.get(getAdapterPosition());
            String id = item.getId();
            String name = item.getName();
            view.getContext().startActivity(new Intent(MyProviderAdapter.this.context, ProviderDetailActivity.class).putExtra("id", id).putExtra("name", name).putExtra("image", item.getImage()));
        }
    }

    public MyProviderAdapter(Context context, ArrayList<MyProviderItem> imageList) {
        this.context = context;
        this.list = imageList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_dash, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        MyProviderItem item = (MyProviderItem) this.list.get(position);
        if (item.getImage().equals("")) {
            Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_user_white_24dp)).into(holder.imageView);
            holder.imageView.setPadding(12, 12, 22, 12);
            return;
        }
        Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_add_white_24dp)).into(holder.imageView);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
