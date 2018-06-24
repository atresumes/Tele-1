package com.talktoangel.gts.adaptertherapist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.PatientItem;
import com.talktoangel.gts.therapist.PatientDetailsActivity;
import com.talktoangel.gts.utils.SessionManager;
import java.util.ArrayList;

public class PatientAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<PatientItem> list;

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
            PatientItem item = (PatientItem) PatientAdapter.this.list.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("id", item.getId());
            bundle.putString("name", item.getName());
            bundle.putString("email", item.getEmail());
            bundle.putString("dob", item.getDob());
            bundle.putString(SessionManager.KEY_GENDER, item.getGender());
            bundle.putString("status", item.getMarital_status());
            bundle.putString(SessionManager.KEY_ADDRESS, item.getAddress());
            bundle.putString("mobile", item.getMobile());
            bundle.putString("image", item.getImage());
            view.getContext().startActivity(new Intent(PatientAdapter.this.context, PatientDetailsActivity.class).putExtras(bundle));
        }
    }

    public PatientAdapter(Context context, ArrayList<PatientItem> imageList) {
        this.context = context;
        this.list = imageList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_provider, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        PatientItem item = (PatientItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.mipmap.ic_launcher)).into(holder.imageView);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
