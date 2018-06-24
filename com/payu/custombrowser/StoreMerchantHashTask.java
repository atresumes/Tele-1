package com.payu.custombrowser;

import android.os.AsyncTask;
import com.payu.custombrowser.util.CBAnalyticsConstant;
import org.json.JSONObject;

class StoreMerchantHashTask extends AsyncTask<String, Void, Void> {
    StoreMerchantHashTask() {
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Void doInBackground(String... params) {
        String payuResponse = params[0];
        ClassLoader mClassLoader = Bank.class.getClassLoader();
        try {
            if (!isCancelled()) {
                JSONObject jsonObject = new JSONObject(payuResponse);
                if (jsonObject.has(CBAnalyticsConstant.CARD_TOKEN) && jsonObject.has(CBAnalyticsConstant.MERCHANT_HASH)) {
                    Object oneClickPaymentObject = mClassLoader.loadClass("com.payu.india.CallBackHandler.OnetapCallback").getDeclaredMethod("getOneTapCallback", new Class[0]).invoke(null, new Object[0]);
                    oneClickPaymentObject.getClass().getDeclaredMethod("saveOneClickHash", new Class[]{String.class, String.class}).invoke(oneClickPaymentObject, new Object[]{jsonObject.getString(CBAnalyticsConstant.CARD_TOKEN), jsonObject.getString(CBAnalyticsConstant.MERCHANT_HASH)});
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
