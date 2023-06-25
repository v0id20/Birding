package com.github.v0id20.birding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity implements ChooseLocationView {
    private ChooseLocationPresenter chooseLocationPresenter;
    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    ProgressBar loader;
    private ActivityResultLauncher<Intent> launcher;
    public static final int REQUEST_CODE = 231;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        TextView chooseLocationTV = findViewById(R.id.choose_location_tv);
        chooseLocationTV.setText(R.string.choose_location);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chooseLocationPresenter = new ChooseLocationPresenter(this);
        chooseLocationPresenter.requestLocationData();
        constraintLayout = findViewById(R.id.constraint_layout);
        //Photo by <a href="https://unsplash.com/@brunus?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Bruno Martins</a> on <a href="https://unsplash.com/photos/BYY_rYlZOkI?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
        ActivityResultContracts.StartActivityForResult contract = new ActivityResultContracts.StartActivityForResult();
        launcher = registerForActivityResult(contract, new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (ChooseLocationActivity.this.getLocationManager().isLocationEnabled()) {
                    chooseLocationPresenter.getCurrentLocation(true);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        chooseLocationPresenter.onRequestPermissionResult(requestCode, grantResults);
    }

    public void showEnableGpsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.gps_settings);
        alertDialog.setMessage(R.string.gps_settings_message);
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launcher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showSnack(constraintLayout, R.string.error_cant_retrieve_obs_without_location_on, 5000);
            }
        });
        alertDialog.show();
    }

    private void showSnack(View view, int stringResourceId, int duration) {
        Snackbar snackbar = Snackbar
                .make(view, stringResourceId, duration);
        snackbar.show();
    }

    private void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    public void requestLocationPermission() {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void displayLocationDataReceived(ArrayList<LocationViewType> locationDataArrayList) {
        hideLoader();
        ChooseLocationAdapter chooseLocationAdapter = new ChooseLocationAdapter();
        chooseLocationAdapter.setLocationViewTypeArrayList(locationDataArrayList);
        recyclerView.setAdapter(chooseLocationAdapter);
        chooseLocationAdapter.setOnMyLocationClickListener(chooseLocationPresenter);
        chooseLocationAdapter.setOnChosenLocationClickListener(chooseLocationPresenter);
    }

    @Override
    public void onChosenLocationClick(String locationCode, String countryName) {
        Intent i = new Intent(this, ViewObservationsListActivity.class);
        i.putExtra(BirdObservation.REGION_CODE_EXTRA, locationCode);
        i.putExtra(BirdObservation.COUNTRY_NAME_EXTRA, countryName);
        this.startActivity(i);
    }

    @Override
    public void onMyLocationClick(double lat, double lon) {
        Intent i = new Intent(this, ViewObservationsListActivity.class);
        i.putExtra(BirdObservation.LATITUDE_EXTRA, lat);
        i.putExtra(BirdObservation.LONGITUDE_EXTRA, lon);
        this.startActivity(i);
    }

    @Override
    public LocationManager getLocationManager() {
        return (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return LocationServices.getFusedLocationProviderClient(this);
    }

    public boolean checkLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void showLocationDisabledToast() {
        showSnack(constraintLayout, R.string.location_access_disabled_in_settings, 5000);
    }

    @Override
    public void showUnableToGetLocationToast() {
        showSnack(constraintLayout, R.string.location_access_disabled_in_settings, 5000);
    }
}
