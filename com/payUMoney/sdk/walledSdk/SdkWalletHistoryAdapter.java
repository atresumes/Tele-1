package com.payUMoney.sdk.walledSdk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class SdkWalletHistoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<SdkWalletHistoryBean> transactionList;

    private static class ViewHolder {
        TextView amount;
        TextView description;
        TextView paymentId;
        TextView statusTextMessage;
        TextView transactionDate;
        TextView vaultAction;

        private ViewHolder() {
        }
    }

    public SdkWalletHistoryAdapter(Context context, List<SdkWalletHistoryBean> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.transactionList.size();
    }

    public Object getItem(int location) {
        return this.transactionList.get(location);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(C0360R.layout.wallet_history, null);
            viewHolder = new ViewHolder();
            viewHolder.amount = (TextView) convertView.findViewById(C0360R.id.amount);
            viewHolder.paymentId = (TextView) convertView.findViewById(C0360R.id.payment_id);
            viewHolder.vaultAction = (TextView) convertView.findViewById(C0360R.id.vault_action);
            viewHolder.transactionDate = (TextView) convertView.findViewById(C0360R.id.transaction_date);
            viewHolder.description = (TextView) convertView.findViewById(C0360R.id.description);
            viewHolder.statusTextMessage = (TextView) convertView.findViewById(C0360R.id.status_text_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SdkWalletHistoryBean walletHistoryBean = (SdkWalletHistoryBean) this.transactionList.get(position);
        String amountInString = "0";
        try {
            amountInString = String.valueOf(round(Double.parseDouble(walletHistoryBean.getAmount()), 2).doubleValue());
        } catch (NumberFormatException e) {
            amountInString = "0";
        } catch (Exception e2) {
            amountInString = "0";
        }
        viewHolder.amount.setText(this.context.getString(C0360R.string.rs) + amountInString);
        if (walletHistoryBean.getPaymentId().trim().equals("") || walletHistoryBean.getPaymentId().trim().equalsIgnoreCase(SdkConstants.NULL_STRING)) {
            viewHolder.paymentId.setVisibility(8);
        } else {
            viewHolder.paymentId.setVisibility(0);
            viewHolder.paymentId.setText(this.context.getString(C0360R.string.transaction_history_payment_id, new Object[]{walletHistoryBean.getPaymentId()}));
        }
        viewHolder.transactionDate.setText(new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(walletHistoryBean.getTransactionDate()));
        if (walletHistoryBean.getVaultAction().trim().isEmpty() || walletHistoryBean.getVaultAction().trim().equalsIgnoreCase(SdkConstants.NULL_STRING)) {
            viewHolder.description.setVisibility(8);
        } else {
            viewHolder.description.setText(walletHistoryBean.getVaultAction());
            viewHolder.description.setVisibility(0);
        }
        if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_success))) {
            viewHolder.statusTextMessage.setText(this.context.getResources().getString(C0360R.string.transaction_status_success).toUpperCase());
        } else if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_failed))) {
            viewHolder.statusTextMessage.setText(this.context.getString(C0360R.string.transaction_status_failed).toUpperCase());
        } else if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_in_progress))) {
            viewHolder.statusTextMessage.setText(C0360R.string.history_status_msg_pending);
        } else if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_pending))) {
            viewHolder.statusTextMessage.setText(C0360R.string.history_status_msg_pending);
        } else if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_refund_initiated))) {
            viewHolder.statusTextMessage.setText(C0360R.string.history_status_msg_refund_initiated);
        } else if (walletHistoryBean.getTransactionStatus().equalsIgnoreCase(this.context.getString(C0360R.string.transaction_status_refund_success))) {
            viewHolder.statusTextMessage.setText(C0360R.string.history_status_msg_refund_success);
        }
        if (walletHistoryBean.getVaultActionMessage().trim().equals("")) {
            viewHolder.vaultAction.setVisibility(8);
        } else {
            viewHolder.vaultAction.setText(walletHistoryBean.getVaultActionMessage().trim());
            viewHolder.vaultAction.setVisibility(0);
        }
        return convertView;
    }

    public BigDecimal round(double d, int decimalPlace) {
        return new BigDecimal(Double.toString(d)).setScale(decimalPlace, 4);
    }
}
