package com.talktoangel.gts.adaptertherapist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.ProviderEducation;
import com.talktoangel.gts.therapist.EditProvEduActivity;
import java.util.ArrayList;

public class ProvEducationAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<ProviderEducation> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        TextView txtClg;
        TextView txtDegree;
        TextView txtYear;

        ViewHolder(View itemView) {
            super(itemView);
            this.txtDegree = (TextView) itemView.findViewById(C0585R.id.txtDegree);
            this.txtClg = (TextView) itemView.findViewById(C0585R.id.txtClg);
            this.txtYear = (TextView) itemView.findViewById(C0585R.id.txtYear);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            ProviderEducation item = (ProviderEducation) ProvEducationAdapter.this.list.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("drId", item.getDrID());
            bundle.putString("eduId", item.getEduID());
            bundle.putString("degree", item.getDegree());
            bundle.putString("clg", item.getClg());
            bundle.putString("year", item.getYear());
            view.getContext().startActivity(new Intent(ProvEducationAdapter.this.context, EditProvEduActivity.class).putExtras(bundle));
        }
    }

    public ProvEducationAdapter(Context context, ArrayList<ProviderEducation> list) {
        this.context = context;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_prov_edu, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderEducation item = (ProviderEducation) this.list.get(position);
        holder.txtDegree.setText(item.getDegree());
        holder.txtClg.setText(item.getClg());
        holder.txtYear.setText(item.getYear());
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
