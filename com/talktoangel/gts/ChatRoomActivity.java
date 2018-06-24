package com.talktoangel.gts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.adapter.ChatRoomThreadAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.Message;
import com.talktoangel.gts.utils.Constant;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.NotificationUtils;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoomActivity extends AppCompatActivity {
    private String TAG = ChatRoomActivity.class.getSimpleName();
    private ChatRoomThreadAdapter adapter;
    private EditText etMessage;
    private ArrayList<Message> list;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SessionManager mSessionManager;
    String receiverType;
    private RecyclerView recyclerView;

    class C05701 extends BroadcastReceiver {
        C05701() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("pushmessage")) {
                Log.e("push recieved", "message");
                ChatRoomActivity.this.handlePushNotification(intent);
            } else if (intent.getAction().equals("typing_status")) {
                Log.e("isTyping", "isTyping");
                CharSequence is_typing = intent.getStringExtra("typing");
                String status = intent.getStringExtra("status");
                Log.e("isTyping", "isTyping");
                if (status.length() <= 0) {
                    ChatRoomActivity.this.getSupportActionBar().setSubtitle(null);
                } else if (status.equals(Constant.ZERO)) {
                    ChatRoomActivity.this.getSupportActionBar().setSubtitle(null);
                } else if (status.equals(Constant.ONE)) {
                    ChatRoomActivity.this.getSupportActionBar().setSubtitle(is_typing);
                }
            }
        }
    }

    class C09717 implements Listener<String> {
        C09717() {
        }

        public void onResponse(String response) {
            Log.e(ChatRoomActivity.this.TAG, "" + response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equalsIgnoreCase("true")) {
                    JSONArray commentsObj = obj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < commentsObj.length(); i++) {
                        JSONObject object = (JSONObject) commentsObj.get(i);
                        String userId = object.getString(SessionManager.KEY_ID);
                        String messageTxt = object.getString("message");
                        String createdAt = object.getString("chat_time");
                        Message message = new Message();
                        message.setMessage(messageTxt);
                        message.setCreatedAt(createdAt);
                        message.setUserId(userId);
                        ChatRoomActivity.this.list.add(message);
                    }
                    ChatRoomActivity.this.adapter.notifyDataSetChanged();
                    if (ChatRoomActivity.this.adapter.getItemCount() > 1) {
                        ChatRoomActivity.this.recyclerView.getLayoutManager().scrollToPosition(ChatRoomActivity.this.adapter.getItemCount() - 1);
                        return;
                    }
                    return;
                }
                Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "" + obj.getString("message"), 1).show();
            } catch (JSONException e) {
                Log.e(ChatRoomActivity.this.TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "error" + e.getMessage(), 0).show();
            }
        }
    }

    class C09728 implements ErrorListener {
        C09728() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e(ChatRoomActivity.this.TAG, "Volley error: " + error.getMessage() + ", code: " + error.networkResponse);
            Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "Volley error: " + error.getMessage(), 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_chat_room);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Log.e("Extras", intent.getStringExtra("name") + "");
        final String receiverID = intent.getStringExtra("chat_room_id");
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        this.etMessage = (EditText) findViewById(C0585R.id.message);
        final FloatingActionButton btnSend = (FloatingActionButton) findViewById(C0585R.id.btn_send);
        btnSend.setEnabled(false);
        this.recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.mSessionManager.setTask(receiverID);
        final String userId = (String) this.mSessionManager.getUser().get(SessionManager.KEY_ID);
        if (((String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("p")) {
            this.receiverType = "t";
        } else {
            this.receiverType = "p";
        }
        Log.e(SdkConstants.TOKEN, getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0).getString("regId", null));
        this.list = new ArrayList();
        this.adapter = new ChatRoomThreadAdapter(this, this.list);
        this.recyclerView.setAdapter(this.adapter);
        this.mRegistrationBroadcastReceiver = new C05701();
        this.etMessage.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    btnSend.setCompatElevation(ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT);
                    btnSend.setEnabled(true);
                    ChatRoomActivity.this.updateTypingStatus(Constant.ONE, ChatRoomActivity.this.receiverType, (String) ChatRoomActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
                    return;
                }
                btnSend.setCompatElevation(0.0f);
                btnSend.setEnabled(false);
                ChatRoomActivity.this.updateTypingStatus(Constant.ZERO, ChatRoomActivity.this.receiverType, (String) ChatRoomActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
            }
        });
        btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String message = ChatRoomActivity.this.etMessage.getText().toString().trim();
                if (message.length() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    int y = calendar.get(1);
                    int m = calendar.get(2) + 1;
                    int d = calendar.get(5);
                    int h = calendar.get(11);
                    int mi = calendar.get(12);
                    Log.e("time", ChatRoomActivity.getTimeStamp(y + "-" + m + "-" + d + " " + h + ":" + mi + ":00"));
                    ChatRoomActivity.this.etMessage.setText(null);
                    ChatRoomActivity.this.sendMessage(userId, receiverID, message, ChatRoomActivity.this.receiverType, (String) ChatRoomActivity.this.mSessionManager.getUser().get(SessionManager.KEY_TYPE), new Message(userId, message, y + "-" + m + "-" + d + " " + h + ":" + mi + ":00"));
                }
            }
        });
        fetchChatThread((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), receiverID, this.receiverType, (String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
    }

    protected void onResume() {
        super.onResume();
        this.mSessionManager.setIsBackground(this.mSessionManager.getTask());
        IntentFilter filterRefreshUpdate = new IntentFilter();
        filterRefreshUpdate.addAction("typing_status");
        filterRefreshUpdate.addAction("pushmessage");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver, filterRefreshUpdate);
        NotificationUtils.clearNotifications();
    }

    protected void onPause() {
        updateTypingStatus(Constant.ZERO, this.receiverType, (String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
        this.mSessionManager.setIsBackground("");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");
        if (message != null && chatRoomId != null) {
            this.list.add(message);
            this.adapter.notifyDataSetChanged();
            if (this.adapter.getItemCount() > 1) {
                this.recyclerView.getLayoutManager().smoothScrollToPosition(this.recyclerView, null, this.adapter.getItemCount() - 1);
            }
        }
    }

    private void sendMessage(String senderID, String receiverID, String msg, String receiverType, String senderType, Message message) {
        HttpStack stack;
        GeneralSecurityException e;
        String str = EndPoints.SEND_CHAT_ROOM_MESSAGE;
        final Message message2 = message;
        final String str2 = msg;
        Listener c09694 = new Listener<String>() {
            public void onResponse(String response) {
                Log.e(ChatRoomActivity.this.TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equals("true")) {
                        ChatRoomActivity.this.list.add(message2);
                        ChatRoomActivity.this.adapter.notifyDataSetChanged();
                        if (ChatRoomActivity.this.adapter.getItemCount() > 1) {
                            ChatRoomActivity.this.recyclerView.getLayoutManager().scrollToPosition(ChatRoomActivity.this.adapter.getItemCount() - 1);
                            return;
                        }
                        return;
                    }
                    Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "" + obj.getString("message"), 1).show();
                } catch (JSONException e) {
                    ChatRoomActivity.this.etMessage.setText(str2);
                    Log.e(ChatRoomActivity.this.TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "json parse error: " + e.getMessage(), 0).show();
                }
            }
        };
        final String str3 = msg;
        final String str4 = senderID;
        final String str5 = receiverID;
        final String str6 = senderType;
        final String str7 = receiverType;
        final String str8 = msg;
        Request strReq = new StringRequest(1, str, c09694, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(ChatRoomActivity.this.TAG, "" + error.getMessage() + ", code: " + error.networkResponse);
                Toast.makeText(ChatRoomActivity.this.getApplicationContext(), "Volley error: " + error.getMessage(), 0).show();
                ChatRoomActivity.this.etMessage.setText(str3);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("sender_id", str4);
                params.put("receiver_id", str5);
                params.put("sender_type", str6);
                params.put("receiver_type", str7);
                params.put("message", str8);
                Log.e(ChatRoomActivity.this.TAG, "Params: " + params.toString());
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

    private void fetchChatThread(String userID, String receiver_id, String receiverType, String senderType) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = userID;
        final String str2 = receiver_id;
        final String str3 = senderType;
        final String str4 = receiverType;
        Request strReq = new StringRequest(1, EndPoints.CHAT_ROOM, new C09717(), new C09728()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("sender_id", str);
                params.put("receiver_id", str2);
                params.put("sender_type", str3);
                params.put("receiver_type", str4);
                Log.e(ChatRoomActivity.this.TAG, params.toString());
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

    private void updateTypingStatus(String typing_status, String receiverType, String senderType) {
        HttpStack stack;
        GeneralSecurityException e;
        final String str = typing_status;
        final String str2 = senderType;
        final String str3 = receiverType;
        Request postRequest = new StringRequest(1, EndPoints.UPDATE_TYPING_STATUS, new Listener<String>() {
            public void onResponse(String response) {
                Log.e(ChatRoomActivity.this.TAG, response);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("sender_id", ChatRoomActivity.this.mSessionManager.getUser().get(SessionManager.KEY_ID));
                params.put("receiver_id", ChatRoomActivity.this.mSessionManager.getTask());
                params.put("typing_status", str);
                params.put("sender_type", str2);
                params.put("receiver_type", str3);
                Log.e(ChatRoomActivity.this.TAG, params.toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            stack = new HurlStack(null, new TLSSocketFactory());
        } catch (KeyManagementException e2) {
            e = e2;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(postRequest, stack);
        } catch (NoSuchAlgorithmException e3) {
            e = e3;
            e.printStackTrace();
            Log.e("Your Wrapper Class", "Could not create new stack for TLS v1.2");
            stack = new HurlStack();
            Controller.getInstance().addToRequestQueue(postRequest, stack);
        }
        Controller.getInstance().addToRequestQueue(postRequest, stack);
    }

    private static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = "";
        String today = String.valueOf(Calendar.getInstance().get(5));
        if (today.length() < 2) {
            today = "0" + today;
        }
        try {
            Date date = format.parse(dateStr);
            if (new SimpleDateFormat("dd", Locale.getDefault()).format(date).equals(today)) {
                format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            } else {
                format = new SimpleDateFormat("dd LLL, hh:mm a", Locale.getDefault());
            }
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return timestamp;
        }
    }
}
