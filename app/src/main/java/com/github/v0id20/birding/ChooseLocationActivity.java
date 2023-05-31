package com.github.v0id20.birding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity {
    public static ArrayList<LocationViewType> locationList;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationPresenter locationPresenter;
    private LocationModel locationModel;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        locationPresenter = new LocationPresenter();
        locationPresenter.setContext(this);
        locationModel = new LocationModel();
        locationList = locationModel.getLocationList();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        constraintLayout = findViewById(R.id.constraint_layout);
        // removed activity parameter from constructor
        LocationAdapter locationAdapter = new LocationAdapter(locationList);
        recyclerView.setAdapter(locationAdapter);
        locationAdapter.setOnMyLocationClickListener(locationPresenter);
        locationAdapter.setOnChosenLocationClickListener(locationPresenter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPresenter.onRequestPermissionResult(requestCode, grantResults);
    }

    public void showSnack(int stringResourceId, int duration) {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, stringResourceId, duration);
        snackbar.show();
    }



}
