package com.payUMoney.sdk.walledSdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkCobbocEvent;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkHomeActivityNew;
import com.payUMoney.sdk.SdkSession;
import com.payUMoney.sdk.utils.SdkHelper;
import com.payUMoney.sdk.walledSdk.SharedPrefsUtils.Keys;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import org.json.JSONObject;

public class WalletSdkLoginSignUpActivity extends FragmentActivity {
    private final int MORE_OPTIONS_MARGIN_BOTTOM = 9;
    private final int MORE_OPTIONS_MARGIN_LEFT = 10;
    public final int PAYMENT_CANCELLED = 4;
    public final int PAYMENT_SUCCESS = 3;
    public final int RESULT_QUIT = 5;
    private final int WALLET_HISTORY = 6;
    private PopupWindow popupWindow;
    private TextView titleTextView;
    private HashMap<String, String> userParams;

    class C04361 implements OnClickListener {
        C04361() {
        }

        public void onClick(View v) {
            WalletSdkLoginSignUpActivity.this.handleMoreOptionsClickListener();
        }
    }

    class C04372 implements OnClickListener {
        C04372() {
        }

        public void onClick(View v) {
            if (SdkHelper.checkNetwork(WalletSdkLoginSignUpActivity.this)) {
                WalletSdkLoginSignUpActivity.this.dismissLogoutButton();
                SdkHelper.showProgressDialog(WalletSdkLoginSignUpActivity.this, "Logging Out");
                SdkSession.getInstance(WalletSdkLoginSignUpActivity.this).logout();
                return;
            }
            SdkHelper.showToastMessage(WalletSdkLoginSignUpActivity.this, WalletSdkLoginSignUpActivity.this.getString(C0360R.string.disconnected_from_internet), true);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0360R.layout.walletsdk_activity_login_sign_up);
        this.titleTextView = (TextView) findViewById(C0360R.id.pages_tabs);
        findViewById(C0360R.id.main_layout).setOnClickListener(new C04361());
        this.userParams = (HashMap) getIntent().getSerializableExtra(SdkConstants.PARAMS);
        checkForRequestType();
    }

    public void startShowingHistory() {
        Intent intent = new Intent(this, SdkHistoryActivity.class);
        intent.putExtra(SdkConstants.PARAMS, this.userParams);
        startActivityForResult(intent, 6);
    }

