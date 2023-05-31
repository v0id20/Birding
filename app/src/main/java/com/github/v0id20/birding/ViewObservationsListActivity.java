package com.github.v0id20.birding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewObservationsListActivity extends AppCompatActivity {


    private String countryName;
    private double currentLatitude;
    private double currentLongitude;
    private ObservationModel observationModel;
    private ObservationPresenter observationPresenter;
    private View loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observations_list);
        loader = findViewById(R.id.loadingPanel);
        loader.setVisibility(View.VISIBLE);
        Intent i = getIntent();
        String regionCode = i.getStringExtra(BirdObservation.REGION_CODE_EXTRA);
        currentLatitude = i.getDoubleExtra(BirdObservation.LATITUDE_EXTRA, -1);
        currentLongitude = i.getDoubleExtra(BirdObservation.LONGITUDE_EXTRA, -1);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView header = findViewById(R.id.header);

        observationPresenter = new ObservationPresenter(this);
        ObservationAdapter observationAdapter = new ObservationAdapter(new ArrayList<>());
        observationAdapter.setOnObservationClickListener(observationPresenter);
        observationModel = new ObservationModel(regionCode, currentLatitude, currentLongitude, observationAdapter);
        observationModel.setObservationPresenter(observationPresenter);
        observationModel.getData(recyclerView);

        if (regionCode != null) {
            countryName = i.getStringExtra(BirdObservation.COUNTRY_NAME_EXTRA);
            header.setText(getString(R.string.recent_obs) + " in " + countryName);
        } else if (currentLatitude != -1 && currentLongitude != -1) {
            header.setText(getString(R.string.recent_obs) + " " + getString(R.string.nearby));
        }
    }

    public void setLoader(View loader) {
        this.loader = loader;
    }

    public void setLoaderGone() {
        loader.setVisibility(View.GONE);
    }
}