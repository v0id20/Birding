package com.github.v0id20.birding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity {
    public static ArrayList<LocationViewType> locationList;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationPresenter presenter;
    private LocationModel locationModel;
    private  ConstraintLayout cl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_activity);
        presenter = new LocationPresenter();
        presenter.setContext(this);
        locationModel = new LocationModel();
        locationList = locationModel.getLocationList();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cl = findViewById(R.id.constraint_layout);
        LocationAdapter locationAdapter = new LocationAdapter(locationList, this);
        recyclerView.setAdapter(locationAdapter);
        locationAdapter.setOnLocationClickListener(presenter);
        locationAdapter.setOnChosenLocationClickListener(presenter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionResult(requestCode, grantResults);
    }

    public void showSnack(int stringResourceId, int duration) {
        Snackbar snackbar = Snackbar
                .make(cl, stringResourceId, duration);
        snackbar.show();
    }



}
