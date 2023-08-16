package com.github.v0id20.birding.viewobservationslist;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.BirdObservation;
import com.github.v0id20.birding.BirdObservationItem;
import com.github.v0id20.birding.R;
import com.github.v0id20.birding.StickyHeader;
import com.github.v0id20.birding.ViewBirdObservationActivity;
import com.github.v0id20.birding.decoder.Decoder;

import java.util.ArrayList;

public class ObservationsFragment extends Fragment implements IDisplayDataReceived {
    private ObservationPresenter observationPresenter;
    private ObservationAdapter observationAdapter;
    private RecyclerView recyclerView;
    private String observationsType;
    private ImageView noDataIcon;
    private TextView noData;
    private TextView errorMessage;
    private View loader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_obsevations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observationsType = getArguments().getString(ViewObservationsListActivity.FRAGMENT_OBSERVATIONS_TYPE);
        noData = view.findViewById(R.id.no_data);
        noDataIcon = view.findViewById(R.id.no_data_icon);
        errorMessage = view.findViewById(R.id.error_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        observationPresenter = new ObservationPresenter(this);
        Decoder decoder = new Decoder(new Geocoder(view.getContext()));
        observationAdapter = new ObservationAdapter(new ArrayList<>(), decoder);

        observationAdapter.setOnObservationClickListener(observationPresenter);
        recyclerView.addItemDecoration(new StickyHeader(observationAdapter));
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

    @Override
    public void displayNoDataMessage() {
        noData.setVisibility(View.VISIBLE);
        noDataIcon.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    @Override
    public void displayErrorMessage() {
        loader.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
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
