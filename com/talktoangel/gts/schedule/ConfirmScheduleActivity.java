package com.talktoangel.gts.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.PayUmoneySdkInitilizer.PaymentParam;
import com.payUMoney.sdk.PayUmoneySdkInitilizer.PaymentParam.Builder;
import com.payUMoney.sdk.SdkConstants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.payu.custombrowser.util.CBConstant;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.PayUMoneyActivity;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.paypal.PayPalConfig;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmScheduleActivity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 123;
    public static final int PAYU_REQUEST_CODE = 456;
    public static final String TAG = "PayUMoneySDK Sample";
    String charge = "1";
    private Button doneButton;
    SessionManager mSessionManager;
    private ProgressBar progressBar;
    String transactionId = "";

    class C06018 implements OnClickListener {
        C06018() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C10032 implements Listener<String> {
        C10032() {
        }

        public void onResponse(String response) {
            ConfirmScheduleActivity.this.progressBar.setVisibility(8);
            Log.e("Response", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equalsIgnoreCase("true")) {
                    ConfirmScheduleActivity.this.mSessionManager.setFreeSession("0");
                    Toast.makeText(ConfirmScheduleActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
                    ConfirmScheduleActivity.this.startActivity(new Intent(ConfirmScheduleActivity.this, MainActivity.class).addFlags(67141632));
                    ConfirmScheduleActivity.this.finish();
                    return;
                }
                Toast.makeText(ConfirmScheduleActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                ConfirmScheduleActivity.this.progressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(ConfirmScheduleActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
            }
        }
    }

    class C10043 implements ErrorListener {
        C10043() {
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            ConfirmScheduleActivity.this.progressBar.setVisibility(8);
        }
    }

    class C10066 implements ErrorListener {
        C10066() {
        }

        public void onErrorResponse(VolleyError error) {
            if (error instanceof NoConnectionError) {
                Toast.makeText(ConfirmScheduleActivity.this, ConfirmScheduleActivity.this.getString(C0585R.string.connect_to_internet), 0).show();
            } else {
                Toast.makeText(ConfirmScheduleActivity.this, error.getMessage(), 0).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_confirm_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");
        String name = bundle.getString("name");
        String image = bundle.getString("image");
        final String type = bundle.getString("type");
        final String date = bundle.getString("date");
        final String time = bundle.getString("time");
        final String slotId = bundle.getString("slot_id");
        final String timingId = bundle.getString("timingId");
        this.charge = bundle.getString(SessionManager.KEY_LICENSE_NO);
        Log.e("TAG", bundle.toString());
        ImageView imageView = (ImageView) findViewById(C0585R.id.img_acs);
        TextView apoView = (TextView) findViewById(C0585R.id.txt_apo_pro);
        TextView nameView = (TextView) findViewById(C0585R.id.txt_name_acs);
        TextView dateView = (TextView) findViewById(C0585R.id.txt_date_acs);
        TextView timeView = (TextView) findViewById(C0585R.id.txt_time_acs);
        this.doneButton = (Button) findViewById(C0585R.id.btnDone);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progressSchedule);
        nameView.setText(name);
        dateView.setText(date);
        timeView.setText(time);
        apoView.setText(getString(C0585R.string.new_appointment_proposed).concat(" you"));
        Glide.with((FragmentActivity) this).load(image).error((int) C0585R.mipmap.ic_launcher).into(imageView);
        this.mSessionManager = new SessionManager(this);
        this.transactionId = ((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID)).concat("TTA").concat(String.valueOf(System.currentTimeMillis()));
        this.doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ConfirmScheduleActivity.this.makeAppointment((String) ConfirmScheduleActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID), id, type, date, time, slotId, timingId, ConfirmScheduleActivity.this.transactionId, ConfirmScheduleActivity.this.charge);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (((String) this.mSessionManager.getUser().get(SessionManager.FREE_SESSION)).equals("0")) {
            PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PayPalConfig.PAYPAL_CLIENT_ID);
            if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_CURRENCY_PREF)).equalsIgnoreCase("INR")) {
                startActivityForResult(new Intent(getApplicationContext(), PayUMoneyActivity.class).putExtra("transactionId", this.transactionId).putExtra(SdkConstants.AMOUNT, this.charge), PAYU_REQUEST_CODE);
                return;
            }
            startService(new Intent(this, PayPalService.class).putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config));
            getPayment();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == -1) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                showDialogMessage("Payment Success Id : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
            } else if (resultCode == 0) {
                Log.i(TAG, "failure");
                showDialogMessage(SdkConstants.USER_CANCELLED_TRANSACTION);
            } else if (resultCode == 90) {
                Log.i("app_activity", "failure");
                if (data != null && !data.getStringExtra(SdkConstants.RESULT).equals(SdkConstants.CANCEL_STRING)) {
                    showDialogMessage("failure");
                }
            } else if (resultCode == 8) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        } else if (requestCode != PAYPAL_REQUEST_CODE) {
            Toast.makeText(this, "Payment Cancelled!", 1).show();
            onBackPressed();
        } else if (resultCode == -1) {
            PaymentConfirmation confirm = (PaymentConfirmation) data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    JSONObject paymentDetails = confirm.toJSONObject();
                    Log.e("paymentDetails", paymentDetails.toString());
                    this.transactionId = paymentDetails.getJSONObject(CBConstant.RESPONSE).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                    onBackPressed();
                }
            }
        }
    }

    private void getPayment() {
        PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(this.charge), "USD", "Therapist Fee", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void makeAppointment(String userID, String providerID, String type, String date, String time, String slotId, String timingId, String transactionId, String charge) {
        GeneralSecurityException e;
        HttpStack stack;
        this.progressBar.setVisibility(0);
        final String str = userID;
        final String str2 = providerID;
        final String str3 = type;
        final String str4 = date;
        final String str5 = time;
        final String str6 = slotId;
        final String str7 = transactionId;
        final String str8 = timingId;
        final String str9 = charge;
        Request strReq = new StringRequest(1, EndPoints.MAKE_APPOINTMENT, new C10032(), new C10043()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_ID, str);
                params.put("dr_id", str2);
                params.put("apo_type", str3);
                params.put("apo_date", str4);
                params.put("apo_time", str5);
                params.put("slot_id", str6);
                params.put("user_to_doctor_msg", "");
                params.put("referral", "y");
                params.put("transaction_id", str7);
                params.put("timing_id", str8);
                params.put(SessionManager.FREE_SESSION, "0");
                params.put("apo_amount", str9);
                Log.e("ConfirmScheduleActivity", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            HttpStack hurlStack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(strReq, stack);
        }
        Controller.getInstance().addToRequestQueue(strReq, stack);
    }

    private void makePayment() {
        String phone = (String) this.mSessionManager.getUser().get("mobile");
        String productName = getResources().getString(C0585R.string.app_name);
        String txnId = "TTA" + System.currentTimeMillis();
        String email = (String) this.mSessionManager.getUser().get(SessionManager.KEY_EMAIL);
        Builder builder = new Builder();
        builder.setAmount(1000.0d).setTnxId(txnId).setPhone(phone).setProductName(productName).setFirstName("TTA").setEmail(email).setsUrl("https://test.payumoney.com/mobileapp/payumoney/success.php").setfUrl("https://test.payumoney.com/mobileapp/payumoney/failure.php").setUdf1("").setUdf2("").setUdf3("").setUdf4("").setUdf5("").setIsDebug(true).setKey("wY51pcD9").setMerchantId("5793485");
        calculateServerSideHashAndInitiatePayment(builder.build());
    }

    private void calculateServerSideHashAndInitiatePayment(final PaymentParam paymentParam) {
        Toast.makeText(this, "Please wait... Generating hash from server ... ", 1).show();
        final PaymentParam paymentParam2 = paymentParam;
        Volley.newRequestQueue(this).add(new StringRequest(1, "https://test.payumoney.com/payment/op/calculateHashForTest", new Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        String status = jsonObject.optString("status");
                        if (status != null || status.equals("1")) {
                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);
                            paymentParam.setMerchantHash(hash);
                            PayUmoneySdkInitilizer.startPaymentActivityForResult(ConfirmScheduleActivity.this, paymentParam);
                            return;
                        }
                        Toast.makeText(ConfirmScheduleActivity.this, jsonObject.getString(SdkConstants.RESULT), 0).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new C10066()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam2.getParams();
            }
        });
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new C06018());
        builder.show();
    }
}
