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
import com.payu.custombrowser.util.CBConstant;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.WebViewActivity;

public class AboutusFragment extends Fragment implements OnClickListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_aboutus, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnContact = (Button) view.findViewById(C0585R.id.btn_contact_us);
        Button btnHowWork = (Button) view.findViewById(C0585R.id.btn_how_work);
        ((Button) view.findViewById(C0585R.id.btn_about_us)).setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnHowWork.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(C0585R.string.about_us));
        ((NavigationView) getActivity().findViewById(C0585R.id.nav_view)).getMenu().getItem(7).setChecked(true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btn_about_us:
                startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra(CBConstant.URL, "http://talktoangel.com/about-us"));
                return;
            case C0585R.id.btn_contact_us:
                startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra(CBConstant.URL, "http://talktoangel.com/contact_us"));
                return;
            case C0585R.id.btn_how_work:
                startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra(CBConstant.URL, "http://talktoangel.com/howit_work"));
                return;
            default:
                return;
        }
    }
}
