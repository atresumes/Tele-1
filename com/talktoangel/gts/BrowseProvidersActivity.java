package com.talktoangel.gts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BrowseProvidersActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0585R.layout.activity_browse_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(C0585R.id.recycler_bp);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
