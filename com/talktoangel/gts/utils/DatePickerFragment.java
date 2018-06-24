package com.talktoangel.gts.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private int day;
    private int minMax;
    private int month;
    OnDateSetListener ondateSet;
    private int year;

    public void setCallBack(OnDateSetListener ondate) {
        this.ondateSet = ondate;
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.year = args.getInt("year");
        this.month = args.getInt("month");
        this.day = args.getInt("day");
        this.minMax = args.getInt("minMax");
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this.ondateSet, this.year, this.month, this.day);
        if (this.minMax == 0) {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 100);
        } else if (this.minMax == 1) {
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        return dialog;
    }
}
