package com.talktoangel.gts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.model.EventObjects;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapter extends ArrayAdapter {
    private List<EventObjects> allEvents;
    private Calendar currentDate;
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;

    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<EventObjects> allEvents) {
        super(context, C0585R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Date mDate = (Date) this.monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(5);
        int displayMonth = dateCal.get(2) + 1;
        int displayYear = dateCal.get(1);
        int currentMonth = this.currentDate.get(2) + 1;
        int currentYear = this.currentDate.get(1);
        View view = convertView;
        if (view == null) {
            view = this.mInflater.inflate(C0585R.layout.single_cell_layout, parent, false);
        }
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#FF5733"));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        ((TextView) view.findViewById(C0585R.id.calendar_date_id)).setText(String.valueOf(dayValue));
        TextView eventIndicator = (TextView) view.findViewById(C0585R.id.event_id);
        Calendar eventCalendar = Calendar.getInstance();
        for (int i = 0; i < this.allEvents.size(); i++) {
            eventCalendar.setTime(((EventObjects) this.allEvents.get(i)).getDate());
            if (dayValue == eventCalendar.get(5) && displayMonth == eventCalendar.get(2) + 1 && displayYear == eventCalendar.get(1)) {
                eventIndicator.setBackgroundColor(Color.parseColor("#FF4081"));
            }
        }
        return view;
    }

    public int getCount() {
        return this.monthlyDates.size();
    }

    @Nullable
    public Object getItem(int position) {
        return this.monthlyDates.get(position);
    }

    public int getPosition(Object item) {
        return this.monthlyDates.indexOf(item);
    }
}
