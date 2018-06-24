package com.talktoangel.gts.adaptertherapist;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.AppointmentItem;
import java.util.ArrayList;

public class CalendarApoListAdapter extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<AppointmentItem> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        TextView txtName;
        TextView txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txt_name_icl);
            this.txtTime = (TextView) itemView.findViewById(C0585R.id.txt_time_icl);
        }

        public void onClick(View v) {
        }
    }

    public CalendarApoListAdapter(Context context, ArrayList<AppointmentItem> list) {
        this.context = context;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_calendar_list, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentItem item = (AppointmentItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtTime.setText(item.getApoTime());
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
