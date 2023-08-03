package com.github.v0id20.birding;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentObservations extends Fragment implements IDisplayDataReceived {
    private ObservationPresenter observationPresenter;
    private ObservationAdapter observationAdapter;
    private RecyclerView recyclerView;
    private String observationsType;
    private View loader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        observationsType = getArguments().getString(ViewObservationsListActivity.FRAGMENT_OBSERVATIONS_TYPE);
        if (observationsType.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            return inflater.inflate(R.layout.fragment_recent_obsevations, container, false);
        } else if (observationsType.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            return inflater.inflate(R.layout.fragment_recent_obsevations, container, false);
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        observationPresenter = new ObservationPresenter(this);
        Decoder decoder = new Decoder(new Geocoder(view.getContext()));
        observationAdapter = new ObservationAdapter(new ArrayList<>(), decoder);

        observationAdapter.setOnObservationClickListener(observationPresenter);
        recyclerView.addItemDecoration(new StickyHeader(recyclerView, observationAdapter));
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        String regionCode = getArguments().getString(BirdObservation.REGION_CODE_EXTRA);
        String countryName = getArguments().getString(BirdObservation.COUNTRY_NAME_EXTRA);
        double currentLatitude = getArguments().getDouble(BirdObservation.LATITUDE_EXTRA);
        double currentLongitude = getArguments().getDouble(BirdObservation.LONGITUDE_EXTRA);
        observationPresenter.getData(observationPresenter, countryName, regionCode, currentLatitude, currentLongitude, observationsType);
    }

    public void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void displayDataReceived(ArrayList<BirdObservationItem> arrayList) {
        hideLoader();
        updateLists(arrayList);
    }

    public void updateLists(ArrayList<BirdObservationItem> birdObservationsData) {
        observationAdapter.updateData(birdObservationsData);
        recyclerView.setAdapter(observationAdapter);
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
        i.putExtra(BirdObservation.OBSERVATION_DATE_EXTRA, birdObservation.getObservationDate());
        i.putExtra(BirdObservation.OBSERVATION_TIME_EXTRA, birdObservation.getTime());
        i.putExtra(BirdObservation.HOW_MANY_EXTRA, birdObservation.getHowMany());
        this.startActivity(i);
    }


}
