package com.payUMoney.sdk.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;

public class SdkQustomDialogBuilder extends Builder {
    private View mDialogView;
    private View mDivider = this.mDialogView.findViewById(C0360R.id.titleDivider);
    private TextView mMessage = ((TextView) this.mDialogView.findViewById(C0360R.id.message));
    private TextView mTitle = ((TextView) this.mDialogView.findViewById(C0360R.id.alertTitle));

    public SdkQustomDialogBuilder(Context context) {
        super(context);
        this.mDialogView = View.inflate(context, C0360R.layout.sdk_qustom_dialog_layout, null);
        setView(this.mDialogView);
    }

    public SdkQustomDialogBuilder(Context context, int resource) {
        super(context, resource);
        this.mDialogView = View.inflate(context, C0360R.layout.sdk_qustom_dialog_layout, null);
        setView(this.mDialogView);
    }

    public SdkQustomDialogBuilder setDividerColor(String colorString) {
        this.mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    public SdkQustomDialogBuilder setTitle(CharSequence text) {
        this.mTitle.setText(text);
        return this;
    }

    public SdkQustomDialogBuilder setTitleColor(String colorString) {
        this.mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public SdkQustomDialogBuilder setMessage(int textResId) {
        this.mMessage.setText(textResId);
        return this;
    }

    public SdkQustomDialogBuilder setMessage(CharSequence text) {
        this.mMessage.setText(text);
        return this;
    }

    public SdkQustomDialogBuilder setCustomView(int resId, Context context) {
        ((LinearLayout) this.mDialogView.findViewById(C0360R.id.topPanel)).addView(View.inflate(context, resId, null));
        return this;
    }

    public AlertDialog show() {
        if (this.mTitle.getText().equals("")) {
            this.mDialogView.findViewById(C0360R.id.topPanel).setVisibility(8);
        }
        return super.show();
    }
}
