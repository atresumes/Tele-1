package com.talktoangel.gts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.ChatRoomActivity;
import com.talktoangel.gts.model.ChatRoom;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatRoomsAdapter extends Adapter<ViewHolder> {
    private static String today;
    private Context context;
    private ArrayList<ChatRoom> list;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView count;
        CircleImageView imageView;
        TextView message;
        TextView name;
        TextView timestamp;

        ViewHolder(View view) {
            super(view);
            this.imageView = (CircleImageView) view.findViewById(C0585R.id.img_pic);
            this.name = (TextView) view.findViewById(C0585R.id.name);
            this.message = (TextView) view.findViewById(C0585R.id.message);
            this.timestamp = (TextView) view.findViewById(C0585R.id.timestamp);
            this.count = (TextView) view.findViewById(C0585R.id.count);
            this.itemView.setOnClickListener(new OnClickListener(ChatRoomsAdapter.this) {
                public void onClick(View view) {
                    ChatRoom chatRoom = (ChatRoom) ChatRoomsAdapter.this.list.get(ViewHolder.this.getAdapterPosition());
                    chatRoom.setUnreadCount(0);
                    ChatRoomsAdapter.this.notifyDataSetChanged();
                    Intent intent = new Intent(ChatRoomsAdapter.this.context, ChatRoomActivity.class);
                    intent.putExtra("chat_room_id", chatRoom.getId());
                    intent.putExtra("name", chatRoom.getName());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public ChatRoomsAdapter(Context context, ArrayList<ChatRoom> list) {
        this.context = context;
        this.list = list;
        today = String.valueOf(Calendar.getInstance().get(5));
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.chat_rooms_list_row, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoom chatRoom = (ChatRoom) this.list.get(position);
        Glide.with(this.context).load(chatRoom.getPic()).error((int) C0585R.drawable.ic_user_black_24dp).into(holder.imageView);
        holder.name.setText(chatRoom.getName());
        holder.message.setText(chatRoom.getLastMessage());
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(0);
        } else {
            holder.count.setVisibility(8);
        }
        holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));
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
