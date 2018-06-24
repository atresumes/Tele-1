package com.talktoangel.gts.therapist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.fragment.MessagesFragment;
import com.talktoangel.gts.utils.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistDashboardFragment extends Fragment implements OnClickListener {
    Button btnAvailability;
    Button btnCalendar;
    Button btnMessage;
    Button btnPatient;
    Button btnProfile;
    Button btnProvider;
    ProgressBar mProgressBar;
    TextView tv_badge;
    TextView txt_provider;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(C0585R.layout.fragment_provider_dashboard, container, false);
            this.mProgressBar = (ProgressBar) this.view.findViewById(C0585R.id.progress_pd);
            CircleImageView imageView = (CircleImageView) this.view.findViewById(C0585R.id.img_dash);
            TextView txt_name = (TextView) this.view.findViewById(C0585R.id.txt_name_dash);
            this.txt_provider = (TextView) this.view.findViewById(C0585R.id.txt_provider);
            this.tv_badge = (TextView) this.view.findViewById(C0585R.id.tv_badge);
            this.btnProvider = (Button) this.view.findViewById(C0585R.id.btn_appointment);
            this.btnProfile = (Button) this.view.findViewById(C0585R.id.btn_profile_pd);
            this.btnMessage = (Button) this.view.findViewById(C0585R.id.btn_message_pd);
            this.btnCalendar = (Button) this.view.findViewById(C0585R.id.btn_calendar_pd);
            this.btnAvailability = (Button) this.view.findViewById(C0585R.id.btn_availability_pd);
            this.btnPatient = (Button) this.view.findViewById(C0585R.id.btn_patient_pd);
            SessionManager manager = new SessionManager(getContext());
            txt_name.setText(((String) manager.getUser().get(SessionManager.KEY_FIRST_NAME)).concat(" " + ((String) manager.getUser().get(SessionManager.KEY_LAST_NAME))));
            Glide.with(getContext()).load((String) manager.getUser().get(SessionManager.KEY_IMAGE)).into(imageView);
            this.btnProvider.setOnClickListener(this);
            this.btnProfile.setOnClickListener(this);
            this.btnMessage.setOnClickListener(this);
            this.btnCalendar.setOnClickListener(this);
            this.btnAvailability.setOnClickListener(this);
            this.btnPatient.setOnClickListener(this);
        }
        return this.view;
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_dashboard));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(0).setChecked(true);
        SessionManager session = new SessionManager(getContext());
        if (session.getNotificationCount() == 0) {
            this.tv_badge.setText("");
        } else {
            this.tv_badge.setText("" + session.getNotificationCount());
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_profile_pd:
                startActivity(new Intent(getContext(), TherapistProfileActivity.class));
                return;
            case C0585R.id.btn_message_pd:
                getActivity().getSupportFragmentManager().beginTransaction().replace(C0585R.id.container, new MessagesFragment()).addToBackStack(null).commit();
                return;
            case C0585R.id.btn_calendar_pd:
                startActivity(new Intent(getContext(), CalendarActivity.class));
                return;
            case C0585R.id.btn_patient_pd:
                startActivity(new Intent(getContext(), PatientListActivity.class));
                return;
            case C0585R.id.btn_availability_pd:
                startActivity(new Intent(getContext(), AvailabilityActivity.class));
                return;
            default:
                return;
        }
    }
}
