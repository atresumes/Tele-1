package com.talktoangel.gts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.talktoangel.gts.listener.OnClickListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarCustomView extends LinearLayout {
    private static final int MAX_CALENDAR_COLUMN = 42;
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private TextView currentDate;
    public Date date;
    private SimpleDateFormat formatter = new SimpleDateFormat("EE, dd MMMM", Locale.ENGLISH);
    private OnClickListener mListener;
    private ImageView nextButton;
    private ImageView previousButton;

    class C05681 implements View.OnClickListener {
        C05681() {
        }

        public void onClick(View v) {
            if (CalendarCustomView.this.cal.getTimeInMillis() > System.currentTimeMillis()) {
                CalendarCustomView.this.cal.add(5, -1);
                CalendarCustomView.this.setUpCalendarAdapter();
                CalendarCustomView.this.mListener.onClick();
            }
        }
    }

    class C05692 implements View.OnClickListener {
        C05692() {
        }

        public void onClick(View v) {
            CalendarCustomView.this.cal.add(5, 1);
            CalendarCustomView.this.setUpCalendarAdapter();
            CalendarCustomView.this.mListener.onClick();
        }
    }

    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        Log.d(TAG, "I need to call this method");
    }

    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {
        View view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0585R.layout.calendar_layout, this);
        this.previousButton = (ImageView) view.findViewById(C0585R.id.previous_month);
        this.nextButton = (ImageView) view.findViewById(C0585R.id.next_month);
        this.currentDate = (TextView) view.findViewById(C0585R.id.display_current_date);
    }

    private void setPreviousButtonClickEvent() {
        this.previousButton.setOnClickListener(new C05681());
    }

    private void setNextButtonClickEvent() {
        this.nextButton.setOnClickListener(new C05692());
    }

    private void setUpCalendarAdapter() {
        List<Date> dayValueInCells = new ArrayList();
        Calendar mCal = (Calendar) this.cal.clone();
        mCal.set(5, 1);
        mCal.add(5, -(mCal.get(7) - 1));
        while (dayValueInCells.size() < 42) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(5, 1);
        }
        this.date = this.cal.getTime();
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        this.currentDate.setText(this.formatter.format(this.cal.getTime()));
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }
}
