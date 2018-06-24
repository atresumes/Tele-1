package com.talktoangel.gts.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.talktoangel.gts.C0585R;
import com.talktoangel.gts.listener.YourFragmentInterface;
import com.talktoangel.gts.model.CareerTest;
import com.talktoangel.gts.utils.SessionManager;

public class Class10Fragment extends Fragment implements OnClickListener, YourFragmentInterface {
    private RadioButton radioCouns;
    private RadioButton radioNoCouns;
    private SessionManager session;
    private TextView tv_description;
    private TextView tv_title;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0585R.layout.fragment_class10, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.tv_title = (TextView) view.findViewById(C0585R.id.tv_title);
        this.tv_description = (TextView) view.findViewById(C0585R.id.tv_description);
        this.radioCouns = (RadioButton) view.findViewById(C0585R.id.radioCouns);
        this.radioNoCouns = (RadioButton) view.findViewById(C0585R.id.radioNoCouns);
        this.session = new SessionManager(getContext());
        Button btnLogin = (Button) view.findViewById(C0585R.id.btnLogin);
        ((Button) view.findViewById(C0585R.id.btnReport)).setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0585R.id.btnLogin:
                if (this.radioCouns.isChecked()) {
                    this.session.setTestData(((CareerTest) Class6Fragment.list.get(1)).getTitle(), ((CareerTest) Class6Fragment.list.get(1)).getWithout_counseling());
                    startActivity(new Intent(getActivity(), TestLoginActivity.class));
                    return;
                } else if (this.radioNoCouns.isChecked()) {
                    this.session.setTestData(((CareerTest) Class6Fragment.list.get(1)).getTitle(), ((CareerTest) Class6Fragment.list.get(1)).getWith_counseling());
                    startActivity(new Intent(getActivity(), TestLoginActivity.class));
                    return;
                } else {
                    Snackbar.make(v, (CharSequence) "Please select any Test", 0).show();
                    return;
                }
            case C0585R.id.btnReport:
                if (Class6Fragment.list.size() > 0) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((CareerTest) Class6Fragment.list.get(1)).getPdf_url())));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void fragmentBecameVisible() {
        if (Class6Fragment.list.size() > 0) {
            this.tv_title.setText(((CareerTest) Class6Fragment.list.get(1)).getTitle());
            this.tv_description.setText(((CareerTest) Class6Fragment.list.get(1)).getDescription());
            this.radioCouns.setText(((CareerTest) Class6Fragment.list.get(1)).getWith_counseling());
            this.radioNoCouns.setText(((CareerTest) Class6Fragment.list.get(1)).getWithout_counseling());
        }
    }
}
