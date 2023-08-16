package com.github.v0id20.birding.chooselocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.BirdObservation;
import com.github.v0id20.birding.mylocation.LocationCountry;
import com.github.v0id20.birding.mylocation.LocationRegion;
import com.github.v0id20.birding.R;
import com.github.v0id20.birding.viewobservationslist.ViewObservationsListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity implements ChooseLocationView {
    public static final int REQUEST_CODE = 231;
    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private CircularProgressIndicator loader;
    private ImageView errorIcon;
    private TextView errorText;
    private TextView tryAgain;
    private ActivityResultLauncher<Intent> launcher;
    private ChooseLocationAdapter chooseLocationAdapter;
    private ChooseLocationPresenter chooseLocationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        loader = findViewById(R.id.loader);
        errorIcon = findViewById(R.id.error_icon);
        errorText = findViewById(R.id.error_text);
        tryAgain = findViewById(R.id.try_again);
        constraintLayout = findViewById(R.id.constraint_layout);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(this);
        chooseLocationPresenter = new ChooseLocationPresenter(this, flpc);
        chooseLocationPresenter.requestLocationData();

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        chooseLocationPresenter.onRequestPermissionResult(requestCode, grantResults);
    }

    private void showSnack(int stringResourceId, int duration) {
        Snackbar snackbar = Snackbar.make(constraintLayout, stringResourceId, duration);
        snackbar.show();
    }

    private void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void requestLocationPermission() {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void displayCountriesListReceived(ArrayList<LocationCountry> locationDataArrayList) {
        hideLoader();
        chooseLocationAdapter = new ChooseLocationAdapter();
        chooseLocationAdapter.setColors(getResources().getColor(R.color.cyan_300), getResources().getColor(R.color.black));
        chooseLocationAdapter.setCountryArrayList(locationDataArrayList);
        recyclerView.setAdapter(chooseLocationAdapter);
        chooseLocationAdapter.setOnMyLocationClickListener(chooseLocationPresenter);
        chooseLocationAdapter.setOnChosenLocationClickListener(chooseLocationPresenter);
        chooseLocationAdapter.setOnCountryClickListener(chooseLocationPresenter);
    }

    @Override
    public void displayRegionListReceived(ArrayList<LocationRegion> locationList, int position) {
        chooseLocationAdapter.bind(locationList, position);
    }

    @Override
    public void onChosenLocationClick(LocationRegion region) {
        Intent i = new Intent(this, ViewObservationsListActivity.class);
        i.putExtra(BirdObservation.REGION_CODE_EXTRA, region.getLocationCode());
        i.putExtra(BirdObservation.REGION_NAME_EXTRA, region.getLocationName());
        i.putExtra(BirdObservation.COUNTRY_NAME_EXTRA, region.getCountry().getLocationName());
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


    @Override
    public boolean checkLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
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
                showSnack(R.string.error_cant_retrieve_obs_without_location_on, 5000);
            }
        });
        alertDialog.show();
    }


    @Override
    public void showLocationDisabledToast() {
        showSnack(R.string.location_access_disabled_in_settings, 5000);
    }

    @Override
    public void showUnableToGetLocationToast() {
        showSnack(R.string.location_access_disabled_in_settings, 5000);
    }

    @Override
    public void setCountriesLoadingErrorState() {
        loader.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        errorIcon.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocationPresenter.retryLocationDataRequest();
            }
        });
    }

    @Override
    public void setLoadingState() {
        loader.setVisibility(View.VISIBLE);
        errorIcon.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        tryAgain.setVisibility(View.GONE);
    }

    @Override
    public void setRegionsLoadingErrorState(int position) {
        chooseLocationAdapter.setViewHolderErrorState(position);
        Snackbar s = Snackbar.make(constraintLayout, R.string.could_not_load_regions_for_this_country_try_again, 7000);
        s.setAction(R.string.try_again, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocationAdapter.refreshRegionList(position);
                s.dismiss();
            }
        });
        s.show();
    }

}
