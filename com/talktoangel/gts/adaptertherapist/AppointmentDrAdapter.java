package com.talktoangel.gts.adaptertherapist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AppointmentDrAdapter extends Adapter<ViewHolder> {
    private Context context;
    private Fragment fragment;
    private boolean isBetweenTime;
    private ArrayList<AppointmentItem> list;
    private ProgressBar mProgressBar;
    private SessionManager manager;

    class C05911 implements OnClickListener {
        C05911() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C09844 implements ErrorListener {
        C09844() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", "" + error.getMessage());
            AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
        }
    }

    class C09867 implements ErrorListener {
        C09867() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e("Error: ", "" + error.getMessage());
            AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
        }
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnAccept;
        Button btnCancel;
        Button btnReschedule;
        Button btnType;
        ImageView imageView;
        TextView txtDate;
        TextView txtName;
        TextView txtStatus;
        TextView txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(C0585R.id.img_iat);
            this.txtName = (TextView) itemView.findViewById(C0585R.id.txt_name_iat);
            this.txtStatus = (TextView) itemView.findViewById(C0585R.id.txt_apo_status);
            this.txtDate = (TextView) itemView.findViewById(C0585R.id.txt_date_iat);
            this.txtTime = (TextView) itemView.findViewById(C0585R.id.txt_time_iat);
            this.btnType = (Button) itemView.findViewById(C0585R.id.btn_type_iat);
            this.btnAccept = (Button) itemView.findViewById(C0585R.id.btn_accept_iat);
            this.btnReschedule = (Button) itemView.findViewById(C0585R.id.btn_reschedule_iat);
            this.btnCancel = (Button) itemView.findViewById(C0585R.id.btn_cancel_iat);
            this.btnType.setOnClickListener(this);
            this.btnAccept.setOnClickListener(this);
            this.btnCancel.setOnClickListener(this);
        }

        public void onClick(View view) {
            AppointmentItem item = (AppointmentItem) AppointmentDrAdapter.this.list.get(getAdapterPosition());
            String id = item.getApoID();
            String userId = item.getUserId();
            String name = item.getName();
            String img = item.getImage();
            String date = item.getApoDate();
            String time = item.getApoTime();
            String mobile = item.getMobile();
            String type = item.getApoType();
            switch (view.getId()) {
                case C0585R.id.btn_type_iat:
                    if (item.getApoStatus().equals("a")) {
                        AppointmentDrAdapter.this.checkTime(date, time);
                        if (!AppointmentDrAdapter.this.isBetweenTime) {
                            return;
                        }
                        if (((MainActivity) AppointmentDrAdapter.this.fragment.getActivity()).getSinchServiceInterface().isStarted()) {
                            AppointmentDrAdapter.this.callButtonClicked(type, mobile, id);
                            return;
                        } else {
                            ((MainActivity) AppointmentDrAdapter.this.fragment.getActivity()).getSinchServiceInterface().startClient((String) AppointmentDrAdapter.this.manager.getUser().get("mobile"));
                            return;
                        }
                    }
                    Toast.makeText(AppointmentDrAdapter.this.context, "Please Accept the Appointment!", 1).show();
                    return;
                case C0585R.id.btn_accept_iat:
                    AppointmentDrAdapter.this.acceptBooking((String) AppointmentDrAdapter.this.manager.getUser().get(SessionManager.KEY_ID), id, "a", getAdapterPosition());
                    return;
                case C0585R.id.btn_cancel_iat:
                    AppointmentDrAdapter.this.showCancelDialog(id, getAdapterPosition());
                    return;
                case C0585R.id.btn_reschedule_iat:
                    Bundle bundle = new Bundle();
                    bundle.putString("apo_id", id);
                    bundle.putString(SessionManager.KEY_ID, userId);
                    bundle.putString("name", name);
                    bundle.putString("image", img);
                    bundle.putString("date", date);
                    bundle.putString("time", time);
                    bundle.putString("mobile", mobile);
                    Log.e("Data", item.toString());
                    view.getContext().startActivity(new Intent(AppointmentDrAdapter.this.context, EditAppointmentActivity.class).putExtras(bundle));
                    return;
                default:
                    return;
            }
        }
    }

    public AppointmentDrAdapter(Context context, ArrayList<AppointmentItem> imageList, ProgressBar mProgressBar, Fragment fragment) {
        this.context = context;
        this.list = imageList;
        this.mProgressBar = mProgressBar;
        this.fragment = fragment;
        this.manager = new SessionManager(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0585R.layout.recycler_item_appointment_therapist, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentItem item = (AppointmentItem) this.list.get(position);
        holder.txtName.setText(item.getName());
        holder.txtDate.setText(item.getApoDate());
        holder.txtTime.setText(item.getApoTime());
        holder.btnType.setText(item.getApoType());
        holder.btnType.setAllCaps(false);
        if (item.getApoStatus().equalsIgnoreCase("p")) {
            holder.txtStatus.setText(this.context.getResources().getString(C0585R.string.new_appointment_proposed).concat(" " + item.getName()));
        } else if (item.getApoStatus().equalsIgnoreCase("a")) {
            holder.txtStatus.setText("Appointment Confirmed");
            holder.btnAccept.setVisibility(8);
        }
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
        Glide.with(this.context).load(item.getImage()).error(this.context.getResources().getDrawable(C0585R.drawable.ic_user_black_24dp)).into(holder.imageView);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.list.size();
    }

    private void showCancelDialog(final String id, final int position) {
        Builder builder = new Builder(this.context);
        builder.setMessage((CharSequence) "Are you sure want cancel your Appointment?");
        builder.setNegativeButton((CharSequence) "No", new C05911());
        builder.setPositiveButton((CharSequence) "Yes", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AppointmentDrAdapter.this.cancelBooking((String) AppointmentDrAdapter.this.manager.getUser().get(SessionManager.KEY_ID), id, "c", position);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void acceptBooking(String userID, String apoID, String status, final int position) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = apoID;
        final String str3 = status;
        Request strReq = new StringRequest(1, EndPoints.ACCEPT_REJECT_APPOINTMENT, new Listener<String>() {
            public void onResponse(String response) {
                AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
                Log.e("Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (status.equals("true")) {
                        Toast.makeText(AppointmentDrAdapter.this.context, jObj.getString("message"), 1).show();
                        ((AppointmentItem) AppointmentDrAdapter.this.list.get(position)).setApoStatus(status);
                        AppointmentDrAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    Toast.makeText(AppointmentDrAdapter.this.context, jObj.getString("message"), 1).show();
                } catch (JSONException e) {
                    AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
                    e.printStackTrace();
                    Toast.makeText(AppointmentDrAdapter.this.context, "error" + e.getMessage(), 1).show();
                }
            }
        }, new C09844()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("apo_id", str2);
                params.put("status", str3);
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

    private void cancelBooking(String userID, String apoID, String status, final int position) {
        HttpStack stack;
        GeneralSecurityException e;
        this.mProgressBar.setVisibility(0);
        final String str = userID;
        final String str2 = apoID;
        final String str3 = status;
        Request strReq = new StringRequest(1, EndPoints.ACCEPT_REJECT_APPOINTMENT, new Listener<String>() {
            public void onResponse(String response) {
                AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
                Log.e("Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (status.equals("true")) {
                        Toast.makeText(AppointmentDrAdapter.this.context, jObj.getString("message"), 1).show();
                        ((AppointmentItem) AppointmentDrAdapter.this.list.get(position)).setApoStatus(status);
                        AppointmentDrAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    Toast.makeText(AppointmentDrAdapter.this.context, jObj.getString("message"), 1).show();
                } catch (JSONException e) {
                    AppointmentDrAdapter.this.mProgressBar.setVisibility(8);
                    e.printStackTrace();
                    Toast.makeText(AppointmentDrAdapter.this.context, "error" + e.getMessage(), 1).show();
                }
            }
        }, new C09867()) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("dr_id", str);
                params.put("apo_id", str2);
                params.put("status", str3);
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

    private void callButtonClicked(String type, String mobileNo, String apo_id) {
        String callId;
        Intent callScreen;
        if (type.equalsIgnoreCase("2")) {
            callId = ((MainActivity) this.fragment.getActivity()).getSinchServiceInterface().callUser(mobileNo).getCallId();
            callScreen = new Intent(this.context, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("apo_id", apo_id);
            this.context.startActivity(callScreen);
        } else if (type.equalsIgnoreCase("1")) {
            callId = ((MainActivity) this.fragment.getActivity()).getSinchServiceInterface().callUserVideo(mobileNo).getCallId();
            callScreen = new Intent(this.context, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("apo_id", apo_id);
            this.context.startActivity(callScreen);
        } else if (type.equalsIgnoreCase("3")) {
            this.fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new MessagesFragment()).addToBackStack(null).commit();
        } else if (type.equalsIgnoreCase("4")) {
            sendMail();
        } else if (type.equalsIgnoreCase("5")) {
            Toast.makeText(this.context, "You can directly contact with provider", 0).show();
        } else {
            Toast.makeText(this.context, "Select a valid method", 0).show();
        }
    }

    private void sendMail() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/html");
        ResolveInfo best = null;
        for (ResolveInfo info : this.context.getPackageManager().queryIntentActivities(intent, 0)) {
            if (!info.activityInfo.packageName.endsWith(".gm")) {
                if (info.activityInfo.name.toLowerCase().contains("gmail")) {
                }
            }
            best = info;
        }
        if (best != null) {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        }
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"radesh@gmail.com"});
        intent.putExtra("android.intent.extra.SUBJECT", "Talk to Angel");
        intent.putExtra("android.intent.extra.TEXT", Html.fromHtml("Talk to Angel"));
        this.context.startActivity(intent);
    }

    private void checkTime(String da, String string1) {
        try {
            Date time1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(da + " " + string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            System.out.println(calendar1.getTime());
            Date time2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(da + " " + string1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(12, 29);
            calendar2.add(13, 0);
            calendar2.add(14, 0);
            System.out.println(calendar2.getTime());
            long yourmilliseconds = System.currentTimeMillis();
            Date d = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).parse(new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(new Date(yourmilliseconds)));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            System.out.println(calendar3.getTime());
            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                this.isBetweenTime = true;
                System.out.println(true);
                return;
            }
            this.isBetweenTime = false;
            System.out.println(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
