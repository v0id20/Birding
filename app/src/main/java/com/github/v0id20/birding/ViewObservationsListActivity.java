package com.github.v0id20.birding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ViewObservationsListActivity extends AppCompatActivity {
    public static final String OBSERVATIONS_TYPE_RECENT = "recent";
    public static final String OBSERVATIONS_TYPE_NOTABLE = "notable";
    private ViewObservationsListModel viewObservationsListModel;

    private ObservationPresenter observationPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observations_list);
        Intent i = getIntent();
        String regionCode = i.getStringExtra(BirdObservation.REGION_CODE_EXTRA);
        double currentLatitude = i.getDoubleExtra(BirdObservation.LATITUDE_EXTRA, -1);
        double currentLongitude = i.getDoubleExtra(BirdObservation.LONGITUDE_EXTRA, -1);

        String countryName = null;
        if (regionCode != null) {
            countryName = i.getStringExtra(BirdObservation.COUNTRY_NAME_EXTRA);
        }
        Bundle locationData = new Bundle();
        locationData.putString(BirdObservation.REGION_CODE_EXTRA, regionCode);
        locationData.putString(BirdObservation.COUNTRY_NAME_EXTRA, countryName);
        locationData.putDouble(BirdObservation.LATITUDE_EXTRA, currentLatitude);
        locationData.putDouble(BirdObservation.LONGITUDE_EXTRA, currentLongitude);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, FragmentRecentObservations.class, locationData).commit();
        }
    }


}