    @SuppressLint({"NewApi"})
    public boolean onCreateOptionsMenu(Menu menu) {
        if (findViewById(C0360R.id.more_options_imageView).getVisibility() == 0) {
            menu.add(0, C0360R.id.logout, menu.size(), C0360R.string.logout).setIcon(C0360R.drawable.logout).setShowAsAction(4);
        } else {
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void invalidateActivityOptionsMenu() {
        invalidateOptionsMenu();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0360R.id.logout) {
            SdkSession.getInstance(this).logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkForRequestType() {
        if (!SdkSession.getInstance(this).isLoggedIn()) {
            loadSignUpFragment(false);
        } else if (this.userParams.containsKey(SdkConstants.IS_HISTORY_CALL)) {
            startShowingHistory();
        } else {
            SdkHelper.showProgressDialog(this, "Getting Wallet Balance");
            SdkSession.getInstance(this).getUserVaults();
        }
    }

    public void dismissLogoutButton() {
        if (this.popupWindow != null && this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
    }

    public void showHideLogoutButton(boolean visibility) {
        int i;
        View findViewById = findViewById(C0360R.id.more_options_imageView);
        if (visibility) {
            i = 0;
        } else {
            i = 4;
        }
        findViewById.setVisibility(i);
        View findViewById2 = findViewById(C0360R.id.more_options_imageView1);
        if (visibility) {
            findViewById2.setVisibility(4);
        } else {
            findViewById2.setVisibility(4);
        }
    }

    private void handleMoreOptionsClickListener() {
        if (this.popupWindow == null || !this.popupWindow.isShowing()) {
            View popupView = ((LayoutInflater) getBaseContext().getSystemService("layout_inflater")).inflate(C0360R.layout.walletsdk_screen_popup, null);
            this.popupWindow = new PopupWindow(popupView, -2, -2);
            this.popupWindow.showAtLocation(findViewById(C0360R.id.more_options_imageView), 53, 10, getPixelValue(9) + 9);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setTouchable(true);
            ((Button) popupView.findViewById(C0360R.id.btn_close_popup)).setOnClickListener(new C04372());
            return;
        }
        this.popupWindow.dismiss();
    }

    public HashMap<String, String> getMapObject() {
        return this.userParams;
    }

    public void setTabNewTitle(String newTitle) {
        this.titleTextView.setText(newTitle);
    }

    private void checkForWalletOnlyPayment() {
        if (Double.parseDouble(SharedPrefsUtils.getStringPreference(this, Keys.WALLET_BALANCE)) >= Double.parseDouble((String) this.userParams.get(SdkConstants.AMOUNT))) {
            inflateWalletPaymentFragment(false);
        } else {
            inflateLoadWalletFragment(false);
        }
    }

    public void callSdkToLoadWallet(JSONObject paymentDetailsObject) {
        Intent intent = new Intent(this, SdkHomeActivityNew.class);
        intent.putExtra(SdkConstants.PARAMS, this.userParams);
        intent.putExtra(SdkConstants.PAYMENT_DETAILS_OBJECT, paymentDetailsObject.toString());
        startActivityForResult(intent, 3);
    }

    public void inflateWalletPaymentFragment(boolean animate) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, 1);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            fragmentTransaction.setCustomAnimations(C0360R.anim.pop_enter, C0360R.anim.pop_exit, C0360R.anim.enter, C0360R.anim.exit);
        }
        fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkWalletPaymentFragment(), SdkWalletPaymentFragment.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
        SdkHelper.dismissProgressDialog();
    }

    public void setTabVisibility() {
        findViewById(C0360R.id.main_layout).setVisibility(0);
    }

    public void inflateLoadWalletFragment(boolean animate) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, 1);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            fragmentTransaction.setCustomAnimations(C0360R.anim.pop_enter, C0360R.anim.pop_exit, C0360R.anim.enter, C0360R.anim.exit);
        }
        fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkLoadWalletFragment(), SdkLoadWalletFragment.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
        SdkHelper.dismissProgressDialog();
    }

    public void onPause() {
        super.onPause();
        SdkHelper.dismissProgressDialog();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onResume() {
        super.onResume();
        SharedPrefsUtils.setStringPreference(this, SdkConstants.USER_SESSION_COOKIE_PAGE_URL, getClass().getSimpleName());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void loadSignUpFragment(boolean animate) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, 1);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            fragmentTransaction.setCustomAnimations(C0360R.anim.pop_enter, C0360R.anim.pop_exit, C0360R.anim.enter, C0360R.anim.exit);
        }
        fragmentTransaction.replace(C0360R.id.login_signup_fragment_container, new SdkSignUpFragmentSdk(), SdkSignUpFragmentSdk.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void finish() {
        super.finish();
    }

    public void onBackPressed() {
        if (this.popupWindow == null || !this.popupWindow.isShowing()) {
            int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            super.onBackPressed();
            if (fragmentCount == 0) {
                close(0, null);
                return;
            }
            return;
        }
        this.popupWindow.dismiss();
    }

    public void onEventMainThread(SdkCobbocEvent event) {
        if (event.getType() == 49) {
            if (event.getStatus()) {
                checkForWalletOnlyPayment();
                return;
            }
            SdkHelper.dismissProgressDialog();
            if (SdkHelper.checkNetwork(this)) {
                SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
                close(0, null);
                return;
            }
            SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
            close(0, null);
        } else if (event.getType() == 2) {
            SdkHelper.dismissProgressDialog();
            if (event.getStatus()) {
                SdkHelper.showToastMessage(this, getString(C0360R.string.logout_success), false);
                loadSignUpFragment(true);
            } else if (SdkHelper.checkNetwork(this)) {
                SdkHelper.showToastMessage(this, getString(C0360R.string.something_went_wrong), true);
            } else {
                SdkHelper.showToastMessage(this, getString(C0360R.string.disconnected_from_internet), true);
            }
        }
    }

    private int getPixelValue(int px) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (((float) px) * dm.scaledDensity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            if (resultCode == -1) {
                SdkSession.getInstance(this).getUserVaults();
                SdkHelper.showToastMessage(this, "load wallet success", false);
            } else if (resultCode == 5) {
                SdkSession.getInstance(this).getUserVaults();
                SdkHelper.showToastMessage(this, "load wallet failed", true);
            } else if (resultCode == 0) {
                if (data == null || data.hasExtra(SdkConstants.IS_LOGOUT_CALL)) {
                    loadSignUpFragment(true);
                } else {
                    SdkSession.getInstance(this).getUserVaults();
                }
                SdkHelper.showToastMessage(this, "load wallet cancelled", true);
            }
        } else if (requestCode != 6) {
        } else {
            if (resultCode == -1) {
                close(6, data);
            } else if (resultCode == 0) {
                loadSignUpFragment(true);
            }
        }
    }

    public void close(int resultCode, Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        if (resultCode == 4) {
            intent.putExtra(SdkConstants.RESULT, SdkConstants.CANCEL_STRING);
            setResult(0, intent);
        } else if (resultCode == 3) {
            intent.putExtra(SdkConstants.RESULT, SdkConstants.SUCCESS_STRING);
            setResult(-1, intent);
        } else if (resultCode == 6) {
            intent.putExtra(SdkConstants.IS_HISTORY_CALL, true);
            setResult(-1, intent);
        }
        finish();
    }
}
