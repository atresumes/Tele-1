package com.payu.custombrowser.custombar;

import android.view.View;
import com.payu.custombrowser.C0517R;

public class CustomProgressBar {
    public void showDialog(View view) {
        view.setVisibility(0);
        showProgressDialogNew(view);
    }

    private void showProgressDialogNew(View view) {
        DotsProgressBar progressBar = (DotsProgressBar) view.findViewById(C0517R.id.dotsProgressBar);
        progressBar.setDotsCount(5);
        progressBar.start();
    }

    public void removeProgressDialog(View view) {
        DotsProgressBar progressBar = (DotsProgressBar) view.findViewById(C0517R.id.dotsProgressBar);
        view.setVisibility(8);
        progressBar.stop();
    }

    public void removeDialog(View view) {
        view.setVisibility(8);
    }
}
