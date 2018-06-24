package com.talktoangel.gts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.AppointmentItem;
import java.util.ArrayList;

public class AppointmentAdapter1 extends Adapter<ViewHolder> {
    private Context context;
    private ArrayList<AppointmentItem> list;

    class C05891 implements OnClickListener {
        C05891() {
        }

        public void onClick(View view) {
        }
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        Button btnType;
        Button cancel;
        Button edit;
        ImageView imageView;
        TextView txtDate;
        TextView txtName;
        TextView txtStatus;
        TextView txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(C0585R.id.img_ia);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txt_name_ia);
            this.txtStatus = (TextView) itemView.findViewById(C0585R.id.txt_apo_status);
            this.txtDate = (TextView) itemView.findViewById(C0585R.id.txt_date_ia);
            this.txtTime = (TextView) itemView.findViewById(C0585R.id.txt_time_ia);
            this.btnType = (Button) itemView.findViewById(C0585R.id.btn_type_ia);
            this.cancel = (Button) itemView.findViewById(C0585R.id.btn_cancel_ia);
            this.edit = (Button) itemView.findViewById(C0585R.id.btn_edit_ia);
        }
    }

    public AppointmentAdapter1(Context context, ArrayList<AppointmentItem> list) {
        this.context = context;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_appointment, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentItem item = (AppointmentItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtDate.setText(item.getApoDate());
        holder.txtTime.setText(item.getApoTime());
        Glide.with(this.context).load(item.getImage()).into(holder.imageView);
        holder.cancel.setVisibility(8);
        holder.edit.setVisibility(8);
        if (item.getApoStatus().equalsIgnoreCase("p")) {
            holder.txtStatus.setText(this.context.getString(C0585R.string.appointment_cancelled));
        } else if (item.getApoStatus().equalsIgnoreCase("c")) {
            holder.txtStatus.setText(this.context.getString(C0585R.string.appointment_cancelled));
        } else {
            holder.txtStatus.setText("Appointment Completed");
        }
        if (item.getApoType().equalsIgnoreCase("2")) {
            holder.btnType.setText("Voice Calling");
            holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_call_black_24dp, 0, 0, 0);
            holder.btnType.setCompoundDrawablePadding(8);
            holder.btnType.setOnClickListener(new C05891());
        } else if (item.getApoType().equalsIgnoreCase("1")) {
            holder.btnType.setText("Video Calling");
            holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_videocam_black_24dp, 0, 0, 0);
            holder.btnType.setCompoundDrawablePadding(8);
        } else if (item.getApoType().equalsIgnoreCase("3")) {
            holder.btnType.setText("Chat");
            holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_message_green_24dp, 0, 0, 0);
            holder.btnType.setCompoundDrawablePadding(8);
        } else if (item.getApoType().equalsIgnoreCase("4")) {
            holder.btnType.setText("Email");
            holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_email_black_24dp, 0, 0, 0);
            holder.btnType.setCompoundDrawablePadding(8);
        } else {
            holder.btnType.setText("Face to Face");
            holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_schedule_black_24dp, 0, 0, 0);
            holder.btnType.setCompoundDrawablePadding(8);
        }
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
