package com.github.v0id20.birding;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewBirdObservationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_observation);
        Bundle extra = getIntent().getExtras();
        String commonName = extra.getString(BirdObservation.COMMON_NAME_EXTRA);
        String sciName = extra.getString(BirdObservation.SCIENTIFIC_NAME_EXTRA);
        String locationName = extra.getString(BirdObservation.LOCATION_NAME_EXTRA);
        String time = extra.getString(BirdObservation.OBSERVATION_TIME_EXTRA);
        String date = extra.getString(BirdObservation.OBSERVATION_DATE_EXTRA);
        int howMany = extra.getInt(BirdObservation.HOW_MANY_EXTRA, 1);
        latitude = extra.getString(BirdObservation.LATITUDE_EXTRA);
        longitude = extra.getString(BirdObservation.LONGITUDE_EXTRA);
        TextView commonNameTV = findViewById(R.id.common_name);
        TextView sciNameTV = findViewById(R.id.sci_name);
        TextView locationNameTV = findViewById(R.id.location);
        TextView dateTimeTV = findViewById(R.id.date_time);
        TextView quantityTV = findViewById(R.id.quantity);
        commonNameTV.setText(commonName);
        sciNameTV.setText(sciName);
        locationNameTV.setText(locationName);
        dateTimeTV.setText(time + " " + date);
        quantityTV.setText(String.valueOf(howMany));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (latitude != null && longitude != null) {
            try {
                LatLng observation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                googleMap.addMarker(new MarkerOptions().position(observation).title("Bird here"));
                pointToPosition(observation, googleMap);
            } catch (NumberFormatException e) {
                Log.e("ViewBirdObservationActivity", "onMapReady: could not convert latitude and longitude to double", e);
            }
        }
    }

    private void pointToPosition(LatLng position, GoogleMap googleMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(12).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
