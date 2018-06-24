package com.talktoangel.gts.therapist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.payUMoney.sdk.SdkConstants;
import com.talktoangel.gts.C0585R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageListActivity extends AppCompatActivity {
    Adapter adapter;
    ArrayList<String> list;
    ListView listView;
    ProgressBar progressBar;

    class Adapter extends ArrayAdapter {
        Context context;

        public Adapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.context = context;
        }

        public int getCount() {
            return LanguageListActivity.this.list.size();
        }

        public Object getItem(int position) {
            return LanguageListActivity.this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(LanguageListActivity.this).inflate(17367056, null);
            CheckedTextView tv = (CheckedTextView) v.findViewById(16908308);
            tv.setTextColor(LanguageListActivity.this.getColor(C0585R.color.primary_text));
            tv.setText((CharSequence) LanguageListActivity.this.list.get(position));
            return v;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_language_list);
        this.listView = (ListView) findViewById(C0585R.id.listView);
        this.progressBar = (ProgressBar) findViewById(C0585R.id.progress_speciality);
        this.list = new ArrayList();
        List<String> list = Arrays.asList(getResources().getStringArray(C0585R.array.language_array));
        for (int i = 0; i < list.size(); i++) {
            this.list.add(list.get(i));
        }
        this.adapter = new Adapter(getApplicationContext(), 17367056);
        this.listView.setAdapter(this.adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0585R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != C0585R.id.action_done) {
            return super.onOptionsItemSelected(item);
        } else {
            String s = "";
            SparseBooleanArray clickedItemPositions = this.listView.getCheckedItemPositions();
            for (int index = 0; index < clickedItemPositions.size(); index++) {
                if (clickedItemPositions.valueAt(index)) {
                    int key = clickedItemPositions.keyAt(index);
                    if (index == 0) {
                        s = (String) this.list.get(key);
                    } else {
                        s = s + ", " + ((String) this.list.get(key));
                    }
                    Log.e("SpecialityActivity", s + ", ");
                }
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SdkConstants.RESULT, 1);
            returnIntent.putExtra(SdkConstants.DATA, s);
            setResult(-1, returnIntent);
            onBackPressed();
            return true;
        }
    }
}
