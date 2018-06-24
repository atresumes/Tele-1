package com.talktoangel.gts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.MainActivity;
import com.talktoangel.gts.controller.Controller;
import com.talktoangel.gts.fragment.AppointmentsFragment;
import com.talktoangel.gts.fragment.MessagesFragment;
import com.talktoangel.gts.model.AppointmentItem;
import com.talktoangel.gts.schedule.EditAppointmentActivity;
import com.talktoangel.gts.sinch.CallScreenActivity;
import com.talktoangel.gts.sinch.SinchService;
import com.talktoangel.gts.utils.EndPoints;
import com.talktoangel.gts.utils.SessionManager;
import com.talktoangel.gts.utils.TLSSocketFactory;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AppointmentAdapter extends Adapter<ViewHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<AppointmentItem> list;
    private ProgressBar mProgressBar;
    private SessionManager manager;

    class C05871 implements OnClickListener {
        C05871() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C09813 implements Listener<String> {
        C09813() {
        }

        public void onResponse(String response) {
            AppointmentAdapter.this.mProgressBar.setVisibility(8);
            Log.e("Response: ", response);
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.getString("status").equals("true")) {
                    Toast.makeText(AppointmentAdapter.this.context, jObj.getString("message"), 1).show();
                    new AppointmentsFragment().onResume();
                    return;
                }
                Toast.makeText(AppointmentAdapter.this.context, jObj.getString("message"), 1).show();
            } catch (JSONException e) {
                AppointmentAdapter.this.mProgressBar.setVisibility(8);
                e.printStackTrace();
                Toast.makeText(AppointmentAdapter.this.context, "error" + e.getMessage(), 1).show();
            }
        }
    }

    class C09824 implements ErrorListener {
        C09824() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", "" + error.getMessage());
            AppointmentAdapter.this.mProgressBar.setVisibility(8);
        }
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnType;
        Button cancel;
        Button edit;
        ImageView imageView;
        TextView txtDate;
        TextView txtName;
        TextView txtStatus;
        TextView txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(C0585R.id.img_ia);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txt_name_ia);
            this.txtStatus = (TextView) itemView.findViewById(C0585R.id.txt_apo_status);
            this.txtDate = (TextView) itemView.findViewById(C0585R.id.txt_date_ia);
            this.txtTime = (TextView) itemView.findViewById(C0585R.id.txt_time_ia);
            this.btnType = (Button) itemView.findViewById(C0585R.id.btn_type_ia);
            this.cancel = (Button) itemView.findViewById(C0585R.id.btn_cancel_ia);
            this.edit = (Button) itemView.findViewById(C0585R.id.btn_edit_ia);
            this.cancel.setOnClickListener(this);
            this.edit.setOnClickListener(this);
        }

        public void onClick(View view) {
            AppointmentItem item = (AppointmentItem) AppointmentAdapter.this.list.get(getAdapterPosition());
            String id = item.getApoID();
            String userId = item.getUserId();
            String name = item.getName();
            String img = item.getImage();
            String date = item.getApoDate();
            String time = item.getApoTime();
            String mobile = item.getMobile();
            String type = item.getApoType();
            switch (view.getId()) {
                case C0585R.id.btn_type_ia:
                    if (((MainActivity) AppointmentAdapter.this.fragment.getActivity()).getSinchServiceInterface().isStarted()) {
                        AppointmentAdapter.this.callButtonClicked(type, mobile);
                        return;
                    } else {
                        ((MainActivity) AppointmentAdapter.this.fragment.getActivity()).getSinchServiceInterface().startClient((String) AppointmentAdapter.this.manager.getUser().get("mobile"));
                        return;
                    }
                case C0585R.id.btn_cancel_ia:
                    AppointmentAdapter.this.showCancelDialog(id);
                    return;
                case C0585R.id.btn_edit_ia:
                    Bundle bundle = new Bundle();
                    bundle.putString("apo_id", id);
                    bundle.putString(SessionManager.KEY_ID, userId);
                    bundle.putString("name", name);
                    bundle.putString("image", img);
                    bundle.putString("date", date);
                    bundle.putString("time", time);
                    bundle.putString("mobile", mobile);
                    view.getContext().startActivity(new Intent(AppointmentAdapter.this.context, EditAppointmentActivity.class).putExtras(bundle));
                    return;
                default:
                    return;
            }
        }
    }

    public AppointmentAdapter(Context context, ArrayList<AppointmentItem> imageList, ProgressBar mProgressBar, Fragment fragment) {
        this.context = context;
        this.list = imageList;
        this.mProgressBar = mProgressBar;
        this.fragment = fragment;
        this.manager = new SessionManager(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_appointment, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentItem item = (AppointmentItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtStatus.setText(this.context.getResources().getString(C0585R.string.new_appointment_proposed).concat(" you"));
        holder.txtDate.setText(item.getApoDate());
        holder.txtTime.setText(item.getApoTime());
        if (item.getApoStatus().equalsIgnoreCase("p")) {
            holder.txtStatus.setText(this.context.getResources().getString(C0585R.string.new_appointment_proposed).concat(" " + item.getName()));
            if (item.getApoType().equalsIgnoreCase("2")) {
                holder.btnType.setText("Voice Calling");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_call_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("1")) {
                holder.btnType.setText("Video Calling");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_videocam_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("3")) {
                holder.btnType.setText("Chat");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_message_green_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("4")) {
                holder.btnType.setText("Email");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_email_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else {
                holder.btnType.setText("Face to Face");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_schedule_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            }
        } else if (item.getApoStatus().equalsIgnoreCase("a")) {
            holder.txtStatus.setText("Appointment Confirmed");
            if (item.getApoType().equalsIgnoreCase("2")) {
                holder.btnType.setText("Voice Calling");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_call_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("1")) {
                holder.btnType.setText("Video Calling");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_videocam_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("3")) {
                holder.btnType.setText("Chat");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_message_green_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else if (item.getApoType().equalsIgnoreCase("4")) {
                holder.btnType.setText("Email");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_email_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            } else {
                holder.btnType.setText("Face to Face");
                holder.btnType.setCompoundDrawablesWithIntrinsicBounds(C0585R.drawable.ic_schedule_black_24dp, 0, 0, 0);
                holder.btnType.setCompoundDrawablePadding(8);
            }
        } else if (item.getApoStatus().equalsIgnoreCase("c")) {
            holder.txtStatus.setText(this.context.getString(C0585R.string.appointment_cancelled));
        } else {
            holder.txtStatus.setText("Appointment Status");
        }
        Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_user_black_24dp)).into(holder.imageView);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }

    private void showCancelDialog(final String id) {
        Builder builder = new Builder(this.context);
        builder.setMessage((CharSequence) "Are you sure want cancel your Appointment?");
        builder.setNegativeButton((CharSequence) "No", new C05871());
        builder.setPositiveButton((CharSequence) "Yes", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AppointmentAdapter.this.cancelBooking((String) AppointmentAdapter.this.manager.getUser().get(SessionManager.KEY_ID), id);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void cancelBooking(String userID, String apoID) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = apoID;
        Request strReq = new StringRequest(1, EndPoints.CANCEL_APPOINTMENT, new C09813(), new C09824()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put(SessionManager.KEY_ID, str);
                params.put("apo_id", str2);
                Log.e(SdkConstants.PARAMS, params.toString());
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

    private void callButtonClicked(String type, String mobileNo) {
        String callId;
        Intent callScreen;
        if (type.equalsIgnoreCase("2")) {
            callId = ((MainActivity) this.fragment.getActivity()).getSinchServiceInterface().callUser(mobileNo).getCallId();
            callScreen = new Intent(this.context, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            this.context.startActivity(callScreen);
        } else if (type.equalsIgnoreCase("1")) {
            callId = ((MainActivity) this.fragment.getActivity()).getSinchServiceInterface().callUserVideo(mobileNo).getCallId();
            callScreen = new Intent(this.context, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            this.context.startActivity(callScreen);
        } else if (type.equalsIgnoreCase("3")) {
            this.fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new MessagesFragment()).addToBackStack(null).commit();
        } else if (!type.equalsIgnoreCase("4")) {
            if (type.equalsIgnoreCase("5")) {
                Toast.makeText(this.context, "You can directly contact with provider", 0).show();
            } else {
                Toast.makeText(this.context, "Select a valid method", 0).show();
            }
        }
    }
}
