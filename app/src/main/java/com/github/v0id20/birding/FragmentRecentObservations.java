package com.github.v0id20.birding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentRecentObservations extends Fragment implements IDisplayData {
    ObservationPresenter observationPresenter;
    private ObservationAdapter observationAdapter;
    private RecyclerView recyclerView;
    private String observationsType;
    private View loader;

    public FragmentRecentObservations() {
        super(R.layout.fragment_recent_obsevations);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observationsType = ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT;
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        observationAdapter = new ObservationAdapter(new ArrayList<>());
        observationPresenter = new ObservationPresenter(FragmentRecentObservations.this);
        observationAdapter.setOnObservationClickListener(observationPresenter);

        loader = view.findViewById(R.id.loadingPanel);
        loader.setVisibility(View.VISIBLE);

        TextView header = view.findViewById(R.id.header);
        String regionCode = getArguments().getString(BirdObservation.REGION_CODE_EXTRA);
        String countryName = getArguments().getString(BirdObservation.COUNTRY_NAME_EXTRA);
        double currentLatitude = getArguments().getDouble(BirdObservation.LATITUDE_EXTRA);
        double currentLongitude = getArguments().getDouble(BirdObservation.LONGITUDE_EXTRA);
        if (regionCode != null) {
            header.setText(getString(R.string.recent_obs) + getString(R.string.in) + countryName);
        } else if (currentLatitude != -1 && currentLongitude != -1) {
            header.setText(getString(R.string.recent_obs) + getString(R.string.nearby));
        }
        observationPresenter.getData(observationPresenter, regionCode, currentLatitude, currentLongitude, observationsType);
    }


    public void updateLists(ArrayList<BirdObservation> birdObservationsData) {
        observationAdapter.updateData(birdObservationsData);
        recyclerView.setAdapter(observationAdapter);
    }

    public void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void displayData(ArrayList<BirdObservation> arrayList) {
        hideLoader();
        updateLists(arrayList);
    }

    @Override
    public void onItemClick(BirdObservation birdObservation) {
        Intent i = new Intent(getContext(), ViewBirdObservationActivity.class);
        i.putExtra(BirdObservation.COMMON_NAME_EXTRA, birdObservation.getCommonName());
        i.putExtra(BirdObservation.SCIENTIFIC_NAME_EXTRA, birdObservation.getScientificName());
        i.putExtra(BirdObservation.COUNTRY_NAME_EXTRA, birdObservation.getCountryName());
        i.putExtra(BirdObservation.LOCATION_NAME_EXTRA, birdObservation.getLocationName());
        i.putExtra(BirdObservation.LATITUDE_EXTRA, birdObservation.getLatitude());
        i.putExtra(BirdObservation.LONGITUDE_EXTRA, birdObservation.getLongitude());
        i.putExtra(BirdObservation.OBSERVATION_DATE_EXTRA, birdObservation.getDate());
        this.startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("kek", "eto destroyy view nahui");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("kek", "eto destroyy nahui");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("kek", "eto resume nahui");
    }
}
