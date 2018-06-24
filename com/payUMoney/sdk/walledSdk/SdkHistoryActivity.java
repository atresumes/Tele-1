package com.payUMoney.sdk.walledSdk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkHistoryActivity extends FragmentActivity {
    public static int lastItem;
    public static int offset_count;
    public static int save_count;
    public static int type;
    private final int HISTORY_CANCELLED = 21;
    private final int HISTORY_LOGOUT = 22;
    private TextView centerMessage;
    private TextView footerMessageTextView;
    private boolean getMoreHistory;
    private boolean resetListAndMetaData;
    private boolean toShowAlert;
    private SdkWalletHistoryAdapter walletHistoryAdapter;
    private List<SdkWalletHistoryBean> walletHistoryBeanList;

    class C04201 implements OnScrollListener {
        C04201() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            SdkHistoryActivity.lastItem = firstVisibleItem + visibleItemCount;
            if (SdkHistoryActivity.this.getMoreHistory && SdkHistoryActivity.lastItem == totalItemCount && SdkHistoryActivity.lastItem > SdkHistoryActivity.save_count) {
                System.out.println("scroll listerner inside");
                SdkHistoryActivity.this.findViewById(C0360R.id.load_more).setVisibility(0);
                SdkHistoryActivity.save_count = SdkHistoryActivity.lastItem;
                SdkHistoryActivity.offset_count++;
                SdkHistoryActivity.this.checkNetworkAndCallServer(SdkHistoryActivity.offset_count);
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0360R.layout.sdk_history_activity);
        this.getMoreHistory = true;
        this.resetListAndMetaData = false;
        this.toShowAlert = false;
        this.centerMessage = (TextView) findViewById(C0360R.id.central_message_text_view);
        findViewById(C0360R.id.no_trans).setVisibility(8);
        findViewById(C0360R.id.load_more).setVisibility(8);
        ListView mListView = (ListView) findViewById(C0360R.id.trans_list);
        this.walletHistoryBeanList = new ArrayList();
        this.walletHistoryAdapter = new SdkWalletHistoryAdapter(this, this.walletHistoryBeanList);
        mListView.setAdapter(this.walletHistoryAdapter);
        save_count = 0;
        offset_count = 0;
        this.getMoreHistory = true;
        mListView.setOnScrollListener(new C04201());
        ViewGroup footer = (ViewGroup) getLayoutInflater().inflate(C0360R.layout.walletsdk_listview_footer, mListView, false);
        mListView.addFooterView(footer);
        this.footerMessageTextView = (TextView) footer.findViewById(C0360R.id.footer_message);
        checkNetworkAndCallServer(0);
    }

    private void checkNetworkAndCallServer(int offset_count) {
        if (SdkHelper.checkNetwork(this)) {
            SdkSession.getInstance(this).getTransactionHistory(offset_count);
            return;
        }
        findViewById(C0360R.id.loadingPanel).setVisibility(8);
        findViewById(C0360R.id.loading_trans).setVisibility(8);
        findViewById(C0360R.id.load_more).setVisibility(8);
        SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
        if (this.walletHistoryBeanList.size() == 0) {
            this.centerMessage.setVisibility(0);
        }
        this.toShowAlert = true;
        this.getMoreHistory = false;
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onBackPressed() {
        close(21);
    }

    public void close(int resultCode) {
        if (resultCode == 21) {
            setResult(-1, null);
        } else if (resultCode == 22) {
            setResult(0, null);
        }
        finish();
    }

    @SuppressLint({"NewApi"})
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, C0360R.id.logout, menu.size(), C0360R.string.logout).setIcon(C0360R.drawable.logout).setShowAsAction(4);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0360R.id.logout) {
            if (SdkHelper.checkNetwork(this)) {
                SdkHelper.showProgressDialog(this, "Logging Out");
                SdkSession.getInstance(this).logout();
            } else {
                SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private List<SdkWalletHistoryBean> getWalletHistoryBeans(JSONArray historyArray) {
        List<SdkWalletHistoryBean> historyBeans = new ArrayList();
        int historyArrayIterator = 0;
        while (historyArrayIterator < historyArray.length()) {
            try {
                SdkWalletHistoryBean parseHistory = new SdkWalletHistoryBean();
                JSONObject jsonCardObject = historyArray.getJSONObject(historyArrayIterator);
                parseHistory.setMode(jsonCardObject.optString("mode"));
                parseHistory.setTransactionDate(jsonCardObject.optLong(SdkConstants.TRANSACTION_DATE));
                parseHistory.setPaymentId(jsonCardObject.optString(SdkConstants.PAYMENT_ID));
                parseHistory.setMerchantId(jsonCardObject.optString("merchantId"));
                parseHistory.setMerchantTransactionId(jsonCardObject.optString(SdkConstants.MERCHANT_TXNID));
                parseHistory.setMerchantName(jsonCardObject.optString(SdkConstants.MERCHANT_NAME));
                parseHistory.setAmount(jsonCardObject.optString(SdkConstants.AMOUNT));
                parseHistory.setRefundToSource(jsonCardObject.optString(SdkConstants.REFUND_TO_SOURCE));
                parseHistory.setExternalRefId(jsonCardObject.optString(SdkConstants.EXTERNAL_REF_ID));
                parseHistory.setVaultAction(jsonCardObject.optString(SdkConstants.VAULT_ACTION));
                parseHistory.setVaultTransactionId(jsonCardObject.optString(SdkConstants.VAULT_TRANSACTION_ID));
                parseHistory.setPaymentType(jsonCardObject.optString(SdkConstants.PAYMENT_TYPE));
                parseHistory.setVaultActionMessage(getVaultActionDescription(jsonCardObject.optString(SdkConstants.VAULT_ACTION)));
                parseHistory.setDescription(getVaultActionDescription(jsonCardObject.optString("description")));
                parseHistory.setTransactionStatus(getVisibleStatus(jsonCardObject.optString("status")));
                historyBeans.add(parseHistory);
                historyArrayIterator++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return historyBeans;
    }

    private String getVisibleStatus(String realStatus) {
        String toLowerCase = realStatus.toLowerCase();
        Object obj = -1;
        switch (toLowerCase.hashCode()) {
            case -1867169789:
                if (toLowerCase.equals(SdkConstants.SUCCESS_STRING)) {
                    obj = null;
                    break;
                }
                break;
            case -1305250514:
                if (toLowerCase.equals("pendingfornextmonthprocessing")) {
                    obj = 3;
                    break;
                }
                break;
            case -1086574198:
                if (toLowerCase.equals("failure")) {
                    obj = 6;
                    break;
                }
                break;
            case -707924457:
                if (toLowerCase.equals("refunded")) {
                    obj = 2;
                    break;
                }
                break;
            case -682587753:
                if (toLowerCase.equals("pending")) {
                    obj = 5;
                    break;
                }
                break;
            case 105010112:
                if (toLowerCase.equals("successinternal")) {
                    obj = 1;
                    break;
                }
                break;
            case 476588369:
                if (toLowerCase.equals(SdkConstants.USER_CANCELLED_TRANSACTION)) {
                    obj = 7;
                    break;
                }
                break;
            case 1575601547:
                if (toLowerCase.equals("pendingforregistration")) {
                    obj = 4;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
            case 1:
                return getString(C0360R.string.transaction_status_success);
            case 2:
                return getString(C0360R.string.transaction_status_refund_success);
            case 3:
            case 4:
            case 5:
                return getString(C0360R.string.transaction_status_pending);
            case 6:
            case 7:
                return getString(C0360R.string.transaction_status_failed);
            default:
                return getString(C0360R.string.transaction_status_failed);
        }
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() == 43) {
            if (event.getStatus()) {
                findViewById(C0360R.id.loadingPanel).setVisibility(8);
                findViewById(C0360R.id.loading_trans).setVisibility(8);
                findViewById(C0360R.id.load_more).setVisibility(8);
                try {
                    JSONArray myPayments = ((JSONObject) event.getValue()).getJSONArray("content");
                    if (myPayments.length() > 0) {
                        findViewById(C0360R.id.no_trans).setVisibility(8);
                        if (this.resetListAndMetaData) {
                            this.walletHistoryBeanList.clear();
                            save_count = 0;
                            offset_count = 0;
                            this.getMoreHistory = true;
                        }
                        this.walletHistoryBeanList.addAll(getWalletHistoryBeans(myPayments));
                        this.walletHistoryAdapter.notifyDataSetChanged();
                    } else {
                        this.walletHistoryAdapter.notifyDataSetChanged();
                        if (this.walletHistoryBeanList.size() == 0) {
                            findViewById(C0360R.id.no_trans).setVisibility(0);
                            this.getMoreHistory = false;
                        } else {
                            this.footerMessageTextView.setVisibility(0);
                            this.getMoreHistory = false;
                        }
                    }
                } catch (JSONException e) {
                    if (this.walletHistoryBeanList.size() == 0) {
                        this.centerMessage.setVisibility(0);
                    }
                    SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                }
            } else {
                findViewById(C0360R.id.loadingPanel).setVisibility(8);
                findViewById(C0360R.id.loading_trans).setVisibility(8);
                findViewById(C0360R.id.load_more).setVisibility(8);
                SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                this.getMoreHistory = false;
                if (this.walletHistoryBeanList.size() == 0) {
                    this.centerMessage.setVisibility(0);
                }
            }
            this.resetListAndMetaData = false;
            this.toShowAlert = true;
        } else if (event.getType() == 2) {
            SdkHelper.dismissProgressDialog();
            if (event.getStatus()) {
                SdkHelper.showToastMessage(this, getString(C0360R.string.logout_success), false);
                close(22);
            } else if (SdkHelper.checkNetwork(this)) {
                SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
            } else {
                SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
            }
        }
    }

    private String getVaultActionDescription(String vaultAction) {
        Object obj = -1;
        switch (vaultAction.hashCode()) {
            case -1695367032:
                if (vaultAction.equals("wallettransferdebit")) {
                    obj = 15;
                    break;
                }
                break;
            case -1331208535:
                if (vaultAction.equals("topupwallet")) {
                    obj = 2;
                    break;
                }
                break;
            case -1033309475:
                if (vaultAction.equals("wallettransfercredit")) {
                    obj = 14;
                    break;
                }
                break;
            case -806207757:
                if (vaultAction.equals("cancelwallet")) {
                    obj = 1;
                    break;
                }
                break;
            case -737132812:
                if (vaultAction.equals("cashoutwallet")) {
                    obj = 7;
                    break;
                }
                break;
            case -723190256:
                if (vaultAction.equals("refundwallettransferdebit")) {
                    obj = 16;
                    break;
                }
                break;
            case -145623517:
                if (vaultAction.equals("refundtopupreversewallet")) {
                    obj = 6;
                    break;
                }
                break;
            case 84136193:
                if (vaultAction.equals("cashbackreversewallet")) {
                    obj = 12;
                    break;
                }
                break;
            case 484284480:
                if (vaultAction.equals("usewallet")) {
                    obj = null;
                    break;
                }
                break;
            case 542352433:
                if (vaultAction.equals("refundtopupwallet")) {
                    obj = 3;
                    break;
                }
                break;
            case 707788568:
                if (vaultAction.equals("nonpaymentrevokewallet")) {
                    obj = 10;
                    break;
                }
                break;
            case 1009109639:
                if (vaultAction.equals("reversetopupwallet")) {
                    obj = 9;
                    break;
                }
                break;
            case 1069941931:
                if (vaultAction.equals("topupreversewallet")) {
                    obj = 5;
                    break;
                }
                break;
            case 1630189937:
                if (vaultAction.equals("refundwallet")) {
                    obj = 4;
                    break;
                }
                break;
            case 1838762496:
                if (vaultAction.equals("cashoutreversewallet")) {
                    obj = 13;
                    break;
                }
                break;
            case 1855798896:
                if (vaultAction.equals("nonpaymenttopupwallet")) {
                    obj = 8;
                    break;
                }
                break;
            case 1964320531:
                if (vaultAction.equals("cashbackwallet")) {
                    obj = 11;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return "Used For A Payment";
            case 1:
                return "Return For Failed Payment";
            case 2:
                return "Loaded To Wallet";
            case 3:
                return "Added Against Refund";
            case 4:
                return "Refunded Back To Wallet";
            case 5:
                return "Load Amount Refunded";
            case 6:
                return "Refunded To Card";
            case 7:
                return "Transferred To Bank";
            case 8:
                return "Wallet Load - Other Sources";
            case 9:
                return "Load Wallet Reversal";
            case 10:
                return "Promotional Reversal";
            case 11:
                return "Cashback Into Wallet";
            case 12:
                return "Cashback Reversal From Wallet";
            case 13:
                return "Transfer Reversal";
            case 14:
                return "Wallet Transfer Received";
            case 15:
                return "Wallet Transfer Sent";
            case 16:
                return "Wallet Transfer Reversal";
            default:
                return vaultAction;
        }
    }
}
