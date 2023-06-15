package com.github.v0id20.birding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity {
    private LocationPresenter locationPresenter;
    private ConstraintLayout constraintLayout;
    private LocationAdapter locationAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationPresenter = new LocationPresenter(this);
        locationPresenter.onCreatePresenter();
        constraintLayout = findViewById(R.id.constraint_layout);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPresenter.onRequestPermissionResult(requestCode, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.gps_settings);
        alertDialog.setMessage(R.string.gps_settings_message);
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ChooseLocationActivity.this.startActivity(intent);
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

    public void showSnack(View view, int stringResourceId, int duration) {
        Snackbar snackbar = Snackbar
                .make(view, stringResourceId, duration);
        snackbar.show();
    }

    public void showLocationsList(ArrayList<LocationViewType> arrayList) {
        locationAdapter = new LocationAdapter();
        locationAdapter.setLocationViewTypeArrayList(arrayList);
        recyclerView.setAdapter(locationAdapter);
        locationAdapter.setOnMyLocationClickListener(locationPresenter);
        locationAdapter.setOnChosenLocationClickListener(locationPresenter);
    }
}
