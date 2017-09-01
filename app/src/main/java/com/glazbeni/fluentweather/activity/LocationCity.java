package com.glazbeni.fluentweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.util.MyCardAdapter;

/**
 * Created by 29044 on 17.8.30.
 */

public class LocationCity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private MyCardAdapter cardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_city);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tb_location_city);
        searchView = (SearchView) findViewById(R.id.sc_location_city);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
