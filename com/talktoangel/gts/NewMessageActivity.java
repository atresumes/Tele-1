package com.talktoangel.gts;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class NewMessageActivity extends AppCompatActivity implements OnClickListener {
    private String TAG = NewMessageActivity.class.getSimpleName();
    Button btnNo;
    Button btnSend;
    Button btnYes;
    EditText etMessage;
    SessionManager mSessionManager;
    String receiverID;
    String referral = "";

    class C05821 implements TextWatcher {
        C05821() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 1) {
                NewMessageActivity.this.btnSend.setEnabled(true);
            } else {
                NewMessageActivity.this.btnSend.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_new_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.receiverID = getIntent().getStringExtra("id");
        this.etMessage = (EditText) findViewById(C0585R.id.etMessage);
        this.btnYes = (Button) findViewById(C0585R.id.btnYes);
        this.btnNo = (Button) findViewById(C0585R.id.btnNo);
        this.btnSend = (Button) findViewById(C0585R.id.btnSend);
        this.etMessage.addTextChangedListener(new C05821());
        this.btnYes.setOnClickListener(this);
        this.btnNo.setOnClickListener(this);
        this.btnSend.setOnClickListener(this);
        this.mSessionManager = new SessionManager(getApplicationContext());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btnYes:
                this.referral = "y";
                this.btnYes.setTextColor(getResources().getColor(C0585R.color.white));
                this.btnNo.setTextColor(getResources().getColor(C0585R.color.secondary_text));
                this.btnYes.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_green_selected));
                this.btnNo.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_grey_border));
                return;
            case C0585R.id.btnNo:
                this.referral = "n";
                this.btnNo.setTextColor(getResources().getColor(C0585R.color.white));
                this.btnYes.setTextColor(getResources().getColor(C0585R.color.secondary_text));
                this.btnNo.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_green_selected));
                this.btnYes.setBackgroundDrawable(getResources().getDrawable(C0585R.drawable.btn_10dp_grey_border));
                return;
            case C0585R.id.btnSend:
                checkFormData(view);
                return;
            default:
                return;
        }
    }

    private void checkFormData(View view) {
        this.etMessage.setError(null);
        String msg = this.etMessage.getText().toString().trim();
        if (msg.length() <= 0) {
            this.etMessage.setError(getString(C0585R.string.error_field_required));
        } else if (this.referral.equals("")) {
            Snackbar.make(view, (CharSequence) "Please select referral!", -1).show();
        } else {
            sendMessage((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), this.receiverID, msg, "t", "p");
        }
    }

    private void sendMessage(String senderID, String receiverID, String msg, String receiverType, String senderType) {
        HttpStack stack;
        GeneralSecurityException e;
        String str = EndPoints.SEND_CHAT_ROOM_MESSAGE;
        final String str2 = msg;
        Listener c09762 = new Listener<String>() {
            public void onResponse(String response) {
                Log.e(NewMessageActivity.this.TAG, "response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("true")) {
                        Toast.makeText(NewMessageActivity.this.getApplicationContext(), object.getString("message"), 1).show();
                        NewMessageActivity.this.finish();
                        return;
                    }
                    Toast.makeText(NewMessageActivity.this.getApplicationContext(), object.getString("message"), 1).show();
                } catch (JSONException e) {
                    NewMessageActivity.this.etMessage.setText(str2);
                    Log.e(NewMessageActivity.this.TAG, "json parsing error: " + e.getMessage());
                }
            }
        };
        str2 = msg;
        final String str3 = senderID;
        final String str4 = receiverID;
        final String str5 = senderType;
        final String str6 = receiverType;
        final String str7 = msg;
        Request strReq = new StringRequest(1, str, c09762, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(NewMessageActivity.this.TAG, "" + error.getMessage() + ", code: " + error.networkResponse);
                Toast.makeText(NewMessageActivity.this.getApplicationContext(), "Volley error: " + error.getMessage(), 0).show();
                NewMessageActivity.this.etMessage.setText(str2);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("sender_id", str3);
                params.put("receiver_id", str4);
                params.put("sender_type", str5);
                params.put("receiver_type", str6);
                params.put("message", str7);
                Log.e(NewMessageActivity.this.TAG, "Params: " + params.toString());
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
