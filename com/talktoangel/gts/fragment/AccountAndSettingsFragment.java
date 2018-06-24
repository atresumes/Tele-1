package com.talktoangel.gts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.settings.AccountDetailsActivity;
import com.talktoangel.gts.settings.BasicInfoActivity;
import com.talktoangel.gts.settings.CardInfoListActivity;
import com.talktoangel.gts.therapist.AvailabilityActivity;
import com.talktoangel.gts.therapist.BankDetailActivity;
import com.talktoangel.gts.therapist.TherapistProfileActivity;
import com.talktoangel.gts.utils.SessionManager;

public class AccountAndSettingsFragment extends Fragment implements OnClickListener {
    SessionManager manager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_account_and_settings, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnBasicInfo = (Button) view.findViewById(C0585R.id.btn_basic_info);
        Button btnCreditCard = (Button) view.findViewById(C0585R.id.btn_credit_card);
        Button btnSettings = (Button) view.findViewById(C0585R.id.btn_settings);
        ((Button) view.findViewById(C0585R.id.btn_ac_detail)).setOnClickListener(this);
        btnBasicInfo.setOnClickListener(this);
        btnCreditCard.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        this.manager = new SessionManager(getContext());
        if (((String) this.manager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
            btnCreditCard.setText(getString(C0585R.string.bank_details));
        } else {
            btnSettings.setVisibility(8);
        }
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.title_activity_account_details));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(5).setChecked(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0585R.id.btn_settings:
                startActivity(new Intent(getActivity(), AvailabilityActivity.class));
                return;
            case C0585R.id.btn_ac_detail:
                startActivity(new Intent(getActivity(), AccountDetailsActivity.class));
                return;
            case C0585R.id.btn_basic_info:
                if (((String) this.manager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                    startActivity(new Intent(getActivity(), TherapistProfileActivity.class));
                    return;
                } else {
                    startActivity(new Intent(getActivity(), BasicInfoActivity.class));
                    return;
                }
            case C0585R.id.btn_credit_card:
                if (((String) this.manager.getUser().get(SessionManager.KEY_TYPE)).equalsIgnoreCase("t")) {
                    startActivity(new Intent(getActivity(), BankDetailActivity.class));
                    return;
                } else {
                    startActivity(new Intent(getActivity(), CardInfoListActivity.class));
                    return;
                }
            default:
                return;
        }
    }
}
