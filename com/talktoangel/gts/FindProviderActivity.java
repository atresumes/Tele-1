package com.talktoangel.gts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FindProviderActivity extends AppCompatActivity {
    EditText etCityCode;

    class C05791 implements OnEditorActionListener {
        C05791() {
        }

        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id != C0585R.id.code && id != 0) {
                return false;
            }
            FindProviderActivity.this.checkFeildData();
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_find_provider);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.etCityCode = (EditText) findViewById(C0585R.id.et_city_code);
        this.etCityCode.setOnEditorActionListener(new C05791());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    private void checkFeildData() {
        if (this.etCityCode.getText().toString().trim().isEmpty()) {
            this.etCityCode.setError(getResources().getString(C0585R.string.error_field_required));
        }
    }
}
