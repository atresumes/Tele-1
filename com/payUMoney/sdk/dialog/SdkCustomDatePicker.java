package com.payUMoney.sdk.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import com.payUMoney.sdk.C0360R;
import java.util.Calendar;

public class SdkCustomDatePicker {
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1970;
    private static final String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private Activity activity;
    private boolean build = false;
    private int currentMonth = 0;
    private int currentYear = 0;
    private int mSelectedMonth = 1;
    private NumberPicker monthNumberPicker = null;
    private AlertDialog pickerDialog = null;
    private View view;
    private NumberPicker yearNumberPicker = null;

    class C03871 implements OnValueChangeListener {
        C03871() {
        }

        public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
            SdkCustomDatePicker.this.monthNumberPicker.setDisplayedValues(SdkCustomDatePicker.PICKER_DISPLAY_MONTHS_NAMES);
            SdkCustomDatePicker.this.monthNumberPicker.setMinValue(0);
            SdkCustomDatePicker.this.monthNumberPicker.setMaxValue(SdkCustomDatePicker.MONTHS.length - 1);
            SdkCustomDatePicker.this.monthNumberPicker.setWrapSelectorWheel(false);
        }
    }

    public SdkCustomDatePicker(Activity activity) {
        this.activity = activity;
        this.view = activity.getLayoutInflater().inflate(C0360R.layout.sdk_custom_month_year_picker, null);
    }

    public void build(OnClickListener positiveButtonListener, OnClickListener negativeButtonListener) {
        build(-1, -1, positiveButtonListener, negativeButtonListener);
    }

    public void build(int selectedMonth, int selectedYear, OnClickListener positiveButtonListener, OnClickListener negativeButtonListener) {
        Calendar instance = Calendar.getInstance();
        this.currentMonth = instance.get(2);
        this.currentYear = instance.get(1);
        if (selectedMonth > 11 || selectedMonth < -1) {
            selectedMonth = this.currentMonth;
        }
        if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
            selectedYear = this.currentYear;
        }
        if (selectedMonth == -1) {
            selectedMonth = this.currentMonth;
        }
        if (selectedYear == -1) {
            selectedYear = this.currentYear;
        }
        Builder builder = new Builder(this.activity);
        builder.setView(this.view);
        this.monthNumberPicker = (NumberPicker) this.view.findViewById(C0360R.id.monthNumberPicker);
        this.monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);
        this.monthNumberPicker.setMinValue(0);
        this.monthNumberPicker.setMaxValue(MONTHS.length - 1);
        this.monthNumberPicker.setWrapSelectorWheel(false);
        this.yearNumberPicker = (NumberPicker) this.view.findViewById(C0360R.id.yearNumberPicker);
        this.yearNumberPicker.setMinValue(selectedYear);
        this.yearNumberPicker.setMaxValue(MAX_YEAR);
        this.yearNumberPicker.setWrapSelectorWheel(false);
        int selectedYearTemp = selectedYear;
        int selectedMonthTemp = selectedMonth;
        this.mSelectedMonth = selectedMonth;
        this.yearNumberPicker.setOnValueChangedListener(new C03871());
        this.monthNumberPicker.setValue(0);
        this.yearNumberPicker.setValue(selectedYear);
        this.monthNumberPicker.setDescendantFocusability(393216);
        this.yearNumberPicker.setDescendantFocusability(393216);
        builder.setTitle("Expiry Date");
        builder.setPositiveButton("Done", positiveButtonListener);
        builder.setNegativeButton("Cancel", negativeButtonListener);
        this.build = true;
        this.pickerDialog = builder.create();
    }

    public void show() {
        if (this.build && this.pickerDialog != null && !this.pickerDialog.isShowing()) {
            this.pickerDialog.show();
        }
    }

    public int getSelectedMonth() {
        return this.monthNumberPicker.getValue();
    }

    public String getSelectedMonthName() {
        return MONTHS[this.monthNumberPicker.getValue()];
    }

    public String getSelectedMonthShortName() {
        return PICKER_DISPLAY_MONTHS_NAMES[this.monthNumberPicker.getValue()];
    }

    public int getSelectedYear() {
        return this.yearNumberPicker.getValue();
    }

    public int getCurrentYear() {
        return this.currentYear;
    }

    public int getCurrentMonth() {
        return this.currentMonth;
    }

    public void setMonthValueChangedListener(OnValueChangeListener valueChangeListener) {
        this.monthNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setYearValueChangedListener(OnValueChangeListener valueChangeListener) {
        this.yearNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setMonthWrapSelectorWheel(boolean wrapSelectorWheel) {
        this.monthNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }

    public void setYearWrapSelectorWheel(boolean wrapSelectorWheel) {
        this.yearNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }

    public void dismissDialog() {
        if (this.pickerDialog != null && this.pickerDialog.isShowing()) {
            this.pickerDialog.dismiss();
        }
    }
}
