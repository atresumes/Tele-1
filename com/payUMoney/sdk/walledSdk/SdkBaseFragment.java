package com.payUMoney.sdk.walledSdk;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.payUMoney.sdk.utils.SdkHelper;

public class SdkBaseFragment extends Fragment {
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (SdkHelper.isValidClick()) {
            super.startActivityForResult(intent, requestCode);
        }
    }
}
