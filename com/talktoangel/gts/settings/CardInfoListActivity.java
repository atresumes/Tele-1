package com.talktoangel.gts.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.PayUmoneySdkInitilizer.PaymentParam.Builder;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.adapter.CardListAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.CardItem;
import com.talktoangel.gts.utils.DividerItemDecoration;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardInfoListActivity extends AppCompatActivity {
    String TAG = "CardInfoListActivity";
    CardListAdapter adapter;
    Button btn_next;
    ArrayList<CardItem> list;
    ProgressBar mProgressBar;
    SessionManager mSessionManager;
    TextView textView;

    class C10212 implements Listener<String> {
        C10212() {
        }

        public void onResponse(String response) {
            Exception e;
            CardInfoListActivity.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    JSONArray array = jObj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        CardItem item = new CardItem();
                        item.setId(object.getString("id"));
                        item.setName(object.getString(SdkConstants.LABEL));
                        item.setNumber(object.getString("card_number"));
                        item.setValidity(object.getString("card_validity"));
                        item.setCvv(object.getString("cvv"));
                        CardInfoListActivity.this.list.add(item);
                    }
                    CardInfoListActivity.this.adapter.notifyDataSetChanged();
                    CardInfoListActivity.this.btn_next.setText(CardInfoListActivity.this.getString(C0585R.string.action_update_card));
                    return;
                }
                CardInfoListActivity.this.textView.setVisibility(0);
                Toast.makeText(CardInfoListActivity.this.getApplicationContext(), jObj.getString("message"), 1).show();
            } catch (JSONException e2) {
                e = e2;
                CardInfoListActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(CardInfoListActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
            } catch (NullPointerException e3) {
                e = e3;
                CardInfoListActivity.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(CardInfoListActivity.this.getApplicationContext(), "error" + e.getMessage(), 1).show();
            }
        }
    }

    class C10223 implements ErrorListener {
        C10223() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error", "" + error.getMessage());
            CardInfoListActivity.this.mProgressBar.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_card_info_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mProgressBar = (ProgressBar) findViewById(C0585R.id.progress_cil);
        this.textView = (TextView) findViewById(C0585R.id.textView_cil);
        RecyclerView recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_cil);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        this.btn_next = (Button) findViewById(C0585R.id.btn_add_card);
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.list = new ArrayList();
        this.adapter = new CardListAdapter(this, this.list);
        recyclerView.setAdapter(this.adapter);
        final Builder builder = new Builder().setMerchantId("4945987").setKey("rjQUPktU").setIsDebug(true).setAmount(1000.0d).setTnxId("TXN8367286482921").setPhone("8827535006").setProductName("TTA").setFirstName("Devendra").setEmail("devendra.rag@gmail.com").setsUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/success.php").setfUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php").setUdf1("fg").setUdf2("dfg").setUdf3("dfg").setUdf4("xc").setUdf5("dgf");
        this.btn_next.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PayUmoneySdkInitilizer.startPaymentActivityForResult(CardInfoListActivity.this, builder.build());
            }
        });
        getCardDetailList((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1001) {
            return;
        }
        if (resultCode == -1) {
            Log.e(this.TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
            data.getStringExtra(SdkConstants.PAYMENT_ID);
        } else if (resultCode == 0) {
            Log.e(this.TAG, SdkConstants.USER_CANCELLED_TRANSACTION);
        } else if (resultCode == 90) {
            Log.e(this.TAG, "failure");
        } else if (resultCode == 8) {
            Log.e(this.TAG, "User returned without login");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void getCardDetailList(String id) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = id;
        Request strReq = new StringRequest(1, EndPoints.GET_CARD_DETAIL, new C10212(), new C10223()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", str);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
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
}
