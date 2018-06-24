package com.talktoangel.gts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.adapter.ChatRoomsAdapter;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.model.ChatRoom;
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

public class MessagesFragment extends Fragment {
    private String TAG = MessagesFragment.class.getSimpleName();
    private ChatRoomsAdapter adapter;
    private ArrayList<ChatRoom> list;
    ProgressBar mProgressBar;
    SessionManager mSessionManager;

    class C09911 implements Listener<String> {
        C09911() {
        }

        public void onResponse(String response) {
            Log.e(MessagesFragment.this.TAG, "response: " + response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equals("true")) {
                    JSONArray chatRoomsArray = obj.getJSONArray(SdkConstants.RESULT);
                    for (int i = 0; i < chatRoomsArray.length(); i++) {
                        JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                        ChatRoom room = new ChatRoom();
                        room.setId(chatRoomsObj.getString("receiver_id"));
                        room.setName(chatRoomsObj.getString("name"));
                        room.setLastMessage(chatRoomsObj.getString("message"));
                        room.setPic(chatRoomsObj.getString("pic"));
                        room.setUnreadCount(0);
                        room.setTimestamp(chatRoomsObj.getString("chat_time"));
                        MessagesFragment.this.list.add(room);
                    }
                } else {
                    Toast.makeText(MessagesFragment.this.getContext(), "" + obj.getString("message"), 1).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MessagesFragment.this.mProgressBar.setVisibility(8);
            MessagesFragment.this.adapter.notifyDataSetChanged();
        }
    }

    class C09922 implements ErrorListener {
        C09922() {
        }

        public void onErrorResponse(VolleyError error) {
            MessagesFragment.this.mProgressBar.setVisibility(8);
            error.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_messages, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mProgressBar = (ProgressBar) view.findViewById(C0585R.id.progress_messages);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C0585R.id.recycler_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        this.mSessionManager = new SessionManager(getContext());
        this.list = new ArrayList();
        this.adapter = new ChatRoomsAdapter(getActivity(), this.list);
        recyclerView.setAdapter(this.adapter);
        fetchChatRooms((String) this.mSessionManager.getUser().get(SessionManager.KEY_ID), (String) this.mSessionManager.getUser().get(SessionManager.KEY_TYPE));
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_messages));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(3).setChecked(true);
    }

    public void onDetach() {
        super.onDetach();
        this.mSessionManager.setNotificationCount(0);
    }

    private void fetchChatRooms(String userID, String userType) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = userType;
        Request strReq = new StringRequest(1, EndPoints.CHAT_LIST, new C09911(), new C09922()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_ID, str);
                params.put(SessionManager.KEY_TYPE, str2);
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
