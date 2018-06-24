package com.talktoangel.gts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.Message;
import com.talktoangel.gts.utils.SessionManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatRoomThreadAdapter extends Adapter<android.support.v7.widget.RecyclerView.ViewHolder> {
    private static String today;
    private int SELF = 100;
    private Context context;
    private ArrayList<Message> list;
    private SessionManager mSessionManager;

    private class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView message = ((TextView) this.itemView.findViewById(C0585R.id.message));
        TextView timestamp = ((TextView) this.itemView.findViewById(C0585R.id.timestamp));

        ViewHolder(View view) {
            super(view);
        }
    }

    public ChatRoomThreadAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
        this.mSessionManager = new SessionManager(context);
        today = String.valueOf(Calendar.getInstance().get(5));
    }

    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == this.SELF) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.chat_item_self, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.chat_item_other, parent, false);
        }
        return new ViewHolder(itemView);
    }

    public int getItemViewType(int position) {
        if (((Message) this.list.get(position)).getUserId().equals(this.mSessionManager.getUser().get(SessionManager.KEY_ID))) {
            return this.SELF;
        }
        return position;
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) this.list.get(position);
        ((ViewHolder) holder).message.setText(message.getMessage());
        ((ViewHolder) holder).timestamp.setText(getTimeStamp(message.getCreatedAt()));
    }

    public int getItemCount() {
        return this.list.size();
    }

    private static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = "";
        today = today.length() < 2 ? "0" + today : today;
        try {
            Date date = format.parse(dateStr);
            if (new SimpleDateFormat("dd", Locale.getDefault()).format(date).equals(today)) {
                format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            } else {
                format = new SimpleDateFormat("dd LLL, hh:mm a", Locale.getDefault());
            }
            timestamp = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
